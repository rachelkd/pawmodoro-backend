package com.pawmodoro.users.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class LoginPresenterTest {

    private LoginPresenter loginPresenter;

    @BeforeEach
    void setUp() {
        loginPresenter = new LoginPresenter();
    }

    @Test
    void formatSuccessResponse_ReturnsCorrectDTO() {
        // Arrange
        LoginOutputData outputData = new LoginOutputData("testuser", "token123");

        // Act
        LoginResponseDTO response = loginPresenter.formatSuccessResponse(outputData);

        // Assert
        assertTrue(response.success());
        assertEquals("token123", response.token());
        assertEquals("Login successful", response.message());
        assertEquals("testuser", response.username());
    }

    @Test
    void formatErrorResponse_ReturnsCorrectDTO() {
        // Arrange
        String errorMessage = "Invalid credentials";

        // Act
        LoginResponseDTO response = loginPresenter.formatErrorResponse(errorMessage);

        // Assert
        assertFalse(response.success());
        assertNull(response.token());
        assertEquals(errorMessage, response.message());
        assertNull(response.username());
    }
}
