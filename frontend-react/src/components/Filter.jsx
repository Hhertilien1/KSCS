import React from "react";

// Filter component for handling multiple filters such as office, installer name, and status
const Filter = ({ filters, handleFilterChange, clearFilters }) => {
  return (
    <div className="filter-container">
      <form
        id="filterForm"
        className="form-inline bg-light p-3 rounded shadow-sm"
      >
        {/* Filter by office selection */}
        <div className="form-group mb-2">
          <label htmlFor="office" className="mr-2 font-weight-bold">
            Filter by Office:
          </label>
          <select
            id="office"
            className="form-control form-control-sm"
            value={filters.office} // Controls the selected office filter value
            onChange={handleFilterChange} // Calls the handler function when the filter changes
          >
            <option value="">All Offices</option>
            <option value="400">400</option>
            <option value="402">402</option>
            <option value="403">403</option>
          </select>
        </div>

        {/* Filter by installer name input */}
        <div className="form-group mx-3 mb-2">
          <label htmlFor="installerName" className="mr-2 font-weight-bold">
            Filter by Installer:
          </label>
          <input
            type="text"
            id="installerName"
            className="form-control form-control-sm"
            value={filters.installerName} // Tracks the installer name filter value
            placeholder="Installer Name" // Placeholder text for the input field
            onChange={handleFilterChange} // Calls the handler function when the installer name changes
          />
        </div>

        {/* Filter by status selection */}
        <div className="form-group mb-2">
          <label htmlFor="status" className="mr-2 font-weight-bold">
            Filter by Status:
          </label>
          <select
            className="form-control form-control-sm"
            id="status"
            value={filters.status} // Tracks the selected status filter value
            onChange={handleFilterChange} // Calls the handler function when the filter changes
          >
            <option value="">All Statuses</option>
            <option value="To-Do">To-Do</option>
            <option value="In Progress">In Progress</option>
            <option value="Completed">Completed</option>
          </select>
        </div>

        {/* Button to clear all applied filters */}
        <div className="form-group mb-2">
          <br />
          <button
            type="button"
            className="btn btn-warning btn-sm"
            onClick={() =>{clearFilters()}} // Clears all filters when clicked
          >
            Clear Filters
          </button>
        </div>
      </form>
    </div>
  );
};

// OfficeFilter component is a simplified version of Filter for just filtering by office
export const OfficeFilter = ({ filters, handleFilterChange, clearFilters }) => {
  return (
    <div className="filter-container">
      <form
        id="filterForm"
        className="form-inline bg-light p-3 rounded shadow-sm"
      >
        {/* Filter by office selection */}
        <div className="form-group mb-2">
          <label htmlFor="office" className="mr-2 font-weight-bold">
            Filter by Office:
          </label>
          <select
            id="office"
            className="form-control form-control-sm"
            value={filters.office} // Controls the selected office filter value
            onChange={handleFilterChange} // Calls the handler function when the filter changes
          >
            <option value="">All Offices</option>
            <option value="400">400</option>
            <option value="402">402</option>
            <option value="403">403</option>
          </select>
        </div>

        {/* Button to clear the office filter */}
        <div className="form-group mb-2">
          <br />
          <button
            type="button"
            className="btn btn-warning btn-sm"
            onClick={clearFilters} // Clears the filter when clicked
          >
            Clear Filters
          </button>
        </div>
      </form>
    </div>
  );
};

export default Filter;
