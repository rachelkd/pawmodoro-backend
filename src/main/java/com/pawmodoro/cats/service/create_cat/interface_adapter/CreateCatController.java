package com.pawmodoro.cats.service.create_cat.interface_adapter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pawmodoro.cats.service.create_cat.CreateCatInputBoundary;
import com.pawmodoro.cats.service.create_cat.CreateCatInputData;
import com.pawmodoro.core.DatabaseAccessException;
import jakarta.validation.Valid;

/**
 * Controller for handling HTTP requests related to creating cats.
 * This endpoint allows users to create multiple cats at once by providing
 * a mapping of cat names to their corresponding image file names.
 */
@RestController
@RequestMapping("/api/cats")
public class CreateCatController {
    private final CreateCatInputBoundary createCatInteractor;

    public CreateCatController(final CreateCatInputBoundary createCatInteractor) {
        this.createCatInteractor = createCatInteractor;
    }

    /**
     * Handles POST requests to create cats for a specific user.
     * @param username the username of the cats' owner
     * @param request the request containing cat name and optional image file
     * @return the response containing the created cat
     * @throws CatAuthenticationException if the authentication fails
     * @throws DatabaseAccessException if there is an error accessing the database
     */
    @PostMapping("/{username}/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateCatResponseDto createCat(
        @PathVariable final String username,
        @Valid @RequestBody final CreateCatRequestDto request) throws DatabaseAccessException {
        final CreateCatInputData inputData = new CreateCatInputData(
            request.name(),
            username,
            request.imageFileName());
        return createCatInteractor.execute(inputData);
    }
}
