package com.pawmodoro.settings.service.get_user_settings.interface_adapter;

import org.springframework.stereotype.Component;

import com.pawmodoro.settings.entity.UserSettings;
import com.pawmodoro.settings.service.get_user_settings.GetUserSettingsOutputBoundary;
import com.pawmodoro.settings.service.get_user_settings.GetUserSettingsOutputData;

/**
 * Presenter for the GetUserSettings use case.
 * Converts the output data into a format suitable for the view.
 */
@Component
public class GetUserSettingsPresenter implements GetUserSettingsOutputBoundary {

    @Override
    public GetUserSettingsResponseDto prepareResponse(GetUserSettingsOutputData outputData) {
        final UserSettings settings = outputData.getUserSettings();
        return new GetUserSettingsResponseDto(
            settings.getUsername(),
            settings.getFocusDuration(),
            settings.getShortBreakDuration(),
            settings.getLongBreakDuration(),
            settings.isAutoStartBreaks(),
            settings.isAutoStartFocus());
    }
}
