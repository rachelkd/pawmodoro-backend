package com.pawmodoro.cats.service.decrease_cat_stats_on_skip.interface_adapter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pawmodoro.cats.service.decrease_cat_stats_on_skip.DecreaseCatStatsOnSkipInputBoundary;
import com.pawmodoro.cats.service.decrease_cat_stats_on_skip.DecreaseCatStatsOnSkipInputData;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Controller for the Decrease Cat Stats On Skip use case.
 * Handles HTTP requests to decrease a random cat's stats when a focus session is skipped.
 */
@RestController
@RequestMapping("/api/cats")
public class DecreaseCatStatsOnSkipController {
    private final DecreaseCatStatsOnSkipInputBoundary inputBoundary;

    public DecreaseCatStatsOnSkipController(DecreaseCatStatsOnSkipInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    /**
     * Handles POST requests to decrease a random cat's stats.
     * @param authHeader the Authorization header containing the access token
     * @return the response containing the updated cat information
     * @throws DatabaseAccessException if there is an error accessing the database
     * @throws AuthenticationException if the access token is invalid or expired
     */
    @PostMapping("/decrease-stats-on-skip")
    @ResponseStatus(HttpStatus.OK)
    public DecreaseCatStatsOnSkipResponseDto decreaseCatStatsOnSkip(
        @RequestHeader("Authorization") String authHeader) throws DatabaseAccessException {

        // Get username from token
        final String token = authHeader.replace("Bearer ", "").trim();
        final DecreaseCatStatsOnSkipInputData inputData = new DecreaseCatStatsOnSkipInputData(token);
        return inputBoundary.execute(inputData);
    }
}
