package com.pawmodoro.user_sessions.service.update_interruption;

import java.util.UUID;

/**
 * Input data for updating session interruption count.
 * Contains only the session ID since it's the only required information.
 * @param sessionId The ID of the session to update
 */
public record UpdateInterruptionInputData(
    UUID sessionId) {

}
