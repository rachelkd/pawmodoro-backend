package com.pawmodoro.settings.service;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.settings.entity.UserSettings;
import com.pawmodoro.users.entity.UserNotFoundException;

public interface UserSettingsDataAccessInterface {
    UserSettings getUserSettings(String username) throws UserNotFoundException, DatabaseAccessException;

    void updateUserSettings(String username, UserSettings userSettings)
        throws UserNotFoundException, DatabaseAccessException;
}
