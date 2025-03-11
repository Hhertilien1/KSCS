import React, { useEffect, useState } from "react";

import Footer from "../components/Footer";
import { Link } from "react-router-dom";
import { NavigationHeader } from "../components/NavigationHeader";
import { apiService } from "../services/apiService";

// Initial job data

function CreateJob() {
  const [jobs, setJobs] = useState([]);
  const [filters, setFilters] = useState({
    office: "",
    installer: "",
    status: "",
  });
  const [form, setForm] = useState({
    jobNumber: "",
    jobName: "",
    numCabinets: "",
    numUppers: "",
    numLowers: "",
    cabinetMakerId: "",
    installerId: "",
    dueDate: "",
    jobColor: "",
    office: "",
    status: "",
  });
  const [message, setMessage] = useState("");
  const [cabinetsMakers, setCabinetMakers] = useState([]);
  const [installers, setInstallers] = useState([]);

  useEffect(() => {
    const fetchEmployees = async () => {
      try {
        const employees = await apiService().getAllEmployees();
        setCabinetMakers(
          employees.filter((emp) => emp.role === "CABINET_MAKER")
        );
        setInstallers(employees.filter((emp) => emp.role === "INSTALLER"));
      } catch (error) {
        setMessage(error.message);
      }
    };
    fetchEmployees();
  }, []);

  const handleFormChange = (e) => {
    const { id, value } = e.target;
    setForm((prevForm) => ({
      ...prevForm,
      [id]: value,
    }));
  };

  const addJob = async () => {
    if (
      Object.values(form).some((value) => value === "") ||
      form.jobNumber === ""
    ) {
      setMessage("All fields are required.");
      return;
    }
    try {
      await apiService().createJob(form);
      setMessage("Job Added Successfully!");
      setJobs([...jobs, form]);
      setForm({
        jobNumber: "",
        jobName: "",
        numCabinets: "",
        numUppers: "",
        numLowers: "",
        cabinetMakerId: "",
        installerId: "",
        dueDate: "",
        jobColor: "",
        office: "",
        status: "",
      });
    } catch (error) {
      setMessage(error.message);
    }
  };

  useEffect(() => {
    const timer = setTimeout(() => setMessage(""), 3000);
    return () => clearTimeout(timer);
  }, [message]);
  return (
    <div>
      <NavigationHeader showLogout />

      <div className="wrapper">
        <header>
          <h1>Create Jobs - Organize Your Projects Seamlessly</h1>
          <h2>
            Input job details, track progress, and manage tasks all in one place
          </h2>
        </header>
        <div className="wrapper">
          <div className="container login-container">
            {/* Job Form */}
            <div className="p-2">
              <form id="jobForm">
                <div className="form-group">
                  <label htmlFor="jobNumber" className="col-form-label">
                    Job #
                  </label>
                  <input
                    type="number"
                    id="jobNumber"
                    className="form-control form-control-sm mb-1"
                    placeholder="Job #"
                    value={form.jobNumber}
                    onChange={handleFormChange}
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="jobName" className="col-form-label">
                    Job Name
                  </label>
                  <input
                    type="text"
                    id="jobName"
                    className="form-control form-control-sm mb-1"
                    placeholder="Job Name"
                    value={form.jobName}
                    onChange={handleFormChange}
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="numCabinets" className="col-form-label">
                    # of Cabinets
                  </label>
                  <input
                    type="number"
                    id="numCabinets"
                    className="form-control form-control-sm mb-1"
                    placeholder="# of Cabinets"
                    value={form.numCabinets}
                    onChange={handleFormChange}
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="numUppers" className="col-form-label">
                    # of Uppers
                  </label>
                  <input
                    type="number"
                    id="numUppers"
                    className="form-control form-control-sm mb-1"
                    placeholder="# of Uppers"
                    value={form.numUppers}
                    onChange={handleFormChange}
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="numLowers" className="col-form-label">
                    # of Lowers
                  </label>
                  <input
                    type="number"
                    id="numLowers"
                    className="form-control form-control-sm mb-1"
                    placeholder="# of Lowers"
                    value={form.numLowers}
                    onChange={handleFormChange}
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="cabinetMakerId" className="col-form-label">
                    Cabinet Maker
                  </label>
                  <select
                    id="cabinetMakerId"
                    className="form-control form-control-sm mb-1"
                    value={form.cabinetMakerId}
                    onChange={handleFormChange}
                  >
                    <option value="">Select Cabinet Maker</option>
                    {cabinetsMakers.map((maker) => (
                      <option key={maker.id} value={maker.id}>
                        {maker.firstName} {maker.lastName}
                      </option>
                    ))}
                  </select>
                </div>

                <div className="form-group">
                  <label htmlFor="installerId" className="col-form-label">
                    Installer
                  </label>
                  <select
                    id="installerId"
                    className="form-control form-control-sm mb-1"
                    value={form.installerId}
                    onChange={handleFormChange}
                  >
                    <option value="">Select Installer</option>
                    {installers.map((installer) => (
                      <option key={installer.id} value={installer.id}>
                        {installer.firstName} {installer.lastName}
                      </option>
                    ))}
                  </select>
                </div>

                <div className="form-group">
                  <label htmlFor="dueDate" className=" col-form-label">
                    Due Date
                  </label>
                  <input
                    type="date"
                    id="dueDate"
                    className="form-control form-control-sm mb-1"
                    value={form.dueDate}
                    onChange={handleFormChange}
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="jobColor" className=" col-form-label">
                    Job Color
                  </label>
                  <input
                    type="text"
                    id="jobColor"
                    className="form-control form-control-sm mb-1"
                    placeholder="Job Color"
                    value={form.jobColor}
                    onChange={handleFormChange}
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="office" className="col-form-label">
                    Office
                  </label>
                  <select
                    id="office"
                    className="form-control form-control-sm mb-1"
                    value={form.office}
                    onChange={handleFormChange}
                  >
                    <option value="">Select Office</option>
                    <option value="400">400</option>
                    <option value="402">402</option>
                    <option value="403">403</option>
                  </select>
                </div>
                <div className="form-group">
                  <label htmlFor="status" className="col-form-label">
                    Status
                  </label>
                  <select
                    id="status"
                    className="form-control form-control-sm mb-1"
                    value={form.status}
                    onChange={handleFormChange}
                  >
                    <option value="">Select Status</option>
                    <option value="To-Do">To-Do</option>
                    <option value="In Progress">In Progress</option>
                    <option value="Completed">Completed</option>
                  </select>
                </div>
              </form>
              <div style={{height:24}}/>
              <button type="button mt-16" onClick={addJob}>
                Add Job
              </button>
              <p className="error-message mt-24 center">{message}</p>
            </div>
          </div>
        </div>
        {/* Feedback Message */}
      </div>
      <Footer />
    </div>
  );
}

export default CreateJob;
