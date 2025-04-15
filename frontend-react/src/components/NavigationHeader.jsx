import { Link, Navigate, useNavigate } from "react-router-dom"; // Import necessary components for routing
import React, { use, useEffect, useState } from "react"; // Import React hooks for state and side-effects

import { useUser } from "../hooks/useUser"; // Import custom hook to get user data

// NavigationHeader component to handle navigation links based on user authentication and role
export const NavigationHeader = ({ showLogout }) => {
  // State to track if the user is logged in
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  
  // Get the user data from the custom hook
  const { user } = useUser();
  
  // useNavigate hook for redirecting to different routes
  const navigate = useNavigate();
  
  // onLogout function to clear the localStorage and redirect to the login page
  const onLogout = () => {
    localStorage.clear(); // Clear all data in localStorage
    return navigate("/login"); // Redirect to login page
  };

  useEffect(() => {
    // Check if there is an authentication token in localStorage
    const token = localStorage.getItem("authToken");
    if (token) {
      setIsLoggedIn(true); // If token exists, mark the user as logged in
    }
  }, []); // This effect runs only once when the component mounts

  return (
    <nav> {/* Main navigation container */}
      <div className="nav-container"> {/* Container for the navigation items */}
        <h1 className="brand-name">Kitchen Saver</h1> {/* Brand name/title */}

        {isLoggedIn ? (
          <Link to={user?.role === "ADMIN" ? "/admin" : "/jobs"} className="ml-2">
            <button> Home</button> {/* Button for home (admin or employee) */}
          </Link>
        ) : null}

        
        
        {/* If the user is logged in and has an 'ADMIN' role, display admin-related links */}
        {isLoggedIn && user?.role === "ADMIN" ? (
          <>
            <Link to="/createjob"> {/* Link to create a new job */}
              <button> Create Job</button> {/* Button for creating a job */}
            </Link>

            <Link to="/create-employee"> {/* Link to create a new employee */}
              <button> Create Employee</button> {/* Button for creating an employee */}
            </Link>
          </>
        ) : null} {/* If not an admin, do not display these links */}

        {/* If the user is logged in, display the option to update profile */}
        {isLoggedIn ? (
          <Link to="/update-profile"> {/* Link to update profile page */}
            <button> Update Profile</button> {/* Button for updating profile */}
          </Link>
        ) : null}

      </div>
      
      {/* If showLogout is true, display the logout button */}
      {showLogout ? <button onClick={onLogout}>Logout</button> : null}
    </nav>
  );
};
