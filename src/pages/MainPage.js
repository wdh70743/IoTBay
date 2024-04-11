import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/main-page.css';

const MainPage = () => {
  const [userData, setUserData] = useState({});
  const navigate = useNavigate();

  useEffect(() => {
    // Function to fetch user data
    const fetchUserData = async () => {
      try {
        const user = localStorage.getItem('user') || localStorage.getItem('staff');
        if (!user) throw new Error('No user data found');

        setUserData(JSON.parse(user));
      } catch (error) {
        console.error(error);
        navigate('/login'); // Redirect to the login page if no data is found
      }
    };
    fetchUserData();
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem('user');
    localStorage.removeItem('staff'); // Remove the token from localStorage
    navigate('/'); // Redirect to the landing page
  };

  // Render user data or a loading message
  const renderUserData = () => {
    if (Object.keys(userData).length) {
      return (
        <div>
          <p>Name: {userData.firstName} {userData.lastName}</p>
          <p>Email: {userData.email}</p>
          <p>Address: {userData.address}</p>
          <p>Phone: {userData.phoneNumber}</p>
          <p>Role: {userData.role}</p>
        </div>
      );
    } else {
      return <p>Loading user data...</p>;
    }
  };

  return (
    <div className="main-container" >
      <div className="content-main">
        <h1>Main Page</h1>
        <div>
          {renderUserData()}
        </div>
        <button className="btn btn-primary"
          onClick={handleLogout}>Logout</button>
      </div>
    </div>
  );
}

export default MainPage;
