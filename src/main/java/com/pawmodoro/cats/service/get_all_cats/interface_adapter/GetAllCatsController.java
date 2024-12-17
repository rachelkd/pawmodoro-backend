package com.pawmodoro.cats.service.get_all_cats.interface_adapter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pawmodoro.cats.entity.CatAuthenticationException;
import com.pawmodoro.cats.service.get_all_cats.GetAllCatsInputBoundary;
import com.pawmodoro.cats.service.get_all_cats.GetAllCatsInputData;
import com.pawmodoro.core.DatabaseAccessException;

/**
 * Controller for handling HTTP requests related to retrieving all cats for a user.
 * This class serves as the interface adapter layer in Clean Architecture.
 */
@RestController
@RequestMapping("/api/cats")
public class GetAllCatsController {
    private final GetAllCatsInputBoundary getAllCatsInteractor;

    public GetAllCatsController(GetAllCatsInputBoundary getAllCatsInteractor) {
        this.getAllCatsInteractor = getAllCatsInteractor;
    }

    /**
     * Handles GET requests to retrieve all cats for a specific user.
     * @param username the username of the cats' owner
     * @return ResponseEntity containing the cats data or error message
     * @throws DatabaseAccessException if there is an error accessing the database
     * @throws CatAuthenticationException if the authentication fails
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<GetAllCatsResponseDTO> getAllCats(@PathVariable
    String username) throws DatabaseAccessException, CatAuthenticationException {
        GetAllCatsInputData inputData = new GetAllCatsInputData(username);
        GetAllCatsResponseDTO responseDTO = getAllCatsInteractor.execute(inputData);

        return responseDTO.success()
            ? ResponseEntity.ok(responseDTO)
            : ResponseEntity.badRequest().body(responseDTO);
    }
}
