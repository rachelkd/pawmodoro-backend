package com.pawmodoro.users.service.refresh.interface_adapter;

import org.springframework.stereotype.Component;

import com.pawmodoro.users.service.refresh.RefreshTokenOutputBoundary;
import com.pawmodoro.users.service.refresh.RefreshTokenOutputData;

/**
 * REST API implementation of the RefreshTokenPresenter.
 * This class formats refresh token responses for the REST API.
 */
@Component
public class RefreshTokenPresenter implements RefreshTokenOutputBoundary {

    @Override
    public RefreshTokenResponseDto prepareResponse(RefreshTokenOutputData outputData) {
        return RefreshTokenResponseDto.from(outputData.getTokens());
    }
}
