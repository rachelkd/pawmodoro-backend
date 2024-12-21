package com.pawmodoro.settings.service.update_user_settings;

import com.pawmodoro.settings.entity.UserSettings;
import lombok.Getter;

/**
 * Output data for the update user settings use case.
 * Contains the updated user settings information.
 */
@Getter
public class UpdateUserSettingsOutputData {
    private final UserSettings updatedSettings;

    /**
     * Constructs an UpdateUserSettingsOutputData object.
     * @param updatedSettings The updated user settings
     */
    public UpdateUserSettingsOutputData(UserSettings updatedSettings) {
        this.updatedSettings = updatedSettings;
    }
}
