package com.pawmodoro.cats.service.get_all_cats.interface_adapter;

import org.springframework.stereotype.Component;

import com.pawmodoro.cats.service.get_all_cats.GetAllCatsOutputBoundary;
import com.pawmodoro.cats.service.get_all_cats.GetAllCatsOutputData;

/**
 * REST API implementation of the GetAllCatsPresenter.
 * This class formats get all cats responses for the REST API.
 */
@Component
public class GetAllCatsPresenter implements GetAllCatsOutputBoundary {

    @Override
    public GetAllCatsResponseDto prepareResponse(GetAllCatsOutputData outputData) {
        return new GetAllCatsResponseDto(
            true,
            outputData.getCats(),
            "Successfully retrieved cats");
    }
}
