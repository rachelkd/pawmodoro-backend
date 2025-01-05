package com.pawmodoro.user_sessions.service.create_session.interface_adapter;

import org.springframework.stereotype.Component;

import com.pawmodoro.user_sessions.service.create_session.CreateSessionOutputBoundary;
import com.pawmodoro.user_sessions.service.create_session.CreateSessionOutputData;

/**
 * Presenter for the create session use case.
 */
@Component
public class CreateSessionPresenter implements CreateSessionOutputBoundary {

    @Override
    public CreateSessionResponseDto prepareResponse(CreateSessionOutputData outputData) {
        return CreateSessionResponseDto.builder()
            .id(outputData.getId())
            .userId(outputData.getUserId())
            .sessionType(outputData.getSessionType().getValue())
            .sessionStartTime(outputData.getSessionStartTime())
            .sessionEndTime(outputData.getSessionEndTime())
            .durationMinutes(outputData.getDurationMinutes())
            .wasCompleted(outputData.isWasCompleted())
            .interruptionCount(outputData.getInterruptionCount())
            .build();
    }
}
