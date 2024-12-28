package com.pawmodoro.cats.service.update_cats_after_study;

import com.pawmodoro.cats.service.update_cats_after_study.interface_adapter.UpdateCatsAfterStudyResponseDto;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Input boundary for updating cats after study completion.
 */
public interface UpdateCatsAfterStudyInputBoundary {
    /**
     * Updates happiness levels for all cats belonging to a user after study completion.
     * @param input The input data containing username and cat names
     * @return The response data containing the updated cats
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    UpdateCatsAfterStudyResponseDto execute(UpdateCatsAfterStudyInputData input) throws DatabaseAccessException;
}
