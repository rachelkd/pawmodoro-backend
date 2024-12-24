package com.pawmodoro.cats.service.delete_cat.interface_adapter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pawmodoro.cats.service.delete_cat.DeleteCatInputBoundary;
import com.pawmodoro.cats.service.delete_cat.DeleteCatInputData;
import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;
import jakarta.validation.Valid;

/**
 * Controller for handling HTTP requests related to deleting cats.
 * This endpoint allows users to delete their cats by providing the cat's name.
 */
@RestController
@RequestMapping("/api/cats")
public class DeleteCatController {
    private final DeleteCatInputBoundary deleteCatInteractor;

    public DeleteCatController(final DeleteCatInputBoundary deleteCatInteractor) {
        this.deleteCatInteractor = deleteCatInteractor;
    }

    /**
     * Handles DELETE requests to delete a cat for a specific user.
     * @param username the username of the cat's owner
     * @param request the request containing the cat name
     * @return the response containing the message and the deleted cat name
     * @throws AuthenticationException if the authentication fails
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public DeleteCatResponseDto deleteCat(
        @PathVariable final String username,
        @Valid @RequestBody final DeleteCatRequestDto request) throws DatabaseAccessException {
        final DeleteCatInputData inputData = new DeleteCatInputData(
            request.name(),
            username);
        return deleteCatInteractor.execute(inputData);
    }
}
