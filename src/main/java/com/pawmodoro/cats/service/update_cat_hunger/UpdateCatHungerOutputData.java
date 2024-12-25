package com.pawmodoro.cats.service.update_cat_hunger;

import com.pawmodoro.cats.entity.Cat;

/**
 * Output data for the Update Cat Hunger use case.
 * Contains the updated cat with its new hunger level.
 */
public class UpdateCatHungerOutputData {
    private final Cat updatedCat;

    /**
     * Creates output data with the updated cat.
     * @param updatedCat the cat with updated hunger level
     */
    public UpdateCatHungerOutputData(Cat updatedCat) {
        this.updatedCat = updatedCat;
    }

    public Cat getUpdatedCat() {
        return updatedCat;
    }
}
