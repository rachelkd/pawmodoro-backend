package com.pawmodoro;

import com.pawmodoro.users.UserNotFoundException;
import com.pawmodoro.users.login.InvalidLoginException;
import com.pawmodoro.users.login.LoginResponseDTO;
import com.pawmodoro.users.signup.InvalidSignupException;
import com.pawmodoro.users.signup.SignupResponseDTO;

import entity.exceptions.DatabaseAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

/**
 * Global exception handler for the application.
 * This class centralizes all exception handling and provides consistent error responses.
 * It follows the REST API best practices for error handling.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors from request body validation.
     * This occurs when @Valid validation fails on controller methods.
     * @param exception the validation exception
     * @param request the web request
     * @return response entity with validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
        MethodArgumentNotValidException exception,
        WebRequest request) {

        String errorMessage = exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getDefaultMessage())
            .collect(Collectors.joining(", "));

        // Determine which DTO to use based on the request path
        String path = request.getDescription(false);
        Object errorResponse;

        if (path.contains("/signup")) {
            errorResponse = SignupResponseDTO.error(errorMessage);
        }
        else if (path.contains("/login")) {
            errorResponse = new LoginResponseDTO(false, null, errorMessage, null);
        }
        else {
            // Generic error response for unknown endpoints
            errorResponse = new ErrorResponse(errorMessage);
        }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
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
            .body(new LoginResponseDTO(false, null, exception.getMessage(), null));
    }

    /**
     * Handles database access errors.
     * This occurs when there are issues connecting to or querying the database.
     * @param exception the database access exception
     * @param request the web request
     * @return response entity with error details
     */
    @ExceptionHandler(DatabaseAccessException.class)
    public ResponseEntity<Object> handleDatabaseAccessException(
        DatabaseAccessException exception,
        WebRequest request) {
        String errorMessage = exception.getMessage();

        // Determine which DTO to use based on the request path
        String path = request.getDescription(false);
        Object errorResponse;

        if (path.contains("/signup")) {
            errorResponse = SignupResponseDTO.error(errorMessage);
        }
        else if (path.contains("/login")) {
            errorResponse = new LoginResponseDTO(false, null, errorMessage, null);
        }
        else {
            errorResponse = new ErrorResponse(errorMessage);
        }

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse);
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
     * @param request the web request
     * @return response entity with error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnexpectedException(
        Exception exception,
        WebRequest request) {
        String errorMessage = "An unexpected error occurred. Please try again later.";

        // Determine which DTO to use based on the request path
        String path = request.getDescription(false);
        Object errorResponse;

        if (path.contains("/signup")) {
            errorResponse = SignupResponseDTO.error(errorMessage);
        }
        else if (path.contains("/login")) {
            errorResponse = new LoginResponseDTO(false, null, errorMessage, null);
        }
        else {
            errorResponse = new ErrorResponse(errorMessage);
        }

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse);
    }

    /**
     * Generic error response for unknown endpoints.
     */
    private record ErrorResponse(String message) {}
}
