package com.pawmodoro.user_sessions.service.cancel_session;

import org.springframework.stereotype.Service;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.user_sessions.entity.UserSession;
import com.pawmodoro.user_sessions.service.cancel_session.interface_adapter.CancelSessionOutputBoundary;
import com.pawmodoro.user_sessions.service.cancel_session.interface_adapter.CancelSessionResponseDto;

/**
 * Interactor for canceling a user's session.
 */
@Service
public class CancelSessionInteractor implements CancelSessionInputBoundary {
    private final CancelSessionDataAccessInterface dataAccess;
    private final CancelSessionOutputBoundary presenter;

    public CancelSessionInteractor(
        CancelSessionDataAccessInterface dataAccess,
        CancelSessionOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public CancelSessionResponseDto execute(CancelSessionInputData input) throws DatabaseAccessException {
        // Get current session
        final UserSession session = dataAccess.getSession(input.sessionId(), input.token());

        // Cancel session (business logic in entity)
        session.cancel();

        // Update in database
        final UserSession updatedSession = dataAccess.updateCancellation(session, input.token());

        // Prepare output data
        final CancelSessionOutputData outputData = CancelSessionOutputData.builder()
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
