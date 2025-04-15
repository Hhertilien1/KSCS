import React, { useEffect, useState } from "react";
import { apiService } from "../services/apiService";
import { useUser } from "../hooks/useUser"; // import the hook


function JobsTable({ filters }) {
  // State hooks to manage job data, filtered jobs, loading, and error messages
  const [jobs, setJobs] = useState([]);
  const [filteredJobs, setFilteredJobs] = useState([]); // To store filtered jobs
  const [loading, setLoading] = useState(true); // To manage loading state
  const [error, setError] = useState(""); // To handle error messages
  const { user } = useUser(); // Correct usage of the custom hook


 // Inside the deleteJob function
const deleteJob = async (id) => {
  // Check if the user is an admin
  if (user && user.role?.toLowerCase() === "admin") {
    const confirmed = window.confirm("Are you sure you want to delete this job?");
    if (confirmed) {
      await apiService().deleteJob(id); // Delete the job using the API service
      await fetchJobs(); // Refresh the job list after deletion
    }
  } else {
    alert("You do not have permission to delete this job.");
  }
};


  // Function to apply filters to the jobs list
  const applyFilters = (filters) => {
    const filteredJobs = jobs.filter((job) => {
      const officeMatch =
        filters.office === "" || job.office === filters.office;
      const installerMatch =
        filters.installerName === "" ||
        job.installerName
          .toLowerCase()
          .includes(filters.installerName.toLowerCase());
      const statusMatch =
        filters.status === "" || job.status === filters.status;

      // Return jobs that match all the filter criteria
      return officeMatch && installerMatch && statusMatch;
    });
    return filteredJobs;
  };

  // Function to update the status of a job
  const updateStatus = async (index, newStatus) => {
    const updatedJobs = [...jobs]; // Create a copy of jobs array
    const oldJob = updatedJobs[index]; // Get the job to update
    const body = { ...oldJob, status: newStatus }; // Create the updated job object

    try {
      await apiService().updateJob(oldJob.id, body); // Update the job status in the API
      fetchJobs(); // Refresh the job list after status update
    } catch (error) {
      console.error(error);
      setError("Something went wrong");
    }
    setJobs(updatedJobs); // Update the state with the modified jobs
  };

  // Function to update the material ordered status of a job
  const updateMaterialOrdered = async (index, newStatus) => {
    const jobToUpdate = { ...jobs[index] }; // Copy the job to update
    jobToUpdate.materialOrderStatus =
      newStatus === "ordered" ? "ordered" : null; // Update material order status
    jobToUpdate.materialArrivalStatus =
      newStatus === "ordered" ? jobToUpdate.materialArrivalStatus : null; // Reset arrival status if not ordered
    try {
      await apiService().updateJob(jobToUpdate.id, jobToUpdate); // Update the job with the new material status
      fetchJobs(); // Refresh the job list after updating
    } catch (error) {
      console.error(error);
      setError("Something went wrong");
    }
  };

  // Function to update the material arrival status of a job
  const updateMaterialArrived = async (index, newStatus) => {
    const jobToUpdate = { ...jobs[index] }; // Copy the job to update

    // Check if material is ordered before marking it as arrived
    if (jobToUpdate.materialOrderStatus !== "ordered") {
      setError("Can't set material as arrived if not ordered");
      return;
    } else {
      jobToUpdate.materialArrivalStatus =
        newStatus === "arrived" ? "arrived" : null; // Update material arrival status

      try {
        await apiService().updateJob(jobToUpdate.id, jobToUpdate); // Update the job with the new material arrival status
        fetchJobs(); // Refresh the job list after updating
      } catch (error) {
        console.error(error);
        setError("Something went wrong");
      }
    }
  };

  // Function to handle file upload for a job
  const handleFileUpload = async (index, event) => {
    const file = event.target.files[0]; // Get the selected file
    const allowedTypes = ["image/png", "image/jpeg", "image/svg+xml"]; // Allowed image types

    // Check if the file type is allowed
    if (!allowedTypes.includes(file.type)) {
      setError("Only png, jpg and svg images are allowed");
      return;
    }

    try {
      const response = await apiService().uploadJobImage(jobs[index].id, file); // Upload the file using the API
      const updatedJobs = [...jobs]; // Copy the jobs array
      updatedJobs[index].image = response.data; // Add the uploaded image URL to the job
      setJobs(updatedJobs); // Update the state with the new job data
    } catch (error) {
      console.error(error);
      setError("Something went wrong");
    }
  };

  // Function to fetch jobs from the API
  const fetchJobs = async () => {
    setLoading(true); // Set loading state to true
    try {
      const response = await apiService().getJobs(); // Get jobs from the API
      setJobs(response); // Update the state with fetched jobs
    } catch (error) {
      console.error("Error during login:", error);
      setError(error.message); // Handle errors and show error message
    } finally {
      setLoading(false); // Set loading state to false
    }
  };

  // useEffect hook to fetch jobs when the component mounts
  useEffect(() => {
    fetchJobs();
  }, []);

  // useEffect hook to apply filters whenever filters change
  useEffect(() => {
    if (
      Object.keys(filters).filter((item) => filters[item] !== "").length === 0
    ) {
      setJobs(jobs); // If no filters are applied, show all jobs
    } else {
      setFilteredJobs(applyFilters(filters)); // Apply the filters to the jobs
    }
  }, [filters]);

  // useEffect hook to clear the error message after 3 seconds
  useEffect(() => {
    const timer = setTimeout(() => setError(""), 3000);
    return () => clearTimeout(timer); // Clear the timeout when the component unmounts or error changes
  }, [error]);

  // Function to render the jobs table rows
  const renderTable = (data) => {
    return data.map((job, index) => {
      // Determine the CSS class based on job status
      let statusClass = "";
      if (job.status === "To-Do") statusClass = "status-to-do";
      else if (job.status === "In Progress") statusClass = "status-in-progress";
      else if (job.status === "Completed") statusClass = "status-completed";

      // Determine the CSS classes for material ordered and material arrived statuses
      const materialsOrderedClass =
        job.materialOrderStatus === "ordered" ? "status-yes" : "status-no";
      const materialsArrivedClass =
        job.materialArrivalStatus === "arrived" ? "status-yes" : "status-no";

      return (
        <tr key={job.jobName}>
          <td>{job.jobNumber}</td>
          <td>{job.jobName}</td>
          <td>{job.numCabinets}</td>
          <td>{job.numUppers}</td>
          <td>{job.numLowers}</td>
          <td>{job.cabinetMakerName}</td>
          <td>{job.installerName}</td>
          <td>
            {new Date(job.dueDate).toLocaleDateString("en-US", {
              year: "numeric",
              month: "long",
              day: "numeric",
            })}
          </td>
          <td>{job.jobColor}</td>
          <td>{job.office}</td>
          <td>
            <select
              value={job.status}
              onChange={(e) => updateStatus(index, e.target.value)}
              className={`${statusClass} form-control form-control-sm`}
            >
              <option value="To-Do">To-Do</option>
              <option value="In Progress">In Progress</option>
              <option value="Completed">Completed</option>
            </select>
          </td>
          
          <td>
            <select
              value={job.materialOrderStatus}
              onChange={(e) => updateMaterialOrdered(index, e.target.value)}
              className={`form-control form-control-sm ${
                job.materialOrderStatus === "ordered"
                  ? "text-success"
                  : "text-danger"
              }`}
            >
              <option value="">No</option>
              <option value="ordered">Yes</option>
            </select>
          </td>

          <td>
            <select
              value={job.materialArrivalStatus}
              onChange={(e) => updateMaterialArrived(index, e.target.value)}
              className={`form-control form-control-sm ${
                job.materialArrivalStatus === "arrived"
                  ? "text-success"
                  : "text-danger"
              }`}
            >
              <option value="">No</option>
              <option value="arrived">Yes</option>
            </select>
          </td>
          <td>
            <input
              type="file"
              className="form-control form-control-sm"
              onChange={(e) => handleFileUpload(index, e)}
            />
            {job.image && (
              <a
                href={`${process.env.REACT_APP_API_BASE_URL}/api/files/${job.image}`}
                target="_blank"
                rel="noopener noreferrer"
              >
                View Photo
              </a>
            )}
          </td>
          <td>
            {user?.role?.toLowerCase() === "admin" && (
            <button onClick={() => deleteJob(job.id)}>Delete</button>
            )}
          </td>
        </tr>
      );
    });
  };

  return loading ? (
    <div className="skeleton-loader">
      {/* Skeleton loader for the table rows while loading */}
      <div className="skeleton-row">
        <div className="skeleton-cell"></div>
        <div className="skeleton-cell"></div>
        <div className="skeleton-cell"></div>
      </div>
      <div className="skeleton-row">
        <div className="skeleton-cell"></div>
        <div className="skeleton-cell"></div>
        <div className="skeleton-cell"></div>
      </div>
      <div className="skeleton-row">
        <div className="skeleton-cell"></div>
        <div className="skeleton-cell"></div>
        <div className="skeleton-cell"></div>
      </div>
    </div>
  ) : (
    <div>
      {/* Error message display */}
      {<div className="error-message">{error}</div>}

      <table>
        <thead>
          <tr>
            <th>Job #</th>
            <th>Job Name</th>
            <th># of Cabinets</th>
            <th># of Uppers</th>
            <th># of Lowers</th>
            <th>Cabinet Maker</th>
            <th>Installer</th>
            <th>Due Date</th>
            <th>Job Color</th>
            <th>Office</th>
            <th>Status</th>
            <th>Materials Ordered</th>
            <th>Materials Arrived</th>
            <th>Photos</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {/* Render the table with either jobs or filteredJobs */}
          {renderTable(
            Object.keys(filters).filter((item) => filters[item] !== "")
              .length === 0
              ? jobs
              : filteredJobs
          )}
        </tbody>
      </table>
    </div>
  );
}

export default JobsTable;
