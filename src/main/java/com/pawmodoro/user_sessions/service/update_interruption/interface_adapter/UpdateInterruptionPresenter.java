package com.pawmodoro.user_sessions.service.update_interruption.interface_adapter;

import org.springframework.stereotype.Component;

import com.pawmodoro.user_sessions.service.update_interruption.UpdateInterruptionOutputBoundary;
import com.pawmodoro.user_sessions.service.update_interruption.UpdateInterruptionOutputData;

/**
 * Presenter for the update interruption use case.
 * Transforms output data into the response DTO format.
 */
@Component
public class UpdateInterruptionPresenter implements UpdateInterruptionOutputBoundary {
    @Override
    public UpdateInterruptionResponseDto prepareResponse(UpdateInterruptionOutputData outputData) {
        return UpdateInterruptionResponseDto.builder()
            .id(outputData.getId())
            .userId(outputData.getUserId())
            .sessionType(outputData.getSessionType().getValue())
            .sessionStartTime(outputData.getSessionStartTime())
            .sessionEndTime(outputData.getSessionEndTime())
            .durationMinutes(outputData.getDurationMinutes())
            .completed(outputData.isCompleted())
            .interruptionCount(outputData.getInterruptionCount())
            .build();
    }
}
