import React, { useState } from "react";

import Filter from "../components/Filter";
import Footer from "../components/Footer";
import JobsTable from "../components/JobsTable";
import { Link } from "react-router-dom";
import { NavigationHeader } from "../components/NavigationHeader";

const JobDashboard = () => {
 

  const [filters, setFilters] = useState({
    office: "",
    installerName: "",
    status: "",
  });


  const clearFilters = () => {
    debugger
    setFilters({ office: "", installer: "", status: "" });
  };





  return (
    <div>
      {/* Navigation */}
      <NavigationHeader showLogout={true} />

      <header>
        <h1 className="dashboard-headline">Manage Your Jobs Efficiently</h1>
        <h2 className="dashboard-subheadline">
          Explore, filter, and update job statuses seamlessly from one
          centralized dashboard.
        </h2>
      </header>
      <h1 className="dashboard-headline">Kitchen Saver Cabinets</h1>
      <div className="wrapper jobs-container">
        <Filter
          filters={filters}
          handleFilterChange={(e) =>
            setFilters({ ...filters, [e.target.id]: e.target.value })
          }
          clearFilters={clearFilters}
        />

        <JobsTable filters={filters} key={JSON.stringify(filters)} />
      </div>

      <Footer />
    </div>
  );
};

export default JobDashboard;
