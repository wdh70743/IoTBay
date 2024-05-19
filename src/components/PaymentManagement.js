import React, { useEffect, useState } from 'react';
import { getPaymentsByOrder, deletePayment, searchPaymentsByDate } from '../services/PaymentService';
import { useNavigate } from 'react-router-dom';

function PaymentManagement() {
    const [payments, setPayments] = useState([]);
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const navigate = useNavigate();
    const orderId = "exampleOrderId";  

    useEffect(() => {
        fetchPayments();
    }, []);

    const fetchPayments = async () => {
        try {
            const response = await getPaymentsByOrder(orderId);
            setPayments(response.data);
        } catch (error) {
            console.error('Error fetching payments:', error);
        }
    };

    const handleDelete = async (paymentId) => {
        try {
            await deletePayment(paymentId);
            fetchPayments(); 
        } catch (error) {
            console.error('Error deleting payment:', error);
        }
    };

    const editPayment = (paymentId) => {
        navigate(`/edit-payment/${paymentId}`);
    };

    const handleSearch = async () => {
        try {
            const response = await searchPaymentsByDate(startDate, endDate);
            setPayments(response.data);
        } catch (error) {
            console.error('Error searching payments:', error);
        }
    };

    return (
        <div>
            <h1>Payment Management</h1>
            <div>
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
            {payments.map(payment => (
                <div key={payment.id}>
                    <p>Payment ID: {payment.id} - Order ID: {payment.orderId} - Amount: ${payment.amount}</p>
                    <button onClick={() => editPayment(payment.id)}>Edit</button>
                    <button onClick={() => handleDelete(payment.id)}>Delete</button>
                </div>
            ))}
        </div>
    );
}

export default PaymentManagement;
