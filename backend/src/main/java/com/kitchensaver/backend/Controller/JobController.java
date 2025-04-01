package com.kitchensaver.backend.Controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.kitchensaver.backend.DTO.JobRequest;
import com.kitchensaver.backend.DTO.JobResponse;
import com.kitchensaver.backend.Service.JobService;
import com.kitchensaver.backend.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * JobController handles HTTP requests related to job management.
 * It includes endpoints for creating, updating, deleting, retrieving, and filtering jobs.
 */
@RestController
@RequestMapping("/api/jobs")
public class JobController {

    // Service layer dependency for handling job-related business logic
    private final JobService jobService;

    // Logger instance for logging events and debugging information
    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    // Constructor to inject JobService dependency
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // ADMIN ENDPOINTS

    /**
     * Creates a new job. Only accessible by users with ADMIN role.
     * 
     * @param request Job request data
     * @return ResponseEntity with JobResponse or error message
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobResponse> createJob(@RequestBody JobRequest request) {
        try {
            JobResponse response = jobService.createJob(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new JobResponse(e.getMessage()));
        }
    }

    /**
     * Updates an existing job. Only accessible by users with ADMIN role.
     * 
     * @param jobId   ID of the job to update
     * @param request Job request data with updated values
     * @return ResponseEntity with updated JobResponse or not found status
     */
    @PutMapping("/{jobId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobResponse> updateJob(@PathVariable Long jobId, @RequestBody JobRequest request) {
        try {
            JobResponse response = jobService.updateJob(jobId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a job by ID. Only accessible by users with ADMIN role.
     * 
     * @param jobId ID of the job to delete
     * @return ResponseEntity with no content or not found status
     */
    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteJob(@PathVariable Long jobId) {
        try {
            jobService.deleteJob(jobId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves all jobs based on user role.
     * - ADMIN: Retrieves all jobs
     * - CABINET_MAKER: Retrieves jobs assigned to the cabinet maker
     * - INSTALLER: Retrieves jobs assigned to the installer
     * 
     * @param httpServletRequest HTTP request containing authentication token
     * @return ResponseEntity with a list of jobs or bad request status
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CABINET_MAKER', 'INSTALLER')")
    public ResponseEntity<List<JobResponse>> getAllJobs(HttpServletRequest httpServletRequest) {
        // Extract the Authorization token from the request header
        String token = httpServletRequest.getHeader("Authorization").replace("Bearer ", "");
        
        // Decode the JWT token to extract user role and ID
        DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
        String role = decodedJWT.getClaim("role").asString();
        Long userId = decodedJWT.getClaim("id").asLong();

        logger.info("fine here :: 111");
        
        // Retrieve jobs based on user role
        List<JobResponse> jobs;
        if ("ADMIN".equals(role)) {
            jobs = jobService.getAllJobs();
        } else if ("CABINET_MAKER".equals(role)) {
            jobs = jobService.getJobsByCabinetMakerId(userId);
        } else if ("INSTALLER".equals(role)) {
            jobs = jobService.getJobsByInstallerId(userId);
        } else {
            logger.info("not fine :: 2");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(jobs);
    }

    // INSTALLER ENDPOINTS

    /**
     * Updates job status including material order and arrival status.
     * Accessible by ADMIN, CABINET_MAKER, and INSTALLER roles.
     * 
     * @param jobId               ID of the job
     * @param status              New job status
     * @param materialOrderStatus Status of material order
     * @param materialArrivalStatus Status of material arrival
     * @return ResponseEntity with updated job details or error message
     */
    @PatchMapping("/{jobId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'CABINET_MAKER', 'INSTALLER')")
    public ResponseEntity<JobResponse> updateJobStatus(
            @PathVariable Long jobId,
            @RequestParam String status,
            @RequestParam String materialOrderStatus,
            @RequestParam String materialArrivalStatus) {
        try {
            JobResponse response = jobService.updateJobStatus(jobId, status, materialOrderStatus, materialArrivalStatus);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new JobResponse(e.getMessage()));
        }
    }

   

    /**
     * Filters jobs based on different criteria such as status, installer ID,
     * material order status, material arrival status, and office location.
     * Accessible by ADMIN, CABINET_MAKER, and INSTALLER roles.
     * 
     * @param status              Job status filter
     * @param installerId         Installer ID filter
     * @param materialOrderStatus Material order status filter
     * @param materialArrivalStatus Material arrival status filter
     * @param office              Office location filter
     * @return ResponseEntity with a filtered list of jobs
     */
    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'CABINET_MAKER', 'INSTALLER')")
    public ResponseEntity<List<JobResponse>> filterJobs(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long installerId,
            @RequestParam(required = false) String materialOrderStatus,
            @RequestParam(required = false) String materialArrivalStatus,
            @RequestParam(required = false) String office) {
        return ResponseEntity.ok(jobService.filterJobs(status, installerId, materialOrderStatus, materialArrivalStatus, office));
    }
}
