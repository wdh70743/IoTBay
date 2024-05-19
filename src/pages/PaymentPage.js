import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../styles/payment-page.css';
import { useNavigate, useParams, useLocation } from 'react-router-dom';

const PaymentPage = () => {
    const [payment, setPayment] = useState({
        orderId: '',
        amount: 0,
        cardLastFourDigits: '',
        paymentMethod: '',
        paymentDate: ''
    });
    const [status, setStatus] = useState('');
    const navigate = useNavigate();
    const { paymentId } = useParams();
    const location = useLocation();

    useEffect(() => {
        if (location.state?.orderId) {
            setPayment(prevPayment => ({
                ...prevPayment,
                orderId: location.state.orderId
            }));

            // Fetch order total amount
            axios.get(`http://localhost:8080/api/order/${location.state.orderId}`)
                .then(response => {
                    const order = response.data;
                    const totalAmount = order.items.reduce((acc, item) => acc + (item.quantity * item.product.price), 0);
                    setPayment(prevPayment => ({
                        ...prevPayment,
                        amount: totalAmount
                    }));
                })
                .catch(error => {
                    console.error('Error fetching order total:', error);
                });
        }

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
    }, [paymentId, location.state]);

    const handleSubmit = async (event) => {
        event.preventDefault();
        const url = paymentId ? `http://localhost:8080/api/payments/${paymentId}` : 'http://localhost:8080/api/payments/create';
        const method = paymentId ? 'put' : 'post';

        if (!payment.paymentDate) {
            payment.paymentDate = new Date().toISOString();
        }

        setStatus('Processing...');
        console.log('Submitting payment:', payment); 

        axios({ method, url, data: payment })
            .then((response) => {
                setStatus('Payment processed successfully!');
                navigate(`/payment-confirmation/${response.data.id}`, { state: { payment: response.data, message: 'Payment created successfully!' } }); // Navigate to PaymentConfirmation
            })
            .catch(error => {
                setStatus(`Error: ${error.response?.data?.message || error.message}`);
                console.error('Submission error:', error.response?.data || error); 
            });
    };

    return (
        <div className="payment-container">
            <form onSubmit={handleSubmit} className="form-payment">
                <h1>{paymentId ? 'Update Payment' : 'Create Payment'}</h1>
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
                <button type="submit" className="btn btn-primary">{paymentId ? 'Update Payment' : 'Create Payment'}</button>
            </form>
        </div>
    );
};

export default PaymentPage;
