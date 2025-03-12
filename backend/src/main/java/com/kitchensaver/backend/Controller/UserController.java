package com.kitchensaver.backend.Controller;

import com.kitchensaver.backend.DTO.CreateEmployeeRequest;
import com.kitchensaver.backend.DTO.LoginRequest;
import com.kitchensaver.backend.DTO.UserRequest;
import com.kitchensaver.backend.DTO.UserResponse;
import com.kitchensaver.backend.Repo.UserRepo;
import com.kitchensaver.backend.Service.UserService;


import jakarta.servlet.http.HttpServletRequest;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;

// This class is a controller that handles user-related actions
@RestController
@RequestMapping("/api/user") // all requests to "/api/user" will come here
public class UserController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // Constructor-based dependency injection
    public UserController(UserService userService, UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userService = userService;
    }

    /**
     * This method is for user registration.
     *
     * @param request The user details sent from the frontend.
     * @return A message saying if registration was successful.
     */
    @PostMapping("/register") // This handles the POST request to "/api/user/register"
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest request) {
        // Calls the service to register the user and returns a response message

        try {
            UserResponse response = userService.registerUser(request);
            return ResponseEntity.ok(response); // Sends back the result of the registration
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserResponse(e.getMessage(), ""));
        }
    }

    /**
     * This method is for user login.
     *
     * @param request The login details (email & password) sent from the frontend.
     * @return A response with login details (like a token).
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@RequestBody LoginRequest request) {
        // Looks for the user by their email
        try {
            UserResponse response = userService.loginUser(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserResponse(e.getMessage(), ""));
        }
    }

    /**
     * This method is for user login.
     *
     * @param request The login details (email & password) sent from the frontend.
     * @return A response with login details (like a token).
     */
    @PostMapping("/createEmployee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createEmployee(@RequestBody CreateEmployeeRequest request) {
        try {
            UserResponse response = userService.registerUser(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserResponse(e.getMessage(), ""));
        }
    }

 
    @PatchMapping("/updateProfile")
    public ResponseEntity<UserResponse> updateProfile(@RequestBody UserRequest request,
            HttpServletRequest httpServletRequest) {
        // Calls the service to update the user and returns a response message
        try {
            UserResponse response = userService.updateProfile(request, httpServletRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserResponse(e.getMessage(), ""));
        }
    }

  

    @GetMapping("/getSelf")
    public ResponseEntity<UserResponse> getSelf(HttpServletRequest httpServletRequest) {
        // Calls the service to get the user and returns a response message
        String email = userService.getEmailFromToken(httpServletRequest);
        try {
            UserResponse response = userService.getSelf(email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserResponse(e.getMessage(), ""));
        }
    }

}
