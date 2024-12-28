package com.pawmodoro.cats.service.update_cats_after_study;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.data_access.CatUpdateDataAccess.CatUpdateResult;
import com.pawmodoro.cats.interface_adapter.CatDto;
import com.pawmodoro.cats.service.update_cats_after_study.interface_adapter.UpdateCatsAfterStudyResponseDto;
import com.pawmodoro.constants.Constants;
import com.pawmodoro.core.DatabaseAccessException;

@ExtendWith(MockitoExtension.class)
class UpdateCatsAfterStudyInteractorTest {

    @Mock
    private UpdateCatsAfterStudyDataAccessInterface dataAccess;

    @Mock
    private UpdateCatsAfterStudyOutputBoundary outputBoundary;

    private UpdateCatsAfterStudyInteractor interactor;
    private static final String TOKEN = "test-token";
    private static final String USERNAME = "testuser";
    private static final String CAT_NAME = "TestCat";
    private static final String IMAGE_FILE = "cat-1.png";

    @Captor
    private ArgumentCaptor<UpdateCatsAfterStudyOutputData> outputDataCaptor;

    @BeforeEach
    void setUp() {
        interactor = new UpdateCatsAfterStudyInteractor(dataAccess, outputBoundary);
    }

    @Test
    void executeWhenNoCatsReturnsEmptyList() throws DatabaseAccessException {
        // Arrange
        when(dataAccess.getUsernameFromToken(TOKEN)).thenReturn(USERNAME);
        when(dataAccess.getCatsByOwner(USERNAME)).thenReturn(List.of());
        when(dataAccess.updateCatsHappiness(any()))
            .thenReturn(new CatUpdateResult(List.of(), List.of()));

        when(outputBoundary.prepareResponse(any(UpdateCatsAfterStudyOutputData.class)))
            .thenReturn(new UpdateCatsAfterStudyResponseDto(List.of(), List.of()));

        // Act
        final UpdateCatsAfterStudyResponseDto result = interactor.execute(
            new UpdateCatsAfterStudyInputData(TOKEN));

        // Assert
        assertEquals(0, result.updatedCats().size());
        assertEquals(0, result.failures().size());

        // Verify output data passed to presenter
        verify(outputBoundary).prepareResponse(outputDataCaptor.capture());
        UpdateCatsAfterStudyOutputData actualOutputData = outputDataCaptor.getValue();
        assertEquals(0, actualOutputData.getUpdatedCats().size());
        assertEquals(0, actualOutputData.getFailures().size());
    }

    @Test
    void executeWhenOneCatUpdatesHappinessSuccessfully() throws DatabaseAccessException {
        // Arrange
        final Cat initialCat = new Cat(CAT_NAME, USERNAME, 90, 100, IMAGE_FILE);
        final int expectedIncrease = Math.clamp(
            Math.round(90 * (1 + Constants.CatStats.STUDY_SESSION_HAPPINESS_INCREASE)),
            0,
            100);
        final Cat updatedCat = new Cat(CAT_NAME, USERNAME, expectedIncrease, 100, IMAGE_FILE);

        when(dataAccess.getUsernameFromToken(TOKEN)).thenReturn(USERNAME);
        when(dataAccess.getCatsByOwner(USERNAME)).thenReturn(List.of(initialCat));
        when(dataAccess.updateCatsHappiness(any()))
            .thenReturn(new CatUpdateResult(List.of(updatedCat), List.of()));

        when(outputBoundary.prepareResponse(any(UpdateCatsAfterStudyOutputData.class)))
            .thenReturn(new UpdateCatsAfterStudyResponseDto(
                List.of(new CatDto(CAT_NAME, USERNAME, expectedIncrease, 100, IMAGE_FILE)),
                List.of()));

        // Act
        final UpdateCatsAfterStudyResponseDto result = interactor.execute(
            new UpdateCatsAfterStudyInputData(TOKEN));

        // Assert
        assertEquals(1, result.updatedCats().size());
        assertEquals(0, result.failures().size());
        CatDto resultCat = result.updatedCats().get(0);
        assertEquals(CAT_NAME, resultCat.name());
        assertEquals(USERNAME, resultCat.ownerUsername());
        assertEquals(expectedIncrease, resultCat.happinessLevel());
        assertEquals(100, resultCat.hungerLevel());
        assertEquals(IMAGE_FILE, resultCat.imageFileName());

        // Verify interactions
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<Cat, Integer>> updatesCaptor = ArgumentCaptor.forClass(Map.class);
        verify(dataAccess).updateCatsHappiness(updatesCaptor.capture());
        Map<Cat, Integer> actualUpdates = updatesCaptor.getValue();
        assertEquals(1, actualUpdates.size());
        Map.Entry<Cat, Integer> update = actualUpdates.entrySet().iterator().next();
        assertEquals(CAT_NAME, update.getKey().getName());
        assertEquals(expectedIncrease, update.getValue());

        // Verify output data passed to presenter
        verify(outputBoundary).prepareResponse(outputDataCaptor.capture());
        UpdateCatsAfterStudyOutputData actualOutputData = outputDataCaptor.getValue();
        assertEquals(1, actualOutputData.getUpdatedCats().size());
        assertEquals(0, actualOutputData.getFailures().size());
        Cat outputCat = actualOutputData.getUpdatedCats().get(0);
        assertEquals(CAT_NAME, outputCat.getName());
        assertEquals(USERNAME, outputCat.getOwnerUsername());
        assertEquals(expectedIncrease, outputCat.getHappinessLevel());
        assertEquals(100, outputCat.getHungerLevel());
        assertEquals(IMAGE_FILE, outputCat.getImageFileName());
    }

    @Test
    void executeWhenMultipleCatsUpdatesAllSuccessfully() throws DatabaseAccessException {
        // Arrange
        final Cat cat1 = new Cat("Cat1", USERNAME, 100, 100, IMAGE_FILE);
        final Cat cat2 = new Cat("Cat2", USERNAME, 80, 90, IMAGE_FILE);
        final int increase1 = Math.clamp(
            Math.round(100 * (1 + Constants.CatStats.STUDY_SESSION_HAPPINESS_INCREASE)),
            0,
            100);
        final int increase2 = Math.clamp(
            Math.round(80 * (1 + Constants.CatStats.STUDY_SESSION_HAPPINESS_INCREASE)),
            0,
            100);
        final Cat updatedCat1 = new Cat("Cat1", USERNAME, increase1, 100, IMAGE_FILE);
        final Cat updatedCat2 = new Cat("Cat2", USERNAME, increase2, 90, IMAGE_FILE);

        when(dataAccess.getUsernameFromToken(TOKEN)).thenReturn(USERNAME);
        when(dataAccess.getCatsByOwner(USERNAME)).thenReturn(List.of(cat1, cat2));
        when(dataAccess.updateCatsHappiness(any()))
            .thenReturn(new CatUpdateResult(List.of(updatedCat1, updatedCat2), List.of()));

        when(outputBoundary.prepareResponse(any(UpdateCatsAfterStudyOutputData.class)))
            .thenReturn(new UpdateCatsAfterStudyResponseDto(List.of(
                new CatDto("Cat1", USERNAME, increase1, 100, IMAGE_FILE),
                new CatDto("Cat2", USERNAME, increase2, 90, IMAGE_FILE)),
                List.of()));

        // Act
        final UpdateCatsAfterStudyResponseDto result = interactor.execute(
            new UpdateCatsAfterStudyInputData(TOKEN));

        // Assert
        assertEquals(2, result.updatedCats().size());
        assertEquals(0, result.failures().size());

        // Verify first cat
        CatDto resultCat1 = result.updatedCats().get(0);
        assertEquals("Cat1", resultCat1.name());
        assertEquals(increase1, resultCat1.happinessLevel());

        // Verify second cat
        CatDto resultCat2 = result.updatedCats().get(1);
        assertEquals("Cat2", resultCat2.name());
        assertEquals(increase2, resultCat2.happinessLevel());

        // Verify updates passed to data access
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<Cat, Integer>> updatesCaptor = ArgumentCaptor.forClass(Map.class);
        verify(dataAccess).updateCatsHappiness(updatesCaptor.capture());
        Map<Cat, Integer> actualUpdates = updatesCaptor.getValue();
        assertEquals(2, actualUpdates.size());

        // Verify output data passed to presenter
        verify(outputBoundary).prepareResponse(outputDataCaptor.capture());
        UpdateCatsAfterStudyOutputData actualOutputData = outputDataCaptor.getValue();
        assertEquals(2, actualOutputData.getUpdatedCats().size());
        assertEquals(0, actualOutputData.getFailures().size());

        // Verify updates for each cat
        actualUpdates.forEach((cat, increase) -> {
            if (cat.getName().equals("Cat1")) {
                assertEquals(increase1, increase);
            }
            else if (cat.getName().equals("Cat2")) {
                assertEquals(increase2, increase);
            }
        });
    }

    @Test
    void executeWhenSomeCatsFailToUpdate() throws DatabaseAccessException {
        // Arrange
        final Cat cat1 = new Cat("Cat1", USERNAME, 100, 100, IMAGE_FILE);
        final Cat cat2 = new Cat("Cat2", USERNAME, 80, 90, IMAGE_FILE);
        final int increase1 = Math.clamp(
            Math.round(100 * (1 + Constants.CatStats.STUDY_SESSION_HAPPINESS_INCREASE)),
            0,
            100);
        final Cat updatedCat1 = new Cat("Cat1", USERNAME, increase1, 100, IMAGE_FILE);
        final String failureMessage = "Failed to update Cat2: Database error";

        when(dataAccess.getUsernameFromToken(TOKEN)).thenReturn(USERNAME);
        when(dataAccess.getCatsByOwner(USERNAME)).thenReturn(List.of(cat1, cat2));
        when(dataAccess.updateCatsHappiness(any()))
            .thenReturn(new CatUpdateResult(List.of(updatedCat1), List.of(failureMessage)));

        when(outputBoundary.prepareResponse(any(UpdateCatsAfterStudyOutputData.class)))
            .thenReturn(new UpdateCatsAfterStudyResponseDto(
                List.of(new CatDto("Cat1", USERNAME, increase1, 100, IMAGE_FILE)),
                List.of(failureMessage)));

        // Act
        final UpdateCatsAfterStudyResponseDto result = interactor.execute(
            new UpdateCatsAfterStudyInputData(TOKEN));

        // Assert
        assertEquals(1, result.updatedCats().size());
        assertEquals(1, result.failures().size());
        assertEquals(failureMessage, result.failures().get(0));

        // Verify output data passed to presenter
        verify(outputBoundary).prepareResponse(outputDataCaptor.capture());
        UpdateCatsAfterStudyOutputData actualOutputData = outputDataCaptor.getValue();
        assertEquals(1, actualOutputData.getUpdatedCats().size());
        assertEquals(1, actualOutputData.getFailures().size());
        assertEquals(failureMessage, actualOutputData.getFailures().get(0));
    }
}
