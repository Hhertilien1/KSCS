package com.kitchensaver.backend.Controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import com.kitchensaver.backend.Service.FileStorageService;

// Controller for handling file storage operations
@RestController
@RequestMapping("/api")
public class FileStorageController {

    // Path where uploaded files will be stored (configured in application.properties)
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    // Service to handle file storage operations
    @Autowired
    private FileStorageService fileStorageService;

    // Logger for logging information and errors
    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    // Endpoint for serving files based on filename
    @GetMapping("/files/{filename}")
    public ResponseEntity<byte[]> serveFile(@PathVariable String filename) {
        try {
            // Log the filename being accessed
            logger.info("here::: " + filename);
            
            // Resolve the file path based on the uploaded directory and the filename
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            
            // Create a resource from the file path
            Resource resource = new UrlResource(filePath.toUri());

            // Check if the resource exists and is readable, then return the file as a response
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(Files.readAllBytes(filePath));
            } else {
                // If the file does not exist or is not readable, return a 404 response
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // If an error occurs, return a 500 internal server error
            return ResponseEntity.internalServerError().build();
        }
    }

    // Endpoint for uploading files
    @PostMapping("/upload")
    // Restrict access to users with specific roles: ADMIN, CABINET_MAKER, or INSTALLER
    @PreAuthorize("hasAnyRole('ADMIN', 'CABINET_MAKER', 'INSTALLER')")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Store the file using the file storage service
            String fileName = fileStorageService.storeFile(file);
            
            // Return the file access URL
            String fileDownloadUri = fileName;
            return ResponseEntity.ok(fileDownloadUri);
        } catch (Exception e) {
            // If an error occurs during file upload, return a bad request response with an error message
            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
        }
    }
}
