import React, { useState } from 'react';

import Footer from '../components/Footer';
import { Link } from 'react-router-dom';
import { NavigationHeader } from '../components/NavigationHeader';

const Profile = () => {
    // State to manage input values
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [cell, setCell] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [showModal, setShowModal] = useState(false);

    // Handle the form submission
    const handleUpdateProfile = () => {
        setError(""); // Clear previous error
        setSuccess(""); // Clear previous success message

        // Validate inputs
        if (!username || !email || !cell || !password) {
            setError("All fields are required.");
            return;
        }

        // Show modal for confirmation
        setShowModal(true);
    };

    // Confirm profile update
    const confirmUpdate = () => {
        setSuccess("Profile updated successfully!");
        setShowModal(false);
        resetForm();
    };

    // Close modal without updating
    const closeModal = () => {
        setShowModal(false);
    };

    // Reset form fields
    const resetForm = () => {
        setUsername("");
        setEmail("");
        setCell("");
        setPassword("");
    };
    return (
        <div>
            <NavigationHeader showLogout />


            {/* Wrapper for content */}
            <div className="wrapper">
                <header>
                    <h1 className="dashboard-headline">Your Profile, Your Way</h1>
                    <h2 className="dashboard-subheadline">
                        Keep your account details up-to-date for seamless communication.
                    </h2>
                </header>

                {/* Profile form */}
                <div className='wrapper'>
                    <div className="login-container ">
                        <form id="profileForm" className="p-1">

                            <div lassName="form-group">
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
                            <div lassName="form-group">
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
                            <div lassName="form-group">
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
                            <div lassName="form-group">
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

                            <button type="button" onClick={handleUpdateProfile}>Update Profile</button>
                            <p className="error">{error}</p>
                            <p className="success">{success}</p>
                            <button onClick={() => window.location.href = "/jobs"}>Cancel</button>
                        </form>
                    </div>
                </div>
                {/* Confirmation Modal */}
                {showModal && (
                    <div id="confirmationModal" className="modal">
                        <h3>Are you sure you want to update your profile?</h3>
                        <button onClick={confirmUpdate}>Confirm</button>
                        <button onClick={closeModal}>Cancel</button>
                    </div>
                )}
                {showModal && <div id="backdrop" className="backdrop"></div>}
            </div>

            <Footer />

        </div>
    );
};

export default Profile;
