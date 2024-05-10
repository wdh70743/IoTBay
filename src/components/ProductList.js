import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import productService from '../services/ProductService';
import '../styles/device.css';

const ProductList = () => {
    const [devices, setDevices] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        fetchDevices();
    }, []);

    const fetchDevices = async () => {
        try {
            const response = await productService.getProduct();
            setDevices(response.data);
        } catch (error) {
            console.error('Failed to fetch devices:', error);
            navigate('/login');
        }
    };

    const handleSearch = async () => {
        try {
            if (!searchTerm.trim()) {
                fetchDevices();
            } else {
                const response = await productService.searchProducts(searchTerm);
                setDevices(response.data);
            }
        } catch (error) {
            console.error('Search failed:', error);
        }
    };

    const handleSearchChange = (event) => {
        setSearchTerm(event.target.value);
    };

    return (
        <div className="main-container">
            <h1>IoT Device Catalogue</h1>
            <input
                type="text"
                placeholder="Search by name or type"
                value={searchTerm}
                onChange={handleSearchChange}
                className="search-input"
            />
            <button onClick={handleSearch} className="btn btn-primary">Search</button>
            <div className="device-list">
                {devices.length > 0 ? devices.map(device => (
                    <div key={device.id} className="device-item">
                        <p><strong>Name:</strong> {device.name}</p>
                        <p><strong>Type:</strong> {device.type}</p>
                        <p><strong>Price:</strong> ${device.price}</p>
                        <p><strong>Stock:</strong> {device.quantity}</p>
                    </div>
                )) : <p>No devices found.</p>}
            </div>
        </div>
    );
}

export default ProductList;
