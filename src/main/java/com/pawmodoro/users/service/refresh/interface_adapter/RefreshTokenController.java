package com.pawmodoro.users.service.refresh.interface_adapter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.users.entity.UserNotFoundException;
import com.pawmodoro.users.service.refresh.RefreshTokenInputBoundary;
import com.pawmodoro.users.service.refresh.RefreshTokenInputData;
import jakarta.validation.Valid;

/**
 * Controller handling token refresh requests.
 * This class serves as the interface adapter layer in Clean Architecture,
 * converting HTTP requests into application-specific DTOs and vice versa.
 */
@RestController
@RequestMapping("/api/users/refresh")
public class RefreshTokenController {
    private final RefreshTokenInputBoundary refreshTokenInteractor;

    /**
     * Constructs a RefreshTokenController with required dependencies.
     * @param refreshTokenInteractor the use case interactor for token refresh operations
     */
    public RefreshTokenController(RefreshTokenInputBoundary refreshTokenInteractor) {
        this.refreshTokenInteractor = refreshTokenInteractor;
    }

    /**
     * Handles POST requests for token refresh.
     * Validates the request, converts it into a domain-specific input data object,
     * and processes it through the use case interactor.
     * @param request the refresh token request containing the refresh token
     * @return RefreshTokenResponseDTO containing the new tokens
     * @throws UserNotFoundException if the user is not found
     * @throws DatabaseAccessException if there's an error accessing the database
     */
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public RefreshTokenResponseDto refreshToken(
        @Valid
        @RequestBody
        RefreshTokenRequestDto request) throws UserNotFoundException, DatabaseAccessException {

        final RefreshTokenInputData inputData = new RefreshTokenInputData(request.refreshToken());
        return refreshTokenInteractor.execute(inputData);
    }
}
