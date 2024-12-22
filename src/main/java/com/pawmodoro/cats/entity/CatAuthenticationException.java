package com.pawmodoro.cats.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.pawmodoro.cats.service.get_all_cats.interface_adapter.GetAllCatsResponseDto;

/**
 * Exception thrown when authentication fails for cat-related operations.
 * This is a runtime exception as it represents an authentication error that should be handled
 * at the application level.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class CatAuthenticationException extends Exception {
    private final transient GetAllCatsResponseDto response;

    /**
     * Constructs a CatAuthenticationException with an error message.
     * @param message the error message
     */
    public CatAuthenticationException(String message) {
        super(message);
        this.response = new GetAllCatsResponseDto(false, null, message);
    }

    /**
     * Gets the response DTO containing error details.
     * @return the error response DTO
     */
    public GetAllCatsResponseDto getResponse() {
        return response;
    }
}
