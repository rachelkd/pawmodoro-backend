package com.pawmodoro.cats.service.update_cat_happiness;

import org.springframework.stereotype.Service;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.service.update_cat_happiness.interface_adapter.UpdateCatHappinessResponseDto;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Interactor that implements the Update Cat Happiness use case.
 * This class orchestrates the updating of a cat's happiness level.
 */
@Service
public class UpdateCatHappinessInteractor implements UpdateCatHappinessInputBoundary {
    private final UpdateCatHappinessDataAccessInterface dataAccess;
    private final UpdateCatHappinessOutputBoundary outputBoundary;

    public UpdateCatHappinessInteractor(
        final UpdateCatHappinessDataAccessInterface dataAccess,
        final UpdateCatHappinessOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public UpdateCatHappinessResponseDto execute(
        final UpdateCatHappinessInputData inputData) throws DatabaseAccessException {
        final String catName = inputData.getCatName();
        final String ownerUsername = inputData.getOwnerUsername();
        final int changeAmount = inputData.getChangeAmount();

        final Cat updatedCat = dataAccess.updateHappiness(catName, ownerUsername, changeAmount);
        final UpdateCatHappinessOutputData outputData = new UpdateCatHappinessOutputData(updatedCat);
        return outputBoundary.prepareResponse(outputData);
    }
}
