package com.pawmodoro.cats.service.get_all_cats;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.service.get_all_cats.interface_adapter.GetAllCatsResponseDTO;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Unit tests for GetAllCatsInteractor.
 */
@ExtendWith(MockitoExtension.class)
class GetAllCatsInteractorTest {

    @Mock
    private GetAllCatsDataAccessInterface catDataAccessObject;

    @Mock
    private GetAllCatsOutputBoundary getAllCatsPresenter;

    @Mock
    private Cat mockCat;

    private GetAllCatsInteractor interactor;
    private static final String TEST_USERNAME = "testuser";

    @BeforeEach
    void setUp() {
        interactor = new GetAllCatsInteractor(catDataAccessObject, getAllCatsPresenter);
    }

    @Test
    void execute_WhenCatsExist_ReturnsSuccessResponse() throws DatabaseAccessException {
        // Arrange
        Collection<Cat> cats = new ArrayList<>();
        cats.add(mockCat);

        GetAllCatsInputData inputData = new GetAllCatsInputData(TEST_USERNAME);
        GetAllCatsResponseDTO expectedResponseDTO =
            new GetAllCatsResponseDTO(true, cats, "Successfully retrieved cats");

        when(catDataAccessObject.getCatsByOwner(TEST_USERNAME)).thenReturn(cats);
        when(getAllCatsPresenter.prepareSuccessResponse(any(GetAllCatsOutputData.class)))
            .thenReturn(expectedResponseDTO);

        // Act
        GetAllCatsResponseDTO actualResponse = interactor.execute(inputData);

        // Assert
        assertNotNull(actualResponse);
        assertTrue(actualResponse.success());
        assertEquals(cats, actualResponse.cats());
        assertEquals("Successfully retrieved cats", actualResponse.message());

        verify(catDataAccessObject).getCatsByOwner(TEST_USERNAME);
        verify(getAllCatsPresenter).prepareSuccessResponse(any(GetAllCatsOutputData.class));
    }

    @Test
    void execute_WhenNoCatsExist_ReturnsEmptySuccessResponse() throws DatabaseAccessException {
        // Arrange
        Collection<Cat> emptyCats = new ArrayList<>();
        GetAllCatsInputData inputData = new GetAllCatsInputData(TEST_USERNAME);
        GetAllCatsResponseDTO expectedResponseDTO =
            new GetAllCatsResponseDTO(true, emptyCats, "Successfully retrieved cats");

        when(catDataAccessObject.getCatsByOwner(TEST_USERNAME)).thenReturn(emptyCats);
        when(getAllCatsPresenter.prepareSuccessResponse(any(GetAllCatsOutputData.class)))
            .thenReturn(expectedResponseDTO);

        // Act
        GetAllCatsResponseDTO actualResponse = interactor.execute(inputData);

        // Assert
        assertNotNull(actualResponse);
        assertTrue(actualResponse.success());
        assertTrue(actualResponse.cats().isEmpty());
        assertEquals("Successfully retrieved cats", actualResponse.message());

        verify(catDataAccessObject).getCatsByOwner(TEST_USERNAME);
        verify(getAllCatsPresenter).prepareSuccessResponse(any(GetAllCatsOutputData.class));
    }

    @Test
    void execute_WhenNullUsername_ThrowsException() throws DatabaseAccessException {
        // Arrange
        GetAllCatsInputData inputData = new GetAllCatsInputData(null);

        // Act & Assert
        InvalidGetAllCatsException exception = assertThrows(InvalidGetAllCatsException.class, () -> {
            interactor.execute(inputData);
        });

        assertEquals("Username cannot be null or empty", exception.getMessage());
        assertNotNull(exception.getResponse());
        assertFalse(exception.getResponse().success());
        assertNull(exception.getResponse().cats());
        assertEquals("Username cannot be null or empty", exception.getResponse().message());

        verify(catDataAccessObject, never()).getCatsByOwner(any());
        verify(getAllCatsPresenter, never()).prepareSuccessResponse(any());
        verify(getAllCatsPresenter, never()).prepareFailResponse(any());
    }

    @Test
    void execute_WhenEmptyUsername_ThrowsException() throws DatabaseAccessException {
        // Arrange
        GetAllCatsInputData inputData = new GetAllCatsInputData("");

        // Act & Assert
        InvalidGetAllCatsException exception = assertThrows(InvalidGetAllCatsException.class, () -> {
            interactor.execute(inputData);
        });

        assertEquals("Username cannot be null or empty", exception.getMessage());
        assertNotNull(exception.getResponse());
        assertFalse(exception.getResponse().success());
        assertNull(exception.getResponse().cats());
        assertEquals("Username cannot be null or empty", exception.getResponse().message());

        verify(catDataAccessObject, never()).getCatsByOwner(any());
        verify(getAllCatsPresenter, never()).prepareSuccessResponse(any());
        verify(getAllCatsPresenter, never()).prepareFailResponse(any());
    }
}
