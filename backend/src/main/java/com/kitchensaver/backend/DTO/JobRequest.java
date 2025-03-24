package com.kitchensaver.backend.DTO;  // Defines the package where this class belongs

import java.util.Date;  // Imports the Date class for handling due dates

/**
 * The JobRequest class represents a request for a job, including details
 * about the job itself, the number of cabinets, assigned personnel, and other relevant attributes.
 */
public class JobRequest {
    private String jobNumber;  // Unique identifier for the job
    private String jobName;  // Name of the job/project
    private int numCabinets;  // Total number of cabinets in the job
    private int numUppers;  // Number of upper (wall-mounted) cabinets
    private int numLowers;  // Number of lower (base) cabinets
    private Long cabinetMakerId;  // ID of the cabinet maker assigned to this job
    private Long installerId;  // ID of the installer responsible for installation
    private Date dueDate;  // Due date for the job completion
    private String jobColor;  // Color specification for the job
    private String office;  // Office location or department handling the job

    // Getters - Methods to retrieve the values of private fields

    /**
     * Gets the job number.
     * @return job number as a string
     */
    public String getJobNumber() {
        return jobNumber;
    }

    /**
     * Gets the job name.
     * @return job name as a string
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * Gets the total number of cabinets.
     * @return number of cabinets as an integer
     */
    public int getNumCabinets() {
        return numCabinets;
    }

    /**
     * Gets the number of upper cabinets.
     * @return number of upper cabinets as an integer
     */
    public int getNumUppers() {
        return numUppers;
    }

    /**
     * Gets the number of lower cabinets.
     * @return number of lower cabinets as an integer
     */
    public int getNumLowers() {
        return numLowers;
    }

    /**
     * Gets the ID of the cabinet maker.
     * @return cabinet maker ID as a Long
     */
    public Long getCabinetMakerId() {
        return cabinetMakerId;
    }

    /**
     * Gets the ID of the installer.
     * @return installer ID as a Long
     */
    public Long getInstallerId() {
        return installerId;
    }

    /**
     * Gets the due date of the job.
     * @return due date as a Date object
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * Gets the job color.
     * @return job color as a string
     */
    public String getJobColor() {
        return jobColor;
    }

    /**
     * Gets the office location associated with the job.
     * @return office location as a string
     */
    public String getOffice() {
        return office;
    }

    // Setters - Methods to update the values of private fields

    /**
     * Sets the job number.
     * @param jobNumber the job number to set
     */
    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    /**
     * Sets the job name.
     * @param jobName the job name to set
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * Sets the total number of cabinets.
     * @param numCabinets the number of cabinets to set
     */
    public void setNumCabinets(int numCabinets) {
        this.numCabinets = numCabinets;
    }

    /**
     * Sets the number of upper cabinets.
     * @param numUppers the number of upper cabinets to set
     */
    public void setNumUppers(int numUppers) {
        this.numUppers = numUppers;
    }

    /**
     * Sets the number of lower cabinets.
     * @param numLowers the number of lower cabinets to set
     */
    public void setNumLowers(int numLowers) {
        this.numLowers = numLowers;
    }

    /**
     * Sets the cabinet maker's ID.
     * @param cabinetMakerId the cabinet maker ID to set
     */
    public void setCabinetMakerId(Long cabinetMakerId) {
        this.cabinetMakerId = cabinetMakerId;
    }

    /**
     * Sets the installer's ID.
     * @param installerId the installer ID to set
     */
    public void setInstallerId(Long installerId) {
        this.installerId = installerId;
    }

    /**
     * Sets the due date for the job.
     * @param dueDate the due date to set
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Sets the job color.
     * @param jobColor the job color to set
     */
    public void setJobColor(String jobColor) {
        this.jobColor = jobColor;
    }

    /**
     * Sets the office location associated with the job.
     * @param office the office location to set
     */
    public void setOffice(String office) {
        this.office = office;
    }
}
