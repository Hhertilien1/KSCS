package com.kitchensaver.backend.Exceptions; // Package declaration for exceptions

// Custom exception class to handle username already exists scenario
public class UsernameAlreadyExistsException extends Exception {
    
    // Constructor that takes a message as a parameter and passes it to the superclass (Exception)
    public UsernameAlreadyExistsException(String message) {
        super(message); // Call the superclass constructor with the provided message
    }
}
