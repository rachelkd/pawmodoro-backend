package com.pawmodoro.users.service.signup;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.users.entity.CommonUserFactory;
import com.pawmodoro.users.entity.User;
import com.pawmodoro.users.entity.UserFactory;
import com.pawmodoro.users.service.signup.interface_adapter.SignupResponseDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class SignupInteractorTest {

    @Mock
    private SignupUserDataAccessInterface userDataAccess;

    @Mock
    private SignupOutputBoundary signupPresenter;

    private SignupInteractor signupInteractor;
    private UserFactory userFactory;

    @BeforeEach
    void setUp() {
        userFactory = new CommonUserFactory();
        signupInteractor = new SignupInteractor(userDataAccess, signupPresenter, userFactory);
    }

    @Test
    void execute_WithValidData_SavesUserAndReturnsSuccess() throws DatabaseAccessException {
        // Arrange
        SignupInputData inputData = new SignupInputData(
            "testuser",
            "test@example.com",
            "password123",
            "password123");
        SignupResponseDTO expectedResponse = SignupResponseDTO.success("testuser");

        when(userDataAccess.existsByName("testuser")).thenReturn(false);
        when(signupPresenter.prepareResponse(any(SignupOutputData.class))).thenReturn(expectedResponse);

        // Act
        SignupResponseDTO response = signupInteractor.execute(inputData);

        // Assert
        assertEquals(expectedResponse, response);
        verify(userDataAccess).save(any(User.class), eq("password123"));
        verify(signupPresenter).prepareResponse(any(SignupOutputData.class));
    }

    @Test
    void execute_WithExistingUsername_ThrowsException() throws DatabaseAccessException {
        // Arrange
        SignupInputData inputData = new SignupInputData(
            "existinguser",
            "test@example.com",
            "password123",
            "password123");
        when(userDataAccess.existsByName("existinguser")).thenReturn(true);

        // Act & Assert
        InvalidSignupException exception = assertThrows(InvalidSignupException.class,
            () -> signupInteractor.execute(inputData));
        assertEquals("Username is already taken", exception.getMessage());
    }

    @Test
    void execute_WithDatabaseError_PropagatesException() throws DatabaseAccessException {
        // Arrange
        SignupInputData inputData = new SignupInputData(
            "testuser",
            "test@example.com",
            "password123",
            "password123");
        when(userDataAccess.existsByName("testuser")).thenReturn(false);
        doThrow(new DatabaseAccessException("Database connection failed"))
            .when(userDataAccess).save(any(User.class), any(String.class));

        // Act & Assert
        DatabaseAccessException exception = assertThrows(DatabaseAccessException.class,
            () -> signupInteractor.execute(inputData));
        assertEquals("Database connection failed", exception.getMessage());
    }

    @Test
    void execute_WithEmptyUsername_ThrowsException() {
        // Arrange
        SignupInputData inputData = new SignupInputData(
            "",
            "test@example.com",
            "password123",
            "password123");
        // Act & Assert
        InvalidSignupException exception = assertThrows(InvalidSignupException.class,
            () -> signupInteractor.execute(inputData));
        assertEquals("Username is required", exception.getMessage());
    }

    @Test
    void execute_WithNullUsername_ThrowsException() {
        // Arrange
        SignupInputData inputData = new SignupInputData(
            null,
            "test@example.com",
            "password123",
            "password123");
        // Act & Assert
        InvalidSignupException exception = assertThrows(InvalidSignupException.class,
            () -> signupInteractor.execute(inputData));
        assertEquals("Username is required", exception.getMessage());
    }

    @Test
    void execute_WithInvalidEmail_ThrowsException() {
        // Arrange
        SignupInputData inputData = new SignupInputData(
            "testuser",
            "notanemail",
            "password123",
            "password123");
        // Act & Assert
        InvalidSignupException exception = assertThrows(InvalidSignupException.class,
            () -> signupInteractor.execute(inputData));
        assertEquals("Please enter a valid email address", exception.getMessage());
    }

    @Test
    void execute_WithEmptyEmail_ThrowsException() {
        // Arrange
        SignupInputData inputData = new SignupInputData(
            "testuser",
            "",
            "password123",
            "password123");
        // Act & Assert
        InvalidSignupException exception = assertThrows(InvalidSignupException.class,
            () -> signupInteractor.execute(inputData));
        assertEquals("Email is required", exception.getMessage());
    }

    @Test
    void execute_WithPasswordMismatch_ThrowsException() {
        // Arrange
        SignupInputData inputData = new SignupInputData(
            "testuser",
            "test@example.com",
            "password123",
            "password456");
        // Act & Assert
        InvalidSignupException exception = assertThrows(InvalidSignupException.class,
            () -> signupInteractor.execute(inputData));
        assertEquals("Passwords do not match", exception.getMessage());
    }

    @Test
    void execute_WithShortPassword_ThrowsException() {
        // Arrange
        SignupInputData inputData = new SignupInputData(
            "testuser",
            "test@example.com",
            "pass",
            "pass");
        // Act & Assert
        InvalidSignupException exception = assertThrows(InvalidSignupException.class,
            () -> signupInteractor.execute(inputData));
        assertEquals("Password must be at least 6 characters", exception.getMessage());
    }

    @Test
    void execute_WithEmptyPassword_ThrowsException() {
        // Arrange
        SignupInputData inputData = new SignupInputData(
            "testuser",
            "test@example.com",
            "",
            "");
        // Act & Assert
        InvalidSignupException exception = assertThrows(InvalidSignupException.class,
            () -> signupInteractor.execute(inputData));
        assertEquals("Password is required", exception.getMessage());
    }

    @Test
    void execute_WithEmptyPasswordConfirmation_ThrowsException() {
        // Arrange
        SignupInputData inputData = new SignupInputData(
            "testuser",
            "test@example.com",
            "p",
            "");
        // Act & Assert
        InvalidSignupException exception = assertThrows(InvalidSignupException.class,
            () -> signupInteractor.execute(inputData));
        assertEquals("Password confirmation is required", exception.getMessage());

    }
}
