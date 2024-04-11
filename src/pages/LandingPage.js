import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/landing-page.css';

const LandingPage = () => {
    const navigate = useNavigate();

    const handleRegisterClick = () => {
        navigate('/register');
    };

    const handleLoginClick = () => {
        navigate('/login');
    };

    return (
        <div className="landing-container">
            <div className="content">
                <h1>Welcome to IoTBay</h1>
                <div>
                    <button
                        className="btn btn-primary"
                        onClick={handleRegisterClick}
                    >
                        Register
                    </button>
                    <button
                        className="btn btn-primary ms-2"
                        onClick={handleLoginClick}
                    >
                        Login
                    </button>
                </div>
            </div>
        </div>
    );
}

export default LandingPage;