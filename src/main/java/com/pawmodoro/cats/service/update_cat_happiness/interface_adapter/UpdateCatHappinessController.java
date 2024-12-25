package com.pawmodoro.cats.service.update_cat_happiness.interface_adapter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pawmodoro.cats.service.update_cat_happiness.UpdateCatHappinessInputBoundary;
import com.pawmodoro.cats.service.update_cat_happiness.UpdateCatHappinessInputData;
import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;
import jakarta.validation.Valid;

/**
 * Controller for handling HTTP requests related to updating cat happiness levels.
 */
@RestController
@RequestMapping("/api/cats")
public class UpdateCatHappinessController {
    private final UpdateCatHappinessInputBoundary updateCatHappinessInteractor;

    public UpdateCatHappinessController(final UpdateCatHappinessInputBoundary updateCatHappinessInteractor) {
        this.updateCatHappinessInteractor = updateCatHappinessInteractor;
    }

    /**
     * Updates a cat's happiness level.
     * @param ownerUsername the username of the cat's owner
     * @param catName the name of the cat
     * @param requestBody the request containing the change amount
     * @return the updated cat information
     * @throws DatabaseAccessException if there is an error accessing the database
     * @throws AuthenticationException if the authentication token is invalid or missing
     */
    @PutMapping("/{ownerUsername}/{catName}/happiness")
    @ResponseStatus(HttpStatus.OK)
    public UpdateCatHappinessResponseDto updateHappiness(
        @PathVariable final String ownerUsername,
        @PathVariable final String catName,
        @Valid @RequestBody final UpdateCatHappinessRequestDto requestBody) throws DatabaseAccessException {
        final UpdateCatHappinessInputData inputData = new UpdateCatHappinessInputData(
            catName,
            ownerUsername,
            requestBody.changeAmount());
        return updateCatHappinessInteractor.execute(inputData);
    }
}
