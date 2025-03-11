package com.kitchensaver.backend.DTO; // Package for Data Transfer Object (DTO) classes

// This class is used to get user details when they register
public class UserRequest {
    private String id; // Unique identifier for the user
    private String firstName; // User's first name
    private String lastName; // User's last name
    private String email; // User's email address
    private String cell; // User's cell phone number
    private String office; // User's office phone number
    private String role; // Role of the user (e.g., admin, user)
    private String password; // User's password
    private String username; // User's chosen username
    private String confirmPassword; // User's password confirmation for validation

    // Getter method for the user ID
    public String getId() {
        return id;
    }

    // Getter method for the user's first name
    public String getFirstName() {
        return firstName;
    }

    // Getter method for the user's last name
    public String getLastName() {
        return lastName;
    }

    // Getter method for the user's email
    public String getEmail() {
        return email;
    }

    // Getter method for the user's username
    public String getUsername() {
        return username;
    }

    // Getter method for the user's cell phone number
    public String getCell() {
        return cell;
    }

    // Getter method for the user's office phone number
    public String getOffice() {
        return office;
    }

    // Getter method for the user's role
    public String getRole() {
        return role;
    }

    // Getter method for the user's password
    public String getPassword() {
        return password;
    }

    // Getter method for confirming the user's password
    public String getConfirmPassword() {
        return confirmPassword;
    }
}
