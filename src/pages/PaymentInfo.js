import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import paymentService from '../services/PaymentService';
import '../styles/payment-info.css';

const PaymentInfo = () => {
    const { paymentId } = useParams();
    const [payment, setPayment] = useState({
        orderId: '',
        amount: 0,
        cardLastFourDigits: '',
        paymentMethod: '',
        paymentDate: '',
        createdAt: ''
    });

    const formatDate = (dateArray) => {
        if (!Array.isArray(dateArray) || dateArray.length < 6) return 'Invalid Date';
        const [year, month, day, hour, minute, second] = dateArray;
        return new Date(year, month - 1, day, hour, minute, second).toLocaleString();
    };

    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchPayment = async () => {
            try {
                console.log(`Fetching payment with ID: ${paymentId}`); 
                const response = await paymentService.getPaymentById(paymentId);
                console.log('Payment data:', response); 

                if (Array.isArray(response.createdAt)) {
                    response.createdAt = formatDate(response.createdAt);
                }

                if (Array.isArray(response.paymentDate)) {
                    response.paymentDate = formatDate(response.paymentDate);
                }

                setPayment(response);
            } catch (error) {
                console.error('Error fetching payment:', error);
                setErrorMessage('Failed to fetch payment details.');
            }
        };

        fetchPayment();
    }, [paymentId]);

    if (errorMessage) {
        return <div className="error-message">{errorMessage}</div>;
    }

    return (
        <div className="payment-info-container">
            <h1>Payment Details</h1>
            <div className="payment-info">
                <p><strong>Payment ID:</strong> {payment.id}</p>
                <p><strong>Order ID:</strong> {payment.orderId}</p>
                <div className="total-amount">
                    <strong>Total Amount:</strong> ${Number(payment.amount).toFixed(2)}
                </div>
                <p><strong>Card Last Four Digits:</strong> {payment.cardLastFourDigits}</p>
                <p><strong>Payment Method:</strong> {payment.paymentMethod}</p>
                <div className="payment-details-item">
                    <strong>Created At:</strong> {payment.createdAt}
                </div>
            </div>
            <button onClick={() => navigate(-1)} className="btn btn-secondary">Back</button>
        </div>
    );
};

export default PaymentInfo;
