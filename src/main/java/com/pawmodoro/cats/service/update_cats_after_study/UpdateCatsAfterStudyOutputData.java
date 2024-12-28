package com.pawmodoro.cats.service.update_cats_after_study;

import java.util.List;

import com.pawmodoro.cats.entity.Cat;

/**
 * Output data for the update cats after study use case.
 * Contains the list of successfully updated cats and any failure messages
 */
public class UpdateCatsAfterStudyOutputData {
    private final List<Cat> updatedCats;
    private final List<String> failures;

    public UpdateCatsAfterStudyOutputData(List<Cat> updatedCats, List<String> failures) {
        this.updatedCats = updatedCats;
        this.failures = failures;
    }

    public List<Cat> getUpdatedCats() {
        return updatedCats;
    }

    public List<String> getFailures() {
        return failures;
    }
}
