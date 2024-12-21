package com.pawmodoro.settings.service.update_user_settings;

import org.springframework.stereotype.Service;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.settings.entity.UserSettings;
import com.pawmodoro.settings.service.update_user_settings.interface_adapter.UpdateUserSettingsResponseDto;
import com.pawmodoro.users.entity.UserNotFoundException;

/**
 * The UpdateUserSettings Interactor implements the business logic for updating user settings.
 * This class follows the Single Responsibility Principle by handling only settings update logic.
 */
@Service
public class UpdateUserSettingsInteractor implements UpdateUserSettingsInputBoundary {
    private final UpdateUserSettingsDataAccessInterface userSettingsDataAccessObject;
    private final UpdateUserSettingsOutputBoundary updateUserSettingsPresenter;

    /**
     * Constructs an UpdateUserSettingsInteractor with required dependencies.
     * @param userSettingsDataAccessObject data access object for user settings operations
     * @param updateUserSettingsPresenter presenter for formatting responses
     */
    public UpdateUserSettingsInteractor(
        UpdateUserSettingsDataAccessInterface userSettingsDataAccessObject,
        UpdateUserSettingsOutputBoundary updateUserSettingsPresenter) {
        this.userSettingsDataAccessObject = userSettingsDataAccessObject;
        this.updateUserSettingsPresenter = updateUserSettingsPresenter;
    
    }

    @Override
    public UpdateUserSettingsResponseDto execute(
        UpdateUserSettingsInputData inputData) throws UserNotFoundException, DatabaseAccessException {

        // Create a UserSettings object from the input data
        final UserSettings userSettings = new UserSettings(
            inputData.getUsername(),
            inputData.getFocusDuration(),
            inputData.getShortBreakDuration(),
            inputData.getLongBreakDuration(),
            inputData.isAutoStartBreaks(),
            inputData.isAutoStartFocus());

        // Update the settings in the database
        userSettingsDataAccessObject.updateUserSettings(
            inputData.getUsername(),
            inputData.getAccessToken(),
            userSettings);

        // Prepare and return the response
        return updateUserSettingsPresenter.prepareResponse(
            new UpdateUserSettingsOutputData(userSettings));
    }
}
