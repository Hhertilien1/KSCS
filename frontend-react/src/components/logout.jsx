import React from "react"; // Import React library to create the component

// LogoutButton component to handle the logout action
export const LogoutButton = () => {
  // onLogout function to clear all data in localStorage when the button is clicked
  const onLogout = () => {
    localStorage.clear(); // Clear all data stored in localStorage
  };

  // Render a button that triggers the onLogout function when clicked
  return <button onClick={onLogout}>Logout</button>; // The button displays "Logout"
};
