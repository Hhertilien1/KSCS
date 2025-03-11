package com.kitchensaver.backend;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

// This class is used to configure the Spring Boot application when it's run as a web application.
public class ServletInitializer extends SpringBootServletInitializer {

    // This method is overridden to configure the Spring application for deployment
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        // It tells Spring Boot to use the BackendApplication class as the main class for the application.
        return application.sources(BackendApplication.class);
    }

}
