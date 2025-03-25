package com.kitchensaver.backend.DTO;

import java.util.Date;

// This class represents a job response that holds information about a job.
public class JobResponse {
    // Unique ID of the job
    private Long id;
    // Job number (identifier for the job)
    private String jobNumber;
    // Name of the job
    private String jobName;
    // Current status of the job (e.g., In Progress, Completed)
    private String status;
    // Status of the materials required for the job
    private String materialStatus;
    // Name of the cabinet maker assigned to the job
    private String cabinetMakerName;
    // Name of the installer assigned to the job
    private String installerName;
    // Due date of the job
    private Date dueDate;
    // Office location associated with the job
    private String office;
    // Color associated with the job (could be for visual representation)
    private String jobColor;

    // Default constructor (used when no specific details are provided)
    public JobResponse() {
    }

    // Constructor with a message (not being used in this code)
    public JobResponse(String message) {
    }

    // Constructor with all details to create a JobResponse object
    public JobResponse(Long id, String jobNumber, String jobName, String status, String materialStatus, String cabinetMakerName, String installerName, Date dueDate, String office, String jobColor) {
        this.id = id;
        this.jobNumber = jobNumber;
        this.jobName = jobName;
        this.status = status;
        this.materialStatus = materialStatus;
        this.cabinetMakerName = cabinetMakerName;
        this.installerName = installerName;
        this.dueDate = dueDate;
        this.office = office;
        this.jobColor = jobColor;
    }

    // Getter methods (used to retrieve values)

    public Long getId() { // Get job ID
        return id;
    }

    public String getJobNumber() { // Get job number
        return jobNumber;
    }

    public String getJobName() { // Get job name
        return jobName;
    }

    public String getStatus() { // Get job status
        return status;
    }

    public String getMaterialStatus() { // Get material status
        return materialStatus;
    }

    public String getCabinetMakerName() { // Get cabinet maker's name
        return cabinetMakerName;
    }

    public String getInstallerName() { // Get installer's name
        return installerName;
    }

    public Date getDueDate() { // Get due date
        return dueDate;
    }

    public String getOffice() { // Get office location
        return office;
    }

    public String getJobColor() { // Get job color
        return jobColor;
    }

    // Setter methods (used to update values)

    public void setId(Long id) { // Set job ID
        this.id = id;
    }

    public void setJobNumber(String jobNumber) { // Set job number
        this.jobNumber = jobNumber;
    }

    public void setJobName(String jobName) { // Set job name
        this.jobName = jobName;
    }

    public void setStatus(String status) { // Set job status
        this.status = status;
    }

    public void setMaterialStatus(String materialStatus) { // Set material status
        this.materialStatus = materialStatus;
    }

    public void setCabinetMakerName(String cabinetMakerName) { // Set cabinet maker's name
        this.cabinetMakerName = cabinetMakerName;
    }

    public void setInstallerName(String installerName) { // Set installer's name
        this.installerName = installerName;
    }

    public void setDueDate(Date dueDate) { // Set due date
        this.dueDate = dueDate;
    }

    public void setOffice(String office) { // Set office location
        this.office = office;
    }

    public void setJobColor(String jobColor) { // Set job color
        this.jobColor = jobColor;
    }
}
