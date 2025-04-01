import axios from "axios"; // Import axios for making API requests

export const apiService = () => {
  // Define the base URL for API requests, using an environment variable
  const API_BASE_URL = `${process.env.REACT_APP_API_BASE_URL}/api`;

  // Create an axios instance for public requests (no authentication required)
  const publicAxios = axios.create({
    baseURL: API_BASE_URL, // Set the base URL for API requests
    headers: { "Content-Type": "application/json" }, // Set default headers
  });

  // Create an axios instance for authenticated requests (needs a token)
  const authAxios = axios.create({
    baseURL: API_BASE_URL, // Set the base URL
    headers: {
      "Content-Type": "application/json", // Set default headers
      Authorization: `Bearer ${localStorage.getItem("authToken")}`, // Get token from local storage
    },
  });

  // Add an interceptor to handle unauthorized access (e.g., token expired)
  authAxios.interceptors.response.use(
    (response) => response, // Return response if successful
    (error) => {
      if (error.response?.status === 401) {
        // If the user is not authorized, they could be redirected to login
        // localStorage.clear(); // Uncomment this to clear stored user data
        // window.location = "/login"; // Uncomment this to redirect to login page
      }
      return Promise.reject(error); // Pass the error forward
    }
  );

  return {
    // Function to log in a user
    login: async (payload) => {
      try {
        const response = await publicAxios.post("/user/login", payload); // Send login request
        if (!response?.data?.token) {
          throw new Error({ error: { response } }); // Throw an error if no token is received
        }
        localStorage.setItem("authToken", response.data.token); // Store token in local storage
        localStorage.setItem("user", JSON.stringify(response.data.user)); // Store user data
        return response.data; // Return the response data
      } catch (error) {
        throw new Error(error.response?.data?.message || "Login failed"); // Handle errors
      }
    },

    // Function to register a new user
    register: async (userData) => {
      try {
        const response = await publicAxios.post("/user/register", userData); // Send registration request
        if (!response?.data?.token) {
          throw new Error({ error: { response } }); // Throw an error if no token is received
        }
        localStorage.setItem("authToken", response.data.token); // Store token
        localStorage.setItem("user", JSON.stringify(response.data.user)); // Store user data
        return response.data; // Return response data
      } catch (error) {
        throw new Error(error.response?.data?.message || "Registration failed"); // Handle errors
      }
    },

    // Function to get the currently logged-in user's information
    getSelf: async () => {
      try {
        const response = await authAxios.get("/user/getSelf"); // Fetch user data
        return response.data; // Return user data
      } catch (error) {
        throw new Error(
          error.response?.data?.message || "Failed to fetch user data"
        ); // Handle errors
      }
    },

    // Function to delete a user by ID
    deleteUser: async (userId) => {
      try {
        await authAxios.delete(`/user/delete/${userId}`);
      } catch (error) {
        throw new Error(
          error.response?.data?.message || "Failed to delete user"
        );
      }
    },

    // Function to create a new employee
    createEmployee: async (employeeData) => {
      try {
        const response = await authAxios.post("/user/createEmployee", employeeData);
        if (!response?.data) {
          throw new Error({ error: { response } });
        }
        return response.data;
      } catch (error) {
        throw new Error(
          error.response?.data?.message || "Failed to create employee"
        );
      }
    },

    // Function to fetch all employees
    getAllEmployees: async () => {
      try {
        const response = await authAxios.get("/user/getAllEmployees");
        if (!response?.data) {
          throw new Error({ error: { response } });
        }
        return response.data;
      } catch (error) {
        throw new Error(
          error.response?.data?.message || "Failed to fetch all employees"
        );
      }
    },

    // Function to create a new job
    createJob: async (jobData) => {
      try {
        const response = await authAxios.post("/jobs", jobData);
        return response.data;
      } catch (error) {
        throw new Error(
          error.response?.data?.message || "Failed to create job"
        );
      }
    },

    // Function to delete a job by ID
    deleteJob: async (jobId) => {
      try {
        const response = await authAxios.delete(`/jobs/${jobId}`);
        return response.data;
      } catch (error) {
        throw new Error(
          error.response?.data?.message || "Failed to delete job"
        );
      }
    },

    // Function to fetch all jobs
    getJobs: async () => {
      try {
        const response = await authAxios.get("/jobs");
        return response.data;
      } catch (error) {
        throw new Error(
          error.response?.data?.message || "Failed to fetch jobs"
        );
      }
    },

    // Function to update a job's details
    updateJob: async (jobId, jobData) => {
      try {
        const response = await authAxios.put(`/jobs/${jobId}`, jobData);
        return response.data;
      } catch (error) {
        throw new Error(
          error.response?.data?.message || "Failed to update job"
        );
      }
    },

    // Function to update the user's profile
    updateProfile: async (userData) => {
      try {
        const response = await authAxios.patch("/user/updateProfile", userData); // Send update request
        if (!response?.data) {
          throw new Error({ error: { response } }); // Throw an error if response is invalid
        }
        localStorage.setItem("user", JSON.stringify(response.data)); // Update stored user data
        return response.data; // Return response data
      } catch (error) {
        throw new Error(
          error.response?.data?.message || "Failed to update user profile"
        ); // Handle errors
      }
    },
  }; 
};
