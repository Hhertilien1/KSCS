package com.kitchensaver.backend.Exceptions; // Package for exception handling

// Custom exception class for handling cases where an email already exists
public class EmailAlreadyExistsException extends Exception {
    
    // Constructor that takes a message and passes it to the parent Exception class
    public EmailAlreadyExistsException(String message) {
        super(message); // Call the parent constructor with the provided message
    }
}
