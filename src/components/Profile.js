import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import userService from '../services/UserService';
import '../styles/profile.css';

const Profile = () => {
    const [userDetails, setUserDetails] = useState({
        firstName: '',
        lastName: '',
        email: '',
        phoneNumber: '',
        address: ''
    });
    const [logs, setLogs] = useState([]);
    const [editMode, setEditMode] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const user = JSON.parse(localStorage.getItem('user'));
        if (user && user.id) {
            fetchUserDetails(user.id);
            fetchAccessLogs(user.id);
        } else {
            console.error('No user ID found');
            navigate('/login');
        }
    }, [navigate]);

    const fetchUserDetails = async (userId) => {
        try {
            const response = await userService.retrieve(userId);
            setUserDetails(response.data);
        } catch (error) {
            console.error('Failed to fetch user details:', error);
            navigate('/login');
        }
    };

    const fetchAccessLogs = async (userId) => {
        try {
            const response = await userService.retrieveLog(userId);
            setLogs(Array.isArray(response.data) ? response.data : []);
        } catch (error) {
            console.error('Failed to fetch access logs:', error);
            setLogs([]); // Set logs to an empty array in case of error
        }
    };
    

    const handleEditToggle = () => {
        setEditMode(!editMode);
    };

    const handleDetailChange = (e) => {
        const { name, value } = e.target;
        setUserDetails(prev => ({ ...prev, [name]: value }));
    };

    const updateDetails = async () => {
        const userId = JSON.parse(localStorage.getItem('user')).id;
        if (userId) {
            try {
                await userService.update(userId, userDetails);
                setEditMode(false);
                fetchUserDetails(userId); // Refresh user details after update
            } catch (error) {
                console.error('Update failed:', error);
            }
        }
    };

    const cancelAccount = async () => {
        const userId = JSON.parse(localStorage.getItem('user')).id;
        if (userId) {
            try {
                await userService.delete(userId);
                localStorage.clear();
                navigate('/login');
            } catch (error) {
                console.error('Failed to cancel account:', error);
            }
        }
    };

    const backToCatalogue = () => {
        navigate('/main');
    }

    return (
        <div className="profile-container">
            <h1>Profile</h1>
            {editMode ? (
                <div>
                    <label>First Name:</label>
                    <input type="text" name="firstName" value={userDetails.firstName} onChange={handleDetailChange} />
                    <label>Last Name:</label>
                    <input type="text" name="lastName" value={userDetails.lastName} onChange={handleDetailChange} />
                    <label>Email (cannot be changed):</label>
                    <input type="email" value={userDetails.email} readOnly />
                    <label>Phone:</label>
                    <input type="text" name="phoneNumber" value={userDetails.phoneNumber} onChange={handleDetailChange} />
                    <button className="btn btn-primary py-2 mb-3" onClick={updateDetails}>Save Changes</button>
                    <button className="btn btn-primary py-2 mb-3 ms-2" onClick={handleEditToggle}>Cancel</button>
                    <button className="btn btn-danger py-2 mb-3 ms-2" onClick={cancelAccount}>Cancel Account</button>
                </div>
            ) : (
                <div>
                    <p>First Name: {userDetails.firstName}</p>
                    <p>Last Name: {userDetails.lastName}</p>
                    <p>Email: {userDetails.email}</p>
                    <p>Phone: {userDetails.phoneNumber}</p>
                    <button className="btn btn-primary py-2 mb-3" onClick={handleEditToggle}>Edit</button>
                    <button className="btn btn-primary py-2 mb-3 ms-2" onClick={backToCatalogue}>Back</button>
                </div>
            )}
            <h2>Access Logs</h2>
            <ul>
                {logs.map(log => (
                    <li key={log.id}>{`Created at: ${log.createdAt}`}</li>
                ))}
            </ul>
        </div>
    );
};

export default Profile;
