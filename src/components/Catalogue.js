import React from 'react';
import { useNavigate } from 'react-router-dom';
import ProductList from './ProductList';  // Import ProductList component
import userService from '../services/UserService';
import '../styles/device.css';

const Catalogue = () => {
    const navigate = useNavigate();
    const handleLogout = async () => {
        try {
            const user = JSON.parse(localStorage.getItem('user'));
            if (user && user.email && user.password) {
                const loginDetails = { email: user.email, password: user.password };
                if (user.role === 'STAFF') {
                    await userService.logoutStaff(loginDetails);
                } else {
                    await userService.logout(loginDetails);
                }
                navigate('/login');
                localStorage.removeItem('user');
            } else {
                throw new Error('User details not found');
            }
        } catch (error) {
            console.error('Logout failed:', error);
        }
    };

    const goToProfilePage = () => {
        navigate('/profile');
    };

    const goToAddProductPage = () => {
        navigate('/create');
    };

    const goToManageProductPage = () => {
        navigate('/manage');
    };

    return (
        <div className="main-container">
            <div className="content-main">
                <div className="top-right-buttons">
                    <button onClick={goToProfilePage} className="btn btn-secondary">Go to Profile</button>
                    <button onClick={handleLogout} className='btn btn-primary ms-2'>Logout</button>
                    {/* Conditionally render these if the user is a staff member */}
                    <button onClick={goToAddProductPage} className="btn btn-info ms-2">Add Product</button>
                    <button onClick={goToManageProductPage} className="btn btn-warning ms-2">Manage Products</button>
                </div>
                <ProductList />  {/* Using ProductList here */}
            </div>
        </div>
    );
}

export default Catalogue;
