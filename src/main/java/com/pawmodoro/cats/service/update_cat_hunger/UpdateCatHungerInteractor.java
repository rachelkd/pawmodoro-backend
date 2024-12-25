package com.pawmodoro.cats.service.update_cat_hunger;

import org.springframework.stereotype.Service;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.service.update_cat_hunger.interface_adapter.UpdateCatHungerResponseDto;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Interactor that implements the Update Cat Hunger use case.
 * This class orchestrates the updating of a cat's hunger level.
 */
@Service
public class UpdateCatHungerInteractor implements UpdateCatHungerInputBoundary {
    private final UpdateCatHungerDataAccessInterface dataAccess;
    private final UpdateCatHungerOutputBoundary outputBoundary;

    public UpdateCatHungerInteractor(
        final UpdateCatHungerDataAccessInterface dataAccess,
        final UpdateCatHungerOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public UpdateCatHungerResponseDto execute(
        final UpdateCatHungerInputData inputData) throws DatabaseAccessException {
        final String catName = inputData.getCatName();
        final String ownerUsername = inputData.getOwnerUsername();
        final int changeAmount = inputData.getChangeAmount();

        final Cat updatedCat = dataAccess.updateHunger(catName, ownerUsername, changeAmount);
        final UpdateCatHungerOutputData outputData = new UpdateCatHungerOutputData(updatedCat);
        return outputBoundary.prepareResponse(outputData);
    }
}
