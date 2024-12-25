package com.pawmodoro.users.service.signup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.users.entity.AuthenticationToken;
import com.pawmodoro.users.entity.CommonUserFactory;
import com.pawmodoro.users.entity.EmailAlreadyRegisteredException;
import com.pawmodoro.users.entity.InvalidSignupException;
import com.pawmodoro.users.entity.User;
import com.pawmodoro.users.entity.UserFactory;
import com.pawmodoro.users.service.signup.interface_adapter.SignupResponseDto;

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
    void executeWithValidDataSavesUserAndReturnsSuccess() throws DatabaseAccessException {
        // Arrange
        final SignupInputData inputData = new SignupInputData(
            "testuser",
            "test@example.com",
            "password123",
            "password123");
        final AuthenticationToken tokens =
            new AuthenticationToken("token123", "refresh123", 3600, 1000000);
        final SignupResponseDto expectedResponse = SignupResponseDto.from(
            "testuser",
            tokens);

        when(userDataAccess.existsByName("testuser")).thenReturn(false);
        when(signupPresenter.prepareResponse(any(SignupOutputData.class))).thenReturn(expectedResponse);

        // Act
        final SignupResponseDto response = signupInteractor.execute(inputData);

        // Assert
        assertEquals(expectedResponse, response);
        verify(userDataAccess).save(any(User.class), eq("password123"));
        verify(signupPresenter).prepareResponse(any(SignupOutputData.class));
    }

    @Test
    void executeWithExistingUsernameThrowsInvalidSignupException() throws DatabaseAccessException {
        // Arrange
        final SignupInputData inputData = new SignupInputData(
            "existinguser",
            "test@example.com",
            "password123",
            "password123");

        when(userDataAccess.existsByName("existinguser")).thenReturn(true);

        // Act & Assert
        final InvalidSignupException exception = assertThrows(InvalidSignupException.class,
            () -> signupInteractor.execute(inputData));
        assertEquals("Username is already taken", exception.getMessage());
    }

    @Test
    void executeWithExistingEmailThrowsInvalidSignupException() throws DatabaseAccessException {
        // Arrange
        final SignupInputData inputData = new SignupInputData(
            "newuser",
            "existing@example.com",
            "password123",
            "password123");

        when(userDataAccess.existsByName("newuser")).thenReturn(false);
        when(userDataAccess.existsByEmail("existing@example.com")).thenReturn(true);

        // Act & Assert
        final EmailAlreadyRegisteredException exception = assertThrows(EmailAlreadyRegisteredException.class,
            () -> signupInteractor.execute(inputData));
        assertEquals("Email is already registered", exception.getMessage());
    }

    @Test
    void executeWithDatabaseErrorThrowsDatabaseAccessException() throws DatabaseAccessException {
        // Arrange
        final SignupInputData inputData = new SignupInputData(
            "testuser",
            "test@example.com",
            "password123",
            "password123");

        when(userDataAccess.existsByName("testuser")).thenReturn(false);
        doThrow(new DatabaseAccessException("Database connection failed"))
            .when(userDataAccess).save(any(User.class), any(String.class));

        // Act & Assert
        final DatabaseAccessException exception = assertThrows(DatabaseAccessException.class,
            () -> signupInteractor.execute(inputData));
        assertEquals("Database connection failed", exception.getMessage());
    }
}
