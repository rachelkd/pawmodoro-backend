package com.pawmodoro.cats.service.decrease_cat_stats_on_skip;

/**
 * Input data for the Decrease Cat Stats On Skip use case.
 * Contains the authentication token used to identify the user.
 */
public class DecreaseCatStatsOnSkipInputData {
    private final String token;

    /**
     * Creates input data for decreasing a random cat's stats.
     * @param token the authentication token used to identify the user
     */
    public DecreaseCatStatsOnSkipInputData(final String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
