package com.kitchensaver.backend.Exceptions; // Package declaration for exceptions

// Custom exception class to handle not found scenarios
public class NotFoundException extends Exception {
    
    // Constructor that takes a message as a parameter and passes it to the superclass (Exception)
    public NotFoundException(String message) {
        super(message); // Call the superclass constructor with the provided message
    }
}
