import { Link, useNavigate } from "react-router-dom";
import React, { useState } from "react";

import Footer from "../components/Footer";
import { NavigationHeader } from "../components/NavigationHeader";
import { apiService } from "../services/apiService";

const SignUpForm = () => {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [cell, setCell] = useState("");
  const [office, setOffice] = useState("");
  const [role, setRole] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  // Validation function
  const validateForm = () => {
    if (
      !firstName ||
      !lastName ||
      !username ||
      !email ||
      !cell ||
      !office ||
      !role ||
      !password ||
      !confirmPassword
    ) {
      setError("All fields are required!");
      return false;
    }

    // Username validation
    const usernameRegex = /^[a-z0-9_]+$/;
    if (!usernameRegex.test(username)) {
      setError("Username can only contain lowercase letters, numbers and underscores.");
      return false;
    }

    // Email validation
    const emailRegex = /\S+@\S+\.\S+/;
    if (!emailRegex.test(email)) {
      setError("Please enter a valid email address.");
      return false;
    }

    // Password validation
    if (password.length < 8) {
      setError("Password must be at least 8 characters long.");
      return false;
    }

    // Confirm password check
    if (password !== confirmPassword) {
      setError("Passwords do not match!");
      return false;
    }

    // Cell number validation (optional - can be numeric)
    const cellRegex = /^[0-9]+$/;
    if (!cellRegex.test(cell)) {
      setError("Cell number must be numeric.");
      return false;
    }

    // Clear previous error messages if validation passes
    setError("");
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) return;

    setLoading(true);

    const payload = {
      firstName,
      lastName,
      username,
      email,
      cell,
      office,
      role,
      password,
      confirmPassword
    };

    try {
    //   resetForm();
      const response = await apiService().register(payload);

      if (response.token) {
        setTimeout(() => {
          if (response?.user?.role === "ADMIN") {
            navigate("/admin"); // Redirects to /admin-dashboard route
          } else if (response.user.role === "CABINET_MAKER" || response.user.role === "INSTALLER") {
            navigate("/jobs"); // Redirects to /admin-dashboard route
          }
          setLoading(false); // Reset loading state
        }, 2000);
      }
    } catch (error) {
      console.error("Error during sign up:", error);
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  const resetForm = () => {
    setFirstName("");
    setLastName("");
    setUsername("");
    setEmail("");
    setCell("");
    setOffice("");
    setRole("");
    setPassword("");
    setConfirmPassword("");
  };

  return (
    <>
      <header>
        <NavigationHeader showLogout={false} />

        <h1 className="dashboard-headline">Welcome Kitchen Saver Employees!</h1>
        <h2 className="dashboard-subheadline">
          Create an account to get started.
        </h2>
      </header>
      <div className="wrapper">
        <div className="signup-container mt-5">
          <div className="form-overlay">
            <h2 className="text-center mb-4">Register</h2>

            {/* Error Message */}
            {error && <div className="alert alert-danger">{error}</div>}

            <form onSubmit={handleSubmit}>
              {/* First Name */}
              <div className="form-group">
                <label htmlFor="first_name">First Name</label>
                <input
                  type="text"
                  name="first_name"
                  id="first_name"
                  className="form-control"
                  placeholder="Enter your first name"
                  value={firstName}
                  onChange={(e) => setFirstName(e.target.value)}
                />
              </div>

              {/* Last Name */}
              <div className="form-group">
                <label htmlFor="last_name">Last Name</label>
                <input
                  type="text"
                  name="last_name"
                  id="last_name"
                  className="form-control"
                  placeholder="Enter your last name"
                  value={lastName}
                  onChange={(e) => setLastName(e.target.value)}
                />
              </div>

              {/* Username */}
              <div className="form-group">
                <label htmlFor="username">Username</label>
                <input
                  type="text"
                  name="username"
                  id="username"
                  className="form-control"
                  placeholder="Enter your username"
                  value={username}
                  onChange={(e) => setUsername(e.target.value.toLowerCase())}
                />
              </div>

              {/* Email */}
              <div className="form-group">
                <label htmlFor="email">Email</label>
                <input
                  type="email"
                  name="email"
                  id="email"
                  className="form-control"
                  placeholder="Enter your email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
              </div>

              {/* Cell Number */}
              <div className="form-group">
                <label htmlFor="cell">Cell Number</label>
                <input
                  type="tel"
                  name="cell"
                  id="cell"
                  className="form-control"
                  placeholder="Enter your cell number"
                  value={cell}
                  onChange={(e) => setCell(e.target.value)}
                />
              </div>

              {/* Office Selection */}
              <div className="form-group">
                <label htmlFor="office">Select Your Office</label>
                <select
                  name="office"
                  id="office"
                  className="form-control"
                  value={office}
                  onChange={(e) => setOffice(e.target.value)}
                >
                  <option value="" disabled>
                    Select your office
                  </option>
                  <option value="400">400</option>
                  <option value="402">402</option>
                  <option value="403">403</option>
                </select>
              </div>

              {/* Role Selection */}
              <div className="form-group">
                <label htmlFor="role">Select Your Role</label>
                <select
                  name="role"
                  id="role"
                  className="form-control"
                  value={role}
                  onChange={(e) => setRole(e.target.value)}
                >
                  <option value="" disabled>
                    Select your role
                  </option>
                  <option value="CABINET_MAKER">Installer</option>
                  <option value="INSTALLER">Cabinet Maker</option>
                  <option value="ADMIN">Admin</option>
                </select>
              </div>

              {/* Password */}
              <div className="form-group">
                <label htmlFor="password">Password</label>
                <input
                  type="password"
                  name="password"
                  id="password"
                  className="form-control"
                  placeholder="Enter your password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </div>

              {/* Confirm Password */}
              <div className="form-group">
                <label htmlFor="confirm_password">Confirm Password</label>
                <input
                  type="password"
                  name="confirm_password"
                  id="confirm_password"
                  className="form-control"
                  placeholder="Confirm your password"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                />
              </div>

              {/* Submit Button */}
              <button
                type="submit"
                className="btn btn-primary mt-3 btn-block"
                disabled={loading}
              >
                {loading ? (
                  <div
                    className="spinner-border spinner-border-sm"
                    role="status"
                  ></div>
                ) : (
                  "Register"
                )}
              </button>
            </form>

            {/* Login Link */}
            <div className="text-center mt-3">
              <p>
                Already have an account?
                <Link to="/login" className="btn btn-sm btn-link">
                  Log in!
                </Link>
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Footer */}
      <Footer />

    </>
  );
};

export default SignUpForm;
