import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import productService from '../services/ProductService';
import '../styles/device.css';

const ProductList = () => {
    const [devices, setDevices] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [searchType, setSearchType] = useState('name'); // Default search type
    const user = JSON.parse(localStorage.getItem('user'));
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
        if (!searchTerm.trim()) {
            fetchDevices();
            return;
        }
        try {
            let response;
            if (searchType === 'name') {
                response = await productService.getProductByName(searchTerm);
            } else {
                response = await productService.getProductByType(searchTerm);
            }
            setDevices(response.data || []);
        } catch (error) {
            console.error('Search failed:', error);
        }
    };

    const handleSearchChange = (event) => {
        setSearchTerm(event.target.value);
    };

    const handleSearchTypeChange = (event) => {
        setSearchType(event.target.value);
    };

    const handleEditButton = (product) => {
        navigate(`/edit/${product.id}`, { state: { product } });
    };
    


    return (
        <div>
            <h1>IoT Device Catalogue</h1>
            <div>
                <select onChange={handleSearchTypeChange} className="search-type-selector">
                    <option value="name">Name</option>
                    <option value="type">Type</option>
                </select>
                <input
                    type="text"
                    placeholder={`Search by ${searchType}`}
                    value={searchTerm}
                    onChange={handleSearchChange}
                    className="search-input"
                />
                <button onClick={handleSearch} className="btn btn-primary">Search</button>
            </div>
            <div className="device-list">
                {devices.map(device => (
                    <div key={device.id} className="device-item">
                        <p><strong>Name:</strong> {device.name}</p>
                        <p><strong>Type:</strong> {device.type}</p>
                        <p><strong>Price:</strong> ${device.price}</p>
                        <p><strong>Stock:</strong> {device.quantity}</p>
                        {user && user.role === 'STAFF' && (
                            <>
                                <button className="btn btn-secondary" onClick={() => handleEditButton(device)}>Edit</button>
                            </>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
}

export default ProductList;
