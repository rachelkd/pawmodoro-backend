package com.pawmodoro.cats.service.decrease_cat_stats_on_skip;

import java.util.Collection;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.service.decrease_cat_stats_on_skip.interface_adapter.DecreaseCatStatsOnSkipResponseDto;
import com.pawmodoro.constants.Constants;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Interactor for the Decrease Cat Stats On Skip use case.
 * Implements the business logic for decreasing a random cat's stats when a focus session is skipped.
 */
@Service
public class DecreaseCatStatsOnSkipInteractor implements DecreaseCatStatsOnSkipInputBoundary {
    private final DecreaseCatStatsOnSkipDataAccessInterface dataAccess;
    private final DecreaseCatStatsOnSkipOutputBoundary outputBoundary;
    private final Random random;

    public DecreaseCatStatsOnSkipInteractor(
        final DecreaseCatStatsOnSkipDataAccessInterface dataAccess,
        final DecreaseCatStatsOnSkipOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
        this.random = new Random();
    }

    @Override
    public DecreaseCatStatsOnSkipResponseDto execute(
        final DecreaseCatStatsOnSkipInputData inputData) throws DatabaseAccessException {
        final DecreaseCatStatsOnSkipOutputData outputData;

        // Get username from token
        final String ownerUsername = dataAccess.getUsernameFromToken(inputData.getToken());

        // Get all cats owned by the user
        final Collection<Cat> cats = dataAccess.getCatsByOwner(ownerUsername);

        // Update no cats if user has no cats
        if (cats.isEmpty()) {
            outputData = new DecreaseCatStatsOnSkipOutputData(
                null,
                ownerUsername,
                0,
                0,
                null,
                false,
                "No cats found to update");
            return outputBoundary.prepareResponse(outputData);
        }

        // Select a random cat
        final Cat selectedCat = cats.stream().skip(random.nextInt(cats.size())).findFirst().get();

        // Calculate new stats
        final int happinessDecrease =
            (int) (selectedCat.getHappinessLevel() * Constants.CatStats.SKIP_PENALTY_PERCENTAGE);
        final int hungerDecrease =
            (int) (selectedCat.getHungerLevel() * Constants.CatStats.SKIP_PENALTY_PERCENTAGE);

        // Update happiness first
        Cat updatedCat = dataAccess.updateHappiness(selectedCat.getName(), ownerUsername, -happinessDecrease);

        // Then update hunger
        updatedCat = dataAccess.updateHunger(updatedCat.getName(), ownerUsername, -hungerDecrease);

        // Check if cat should be deleted
        if (updatedCat.getHappinessLevel() <= 0 || updatedCat.getHungerLevel() <= 0) {
            dataAccess.deleteCat(updatedCat.getName(), ownerUsername);
            outputData = new DecreaseCatStatsOnSkipOutputData(
                updatedCat.getName(),
                ownerUsername,
                0,
                0,
                updatedCat.getImageFileName(),
                true,
                String.format("Cat %s ran away due to neglect!", updatedCat.getName()));
            return outputBoundary.prepareResponse(outputData);
        }

        outputData = new DecreaseCatStatsOnSkipOutputData(
            updatedCat.getName(),
            ownerUsername,
            updatedCat.getHappinessLevel(),
            updatedCat.getHungerLevel(),
            updatedCat.getImageFileName(),
            false,
            String.format("Cat %s's stats were decreased", updatedCat.getName()));

        return outputBoundary.prepareResponse(outputData);
    }
}
