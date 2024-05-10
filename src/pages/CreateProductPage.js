import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import userService from '../services/UserService';
import productService from '../services/ProductService';

const CreateProductPage = () => {
    const [userId, setUserId] = useState('');
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [type, setType] = useState('');
    const [quantity, setQuantity] = useState('');
    const [price, setPrice] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const user = JSON.parse(localStorage.getItem('user'));
        if (user && user.id) {
            setUserId(user.id);
        } else {
            setErrorMessage("No user data found. Please login again.");
            navigate('/main'); 
        }
    }, [navigate]);

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (!userId) {
            setErrorMessage('User ID is missing, please re-login.');
            return;
        }
        try {
            const product = {
                userId,
                name,
                description,
                type,
                quantity,
                price,
            };

            const response = await productService.create(product);
            if (response.status === 200) {
                navigate('/main');
            }
        } catch (error) {
            setErrorMessage(error.response?.data?.message || 'Registration failed, please try again.');
            console.error(error);
        }
    };

    const backToMain = () => {
        navigate('/main');
    };

    return (
        <div className="register-container">
            <form onSubmit={handleSubmit} className="form-register">
                <h1>Create a product</h1>
                {errorMessage && <div className="alert alert-danger" role="alert">{errorMessage}</div>}
                <input type="text" value={name} onChange={e => setName(e.target.value)} placeholder="Product Name" required />
                <input type="textarea" value={description} onChange={e => setDescription(e.target.value)} placeholder="Description" required />
                <input type="text" value={type} onChange={e => setType(e.target.value)} placeholder="Type" required />
                <input type="number" value={quantity} onChange={e => setQuantity(e.target.value)} placeholder="Quantity" required />
                <input type="number" value={price} onChange={e => setPrice(e.target.value)} placeholder="Price" required />
                <button type="submit" className="btn btn-primary">Create Product</button>
                <button type="button" className="btn btn-secondary" onClick={backToMain}>Back to Main</button>
            </form>
        </div>
    );
}

export default CreateProductPage;