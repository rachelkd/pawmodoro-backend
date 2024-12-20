package com.pawmodoro.settings.service.get_user_settings;

import com.pawmodoro.settings.entity.UserSettings;

/**
 * Output data for the GetUserSettings use case.
 * Contains the user settings retrieved from the database.
 */
public class GetUserSettingsOutputData {
    private final UserSettings userSettings;

    /**
     * Constructs a GetUserSettingsOutputData object.
     * @param userSettings the user settings retrieved from the database
     */
    public GetUserSettingsOutputData(UserSettings userSettings) {
        this.userSettings = userSettings;
    }

    /**
     * Gets the user settings.
     * @return the user settings
     */
    public UserSettings getUserSettings() {
        return userSettings;
    }
}
