import { Link, data, useNavigate } from "react-router-dom";
import React, { useState } from "react";

import Footer from "../components/Footer";
import { NavigationHeader } from "../components/NavigationHeader";
import { apiService } from "../services/apiService";

const LoginForm = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false); // To manage loading state
  const [error, setError] = useState(""); // To handle error messages
  const [emailError, setEmailError] = useState(""); // To handle email specific validation errors
  const [passwordError, setPasswordError] = useState(""); // To handle password specific validation errors
  const navigate = useNavigate(); // Use navigate hook for redirection

  // Function to validate email format
  const validateEmail = (email) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(""); // Clear any previous error messages
    setEmailError(""); // Clear email validation error
    setPasswordError(""); // Clear password validation error

    // Validate the email and password
    if (!email || !password) {
      if (!email) setEmailError("Email is required.");
      if (!password) setPasswordError("Password is required.");
      setLoading(false);
      return;
    }

    if (!validateEmail(email)) {
      setEmailError("Please enter a valid email address.");
      setLoading(false);
      return;
    }

    // Create payload for login
    const payload = {
      email: email,
      password: password,
    };

    try {
      const response = await apiService().login(payload);

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
      console.error("Error during login:", error);
      setError(error.message);
      setLoading(false);
    } finally {
    }
  };

  return (
    <>
      <header>
        <NavigationHeader showLogout={false} />
        <h1 className="dashboard-headline">Welcome Kitchen Saver Employees!</h1>
        <h2 className="dashboard-subheadline">
          Login to access your personalized dashboard and tools.
        </h2>
      </header>
      <div className="wrapper">
        <div className="login-container px-4">
          <h2 className="dashboard-headline">Login</h2>
          <form onSubmit={handleSubmit}>
            <div className="p-1">
              <div className="form-group">
                <label htmlFor="first_name">Email</label>
                <input
                  type="email"
                  name="email"
                  className="form-input form-control mb-3"
                  placeholder="Enter your email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
                {emailError && (
                  <p className="error-message taxt-danger ">{emailError}</p>
                )}
              </div>
              <div className="form-group">
                <label htmlFor="first_name">Password</label>

                <input
                  type="password"
                  name="password"
                  className="form-input form-control mb-3"
                  placeholder="Enter your password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
                {passwordError && (
                  <p className="error-message">{passwordError}</p>
                )}
              </div>

              <button
                type="submit"
                className="form-button-login"
                disabled={loading}
              >
                {loading ? "Logging in..." : "Login"}
              </button>
            </div>
          </form>
          {error && <p className="error-message m-2">{error}</p>}
          <div className="signup-link">
            <p>
              Don't have an account?{" "}
              <Link to="/signup" className="signup-link-btn">
                Sign up!
              </Link>
            </p>
          </div>
        </div>
      </div>

      <Footer />

    </>
  );
};

export default LoginForm;
