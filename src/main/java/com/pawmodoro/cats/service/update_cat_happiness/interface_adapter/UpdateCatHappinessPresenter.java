package com.pawmodoro.cats.service.update_cat_happiness.interface_adapter;

import org.springframework.stereotype.Component;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.service.update_cat_happiness.UpdateCatHappinessOutputBoundary;
import com.pawmodoro.cats.service.update_cat_happiness.UpdateCatHappinessOutputData;

/**
 * Presenter for the Update Cat Happiness use case.
 * Transforms the output data into a format suitable for the view.
 */
@Component
public class UpdateCatHappinessPresenter implements UpdateCatHappinessOutputBoundary {
    @Override
    public UpdateCatHappinessResponseDto prepareResponse(UpdateCatHappinessOutputData outputData) {
        final Cat updatedCat = outputData.getUpdatedCat();
        return new UpdateCatHappinessResponseDto(
            updatedCat.getName(),
            updatedCat.getOwnerUsername(),
            updatedCat.getHappinessLevel(),
            updatedCat.getHungerLevel(),
            updatedCat.getImageFileName());
    }
}
