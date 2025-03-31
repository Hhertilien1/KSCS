package com.kitchensaver.backend.model; // Defines the package where this class belongs

import jakarta.persistence.*; // Imports JPA (Jakarta Persistence API) annotations for ORM (Object-Relational Mapping)
import java.util.Date; // Imports the Date class for handling job due dates

// Marks this class as an entity, meaning it will be mapped to a database table
@Entity
public class Job {

    // Declares the primary key for the table and specifies that it is auto-generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    
    private String jobNumber; // Unique identifier for the job
    private String jobName; // Name or description of the job
    private int numCabinets; // Total number of cabinets in the job
    private int numUppers; // Number of upper (wall-mounted) cabinets
    private int numLowers; // Number of lower (base) cabinets

    // Many-to-One relationship with the Users entity (Cabinet Maker)
    @ManyToOne
    @JoinColumn(name = "cabinet_maker_id") // Foreign key column in the database
    private Users cabinetMaker;

    // Many-to-One relationship with the Users entity (Installer)
    @ManyToOne
    @JoinColumn(name = "installer_id") // Foreign key column in the database
    private Users installer;

    private Date dueDate; // The deadline for completing the job
    private String jobColor; // The color of the cabinets in this job
    private String office; // Office location responsible for this job
    private String status; // Current job status (e.g., Pending, In Progress, Completed)
    private String materialOrderStatus; // Status of material orders for this job
    private String materialArrivalStatus; // Status of material arrivals for this job
    private String image; // Image reference (file path or URL) related to the job

    // Getter Methods - Used to retrieve values of the private fields

    public Long getId() {
        return id;
    }

    public String getJobNumber() {
        return jobNumber;
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

    public Users getCabinetMaker() {
        return cabinetMaker;
    }

    public Users getInstaller() {
        return installer;
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

    public String getStatus() {
        return status;
    }

    public String getMaterialOrderStatus() {
        return materialOrderStatus;
    }

    public String getMaterialArrivalStatus() {
        return materialArrivalStatus;
    }

    public String getImage() {
        return image;
    }

    // Setter Methods - Used to modify values of the private fields

    public void setId(Long id) {
        this.id = id;
    }

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

    public void setCabinetMaker(Users cabinetMaker) {
        this.cabinetMaker = cabinetMaker;
    }

    public void setInstaller(Users installer) {
        this.installer = installer;
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

    public void setImage(String image) {
        this.image = image;
    }
}
