package com.pawmodoro.user_sessions.data_access;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pawmodoro.constants.Constants;
import com.pawmodoro.core.AbstractDataAccess;
import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.core.ForbiddenAccessException;
import com.pawmodoro.user_sessions.entity.UserSession;
import com.pawmodoro.user_sessions.service.cancel_session.CancelSessionDataAccessInterface;
import com.pawmodoro.user_sessions.service.complete_session.CompleteSessionDataAccessInterface;
import com.pawmodoro.user_sessions.service.create_session.CreateSessionDataAccessInterface;
import com.pawmodoro.user_sessions.service.update_interruption.UpdateInterruptionDataAccessInterface;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Implementation of data access operations for user sessions using Supabase.
 */
@Repository
public class UserSessionDataAccess extends AbstractDataAccess
    implements CreateSessionDataAccessInterface, UpdateInterruptionDataAccessInterface,
    CompleteSessionDataAccessInterface, CancelSessionDataAccessInterface {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private final ObjectMapper objectMapper;

    public UserSessionDataAccess(
        @Value("${supabase.url}") String apiUrl,
        @Value("${supabase.key}") String apiKey) {
        super(apiUrl, apiKey);
        this.objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .registerModule(new JavaTimeModule());
    }

    @Override
    public UserSession getSession(UUID sessionId) throws DatabaseAccessException {
        try {
            // Get auth token
            final String authToken = getAndValidateAuthToken();

            // Build request
            final Request request = new Request.Builder()
                .url(getApiUrl() + Constants.Endpoints.USER_SESSIONS_ENDPOINT + Constants.Http.QUERY_START
                    + Constants.JsonFields.ID_FIELD + Constants.Http.QUERY_EQUALS + sessionId)
                .addHeader(Constants.Http.API_KEY_HEADER, getApiKey())
                .addHeader(Constants.Http.AUTH_HEADER, authToken)
                .get()
                .build();

            // Execute request
            try (Response response = getClient().newCall(request).execute()) {
                checkResponse(response);

                // Parse response and return the session
                final String responseBody = response.body().string();
                final UserSession[] sessions = objectMapper.readValue(responseBody, UserSession[].class);

                if (sessions.length == 0) {
                    throw new DatabaseAccessException("Session not found: " + sessionId);
                }

                return sessions[0];
            }
        }
        catch (IOException exception) {
            throw new DatabaseAccessException("Failed to get session: " + exception.getMessage());
        }
    }

    @Override
    public UserSession updateInterruptionCount(UserSession session) throws DatabaseAccessException {
        try {
            // Get auth token
            final String authToken = getAndValidateAuthToken();

            // Create minimal JSON with only interruption count
            final ObjectNode jsonNode = objectMapper.createObjectNode()
                .put(Constants.JsonFields.INTERRUPTION_COUNT, session.getInterruptionCount());

            final RequestBody body = RequestBody.create(jsonNode.toString(), JSON);

            // Build request
            final Request request = new Request.Builder()
                .url(getApiUrl() + Constants.Endpoints.USER_SESSIONS_ENDPOINT + Constants.Http.QUERY_START
                    + Constants.JsonFields.ID_FIELD + Constants.Http.QUERY_EQUALS + session.getId())
                .addHeader(Constants.Http.API_KEY_HEADER, getApiKey())
                .addHeader(Constants.Http.CONTENT_TYPE_HEADER, Constants.Http.CONTENT_TYPE_JSON)
                .addHeader(Constants.Http.PREFER_HEADER, Constants.Http.PREFER_REPRESENTATION)
                .addHeader(Constants.Http.AUTH_HEADER, authToken)
                .patch(body)
                .build();

            // Execute request
            try (Response response = getClient().newCall(request).execute()) {
                checkResponse(response);

                // Parse response and return the updated session
                final String responseBody = response.body().string();
                final UserSession[] sessions = objectMapper.readValue(responseBody, UserSession[].class);
                return sessions[0];
            }
        }
        catch (IOException exception) {
            throw new DatabaseAccessException("Failed to update session interruption count: " + exception.getMessage());
        }
    }

    @Override
    public UserSession updateCompletion(UserSession session) throws DatabaseAccessException {
        try {
            // Get auth token
            final String authToken = getAndValidateAuthToken();

            // Create minimal JSON with completion status and end time
            final ObjectNode jsonNode = objectMapper.createObjectNode()
                .put(Constants.JsonFields.WAS_COMPLETED, session.isCompleted())
                .put(Constants.JsonFields.SESSION_END_TIME, session.getSessionEndTime().format(ISO_FORMATTER));

            final RequestBody body = RequestBody.create(jsonNode.toString(), JSON);

            // Build request
            final Request request = new Request.Builder()
                .url(getApiUrl() + Constants.Endpoints.USER_SESSIONS_ENDPOINT + Constants.Http.QUERY_START
                    + Constants.JsonFields.ID_FIELD + Constants.Http.QUERY_EQUALS + session.getId())
                .addHeader(Constants.Http.API_KEY_HEADER, getApiKey())
                .addHeader(Constants.Http.CONTENT_TYPE_HEADER, Constants.Http.CONTENT_TYPE_JSON)
                .addHeader(Constants.Http.PREFER_HEADER, Constants.Http.PREFER_REPRESENTATION)
                .addHeader(Constants.Http.AUTH_HEADER, authToken)
                .patch(body)
                .build();

            // Execute request
            try (Response response = getClient().newCall(request).execute()) {
                checkResponse(response);

                // Parse response and return the updated session
                final String responseBody = response.body().string();
                final UserSession[] sessions = objectMapper.readValue(responseBody, UserSession[].class);
                return sessions[0];
            }
        }
        catch (IOException exception) {
            throw new DatabaseAccessException("Failed to update session completion: " + exception.getMessage());
        }
    }

    @Override
    public UserSession create(UserSession userSession) throws DatabaseAccessException {
        try {
            // Get auth token and user ID
            final String authToken = getAndValidateAuthToken();
            final UUID userId = getUserIdFromToken(authToken);

            // Create minimal JSON with only required fields
            final ObjectNode jsonNode = objectMapper.createObjectNode()
                .put(Constants.JsonFields.USER_ID, userId.toString())
                .put(Constants.JsonFields.SESSION_TYPE, userSession.getSessionType().getValue())
                .put(Constants.JsonFields.DURATION_MINUTES, userSession.getDurationMinutes())
                .put(Constants.JsonFields.SESSION_START_TIME,
                    userSession.getSessionStartTime().format(ISO_FORMATTER))
                .put(Constants.JsonFields.SESSION_END_TIME, userSession.getSessionEndTime().format(ISO_FORMATTER));

            final RequestBody body = RequestBody.create(jsonNode.toString(), JSON);

            // Build request
            final Request request = new Request.Builder()
                .url(getApiUrl() + Constants.Endpoints.USER_SESSIONS_ENDPOINT)
                .addHeader(Constants.Http.API_KEY_HEADER, getApiKey())
                .addHeader(Constants.Http.CONTENT_TYPE_HEADER, Constants.Http.CONTENT_TYPE_JSON)
                .addHeader(Constants.Http.PREFER_HEADER, Constants.Http.PREFER_REPRESENTATION)
                .addHeader(Constants.Http.AUTH_HEADER, authToken)
                .post(body)
                .build();

            // Execute request
            try (Response response = getClient().newCall(request).execute()) {
                checkResponse(response);

                // Parse response and return the created session
                final String responseBody = response.body().string();
                final UserSession[] sessions = objectMapper.readValue(responseBody, UserSession[].class);
                return sessions[0];
            }
        }
        catch (IOException exception) {
            throw new DatabaseAccessException(
                String.format(Constants.ErrorMessages.STATS_CREATE_FAILED, exception.getMessage()));
        }
    }

    @Override
    public UserSession updateCancellation(UserSession session) throws DatabaseAccessException {
        try {
            // Get auth token
            final String authToken = getAndValidateAuthToken();

            // Create minimal JSON with completion status and end time
            final ObjectNode jsonNode = objectMapper.createObjectNode()
                .put(Constants.JsonFields.WAS_COMPLETED, session.isCompleted())
                .put(Constants.JsonFields.SESSION_END_TIME, session.getSessionEndTime().format(ISO_FORMATTER));

            final RequestBody body = RequestBody.create(jsonNode.toString(), JSON);

            // Build request
            final Request request = new Request.Builder()
                .url(getApiUrl() + Constants.Endpoints.USER_SESSIONS_ENDPOINT + Constants.Http.QUERY_START
                    + Constants.JsonFields.ID_FIELD + Constants.Http.QUERY_EQUALS + session.getId())
                .addHeader(Constants.Http.API_KEY_HEADER, getApiKey())
                .addHeader(Constants.Http.CONTENT_TYPE_HEADER, Constants.Http.CONTENT_TYPE_JSON)
                .addHeader(Constants.Http.PREFER_HEADER, Constants.Http.PREFER_REPRESENTATION)
                .addHeader(Constants.Http.AUTH_HEADER, authToken)
                .patch(body)
                .build();

            // Execute request
            try (Response response = getClient().newCall(request).execute()) {
                checkResponse(response);

                // Parse response and return the updated session
                final String responseBody = response.body().string();
                final UserSession[] sessions = objectMapper.readValue(responseBody, UserSession[].class);
                return sessions[0];
            }
        }
        catch (IOException exception) {
            throw new DatabaseAccessException("Failed to update session cancellation: " + exception.getMessage());
        }
    }

    /**
     * Checks the response status and throws appropriate exceptions.
     * @param response The HTTP response to check
     * @throws DatabaseAccessException if there's a general database error
     * @throws AuthenticationException if the user is not authenticated (401)
     * @throws ForbiddenAccessException if the user is not authorized (403)
     * @throws IOException if there's an error reading the response
     */
    private void checkResponse(Response response) throws DatabaseAccessException, IOException {
        if (!response.isSuccessful()) {
            final String errorMessage = getErrorMessage(response);

            final int statusCode = response.code();

            if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                throw new AuthenticationException(Constants.ErrorMessages.AUTH_TOKEN_INVALID);
            }
            else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                throw new ForbiddenAccessException(Constants.ErrorMessages.UNAUTHORIZED_ACCESS);
            }
            else {
                throw new DatabaseAccessException(
                    String.format(Constants.ErrorMessages.STATS_CREATE_FAILED, errorMessage));
            }
        }

        if (response.body() == null) {
            throw new DatabaseAccessException(
                String.format(Constants.ErrorMessages.STATS_CREATE_FAILED, "Empty response body"));
        }
    }

    /**
     * Gets the error message from a response.
     * @param response The HTTP response
     * @return The error message from the response body or message
     * @throws IOException if there's an error reading the response body
     */
    private String getErrorMessage(Response response) throws IOException {
        final ResponseBody responseBody = response.body();
        final String errorMessage;
        if (responseBody != null) {
            errorMessage = responseBody.string();
        }
        else {
            errorMessage = response.message();
        }
        return errorMessage;
    }
}
