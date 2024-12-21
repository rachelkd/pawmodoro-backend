package com.pawmodoro.settings.service.update_user_settings;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.settings.service.update_user_settings.interface_adapter.UpdateUserSettingsResponseDto;
import com.pawmodoro.users.entity.UserNotFoundException;

/**
 * Input boundary for the UpdateUserSettings use case.
 * Defines the interface for executing the use case.
 */
public interface UpdateUserSettingsInputBoundary {
    /**
     * Executes the UpdateUserSettings use case.
     * @param inputData the input data containing username, access token, and new settings values
     * @return a DTO containing the updated user settings
     * @throws UserNotFoundException if the user is not found
     * @throws DatabaseAccessException if there's an error accessing the database or if authentication fails
     */
    UpdateUserSettingsResponseDto execute(
        UpdateUserSettingsInputData inputData) throws UserNotFoundException, DatabaseAccessException;
}
