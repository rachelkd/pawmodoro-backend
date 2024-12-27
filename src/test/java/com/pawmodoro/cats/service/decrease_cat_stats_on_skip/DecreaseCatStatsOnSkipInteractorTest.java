package com.pawmodoro.cats.service.decrease_cat_stats_on_skip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.service.decrease_cat_stats_on_skip.interface_adapter.DecreaseCatStatsOnSkipResponseDto;
import com.pawmodoro.cats.service.decrease_cat_stats_on_skip.interface_adapter.DecreaseCatStatsOnSkipResponseDto.CatDto;
import com.pawmodoro.constants.Constants;
import com.pawmodoro.core.DatabaseAccessException;

@ExtendWith(MockitoExtension.class)
class DecreaseCatStatsOnSkipInteractorTest {

    @Mock
    private DecreaseCatStatsOnSkipDataAccessInterface dataAccess;

    @Mock
    private DecreaseCatStatsOnSkipOutputBoundary outputBoundary;

    private DecreaseCatStatsOnSkipInteractor interactor;
    private static final String TOKEN = "test-token";
    private static final String USERNAME = "testuser";
    private static final String CAT_NAME = "TestCat";
    private static final String IMAGE_FILE = "cat-1.png";

    @Captor
    private ArgumentCaptor<DecreaseCatStatsOnSkipOutputData> outputDataCaptor;

    @BeforeEach
    void setUp() {
        interactor = new DecreaseCatStatsOnSkipInteractor(dataAccess, outputBoundary);
    }

    @Test
    void executeWhenNoCatsReturnsNoCatsFoundMessage() throws DatabaseAccessException {
        // Arrange
        when(dataAccess.getUsernameFromToken(TOKEN)).thenReturn(USERNAME);
        when(dataAccess.getCatsByOwner(USERNAME)).thenReturn(List.of());

        when(outputBoundary.prepareResponse(any(DecreaseCatStatsOnSkipOutputData.class)))
            .thenReturn(new DecreaseCatStatsOnSkipResponseDto(
                null,
                false,
                "No cats found to update"));

        // Act
        final DecreaseCatStatsOnSkipResponseDto result = interactor.execute(
            new DecreaseCatStatsOnSkipInputData(TOKEN));

        // Assert
        assertFalse(result.isDeleted());
        assertNull(result.updatedCat());
        assertEquals("No cats found to update", result.message());

        // Verify output data passed to presenter
        verify(outputBoundary).prepareResponse(outputDataCaptor.capture());
        DecreaseCatStatsOnSkipOutputData actualOutputData = outputDataCaptor.getValue();
        assertNull(actualOutputData.getCatName());
        assertEquals(USERNAME, actualOutputData.getOwnerUsername());
        assertEquals(0, actualOutputData.getHappinessLevel());
        assertEquals(0, actualOutputData.getHungerLevel());
        assertNull(actualOutputData.getImageFileName());
        assertFalse(actualOutputData.isDeleted());
        assertEquals("No cats found to update", actualOutputData.getMessage());
    }

    @Test
    void executeWhenCatStatsAboveZeroUpdatesStatsAndReturnsSuccess() throws DatabaseAccessException {
        // Arrange
        final Cat initialCat = new Cat(CAT_NAME, USERNAME, 100, 100, IMAGE_FILE);
        final int expectedDecrease = (int) (100 * Constants.CatStats.SKIP_PENALTY_PERCENTAGE);
        final Cat updatedCat = new Cat(CAT_NAME, USERNAME, 90, 90, IMAGE_FILE);

        when(dataAccess.getUsernameFromToken(TOKEN)).thenReturn(USERNAME);
        when(dataAccess.getCatsByOwner(USERNAME)).thenReturn(List.of(initialCat));
        when(dataAccess.updateHappiness(CAT_NAME, USERNAME, -expectedDecrease))
            .thenReturn(new Cat(CAT_NAME, USERNAME, 90, 100, IMAGE_FILE));
        when(dataAccess.updateHunger(CAT_NAME, USERNAME, -expectedDecrease))
            .thenReturn(updatedCat);

        when(outputBoundary.prepareResponse(any(DecreaseCatStatsOnSkipOutputData.class)))
            .thenReturn(new DecreaseCatStatsOnSkipResponseDto(
                new CatDto(CAT_NAME, USERNAME, 90, 90, IMAGE_FILE),
                false,
                String.format("Cat %s's stats were decreased", CAT_NAME)));

        // Act
        final DecreaseCatStatsOnSkipResponseDto result = interactor.execute(
            new DecreaseCatStatsOnSkipInputData(TOKEN));

        // Assert
        assertFalse(result.isDeleted());
        assertEquals(CAT_NAME, result.updatedCat().catName());
        assertEquals(USERNAME, result.updatedCat().ownerUsername());
        assertEquals(90, result.updatedCat().happinessLevel());
        assertEquals(90, result.updatedCat().hungerLevel());
        assertEquals(IMAGE_FILE, result.updatedCat().imageFileName());
        assertEquals(String.format("Cat %s's stats were decreased", CAT_NAME), result.message());

        // Verify interactions
        verify(dataAccess).updateHappiness(CAT_NAME, USERNAME, -expectedDecrease);
        verify(dataAccess).updateHunger(CAT_NAME, USERNAME, -expectedDecrease);

        // Verify output data passed to presenter
        verify(outputBoundary).prepareResponse(outputDataCaptor.capture());
        DecreaseCatStatsOnSkipOutputData actualOutputData = outputDataCaptor.getValue();
        assertEquals(CAT_NAME, actualOutputData.getCatName());
        assertEquals(USERNAME, actualOutputData.getOwnerUsername());
        assertEquals(90, actualOutputData.getHappinessLevel());
        assertEquals(90, actualOutputData.getHungerLevel());
        assertEquals(IMAGE_FILE, actualOutputData.getImageFileName());
        assertFalse(actualOutputData.isDeleted());
        assertEquals(String.format("Cat %s's stats were decreased", CAT_NAME), actualOutputData.getMessage());
    }

    @Test
    void executeWhenCatStatsDropToZeroDeletesCatAndReturnsDeleted() throws DatabaseAccessException {
        // Arrange
        final Cat initialCat = new Cat(CAT_NAME, USERNAME, 10, 10, IMAGE_FILE);
        final int expectedDecrease = (int) (10 * Constants.CatStats.SKIP_PENALTY_PERCENTAGE);
        final Cat updatedCat = new Cat(CAT_NAME, USERNAME, 0, 0, IMAGE_FILE);

        when(dataAccess.getUsernameFromToken(TOKEN)).thenReturn(USERNAME);
        when(dataAccess.getCatsByOwner(USERNAME)).thenReturn(List.of(initialCat));
        when(dataAccess.updateHappiness(anyString(), anyString(), anyInt()))
            .thenReturn(new Cat(CAT_NAME, USERNAME, 0, 10, IMAGE_FILE));
        when(dataAccess.updateHunger(anyString(), anyString(), anyInt()))
            .thenReturn(updatedCat);

        when(outputBoundary.prepareResponse(any(DecreaseCatStatsOnSkipOutputData.class)))
            .thenReturn(new DecreaseCatStatsOnSkipResponseDto(
                null,
                true,
                String.format("Cat %s ran away due to neglect!", CAT_NAME)));

        // Act
        final DecreaseCatStatsOnSkipResponseDto result = interactor.execute(
            new DecreaseCatStatsOnSkipInputData(TOKEN));

        // Assert
        assertTrue(result.isDeleted());
        assertNull(result.updatedCat());
        assertEquals(String.format("Cat %s ran away due to neglect!", CAT_NAME), result.message());

        // Verify interactions
        verify(dataAccess).updateHappiness(CAT_NAME, USERNAME, -expectedDecrease);
        verify(dataAccess).updateHunger(CAT_NAME, USERNAME, -expectedDecrease);
        verify(dataAccess).deleteCat(CAT_NAME, USERNAME);

        // Verify output data passed to presenter
        verify(outputBoundary).prepareResponse(outputDataCaptor.capture());
        DecreaseCatStatsOnSkipOutputData actualOutputData = outputDataCaptor.getValue();
        assertEquals(CAT_NAME, actualOutputData.getCatName());
        assertEquals(USERNAME, actualOutputData.getOwnerUsername());
        assertEquals(0, actualOutputData.getHappinessLevel());
        assertEquals(0, actualOutputData.getHungerLevel());
        assertEquals(IMAGE_FILE, actualOutputData.getImageFileName());
        assertTrue(actualOutputData.isDeleted());
        assertEquals(String.format("Cat %s ran away due to neglect!", CAT_NAME), actualOutputData.getMessage());
    }
}
