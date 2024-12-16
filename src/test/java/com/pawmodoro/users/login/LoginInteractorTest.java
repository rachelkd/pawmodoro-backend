package com.pawmodoro.users.login;

import com.pawmodoro.users.CommonUserFactory;
import com.pawmodoro.users.User;
import com.pawmodoro.users.UserFactory;
import com.pawmodoro.users.UserNotFoundException;
import entity.exceptions.DatabaseAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoginInteractorTest {

    @Mock
    private LoginUserDataAccessInterface mockUserDataAccess;

    @Mock
    private LoginOutputBoundary mockLoginPresenter;

    private LoginInteractor loginInteractor;
    private UserFactory userFactory;

    @BeforeEach
    void setUp() {
        loginInteractor = new LoginInteractor(mockUserDataAccess, mockLoginPresenter);
        userFactory = new CommonUserFactory();
    }

    @Test
    void execute_WithValidCredentials_ReturnsSuccessResponse()
        throws DatabaseAccessException, UserNotFoundException {
        // Arrange
        LoginInputData inputData = new LoginInputData("testuser", "password123");
        User user = userFactory.create("testuser", "test@example.com");
        LoginResponseDTO expectedResponse = new LoginResponseDTO(true, "token123", "Login successful", "testuser");

        when(mockUserDataAccess.existsByName("testuser")).thenReturn(true);
        when(mockUserDataAccess.get("testuser")).thenReturn(user);
        when(mockUserDataAccess.authenticate("test@example.com", "password123")).thenReturn(user);
        when(mockUserDataAccess.getAccessToken()).thenReturn("token123");
        when(mockLoginPresenter.formatSuccessResponse(any(LoginOutputData.class))).thenReturn(expectedResponse);

        // Act
        LoginResponseDTO response = loginInteractor.execute(inputData);

        // Assert
        assertEquals(expectedResponse, response);
        verify(mockUserDataAccess).authenticate("test@example.com", "password123");
        verify(mockLoginPresenter).formatSuccessResponse(any(LoginOutputData.class));
    }

    @Test
    void execute_WithNonexistentUser_ThrowsException() throws DatabaseAccessException {
        // Arrange
        LoginInputData inputData = new LoginInputData("nonexistent", "password123");
        LoginResponseDTO errorResponse = new LoginResponseDTO(false, null, "User does not exist.", null);

        when(mockUserDataAccess.existsByName("nonexistent")).thenReturn(false);
        when(mockLoginPresenter.formatErrorResponse(anyString())).thenReturn(errorResponse);

        // Act & Assert
        InvalidLoginException exception = assertThrows(InvalidLoginException.class,
            () -> loginInteractor.execute(inputData));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void execute_WithIncorrectPassword_ThrowsException()
        throws DatabaseAccessException, UserNotFoundException {
        // Arrange
        LoginInputData inputData = new LoginInputData("testuser", "wrongpassword");
        User user = userFactory.create("testuser", "test@example.com");
        LoginResponseDTO errorResponse = new LoginResponseDTO(false, null, "Incorrect password.", null);

        when(mockUserDataAccess.existsByName("testuser")).thenReturn(true);
        when(mockUserDataAccess.get("testuser")).thenReturn(user);
        when(mockUserDataAccess.authenticate("test@example.com", "wrongpassword"))
            .thenThrow(new UserNotFoundException("Authentication failed"));
        when(mockLoginPresenter.formatErrorResponse(anyString())).thenReturn(errorResponse);

        // Act & Assert
        InvalidLoginException exception = assertThrows(InvalidLoginException.class,
            () -> loginInteractor.execute(inputData));
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void execute_WithDatabaseError_ThrowsException() throws DatabaseAccessException {
        // Arrange
        LoginInputData inputData = new LoginInputData("testuser", "password123");
        LoginResponseDTO errorResponse = new LoginResponseDTO(false, null, "Database error", null);

        when(mockUserDataAccess.existsByName("testuser"))
            .thenThrow(new DatabaseAccessException("Database error"));
        when(mockLoginPresenter.formatErrorResponse(anyString())).thenReturn(errorResponse);

        // Act & Assert
        InvalidLoginException exception = assertThrows(InvalidLoginException.class,
            () -> loginInteractor.execute(inputData));
        assertEquals("Authentication failed", exception.getMessage());
    }

    @Test
    void execute_WithUserNotFoundAfterExistsCheck_ThrowsException()
        throws DatabaseAccessException, UserNotFoundException {
        // Arrange
        LoginInputData inputData = new LoginInputData("testuser", "password123");
        LoginResponseDTO errorResponse = new LoginResponseDTO(false, null, "User not found", null);

        when(mockUserDataAccess.existsByName("testuser")).thenReturn(true);
        when(mockUserDataAccess.get("testuser"))
            .thenThrow(new UserNotFoundException("User not found"));
        when(mockLoginPresenter.formatErrorResponse(anyString())).thenReturn(errorResponse);

        // Act & Assert
        InvalidLoginException exception = assertThrows(InvalidLoginException.class,
            () -> loginInteractor.execute(inputData));
        assertEquals("User not found", exception.getMessage());
    }
}
