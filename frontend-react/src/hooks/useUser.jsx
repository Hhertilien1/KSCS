import { useEffect, useState } from "react";

// Custom hook to manage user data
export const useUser = () => {
  // State to store user information
  const [user, setUser] = useState();

  // useEffect runs once when the component mounts (empty dependency array means it only runs once)
  useEffect(() => {
    // Try to get the user data from localStorage
    const user = localStorage.getItem("user");
    
    // If there is user data in localStorage, parse it and set it in state
    if (user) {
      setUser(JSON.parse(user)); // Parse the JSON string into a JavaScript object
    }
  }, []); // Empty array means this effect runs only once when the component mounts

  // Return the user data so it can be accessed elsewhere
  return {
    user, // return the user state to other components
  };
};
