package com.pawmodoro.users.service.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.users.entity.AuthenticatedUser;
import com.pawmodoro.users.entity.AuthenticationToken;
import com.pawmodoro.users.entity.CommonUserFactory;
import com.pawmodoro.users.entity.User;
import com.pawmodoro.users.entity.UserFactory;
import com.pawmodoro.users.entity.UserNotFoundException;
import com.pawmodoro.users.service.login.interface_adapter.LoginResponseDto;

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
    void executeWithValidCredentialsReturnsSuccessResponse() throws DatabaseAccessException, UserNotFoundException {
        // Arrange
        final LoginInputData inputData = new LoginInputData("testuser", "password123");
        final User user = userFactory.create("testuser", "test@example.com");
        final AuthenticationToken tokens =
            new AuthenticationToken("token123", "refresh123", 3600, 1000000);
        final AuthenticatedUser authenticatedUser = new AuthenticatedUser(user, tokens);
        final LoginResponseDto expectedResponse = LoginResponseDto.from("testuser", tokens);

        when(mockUserDataAccess.get("testuser")).thenReturn(user);
        when(mockUserDataAccess.authenticate("test@example.com", "password123"))
            .thenReturn(authenticatedUser);
        when(mockLoginPresenter.prepareResponse(any(LoginOutputData.class)))
            .thenReturn(expectedResponse);

        // Act
        final LoginResponseDto response = loginInteractor.execute(inputData);

        // Assert
        assertEquals(expectedResponse, response);
        verify(mockUserDataAccess).authenticate("test@example.com", "password123");
        verify(mockLoginPresenter).prepareResponse(any(LoginOutputData.class));
    }

    @Test
    void executeWithNonexistentUserThrowsException() throws DatabaseAccessException, UserNotFoundException {
        // Arrange
        final LoginInputData inputData = new LoginInputData("nonexistent", "password123");

        when(mockUserDataAccess.get("nonexistent"))
            .thenThrow(new UserNotFoundException("User not found"));

        // Act & Assert
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class,
            () -> loginInteractor.execute(inputData));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void executeWithIncorrectPasswordThrowsException() throws DatabaseAccessException, UserNotFoundException {
        // Arrange
        final LoginInputData inputData = new LoginInputData("testuser", "wrongpassword");
        final User user = userFactory.create("testuser", "test@example.com");

        when(mockUserDataAccess.get("testuser")).thenReturn(user);
        when(mockUserDataAccess.authenticate("test@example.com", "wrongpassword"))
            .thenThrow(new UserNotFoundException("Wrong password"));

        // Act & Assert
        final InvalidLoginException exception = assertThrows(InvalidLoginException.class,
            () -> loginInteractor.execute(inputData));
        assertEquals("Wrong password", exception.getMessage());
    }

    @Test
    void executeWithDatabaseErrorThrowsException() throws DatabaseAccessException, UserNotFoundException {
        // Arrange
        final LoginInputData inputData = new LoginInputData("testuser", "password123");

        when(mockUserDataAccess.get("testuser"))
            .thenThrow(new DatabaseAccessException("Database error"));

        // Act & Assert
        final DatabaseAccessException exception = assertThrows(DatabaseAccessException.class,
            () -> loginInteractor.execute(inputData));
        assertEquals("Database error", exception.getMessage());
    }
}
