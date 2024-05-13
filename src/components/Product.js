import React, { useEffect, useState } from 'react';
import { useNavigate, useLocation, useParams } from 'react-router-dom';
import productService from '../services/ProductService';
import { bool } from "prop-types";

const Product = ({ editing }) => {
    const navigate = useNavigate();
    const location = useLocation();


    // Initialize product state with defaults to avoid controlled-to-uncontrolled warning
    const [product, setProduct] = useState({
        userId: '',
        name: '',
        description: '',
        type: '',
        quantity: '',
        price: '',
    });
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        const user = JSON.parse(localStorage.getItem('user'));
        if (user && user.id) {
            setProduct(prev => ({ ...prev, userId: user.id }));
        } else {
            setErrorMessage("No user data found. Please login again.");
            navigate('/login');
        }

        if (editing && location.state && location.state.product) {
            setProduct(prev => ({
                ...location.state.product,
                userId: prev.userId // Preserve the userId
            }));
        }
    }, [navigate, location, editing]);


    const handleChange = (e) => {
        const { name, value } = e.target;
        setProduct(prev => {
            const newState = { ...prev, [name]: value };
            return newState;
        });
    };


    const handleSubmit = async (event) => {
        event.preventDefault();
        if (!product.userId) {
            setErrorMessage('User ID is missing, please re-login.');
            return;
        }

        try {
            let response;
            if (editing) {
                response = await productService.update(product.id, product);
            } else {
                response = await productService.create(product);
            }
            if (response.status === 200) {
                navigate('/main');
            } else {
                setErrorMessage(`Failed to ${editing ? 'update' : 'create'} product, server responded with status: ${response.status}`);
            }
        } catch (error) {
            setErrorMessage(error.response?.data?.message || 'Operation failed, please try again.');
            console.error('Error in handleSubmit:', error);
        }
    };

    const handleGoBack = () => {
        navigate('/main');
    }

    const handleDelete = async (event) => {

        try {
            let response;
            response = await productService.delete(product.id, product.userId);
            if (response.status === 200) {
                navigate('/main');
            } else {
                setErrorMessage(`Failed to ${editing ? 'update' : 'create'} product, server responded with status: ${response.status}`);
            }
        } catch (error) {
            setErrorMessage(error.response?.data?.message || 'Operation failed, please try again.');
            console.error('Error in handleSubmit:', error);
        }

    }

    return (
        <div className="register-container">
            <form onSubmit={handleSubmit} className="form-register">
                <h1>{editing ? 'Edit' : 'Create'} a Product</h1>
                {errorMessage && <div className="alert alert-danger" role="alert">{errorMessage}</div>}
                <input type="text" name="name" value={product.name || ''} onChange={handleChange} placeholder="Product Name" required />
                <textarea name="description" value={product.description || ''} onChange={handleChange} placeholder="Description" required />
                <input type="text" name="type" value={product.type || ''} onChange={handleChange} placeholder="Type" required />
                <input type="number" name="quantity" value={product.quantity || ''} onChange={handleChange} placeholder="Quantity" required />
                <input type="number" name="price" value={product.price || ''} onChange={handleChange} placeholder="Price" required />
                <button type="submit" className="btn btn-primary">{editing ? 'Update' : 'Create'} Product</button>
                <button type="Cancel" className="btn btn-secondary" onClick={handleGoBack}>Back</button>
                {editing && (
                            <>
                            <button type="delete" className="btn btn-danger" onClick={handleDelete}>Delete</button>
                            </>
                        )}
                
            </form>
        </div>
    );
}

Product.propTypes = {
    editing: bool.isRequired
};

Product.defaultProps = {
    editing: false
};

export default Product;
