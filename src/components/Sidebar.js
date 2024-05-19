import React from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../styles/sidebar.css';

const Sidebar = ({ items, onClose, onRemoveItem, onUpdateItemQuantity }) => {
    const navigate = useNavigate();

    const calculateTotal = () => {
        return items.reduce((total, item) => total + item.price * item.quantity, 0);
    };

    const handleCreateOrder = async () => {
        try {
            const orderData = {
                userId: JSON.parse(localStorage.getItem('user')).id,
                items: items.map(item => ({
                    productId: item.id,
                    quantity: item.quantity
                })),
                status: 'pending'
            };
            const response = await axios.post(`http://localhost:8080/api/order/create`, orderData);
            navigate(`/order-details/${response.data.id}`, { state: { message: 'Order created successfully!' } }); // Navigate to OrderDetails with new order ID
        } catch (error) {
            console.error("Failed to create order:", error);
            alert("Failed to create order: " + (error.response?.data?.message || error.message));
        }
    };

    const handleQuantityChange = (id, e) => {
        const newQuantity = parseInt(e.target.value, 10);
        if (Number.isInteger(newQuantity)) {
            onUpdateItemQuantity(id, newQuantity);
        }
    };

    return (
        <div className="sidebar">
            <button onClick={onClose} className="close-sidebar">Close</button>
            {items.map(item => (
                <div key={item.id} className="sidebar-item">
                    <p>{item.name} - ${item.price}</p>
                    <div className="input-group">
                        <input
                            type="number"
                            min="0"
                            value={item.quantity}
                            onChange={(e) => handleQuantityChange(item.id, e)}
                            className="quantity-input"
                        />
                        <button onClick={() => onRemoveItem(item.id)} className="remove-item">Remove</button>
                    </div>
                </div>
            ))}
            <div className="total-price">Total: ${calculateTotal().toFixed(2)}</div>
            <button className='btn btn-primary' onClick={handleCreateOrder}>Order</button>
        </div>
    );
};

export default Sidebar;
