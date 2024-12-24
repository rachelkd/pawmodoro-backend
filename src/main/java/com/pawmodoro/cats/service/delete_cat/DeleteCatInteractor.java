package com.pawmodoro.cats.service.delete_cat;

import org.springframework.stereotype.Service;

import com.pawmodoro.cats.service.delete_cat.interface_adapter.DeleteCatResponseDto;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Interactor that implements the Delete Cat use case.
 * This class orchestrates the deletion of a cat by validating the input
 * and using the data access interface to perform the deletion.
 */
@Service
public class DeleteCatInteractor implements DeleteCatInputBoundary {
    private final DeleteCatDataAccessInterface dataAccess;
    private final DeleteCatOutputBoundary outputBoundary;

    /**
     * Creates a new DeleteCatInteractor.
     * @param dataAccess the data access interface for deleting cats
     * @param outputBoundary the output boundary for presenting results
     */
    public DeleteCatInteractor(
        final DeleteCatDataAccessInterface dataAccess,
        final DeleteCatOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public DeleteCatResponseDto execute(final DeleteCatInputData inputData) throws DatabaseAccessException {
        final String catName = inputData.getCatName();
        final String ownerUsername = inputData.getOwnerUsername();

        dataAccess.deleteCat(catName, ownerUsername);
        return outputBoundary.prepareResponse(new DeleteCatOutputData("Cat deleted successfully", catName));
    }
}
