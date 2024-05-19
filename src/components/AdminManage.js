import React, { useState, useEffect } from 'react';
import adminService from '../services/AdminService';
import userService from '../services/UserService';
import { useNavigate } from 'react-router-dom';
import '../styles/admin-manage.css'; // 스타일 파일 임포트

const AdminManage = () => {
    const navigate = useNavigate();
    const [users, setUsers] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);
    const [newUser, setNewUser] = useState({
        adminId: '',
        email: '',
        password: '',
        firstName: '',
        lastName: '',
        phoneNumber: '',
        role: '',
        address: '',
        active: false
    });
    const [searchId, setSearchId] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const admin = JSON.parse(localStorage.getItem('user'));

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        setLoading(true);
        try {
            const storedUserIds = JSON.parse(localStorage.getItem('userIds')) || [];
            const usersData = [];
            for (const id of storedUserIds) {
                const response = await adminService.getUserById(id);
                usersData.push(response.data);
            }
            setUsers(usersData);
        } catch (error) {
            console.error('Failed to fetch users:', error);
            setError('Failed to fetch users.');
        } finally {
            setLoading(false);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewUser((prevUser) => ({ ...prevUser, [name]: value }));
    };

    const handleCreateUser = async () => {
        setLoading(true);
        setError('');
        try {
            const { adminId, email, password, firstName, lastName, phoneNumber, role, address } = newUser;
            const response = await adminService.createUser(adminId, email, password, firstName, lastName, phoneNumber, role, address);
            const userId = response.data.id;
            const storedUserIds = JSON.parse(localStorage.getItem('userIds')) || [];
            storedUserIds.push(userId);
            localStorage.setItem('userIds', JSON.stringify(storedUserIds));
            fetchUsers();
            setNewUser({
                adminId: '',
                email: '',
                password: '',
                firstName: '',
                lastName: '',
                phoneNumber: '',
                role: '',
                address: '',
                active: false
            });
        } catch (error) {
            console.error('Failed to create user:', error);
            setError('Failed to create user.');
        } finally {
            setLoading(false);
        }
    };

    const handleUpdateUser = async () => {
        setLoading(true);
        setError('');
        try {
            const { id, adminId, email, password, firstName, lastName, phoneNumber, role, address } = selectedUser;
            await adminService.updateUser(id, adminId, email, password, firstName, lastName, phoneNumber, role, address);
            fetchUsers();
            setSelectedUser(null);
        } catch (error) {
            console.error('Failed to update user:', error);
            setError('Failed to update user.');
        } finally {
            setLoading(false);
        }
    };

    const handleDeleteUser = async (id) => {
        setLoading(true);
        setError('');
        try {
            const adminId = admin.id;
            await adminService.deleteUser(id, adminId);
            const storedUserIds = JSON.parse(localStorage.getItem('userIds')) || [];
            const updatedUserIds = storedUserIds.filter(userId => userId !== id);
            localStorage.setItem('userIds', JSON.stringify(updatedUserIds));
            fetchUsers();
        } catch (error) {
            console.error('Failed to delete user:', error);
            setError('Failed to delete user.');
        } finally {
            setLoading(false);
        }
    };

    const handleActivateUser = async (id) => {
        setLoading(true);
        setError('');
        try {
            const adminId = admin.id;
            await adminService.activateUser(id, adminId);
            fetchUsers();
        } catch (error) {
            console.error('Failed to activate user:', error);
            setError('Failed to activate user.');
        } finally {
            setLoading(false);
        }
    };

    const handleDeactivateUser = async (id) => {
        setLoading(true);
        setError('');
        try {
            const adminId = admin.id;
            await adminService.deactivateUser(id, adminId);
            fetchUsers();
        } catch (error) {
            console.error('Failed to deactivate user:', error);
            setError('Failed to deactivate user.');
        } finally {
            setLoading(false);
        }
    };

    const handleSearchUser = async () => {
        setLoading(true);
        setError('');
        try {
            if (searchId.trim() === '') {
                fetchUsers();
            } else {
                const response = await adminService.getUserById(searchId);
                setUsers(response.data ? [response.data] : []);
            }
        } catch (error) {
            console.error('Failed to search user:', error);
            setError('Failed to search user.');
        } finally {
            setLoading(false);
        }
    };

    const handleLogout = async () => {
        if (admin && admin.email && admin.password) {
            const loginDetails = { email: admin.email, password: admin.password };
            await userService.logoutAdmin(loginDetails);
            navigate('/login');
            localStorage.removeItem('admin');
        }
    };

    return (
        <div className="admin-manage">
                        <div className="top-right-buttons">
                <button onClick = {handleLogout} className="btn btn-primary">Log out</button>
            </div>
            <h1 className="text-center my-4">User Management</h1>
            {loading && <p>Loading...</p>}
            {error && <p className="text-danger">{error}</p>}
            <div className="row">
                <div className="col-md-6">
                    <div>
                        <h2>Create User</h2>
                        <div className="form-group mb-2">
                            <input type="text" name="adminId" className="form-control" placeholder="Admin ID" value={newUser.adminId} onChange={handleInputChange} />
                        </div>
                        <div className="form-group mb-2">
                            <input type="email" name="email" className="form-control" placeholder="Email" value={newUser.email} onChange={handleInputChange} />
                        </div>
                        <div className="form-group mb-2">
                            <input type="password" name="password" className="form-control" placeholder="Password" value={newUser.password} onChange={handleInputChange} />
                        </div>
                        <div className="form-group mb-2">
                            <input type="text" name="firstName" className="form-control" placeholder="First Name" value={newUser.firstName} onChange={handleInputChange} />
                        </div>
                        <div className="form-group mb-2">
                            <input type="text" name="lastName" className="form-control" placeholder="Last Name" value={newUser.lastName} onChange={handleInputChange} />
                        </div>
                        <div className="form-group mb-2">
                            <input type="text" name="phoneNumber" className="form-control" placeholder="Phone Number" value={newUser.phoneNumber} onChange={handleInputChange} />
                        </div>
                        <div className="form-group mb-2">
                            <input type="text" name="role" className="form-control" placeholder="Role" value={newUser.role} onChange={handleInputChange} />
                        </div>
                        <div className="form-group mb-2">
                            <input type="text" name="address" className="form-control" placeholder="Address" value={newUser.address} onChange={handleInputChange} />
                        </div>
                        <button onClick={handleCreateUser} className="btn btn-primary mb-4">Create User</button>
                    </div>
                    <div className="mt-4">
                        <h2>Search User</h2>
                        <div className="form-group mb-2">
                            <input type="text" name="searchId" className="form-control" placeholder="User ID" value={searchId} onChange={(e) => setSearchId(e.target.value)} />
                        </div>
                        <button onClick={handleSearchUser} className="btn btn-primary mb-4">Search</button>
                    </div>
                </div>
                <div className="col-md-6">
                    <h2>User List</h2>
                    <table className="table table-striped">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Admin ID</th>
                                <th>Email</th>
                                <th>Password</th>
                                <th>First Name</th>
                                <th>Last Name</th>
                                <th>Phone Number</th>
                                <th>Role</th>
                                <th>Address</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {users.map(user => (
                                <tr key={user.id}>
                                    <td>{user.id}</td>
                                    <td>{admin.id}</td>
                                    <td>{user.email}</td>
                                    <td>{user.password}</td>
                                    <td>{user.firstName}</td>
                                    <td>{user.lastName}</td>
                                    <td>{user.phoneNumber}</td>
                                    <td>{user.role}</td>
                                    <td>{user.address}</td>
                                    <td>
                                        <button onClick={() => setSelectedUser(user)} className="btn btn-secondary btn-sm me-2">Edit</button>
                                        <button onClick={() => handleDeleteUser(user.id)} className="btn btn-danger btn-sm me-2">Delete</button>
                                        {user.active ? (
                                            <button onClick={() => handleDeactivateUser(user.id)} className="btn btn-warning btn-sm">Deactivate</button>
                                        ) : (
                                            <button onClick={() => handleActivateUser(user.id)} className="btn btn-success btn-sm">Activate</button>
                                        )}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
            {selectedUser && (
                <div className="mt-4">
                    <h2>Edit User</h2>
                    <div className="form-group mb-2">
                        <input type="text" name="adminId" className="form-control" placeholder="Admin ID" value={selectedUser.adminId} onChange={(e) => setSelectedUser({ ...selectedUser, adminId: e.target.value })} />
                    </div>
                    <div className="form-group mb-2">
                        <input type="email" name="email" className="form-control" placeholder="Email" value={selectedUser.email} onChange={(e) => setSelectedUser({ ...selectedUser, email: e.target.value })} />
                    </div>
                    <div className="form-group mb-2">
                        <input type="password" name="password" className="form-control" placeholder="Password" value={selectedUser.password} onChange={(e) => setSelectedUser({ ...selectedUser, password: e.target.value })} />
                    </div>
                    <div className="form-group mb-2">
                        <input type="text" name="firstName" className="form-control" placeholder="First Name" value={selectedUser.firstName} onChange={(e) => setSelectedUser({ ...selectedUser, firstName: e.target.value })} />
                    </div>
                    <div className="form-group mb-2">
                        <input type="text" name="lastName" className="form-control" placeholder="Last Name" value={selectedUser.lastName} onChange={(e) => setSelectedUser({ ...selectedUser, lastName: e.target.value })} />
                    </div>
                    <div className="form-group mb-2">
                        <input type="text" name="phoneNumber" className="form-control" placeholder="Phone Number" value={selectedUser.phoneNumber} onChange={(e) => setSelectedUser({ ...selectedUser, phoneNumber: e.target.value })} />
                    </div>
                    <div className="form-group mb-2">
                        <input type="text" name="role" className="form-control" placeholder="Role" value={selectedUser.role} onChange={(e) => setSelectedUser({ ...selectedUser, role: e.target.value })} />
                    </div>
                    <div className="form-group mb-2">
                        <input type="text" name="address" className="form-control" placeholder="Address" value={selectedUser.address} onChange={(e) => setSelectedUser({ ...selectedUser, address: e.target.value })} />
                    </div>
                    <button onClick={handleUpdateUser} className="btn btn-primary">Update User</button>
                </div>
            )}
        </div>
    );
}

export default AdminManage;
