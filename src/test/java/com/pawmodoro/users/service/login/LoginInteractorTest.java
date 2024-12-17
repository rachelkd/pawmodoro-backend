package com.pawmodoro.users.service.login;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.users.entity.CommonUserFactory;
import com.pawmodoro.users.entity.User;
import com.pawmodoro.users.entity.UserFactory;
import com.pawmodoro.users.entity.UserNotFoundException;
import com.pawmodoro.users.service.login.InvalidLoginException;
import com.pawmodoro.users.service.login.LoginInputData;
import com.pawmodoro.users.service.login.LoginInteractor;
import com.pawmodoro.users.service.login.LoginOutputBoundary;
import com.pawmodoro.users.service.login.LoginOutputData;
import com.pawmodoro.users.service.login.LoginUserDataAccessInterface;
import com.pawmodoro.users.service.login.interface_adapter.LoginResponseDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

        when(mockUserDataAccess.get("testuser")).thenReturn(user);
        when(mockUserDataAccess.authenticate("test@example.com", "password123")).thenReturn(user);
        when(mockUserDataAccess.getAccessToken()).thenReturn("token123");
        when(mockLoginPresenter.prepareResponse(any(LoginOutputData.class))).thenReturn(expectedResponse);

        // Act
        LoginResponseDTO response = loginInteractor.execute(inputData);

        // Assert
        assertEquals(expectedResponse, response);
        verify(mockUserDataAccess).authenticate("test@example.com", "password123");
        verify(mockLoginPresenter).prepareResponse(any(LoginOutputData.class));
    }

    @Test
    void execute_WithNonexistentUser_ThrowsUserNotFoundException()
        throws DatabaseAccessException, UserNotFoundException {
        // Arrange
        LoginInputData inputData = new LoginInputData("nonexistent", "password123");

        when(mockUserDataAccess.get("nonexistent"))
            .thenThrow(new UserNotFoundException("User not found"));

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
            () -> loginInteractor.execute(inputData));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void execute_WithIncorrectPassword_ThrowsUserNotFoundException()
        throws DatabaseAccessException, UserNotFoundException {
        // Arrange
        LoginInputData inputData = new LoginInputData("testuser", "wrongpassword");
        User user = userFactory.create("testuser", "test@example.com");

        when(mockUserDataAccess.get("testuser")).thenReturn(user);
        when(mockUserDataAccess.authenticate("test@example.com", "wrongpassword"))
            .thenThrow(new UserNotFoundException("Authentication failed"));

        // Act & Assert
        InvalidLoginException exception = assertThrows(InvalidLoginException.class,
            () -> loginInteractor.execute(inputData));
        assertEquals("Wrong password", exception.getMessage());
    }

    @Test
    void execute_WithDatabaseError_ThrowsDatabaseAccessException()
        throws DatabaseAccessException, UserNotFoundException {
        // Arrange
        LoginInputData inputData = new LoginInputData("testuser", "password123");

        when(mockUserDataAccess.get("testuser"))
            .thenThrow(new DatabaseAccessException("Database error"));

        // Act & Assert
        DatabaseAccessException exception = assertThrows(DatabaseAccessException.class,
            () -> loginInteractor.execute(inputData));
        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void execute_WithEmptyUsername_ThrowsInvalidLoginException() {
        // Arrange
        LoginInputData inputData = new LoginInputData("", "password123");

        // Act & Assert
        InvalidLoginException exception = assertThrows(InvalidLoginException.class,
            () -> loginInteractor.execute(inputData));
        assertEquals("Username is required", exception.getMessage());
    }

    @Test
    void execute_WithEmptyPassword_ThrowsInvalidLoginException() {
        // Arrange
        LoginInputData inputData = new LoginInputData("testuser", "");

        // Act & Assert
        InvalidLoginException exception = assertThrows(InvalidLoginException.class,
            () -> loginInteractor.execute(inputData));
        assertEquals("Password is required", exception.getMessage());
    }
}
