package com.kitchensaver.backend.Service;

import com.kitchensaver.backend.DTO.JobRequest;
import com.kitchensaver.backend.DTO.JobResponse;
import com.kitchensaver.backend.Exceptions.NotFoundException;
import com.kitchensaver.backend.Repo.JobRepo;
import com.kitchensaver.backend.Repo.UserRepo;
import com.kitchensaver.backend.model.Job;
import com.kitchensaver.backend.model.Role;
import com.kitchensaver.backend.model.Users;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {
    // Repositories for interacting with the database
    private final JobRepo jobRepo;  // Repository for Job entities
    private final UserRepo userRepo;  // Repository for User entities (cabinet maker, installer)

    // Constructor for dependency injection
    public JobService(JobRepo jobRepo, UserRepo userRepo) {
        this.jobRepo = jobRepo;
        this.userRepo = userRepo;
    }

    // Method to create a new job request
    public JobResponse createJob(JobRequest request) throws Exception {
        Job job = new Job();  // Create a new Job entity
        mapRequestToEntity(request, job);  // Map the request data to the Job entity
        return mapEntityToResponse(jobRepo.save(job));  // Save the job and map the entity to a response DTO
    }

    // Method to update an existing job's details
    public JobResponse updateJob(Long jobId, JobRequest request) throws Exception {
        // Find the job by ID, throw exception if not found
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new NotFoundException("Job not found"));
        mapRequestToEntity(request, job);  // Map the updated request data to the existing Job entity
        return mapEntityToResponse(jobRepo.save(job));  // Save and return the updated job response
    }

    // Method to update the job's status and material order/arrival statuses
    public JobResponse updateJobStatus(Long jobId, String status, String materialOrderStatus, String materialArrivalStatus) throws Exception {
        // Find the job by ID, throw exception if not found
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new NotFoundException("Job not found"));
        // Set the updated status information
        job.setStatus(status);
        job.setMaterialOrderStatus(materialOrderStatus);
        job.setMaterialArrivalStatus(materialArrivalStatus);
        return mapEntityToResponse(jobRepo.save(job));  // Save and return the updated job response
    }

    // Method to delete a job from the system
    public void deleteJob(Long jobId) throws Exception {
        // Find the job by ID, throw exception if not found
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new NotFoundException("Job not found"));
        jobRepo.delete(job);  // Delete the found job
    }


    // Helper method to map JobRequest data to a Job entity
    private void mapRequestToEntity(JobRequest request, Job job) throws Exception {
        job.setJobNumber(request.getJobNumber());  // Set the job number
        job.setJobName(request.getJobName());  // Set the job name
        job.setNumCabinets(request.getNumCabinets());  // Set the number of cabinets
        job.setNumUppers(request.getNumUppers());  // Set the number of upper cabinets
        job.setNumLowers(request.getNumLowers());  // Set the number of lower cabinets
        // Set the cabinet maker by fetching it from the user repository
        job.setCabinetMaker(userRepo.findById(request.getCabinetMakerId())
                .orElseThrow(() -> new NotFoundException("Cabinet maker not found")));
        // Set the installer by fetching it from the user repository
        job.setInstaller(userRepo.findById(request.getInstallerId())
                .orElseThrow(() -> new NotFoundException("Installer not found")));
        job.setDueDate(request.getDueDate());  // Set the due date for the job
        job.setJobColor(request.getJobColor());  // Set the job color
        job.setOffice(request.getOffice());  // Set the office location
        job.setStatus(request.getStatus());  // Set the job status
        job.setMaterialOrderStatus(request.getMaterialOrderStatus());  // Set the material order status
        job.setMaterialArrivalStatus(request.getMaterialArrivalStatus());  // Set the material arrival status
    }

    // Helper method to map Job entity to JobResponse DTO
    private JobResponse mapEntityToResponse(Job job) {
        JobResponse response = new JobResponse();  // Create a new JobResponse DTO
        response.setId(job.getId());  // Set job ID
        response.setJobNumber(job.getJobNumber());  // Set job number
        response.setInstallerId(job.getInstaller().getId());  // Set installer ID
        response.setCabinetMakerId(job.getCabinetMaker().getId());  // Set cabinet maker ID
        response.setJobNumber(job.getJobNumber());  // Set job number again (duplicate in original code, may need review)
        response.setJobName(job.getJobName());  // Set job name
        response.setStatus(job.getStatus());  // Set job status
        response.setNumCabinets(job.getNumCabinets());  // Set number of cabinets
        response.setNumLowers(job.getNumLowers());  // Set number of lower cabinets
        response.setNumUppers(job.getNumUppers());  // Set number of upper cabinets
        response.setMaterialOrderStatus(job.getMaterialOrderStatus());  // Set material order status
        response.setMaterialArrivalStatus(job.getMaterialArrivalStatus());  // Set material arrival status
        // Set the cabinet maker name by combining first and last names
        response.setCabinetMakerName(job.getCabinetMaker().getFirstName() + " " + job.getCabinetMaker().getLastName());
        // Set the installer name by combining first and last names
        response.setInstallerName(job.getInstaller().getFirstName() + " " + job.getInstaller().getLastName());
        response.setDueDate(job.getDueDate());  // Set due date
        response.setOffice(job.getOffice());  // Set office location
        response.setJobColor(job.getJobColor());  // Set job color
        response.setImage(job.getImage());  // Set job image
        return response;  // Return the populated JobResponse DTO
    }

    // Method to get a list of all jobs
    public List<JobResponse> getAllJobs() {
        return jobRepo.findAll().stream()  // Fetch all jobs from the repository
                .map(this::mapEntityToResponse)  // Convert each Job entity to JobResponse DTO
                .collect(Collectors.toList());  // Collect and return the list of JobResponse DTOs
    }

    // Method to filter jobs based on various criteria
    public List<JobResponse> filterJobs(String status, Long installerId, String materialOrderStatus, String materialArrivalStatus, String office) {
        return jobRepo.findByFilters(status, installerId, materialOrderStatus, materialArrivalStatus, office).stream()  // Apply filters to fetch jobs
                .map(this::mapEntityToResponse)  // Convert each filtered Job entity to JobResponse DTO
                .collect(Collectors.toList());  // Collect and return the filtered list of JobResponse DTOs
    }

    // Method to get jobs by cabinet maker ID
    public List<JobResponse> getJobsByCabinetMakerId(Long id) {
        return jobRepo.findByCabinetMakerId(id).stream()  // Fetch jobs by cabinet maker ID
                .map(this::mapEntityToResponse)  // Convert each Job entity to JobResponse DTO
                .collect(Collectors.toList());  // Collect and return the list of JobResponse DTOs
    }

    // Method to get jobs by installer ID
    public List<JobResponse> getJobsByInstallerId(Long id) {
        return jobRepo.findByInstallerId(id).stream()  // Fetch jobs by installer ID
                .map(this::mapEntityToResponse)  // Convert each Job entity to JobResponse DTO
                .collect(Collectors.toList());  // Collect and return the list of JobResponse DTOs
    }

}
