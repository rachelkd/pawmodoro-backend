package com.pawmodoro.settings.service.update_user_settings;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.settings.entity.UserSettings;
import com.pawmodoro.users.entity.UserNotFoundException;

/**
 * Data Access Interface for update user settings use case.
 */
public interface UpdateUserSettingsDataAccessInterface {
    /**
     * Update the user settings for the given username based on the given user settings object.
     * @param username the username
     * @param accessToken the access token for the given username
     * @param userSettings the user settings to be updated to
     * @throws UserNotFoundException if username is invalid
     * @throws DatabaseAccessException if there's an error accessing the database or if authentication fails
     */
    void updateUserSettings(String username, String accessToken,
        UserSettings userSettings) throws UserNotFoundException, DatabaseAccessException;
}
