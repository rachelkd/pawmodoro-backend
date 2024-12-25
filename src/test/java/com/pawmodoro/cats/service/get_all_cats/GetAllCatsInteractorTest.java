package com.pawmodoro.cats.service.get_all_cats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.entity.InvalidGetAllCatsException;
import com.pawmodoro.cats.service.get_all_cats.interface_adapter.GetAllCatsResponseDto;
import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Unit tests for GetAllCatsInteractor.
 */
@ExtendWith(MockitoExtension.class)
class GetAllCatsInteractorTest {
    private static final String TEST_USERNAME = "testuser";

    @Mock
    private GetAllCatsDataAccessInterface catDataAccessObject;

    @Mock
    private GetAllCatsOutputBoundary getAllCatsPresenter;

    @Mock
    private Cat mockCat;

    private GetAllCatsInteractor interactor;

    @BeforeEach
    void setUp() {
        interactor = new GetAllCatsInteractor(catDataAccessObject, getAllCatsPresenter);
    }

    @Test
    void executeWhenCatsExistReturnsSuccessResponse() throws DatabaseAccessException {
        // Arrange
        final Collection<Cat> cats = new ArrayList<>();
        cats.add(mockCat);

        final GetAllCatsInputData inputData = new GetAllCatsInputData(TEST_USERNAME);
        final GetAllCatsResponseDto expectedResponseDto =
            new GetAllCatsResponseDto(true, cats, "Successfully retrieved cats");

        when(catDataAccessObject.getCatsByOwner(TEST_USERNAME))
            .thenReturn(cats);
        when(getAllCatsPresenter.prepareResponse(any(GetAllCatsOutputData.class)))
            .thenReturn(expectedResponseDto);

        // Act
        final GetAllCatsResponseDto actualResponse = interactor.execute(inputData);

        // Assert
        assertNotNull(actualResponse);
        assertTrue(actualResponse.success());
        assertEquals(cats, actualResponse.cats());
        assertEquals("Successfully retrieved cats", actualResponse.message());

        verify(catDataAccessObject).getCatsByOwner(TEST_USERNAME);
        verify(getAllCatsPresenter).prepareResponse(any(GetAllCatsOutputData.class));
    }

    @Test
    void executeWhenNoCatsExistReturnsEmptySuccessResponse() throws DatabaseAccessException {
        // Arrange
        final Collection<Cat> emptyCats = new ArrayList<>();
        final GetAllCatsInputData inputData = new GetAllCatsInputData(TEST_USERNAME);
        final GetAllCatsResponseDto expectedResponseDto =
            new GetAllCatsResponseDto(true, emptyCats, "Successfully retrieved cats");

        when(catDataAccessObject.getCatsByOwner(TEST_USERNAME))
            .thenReturn(emptyCats);
        when(getAllCatsPresenter.prepareResponse(any(GetAllCatsOutputData.class)))
            .thenReturn(expectedResponseDto);

        // Act
        final GetAllCatsResponseDto actualResponse = interactor.execute(inputData);

        // Assert
        assertNotNull(actualResponse);
        assertTrue(actualResponse.success());
        assertTrue(actualResponse.cats().isEmpty());
        assertEquals("Successfully retrieved cats", actualResponse.message());

        verify(catDataAccessObject).getCatsByOwner(TEST_USERNAME);
        verify(getAllCatsPresenter).prepareResponse(any(GetAllCatsOutputData.class));
    }

    @Test
    void executeWhenNullUsernameThrowsException() throws DatabaseAccessException {
        // Arrange
        final GetAllCatsInputData inputData = new GetAllCatsInputData(null);

        // Act & Assert
        final InvalidGetAllCatsException exception = assertThrows(
            InvalidGetAllCatsException.class,
            () -> {
                interactor.execute(inputData);
            });

        assertEquals("Username cannot be null or empty", exception.getMessage());
        assertNotNull(exception.getResponse());
        assertFalse(exception.getResponse().success());
        assertNull(exception.getResponse().cats());
        assertEquals("Username cannot be null or empty", exception.getResponse().message());

        verify(catDataAccessObject, never()).getCatsByOwner(any());
        verify(getAllCatsPresenter, never()).prepareResponse(any());
    }

    @Test
    void executeWhenEmptyUsernameThrowsException() throws DatabaseAccessException {
        // Arrange
        final GetAllCatsInputData inputData = new GetAllCatsInputData("");

        // Act & Assert
        final InvalidGetAllCatsException exception = assertThrows(
            InvalidGetAllCatsException.class,
            () -> {
                interactor.execute(inputData);
            });

        assertEquals("Username cannot be null or empty", exception.getMessage());
        assertNotNull(exception.getResponse());
        assertFalse(exception.getResponse().success());
        assertNull(exception.getResponse().cats());
        assertEquals("Username cannot be null or empty", exception.getResponse().message());

        verify(catDataAccessObject, never()).getCatsByOwner(any());
        verify(getAllCatsPresenter, never()).prepareResponse(any());
    }

    @Test
    void executeWhenUnauthorizedThrowsCatAuthenticationException() throws DatabaseAccessException {
        // Arrange
        final GetAllCatsInputData inputData = new GetAllCatsInputData(TEST_USERNAME);
        when(catDataAccessObject.getCatsByOwner(TEST_USERNAME))
            .thenThrow(new AuthenticationException("User not authorized to view these cats"));

        // Act & Assert
        final AuthenticationException exception = assertThrows(
            AuthenticationException.class,
            () -> {
                interactor.execute(inputData);
            });

        assertEquals("User not authorized to view these cats", exception.getMessage());
        verify(catDataAccessObject).getCatsByOwner(TEST_USERNAME);
        verify(getAllCatsPresenter, never()).prepareResponse(any());
    }
}
