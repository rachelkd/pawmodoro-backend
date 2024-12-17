package com.pawmodoro.users.service.signup.interface_adapter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

/**
 * Data Transfer Object for signup requests.
 * Uses records for immutable data and validation annotations for input validation.
 * @param username the desired username for the new account
 * @param email the email address for the new account
 * @param password the password for the new account
 * @param confirmPassword confirmation of the password to ensure it matches
 */
public record SignupRequestDTO(
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$",
        message = "Username can only contain letters, numbers, underscores and hyphens")
    String username,

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email,

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    String password,

    @NotBlank(message = "Password confirmation is required")
    String confirmPassword) {}
