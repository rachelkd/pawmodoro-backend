package com.pawmodoro.users.login;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;

/**
 * Controller handling user login requests.
 * This class serves as the interface adapter layer in Clean Architecture,
 * converting HTTP requests into application-specific DTOs and vice versa.
 */
@RestController
@RequestMapping("/api/users/login")
public class LoginController {
    private final LoginInputBoundary loginInteractor;

    /**
     * Constructs a LoginController with required dependencies.
     * @param loginInteractor the use case interactor for login operations
     */
    public LoginController(LoginInputBoundary loginInteractor) {
        this.loginInteractor = loginInteractor;
    }

    /**
     * Handles POST requests for user login.
     * Validates the request, converts it into a domain-specific input data object,
     * processes it through the use case interactor, and returns the formatted response.
     * @param request the login request containing username and password
     * @return ResponseEntity containing the login response
     */
    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(
        @Valid
        @RequestBody
        LoginRequestDTO request) {

        LoginInputData inputData = new LoginInputData(request.username(), request.password());
        LoginResponseDTO responseDTO = loginInteractor.execute(inputData);
        return ResponseEntity.ok(responseDTO);

    }
}
