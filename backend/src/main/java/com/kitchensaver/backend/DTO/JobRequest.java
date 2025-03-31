package com.kitchensaver.backend.DTO;

import java.util.Date;

public class JobRequest {
    // Fields to store job request details
    private String jobNumber;  // Job number
    private String jobName;  // Name of the job
    private int numCabinets;  // Total number of cabinets requested
    private int numUppers;  // Number of upper cabinets requested
    private int numLowers;  // Number of lower cabinets requested
    private Long cabinetMakerId;  // ID of the cabinet maker for the job
    private Long installerId;  // ID of the installer for the job
    private Date dueDate;  // Due date for the job completion
    private String jobColor;  // Color scheme for the job
    private String office;  // Office location or department associated with the job
    private String status;  // Current status of the job request
    private String materialOrderStatus;  // Status of the material order (e.g., pending, ordered)
    private String materialArrivalStatus;  // Status of the material arrival (e.g., on time, delayed)

    // Getters for each field to retrieve the values
    public String getJobNumber() {
        return jobNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getJobName() {
        return jobName;
    }

    public int getNumCabinets() {
        return numCabinets;
    }

    public int getNumUppers() {
        return numUppers;
    }

    public int getNumLowers() {
        return numLowers;
    }

    public Long getCabinetMakerId() {
        return cabinetMakerId;
    }

    public Long getInstallerId() {
        return installerId;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public String getJobColor() {
        return jobColor;
    }

    public String getOffice() {
        return office;
    }

    public String getMaterialOrderStatus() {
        return materialOrderStatus;
    }

    public String getMaterialArrivalStatus() {
        return materialArrivalStatus;
    }

    // Setters for each field to set the values
    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setNumCabinets(int numCabinets) {
        this.numCabinets = numCabinets;
    }

    public void setNumUppers(int numUppers) {
        this.numUppers = numUppers;
    }

    public void setNumLowers(int numLowers) {
        this.numLowers = numLowers;
    }

    public void setCabinetMakerId(Long cabinetMakerId) {
        this.cabinetMakerId = cabinetMakerId;
    }

    public void setInstallerId(Long installerId) {
        this.installerId = installerId;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setJobColor(String jobColor) {
        this.jobColor = jobColor;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMaterialOrderStatus(String materialOrderStatus) {
        this.materialOrderStatus = materialOrderStatus;
    }

    public void setMaterialArrivalStatus(String materialArrivalStatus) {
        this.materialArrivalStatus = materialArrivalStatus;
    }

}
