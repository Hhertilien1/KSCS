import axios from "axios";

// Function that provides API service methods
export const apiService = () => {
  // Base URL for API from environment variables
  const API_BASE_URL = `${process.env.REACT_APP_API_BASE_URL}/api`;

  // Axios instance for public (non-authenticated) requests
  const publicAxios = axios.create({
    baseURL: API_BASE_URL,
    headers: { "Content-Type": "application/json" },
  });

  // Axios instance for authenticated requests
  const authAxios = axios.create({
    baseURL: API_BASE_URL,
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${localStorage.getItem("authToken")}`,
    },
  });

  // Interceptor for handling unauthorized responses (401)
  authAxios.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response?.status === 401) {
        // Handle unauthorized error (you can redirect or clear storage if needed)
        // localStorage.clear();
        // window.location = "/login";
      }
      return Promise.reject(error);
    }
  );

  // Return all API functions
  return {
    // Login user
    login: async (payload) => {
      try {
        const response = await publicAxios.post("/user/login", payload);
        if (!response?.data?.token) {
          throw new Error({ error: { response } });
        }
        // Save token and user data to localStorage
        localStorage.setItem("authToken", response.data.token);
        localStorage.setItem("user", JSON.stringify(response.data.user));
        return response.data;
      } catch (error) {
        throw new Error(error.response?.data?.message || "Login failed");
      }
    },

    // Delete user by ID
    deleteUser: async (userId) => {
      try {
        await authAxios.delete(`/user/delete/${userId}`);
      } catch (error) {
        throw new Error(
          error.response?.data?.message || "Failed to delete user"
        );
      }
    },

    // Create a new employee
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

    // Update employee details
    updateEmployee: async (employeeData) => {
      try {
        const response = await authAxios.patch("/user/updateEmployee", employeeData);
        if (!response?.data) {
          throw new Error({ error: { response } });
        }
        return response.data;
      } catch (error) {
        throw new Error(
          error.response?.data?.message || "Failed to update employee"
        );
      }
    },

    // Get all employees
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

    // Register a new user
    register: async (userData) => {
      try {
        const response = await publicAxios.post("/user/register", userData);
        if (!response?.data?.token) {
          throw new Error({ error: { response } });
        }
        // Save token and user data to localStorage
        localStorage.setItem("authToken", response.data.token);
        localStorage.setItem("user", JSON.stringify(response.data.user));
        return response.data;
      } catch (error) {
        throw new Error(error.response?.data?.message || "Registration failed");
      }
    },

    // Create a new job
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

    // Delete a job by ID
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

    // Get all jobs
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

    // Update job by ID
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

    // Upload image for a job
    uploadJobImage: async (jobId, imageFile) => {
      try {
        const formData = new FormData();
        formData.append('file', imageFile);

        const response = await authAxios.post(`/upload`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });

        if (response.data) {
          // Call updateJobImage after uploading
          await apiService().updateJobImage(jobId, response.data);
        }

        return response.data;
      } catch (error) {
        throw new Error(
          error.response?.data?.message || "Failed to upload job image"
        );
      }
    },

    // Update image URL for a job
    updateJobImage: async (jobId, imageUrl) => {
      try {
        const response = await authAxios.post(`/jobs/${jobId}/uploadImage`, {imageUrl});

        return response.data;
      } catch (error) {
        throw new Error(
          error.response?.data?.message || "Failed to upload job image"
        );
      }
    },

    // Return image URL for display (example placeholder)
    getJobImage:  (imageUrl) => {
      try {
        return `${API_BASE_URL}/files/1741617713456_a_unique_logo_for_a_platform_called(1).jpeg`
      } catch (error) {
        return ""
      }
    },

    // Get current user info
    getSelf: async () => {
      try {
        const response = await authAxios.get("/user/getSelf");
        return response.data;
      } catch (error) {
        throw new Error(
          error.response?.data?.message || "Failed to fetch user data"
        );
      }
    },

    // Update user profile
    updateProfile: async (userData) => {
      try {
        const response = await authAxios.patch("/user/updateProfile", userData);
        if (!response?.data) {
          throw new Error({ error: { response } });
        }
        // Update user info in localStorage
        localStorage.setItem("user", JSON.stringify(response.data));
        return response.data;
      } catch (error) {
        throw new Error(
          error.response?.data?.message || "Failed to update user profile"
        );
      }
    },
  };
};
