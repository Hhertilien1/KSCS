package com.kitchensaver.backend.Config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.kitchensaver.backend.Service.UserService;
import com.kitchensaver.backend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// @Component
// public class JwtAuthenticationFilter extends OncePerRequestFilter {

//     private final UserService userService;
//     private static final Logger logger = LoggerFactory.getLogger(UserService.class);

//     public JwtAuthenticationFilter(UserService userService) {
//         this.userService = userService;
//     }

//     @Override
//     protected void doFilterInternal(HttpServletRequest request,
//                                     HttpServletResponse response,
//                                     FilterChain filterChain) throws ServletException, IOException {
//         final String authHeader = request.getHeader("Authorization");
        
//         if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//             filterChain.doFilter(request, response);
//             return;
//         }

//         try {
//             final String jwt = authHeader.substring(7);
//             final DecodedJWT decodedJWT = JwtUtil.verifyToken(jwt);
//             final String userEmail = decodedJWT.getSubject();
            
//             if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                 UserDetails userDetails = userService.loadUserByUsername(userEmail);
                
//                 UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                     userDetails,
//                     null,
//                     userDetails.getAuthorities()
//                 );
//                 authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                 SecurityContextHolder.getContext().setAuthentication(authToken);
//             }
//         } catch (Exception e) {
//             logger.info("Cannot set user authentication: {}", e.getMessage());
//         }
        
//         filterChain.doFilter(request, response);
//     }
// }

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;  // Service for handling user details
    private final JwtUtil jwtUtil;  // Utility class for JWT token operations

    // Constructor to initialize JwtAuthenticationFilter with userService and jwtUtil
    public JwtAuthenticationFilter(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // Overridden method that filters each HTTP request and validates the JWT token
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Retrieve the "Authorization" header from the incoming HTTP request
        final String authHeader = request.getHeader("Authorization");
        
        // If the Authorization header is missing or doesn't start with "Bearer ", move to the next filter
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);  // Proceed with the filter chain
            return;
        }

        try {
            // Extract the JWT token from the Authorization header (after "Bearer " prefix)
            final String jwt = authHeader.substring(7);
            
            // Verify and decode the JWT token using the jwtUtil utility
            final DecodedJWT decodedJWT = jwtUtil.verifyToken(jwt);
            
            // Extract the user email (subject) from the decoded JWT token
            final String userEmail = decodedJWT.getSubject();
            
            // If the userEmail is valid and the authentication context is not set, load user details
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Load the user details from the UserService
                UserDetails userDetails = userService.loadUserByUsername(userEmail);
                
                // Create an authentication token for the user with the necessary details
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails,  // User details (username, roles, etc.)
                        null,  // No credentials needed (JWT-based authentication)
                        userDetails.getAuthorities()  // User's authorities (roles)
                    );
                
                // Attach the HTTP request details to the authentication token
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Set the authentication context with the newly created token
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // If any exception occurs during token verification, clear the authentication context
            // and return an unauthorized error
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;  // Exit the method after sending the error response
        }
        
        // Continue the filter chain by passing the request and response to the next filter
        filterChain.doFilter(request, response);
    }
}
