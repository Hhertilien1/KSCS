import React, { useState } from "react";

import Filter from "../components/Filter";
import Footer from "../components/Footer";
import { NavigationHeader } from "../components/NavigationHeader";
import Table from "../components/JobsTable";

const AdminDashboard = () => {
  const [filters, setFilters] = useState({
    office: "",
    installerName: "",
    status: "",
  });

  const clearFilters = () => {
    setFilters({ office: "", installerName: "", status: "" });
  };

  return (
    <div>
      <NavigationHeader showLogout />
      <div className="wrapper">
        <Filter
          filters={filters}
          handleFilterChange={(e) =>
            setFilters({ ...filters, [e.target.id]: e.target.value })
          }
          clearFilters={clearFilters}
        />

        <Table filters={filters} />
      </div>

      <Footer />
    </div>
  );
};
export default AdminDashboard;
