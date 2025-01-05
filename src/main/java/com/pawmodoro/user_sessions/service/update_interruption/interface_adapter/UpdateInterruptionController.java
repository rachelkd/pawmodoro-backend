package com.pawmodoro.user_sessions.service.update_interruption.interface_adapter;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.user_sessions.service.update_interruption.UpdateInterruptionInputBoundary;
import com.pawmodoro.user_sessions.service.update_interruption.UpdateInterruptionInputData;

/**
 * Controller for handling session interruption updates.
 */
@RestController
@RequestMapping("/api/sessions")
public class UpdateInterruptionController {
    private final UpdateInterruptionInputBoundary interactor;

    public UpdateInterruptionController(UpdateInterruptionInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Updates the interruption count for a session.
     * @param id The session ID
     * @return The updated session details
     * @throws DatabaseAccessException if there's an error accessing the database
     */
    @PatchMapping("/{id}/interruption")
    @ResponseStatus(HttpStatus.OK)
    public UpdateInterruptionResponseDto updateInterruption(@PathVariable UUID id) throws DatabaseAccessException {
        return interactor.execute(new UpdateInterruptionInputData(id));
    }
}
