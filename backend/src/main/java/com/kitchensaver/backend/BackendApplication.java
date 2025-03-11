package com.kitchensaver.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// This annotation marks the class as the main entry point for a Spring Boot application.
@SpringBootApplication
public class BackendApplication {

    // This is the main method which is the entry point to run the Spring Boot application.
    public static void main(String[] args) {
        // This line runs the Spring Boot application, using BackendApplication as the main class.
        SpringApplication.run(BackendApplication.class, args);
    }

}
