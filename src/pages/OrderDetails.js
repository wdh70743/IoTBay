import React, { useEffect, useState } from 'react';
import { useLocation, useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../styles/order-page.css';

const OrderDetails = () => {
    const { orderId } = useParams();
    const location = useLocation();
    const navigate = useNavigate();
    const [orderDetails, setOrderDetails] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');
    const [message] = useState(location.state?.message || '');

    useEffect(() => {
        setIsLoading(true);
        axios.get(`http://localhost:8080/api/order/${orderId}`)
            .then(response => {
                setOrderDetails(response.data);
                setIsLoading(false);
            })
            .catch(error => {
                console.error('Loading order details failed:', error);
                setError('Failed to load order details');
                setIsLoading(false);
            });
    }, [orderId]);

    const handleDelete = () => {
        axios.delete(`http://localhost:8080/api/order/${orderId}`)
            .then(() => {
                alert('Order deleted successfully!');
                navigate('/orders');
            })
            .catch(error => {
                alert('Failed to delete order: ' + error.message);
            });
    };

    const formatDate = (dateArray) => {
        if (!Array.isArray(dateArray) || dateArray.length < 6) return 'Invalid Date';
        const [year, month, day, hour, minute, second] = dateArray;
        return new Date(year, month - 1, day, hour, minute, second).toLocaleString();
    };

    const handleProceedToPayment = () => {
        navigate(`/payments`, { state: { orderId: orderDetails.id } });
    };

    return (
        <div className="order-details-container">
            {isLoading ? <p>Loading...</p> : error ? <p>{error}</p> : orderDetails && (
                <div className="order-details">
                    {message && <div className="alert alert-success" role="alert">{message}</div>}
                    <h2>Order Details</h2>
                    <div className="order-details-content">
                        <div className="order-details-item">
                            <strong>Order ID:</strong> {orderDetails.id}
                        </div>
                        <div className="order-details-item">
                            <strong>Status:</strong> {orderDetails.status}
                        </div>
                        <div className="order-details-item">
                            <strong>Created At:</strong> {formatDate(orderDetails.createdAt)}
                        </div>
                        <div className="order-details-item">
                            <strong>User:</strong> {orderDetails.user.email} ({orderDetails.user.firstName} {orderDetails.user.lastName})
                        </div>
                        <div className="order-details-item">
                            <strong>Items:</strong>
                            <ul>
                                {orderDetails.items.map(item => (
                                    <li key={item.id}>
                                        {item.product.name} - Quantity: {item.quantity}, Price: ${item.product.price}, Total Price: ${(item.quantity * item.product.price).toFixed(2)}
                                    </li>
                                ))}
                            </ul>
                        </div>
                    </div>
                    <button onClick={handleDelete} className="btn btn-danger">Delete Order</button>
                    <button onClick={handleProceedToPayment} className="btn btn-primary">Proceed to Payment</button>
                </div>
            )}
        </div>
    );
};

export default OrderDetails;
