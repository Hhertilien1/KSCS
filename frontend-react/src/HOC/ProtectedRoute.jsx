import { Navigate, Outlet } from "react-router-dom"; // Import components for routing
import { useEffect, useState } from "react"; // Import hooks for state and side-effects

import { useUser } from "../hooks/useUser"; // Import custom hook to get user data

// ProtectedRoute component to handle route protection based on authentication and user role
const ProtectedRoute = ({ role }) => {
  // State to track if the user is authenticated and if the loading state is active
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  
  // Get the user data from the custom hook
  const { user } = useUser();
  
  useEffect(() => {
    // Check if there is an authentication token in localStorage
    const token = localStorage.getItem("authToken");
    if (token) {
      setIsAuthenticated(true); // If token exists, set the user as authenticated
    }
    
    // Simulate loading time by setting a delay before marking loading as complete
    setTimeout(() => {
      setIsLoading(false); // Set loading to false after 2 seconds
    }, 2000);
  }, []); // Run this effect only once when the component mounts
  
  // If the app is still loading or if the user role is not yet available, show a loading message
  if (isLoading || !user?.role) {
    return <div>Loading...</div>;
  }

  // If the role is provided as a prop and the user's role doesn't match, redirect to the home page
  if (role && user?.role !== role) {
    return <Navigate to="/" />;
  }

  // If the user is not authenticated, redirect to the login page
  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }

  // If all checks pass, render the child components of the route
  return <Outlet />;
};

export default ProtectedRoute; // Export the ProtectedRoute component for use in routing
