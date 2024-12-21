package com.pawmodoro.settings.service.update_user_settings.interface_adapter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.settings.service.update_user_settings.UpdateUserSettingsInputBoundary;
import com.pawmodoro.settings.service.update_user_settings.UpdateUserSettingsInputData;
import com.pawmodoro.users.entity.UserNotFoundException;

/**
 * Controller handling user settings update requests.
 * This class serves as the interface adapter layer in Clean Architecture,
 * converting HTTP requests into application-specific DTOs and vice versa.
 */
@RestController
@RequestMapping("/api/settings")
public class UpdateUserSettingsController {
    private final UpdateUserSettingsInputBoundary updateUserSettingsInteractor;

    /**
     * Constructs an UpdateUserSettingsController with required dependencies.
     * @param updateUserSettingsInteractor the use case interactor for updating user settings
     */
    public UpdateUserSettingsController(UpdateUserSettingsInputBoundary updateUserSettingsInteractor) {
        this.updateUserSettingsInteractor = updateUserSettingsInteractor;
    }

    /**
     * Handles PUT requests for updating user settings.
     * Validates the request, converts it into a domain-specific input data object,
     * and processes it through the use case interactor.
     * @param username the username of the user
     * @param authHeader the Authorization header containing the access token
     * @param request the request body containing the new settings values
     * @return UpdateUserSettingsResponseDto containing the updated user settings
     * @throws UserNotFoundException if the user is not found
     * @throws DatabaseAccessException if there's an error accessing the database
     */
    @PutMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UpdateUserSettingsResponseDto updateUserSettings(
        @PathVariable
        String username,
        @RequestHeader(value = "Authorization")
        String authHeader,
        @RequestBody
        UpdateUserSettingsRequestDto request) throws UserNotFoundException, DatabaseAccessException {

        final String token = authHeader.replace("Bearer ", "").trim();

        final UpdateUserSettingsInputData inputData = new UpdateUserSettingsInputData(
            username,
            token,
            request.focusDuration(),
            request.shortBreakDuration(),
            request.longBreakDuration(),
            request.autoStartBreaks(),
            request.autoStartFocus());

        return updateUserSettingsInteractor.execute(inputData);
    }
}
