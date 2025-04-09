package com.kitchensaver.backend.Controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.kitchensaver.backend.DTO.JobImageRequest;
import com.kitchensaver.backend.DTO.JobRequest;
import com.kitchensaver.backend.DTO.JobResponse;
import com.kitchensaver.backend.Service.JobService;
import com.kitchensaver.backend.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marks this class as a REST controller
@RequestMapping("/api/jobs") // Base URL mapping for all job-related endpoints
public class JobController {
    private final JobService jobService; // Service layer for job operations
    private static final Logger logger = LoggerFactory.getLogger(JobController.class); // Logger for debugging and monitoring

    // Constructor injection for JobService
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // ADMIN ENDPOINTS

    // Endpoint to create a new job, accessible only by ADMIN
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Restricts access to users with ADMIN role
    public ResponseEntity<JobResponse> createJob(@RequestBody JobRequest request) {
        try {
            JobResponse response = jobService.createJob(request); // Calls service to create job
            return ResponseEntity.ok(response); // Returns success response
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new JobResponse(e.getMessage())); // Returns error response
        }
    }

    // Endpoint to update an existing job by ID, accessible only by ADMIN
    @PutMapping("/{jobId}")
    @PreAuthorize("hasRole('ADMIN')") // Restricts access to users with ADMIN role
    public ResponseEntity<JobResponse> updateJob(@PathVariable Long jobId, @RequestBody JobRequest request) {
        try {
            JobResponse response = jobService.updateJob(jobId, request); // Calls service to update job
            return ResponseEntity.ok(response); // Returns success response
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); // Returns 404 if job not found
        }
    }

    // Endpoint to delete a job by ID, accessible only by ADMIN
    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasRole('ADMIN')") // Restricts access to users with ADMIN role
    public ResponseEntity<Void> deleteJob(@PathVariable Long jobId) {
        try {
            jobService.deleteJob(jobId); // Calls service to delete job
            return ResponseEntity.noContent().build(); // Returns 204 No Content on success
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); // Returns 404 if job not found
        }
    }

    // Endpoint to get all jobs based on user role
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CABINET_MAKER', 'INSTALLER')") // Allows access to all listed roles
    public ResponseEntity<List<JobResponse>> getAllJobs(HttpServletRequest httpServletRequest) {
        // Extracts JWT token from Authorization header
        String token = httpServletRequest.getHeader("Authorization").replace("Bearer ", "");
        DecodedJWT decodedJWT = JwtUtil.verifyToken(token); // Verifies and decodes token
        String role = decodedJWT.getClaim("role").asString(); // Gets role from token
        Long userId = decodedJWT.getClaim("id").asLong(); // Gets user ID from token
        logger.info("fine here :: 111" );

        List<JobResponse> jobs;
        if ("ADMIN".equals(role)) {
            jobs = jobService.getAllJobs(); // Admin gets all jobs
        } else if ("CABINET_MAKER".equals(role)) {
            jobs = jobService.getJobsByCabinetMakerId(userId); // Cabinet Maker gets their jobs
        } else if ("INSTALLER".equals(role)) {
            jobs = jobService.getJobsByInstallerId(userId); // Installer gets their jobs
        } else {
            logger.info("not fine :: 2");
            return ResponseEntity.badRequest().build(); // Returns 400 if role is invalid
        }
        return ResponseEntity.ok(jobs); // Returns list of jobs
    }

    // INSTALLER ENDPOINTS

    // Endpoint to update the status of a job, accessible by all listed roles
    @PatchMapping("/{jobId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'CABINET_MAKER', 'INSTALLER')") // Allows access to all listed roles
    public ResponseEntity<JobResponse> updateJobStatus(
            @PathVariable Long jobId,
            @RequestParam String status,
            @RequestParam String materialOrderStatus,
            @RequestParam String materialArrivalStatus) {
        try {
            JobResponse response = jobService.updateJobStatus(jobId, status, materialOrderStatus,
                    materialArrivalStatus); // Calls service to update job status
            return ResponseEntity.ok(response); // Returns updated job response
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new JobResponse(e.getMessage())); // Returns error message
        }
    }

    // Endpoint to upload an image to a job, accessible by all listed roles
    @PostMapping("/{jobId}/uploadImage")
    @PreAuthorize("hasAnyRole('ADMIN', 'CABINET_MAKER', 'INSTALLER')") // Allows access to all listed roles
    public ResponseEntity<JobResponse> uploadJobImage(@PathVariable Long jobId, @RequestBody JobImageRequest request) {
        try {
            JobResponse response = jobService.updateJobImage(jobId, request.getImageUrl()); // Calls service to update image
            return ResponseEntity.ok(response); // Returns success response
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new JobResponse(e.getMessage())); // Returns error message
        }
    }

    // Endpoint to filter jobs based on query parameters, accessible by all listed roles
    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'CABINET_MAKER', 'INSTALLER')") // Allows access to all listed roles
    public ResponseEntity<List<JobResponse>> filterJobs(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long installerId,
            @RequestParam(required = false) String materialOrderStatus,
            @RequestParam(required = false) String materialArrivalStatus,
            @RequestParam(required = false) String office) {
        return ResponseEntity
                .ok(jobService.filterJobs(status, installerId, materialOrderStatus, materialArrivalStatus, office)); // Calls service to filter jobs
    }
}
