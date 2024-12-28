package com.pawmodoro.cats.service.update_cats_after_study;

import org.springframework.stereotype.Service;
import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.data_access.CatUpdateDataAccess.CatUpdateResult;
import com.pawmodoro.cats.service.update_cats_after_study.interface_adapter.UpdateCatsAfterStudyResponseDto;
import com.pawmodoro.constants.Constants;
import com.pawmodoro.core.DatabaseAccessException;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Interactor that implements the business logic for updating cats after study completion
 */
@Service
public class UpdateCatsAfterStudyInteractor implements UpdateCatsAfterStudyInputBoundary {
    private final UpdateCatsAfterStudyDataAccessInterface catDataAccess;
    private final UpdateCatsAfterStudyOutputBoundary outputBoundary;

    public UpdateCatsAfterStudyInteractor(
        UpdateCatsAfterStudyDataAccessInterface catDataAccess,
        UpdateCatsAfterStudyOutputBoundary outputBoundary) {
        this.catDataAccess = catDataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public UpdateCatsAfterStudyResponseDto execute(UpdateCatsAfterStudyInputData input) throws DatabaseAccessException {
        // Get username from token
        String username = catDataAccess.getUsernameFromToken(input.getToken());

        // Get all cats for the user
        Collection<Cat> cats = catDataAccess.getCatsByOwner(username);

        // Calculate new happiness levels for all cats after increase
        Map<Cat, Integer> updates = cats.stream()
            .collect(Collectors.toMap(
                cat -> cat,
                cat -> Math.clamp(
                    Math.round(cat.getHappinessLevel() * (1 + Constants.CatStats.STUDY_SESSION_HAPPINESS_INCREASE)),
                    0,
                    100)));

        // Update all cats in a single transaction
        CatUpdateResult updateResult = catDataAccess.updateCatsHappiness(updates);

        // Include both successful updates and failures in the response
        return outputBoundary.prepareResponse(new UpdateCatsAfterStudyOutputData(
            updateResult.getUpdatedCats(),
            updateResult.getFailures()));
    }
}
