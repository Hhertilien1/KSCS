package com.kitchensaver.backend.Service;

import com.kitchensaver.backend.DTO.JobRequest;
import com.kitchensaver.backend.DTO.JobResponse;
import com.kitchensaver.backend.Exceptions.NotFoundException;
import com.kitchensaver.backend.Repo.JobRepo;
import com.kitchensaver.backend.Repo.UserRepo;
import com.kitchensaver.backend.model.Job;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // Marks this class as a service component in Spring
public class JobService {
    private final JobRepo jobRepo; // Repository to manage Job entities
    private final UserRepo userRepo; // Repository to manage User entities

    // Constructor to inject JobRepo and UserRepo
    public JobService(JobRepo jobRepo, UserRepo userRepo) {
        this.jobRepo = jobRepo;
        this.userRepo = userRepo;
    }

    // Method to create a new job
    public JobResponse createJob(JobRequest request) throws Exception {
        Job job = new Job(); // Create new Job object
        mapRequestToEntity(request, job); // Map request data to Job entity
        return mapEntityToResponse(jobRepo.save(job)); // Save job and return response
    }

    // Method to update an existing job
    public JobResponse updateJob(Long jobId, JobRequest request) throws Exception {
        Job job = jobRepo.findById(jobId) // Find job by ID
                .orElseThrow(() -> new NotFoundException("Job not found")); // Throw error if not found
        mapRequestToEntity(request, job); // Update job fields
        return mapEntityToResponse(jobRepo.save(job)); // Save and return updated job
    }

    // Method to update only the status fields of a job
    public JobResponse updateJobStatus(Long jobId, String status, String materialOrderStatus, String materialArrivalStatus) throws Exception {
        Job job = jobRepo.findById(jobId) // Find job by ID
                .orElseThrow(() -> new NotFoundException("Job not found")); // Throw error if not found
        job.setStatus(status); // Set job status
        job.setMaterialOrderStatus(materialOrderStatus); // Set material order status
        job.setMaterialArrivalStatus(materialArrivalStatus); // Set material arrival status
        return mapEntityToResponse(jobRepo.save(job)); // Save and return updated job
    }

    // Method to delete a job by ID
    public void deleteJob(Long jobId) throws Exception {
        Job job = jobRepo.findById(jobId) // Find job by ID
                .orElseThrow(() -> new NotFoundException("Job not found")); // Throw error if not found
        jobRepo.delete(job); // Delete the job
    }

    // Method to update a job's image
    public JobResponse updateJobImage(Long jobId, String image) throws Exception {
        Job job = jobRepo.findById(jobId) // Find job by ID
                .orElseThrow(() -> new NotFoundException("Job not found")); // Throw error if not found
        job.setImage(image); // Set the job image
        return mapEntityToResponse(jobRepo.save(job)); // Save and return updated job
    }

    // Helper method to map request data to a Job entity
    private void mapRequestToEntity(JobRequest request, Job job) throws Exception {
        job.setJobNumber(request.getJobNumber()); // Set job number
        job.setJobName(request.getJobName()); // Set job name
        job.setNumCabinets(request.getNumCabinets()); // Set number of cabinets
        job.setNumUppers(request.getNumUppers()); // Set number of upper cabinets
        job.setNumLowers(request.getNumLowers()); // Set number of lower cabinets
        job.setCabinetMaker(userRepo.findById(request.getCabinetMakerId()) // Find cabinet maker by ID
                .orElseThrow(() -> new NotFoundException("Cabinet maker not found"))); // Error if not found
        job.setInstaller(userRepo.findById(request.getInstallerId()) // Find installer by ID
                .orElseThrow(() -> new NotFoundException("Installer not found"))); // Error if not found
        job.setDueDate(request.getDueDate()); // Set job due date
        job.setJobColor(request.getJobColor()); // Set job color
        job.setOffice(request.getOffice()); // Set office
        job.setStatus(request.getStatus()); // Set status
        job.setMaterialOrderStatus(request.getMaterialOrderStatus()); // Set material order status
        job.setMaterialArrivalStatus(request.getMaterialArrivalStatus()); // Set material arrival status
    }

    // Helper method to map Job entity data to a response object
    private JobResponse mapEntityToResponse(Job job) {
        JobResponse response = new JobResponse(); // Create response object
        response.setId(job.getId()); // Set job ID
        response.setJobNumber(job.getJobNumber()); // Set job number
        response.setInstallerId(job.getInstaller().getId()); // Set installer ID
        response.setCabinetMakerId(job.getCabinetMaker().getId()); // Set cabinet maker ID
        response.setJobNumber(job.getJobNumber()); // Set job number
        response.setJobName(job.getJobName()); // Set job name
        response.setStatus(job.getStatus()); // Set status
        response.setNumCabinets(job.getNumCabinets()); // Set number of cabinets
        response.setNumLowers(job.getNumLowers()); // Set number of lower cabinets
        response.setNumUppers(job.getNumUppers()); // Set number of upper cabinets
        response.setMaterialOrderStatus(job.getMaterialOrderStatus()); // Set material order status
        response.setMaterialArrivalStatus(job.getMaterialArrivalStatus()); // Set material arrival status
        response.setCabinetMakerName(job.getCabinetMaker().getFirstName() + " " + job.getCabinetMaker().getLastName()); // Set cabinet maker full name
        response.setInstallerName(job.getInstaller().getFirstName() + " " + job.getInstaller().getLastName()); // Set installer full name
        response.setDueDate(job.getDueDate()); // Set due date
        response.setOffice(job.getOffice()); // Set office
        response.setJobColor(job.getJobColor()); // Set job color
        response.setImage(job.getImage()); // Set image
        return response; // Return response
    }

    // Method to get all jobs from the database
    public List<JobResponse> getAllJobs() {
        return jobRepo.findAll().stream() // Fetch all jobs
                .map(this::mapEntityToResponse) // Map each to response
                .collect(Collectors.toList()); // Return as list
    }

    // Method to filter jobs based on status, installer, material order/arrival status, and office
    public List<JobResponse> filterJobs(String status, Long installerId, String materialOrderStatus, String materialArrivalStatus, String office) {
        return jobRepo.findByFilters(status, installerId, materialOrderStatus, materialArrivalStatus, office).stream() // Fetch filtered jobs
                .map(this::mapEntityToResponse) // Map each to response
                .collect(Collectors.toList()); // Return as list
    }

    // Method to get jobs by cabinet maker ID
    public List<JobResponse> getJobsByCabinetMakerId(Long id) {
        return jobRepo.findByCabinetMakerId(id).stream() // Fetch jobs by cabinet maker ID
                .map(this::mapEntityToResponse) // Map each to response
                .collect(Collectors.toList()); // Return as list
    }

    // Method to get jobs by installer ID
    public List<JobResponse> getJobsByInstallerId(Long id) {
        return jobRepo.findByInstallerId(id).stream() // Fetch jobs by installer ID
                .map(this::mapEntityToResponse) // Map each to response
                .collect(Collectors.toList()); // Return as list
    }
}
