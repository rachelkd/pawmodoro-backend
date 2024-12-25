package com.pawmodoro.cats.service.update_cat_happiness;

import com.pawmodoro.cats.service.update_cat_happiness.interface_adapter.UpdateCatHappinessResponseDto;

/**
 * Output boundary for the Update Cat Happiness use case.
 * Defines the contract for presenting the results of updating a cat's happiness level.
 */
public interface UpdateCatHappinessOutputBoundary {
    /**
     * Prepares the response for the update happiness operation.
     * @param outputData the output data containing updated cat
     * @return the response data
     */
    UpdateCatHappinessResponseDto prepareResponse(UpdateCatHappinessOutputData outputData);
}
