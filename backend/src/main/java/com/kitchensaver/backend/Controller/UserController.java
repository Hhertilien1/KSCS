package com.kitchensaver.backend.Controller;

import com.kitchensaver.backend.DTO.CreateEmployeeRequest;
import com.kitchensaver.backend.DTO.LoginRequest;
import com.kitchensaver.backend.DTO.UserRequest;
import com.kitchensaver.backend.DTO.UserResponse;
import com.kitchensaver.backend.Repo.UserRepo;
import com.kitchensaver.backend.Service.UserService;
import com.kitchensaver.backend.model.Users;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling user-related actions.
 */
@RestController
@RequestMapping("/api/user") // All API requests with "/api/user" will be handled here
public class UserController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // Constructor-based dependency injection
    public UserController(UserService userService, UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userService = userService;
    }

    /**
     * Handles user registration.
     *
     * @param request Contains user details from the frontend.
     * @return Response with registration success or failure message.
     */
    @PostMapping("/register") // Handles POST requests to "/api/user/register"
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest request) {
        try {
            UserResponse response = userService.registerUser(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserResponse(e.getMessage(), ""));
        }
    }

    /**
     * Handles user login.
     *
     * @param request Contains login credentials (email & password).
     * @return Response containing login details (e.g., authentication token).
     */
    @PostMapping("/login") // Handles POST requests to "/api/user/login"
    public ResponseEntity<UserResponse> loginUser(@RequestBody LoginRequest request) {
        try {
            UserResponse response = userService.loginUser(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserResponse(e.getMessage(), ""));
        }
    }

    /**
     * Handles employee creation (Admin only).
     *
     * @param request Contains details of the employee to be created.
     * @return Response confirming the employee creation.
     */
    @PostMapping("/createEmployee") // Handles POST requests to "/api/user/createEmployee"
    @PreAuthorize("hasRole('ADMIN')") // Only ADMIN users can access this endpoint
    public ResponseEntity<UserResponse> createEmployee(@RequestBody CreateEmployeeRequest request) {
        try {
            UserResponse response = userService.registerUser(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserResponse(e.getMessage(), ""));
        }
    }

    /**
     * Handles user deletion (Admin only).
     *
     * @param userId ID of the user to be deleted.
     * @return Response confirming the deletion of the user.
     */
    @DeleteMapping("/delete/{userId}") // Handles DELETE requests to "/api/user/delete/{userId}"
    @PreAuthorize("hasRole('ADMIN')") // Only ADMIN users can access this endpoint
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Fetches all employees (Admin only).
     *
     * @return List of all employees.
     */
    @GetMapping("/getAllEmployees") // Handles GET requests to "/api/user/getAllEmployees"
    @PreAuthorize("hasAnyRole('ADMIN')") // Only ADMIN users can access this endpoint
    public ResponseEntity<List<Users>> getAllEmployees() {
        try {
            List<Users> response = userService.getAllEmployees();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Updates the profile of the currently logged-in user.
     *
     * @param request             Contains updated user details.
     * @param httpServletRequest  The HTTP request containing authentication details.
     * @return Response with updated profile information.
     */
    @PatchMapping("/updateProfile") // Handles PATCH requests to "/api/user/updateProfile"
    public ResponseEntity<UserResponse> updateProfile(@RequestBody UserRequest request,
            HttpServletRequest httpServletRequest) {
        try {
            UserResponse response = userService.updateProfile(request, httpServletRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserResponse(e.getMessage(), ""));
        }
    }

    /**
     * Updates an employee's details (Admin only).
     *
     * @param request             Contains updated employee details.
     * @param httpServletRequest  The HTTP request containing authentication details.
     * @return Response with updated employee information.
     */
    @PatchMapping("/updateEmployee") // Handles PATCH requests to "/api/user/updateEmployee"
    @PreAuthorize("hasAnyRole('ADMIN')") // Only ADMIN users can access this endpoint
    public ResponseEntity<UserResponse> updateEmployee(@RequestBody UserRequest request,
            HttpServletRequest httpServletRequest) {
        try {
            UserResponse response = userService.updateEmployee(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserResponse(e.getMessage(), ""));
        }
    }

    /**
     * Retrieves details of the currently logged-in user.
     *
     * @param httpServletRequest The HTTP request containing authentication details.
     * @return Response with user details.
     */
    @GetMapping("/getSelf") // Handles GET requests to "/api/user/getSelf"
    public ResponseEntity<UserResponse> getSelf(HttpServletRequest httpServletRequest) {
        String email = userService.getEmailFromToken(httpServletRequest);
        try {
            UserResponse response = userService.getSelf(email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserResponse(e.getMessage(), ""));
        }
    }
}
