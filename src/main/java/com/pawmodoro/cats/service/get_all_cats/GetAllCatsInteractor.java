package com.pawmodoro.cats.service.get_all_cats;

import java.util.Collection;
import org.springframework.stereotype.Service;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.service.get_all_cats.interface_adapter.GetAllCatsResponseDTO;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Interactor for the Get All Cats use case.
 * Implements the business logic for retrieving all cats owned by a user.
 */
@Service
public class GetAllCatsInteractor implements GetAllCatsInputBoundary {
    private final GetAllCatsDataAccessInterface catDataAccessObject;
    private final GetAllCatsOutputBoundary getAllCatsPresenter;

    public GetAllCatsInteractor(
        GetAllCatsDataAccessInterface catDataAccessObject,
        GetAllCatsOutputBoundary getAllCatsPresenter) {
        this.catDataAccessObject = catDataAccessObject;
        this.getAllCatsPresenter = getAllCatsPresenter;
    }

    @Override
    public GetAllCatsResponseDTO execute(GetAllCatsInputData getAllCatsInputData) throws DatabaseAccessException {
        // Input validation
        if (getAllCatsInputData.getOwnerUsername() == null || getAllCatsInputData.getOwnerUsername().trim().isEmpty()) {
            throw new InvalidGetAllCatsException("Username cannot be null or empty");
        }

        Collection<Cat> cats = catDataAccessObject.getCatsByOwner(getAllCatsInputData.getOwnerUsername());
        GetAllCatsOutputData outputData = new GetAllCatsOutputData(cats, true, null);
        return getAllCatsPresenter.prepareSuccessResponse(outputData);
    }
}
