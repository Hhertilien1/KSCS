import { Navigate, Outlet } from "react-router-dom"; // Import necessary components for routing

import { useUser } from "../hooks/useUser"; // Import the custom hook to get user data

// Component to handle route protection based on user role
const StrictNoAuthRoute = () => {
  // Destructure the user data from the custom hook
  const { user } = useUser();

  // If the user is an ADMIN, redirect them to the /admin page
  if (user?.role === "ADMIN") {
    return <Navigate to="/admin" />;
  } 
  // If the user is a CABINET_MAKER or INSTALLER, redirect them to the /jobs page
  else if (user?.role === "CABINET_MAKER" || user?.role === "INSTALLER") {
    return <Navigate to="/jobs" />;
  }

  // If the user does not match any role, render the child components (this happens if no redirection)
  return <Outlet />;
};

export default StrictNoAuthRoute; // Export the component to be used in routing configuration
