import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/register-page.css'; // Ensure the path is correct
import userService from '../services/UserService';



const RegisterPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [address, setAddress] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const user = {
                email,
                password,
                firstName,
                lastName,
                address,
                phoneNumber,
                role: 'USER', // Assuming 'user' as a default role
                createAt: new Date().toISOString() // Assuming backend requires an ISO string
            }

            const response = await userService.register(user);
            
            if (response.status === 200) {
                navigate('/login');
            }
            // Handle response, e.g., storing a received token, navigating to another page, etc.

        } catch (error) {
            setErrorMessage(error.response?.data?.message || 'Registration failed, please try again.');
            console.error(error);
        }
    };

    const backToLanding = () => {
        navigate('/');
    };

    return (
        <div className="register-container">
            <form onSubmit={handleSubmit} className="form-register">
                <h1>Register for IoTBay</h1>
                {errorMessage && <div className="alert alert-danger" role="alert">{errorMessage}</div>}
                <input type="email" value={email} onChange={e => setEmail(e.target.value)} placeholder="Email" required />
                <input type="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="Password" required />
                <input type="text" value={firstName} onChange={e => setFirstName(e.target.value)} placeholder="First Name" required />
                <input type="text" value={lastName} onChange={e => setLastName(e.target.value)} placeholder="Last Name" required />
                <input type="text" value={address} onChange={e => setAddress(e.target.value)} placeholder="Address" required />
                <input type="tel" value={phoneNumber} onChange={e => setPhoneNumber(e.target.value)} placeholder="Phone Number" required />
                <button type="submit" className="btn btn-primary">Register</button>
                <button className='btn btn-primary' onClick={backToLanding}>Back to Landing Page</button>
            </form>
        </div>
    );
};

export default RegisterPage;

