package com.kitchensaver.backend.Service; // defines the package location

import org.springframework.beans.factory.annotation.Value; // allows us to read value from application.properties
import org.springframework.stereotype.Service; // marks this class as a service in Spring
import org.springframework.web.multipart.MultipartFile; // used to handle uploaded files

import java.io.IOException; // handles input/output exceptions
import java.nio.file.Files; // used for file operations
import java.nio.file.Path; // represents a file path
import java.nio.file.Paths; // helps create Path objects
import java.nio.file.StandardCopyOption; // defines how files are copied

@Service // tells Spring to treat this class as a service
public class FileStorageService {

    @Value("${file.upload-dir}") // reads the file upload directory from application.properties
    private String uploadDir;

    // method to save the uploaded file
    public String storeFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir); // get the path to the upload directory

        // create the directory if it doesn't exist
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // create a unique file name by adding the current time and removing spaces
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll("\\s", "");
        Path filePath = uploadPath.resolve(fileName); // get the full path of the new file

        // copy the file to the target location (replace if it already exists)
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName; // return the new file name
    }
}
