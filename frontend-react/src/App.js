import './App.css'; // Import the CSS file for styling

import { Route, BrowserRouter as Router, Routes } from 'react-router-dom'; // Import routing tools from react-router-dom


// Import different pages for the app
import AdminDashboard from './pages/AdminDashboard';
import CreateEmployee from './pages/CreateEmployee';
import CreateJob from './pages/CreateJob';
import JobDashboard from './pages/JobDashboard';
import LoginForm from './pages/LoginForm';
import Profile from './pages/Profile';
import ProtectedRoute from './HOC/ProtectedRoute'; // component that blocks users who are not logged in
import SignUpForm from './pages/SignUpForm';
import StrictNoAuthRoute from './HOC/StrictNoAuthRoute'; // component that blocks users who are already logged in
import UpdateProfile from './pages/UpdateProfile';

function App() {
  return (
    <Router> {/* Ensure Router is wrapping the entire app */}

<div className="App"> {/* Main wrapper for the app */}
        <Routes> {/* This holds all the different routes */}
          
          {/* Routes that only logged-out users can access */}
          <Route element={<StrictNoAuthRoute />}> 
            <Route path="/" element={<LoginForm />} /> {/* Login page */}
            <Route path="/login" element={<LoginForm />} /> {/* Login page (same as /) */}
            <Route path="/signup" element={<SignUpForm />} /> {/* Sign-up page */}
          </Route>

          {/* Routes that only logged-in users can access */}
          <Route element={<ProtectedRoute />}> 
            <Route path="/jobs" element={<JobDashboard />} /> {/* Job listings */}
            <Route path="/update-profile" element={<UpdateProfile />} /> {/* Page to update profile */}
            <Route path="/profile" element={<Profile />} /> {/* User profile page */}
          </Route>
          
          {/* Routes that only admins can access */}
          <Route element={<ProtectedRoute role="ADMIN" />}> 
            <Route path="/create-employee" element={<CreateEmployee />} /> {/* Create employee page */}
            <Route path="/createjob" element={<CreateJob />} /> {/* Create job page */}
            <Route path="/admin" element={<AdminDashboard />} /> {/* Admin dashboard */}
          </Route>
        </Routes>
      </div>
    </Router>
  );
}

export default App; // Export the App so it can be used in other files
