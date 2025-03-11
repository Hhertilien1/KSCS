import React, { useState } from "react"; // Import React and useState hook

import Footer from "../components/Footer"; // Import Footer component
import { NavigationHeader } from "../components/NavigationHeader"; // Import Navigation Header component
import { apiService } from "../services/apiService"; // Import API service for making requests

const UpdateProfile = () => {
  // Define state variables for form fields
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [cell, setCell] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(""); // State to store error messages
  const [success, setSuccess] = useState(""); // State to store success messages
  const [showModal, setShowModal] = useState(false); // State to control modal visibility

  // Function to validate input fields before updating profile
  const validateInputs = () => {
    if (!username || !email || !cell || !password) {
      setError("All fields are required.");
      return false;
    }
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // Regular expression for email validation
    if (!emailRegex.test(email)) {
      setError("Please enter a valid email address.");
      return false;
    }
    const phoneRegex = /^[0-9]+$/; // Regular expression to allow only numbers in cell field
    if (!phoneRegex.test(cell)) {
      setError("Please enter a valid cell phone number (numbers only).");
      return false;
    }
    setError(""); // Clear error if all validations pass
    return true;
  };

  // Function to handle profile update
  const handleUpdateProfile = async () => {
    setSuccess(""); // Reset success message
    if (validateInputs()) {
      try {
        const response = await apiService().updateProfile({
          username,
          email,
          cell,
          password,
          confirmPassword: password, // Confirm password
        });

        setSuccess("Profile updated successfully!"); // Show success message
        setUsername(response.user.username); // Update username state
        setEmail(response.user.email); // Update email state
        setCell(response.user.cell); // Update cell state
        setShowModal(false); // Close modal after success
      } catch (error) {
        setError("Failed to update profile."); // Show error message on failure
      }
    }
  };

  // Function to confirm profile update by showing modal
  const confirmUpdate = () => {
    setShowModal(true);
  };

  // Function to close the modal
  const closeModal = () => {
    setShowModal(false);
  };

  // Function to reset form fields
  const resetForm = () => {
    setUsername("");
    setEmail("");
    setCell("");
    setPassword("");
  };

  // Function to fetch user profile data when component loads
  const init = async () => {
    try {
      const response = await apiService().getSelf();
      setUsername(response.user.username);
      setEmail(response.user.email);
      setCell(response.user.cell);
    } catch (error) {
      setError("Failed to load your profile."); // Show error message if fetching profile fails
    }
  };

  // Fetch user data when component mounts
  React.useEffect(() => {
    init();
  }, []);

  return (
    <div>
      <NavigationHeader showLogout /> {/* Show navigation header with logout option */}
      <div className="wrapper">
        <header>
          <h1 className="dashboard-headline">Your Profile, Your Way</h1>
          <h2 className="dashboard-subheadline">
            Keep your account details up-to-date for seamless communication.
          </h2>
        </header>
        <div className="wrapper">
          <div className="login-container p-8">
            <form id="profileForm" className="p-1"> {/* Profile update form */}
              <div className="form-group">
                <label htmlFor="username">Username: </label>
                <input
                  type="text"
                  id="username"
                  value={username}
                  className="form-control form-control-sm mb-1"
                  placeholder="Enter your Username"
                  onChange={(e) => setUsername(e.target.value)}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="email">Email: </label>
                <input
                  type="email"
                  className="form-control form-control-sm mb-1"
                  id="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="cell">Cell #: </label>
                <input
                  type="text"
                  id="cell"
                  className="form-control form-control-sm mb-1"
                  value={cell}
                  onChange={(e) => setCell(e.target.value)}
                  required
                />
              </div>
              <div className="form-group ">
                <label htmlFor="password">New Password: </label>
                <input
                  type="password"
                  id="password"
                  className="form-control form-control-sm mb-1"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>
              <div
                style={{
                  justifyContent: "space-between",
                  width: "100%",
                  display: "flex",
                  marginTop: "16px",
                }}
              >
                <button type="button" onClick={confirmUpdate}> {/* Show modal on click */}
                  Update Profile
                </button>
                <a href={"/admin"}> {/* Link to admin page */}
                  <button>Cancel</button>
                </a>
              </div>

              {error && <p className="error-message">{error}</p>} {/* Display error message if any */}
              {success && <p className="success-message">{success}</p>} {/* Display success message if any */}
            </form>
          </div>
        </div>
        {showModal && (
          <dialog className="modal-container" open> {/* Modal confirmation dialog */}
            <div className="modal-content">
              <div className="modal-header">
                <h2>Profile Upload Confirmation</h2>
                <span className="close" onClick={closeModal}> {/* Close modal on click */}
                  &times;
                </span>
              </div>
              <div className="modal-body">
                <p className="confirmation-message">
                  Your profile has been updated successfully!
                </p>
              </div>
              <div className="modal-footer">
                <button className="button" onClick={handleUpdateProfile}> {/* Confirm profile update */}
                  OK
                </button>
              </div>
            </div>
          </dialog>
        )}
        {showModal && <div id="backdrop" className="backdrop"></div>} {/* Modal backdrop */}
      </div>
      <Footer /> {/* Footer component */}
    </div>
  );
};

export default UpdateProfile; // Export the component
