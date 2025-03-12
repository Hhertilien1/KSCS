import React, { useState } from "react";

import Footer from "../components/Footer";
import { NavigationHeader } from "../components/NavigationHeader";
import { apiService } from "../services/apiService";

const UpdateProfile = () => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [cell, setCell] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [showModal, setShowModal] = useState(false);

  const validateInputs = () => {
    if (!username || !email || !cell) {
      setError("All fields are required.");
      return false;
    }
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      setError("Please enter a valid email address.");
      return false;
    }
    const phoneRegex = /^[0-9]+$/;
    if (!phoneRegex.test(cell)) {
      setError("Please enter a valid cell phone number (numbers only).");
      return false;
    }
    setError("");
    return true;
  };

  const handleUpdateProfile = async () => {
    setSuccess("");
    if (validateInputs()) {
      try {
        debugger
        const response = await apiService().updateProfile({
          username,
          email,
          cell,
          password,
          confirmPassword: password,
        });

       
        if(response.user){
          setSuccess("Profile updated successfully!");
          setUsername(response.user.username);
          setEmail(response.user.email);
          setCell(response.user.cell);
          localStorage.setItem("user", JSON.stringify(response.user));
        }
        if(response.token){
          localStorage.setItem("authToken", response.token);  
        }
        
        setShowModal(false);
      } catch (error) {
        setError("Failed to update profile.");
      }finally{
        setShowModal(false);

      }
    }
  };

  const confirmUpdate = () => {
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
  };

  const resetForm = () => {
    setUsername("");
    setEmail("");
    setCell("");
    setPassword("");
  };

  const init = async () => {
    try {
      const response = await apiService().getSelf();
      setUsername(response.user.username);
      setEmail(response.user.email);
      setCell(response.user.cell);
    } catch (error) {
      setError("Failed to load your profile.");
    }
  };

  React.useEffect(() => {
    init();
  }, []);

  return (
    <div>
      <NavigationHeader showLogout />
      <div className="wrapper">
        <header>
          <h1 className="dashboard-headline">Your Profile, Your Way</h1>
          <h2 className="dashboard-subheadline">
            Keep your account details up-to-date for seamless communication.
          </h2>
        </header>
        <div className="wrapper">
          <div className="login-container p-8">
            <form id="profileForm" className="p-1">
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
                <button type="button" onClick={confirmUpdate}>
                  Update Profile
                </button>
                <a href={"/admin"}>
                  <button>Cancel</button>
                </a>
              </div>

              {error && <p className="error-message">{error}</p>}
              {success && <p className="success-message">{success}</p>}
            </form>
          </div>
        </div>
        {showModal && (
          <dialog className="modal-container" open>
            <div className="modal-content">
              <div className="modal-header">
                <h2>Profile Upload Confirmation</h2>
                <span className="close" onClick={closeModal}>
                  &times;
                </span>
              </div>
              <div className="modal-body">
                <p className="confirmation-message">
                  Are you sure you want to update your profile?
                </p>
              </div>
              <div className="modal-footer">
                <button className="button" onClick={handleUpdateProfile}>
                  OK
                </button>
              </div>
            </div>
          </dialog>
        )}
        {showModal && <div id="backdrop" className="backdrop"></div>}
      </div>
      <Footer />
    </div>
  );
};

export default UpdateProfile;