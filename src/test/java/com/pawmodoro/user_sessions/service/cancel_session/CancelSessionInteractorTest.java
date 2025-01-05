package com.pawmodoro.user_sessions.service.cancel_session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import com.pawmodoro.user_sessions.service.cancel_session.interface_adapter.CancelSessionOutputBoundary;
import com.pawmodoro.user_sessions.service.cancel_session.interface_adapter.CancelSessionResponseDto;

/**
 * Unit tests for CancelSessionInteractor.
 */
@ExtendWith(MockitoExtension.class)
class CancelSessionInteractorTest {
    @Mock
    private CancelSessionDataAccessInterface dataAccess;

    @Mock
    private CancelSessionOutputBoundary presenter;

    private CancelSessionInteractor interactor;

    @BeforeEach
    void setUp() {
        interactor = new CancelSessionInteractor(dataAccess, presenter);
    }

    @Test
    void testExecuteWithValidInput() throws DatabaseAccessException {
        // Arrange
        final UUID sessionId = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        final ZonedDateTime startTime = ZonedDateTime.now();
        final CancelSessionInputData input = new CancelSessionInputData(sessionId);

        // Create initial session
        final UserSession initialSession = new UserSession(SessionType.FOCUS, 25);
        setPrivateField(initialSession, "id", sessionId);
        setPrivateField(initialSession, "userId", userId);
        setPrivateField(initialSession, "sessionStartTime", startTime);
        setPrivateField(initialSession, "sessionEndTime", startTime);

        // Mock data access calls
        when(dataAccess.getSession(sessionId)).thenReturn(initialSession);
        when(dataAccess.updateCancellation(any(UserSession.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });

        final CancelSessionResponseDto expectedResponse = CancelSessionResponseDto.builder()
            .id(sessionId)
            .userId(userId)
            .sessionType(SessionType.FOCUS.getValue())
            .sessionStartTime(startTime)
            .sessionEndTime(startTime)
            .durationMinutes(25)
            .completed(false)
            .interruptionCount(0)
            .build();

        when(presenter.prepareResponse(any(CancelSessionOutputData.class))).thenReturn(expectedResponse);

        // Act
        interactor.execute(input);

        // Verify interactions and output data
        verify(dataAccess).getSession(sessionId);
        verify(dataAccess).updateCancellation(any(UserSession.class));
        verifyOutputData(sessionId, userId, SessionType.FOCUS, startTime, 25, 0);
    }

    @Test
    void testExecuteWithDatabaseError() throws DatabaseAccessException {
        // Arrange
        final UUID sessionId = UUID.randomUUID();
        final CancelSessionInputData input = new CancelSessionInputData(sessionId);
        when(dataAccess.getSession(sessionId))
            .thenThrow(new DatabaseAccessException("Database error"));

        // Act & Assert
        assertThrows(DatabaseAccessException.class, () -> interactor.execute(input));
    }

    private void verifyOutputData(UUID sessionId, UUID userId, SessionType sessionType, ZonedDateTime startTime,
        int durationMinutes, int expectedInterruptions) {
        final ArgumentCaptor<CancelSessionOutputData> outputDataCaptor =
            forClass(CancelSessionOutputData.class);
        verify(presenter).prepareResponse(outputDataCaptor.capture());
        final CancelSessionOutputData capturedOutputData = outputDataCaptor.getValue();

        assertEquals(sessionId, capturedOutputData.getId());
        assertEquals(userId, capturedOutputData.getUserId());
        assertEquals(sessionType, capturedOutputData.getSessionType());
        assertEquals(startTime, capturedOutputData.getSessionStartTime());
        assertTrue(capturedOutputData.getSessionEndTime().isAfter(startTime),
            "Session end time should be after start time");
        assertEquals(durationMinutes, capturedOutputData.getDurationMinutes());
        assertEquals(false, capturedOutputData.isCompleted());
        assertEquals(expectedInterruptions, capturedOutputData.getInterruptionCount());
    }

    private void setPrivateField(UserSession session, String fieldName, Object value) {
        try {
            final var field = UserSession.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(session, value);
        }
        catch (NoSuchFieldException | IllegalAccessException exception) {
            throw new RuntimeException("Failed to set field: " + fieldName, exception);
        }
    }
}
