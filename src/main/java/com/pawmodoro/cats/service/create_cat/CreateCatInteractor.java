package com.pawmodoro.cats.service.create_cat;

import org.springframework.stereotype.Service;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.entity.CatFactory;
import com.pawmodoro.cats.service.create_cat.interface_adapter.CreateCatResponseDto;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Interactor that implements the Create Cat use case.
 * This class orchestrates the creation of a cat for a user,
 * using the provided cat name and image file.
 */
@Service
public class CreateCatInteractor implements CreateCatInputBoundary {
    private final CatFactory catFactory;
    private final CreateCatDataAccessInterface dataAccess;
    private final CreateCatOutputBoundary outputBoundary;

    public CreateCatInteractor(
        final CatFactory catFactory,
        final CreateCatDataAccessInterface dataAccess,
        final CreateCatOutputBoundary outputBoundary) {
        this.catFactory = catFactory;
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public CreateCatResponseDto execute(
        final CreateCatInputData inputData) throws DatabaseAccessException {
        final String catName = inputData.getCatName();
        final String ownerUsername = inputData.getOwnerUsername();
        final String imageFileName = inputData.getImageFileName();

        final Cat cat;
        if (imageFileName == null || imageFileName.trim().isEmpty()) {
            cat = catFactory.create(catName, ownerUsername);
        }
        else {
            cat = catFactory.create(catName, ownerUsername, imageFileName);
        }

        final Cat savedCat = dataAccess.saveCat(cat);

        final CreateCatOutputData outputData = new CreateCatOutputData(savedCat);
        return outputBoundary.prepareResponse(outputData);
    }
}
