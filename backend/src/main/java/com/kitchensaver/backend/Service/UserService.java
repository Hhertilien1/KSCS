// Importing necessary libraries
package com.kitchensaver.backend.Service;

import com.auth0.jwt.interfaces.DecodedJWT; // For decoding JWT tokens
import com.kitchensaver.backend.DTO.LoginRequest; // Data Transfer Object for login request
import com.kitchensaver.backend.DTO.UserRequest; // Data Transfer Object for user registration request
import com.kitchensaver.backend.DTO.UserResponse; // Data Transfer Object for user response after login or registration
import com.kitchensaver.backend.Exceptions.EmailAlreadyExistsException; // Exception for existing email
import com.kitchensaver.backend.Exceptions.InvalidCredentialsException; // Exception for invalid credentials
import com.kitchensaver.backend.Exceptions.InvalidRequestException; // Exception for invalid user request
import com.kitchensaver.backend.Exceptions.UsernameAlreadyExistsException; // Exception for existing username
import com.kitchensaver.backend.model.Role; // Role model (ADMIN, CABINET_MAKER, INSTALLER)
import com.kitchensaver.backend.model.Users; // User model to interact with the user data
import com.kitchensaver.backend.Repo.UserRepo; // Repository to interact with the database

import org.springframework.security.core.userdetails.UserDetails; // UserDetails for user authentication
import org.springframework.security.core.userdetails.UserDetailsService; // UserDetailsService to load user by username
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Exception for user not found
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // For password encryption
import org.springframework.stereotype.Service; // Service annotation for spring beans

import com.kitchensaver.backend.util.JwtUtil; // Utility class for handling JWT generation and verification

import jakarta.servlet.http.HttpServletRequest; // For accessing HTTP request

import java.util.List;
import java.util.Optional; // Optional to handle null values safely

import org.slf4j.Logger; // Logger for logging events
import org.slf4j.LoggerFactory; // LoggerFactory to create the logger

// This class handles user-related tasks
@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo; // This helps save and find users in the database
    private final BCryptPasswordEncoder passwordEncoder; // This helps secure passwords
    private static final Logger logger = LoggerFactory.getLogger(UserService.class); // Logger for the service

    // Setting up the service and password encoder
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Initializing the password encoder
    }

    // Method to register a new user
    public UserResponse registerUser(UserRequest request) {
        try {
            // Check if passwords match
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                throw new InvalidRequestException("Passwords do not match!"); // Throw exception if passwords don't match
            }

            // Check if email already exists in the database
            if (userRepo.findByEmail(request.getEmail()).isPresent()) {
                throw new EmailAlreadyExistsException("Email already exists!"); // Throw exception if email is taken
            }

            // Check if username already exists in the database
            if (userRepo.findByUsername(request.getUsername()).isPresent()) {
                throw new UsernameAlreadyExistsException("Username already exists!"); // Throw exception if username is taken
            }

            // Validate input fields
            if (request.getFirstName() == null || request.getFirstName().isEmpty()) {
                throw new InvalidRequestException("First name is required!"); // Throw exception if first name is empty
            }
            if (request.getLastName() == null || request.getLastName().isEmpty()) {
                throw new InvalidRequestException("Last name is required!"); // Throw exception if last name is empty
            }
            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                throw new InvalidRequestException("Email is required!"); // Throw exception if email is empty
            }
            if (request.getCell() == null || request.getCell().isEmpty()) {
                throw new InvalidRequestException("Cell number is required!"); // Throw exception if cell number is empty
            }
            if (request.getOffice() == null || request.getOffice().isEmpty()) {
                throw new InvalidRequestException("Office is required!"); // Throw exception if office is empty
            }
            if (request.getRole() == null || request.getRole().isEmpty()) {
                throw new InvalidRequestException("Role is required!"); // Throw exception if role is empty
            }

            if (request.getUsername() == null || request.getUsername().isEmpty()) {
                throw new InvalidRequestException("Username is required!"); // Throw exception if username is empty
            }

            // Create a new user object with the details from the request
            Users user = new Users();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setUsername(request.getUsername());
            user.setCell(request.getCell());
            user.setOffice(request.getOffice());

            // Assign role (validate input)
            try {
                user.setRole(Role.valueOf(request.getRole().toUpperCase())); // Convert the role to uppercase and set it
            } catch (IllegalArgumentException e) {
                throw new InvalidRequestException("Invalid role! Allowed values: ADMIN, CABINET_MAKER, INSTALLER"); // Throw exception for invalid role
            }

            // Encrypt the password before saving it
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            // Save the new user to the database
            userRepo.save(user);

            // Generate JWT token
            String token = JwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());
            user.removePassword(); // Remove password from user object before returning

            // Return the user response with success message, token, and user details
            return new UserResponse("User registered successfully", token, user);

        } catch (InvalidRequestException e) {
            return new UserResponse(e.getMessage(), ""); // Return error message if invalid request
        } catch (EmailAlreadyExistsException e) {
            return new UserResponse(e.getMessage(), ""); // Return error message if email exists
        } catch (Exception e) {
            return new UserResponse(e.getMessage(), ""); // Return error message for other exceptions
        }
    }

    // Method for user login
    public UserResponse loginUser(LoginRequest request) {
        try {
            // Try to find the user by their email
            Optional<Users> userOptional = userRepo.findByEmail(request.getEmail());

            // If the user is not found, return an error message
            if (userOptional.isEmpty()) {
                throw new InvalidCredentialsException("User not found!"); // Throw exception if user not found
            }

            // If the user is found, check if the password matches
            Users user = userOptional.get();
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new InvalidCredentialsException("Invalid credentials!"); // Throw exception if password is incorrect
            }

            // Generate JWT token
            String token = JwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());

            // Return the user response with success message, token, and user details
            return new UserResponse("User login successfully", token, user);

        } catch (InvalidCredentialsException e) {
            return new UserResponse(e.getMessage(), ""); // Return error message if invalid credentials
        } catch (Exception e) {
            return new UserResponse(e.getMessage(), ""); // Return error message for other exceptions
        }
    }

    // Method to load user by username (for Spring Security)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Return user details for authentication
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    public UserResponse updateProfile(UserRequest request, HttpServletRequest httpServletRequest) {
        try {
            // Get the JWT token
            String token = httpServletRequest.getHeader("Authorization").replace("Bearer ", "");

            // Decode the JWT token to get the user email
            DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
            String email = decodedJWT.getSubject();

            // Find the user by the email
            Optional<Users> userOptional = userRepo.findByEmail(email);

            // If the user is not found, return a message
            if (userOptional.isEmpty()) {
                throw new InvalidRequestException("User not found!");
            }

            // Update the user with the new details
            Users user = userOptional.get();
            if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
                user.setFirstName(request.getFirstName());
            }
            if (request.getLastName() != null && !request.getLastName().isEmpty()) {
                user.setLastName(request.getLastName());
            }
            if (request.getUsername() != null && !request.getUsername().isEmpty()) {
                user.setUsername(request.getUsername());
            }
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                user.setEmail(request.getEmail());
            }
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }
            if (request.getCell() != null && !request.getCell().isEmpty()) {
                user.setCell(request.getCell());
            }

            // Save the updated user
            userRepo.save(user);
            user.setPassword("");
            // Generate a new JWT token

            String newToken = JwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());
            // Return the user response
            return new UserResponse("User updated successfully", newToken, user);

        } catch (InvalidRequestException e) {
            return new UserResponse(e.getMessage(), "");
        } catch (Exception e) {
            return new UserResponse(e.getMessage(), "");
        }
    }

    // Method to get user's own profile information
    public UserResponse getSelf(String email) {
        try {
            // Find the user by email
            Optional<Users> userOptional = userRepo.findByEmail(email);

            // If the user is not found, return an error message
            if (userOptional.isEmpty()) {
                throw new InvalidRequestException("User not found!"); // Throw exception if user not found
            }

            // Return the user response with success message and user details
            return new UserResponse("User found successfully", "", userOptional.get());

        } catch (InvalidRequestException e) {
            return new UserResponse(e.getMessage(), ""); // Return error message if invalid request
        } catch (Exception e) {
            return new UserResponse(e.getMessage(), ""); // Return error message for other exceptions
        }
    }

    // Method to get email from the JWT token
    public String getEmailFromToken(HttpServletRequest httpServletRequest) {
        try {
            // Retrieves the JWT token from the Authorization header
            String token = httpServletRequest.getHeader("Authorization").replace("Bearer ", "");

            // Decodes the JWT token to get the email
            DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
            String email = decodedJWT.getSubject(); // Extract email from token

            // Return the email in the response
            return email;
        } catch (Exception e) {
            return "User not found"; // Return error if something goes wrong
        }
    }

    //Method to update employee information
    public UserResponse updateEmployee(UserRequest request) {
        try {
            // Validate that the request contains an ID
            if (request.getId() == null || request.getId().isEmpty()) {
                throw new InvalidRequestException("Id is required!");
            }
    
            // Retrieve the user from the database using the provided ID
            Optional<Users> userOptional = userRepo.findById(Long.parseLong(request.getId()));
    
            // If the user is not found, throw an exception
            if (userOptional.isEmpty()) {
                throw new InvalidRequestException("User not found!");
            }
    
            // Extract the user object from the optional
            Users user = userOptional.get();
    
            // Update the first name if provided in the request
            if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
                user.setFirstName(request.getFirstName());
            }
    
            // Update the last name if provided in the request
            if (request.getLastName() != null && !request.getLastName().isEmpty()) {
                user.setLastName(request.getLastName());
            }
    
            // Update the email if provided and ensure it is unique
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                // Check if the email is already in use by another user
                if (userRepo.findByEmail(request.getEmail()).isPresent()
                        && userRepo.findByEmail(request.getEmail()).get().getId() != user.getId()) {
                    throw new EmailAlreadyExistsException("Email already exists!");
                }
                user.setEmail(request.getEmail());
            }
    
            // Update the cell phone number if provided in the request
            if (request.getCell() != null && !request.getCell().isEmpty()) {
                user.setCell(request.getCell());
            }
    
            // Update the office location if provided in the request
            if (request.getOffice() != null && !request.getOffice().isEmpty()) {
                user.setOffice(request.getOffice());
            }
    
            // Update the role if provided in the request
            if (request.getRole() != null && !request.getRole().isEmpty()) {
                try {
                    // Convert role to uppercase and set it
                    user.setRole(Role.valueOf(request.getRole().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    // Throw an exception if the role is invalid
                    throw new InvalidRequestException("Invalid role! Allowed values: ADMIN, CABINET_MAKER, INSTALLER");
                }
            }
    
            // Encrypt the password before saving it, ensuring it matches the confirmation password
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                if (!request.getPassword().equals(request.getConfirmPassword())) {
                    throw new InvalidRequestException("Passwords do not match!");
                }
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }
    
            // Save the updated user information to the database
            userRepo.save(user);
    
            // Return a success response
            return new UserResponse("User updated successfully", "", user);
    
        } catch (InvalidRequestException e) {
            // Handle invalid request errors
            return new UserResponse(e.getMessage(), "");
        } catch (EmailAlreadyExistsException e) {
            // Handle email duplication errors
            return new UserResponse(e.getMessage(), "");
        } catch (Exception e) {
            // Handle general exceptions
            return new UserResponse(e.getMessage(), "");
        }
    }
    
    public List<Users> getAllEmployees() {
        // Retrieve all users from the database except those with the ADMIN role
        List<Users> users = userRepo.findAllByRoleNot(Role.ADMIN);
    
        // Return the list of employees
        return users;
    }
    
    public void deleteUser(Long id) {
        // Delete the user from the database based on the provided ID
        userRepo.deleteById(id);
    }
}    