package com.pawmodoro.users.service.login.interface_adapter;

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
    @NotBlank(message = "Please enter a username")
    @Size(min = 3, max = 50, message = "Username length must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$",
        message = "Username can only contain letters, numbers, dots, underscores, and hyphens")
    String username,

    @NotBlank(message = "Please enter a password")
    @Size(min = 6, max = 50, message = "Password length must be between 6 and 50 characters")
    String password) {}
