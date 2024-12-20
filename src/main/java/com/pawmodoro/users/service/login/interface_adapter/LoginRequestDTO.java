package com.pawmodoro.users.service.login.interface_adapter;

import com.pawmodoro.constants.Constants.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for login requests.
 * This record encapsulates the data sent by the client for login attempts.
 * Contains validation annotations to ensure data integrity before processing.
 * @param username the username or email of the user attempting to login
 * @param password the user's password
 */
public record LoginRequestDto(
    @NotBlank(message = "Please enter a username")
    @Size(min = ValidationConstants.MIN_USERNAME_LENGTH,
        max = ValidationConstants.MAX_USERNAME_LENGTH,
        message = "Username length must be between "
            + ValidationConstants.MIN_USERNAME_LENGTH + " and "
            + ValidationConstants.MAX_USERNAME_LENGTH + " characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$",
        message = "Username can only contain letters, numbers, dots, underscores, and hyphens")
    String username,

    @NotBlank(message = "Please enter a password")
    @Size(min = ValidationConstants.MIN_PASSWORD_LENGTH,
        max = ValidationConstants.MAX_PASSWORD_LENGTH,
        message = "Password length must be between "
            + ValidationConstants.MIN_PASSWORD_LENGTH + " and "
            + ValidationConstants.MAX_PASSWORD_LENGTH + " characters")
    String password) {

}
