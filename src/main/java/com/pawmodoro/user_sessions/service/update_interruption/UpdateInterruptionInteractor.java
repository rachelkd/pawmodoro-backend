package com.pawmodoro.user_sessions.service.update_interruption;

import org.springframework.stereotype.Service;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.user_sessions.entity.UserSession;
import com.pawmodoro.user_sessions.service.update_interruption.interface_adapter.UpdateInterruptionResponseDto;

/**
 * Interactor for updating session interruption count.
 */
@Service
public class UpdateInterruptionInteractor implements UpdateInterruptionInputBoundary {
    private final UpdateInterruptionDataAccessInterface dataAccess;
    private final UpdateInterruptionOutputBoundary presenter;

    public UpdateInterruptionInteractor(
        UpdateInterruptionDataAccessInterface dataAccess,
        UpdateInterruptionOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public UpdateInterruptionResponseDto execute(UpdateInterruptionInputData input) throws DatabaseAccessException {
        // Get current session
        final UserSession session = dataAccess.getSession(input.sessionId());

        // Record interruption (business logic in entity)
        session.recordInterruption();

        // Update in database
        final UserSession updatedSession = dataAccess.updateSession(session);

        // Prepare output data
        final UpdateInterruptionOutputData outputData = UpdateInterruptionOutputData.builder()
            .id(updatedSession.getId())
            .userId(updatedSession.getUserId())
            .sessionType(updatedSession.getSessionType())
            .sessionStartTime(updatedSession.getSessionStartTime())
            .sessionEndTime(updatedSession.getSessionEndTime())
            .durationMinutes(updatedSession.getDurationMinutes())
            .completed(updatedSession.isCompleted())
            .interruptionCount(updatedSession.getInterruptionCount())
            .build();

        return presenter.prepareResponse(outputData);
    }
}
