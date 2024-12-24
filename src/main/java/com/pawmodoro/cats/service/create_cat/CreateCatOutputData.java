package com.pawmodoro.cats.service.create_cat;

import com.pawmodoro.cats.entity.Cat;

/**
 * Output data for the Create Cat use case.
 * Contains the created cat.
 */
public class CreateCatOutputData {
    private final Cat createdCat;

    public CreateCatOutputData(Cat createdCat) {
        this.createdCat = createdCat;
    }

    public Cat getCreatedCat() {
        return createdCat;
    }
}
