package com.pawmodoro.cats.service.update_cat_hunger.interface_adapter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pawmodoro.cats.service.update_cat_hunger.UpdateCatHungerInputBoundary;
import com.pawmodoro.cats.service.update_cat_hunger.UpdateCatHungerInputData;
import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;
import jakarta.validation.Valid;

/**
 * Controller for handling HTTP requests related to updating cat hunger levels.
 */
@RestController
@RequestMapping("/api/cats")
public class UpdateCatHungerController {
    private final UpdateCatHungerInputBoundary updateCatHungerInteractor;

    public UpdateCatHungerController(final UpdateCatHungerInputBoundary updateCatHungerInteractor) {
        this.updateCatHungerInteractor = updateCatHungerInteractor;
    }

    /**
     * Updates a cat's hunger level.
     * @param ownerUsername the username of the cat's owner
     * @param catName the name of the cat
     * @param requestBody the request containing the change amount
     * @return the updated cat information
     * @throws DatabaseAccessException if there is an error accessing the database
     * @throws AuthenticationException if the authentication token is invalid or missing
     */
    @PutMapping("/{ownerUsername}/{catName}/hunger")
    @ResponseStatus(HttpStatus.OK)
    public UpdateCatHungerResponseDto updateHunger(
        @PathVariable final String ownerUsername,
        @PathVariable final String catName,
        @Valid @RequestBody final UpdateCatHungerRequestDto requestBody) throws DatabaseAccessException {
        final UpdateCatHungerInputData inputData = new UpdateCatHungerInputData(
            catName,
            ownerUsername,
            requestBody.changeAmount());
        return updateCatHungerInteractor.execute(inputData);
    }
}
