package com.pawmodoro.cats.service.update_cats_after_study;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pawmodoro.cats.data_access.CatUpdateDataAccess.CatUpdateResult;
import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.service.update_cats_after_study.interface_adapter.UpdateCatsAfterStudyResponseDto;
import com.pawmodoro.constants.Constants;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Interactor that implements the business logic for updating cats after study completion.
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
        final String username = catDataAccess.getUsernameFromToken(input.getToken());
        final List<Cat> cats = catDataAccess.getCatsByOwner(username);
        final Map<Cat, Integer> updates = calculateHappinessUpdates(cats);
        final CatUpdateResult updateResult = catDataAccess.updateCatsHappiness(updates);

        return outputBoundary.prepareResponse(new UpdateCatsAfterStudyOutputData(
            updateResult.getUpdatedCats(),
            updateResult.getFailures()));
    }

    private Map<Cat, Integer> calculateHappinessUpdates(List<Cat> cats) {
        return cats.stream()
            .collect(Collectors.toMap(
                cat -> cat,
                this::calculateNewHappiness,
                (first, second) -> first,
                LinkedHashMap::new));
    }

    private int calculateNewHappiness(Cat cat) {
        final int currentHappiness = cat.getHappinessLevel();
        double increasePercentage = Constants.CatStats.BASE_HAPPINESS_PERCENTAGE;

        if (currentHappiness < Constants.CatStats.LOW_HAPPINESS_THRESHOLD) {
            increasePercentage += Constants.CatStats.LOW_HAPPINESS_BONUS_PERCENTAGE;
        }

        final int increase = Math.max(
            Constants.CatStats.MIN_HAPPINESS_INCREASE,
            (int) Math.round(currentHappiness * increasePercentage));

        return Math.clamp((long) currentHappiness + increase, 0, Constants.CatStats.MAX_HAPPINESS_LEVEL);
    }
}
