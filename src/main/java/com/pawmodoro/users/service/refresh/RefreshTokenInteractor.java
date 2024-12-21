package com.pawmodoro.users.service.refresh;

import org.springframework.stereotype.Service;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.users.entity.AuthenticationToken;
import com.pawmodoro.users.entity.UserNotFoundException;
import com.pawmodoro.users.service.refresh.interface_adapter.RefreshTokenResponseDto;

/**
 * The RefreshToken Interactor implements the business logic for token refresh.
 * This class follows the Single Responsibility Principle by handling only token refresh logic.
 */
@Service
public class RefreshTokenInteractor implements RefreshTokenInputBoundary {
    private final RefreshTokenDataAccessInterface tokenDataAccessObject;
    private final RefreshTokenOutputBoundary refreshTokenPresenter;

    /**
     * Constructs a RefreshTokenInteractor with required dependencies.
     * @param tokenDataAccessObject data access object for token operations
     * @param refreshTokenPresenter presenter for formatting responses
     */
    public RefreshTokenInteractor(
        RefreshTokenDataAccessInterface tokenDataAccessObject,
        RefreshTokenOutputBoundary refreshTokenPresenter) {
        this.tokenDataAccessObject = tokenDataAccessObject;
        this.refreshTokenPresenter = refreshTokenPresenter;
    }

    @Override
    public RefreshTokenResponseDto execute(
        RefreshTokenInputData refreshTokenInputData) throws UserNotFoundException, DatabaseAccessException {

        final AuthenticationToken newTokens = tokenDataAccessObject.refreshTokens(
            refreshTokenInputData.refreshToken());

        return refreshTokenPresenter.prepareResponse(
            new RefreshTokenOutputData(newTokens));
    }
}
