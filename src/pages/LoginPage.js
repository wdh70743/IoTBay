import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/sign-in.css';
import userService from '../services/UserService'; // Ensure the import name matches the exported userService instance

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [isStaffLogin, setIsStaffLogin] = useState(false); // This will toggle between staff and user login
    const navigate = useNavigate();

    const handleLogin = async (event) => {
        event.preventDefault();
        setErrorMessage(''); // Clear any existing error messages
        try {
            let response;
            if (isStaffLogin) {
                // Attempt staff login
                response = await userService.loginStaff(email, password);
            } else {
                // Attempt user login
                response = await userService.login(email, password);
            }

            // Assuming both responses have the same format
            if (response.status === 200) {
                // Assuming the backend correctly sets a user or staff role in the response
                localStorage.setItem('user', JSON.stringify(response.data)); // Consider storing as 'profile' to unify user/staff data
                navigate('/main'); // Redirect to the main/welcome page
            }
        } catch (error) {
            setErrorMessage(error.response?.data?.message || 'Failed to login, please try again.');
            console.error(error);
        }
    };

    return (
        <div className="signin-container">
            <main className="form-signin w-100 m-auto">
                <form onSubmit={handleLogin}>
                    <div className="h3 mb-3 fw-normal">{isStaffLogin ? "Please sign in (Staff)" : "Please sign in (User)"}</div>
                    {errorMessage && <div className="alert alert-danger" role="alert">{errorMessage}</div>}
                    <div className="form-floating">
                        <input
                            type="email"
                            className="form-control"
                            id="floatingInput"
                            placeholder="name@example.com"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                        <label htmlFor="floatingInput">Email address</label>
                    </div>
                    <div className="form-floating">
                        <input
                            type="password"
                            className="form-control"
                            id="floatingPassword"
                            placeholder="Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                        <label htmlFor="floatingPassword">Password</label>
                    </div>

                    <div className="mb-3">
                        <input
                            type="checkbox"
                            id="staffLoginCheck"
                            checked={isStaffLogin}
                            onChange={(e) => setIsStaffLogin(e.target.checked)}
                        />
                        <label htmlFor="staffLoginCheck" className="ms-2">Login as Staff</label>
                    </div>

                    <button className="btn btn-primary w-100 py-2" type="submit">Sign in</button>
                </form>
            </main>
        </div>
    );
}

export default LoginPage;
