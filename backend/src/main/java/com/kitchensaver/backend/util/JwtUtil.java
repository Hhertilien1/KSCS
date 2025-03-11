package com.kitchensaver.backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kitchensaver.backend.Service.UserService;

import jakarta.annotation.PostConstruct;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

// This annotation marks this class as a Spring component so it can be injected where needed.
@Component
public class JwtUtil {
    // Default secret and expiration time values.
    private static String secret = "secret";
    private static int expirationTime = 86400000; // 1 day in milliseconds
    // Logger to log messages related to user service.
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // This method is used to inject the secret key from the application properties.
    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        logger.info("secret from env is heree:::" + secret);
        JwtUtil.secret = secret;
    }

    // This method is used to inject the expiration time from the application properties.
    @Value("${jwt.expiration}")
    public void setExpirationTime(int expirationTime) {
        logger.info("expirationTime from env is heree:::" + expirationTime);
        JwtUtil.expirationTime = expirationTime;
    }

    // Constructor to inject secret and expiration time values into the class.
    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") int expirationTime) {
        logger.info("secret and expirationTime from env is heree:::" + secret + ":::" + expirationTime);
        JwtUtil.secret = secret;
        JwtUtil.expirationTime = expirationTime;
    }

    // Method to generate a JWT token with username, role, and user id, along with expiration time.
    public static String generateToken(String username, String role, Long id) {
        Algorithm algorithm = Algorithm.HMAC256(secret); // Define the algorithm for encoding
        // Create and return the token
        return JWT.create()
                .withSubject(username) // Set username as the subject
                .withClaim("role", role) // Set role as a custom claim
                .withClaim("id", id) // Set user id as a custom claim
                .withIssuedAt(new Date()) // Set the issued date
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime)) // Set expiration time
                .sign(algorithm); // Sign the token with the algorithm
    }

    // Method to extract the username from the token.
    public static String extractUsername(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret); // Define the algorithm for decoding
        JWTVerifier verifier = JWT.require(algorithm) // Create JWT verifier with the algorithm
                .build();
        DecodedJWT decodedJWT = verifier.verify(token); // Verify and decode the token
        return decodedJWT.getSubject(); // Return the username (subject)
    }

    // Method to extract the role from the token.
    public static String extractRole(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret); // Define the algorithm for decoding
        JWTVerifier verifier = JWT.require(algorithm) // Create JWT verifier with the algorithm
                .build();
        DecodedJWT decodedJWT = verifier.verify(token); // Verify and decode the token
        return decodedJWT.getClaim("role").asString(); // Return the role claim as a string
    }

    // Method to verify the token and return the decoded JWT object.
    public static DecodedJWT verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret); // Define the algorithm for decoding
        JWTVerifier verifier = JWT.require(algorithm).build(); // Create JWT verifier
        return verifier.verify(token); // Verify the token and return the decoded JWT object
    }

    // Method to decode the token and return the decoded JWT object.
    public static DecodedJWT decodeToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret); // Define the algorithm for decoding
        JWTVerifier verifier = JWT.require(algorithm).build(); // Create JWT verifier
        return verifier.verify(token); // Decode and return the JWT object
    }

    // Method to get the user id from the decoded JWT token.
    public static Long getUserIdByDecodedToken(DecodedJWT decodedJWT) {
        // Extract and parse the user id from the decoded JWT
        return Long.parseLong(decodedJWT.getClaim("id").asString());
    }
}
