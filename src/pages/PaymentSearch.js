import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import paymentService from '../services/PaymentService';
import '../styles/payment-search.css';

function PaymentSearch() {
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [payments, setPayments] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    const handleSearch = async () => {
        try {
            const response = await paymentService.searchPaymentsByDate(startDate, endDate);
            setPayments(response);
            setErrorMessage('');
        } catch (error) {
            console.error('Error searching payments:', error);
            setErrorMessage('Failed to search payments. Please check the date range and try again.');
        }
    };

    const handleDelete = async (paymentId) => {
        try {
            await paymentService.deletePayment(paymentId);
            setPayments(payments.filter(payment => payment.id !== paymentId));
            setErrorMessage('');
        } catch (error) {
            console.error('Error deleting payment:', error);
            setErrorMessage('Failed to delete payment.');
        }
    };

    return (
        <div className="payment-search-container">
            <h1 className="payment-search-header">Search Payments</h1>
            <div className="payment-search-form">
                <label>
                    Start Date:
                    <input type="date" value={startDate} onChange={e => setStartDate(e.target.value)} />
                </label>
                <label>
                    End Date:
                    <input type="date" value={endDate} onChange={e => setEndDate(e.target.value)} />
                </label>
                <button onClick={handleSearch}>Search</button>
            </div>
            {errorMessage && <div className="error-message">{errorMessage}</div>}
            {payments.length > 0 && (
                <div className="search-results">
                    <h2>Search Results</h2>
                    {payments.map(payment => (
                        <div key={payment.id} className="payment-item">
                            <p>Payment ID: {payment.id}</p>
                            <div className="btn-container">
                                <button onClick={() => {
                                    console.log(`Navigating to /payment-info/${payment.id}`);
                                    navigate(`/payment-info/${payment.id}`);
                                }} className="btn btn-primary">View Details</button>
                                <button onClick={() => handleDelete(payment.id)} className="btn btn-danger">Delete</button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default PaymentSearch;
