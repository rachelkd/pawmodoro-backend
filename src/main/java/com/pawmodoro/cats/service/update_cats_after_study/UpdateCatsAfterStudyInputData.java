package com.pawmodoro.cats.service.update_cats_after_study;

/**
 * Input data for updating cats after study completion.
 */
public class UpdateCatsAfterStudyInputData {
    private final String token;

    public UpdateCatsAfterStudyInputData(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
