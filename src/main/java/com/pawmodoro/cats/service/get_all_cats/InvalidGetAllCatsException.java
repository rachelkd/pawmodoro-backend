package com.pawmodoro.cats.service.get_all_cats;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.pawmodoro.cats.service.get_all_cats.interface_adapter.GetAllCatsResponseDto;

/**
 * Exception thrown when get all cats validation fails.
 * This is a runtime exception as it represents a validation error that should be handled
 * at the application level rather than requiring explicit exception handling.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidGetAllCatsException extends RuntimeException {
    private final transient GetAllCatsResponseDto response;

    /**
     * Constructs an InvalidGetAllCatsException with an error message.
     * @param message the error message
     */
    public InvalidGetAllCatsException(String message) {
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
