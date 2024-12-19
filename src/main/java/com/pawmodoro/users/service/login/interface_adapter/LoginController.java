package com.pawmodoro.users.service.login.interface_adapter;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.users.entity.UserNotFoundException;
import com.pawmodoro.users.service.login.LoginInputBoundary;
import com.pawmodoro.users.service.login.LoginInputData;

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
     * and processes it through the use case interactor.
     * @param request the login request containing username and password
     * @return LoginResponseDTO containing the login response
     * @throws UserNotFoundException if the user is not found
     * @throws DatabaseAccessException if there's an error accessing the database
     */
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDTO login(
        @Valid
        @RequestBody
        LoginRequestDTO request) throws UserNotFoundException, DatabaseAccessException {

        LoginInputData inputData = new LoginInputData(request.username(), request.password());
        return loginInteractor.execute(inputData);
    }
}
