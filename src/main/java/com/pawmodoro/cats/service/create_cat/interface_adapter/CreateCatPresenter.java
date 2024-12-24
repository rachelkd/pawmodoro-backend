package com.pawmodoro.cats.service.create_cat.interface_adapter;

import org.springframework.stereotype.Component;

import com.pawmodoro.cats.service.create_cat.CreateCatOutputBoundary;
import com.pawmodoro.cats.service.create_cat.CreateCatOutputData;

/**
 * REST API implementation of the CreateCatPresenter.
 * This class formats create cat responses for the REST API.
 */
@Component
public class CreateCatPresenter implements CreateCatOutputBoundary {

    @Override
    public CreateCatResponseDto prepareResponse(CreateCatOutputData outputData) {
        return new CreateCatResponseDto(
            outputData.getCreatedCat().getName(),
            outputData.getCreatedCat().getOwnerUsername(),
            outputData.getCreatedCat().getImageFileName());
    }
}
