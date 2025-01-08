package com.pawmodoro.user_sessions.service.cancel_session.interface_adapter;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.user_sessions.service.cancel_session.CancelSessionInputBoundary;
import com.pawmodoro.user_sessions.service.cancel_session.CancelSessionInputData;

/**
 * Controller for handling cancel session requests.
 */
@RestController
public class CancelSessionController {
    private final CancelSessionInputBoundary interactor;

    public CancelSessionController(CancelSessionInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Cancels a session. Supports both PATCH (regular requests) and POST (beacon requests).
     * @param id The session ID
     * @param headerAuth The authorization header
     * @param queryAuth The authorization from query parameters (for beacon requests)
     * @return The updated session details
     * @throws DatabaseAccessException if there's an error accessing the database
     * @throws AuthenticationException if no valid authorization is provided
     */
    @RequestMapping(
        path = "/api/sessions/{id}/cancel",
        method = {RequestMethod.PATCH, RequestMethod.POST})
    @ResponseStatus(HttpStatus.OK)
    public CancelSessionResponseDto cancelSession(
        @PathVariable UUID id,
        @RequestHeader(value = "Authorization", required = false) String headerAuth,
        @RequestParam(value = "Authorization", required = false) String queryAuth) throws DatabaseAccessException {

        final String authorization;
        if (headerAuth != null) {
            authorization = headerAuth;
        }
        else {
            authorization = queryAuth;
        }

        return interactor.execute(new CancelSessionInputData(id, authorization));
    }
}
