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
    private static final Logger logger = LoggerFactory.getLogger(UserService.class); // Logger for tracking events

    // Constructor for injecting UserRepo and initializing password encoder
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Registers a new user based on the UserRequest data
    public UserResponse registerUser(UserRequest request) {
        try {
            // Check if passwords match
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                throw new InvalidRequestException("Passwords do not match!");
            }

            // Check if email already exists in the database
            if (userRepo.findByEmail(request.getEmail()).isPresent()) {
                throw new EmailAlreadyExistsException("Email already exists!");
            }

            // Check if username already exists in the database
            if (userRepo.findByUsername(request.getUsername()).isPresent()) {
                throw new UsernameAlreadyExistsException("Username already exists!");
            }

            // Validate input fields (basic null/empty checks)
            if (request.getFirstName() == null || request.getFirstName().isEmpty()) {
                throw new InvalidRequestException("First name is required!");
            }
            if (request.getLastName() == null || request.getLastName().isEmpty()) {
                throw new InvalidRequestException("Last name is required!");
            }
            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                throw new InvalidRequestException("Email is required!");
            }
            if (request.getCell() == null || request.getCell().isEmpty()) {
                throw new InvalidRequestException("Cell number is required!");
            }
            if (request.getOffice() == null || request.getOffice().isEmpty()) {
                throw new InvalidRequestException("Office is required!");
            }
            if (request.getRole() == null || request.getRole().isEmpty()) {
                throw new InvalidRequestException("Role is required!");
            }

            if (request.getUsername() == null || request.getUsername().isEmpty()) {
                throw new InvalidRequestException("Username is required!");
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
                throw new InvalidRequestException("Invalid role! Allowed values: ADMIN, CABINET_MAKER, INSTALLER");
            }

            // Encrypt the password before saving it
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            // Save the new user to the database
            userRepo.save(user);

            // Generate JWT token for the newly registered user
            String token = JwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());
            user.removePassword(); // Remove password from the response for security

            return new UserResponse("User registered successfully", token, user);

        } catch (InvalidRequestException e) {
            return new UserResponse(e.getMessage(), "");
        } catch (EmailAlreadyExistsException e) {
            return new UserResponse(e.getMessage(), "");
        } catch (Exception e) {
            return new UserResponse(e.getMessage(), "");
        }
    }

    // Updates an existing user's information
    public UserResponse updateEmployee(UserRequest request) {
        try {
            // Ensure ID is present
            if (request.getId() == null || request.getId().isEmpty()) {
                throw new InvalidRequestException("Id is required!");
            }

            // Find user by ID
            Optional<Users> userOptional = userRepo.findById(Long.parseLong(request.getId()));

            // If the user is not found, return a message
            if (userOptional.isEmpty()) {
                throw new InvalidRequestException("User not found!");
            }

            // Update user fields if they are present and valid
            Users user = userOptional.get();
            if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
                user.setFirstName(request.getFirstName());
            }
            if (request.getLastName() != null && !request.getLastName().isEmpty()) {
                user.setLastName(request.getLastName());
            }
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                if (userRepo.findByEmail(request.getEmail()).isPresent()
                        && userRepo.findByEmail(request.getEmail()).get().getId() != user.getId()) {
                    throw new EmailAlreadyExistsException("Email already exists!");
                }
                user.setEmail(request.getEmail());
            }
            if (request.getCell() != null && !request.getCell().isEmpty()) {
                user.setCell(request.getCell());
            }
            if (request.getOffice() != null && !request.getOffice().isEmpty()) {
                user.setOffice(request.getOffice());
            }
            if (request.getRole() != null && !request.getRole().isEmpty()) {
                try {
                    user.setRole(Role.valueOf(request.getRole().toUpperCase())); // Convert the role to uppercase and set it
                } catch (IllegalArgumentException e) {
                    throw new InvalidRequestException("Invalid role! Allowed values: ADMIN, CABINET_MAKER, INSTALLER");
                }
            }

            // Encrypt the password before saving it
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                if (!request.getPassword().equals(request.getConfirmPassword())) {
                    throw new InvalidRequestException("Passwords do not match!");
                }
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }

            // Save the updated user to the database
            userRepo.save(user);

            return new UserResponse("User updated successfully", "", user);

        } catch (InvalidRequestException e) {
            return new UserResponse(e.getMessage(), "");
        } catch (EmailAlreadyExistsException e) {
            return new UserResponse(e.getMessage(), "");
        } catch (Exception e) {
            return new UserResponse(e.getMessage(), "");
        }
    }

    // Authenticates a user by email and password
    public UserResponse loginUser(LoginRequest request) {
        try {
            // Try to find the user by their email
            Optional<Users> userOptional = userRepo.findByEmail(request.getEmail());

            // If the user is not found, return a message
            if (userOptional.isEmpty()) {
                throw new InvalidCredentialsException("User not found!");
            }

            // If the user is found, check if the password matches
            Users user = userOptional.get();
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new InvalidCredentialsException("Invalid credentials!");
            }

            // Generate JWT token
            String token = JwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());

            return new UserResponse("User login successfully", token, user);

        } catch (InvalidCredentialsException e) {
            return new UserResponse(e.getMessage(), "");
        } catch (Exception e) {
            return new UserResponse(e.getMessage(), "");
        }
    }

    // Retrieves a list of all users that are not ADMIN
    public List<Users> getAllEmployees() {
        List<Users> users = userRepo.findAllByRoleNot(Role.ADMIN);
        return users;
    }

    // Deletes a user by ID
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    // Loads user by email for Spring Security authentication
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    public UserResponse updateProfile(UserRequest request, HttpServletRequest httpServletRequest) {
        try {
            // Get the JWT token from the request header
            String token = httpServletRequest.getHeader("Authorization").replace("Bearer ", "");
    
            // Decode the JWT token to get the user's email
            DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
            String email = decodedJWT.getSubject();
    
            // Find the user in the database by email
            Optional<Users> userOptional = userRepo.findByEmail(email);
    
            // If user not found, throw an exception
            if (userOptional.isEmpty()) {
                throw new InvalidRequestException("User not found!");
            }
    
            // Get the existing user object
            Users user = userOptional.get();
    
            // Update first name if provided
            if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
                user.setFirstName(request.getFirstName());
            }
    
            // Update last name if provided
            if (request.getLastName() != null && !request.getLastName().isEmpty()) {
                user.setLastName(request.getLastName());
            }
    
            // Update username if provided
            if (request.getUsername() != null && !request.getUsername().isEmpty()) {
                user.setUsername(request.getUsername());
            }
    
            // Update email if provided
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                user.setEmail(request.getEmail());
            }
    
            // Update password if provided and encode it
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }
    
            // Update cell number if provided
            if (request.getCell() != null && !request.getCell().isEmpty()) {
                user.setCell(request.getCell());
            }
    
            // Save the updated user to the database
            userRepo.save(user);
    
            // Clear the password before returning user data
            user.setPassword("");
    
            // Generate a new JWT token with updated user info
            String newToken = JwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());
    
            // Return a successful response with the updated user and new token
            return new UserResponse("User updated successfully", newToken, user);
    
        } catch (InvalidRequestException e) {
            // Return error response for invalid request
            return new UserResponse(e.getMessage(), "");
        } catch (Exception e) {
            // Return generic error response
            return new UserResponse(e.getMessage(), "");
        }
    }
    

    // Retrieves user data for the currently logged-in user using their email
    public UserResponse getSelf(String email) {
        try {
            // Find the user by the email
            Optional<Users> userOptional = userRepo.findByEmail(email);

            // If the user is not found, return a message
            if (userOptional.isEmpty()) {
                throw new InvalidRequestException("User not found!");
            }

            // Return the user response
            return new UserResponse("User found successfully", "", userOptional.get());

        } catch (InvalidRequestException e) {
            return new UserResponse(e.getMessage(), "");
        } catch (Exception e) {
            return new UserResponse(e.getMessage(), "");
        }
    }

    // Retrieves the email from the JWT token provided in the HTTP request
    public String getEmailFromToken(HttpServletRequest httpServletRequest) {
        try {
            // Retrieves the JWT token from the Authorization header
            String token = httpServletRequest.getHeader("Authorization").replace("Bearer ", "");

            // Decodes the JWT token to get the email
            DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
            String email = decodedJWT.getSubject();

            // Returns the email in the response
            return email;
        } catch (Exception e) {
            return "User not found";
        }
    }
}
