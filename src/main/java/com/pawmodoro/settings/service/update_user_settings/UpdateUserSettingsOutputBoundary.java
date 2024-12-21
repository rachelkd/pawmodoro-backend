package com.pawmodoro.settings.service.update_user_settings;

import com.pawmodoro.settings.service.update_user_settings.interface_adapter.UpdateUserSettingsResponseDto;

/**
 * Output boundary for the update user settings use case.
 * Defines the contract for preparing the response after updating user settings.
 */
public interface UpdateUserSettingsOutputBoundary {
    /**
     * Prepares the response after updating user settings.
     * @param outputData The output data containing the updated settings
     * @return A response DTO containing the formatted settings data
     */
    UpdateUserSettingsResponseDto prepareResponse(UpdateUserSettingsOutputData outputData);
}
