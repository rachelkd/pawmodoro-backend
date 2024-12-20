package com.pawmodoro.users.service.signup.interface_adapter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.users.service.signup.SignupInputBoundary;
import com.pawmodoro.users.service.signup.SignupInputData;
import jakarta.validation.Valid;

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
     * and processes it through the use case interactor.
     * @param request the signup request containing username, email, and password
     * @return SignupResponseDTO containing the signup response
     * @throws DatabaseAccessException if there is a database error
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SignupResponseDto signup(
        @Valid
        @RequestBody
        SignupRequestDto request) throws DatabaseAccessException {

        final SignupInputData inputData = new SignupInputData(
            request.username(),
            request.email(),
            request.password(),
            request.confirmPassword());

        return signupInteractor.execute(inputData);
    }
}
