package com.pawmodoro.user_sessions.service.create_session.interface_adapter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.user_sessions.service.create_session.CreateSessionInputBoundary;
import com.pawmodoro.user_sessions.service.create_session.CreateSessionInputData;
import jakarta.validation.Valid;

/**
 * Controller for creating new study sessions.
 */
@RestController
@RequestMapping("/api/sessions")
public class CreateSessionController {
    private final CreateSessionInputBoundary createSessionUseCase;

    public CreateSessionController(CreateSessionInputBoundary createSessionUseCase) {
        this.createSessionUseCase = createSessionUseCase;
    }

    /**
     * Creates a new study session.
     * @param requestDto The session details
     * @return The created session response
     * @throws DatabaseAccessException if there's an error accessing the database
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateSessionResponseDto create(
        @Valid @RequestBody CreateSessionRequestDto requestDto) throws DatabaseAccessException {
        // Convert request DTO to input data
        final CreateSessionInputData inputData = new CreateSessionInputData(
            requestDto.sessionType(),
            requestDto.durationMinutes());

        // Execute use case and return response
        return createSessionUseCase.execute(inputData);
    }
}
