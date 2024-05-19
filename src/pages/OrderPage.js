import React, { useState, useEffect } from 'react';
import orderService from '../services/OrderService';
import { useNavigate, useParams } from 'react-router-dom';
import { useCart } from '../contexts/CartContext';

const OrderPage = () => {
    const { cartItems, updateItemQuantity } = useCart();
    const [isLoading, setIsLoading] = useState(false);
    const [message, setMessage] = useState('');
    const navigate = useNavigate();
    const { orderId } = useParams();

    useEffect(() => {
        console.log("Cart Items:", cartItems);
    }, [cartItems]);

    const handleQuantityChange = (productId, newQuantity) => {
        updateItemQuantity(productId, newQuantity);
    };

    const handleCreateOrUpdateOrder = async () => {
        setIsLoading(true);
        setMessage('');

        const userData = JSON.parse(localStorage.getItem('user'));
        if (!userData || !userData.id) {
            navigate('/login');
            return;
        }

        const orderData = {
            userId: userData.id,
            items: cartItems.map(item => ({ productId: item.id, quantity: item.quantity })),
            status: 'pending'
        };

        try {
            if (orderId) {
                await orderService.updateOrder(orderId, orderData);
                setMessage('Order created successfully!');
                navigate(`/order-details/${orderId}`, { state: { message: 'Order created successfully!' } });
            }
        } catch (error) {
            console.error('Failed to process order:', error);
            setMessage(`Error: ${error.message}`);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="order-container">
            <div className="order-form">
                <h1>{orderId ? 'Confirm Your Order' : 'Create Your Order'}</h1>
                {message && <div className={`alert ${message.startsWith('Error') ? 'alert-danger' : 'alert-success'}`} role="alert">{message}</div>}
                <div className="order-items">
                    {cartItems.length > 0 ? cartItems.map((item) => (
                        <div key={item.id} className="order-item">
                            <span>{item.name} - ${item.price}</span>
                            <input 
                                type="number" 
                                value={item.quantity} 
                                onChange={(e) => handleQuantityChange(item.id, parseInt(e.target.value) || 0)} 
                                className="quantity-input"
                            />
                        </div>
                    )) : <p>No items in cart</p>}
                </div>
                <button onClick={handleCreateOrUpdateOrder} disabled={isLoading || !cartItems.length} className="btn btn-primary">
                    {isLoading ? 'Processing...' : (orderId ? 'Confirm Order' : 'Create Order')}
                </button>
            </div>
        </div>
    );
};

export default OrderPage;
