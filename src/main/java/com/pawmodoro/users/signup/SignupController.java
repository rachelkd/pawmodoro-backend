package com.pawmodoro.users.signup;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;
import entity.exceptions.DatabaseAccessException;

/**
 * Controller handling user signup requests.
 * This class serves as the interface adapter layer in Clean Architecture,
 * converting HTTP requests into application-specific DTOs and vice versa.
 */
@RestController
@RequestMapping("/api/users/signup")
public class SignupController {
    private final SignupInputBoundary signupInteractor;

    /**
     * Constructs a SignupController with required dependencies.
     * @param signupInteractor the use case interactor for signup operations
     */
    public SignupController(SignupInputBoundary signupInteractor) {
        this.signupInteractor = signupInteractor;
    }

    /**
     * Handles POST requests for user signup.
     * Validates the request, converts it into a domain-specific input data object,
     * processes it through the use case interactor, and returns the formatted response.
     * @param request the signup request containing username, email, and password
     * @return ResponseEntity containing the signup response
     * @throws DatabaseAccessException if there is a database error
     */
    @PostMapping
    public ResponseEntity<SignupResponseDTO> signup(
        @Valid
        @RequestBody
        SignupRequestDTO request) throws DatabaseAccessException {

        SignupInputData inputData = new SignupInputData(
            request.username(),
            request.email(),
            request.password(),
            request.confirmPassword());

        SignupResponseDTO responseDTO = signupInteractor.execute(inputData);
        return ResponseEntity.ok(responseDTO);
    }
}
