import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/sign-in.css';
import userService from '../services/UserService'; // Ensure the import name matches the exported userService instance

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [isStaffLogin, setIsStaffLogin] = useState(false); // This will toggle between staff and user login
    const [isAdminLogin, setIsAdminLogin] = useState(false); // This will toggle between admin and other logins
    const navigate = useNavigate();

    const handleLogin = async (event) => {
        event.preventDefault();
        setErrorMessage(''); // Clear any existing error messages
        try {
            // Attempt login (same API for admin, staff, and user)
            let response;
            if (isStaffLogin) {
                response = await userService.loginStaff(email, password);
            }
            else if (isAdminLogin) {
                response = await userService.loginAdmin(email, password);
            }
            else{
                response = await userService.login(email, password);
            }


            // Assuming response has a role attribute in data
            if (response.status === 200) {
                // Determine the role based on the checkboxes
                const role = isAdminLogin ? 'ADMIN' : (isStaffLogin ? 'STAFF' : 'USER');
                localStorage.setItem('user', JSON.stringify({
                    ...response.data,
                    role: role
                }));

                // Navigate based on role
                if (role === 'ADMIN') {
                    navigate('/admin'); // Redirect to the admin page
                } else {
                    navigate('/main'); // Redirect to the main/welcome page
                }
            }
        } catch (error) {
            setErrorMessage(error.response?.data?.message || 'Failed to login, please try again.');
            console.error(error);
        }
    };

    const backToLanding = () => {
        navigate('/');
    };

    return (
        <div className="signin-container">
            <main className="form-signin w-100 m-auto">
                <form onSubmit={handleLogin}>
                    <div className="h3 mb-3 fw-normal">
                        {isAdminLogin
                            ? "Please sign in (Admin)"
                            : isStaffLogin
                                ? "Please sign in (Staff)"
                                : "Please sign in (User)"}
                    </div>
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
                            id="adminLoginCheck"
                            checked={isAdminLogin}
                            onChange={(e) => {
                                setIsAdminLogin(e.target.checked);
                                if (e.target.checked) setIsStaffLogin(false); // Uncheck staff login if admin login is selected
                            }}
                        />
                        <label htmlFor="adminLoginCheck" className="ms-2">Login as Admin</label>
                    </div>

                    <div className="mb-3">
                        <input
                            type="checkbox"
                            id="staffLoginCheck"
                            checked={isStaffLogin}
                            onChange={(e) => {
                                setIsStaffLogin(e.target.checked);
                                if (e.target.checked) setIsAdminLogin(false); // Uncheck admin login if staff login is selected
                            }}
                        />
                        <label htmlFor="staffLoginCheck" className="ms-2">Login as Staff</label>
                    </div>

                    <button className="btn btn-primary w-100 py-2 mb-3" type="submit">Sign in</button>
                </form>
                <button className='btn btn-primary w-100 py-2' onClick={backToLanding}>Back to Landing Page</button>
            </main>
        </div>
    );
}

export default LoginPage;
