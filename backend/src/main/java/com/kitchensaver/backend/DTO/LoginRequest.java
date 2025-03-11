package com.kitchensaver.backend.DTO; // Package for Data Transfer Object (DTO) classes


// This class is used to get login details from the user
public class LoginRequest {
    private String email; // User's email address for login
    private String password; // User's password for login

    // Getter method for the user's email
    public String getEmail() {
        return email;
    }

    // Setter method for the user's email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter method for the user's password
    public String getPassword() {
        return password;
    }

    // Setter method for the user's password
    public void setPassword(String password) {
        this.password = password;
    }
}
