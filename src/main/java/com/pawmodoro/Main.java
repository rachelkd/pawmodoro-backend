package com.pawmodoro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * Main application class that bootstraps the Spring Boot application.
 * This class initializes the Spring context and starts the embedded web server.
 * It also configures the application to run with the 'local' profile by default.
 */
@SpringBootApplication
public class Main {
    /**
     * The main method that serves as the entry point for the application.
     * It sets up the Spring Boot environment and launches the application.
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
