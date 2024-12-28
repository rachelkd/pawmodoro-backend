package com.pawmodoro.cats.service.update_cats_after_study.interface_adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.interface_adapter.CatDto;
import com.pawmodoro.cats.service.update_cats_after_study.UpdateCatsAfterStudyOutputBoundary;
import com.pawmodoro.cats.service.update_cats_after_study.UpdateCatsAfterStudyOutputData;

/**
 * Presenter for the update cats after study use case.
 */
@Component
public class UpdateCatsAfterStudyPresenter implements UpdateCatsAfterStudyOutputBoundary {
    @Override
    public UpdateCatsAfterStudyResponseDto prepareResponse(UpdateCatsAfterStudyOutputData outputData) {
        final List<CatDto> updatedCats = outputData.getUpdatedCats().stream()
            .map(this::mapCatToDto)
            .toList();

        return new UpdateCatsAfterStudyResponseDto(
            updatedCats,
            outputData.getFailures());
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
