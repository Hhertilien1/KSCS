package com.kitchensaver.backend.Config;

import com.kitchensaver.backend.Service.UserService;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration // Indicates this class provides Spring configuration
@EnableWebSecurity // Enables web security features
@EnableMethodSecurity // Enables method security annotations
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter; // JWT filter for authentication
    private final CustomAuthenticationEntryPoint unauthorizedHandler; // Custom handler for unauthorized access
    private final UserService userService; // User service to fetch user details

    // Constructor for initializing the SecurityConfig with the required components
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter,
            CustomAuthenticationEntryPoint unauthorizedHandler, UserService userService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.unauthorizedHandler = unauthorizedHandler;
        this.userService = userService;
    }

    // Security filter chain configuration
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS with custom configuration
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection (as we are using JWT)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedHandler)) // Handle unauthorized exceptions
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user/register", "/api/user/login").permitAll()) // Allow registration and login without authentication
                .authorizeHttpRequests(auth3 -> auth3
                        .anyRequest().authenticated()) // All other requests require authentication
                // .exceptionHandling(exception -> exception
                // .authenticationEntryPoint((request, response, e) -> {
                // response.sendError(404, "endpoint not available");
                // })) // Custom exception handling for unavailable endpoints (commented out)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Use stateless session (JWT)
                .authenticationProvider(authenticationProvider()) // Add custom authentication provider
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter before authentication filter

        return http.build(); // Build and return the security filter chain
    }

    // Bean for authentication provider (used for user authentication)
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); // Dao authentication provider
        authProvider.setUserDetailsService(userService); // Set user details service to fetch user info
        authProvider.setPasswordEncoder(passwordEncoder()); // Set password encoder (BCrypt)
        return authProvider; // Return the configured authentication provider
    }

    // Bean for password encoder (BCrypt password encoder used here)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Return a BCrypt password encoder
    }

    // Bean for authentication manager (used for managing authentication requests)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Return authentication manager from Spring configuration
    }

    // Bean for CORS configuration (allows cross-origin requests from specified origins)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration(); // Create new CORS configuration
        configuration.setAllowedOrigins(Arrays.asList("*")); // Allow all origins (could be modified for more restrictive policies)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3001", "http://localhost:3000")); // Allow React app origins

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // Allow these HTTP methods
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type")); // Allowed headers
        configuration.setExposedHeaders(Arrays.asList("Authorization")); // Expose authorization header
        configuration.setAllowCredentials(true); // Allow credentials (cookies, etc.)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // Create CORS configuration source
        source.registerCorsConfiguration("/**", configuration); // Register the CORS configuration for all URLs
        return source; // Return the CORS configuration source
    }
    // Rest of the configuration remains the same
}
