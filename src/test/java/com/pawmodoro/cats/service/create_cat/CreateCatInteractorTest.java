package com.pawmodoro.cats.service.create_cat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.entity.CatAlreadyExistsException;
import com.pawmodoro.cats.entity.CatFactory;
import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;

@ExtendWith(MockitoExtension.class)
class CreateCatInteractorTest {

    private static final String VALID_IMAGE = "cat-1.png";
    private static final String VALID_NAME = "Whiskers";
    private static final String VALID_USERNAME = "testuser";

    @Mock
    private CatFactory catFactory;

    @Mock
    private CreateCatDataAccessInterface dataAccess;

    @Mock
    private CreateCatOutputBoundary outputBoundary;

    @InjectMocks
    private CreateCatInteractor interactor;

    @Nested
    @DisplayName("Create Cat Success Tests")
    class CreateCatSuccessTests {

        @Test
        @DisplayName("Should create cat successfully with image file")
        void shouldCreateCatSuccessfullyWithImage() throws DatabaseAccessException, CatAlreadyExistsException {
            // Arrange
            final CreateCatInputData inputData = new CreateCatInputData(
                VALID_NAME,
                VALID_USERNAME,
                VALID_IMAGE);

            final Cat createdCat = new Cat(VALID_NAME, VALID_USERNAME, VALID_IMAGE);
            when(catFactory.create(VALID_NAME, VALID_USERNAME, VALID_IMAGE)).thenReturn(createdCat);
            when(dataAccess.saveCat(createdCat)).thenReturn(createdCat);

            // Act
            interactor.execute(inputData);

            // Assert
            verify(catFactory).create(VALID_NAME, VALID_USERNAME, VALID_IMAGE);
            verify(dataAccess).saveCat(createdCat);

            final ArgumentCaptor<CreateCatOutputData> outputDataCaptor = forClass(CreateCatOutputData.class);
            verify(outputBoundary).prepareResponse(outputDataCaptor.capture());
            final Cat resultCat = outputDataCaptor.getValue().getCreatedCat();

            assertEquals(VALID_NAME, resultCat.getName(), "Cat name should match input");
            assertEquals(VALID_USERNAME, resultCat.getOwnerUsername(), "Owner username should match input");
            assertEquals(VALID_IMAGE, resultCat.getImageFileName(), "Image file name should match input");
            assertEquals(createdCat.getHappinessLevel(), resultCat.getHappinessLevel(),
                "Happiness level should match created cat");
            assertEquals(createdCat.getHungerLevel(), resultCat.getHungerLevel(),
                "Hunger level should match created cat");
        }

        @Test
        @DisplayName("Should create cat successfully without image file")
        void shouldCreateCatSuccessfullyWithoutImage() throws DatabaseAccessException, CatAlreadyExistsException {
            // Arrange
            final CreateCatInputData inputData = new CreateCatInputData(
                VALID_NAME,
                VALID_USERNAME,
                null);

            final Cat createdCat = new Cat(VALID_NAME, VALID_USERNAME);
            when(catFactory.create(VALID_NAME, VALID_USERNAME)).thenReturn(createdCat);
            when(dataAccess.saveCat(createdCat)).thenReturn(createdCat);

            // Act
            interactor.execute(inputData);

            // Assert
            verify(catFactory).create(VALID_NAME, VALID_USERNAME);
            verify(dataAccess).saveCat(createdCat);

            final ArgumentCaptor<CreateCatOutputData> outputDataCaptor = forClass(CreateCatOutputData.class);
            verify(outputBoundary).prepareResponse(outputDataCaptor.capture());
            final Cat resultCat = outputDataCaptor.getValue().getCreatedCat();

            assertEquals(VALID_NAME, resultCat.getName(), "Cat name should match input");
            assertEquals(VALID_USERNAME, resultCat.getOwnerUsername(), "Owner username should match input");
            assertNotNull(resultCat.getImageFileName(), "Image file name should be assigned");
            assertTrue(resultCat.getImageFileName().matches("cat-[1-5]\\.png"),
                "Image file name should match pattern");
            assertEquals(createdCat.getHappinessLevel(), resultCat.getHappinessLevel(),
                "Happiness level should match created cat");
            assertEquals(createdCat.getHungerLevel(), resultCat.getHungerLevel(),
                "Hunger level should match created cat");
        }
    }

    @Nested
    @DisplayName("Create Cat Error Tests")
    class CreateCatErrorTests {

        @Test
        @DisplayName("Should throw authentication error")
        void shouldThrowAuthenticationError() throws DatabaseAccessException, CatAlreadyExistsException {
            // Arrange
            final CreateCatInputData inputData = new CreateCatInputData(
                VALID_NAME,
                VALID_USERNAME,
                VALID_IMAGE);

            final Cat createdCat = new Cat(VALID_NAME, VALID_USERNAME, VALID_IMAGE);
            when(catFactory.create(VALID_NAME, VALID_USERNAME, VALID_IMAGE)).thenReturn(createdCat);
            when(dataAccess.saveCat(createdCat))
                .thenThrow(new AuthenticationException("Authentication failed"));

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
        void shouldThrowDatabaseError() throws DatabaseAccessException, CatAlreadyExistsException {
            // Arrange
            final CreateCatInputData inputData = new CreateCatInputData(
                VALID_NAME,
                VALID_USERNAME,
                VALID_IMAGE);

            final Cat createdCat = new Cat(VALID_NAME, VALID_USERNAME, VALID_IMAGE);
            when(catFactory.create(VALID_NAME, VALID_USERNAME, VALID_IMAGE)).thenReturn(createdCat);
            when(dataAccess.saveCat(createdCat))
                .thenThrow(new DatabaseAccessException("Database error"));

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
