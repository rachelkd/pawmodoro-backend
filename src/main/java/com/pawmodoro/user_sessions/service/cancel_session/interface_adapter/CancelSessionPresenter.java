package com.pawmodoro.user_sessions.service.cancel_session.interface_adapter;

import org.springframework.stereotype.Component;

import com.pawmodoro.user_sessions.service.cancel_session.CancelSessionOutputData;

/**
 * Presenter for the cancel session use case.
 * Transforms output data into the response DTO format.
 */
@Component
public class CancelSessionPresenter implements CancelSessionOutputBoundary {

    @Override
    public CancelSessionResponseDto prepareResponse(CancelSessionOutputData output) {
        return CancelSessionResponseDto.builder()
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
