package com.pawmodoro.user_sessions.service.cancel_session.interface_adapter;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
     * Cancels a session.
     * @param id The session ID
     * @return The updated session details
     * @throws DatabaseAccessException if there's an error accessing the database
     */
    @PatchMapping("/api/sessions/{id}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public CancelSessionResponseDto cancelSession(@PathVariable UUID id) throws DatabaseAccessException {
        return interactor.execute(new CancelSessionInputData(id));
    }
}
