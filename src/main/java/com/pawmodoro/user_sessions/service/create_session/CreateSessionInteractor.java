package com.pawmodoro.user_sessions.service.create_session;

import org.springframework.stereotype.Service;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.user_sessions.entity.UserSession;
import com.pawmodoro.user_sessions.service.create_session.interface_adapter.CreateSessionResponseDto;

/**
 * Interactor for creating a new user session.
 */
@Service
public class CreateSessionInteractor implements CreateSessionInputBoundary {
    private final CreateSessionDataAccessInterface dataAccess;
    private final CreateSessionOutputBoundary presenter;

    public CreateSessionInteractor(
        CreateSessionDataAccessInterface dataAccess,
        CreateSessionOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public CreateSessionResponseDto execute(CreateSessionInputData input) throws DatabaseAccessException {
        // Create new UserSession entity - user ID will be set by Supabase based on JWT token
        final UserSession userSession = new UserSession(
            input.sessionType(),
            input.durationMinutes());

        // Save to database - Supabase will set the user ID from the JWT token
        final UserSession savedSession = dataAccess.create(userSession);

        // Prepare output data
        final CreateSessionOutputData outputData = CreateSessionOutputData.builder()
            .id(savedSession.getId())
            .userId(savedSession.getUserId())
            .sessionType(savedSession.getSessionType())
            .sessionStartTime(savedSession.getSessionStartTime())
            .sessionEndTime(savedSession.getSessionEndTime())
            .durationMinutes(savedSession.getDurationMinutes())
            .wasCompleted(savedSession.getWasCompleted())
            .interruptionCount(savedSession.getInterruptionCount())
            .build();

        // Transform to response format using presenter
        return presenter.prepareResponse(outputData);
    }
}
