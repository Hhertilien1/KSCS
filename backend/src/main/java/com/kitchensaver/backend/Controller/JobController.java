package com.kitchensaver.backend.Controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.kitchensaver.backend.DTO.JobRequest;
import com.kitchensaver.backend.DTO.JobResponse;
import com.kitchensaver.backend.Service.JobService;
import com.kitchensaver.backend.Service.UserService;
import com.kitchensaver.backend.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Marks this class as a REST controller for managing jobs
@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService jobService;  // Dependency injection for JobService to handle job-related logic
    private final UserService userService; // Dependency injection for UserService to handle job-related logic
    private static final Logger logger = LoggerFactory.getLogger(JobController.class);  // Logger for logging messages
    
        // Constructor to initialize the JobController with JobService
        public JobController(JobService jobService, UserService userService) {
            this.jobService = jobService;
            this.userService = userService;
    }


    // ADMIN ENDPOINTS
    // POST endpoint for creating a job; accessible only by users with the ADMIN role
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobResponse> createJob(@RequestBody JobRequest request) {
        try {
            JobResponse response = jobService.createJob(request);  // Create the job using the service
            return ResponseEntity.ok(response);  // Return the created job as a successful response
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new JobResponse(e.getMessage()));  // Handle exceptions and return error
        }
    }

    // PUT endpoint for updating a job; accessible only by users with the ADMIN role
    @PutMapping("/{jobId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobResponse> updateJob(@PathVariable Long jobId, @RequestBody JobRequest request) {
        try {
            JobResponse response = jobService.updateJob(jobId, request);  // Update the job using the service
            return ResponseEntity.ok(response);  // Return the updated job as a successful response
        } catch (Exception e) {
            return ResponseEntity.notFound().build();  // Handle case where job is not found
        }
    }

    // DELETE endpoint for deleting a job; accessible only by users with the ADMIN role
    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteJob(@PathVariable Long jobId) {
        try {
            jobService.deleteJob(jobId);  // Delete the job using the service
            return ResponseEntity.noContent().build();  // Return no content response on successful deletion
        } catch (Exception e) {
            return ResponseEntity.notFound().build();  // Handle case where job is not found
        }
    }

    // GET endpoint to retrieve all jobs; accessible to users with the roles ADMIN, CABINET_MAKER, or INSTALLER
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CABINET_MAKER', 'INSTALLER')")
    public ResponseEntity<List<JobResponse>> getAllJobs(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").replace("Bearer ", "");  // Extract JWT token from request header
        DecodedJWT decodedJWT = JwtUtil.verifyToken(token);  // Verify and decode the JWT token
        String role = decodedJWT.getClaim("role").asString();  // Get the role from the decoded JWT token
        Long userId = decodedJWT.getClaim("id").asLong();  // Get the user ID from the decoded JWT token

        logger.info("fine here :: 111" );  // Log for debugging purposes

        List<JobResponse> jobs;  // List to hold the job responses

        // Check the user's role and fetch jobs accordingly
        if ("ADMIN".equals(role)) {
            jobs = jobService.getAllJobs();  // Fetch all jobs for an admin
        } else if ("CABINET_MAKER".equals(role)) {
            jobs = jobService.getJobsByCabinetMakerId(userId);  // Fetch jobs assigned to the cabinet maker
        } else if ("INSTALLER".equals(role)) {
            jobs = jobService.getJobsByInstallerId(userId);  // Fetch jobs assigned to the installer
        } else {
            logger.info("not fine :: 2");  // Log an error if the role is not recognized
            return ResponseEntity.badRequest().build();  // Return bad request if the role is invalid
        }

        return ResponseEntity.ok(jobs);  // Return the list of jobs as a successful response
    }

    // INSTALLER ENDPOINTS
    // PATCH endpoint to update the status of a job; accessible to users with the roles ADMIN, CABINET_MAKER, or INSTALLER
    @PatchMapping("/{jobId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'CABINET_MAKER', 'INSTALLER')")
    public ResponseEntity<JobResponse> updateJobStatus(
            @PathVariable Long jobId,
            @RequestParam String status,
            @RequestParam String materialOrderStatus,
            @RequestParam String materialArrivalStatus) {
        try {
            JobResponse response = jobService.updateJobStatus(jobId, status, materialOrderStatus, materialArrivalStatus);  // Update the job status using the service
            return ResponseEntity.ok(response);  // Return the updated job as a successful response
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new JobResponse(e.getMessage()));  // Return error if the update fails
        }
    }


    // GET endpoint to filter jobs based on various parameters; accessible to users with the roles ADMIN, CABINET_MAKER, or INSTALLER
    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'CABINET_MAKER', 'INSTALLER')")
    public ResponseEntity<List<JobResponse>> filterJobs(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long installerId,
            @RequestParam(required = false) String materialOrderStatus,
            @RequestParam(required = false) String materialArrivalStatus,
            @RequestParam(required = false) String office) {
        return ResponseEntity
                .ok(jobService.filterJobs(status, installerId, materialOrderStatus, materialArrivalStatus, office));  // Return filtered list of jobs
    }
    
}
