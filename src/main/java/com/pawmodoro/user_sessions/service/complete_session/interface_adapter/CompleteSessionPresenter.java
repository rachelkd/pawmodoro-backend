package com.pawmodoro.user_sessions.service.complete_session.interface_adapter;

import org.springframework.stereotype.Component;

import com.pawmodoro.user_sessions.service.complete_session.CompleteSessionOutputData;

/**
 * Presenter for the complete session use case.
 * Transforms output data into the response DTO format.
 */
@Component
public class CompleteSessionPresenter implements CompleteSessionOutputBoundary {
    @Override
    public CompleteSessionResponseDto prepareResponse(CompleteSessionOutputData output) {
        return CompleteSessionResponseDto.builder()
            .id(output.getId())
            .userId(output.getUserId())
            .sessionType(output.getSessionType().getValue())
            .sessionStartTime(output.getSessionStartTime())
            .sessionEndTime(output.getSessionEndTime())
            .durationMinutes(output.getDurationMinutes())
            .completed(output.isCompleted())
            .interruptionCount(output.getInterruptionCount())
            .build();
    }
}
