package com.pawmodoro.settings.service.get_user_settings;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.settings.entity.UserSettings;
import com.pawmodoro.users.entity.UserNotFoundException;

/**
 * The Data Access Interface for GetUserSettings use case.
 */
public interface GetUserSettingsDataAccessInterface {
    /**
     * Gets the user settings for the given username with the user's access token.
     * @param username the username
     * @param accessToken the access token for the given username
     * @return a UserSettings object for the given user
     * @throws UserNotFoundException if username is not in the database
     * @throws DatabaseAccessException if database connection fails or request is unauthorized
     */
    UserSettings getUserSettings(String username,
        String accessToken) throws UserNotFoundException, DatabaseAccessException;
}
