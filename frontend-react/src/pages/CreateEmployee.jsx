import React, { useEffect, useState } from "react";

import Footer from "../components/Footer";
import { Link } from "react-router-dom";
import { NavigationHeader } from "../components/NavigationHeader";
import { OfficeFilter } from "../components/Filter";
import { apiService } from "../services/apiService";

const CreateEmployee = () => {
  const [employees, setEmployees] = useState([]);
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    username: "",
    email: "",
    cell: "",
    role: "",
    office: "",
    password: "",
  });

  const [filters, setFilters] = useState({
    office: "",
  });
  const [error, setError] = useState("");
  const [editIndex, setEditIndex] = useState(null);
  const fetchAllEmployees = async () => {
    try {
      const response = await apiService().getAllEmployees();
      setEmployees(response);
    } catch (error) {
      setError(error.message);
    }
  };
  useEffect(() => {
    fetchAllEmployees();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const addEmployee = async () => {
    const {
      firstName,
      lastName,
      username,
      email,
      cell,
      role,
      office,
      password,
    } = formData;
    setError("");

    // Validation
    if (
      !firstName ||
      !lastName ||
      !username ||
      !email ||
      !cell ||
      !role ||
      !office ||
      !password
    ) {
      setError("All fields are required.");
      return;
    }

    const duplicate = employees.some(
      (emp) => emp.username === username || emp.email === email
    );
    if (duplicate) {
      setError("An employee with the same username or email already exists.");
      return;
    }

    const newEmployee = {
      firstName,
      lastName,
      username,
      email,
      cell,
      role,
      office,
      ...(password
        ? {
            password,
            confirmPassword: password,
          }
        : {}),
    };

    try {
      await apiService().createEmployee(newEmployee);
      fetchAllEmployees();

      setEmployees([...employees, newEmployee]);
      clearForm();
    } catch (error) {
      setError(error.message);
    }
  };

  const updateEmployee = async () => {
    const {
      firstName,
      lastName,
      username,
      email,
      cell,
      role,
      office,
      password,
    } = formData;

    await apiService().updateEmployee({
      id: employees[editIndex].id,
      ...formData,
      password,
      confirmPassword: password,
    });
    const updatedEmployees = [...employees];
    updatedEmployees[editIndex] = {
      id: employees[editIndex].id,
      firstName,
      lastName,
      username,
      email,
      cell,
      role,
      office,
      password,
    };
    setEmployees(updatedEmployees);
    fetchAllEmployees();

    setEditIndex(null);
    clearForm();
  };

  const clearForm = () => {
    setFormData({
      firstName: "",
      lastName: "",
      username: "",
      email: "",
      cell: "",
      role: "",
      office: "",
    });
    setError("");
  };

  const deleteEmployee = async (id) => {
    if (window.confirm("Are you sure you want to delete this employee?")) {
      try {
        await apiService().deleteUser(id);
        fetchAllEmployees();
      } catch (error) {
        setError(error.message);
      }
    }
  };

  const editEmployee = (index) => {
    setFormData({ ...employees[index], password: "" });
    setEditIndex(index);
  };

  const filterEmployees = () => {
    return employees.filter((employee) =>
      filters.office ? employee.office == filters.office : true
    );
  };

  const clearFilters = () => {
    setFilters({ office: "" });
  };

  return (
    <>
      <header>
        <NavigationHeader showLogout />
        <h1 className="dashboard-headline">Effortlessly Build Your Team</h1>
        <h2 className="dashboard-subheadline">
          Add, manage, and organize employees seamlessly with our intuitive
          interface.
        </h2>
      </header>

      <div className="wrapper">
        <div className="login-container create-employee-container">
          <form id="employeeForm">
            <div className="p-1">
              <div className="form-group">
                <label htmlFor="firstName">First Name: </label>
                <input
                  type="text"
                  id="firstName"
                  name="firstName"
                  placeholder="Enter your First Name"
                  className="form-control form-control-sm mb-1"
                  value={formData.firstName}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="lastName">Last Name: </label>
                <input
                  type="text"
                  id="lastName"
                  className="form-control form-control-sm mb-1"
                  placeholder="Enter your Last Name"
                  name="lastName"
                  value={formData.lastName}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="username">Username: </label>
                <input
                  type="text"
                  id="username"
                  placeholder="Enter your Username"
                  className="form-control form-control-sm mb-1"
                  name="username"
                  value={formData.username}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="email">Email: </label>
                <input
                  type="email"
                  id="email"
                  className="form-control form-control-sm mb-1"
                  placeholder="Enter your Email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="cell">Cell #: </label>
                <input
                  type="text"
                  id="cell"
                  placeholder="Enter your Phone Number"
                  name="cell"
                  className="form-control form-control-sm mb-1"
                  value={formData.cell}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="password">Password: </label>
                <input
                  type="text"
                  id="password"
                  className="form-control form-control-sm mb-1"
                  placeholder="Enter password for user"
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="role">Role: </label>
                <select
                  id="role"
                  name="role"
                  className="form-control form-control-sm mb-1"
                  value={formData.role}
                  onChange={handleChange}
                  required
                >
                  <option value="">Select Role</option>
                  <option value="CABINET_MAKER">Cabinet Maker</option>
                  <option value="INSTALLER">Installer</option>
                </select>
              </div>
              <div className="form-group">
                <label htmlFor="office">Office: </label>
                <select
                  id="office"
                  name="office"
                  className="form-control form-control-sm mb-1"
                  value={formData.office}
                  onChange={handleChange}
                  required
                >
                  <option value="">Select Office</option>

                  <option value="400">400</option>
                  <option value="402">402</option>
                  <option value="403">403</option>
                </select>
              </div>

              <div className="form-actions">
                <div class="mt-24 d-flex justify-content-between align-items-center">
                  {editIndex === null ? (
                    <button type="button" onClick={addEmployee}>
                      Add Employee
                    </button>
                  ) : (
                    <button type="button" onClick={updateEmployee}>
                      Update Employee
                    </button>
                  )}{" "}
                  <button type="button" onClick={clearForm}>
                    Clear Form
                  </button>
                </div>
              </div>
              {error && <p className="error">{error}</p>}
            </div>
          </form>
        </div>
      </div>

      <div className="p-8 center">
        <OfficeFilter
          filters={filters}
          handleFilterChange={(e) =>
            setFilters({ ...filters, [e.target.id]: e.target.value })
          }
          clearFilters={clearFilters}
        />
        <table>
          <thead>
            <tr>
              <th>First Name</th>
              <th>Last Name</th>
              <th>Username</th>
              <th>Email</th>
              <th>Cell</th>
              <th>Role</th>
              <th>Office</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filterEmployees().map((employee, index) => (
              <tr key={index}>
                <td>{employee.firstName}</td>
                <td>{employee.lastName}</td>
                <td>{employee.username}</td>
                <td>{employee.email}</td>
                <td>{employee.cell}</td>
                <td>{employee.role}</td>
                <td>{employee.office}</td>
                <td>
                  <button onClick={() => editEmployee(index)}>Edit</button>
                  <button onClick={() => deleteEmployee(employee.id)}>
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <Footer />
    </>
  );
};

export default CreateEmployee;