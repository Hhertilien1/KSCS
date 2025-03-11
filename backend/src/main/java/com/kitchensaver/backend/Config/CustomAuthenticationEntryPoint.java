package com.kitchensaver.backend.Config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.kitchensaver.backend.Service.UserService;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    // Logger to log authentication-related events
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // Overridden method to handle authentication errors
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        
        // Log the authentication exception details
        logger.info("authException :::" + authException.toString());
        
        // Set the response content type to JSON
        response.setContentType("application/json");

        // Retrieve the "Authorization" header from the incoming HTTP request
        String authHeader = request.getHeader("Authorization");

        // Check if the Authorization header is missing or does not start with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // If no token, respond with 401 Unauthorized and a corresponding message
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(""" 
                    {
                        "status": 401,
                        "message": "Access Denied: Token not available"
                    }
                    """);
        } 
        // Check if the exception is an InsufficientAuthenticationException
        else if (authException instanceof InsufficientAuthenticationException) {
            // If insufficient authentication, respond with 403 Forbidden and a corresponding message
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("""
                    {
                        "status": 403,
                        "message": "Access Denied: Full authentication is required to access this resource"
                    }
                    """);
        } 
        // For any other authentication exception
        else {
            // Respond with 401 Unauthorized and a generic authentication required message
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("""
                    {
                        "status": 401,
                        "message": "Access Denied: Authentication required"
                    }
                    """);
        }
    }
}
