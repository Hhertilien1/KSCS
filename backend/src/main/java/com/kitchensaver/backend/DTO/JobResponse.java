package com.kitchensaver.backend.DTO;

import java.util.Date;

public class JobResponse {
    // Fields to store job details
    private Long id;  // Unique identifier for the job
    private String jobNumber;  // Job number
    private String jobName;  // Name of the job
    private String status;  // Current status of the job
    private int numCabinets;  // Total number of cabinets for the job
    private int numUppers;  // Number of upper cabinets for the job
    private int numLowers;  // Number of lower cabinets for the job
    private Long installerId;  // ID of the installer assigned to the job
    private Long cabinetMakerId;  // ID of the cabinet maker assigned to the job
    private String materialOrderStatus;  // Status of material order (e.g., pending, completed)
    private String materialArrivalStatus;  // Status of material arrival (e.g., on time, delayed)
    private String cabinetMakerName;  // Name of the cabinet maker
    private String installerName;  // Name of the installer
    private Date dueDate;  // Due date for job completion
    private String office;  // Office location or department associated with the job
    private String jobColor;  // Color scheme for the job
    private String image;  // Image related to the job (could be a link to the image)

    // No-argument constructor
    public JobResponse() {
    }

    // Constructor to accept a message (currently not used in the code)
    public JobResponse(String message) {
    }

    // All-argument constructor to initialize all fields
    public JobResponse(Long id, String jobNumber, String jobName, String status, String materialOrderStatus, String materialArrivalStatus, String cabinetMakerName, String installerName, Date dueDate, String office, String jobColor, Long installerId, Long cabinetMakerId, 
     int numCabinets,
     int numUppers,
     int numLowers, String image) {
        this.id = id;
        this.jobNumber = jobNumber;
        this.jobName = jobName;
        this.status = status;
        this.materialOrderStatus = materialOrderStatus;
        this.materialArrivalStatus = materialArrivalStatus;
        this.installerId = installerId;
        this.cabinetMakerId = cabinetMakerId;
        this.materialArrivalStatus = materialArrivalStatus;
        this.cabinetMakerName = cabinetMakerName;
        this.installerName = installerName;
        this.dueDate = dueDate;
        this.office = office;
        this.numCabinets = numCabinets;
        this.numUppers = numUppers;
        this.numLowers = numLowers;
        this.jobColor = jobColor;
        this.image = image;
    }

    // Getters for each field to retrieve the values
    public Long getId() {
        return id;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public String getJobName() {
        return jobName;
    }

    public String getStatus() {
        return status;
    }

    public String getMaterialOrderStatus() {
        return materialOrderStatus;
    }

    public String getMaterialArrivalStatus() {
        return materialArrivalStatus;
    }

    public String getCabinetMakerName() {
        return cabinetMakerName;
    }

    public String getInstallerName() {
        return installerName;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public String getOffice() {
        return office;
    }

    public Long getInstallerId() {
        return installerId;
    }

    public Long getCabinetMakerId() {
        return cabinetMakerId;
    }
    
    public String getJobColor() {
        return jobColor;
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
    
    public String getImage() {
        return image;
    }

    // Setters for each field to set the values
    public void setId(Long id) {
        this.id = id;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
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

    public void setCabinetMakerName(String cabinetMakerName) {
        this.cabinetMakerName = cabinetMakerName;
    }

    public void setInstallerName(String installerName) {
        this.installerName = installerName;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public void setJobColor(String jobColor) {
        this.jobColor = jobColor;
    }

    public void setInstallerId(Long installerId) {
        this.installerId = installerId;
    }
    
    public void setCabinetMakerId(Long cabinetMakerId) {
        this.cabinetMakerId = cabinetMakerId;
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
    
    public void setImage(String image) {
        this.image = image;
    }

}
