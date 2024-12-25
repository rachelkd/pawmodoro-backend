package com.pawmodoro.cats.service.update_cat_hunger;

import com.pawmodoro.cats.service.update_cat_hunger.interface_adapter.UpdateCatHungerResponseDto;

/**
 * Output boundary for the Update Cat Hunger use case.
 * Defines the contract for presenting the results of updating a cat's hunger level.
 */
public interface UpdateCatHungerOutputBoundary {
    /**
     * Prepares the response for the update hunger operation.
     * @param outputData the output data containing updated cat
     * @return the response data
     */
    UpdateCatHungerResponseDto prepareResponse(UpdateCatHungerOutputData outputData);
}
