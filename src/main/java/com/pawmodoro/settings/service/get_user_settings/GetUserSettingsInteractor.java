package com.pawmodoro.settings.service.get_user_settings;

import org.springframework.stereotype.Service;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.settings.entity.UserSettings;
import com.pawmodoro.settings.service.get_user_settings.interface_adapter.GetUserSettingsResponseDto;
import com.pawmodoro.users.entity.UserNotFoundException;

/**
 * The GetUserSettings Interactor implements the business logic for retrieving user settings.
 * This class follows the Single Responsibility Principle by handling only settings retrieval logic.
 */
@Service
public class GetUserSettingsInteractor implements GetUserSettingsInputBoundary {
    private final GetUserSettingsDataAccessInterface userSettingsDataAccessObject;
    private final GetUserSettingsOutputBoundary getUserSettingsPresenter;

    /**
     * Constructs a GetUserSettingsInteractor with required dependencies.
     * @param userSettingsDataAccessObject data access object for user settings operations
     * @param getUserSettingsPresenter presenter for formatting responses
     */
    public GetUserSettingsInteractor(
        GetUserSettingsDataAccessInterface userSettingsDataAccessObject,
        GetUserSettingsOutputBoundary getUserSettingsPresenter) {
        this.userSettingsDataAccessObject = userSettingsDataAccessObject;
        this.getUserSettingsPresenter = getUserSettingsPresenter;
    }

    @Override
    public GetUserSettingsResponseDto execute(
        GetUserSettingsInputData inputData) throws UserNotFoundException, DatabaseAccessException {

        final UserSettings userSettings = userSettingsDataAccessObject.getUserSettings(
            inputData.getUsername(),
            inputData.getAccessToken());

        return getUserSettingsPresenter.prepareResponse(
            new GetUserSettingsOutputData(userSettings));
    }
}
