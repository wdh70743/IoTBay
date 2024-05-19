import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../styles/payment-confirmation.css';

const PaymentConfirmation = () => {
    const { paymentId } = useParams();
    const navigate = useNavigate();
    const [payment, setPayment] = useState({
        orderId: '',
        amount: 0,
        cardLastFourDigits: '',
        paymentMethod: '',
        paymentDate: ''
    });
    const [status, setStatus] = useState('');

    useEffect(() => {
        if (paymentId) {
            axios.get(`http://localhost:8080/api/payments/${paymentId}`)
                .then(response => {
                    setPayment(response.data);
                    setStatus('');
                })
                .catch(error => {
                    setStatus('Error fetching payment: ' + error.message);
                    console.error('Fetch error:', error);
                });
        }
    }, [paymentId]);

    const handleUpdate = async (event) => {
        event.preventDefault();
        const url = `http://localhost:8080/api/payments/${paymentId}`;

        setStatus('Processing...');

        axios.put(url, payment)
            .then((response) => {
                setStatus('Payment updated successfully!');
                navigate(`/payments/${response.data.id}`, { state: { message: 'Payment updated successfully!' } });
            })
            .catch(error => {
                setStatus(`Error: ${error.response?.data?.message || error.message}`);
                console.error('Update error:', error.response?.data || error);
            });
    };

    const handleDelete = async () => {
        axios.delete(`http://localhost:8080/api/payments/${paymentId}`)
            .then(() => {
                alert('Payment deleted successfully!');
                navigate('/main');
            })
            .catch(error => alert('Failed to delete payment: ' + error.message));
    };

    return (
        <div className="payment-container">
            <form onSubmit={handleUpdate} className="form-payment">
                <h1>Confirm Payment</h1>
                <div className="order-id">
                    <strong>Order ID:</strong> {payment.orderId}
                </div>
                <div className="total-amount">
                    <strong>Total Amount:</strong> ${Number(payment.amount).toFixed(2)}
                </div>
                <div className="status-message">{status}</div>
                
                <input 
                    type="text" 
                    className="form-control" 
                    value={payment.cardLastFourDigits} 
                    onChange={e => setPayment({ ...payment, cardLastFourDigits: e.target.value })} 
                    placeholder="Last Four Digits of Card" 
                    required 
                />
                <select 
                    className="form-control select-control" 
                    value={payment.paymentMethod} 
                    onChange={e => setPayment({ ...payment, paymentMethod: e.target.value })} 
                    required
                >
                    <option value="">Select Payment Method</option>
                    <option value="credit_card">Credit Card</option>
                    <option value="paypal">PayPal</option>
                    <option value="bank_transfer">Bank Transfer</option>
                </select>
                <button type="submit" className="btn btn-primary">Proceed Payment</button>
                <button type="button" className="btn btn-danger" onClick={handleDelete}>Delete Payment</button>
            </form>
        </div>
    );
};

export default PaymentConfirmation;
