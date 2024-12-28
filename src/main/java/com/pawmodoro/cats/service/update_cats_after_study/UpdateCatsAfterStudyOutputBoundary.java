package com.pawmodoro.cats.service.update_cats_after_study;

import com.pawmodoro.cats.service.update_cats_after_study.interface_adapter.UpdateCatsAfterStudyResponseDto;

/**
 * Output boundary for presenting the results of updating cats after study
 */
public interface UpdateCatsAfterStudyOutputBoundary {
    /**
     * Prepares the response for updating cats' happiness levels
     * @param outputData The output data containing updated cats
     * @return The response DTO containing the updated cats
     */
    UpdateCatsAfterStudyResponseDto prepareResponse(UpdateCatsAfterStudyOutputData outputData);
}
