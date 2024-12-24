package com.pawmodoro.settings.service.get_user_settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.settings.entity.UserSettings;
import com.pawmodoro.settings.service.get_user_settings.interface_adapter.GetUserSettingsResponseDto;
import com.pawmodoro.users.entity.UserNotFoundException;

/**
 * Unit tests for GetUserSettingsInteractor.
 * Tests both successful and error scenarios for retrieving user settings.
 */
@ExtendWith(MockitoExtension.class)
class GetUserSettingsInteractorTest {

    private static final boolean TEST_AUTO_START_BREAKS = false;
    private static final boolean TEST_AUTO_START_FOCUS = false;
    private static final int TEST_FOCUS_DURATION = 25;
    private static final int TEST_LONG_BREAK = 15;
    private static final int TEST_SHORT_BREAK = 5;
    private static final String TEST_ACCESS_TOKEN = "test-token";
    private static final String TEST_USERNAME = "testuser";

    @Mock
    private GetUserSettingsDataAccessInterface userSettingsDataAccessObject;

    @Mock
    private GetUserSettingsOutputBoundary getUserSettingsPresenter;

    private GetUserSettingsInteractor interactor;

    @BeforeEach
    void setUp() {
        interactor = new GetUserSettingsInteractor(userSettingsDataAccessObject, getUserSettingsPresenter);
    }

    @Nested
    @DisplayName("execute")
    class Execute {

        @Test
        @DisplayName("should return user settings when user exists")
        void shouldReturnUserSettingsWhenUserExists() throws UserNotFoundException, DatabaseAccessException {
            // Arrange
            final GetUserSettingsInputData inputData = new GetUserSettingsInputData(
                TEST_USERNAME,
                TEST_ACCESS_TOKEN);

            final UserSettings mockUserSettings = new UserSettings(
                TEST_USERNAME,
                TEST_FOCUS_DURATION,
                TEST_SHORT_BREAK,
                TEST_LONG_BREAK,
                TEST_AUTO_START_BREAKS,
                TEST_AUTO_START_FOCUS);

            final GetUserSettingsResponseDto expectedResponse = new GetUserSettingsResponseDto(
                TEST_USERNAME,
                TEST_FOCUS_DURATION,
                TEST_SHORT_BREAK,
                TEST_LONG_BREAK,
                TEST_AUTO_START_BREAKS,
                TEST_AUTO_START_FOCUS);

            when(userSettingsDataAccessObject.getUserSettings(TEST_USERNAME, TEST_ACCESS_TOKEN))
                .thenReturn(mockUserSettings);
            when(getUserSettingsPresenter.prepareResponse(any(GetUserSettingsOutputData.class)))
                .thenReturn(expectedResponse);

            // Act
            final GetUserSettingsResponseDto actualResponse = interactor.execute(inputData);

            // Assert
            assertEquals(expectedResponse, actualResponse);
            verify(userSettingsDataAccessObject).getUserSettings(TEST_USERNAME, TEST_ACCESS_TOKEN);
            verify(getUserSettingsPresenter).prepareResponse(any(GetUserSettingsOutputData.class));
        }

        @Test
        @DisplayName("should throw UserNotFoundException when user does not exist")
        void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist()
                throws DatabaseAccessException, UserNotFoundException {
            // Arrange
            final GetUserSettingsInputData inputData = new GetUserSettingsInputData(
                TEST_USERNAME,
                TEST_ACCESS_TOKEN);

            when(userSettingsDataAccessObject.getUserSettings(TEST_USERNAME, TEST_ACCESS_TOKEN))
                .thenThrow(new UserNotFoundException("User not found: " + TEST_USERNAME));

            // Act & Assert
            assertThrows(UserNotFoundException.class, () -> interactor.execute(inputData));
            verify(userSettingsDataAccessObject).getUserSettings(TEST_USERNAME, TEST_ACCESS_TOKEN);
        }

        @Test
        @DisplayName("should throw DatabaseAccessException when database access fails")
        void shouldThrowDatabaseAccessExceptionWhenDatabaseAccessFails()
                throws DatabaseAccessException, UserNotFoundException {
            // Arrange
            final GetUserSettingsInputData inputData = new GetUserSettingsInputData(
                TEST_USERNAME,
                TEST_ACCESS_TOKEN);

            when(userSettingsDataAccessObject.getUserSettings(TEST_USERNAME, TEST_ACCESS_TOKEN))
                .thenThrow(new DatabaseAccessException("Database access failed"));

            // Act & Assert
            assertThrows(DatabaseAccessException.class, () -> interactor.execute(inputData));
            verify(userSettingsDataAccessObject).getUserSettings(TEST_USERNAME, TEST_ACCESS_TOKEN);
        }
    }
}
