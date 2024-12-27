package com.pawmodoro.cats.service.decrease_cat_stats_on_skip.interface_adapter;

import org.springframework.stereotype.Component;

import com.pawmodoro.cats.interface_adapter.CatDto;
import com.pawmodoro.cats.service.decrease_cat_stats_on_skip.DecreaseCatStatsOnSkipOutputBoundary;
import com.pawmodoro.cats.service.decrease_cat_stats_on_skip.DecreaseCatStatsOnSkipOutputData;

/**
 * Presenter for the decrease cat stats on skip use case.
 * Converts the output data into a response format.
 */
@Component
public class DecreaseCatStatsOnSkipPresenter implements DecreaseCatStatsOnSkipOutputBoundary {

    @Override
    public DecreaseCatStatsOnSkipResponseDto prepareResponse(DecreaseCatStatsOnSkipOutputData outputData) {
        final CatDto catDto = !outputData.isDeleted() ? new CatDto(
            outputData.getCatName(),
            outputData.getOwnerUsername(),
            outputData.getHappinessLevel(),
            outputData.getHungerLevel(),
            outputData.getImageFileName()) : null;

        return new DecreaseCatStatsOnSkipResponseDto(
            catDto,
            outputData.isDeleted(),
            outputData.getMessage());
    }
}
