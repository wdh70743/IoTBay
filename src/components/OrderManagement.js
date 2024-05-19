import React, { useEffect, useState } from 'react';
import { listOrdersByUser, deleteOrder } from '../services/OrderService';
import { useNavigate } from 'react-router-dom';

function OrderManagement() {
    const [orders, setOrders] = useState([]);
    const navigate = useNavigate();
    const userId = JSON.parse(localStorage.getItem('user')).id;

    useEffect(() => {
        fetchOrders();
    }, []);

    const fetchOrders = async () => {
        try {
            const response = await listOrdersByUser(userId);
            setOrders(response.data);
        } catch (error) {
            console.error('Error fetching orders:', error);
        }
    };

    const handleDelete = async (orderId) => {
        try {
            await deleteOrder(orderId);
            fetchOrders(); 
        } catch (error) {
            console.error('Error deleting order:', error);
        }
    };

    const viewOrderDetails = (orderId) => {
        navigate(`/order-details/${orderId}`);
    };

    return (
        <div>
            <h1>Order Management</h1>
            {orders.map(order => (
                <div key={order.id}>
                    <p>Order ID: {order.id} - Product ID: {order.productId}</p>
                    <button onClick={() => viewOrderDetails(order.id)}>View Details</button>
                    <button onClick={() => handleDelete(order.id)}>Delete Order</button>
                </div>
            ))}
        </div>
    );
}

export default OrderManagement;