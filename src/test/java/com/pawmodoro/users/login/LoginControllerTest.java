package com.pawmodoro.users.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private LoginInputBoundary mockLoginInteractor;

    @Mock
    private LoginPresenter mockLoginPresenter;

    @Mock
    private BindingResult mockBindingResult;

    private LoginController loginController;

    @BeforeEach
    void setUp() {
        loginController = new LoginController(mockLoginInteractor, mockLoginPresenter);
    }

    @Test
    void login_WithValidCredentials_ReturnsOkResponse() {
        // Arrange
        LoginRequestDTO request = new LoginRequestDTO("testuser", "password123");
        LoginResponseDTO expectedResponse = new LoginResponseDTO(true, "token123", "Login successful", "testuser");

        when(mockBindingResult.hasErrors()).thenReturn(false);
        when(mockLoginInteractor.execute(any(LoginInputData.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<LoginResponseDTO> response = loginController.login(request, mockBindingResult);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedResponse, response.getBody());
        verify(mockLoginInteractor).execute(any(LoginInputData.class));
    }

    @Test
    void login_WithInvalidCredentials_ReturnsUnauthorized() {
        // Arrange
        LoginRequestDTO request = new LoginRequestDTO("testuser", "wrongpassword");
        LoginResponseDTO errorResponse = new LoginResponseDTO(false, null, "Invalid credentials", null);

        when(mockBindingResult.hasErrors()).thenReturn(false);
        when(mockLoginInteractor.execute(any(LoginInputData.class)))
            .thenThrow(new InvalidLoginException("Invalid credentials", errorResponse));

        // Act
        ResponseEntity<LoginResponseDTO> response = loginController.login(request, mockBindingResult);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(errorResponse, response.getBody());
    }

    @Test
    void login_WithValidationErrors_ReturnsBadRequest() {
        // Arrange
        LoginRequestDTO request = new LoginRequestDTO("", "");
        LoginResponseDTO errorResponse = new LoginResponseDTO(false, null, "Username: Username is required", null);

        when(mockBindingResult.hasErrors()).thenReturn(true);
        when(mockBindingResult.getFieldErrors()).thenReturn(
            List.of(new FieldError("loginRequestDTO", "username", "Username is required")));
        when(mockLoginPresenter.formatErrorResponse(any())).thenReturn(errorResponse);

        // Act
        ResponseEntity<LoginResponseDTO> response = loginController.login(request, mockBindingResult);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorResponse, response.getBody());
    }
}
