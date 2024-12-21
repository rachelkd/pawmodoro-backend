package com.pawmodoro.users.service.logout;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when logout fails.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidLogoutException extends RuntimeException {
    public InvalidLogoutException(String message) {
        super(message);
    }
}
