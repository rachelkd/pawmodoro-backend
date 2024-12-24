package com.pawmodoro.cats.service.delete_cat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;

@ExtendWith(MockitoExtension.class)
class DeleteCatInteractorTest {

    private static final String VALID_NAME = "Whiskers";
    private static final String VALID_USERNAME = "testuser";

    @Mock
    private DeleteCatDataAccessInterface dataAccess;

    @Mock
    private DeleteCatOutputBoundary outputBoundary;

    @InjectMocks
    private DeleteCatInteractor interactor;

    @Nested
    @DisplayName("Delete Cat Success Tests")
    class DeleteCatSuccessTests {

        @Test
        @DisplayName("Should delete cat successfully")
        void shouldDeleteCatSuccessfully() throws DatabaseAccessException {
            // Arrange
            final DeleteCatInputData inputData = new DeleteCatInputData(
                VALID_NAME,
                VALID_USERNAME);

            final DeleteCatOutputData expectedOutputData = new DeleteCatOutputData(
                "Cat deleted successfully",
                VALID_NAME);

            // Act
            interactor.execute(inputData);

            // Assert
            verify(dataAccess).deleteCat(VALID_NAME, VALID_USERNAME);

            final ArgumentCaptor<DeleteCatOutputData> outputDataCaptor = forClass(DeleteCatOutputData.class);
            verify(outputBoundary).prepareResponse(outputDataCaptor.capture());

            final DeleteCatOutputData actualOutputData = outputDataCaptor.getValue();
            assertEquals(expectedOutputData.getMessage(), actualOutputData.getMessage(),
                "Output message should match expected");
            assertEquals(expectedOutputData.getCatName(), actualOutputData.getCatName(),
                "Cat name in output should match input");
        }
    }

    @Nested
    @DisplayName("Delete Cat Error Tests")
    class DeleteCatErrorTests {

        @Test
        @DisplayName("Should throw authentication error")
        void shouldThrowAuthenticationError() throws DatabaseAccessException {
            // Arrange
            final DeleteCatInputData inputData = new DeleteCatInputData(
                VALID_NAME,
                VALID_USERNAME);

            doThrow(new AuthenticationException("Authentication failed"))
                .when(dataAccess)
                .deleteCat(VALID_NAME, VALID_USERNAME);

            // Act & Assert
            final AuthenticationException exception = assertThrows(
                AuthenticationException.class,
                () -> interactor.execute(inputData),
                "Should throw AuthenticationException");
            assertEquals("Authentication failed", exception.getMessage(),
                "Exception message should match");
        }

        @Test
        @DisplayName("Should throw database error")
        void shouldThrowDatabaseError() throws DatabaseAccessException {
            // Arrange
            final DeleteCatInputData inputData = new DeleteCatInputData(
                VALID_NAME,
                VALID_USERNAME);

            doThrow(new DatabaseAccessException("Database error"))
                .when(dataAccess)
                .deleteCat(VALID_NAME, VALID_USERNAME);

            // Act & Assert
            final DatabaseAccessException exception = assertThrows(
                DatabaseAccessException.class,
                () -> interactor.execute(inputData),
                "Should throw DatabaseAccessException");
            assertEquals("Database error", exception.getMessage(),
                "Exception message should match");
        }
    }
}
