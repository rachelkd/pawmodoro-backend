package com.pawmodoro.user_sessions.service.update_interruption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.user_sessions.entity.SessionType;
import com.pawmodoro.user_sessions.entity.UserSession;
import com.pawmodoro.user_sessions.service.update_interruption.interface_adapter.UpdateInterruptionResponseDto;

/**
 * Unit tests for UpdateInterruptionInteractor.
 */
@ExtendWith(MockitoExtension.class)
class UpdateInterruptionInteractorTest {
    @Mock
    private UpdateInterruptionDataAccessInterface dataAccess;

    @Mock
    private UpdateInterruptionOutputBoundary presenter;

    private UpdateInterruptionInteractor interactor;

    @BeforeEach
    void setUp() {
        interactor = new UpdateInterruptionInteractor(dataAccess, presenter);
    }

    @Test
    void testExecuteWithValidInput() throws DatabaseAccessException {
        // Arrange
        final UUID sessionId = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        final ZonedDateTime now = ZonedDateTime.now();
        final UpdateInterruptionInputData input = new UpdateInterruptionInputData(sessionId);

        // Create initial session with 0 interruptions
        final UserSession initialSession = new UserSession(SessionType.FOCUS, 25);
        setPrivateField(initialSession, "id", sessionId);
        setPrivateField(initialSession, "userId", userId);
        setPrivateField(initialSession, "sessionStartTime", now);
        setPrivateField(initialSession, "sessionEndTime", now);

        // Create expected updated session with 1 interruption
        final UserSession updatedSession = new UserSession(SessionType.FOCUS, 25);
        setPrivateField(updatedSession, "id", sessionId);
        setPrivateField(updatedSession, "userId", userId);
        setPrivateField(updatedSession, "sessionStartTime", now);
        setPrivateField(updatedSession, "sessionEndTime", now);
        updatedSession.recordInterruption();

        // Mock data access calls
        when(dataAccess.getSession(sessionId)).thenReturn(initialSession);
        when(dataAccess.updateInterruptionCount(any(UserSession.class))).thenReturn(updatedSession);

        final UpdateInterruptionResponseDto expectedResponse = UpdateInterruptionResponseDto.builder()
            .id(sessionId)
            .userId(userId)
            .sessionType(SessionType.FOCUS.getValue())
            .sessionStartTime(now)
            .sessionEndTime(now)
            .durationMinutes(25)
            .completed(false)
            .interruptionCount(1)
            .build();

        when(presenter.prepareResponse(any(UpdateInterruptionOutputData.class))).thenReturn(expectedResponse);

        // Act
        final UpdateInterruptionResponseDto response = interactor.execute(input);

        // Assert response
        assertResponseMatches(response, sessionId, userId, SessionType.FOCUS, now, 25, 1);

        // Verify interactions and output data
        verify(dataAccess).getSession(sessionId);
        verify(dataAccess).updateInterruptionCount(any(UserSession.class));
        verifyOutputData(sessionId, userId, SessionType.FOCUS, now, 25, 1);
    }

    @Test
    void testExecuteWithDatabaseError() throws DatabaseAccessException {
        // Arrange
        final UUID sessionId = UUID.randomUUID();
        final UpdateInterruptionInputData input = new UpdateInterruptionInputData(sessionId);
        when(dataAccess.getSession(sessionId))
            .thenThrow(new DatabaseAccessException("Database error"));

        // Act & Assert
        assertThrows(DatabaseAccessException.class, () -> interactor.execute(input));
    }

    private void assertResponseMatches(UpdateInterruptionResponseDto response, UUID sessionId, UUID userId,
        SessionType sessionType, ZonedDateTime timestamp, int durationMinutes, int expectedInterruptions) {
        assertNotNull(response);
        assertEquals(sessionId, response.id());
        assertEquals(userId, response.userId());
        assertEquals(sessionType.getValue(), response.sessionType());
        assertEquals(timestamp, response.sessionStartTime());
        assertEquals(timestamp, response.sessionEndTime());
        assertEquals(durationMinutes, response.durationMinutes());
        assertEquals(false, response.completed());
        assertEquals(expectedInterruptions, response.interruptionCount());
    }

    private void verifyOutputData(UUID sessionId, UUID userId, SessionType sessionType, ZonedDateTime timestamp,
        int durationMinutes, int expectedInterruptions) {
        final ArgumentCaptor<UpdateInterruptionOutputData> outputDataCaptor =
            forClass(UpdateInterruptionOutputData.class);
        verify(presenter).prepareResponse(outputDataCaptor.capture());
        final UpdateInterruptionOutputData capturedOutputData = outputDataCaptor.getValue();

        assertEquals(sessionId, capturedOutputData.getId());
        assertEquals(userId, capturedOutputData.getUserId());
        assertEquals(sessionType, capturedOutputData.getSessionType());
        assertEquals(timestamp, capturedOutputData.getSessionStartTime());
        assertEquals(timestamp, capturedOutputData.getSessionEndTime());
        assertEquals(durationMinutes, capturedOutputData.getDurationMinutes());
        assertEquals(false, capturedOutputData.isCompleted());
        assertEquals(expectedInterruptions, capturedOutputData.getInterruptionCount());
    }

    /**
     * Helper method to set private fields using reflection.
     * This is necessary because some fields are set by Supabase and don't have setters.
     */
    private void setPrivateField(Object object, String fieldName, Object value) {
        try {
            final var field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        }
        catch (NoSuchFieldException | IllegalAccessException exception) {
            throw new RuntimeException("Failed to set field " + fieldName, exception);
        }
    }
}
