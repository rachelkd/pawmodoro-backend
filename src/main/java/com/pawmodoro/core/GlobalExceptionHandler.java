package com.pawmodoro.core;

import com.pawmodoro.cats.entity.CatAuthenticationException;
import com.pawmodoro.cats.service.get_all_cats.InvalidGetAllCatsException;
import com.pawmodoro.users.entity.UserNotFoundException;
import com.pawmodoro.users.service.login.InvalidLoginException;
import com.pawmodoro.users.service.signup.InvalidSignupException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * This class provides centralized exception handling across all @RequestMapping methods
 * through @ExceptionHandler methods.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Standard error response structure for consistent error handling.
     * @param message Error message
     * @return Map containing error details
     */
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", message);
        return errorResponse;
    }

    /**
     * Handles validation errors for request parameters and request body.
     * @param exception Validation exception
     * @return Map containing field-level validation errors
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors()
            .forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return errorMap;
    }

    /**
     * Handles invalid login attempts.
     * @param exception Invalid login exception
     * @return Map containing error details
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidLoginException.class)
    public Map<String, String> handleInvalidLoginException(InvalidLoginException exception) {
        return createErrorResponse(exception.getMessage());
    }

    /**
     * Handles user not found scenarios.
     * @param exception User not found exception
     * @return Map containing error details
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public Map<String, String> handleUserNotFoundException(UserNotFoundException exception) {
        return createErrorResponse(exception.getMessage());
    }

    /**
     * Handles database access errors.
     * @param exception Database access exception
     * @return Map containing error details
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DatabaseAccessException.class)
    public Map<String, String> handleDatabaseAccessException(DatabaseAccessException exception) {
        return createErrorResponse(exception.getMessage());
    }

    /**
     * Handles invalid signup attempts.
     * @param exception Invalid signup exception
     * @return Map containing error details
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidSignupException.class)
    public Map<String, String> handleInvalidSignupException(InvalidSignupException exception) {
        return createErrorResponse(exception.getMessage());
    }

    /**
     * Handles cat authentication failures.
     * @param exception Cat authentication exception
     * @return Map containing error details
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CatAuthenticationException.class)
    public Map<String, String> handleCatAuthenticationException(CatAuthenticationException exception) {
        return createErrorResponse(exception.getMessage());
    }

    /**
     * Handles invalid get all cats inputs.
     * @param exception InvalidGetAllCatsException
     * @return Map containing error details
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidGetAllCatsException.class)
    public Map<String, String> handleInvalidGetAllCatsException(InvalidGetAllCatsException exception) {
        return createErrorResponse(exception.getMessage());
    }

    /**
     * Handles all other unhandled exceptions.
     * @param exception Unexpected exception
     * @return Map containing error details
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map<String, String> handleUnexpectedException(Exception exception) {
        return createErrorResponse("An unexpected error occurred. Please try again later.");
    }
}
