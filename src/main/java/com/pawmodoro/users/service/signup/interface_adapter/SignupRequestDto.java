package com.pawmodoro.users.service.signup.interface_adapter;

import com.pawmodoro.constants.Constants.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for signup requests.
 * Uses records for immutable data and validation annotations for input validation.
 *
 * @param username the desired username for the new account
 * @param email the email address for the new account
 * @param password the password for the new account
 * @param confirmPassword confirmation of the password to ensure it matches
 */
public record SignupRequestDto(
        @NotBlank(message = "Username is required")
        @Size(min = ValidationConstants.MIN_USERNAME_LENGTH,
              max = ValidationConstants.MAX_USERNAME_LENGTH,
              message = "Username must be between "
                      + ValidationConstants.MIN_USERNAME_LENGTH + " and "
                      + ValidationConstants.MAX_USERNAME_LENGTH + " characters")
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$",
                message = "Username can only contain letters, numbers, dots, underscores, and hyphens")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = ValidationConstants.MIN_PASSWORD_LENGTH,
              max = ValidationConstants.MAX_PASSWORD_LENGTH,
              message = "Password must be between "
                      + ValidationConstants.MIN_PASSWORD_LENGTH + " and "
                      + ValidationConstants.MAX_PASSWORD_LENGTH + " characters")
        String password,

        @NotBlank(message = "Password confirmation is required")
        String confirmPassword
) {
}
