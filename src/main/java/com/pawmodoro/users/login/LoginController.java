package com.pawmodoro.users.login;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.http.HttpStatus;

/**
 * Controller handling user login requests.
 * This class serves as the interface adapter layer in Clean Architecture,
 * converting HTTP requests into application-specific DTOs and vice versa.
 */
@RestController
@RequestMapping("/api/users/login")
public class LoginController {
    private final LoginInputBoundary loginInteractor;
    private final LoginPresenter loginPresenter;

    /**
     * Constructs a LoginController with required dependencies.
     * @param loginInteractor the use case interactor for login operations
     * @param loginPresenter the presenter for formatting responses
     */
    public LoginController(LoginInputBoundary loginInteractor, LoginPresenter loginPresenter) {
        this.loginInteractor = loginInteractor;
        this.loginPresenter = loginPresenter;
    }

    /**
     * Handles POST requests for user login.
     * Validates the request, converts it into a domain-specific input data object,
     * processes it through the use case interactor, and returns the formatted response.
     * @param request the login request containing username and password
     * @param bindingResult contains the validation results
     * @return ResponseEntity containing the login response or error details
     */
    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(
        @Valid
        @RequestBody
        LoginRequestDTO request,
        BindingResult bindingResult) {

        // Handle validation errors
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request");

            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(loginPresenter.formatErrorResponse(errorMessage));
        }

        try {
            LoginInputData inputData = new LoginInputData(request.username(), request.password());
            LoginResponseDTO responseDTO = loginInteractor.execute(inputData);
            return ResponseEntity.ok(responseDTO);
        }
        catch (InvalidLoginException exception) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(exception.getResponse());
        }
    }
}
