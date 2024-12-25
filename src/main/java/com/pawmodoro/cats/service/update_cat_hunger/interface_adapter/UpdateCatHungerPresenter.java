package com.pawmodoro.cats.service.update_cat_hunger.interface_adapter;

import org.springframework.stereotype.Component;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.service.update_cat_hunger.UpdateCatHungerOutputBoundary;
import com.pawmodoro.cats.service.update_cat_hunger.UpdateCatHungerOutputData;

/**
 * Presenter for the Update Cat Hunger use case.
 * Transforms the output data into a format suitable for the view.
 */
@Component
public class UpdateCatHungerPresenter implements UpdateCatHungerOutputBoundary {
    @Override
    public UpdateCatHungerResponseDto prepareResponse(UpdateCatHungerOutputData outputData) {
        final Cat updatedCat = outputData.getUpdatedCat();
        return new UpdateCatHungerResponseDto(
            updatedCat.getName(),
            updatedCat.getOwnerUsername(),
            updatedCat.getHappinessLevel(),
            updatedCat.getHungerLevel(),
            updatedCat.getImageFileName());
    }
}
