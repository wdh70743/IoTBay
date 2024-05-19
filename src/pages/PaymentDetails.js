import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../styles/payment-page.css';

const PaymentDetails = () => {
    const { paymentId } = useParams();
    const navigate = useNavigate();
    const [payment, setPayment] = useState(null);

    useEffect(() => {
        axios.get(`http://localhost:8080/api/payments/${paymentId}`)
            .then(response => setPayment(response.data))
            .catch(error => console.error('Loading payment details failed:', error));
    }, [paymentId]);

    const handleDelete = () => {
        axios.delete(`http://localhost:8080/api/payments/${paymentId}`)
            .then(() => {
                alert('Payment deleted successfully!');
                navigate('/main');
            })
            .catch(error => alert('Failed to delete payment: ' + error.message));
    };

    const formatDate = (dateArray) => {
        if (!Array.isArray(dateArray) || dateArray.length < 6) return 'Invalid Date';
        const [year, month, day, hour, minute, second] = dateArray;
        return new Date(year, month - 1, day, hour, minute, second).toLocaleString();
    };

    return (
        <div className="payment-details-container">
            {payment ? (
                <div className="payment-details">
                    <h2>Payment Details</h2>
                    <div className="payment-details-content">
                        <div className="payment-details-item">
                            <strong>Payment ID:</strong> {payment.id}
                        </div>
                        <div className="payment-details-item">
                            <strong>Order ID:</strong> {payment.orderId}
                        </div>
                        <div className="payment-details-item">
                            <strong>Card Last Four Digits:</strong> {payment.cardLastFourDigits}
                        </div>
                        <div className="payment-details-item">
                            <strong>Payment Method:</strong> {payment.paymentMethod}
                        </div>
                        <div className="payment-details-item">
                            <strong>Created At:</strong> {formatDate(payment.createdAt)}
                        </div>
                    </div>
                    <button onClick={handleDelete} className="btn btn-danger">Delete</button>
                </div>
            ) : (
                <p>Loading...</p>
            )}
        </div>
    );
};

export default PaymentDetails;
