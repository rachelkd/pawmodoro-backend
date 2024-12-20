package com.pawmodoro.settings.service.get_user_settings;

import com.pawmodoro.settings.service.get_user_settings.interface_adapter.GetUserSettingsResponseDto;

/**
 * Output boundary for the GetUserSettings use case.
 * Defines the interface for preparing the response.
 */
public interface GetUserSettingsOutputBoundary {
    /**
     * Prepares the response for the GetUserSettings use case.
     * @param outputData the output data containing the user settings
     * @return a DTO containing the formatted user settings response
     */
    GetUserSettingsResponseDto prepareResponse(GetUserSettingsOutputData outputData);
}
