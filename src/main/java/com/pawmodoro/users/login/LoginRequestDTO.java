package com.pawmodoro.users.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

/**
 * Data Transfer Object for login requests.
 * This record encapsulates the data sent by the client for login attempts.
 * Contains validation annotations to ensure data integrity before processing.
 * @param username the username or email of the user attempting to login
 * @param password the user's password
 */
public record LoginRequestDTO(
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$",
        message = "Username can only contain letters, numbers, dots, underscores, and hyphens")
    String username,

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
    String password) {}
