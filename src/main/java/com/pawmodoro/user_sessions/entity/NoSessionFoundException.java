package com.pawmodoro.user_sessions.entity;

import com.pawmodoro.constants.Constants;

/**
 * Exception thrown when a session is not found.
 */
public class NoSessionFoundException extends RuntimeException {
    public NoSessionFoundException(String sessionId) {
        super(String.format(Constants.ErrorMessages.NO_SESSION_FOUND, sessionId));
    }
}
