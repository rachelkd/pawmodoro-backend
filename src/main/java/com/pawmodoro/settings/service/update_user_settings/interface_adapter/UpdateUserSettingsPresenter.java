package com.pawmodoro.settings.service.update_user_settings.interface_adapter;

import org.springframework.stereotype.Component;

import com.pawmodoro.settings.entity.UserSettings;
import com.pawmodoro.settings.service.update_user_settings.UpdateUserSettingsOutputBoundary;
import com.pawmodoro.settings.service.update_user_settings.UpdateUserSettingsOutputData;

/**
 * Presenter for the UpdateUserSettings use case.
 * Converts the output data into a format suitable for the view.
 */
@Component
public class UpdateUserSettingsPresenter implements UpdateUserSettingsOutputBoundary {

    @Override
    public UpdateUserSettingsResponseDto prepareResponse(UpdateUserSettingsOutputData outputData) {
        final UserSettings settings = outputData.getUpdatedSettings();
        return new UpdateUserSettingsResponseDto(
            settings.getUsername(),
            settings.getFocusDuration(),
            settings.getShortBreakDuration(),
            settings.getLongBreakDuration(),
            settings.isAutoStartBreaks(),
            settings.isAutoStartFocus());
    }
}
