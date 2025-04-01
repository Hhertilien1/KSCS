package com.kitchensaver.backend.DTO; // Package for Data Transfer Object (DTO) classes

import com.kitchensaver.backend.model.Users; // Importing Users model class

// This class is used to format the response when a user interacts with the system
public class UserResponse {
    private String token; // JWT token for the user
    private String message; // Message to display
    private String role; // Role of the user
    private Users user; // The user object
    

    // Constructor to initialize the UserResponse with message, token, and user
    public UserResponse(String message, String token, Users user) {
        this.token = token; // Set the token
        this.user = user; // Set the user object
        this.role = user.getRole().name(); // Set the role of the user
        this.message = message; // Set the message
    
    }

    // Constructor to initialize the UserResponse with message and token
    public UserResponse(String message, String token) {
        this.token = token; // Set the token
        this.role = null; // Role is null when there is no user
        this.message = message; // Set the message
    }

    // Getter method for the token
    public String getToken() {
        return token;
    }

    // Setter method for the message
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter method for the message
    public String getMessage() {
        return message;
    }

    // Setter method for the token
    public void setToken(String token) {
        this.token = token;
    }

    // Getter method for the user
    public Users getUser() {
        return user;
    }

    // Getter method for the role
    public String getRole() {
        return role;
    }

    // Setter method for the user, also updates the role
    public void setUser(Users user) {
        this.role = user.getRole().name(); // Update the role based on user
        this.user = user; // Set the user object
    }


}
