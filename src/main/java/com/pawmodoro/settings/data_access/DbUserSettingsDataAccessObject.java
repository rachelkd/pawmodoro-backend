package com.pawmodoro.settings.data_access;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.settings.entity.UserSettings;
import com.pawmodoro.settings.service.UserSettingsDataAccessInterface;
import com.pawmodoro.users.entity.UserNotFoundException;

/**
 * Data access object for user settings.
 */
public class DbUserSettingsDataAccessObject implements UserSettingsDataAccessInterface {

    @Override
    public UserSettings getUserSettings(String username) throws UserNotFoundException, DatabaseAccessException {
        return null;
    }

    @Override
    public void updateUserSettings(String username, UserSettings userSettings)
        throws UserNotFoundException, DatabaseAccessException {
        // TODO: Implement the method for updating user settings.
    }
}
