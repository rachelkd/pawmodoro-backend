package com.pawmodoro.core;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pawmodoro.cats.entity.CatAlreadyExistsException;
import com.pawmodoro.cats.entity.InvalidGetAllCatsException;
import com.pawmodoro.cats.entity.NoCatsFoundException;
import com.pawmodoro.user_sessions.entity.NoSessionFoundException;
import com.pawmodoro.users.entity.EmailAlreadyRegisteredException;
import com.pawmodoro.users.entity.InvalidLoginException;
import com.pawmodoro.users.entity.InvalidLogoutException;
import com.pawmodoro.users.entity.InvalidSignupException;
import com.pawmodoro.users.entity.UserNotFoundException;

/**
 * Global exception handler for the application.
 * This class provides centralized exception handling across all @RequestMapping methods
 * through @ExceptionHandler methods.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Standard error response structure for consistent error handling.
     * @param message Error message
     * @return Map containing error details
     */
    private Map<String, String> createErrorResponse(String message) {
        final Map<String, String> errorResponse = new HashMap<>();
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
        final Map<String, String> errorMap = new HashMap<>();
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
     * Handles authentication failures.
     * @param exception Authentication exception
     * @return Map containing error details
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public Map<String, String> handleAuthenticationException(AuthenticationException exception) {
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
     * Handles no cats found scenarios.
     * @param exception No cats found exception
     * @return Map containing error details
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoCatsFoundException.class)
    public Map<String, String> handleNoCatsFoundException(NoCatsFoundException exception) {
        return createErrorResponse(exception.getMessage());
    }

    /**
     * Handles cat already exists scenarios.
     * @param exception Cat already exists exception
     * @return Map containing error details
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CatAlreadyExistsException.class)
    public Map<String, String> handleCatAlreadyExistsException(CatAlreadyExistsException exception) {
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
        LOGGER.error("Database access error: {}", exception.getMessage());
        LOGGER.error("Stack trace:", exception);
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
     * Handles attempts to register with an email that is already in use.
     * @param exception Email already registered exception
     * @return Map containing error details
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public Map<String, String> handleEmailAlreadyRegisteredException(EmailAlreadyRegisteredException exception) {
        return createErrorResponse(exception.getMessage());
    }

    /**
     * Handles no session found scenarios.
     * @param exception No session found exception
     * @return Map containing error details
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSessionFoundException.class)
    public Map<String, String> handleNoSessionFoundException(NoSessionFoundException exception) {
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
     * Handles logout failures.
     * @param exception Invalid logout exception
     * @return Map containing error details
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidLogoutException.class)
    public Map<String, String> handleInvalidLogoutException(InvalidLogoutException exception) {
        return createErrorResponse(exception.getMessage());
    }

    /**
     * Handles forbidden access attempts.
     * @param exception Forbidden access exception
     * @return Map containing error details
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenAccessException.class)
    public Map<String, String> handleForbiddenAccessException(ForbiddenAccessException exception) {
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
        LOGGER.error("Unexpected error: {}", exception.getMessage());
        LOGGER.error("Stack trace:", exception);
        return createErrorResponse("An unexpected error occurred. Please try again later.");
    }
}
