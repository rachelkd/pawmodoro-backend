package com.pawmodoro.cats.service.update_cat_happiness;

import com.pawmodoro.cats.entity.Cat;

/**
 * Output data for the Update Cat Happiness use case.
 * Contains the updated cat with its new happiness level.
 */
public class UpdateCatHappinessOutputData {
    private final Cat updatedCat;

    /**
     * Creates output data with the updated cat.
     * @param updatedCat the cat with updated happiness level
     */
    public UpdateCatHappinessOutputData(Cat updatedCat) {
        this.updatedCat = updatedCat;
    }

    public Cat getUpdatedCat() {
        return updatedCat;
    }
}
