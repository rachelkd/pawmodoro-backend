package com.pawmodoro.settings.service.get_user_settings;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.settings.service.get_user_settings.interface_adapter.GetUserSettingsResponseDto;
import com.pawmodoro.users.entity.UserNotFoundException;

/**
 * Input boundary for the GetUserSettings use case.
 * Defines the interface for executing the use case.
 */
public interface GetUserSettingsInputBoundary {
    /**
     * Executes the GetUserSettings use case.
     * @param inputData the input data containing username and access token
     * @return a DTO containing the user settings
     * @throws UserNotFoundException if the user is not found
     * @throws DatabaseAccessException if there's an error accessing the database
     */
    GetUserSettingsResponseDto execute(
        GetUserSettingsInputData inputData) throws UserNotFoundException, DatabaseAccessException;
}
