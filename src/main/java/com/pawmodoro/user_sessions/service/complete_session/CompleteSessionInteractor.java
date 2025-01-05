package com.pawmodoro.user_sessions.service.complete_session;

import org.springframework.stereotype.Service;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.user_sessions.entity.UserSession;
import com.pawmodoro.user_sessions.service.complete_session.interface_adapter.CompleteSessionOutputBoundary;
import com.pawmodoro.user_sessions.service.complete_session.interface_adapter.CompleteSessionResponseDto;

/**
 * Interactor for completing a user's session.
 */
@Service
public class CompleteSessionInteractor implements CompleteSessionInputBoundary {
    private final CompleteSessionDataAccessInterface dataAccess;
    private final CompleteSessionOutputBoundary presenter;

    public CompleteSessionInteractor(
        CompleteSessionDataAccessInterface dataAccess,
        CompleteSessionOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public CompleteSessionResponseDto execute(CompleteSessionInputData input) throws DatabaseAccessException {
        // Get current session
        final UserSession session = dataAccess.getSession(input.sessionId());

        // Complete session (business logic in entity)
        session.complete();

        // Update in database
        final UserSession updatedSession = dataAccess.updateCompletion(session);

        // Prepare output data
        final CompleteSessionOutputData outputData = CompleteSessionOutputData.builder()
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
