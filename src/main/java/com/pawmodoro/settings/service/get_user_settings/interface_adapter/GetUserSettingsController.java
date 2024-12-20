package com.pawmodoro.settings.service.get_user_settings.interface_adapter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.settings.service.get_user_settings.GetUserSettingsInputBoundary;
import com.pawmodoro.settings.service.get_user_settings.GetUserSettingsInputData;
import com.pawmodoro.users.entity.UserNotFoundException;

/**
 * Controller handling user settings retrieval requests.
 * This class serves as the interface adapter layer in Clean Architecture,
 * converting HTTP requests into application-specific DTOs and vice versa.
 */
@RestController
@RequestMapping("/api/settings")
public class GetUserSettingsController {
    private final GetUserSettingsInputBoundary getUserSettingsInteractor;

    /**
     * Constructs a GetUserSettingsController with required dependencies.
     * @param getUserSettingsInteractor the use case interactor for getting user settings
     */
    public GetUserSettingsController(GetUserSettingsInputBoundary getUserSettingsInteractor) {
        this.getUserSettingsInteractor = getUserSettingsInteractor;
    }

    /**
     * Handles GET requests for user settings.
     * Validates the request, converts it into a domain-specific input data object,
     * and processes it through the use case interactor.
     * @param username the username of the user
     * @param authHeader the Authorization header containing the access token
     * @return GetUserSettingsResponseDto containing the user settings
     * @throws UserNotFoundException if the user is not found
     * @throws DatabaseAccessException if there's an error accessing the database
     */
    @GetMapping("/{username}")
    public ResponseEntity<GetUserSettingsResponseDto> getUserSettings(
        @PathVariable
        String username,
        @RequestHeader(value = "Authorization")
        String authHeader) throws UserNotFoundException, DatabaseAccessException {

        final String token = authHeader.replace("Bearer ", "").trim();

        final GetUserSettingsInputData inputData = new GetUserSettingsInputData(
            username,
            token);
        final GetUserSettingsResponseDto responseDto = getUserSettingsInteractor.execute(inputData);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
