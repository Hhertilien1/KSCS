package com.kitchensaver.backend.model;

import jakarta.persistence.*; // Import for JPA annotations
import java.util.Date; // Import for handling dates

// Marks this class as a JPA entity, meaning it will be mapped to a database table
@Entity
public class Job {
    
    // Primary key for the Job table, with auto-generated values
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Unique job number identifier
    private String jobNumber;
    
    // Name of the job
    private String jobName;
    
    // Number of cabinets in the job
    private int numCabinets;
    
    // Number of upper cabinets
    private int numUppers;
    
    // Number of lower cabinets
    private int numLowers;
    
    // Relationship to the Users entity for cabinet maker, linking via foreign key
    @ManyToOne
    @JoinColumn(name = "cabinet_maker_id")
    private Users cabinetMaker;
    
    // Relationship to the Users entity for installer, linking via foreign key
    @ManyToOne
    @JoinColumn(name = "installer_id")
    private Users installer;
    
    // Due date for the job completion
    private Date dueDate;
    
    // Color associated with the job
    private String jobColor;
    
    // Office location managing the job
    private String office;
    
    // Current status of the job (e.g., In Progress, Completed)
    private String status;
    
    // Status of the materials required for the job
    private String materialStatus;
    
}