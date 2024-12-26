package com.pawmodoro.cats.service.decrease_cat_stats_on_skip;

import com.pawmodoro.cats.service.decrease_cat_stats_on_skip.interface_adapter.DecreaseCatStatsOnSkipResponseDto;

/**
 * Output boundary for the decrease cat stats on skip use case.
 * This interface defines how the use case will format its results.
 */
public interface DecreaseCatStatsOnSkipOutputBoundary {
    /**
     * Formats the response.
     * @param outputData the output data containing the updated cat information
     * @return response DTO with the updated cat information
     */
    DecreaseCatStatsOnSkipResponseDto prepareResponse(DecreaseCatStatsOnSkipOutputData outputData);
}
