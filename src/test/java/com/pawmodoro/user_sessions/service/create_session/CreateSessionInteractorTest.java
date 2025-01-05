package com.pawmodoro.user_sessions.service.create_session;

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
import com.pawmodoro.user_sessions.service.create_session.interface_adapter.CreateSessionResponseDto;

/**
 * Unit tests for CreateSessionInteractor.
 */
@ExtendWith(MockitoExtension.class)
class CreateSessionInteractorTest {
    @Mock
    private CreateSessionDataAccessInterface dataAccess;

    @Mock
    private CreateSessionOutputBoundary presenter;

    private CreateSessionInteractor interactor;

    @BeforeEach
    void setUp() {
        interactor = new CreateSessionInteractor(dataAccess, presenter);
    }

    @Test
    void testExecuteWithValidInput() throws DatabaseAccessException {
        // Arrange
        final SessionType sessionType = SessionType.FOCUS;
        final int durationMinutes = 25;
        final CreateSessionInputData input = new CreateSessionInputData(sessionType, durationMinutes);

        final UUID userId = UUID.randomUUID();
        final UUID sessionId = UUID.randomUUID();
        final ZonedDateTime now = ZonedDateTime.now();

        // Mock saved session
        final UserSession savedSession = new UserSession(sessionType, durationMinutes);
        // Use reflection to set fields that would be set by Supabase
        setPrivateField(savedSession, "id", sessionId);
        setPrivateField(savedSession, "userId", userId);
        setPrivateField(savedSession, "sessionStartTime", now);
        setPrivateField(savedSession, "sessionEndTime", now);

        when(dataAccess.create(any(UserSession.class))).thenReturn(savedSession);

        final CreateSessionResponseDto expectedResponse = CreateSessionResponseDto.builder()
            .id(sessionId)
            .userId(userId)
            .sessionType(sessionType.getValue())
            .sessionStartTime(now)
            .sessionEndTime(now)
            .durationMinutes(durationMinutes)
            .completed(false)
            .interruptionCount(0)
            .build();

        when(presenter.prepareResponse(any(CreateSessionOutputData.class))).thenReturn(expectedResponse);

        // Act
        final CreateSessionResponseDto response = interactor.execute(input);

        // Assert response
        assertResponseMatches(response, sessionId, userId, sessionType, durationMinutes);

        // Verify interactions and output data
        verify(dataAccess).create(any(UserSession.class));
        verifyOutputData(sessionId, userId, sessionType, now, durationMinutes);
    }

    private void assertResponseMatches(CreateSessionResponseDto response, UUID sessionId, UUID userId,
        SessionType sessionType, int durationMinutes) {
        assertNotNull(response);
        assertEquals(sessionId, response.id());
        assertEquals(userId, response.userId());
        assertEquals(sessionType.getValue(), response.sessionType());
        assertEquals(durationMinutes, response.durationMinutes());
        assertEquals(false, response.completed());
        assertEquals(0, response.interruptionCount());
    }

    private void verifyOutputData(UUID sessionId, UUID userId, SessionType sessionType, ZonedDateTime timestamp,
        int durationMinutes) {
        final ArgumentCaptor<CreateSessionOutputData> outputDataCaptor = forClass(CreateSessionOutputData.class);
        verify(presenter).prepareResponse(outputDataCaptor.capture());
        final CreateSessionOutputData capturedOutputData = outputDataCaptor.getValue();

        assertEquals(sessionId, capturedOutputData.getId());
        assertEquals(userId, capturedOutputData.getUserId());
        assertEquals(sessionType, capturedOutputData.getSessionType());
        assertEquals(timestamp, capturedOutputData.getSessionStartTime());
        assertEquals(timestamp, capturedOutputData.getSessionEndTime());
        assertEquals(durationMinutes, capturedOutputData.getDurationMinutes());
        assertEquals(false, capturedOutputData.isCompleted());
        assertEquals(0, capturedOutputData.getInterruptionCount());
    }

    @Test
    void testExecuteWithDatabaseError() throws DatabaseAccessException {
        // Arrange
        final CreateSessionInputData input = new CreateSessionInputData(SessionType.FOCUS, 25);
        when(dataAccess.create(any(UserSession.class)))
            .thenThrow(new DatabaseAccessException("Database error"));

        // Act & Assert
        assertThrows(DatabaseAccessException.class, () -> interactor.execute(input));
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
