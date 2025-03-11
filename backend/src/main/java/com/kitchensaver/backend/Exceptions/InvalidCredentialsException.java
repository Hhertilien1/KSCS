package com.kitchensaver.backend.Exceptions; // Package declaration for exceptions

// Custom exception class for invalid credentials
public class InvalidCredentialsException extends Exception {
    
    // Constructor that takes a message as a parameter and passes it to the superclass (Exception)
    public InvalidCredentialsException(String message) {
        super(message); // Call the superclass constructor with the provided message
    }
}
