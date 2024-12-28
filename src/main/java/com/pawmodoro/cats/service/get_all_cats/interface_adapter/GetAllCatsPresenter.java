package com.pawmodoro.cats.service.get_all_cats.interface_adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.interface_adapter.CatDto;
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
        List<CatDto> catDtos = outputData.getCats().stream()
            .map(this::mapCatToDto)
            .toList();

        return new GetAllCatsResponseDto(
            true,
            catDtos,
            "Successfully retrieved cats");
    }

    private CatDto mapCatToDto(Cat cat) {
        return new CatDto(
            cat.getName(),
            cat.getOwnerUsername(),
            cat.getHappinessLevel(),
            cat.getHungerLevel(),
            cat.getImageFileName());
    }
}
