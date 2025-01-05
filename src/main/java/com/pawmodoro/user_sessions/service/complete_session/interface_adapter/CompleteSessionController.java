package com.pawmodoro.user_sessions.service.complete_session.interface_adapter;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.user_sessions.service.complete_session.CompleteSessionInputBoundary;
import com.pawmodoro.user_sessions.service.complete_session.CompleteSessionInputData;

/**
 * Controller for handling session completion.
 */
@RestController
@RequestMapping("/api/sessions")
public class CompleteSessionController {
    private final CompleteSessionInputBoundary interactor;

    public CompleteSessionController(CompleteSessionInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Completes a session.
     * @param id The session ID
     * @return The updated session details
     * @throws DatabaseAccessException if there's an error accessing the database
     */
    @PatchMapping("/{id}/complete")
    @ResponseStatus(HttpStatus.OK)
    public CompleteSessionResponseDto completeSession(@PathVariable UUID id) throws DatabaseAccessException {
        return interactor.execute(new CompleteSessionInputData(id));
    }
}
