package com.pawmodoro;

import com.pawmodoro.users.UserNotFoundException;
import com.pawmodoro.users.login.InvalidLoginException;
import com.pawmodoro.users.login.LoginPresenter;
import com.pawmodoro.users.login.LoginResponseDTO;
import com.pawmodoro.users.signup.InvalidSignupException;
import com.pawmodoro.users.signup.SignupResponseDTO;
import com.pawmodoro.users.login.LoginOutputData;

import entity.exceptions.DatabaseAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * This class centralizes all exception handling and provides consistent error responses.
 * It follows the REST API best practices for error handling.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final LoginPresenter loginPresenter;

    public GlobalExceptionHandler(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    /**
     * Handles validation errors from request body validation.
     * This occurs when @Valid validation fails on controller methods.
     * @param exception the validation exception
     * @return map of field names to error messages
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors()
            .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    /**
     * Handles invalid login attempts.
     * This occurs when username/password combination is incorrect.
     * @param exception the invalid login exception
     * @return response entity with error details
     */
    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<LoginResponseDTO> handleInvalidLoginException(InvalidLoginException exception) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(exception.getResponse());
    }

    /**
     * Handles user not found errors.
     * This occurs when trying to access a non-existent user.
     * @param exception the user not found exception
     * @return response entity with error details
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<LoginResponseDTO> handleUserNotFoundException(UserNotFoundException exception) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(loginPresenter.prepareResponse(new LoginOutputData(exception.getMessage())));
    }

    /**
     * Handles database access errors.
     * This occurs when there are issues connecting to or querying the database.
     * @param exception the database access exception
     * @return response entity with error details
     */
    @ExceptionHandler(DatabaseAccessException.class)
    public ResponseEntity<LoginResponseDTO> handleDatabaseAccessException(DatabaseAccessException exception) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(loginPresenter.prepareResponse(
                new LoginOutputData("An internal error occurred. Please try again later.")));
    }

    /**
     * Handles invalid signup exceptions.
     * @param exception the invalid signup exception
     * @return response entity with error details
     */
    @ExceptionHandler(InvalidSignupException.class)
    public ResponseEntity<SignupResponseDTO> handleInvalidSignup(InvalidSignupException exception) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(exception.getResponse());
    }

    /**
     * Handles all other unhandled exceptions.
     * This is a catch-all handler for any unexpected errors.
     * @param exception the unhandled exception
     * @return response entity with error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<LoginResponseDTO> handleUnexpectedException(Exception exception) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(loginPresenter.prepareResponse(
                new LoginOutputData("An unexpected error occurred. Please try again later.")));
    }
}
