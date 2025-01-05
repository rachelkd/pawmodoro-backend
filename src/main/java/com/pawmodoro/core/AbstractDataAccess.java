package com.pawmodoro.core;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawmodoro.constants.Constants;
import jakarta.servlet.http.HttpServletRequest;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Abstract base class for all data access operations.
 * Provides common functionality for Supabase authentication and HTTP client operations.
 */
public abstract class AbstractDataAccess {
    protected static final MediaType JSON = MediaType.get(Constants.Http.CONTENT_TYPE_JSON);
    private final OkHttpClient client;
    private final String apiUrl;
    private final String apiKey;
    private final ObjectMapper objectMapper;

    /**
     * Creates a new instance with a default ObjectMapper.
     * @param apiUrl The Supabase API URL
     * @param apiKey The Supabase API key
     */
    protected AbstractDataAccess(
        @Value("${supabase.url}") String apiUrl,
        @Value("${supabase.key}") String apiKey) {
        this(apiUrl, apiKey, new ObjectMapper());
    }

    /**
     * Creates a new instance with a custom ObjectMapper.
     * @param apiUrl The Supabase API URL
     * @param apiKey The Supabase API key
     * @param objectMapper The ObjectMapper to use
     */
    protected AbstractDataAccess(
        @Value("${supabase.url}") String apiUrl,
        @Value("${supabase.key}") String apiKey,
        ObjectMapper objectMapper) {
        this.client = new OkHttpClient().newBuilder().build();
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;
    }

    protected OkHttpClient getClient() {
        return client;
    }

    protected String getApiUrl() {
        return apiUrl;
    }

    protected String getApiKey() {
        return apiKey;
    }

    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Gets and validates the authorization token from the request header.
     * This token will be used by Supabase to enforce Row Level Security policies.
     * @return The authorization token if valid
     * @throws AuthenticationException if the token is missing or invalid
     */
    protected String getAndValidateAuthToken() throws AuthenticationException {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
            .getRequest();
        final String authToken = request.getHeader(Constants.Http.AUTH_HEADER);
        if (authToken == null || !authToken.startsWith(Constants.Http.BEARER_PREFIX)) {
            throw new AuthenticationException(Constants.ErrorMessages.AUTH_TOKEN_REQUIRED);
        }
        return authToken;
    }

    /**
     * Gets the user ID from a Supabase JWT token by calling the auth API.
     * @param authToken The JWT token to get the user ID from
     * @return The user ID from the token
     * @throws DatabaseAccessException if there's an error getting the user ID
     */
    protected UUID getUserIdFromToken(String authToken) throws DatabaseAccessException {
        try {
            final Request request = new Request.Builder()
                .url(apiUrl + Constants.Endpoints.AUTH_USERS_ENDPOINT)
                .addHeader(Constants.Http.API_KEY_HEADER, apiKey)
                .addHeader(Constants.Http.AUTH_HEADER, authToken)
                .get()
                .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful() || response.body() == null) {
                    throw new DatabaseAccessException(Constants.ErrorMessages.AUTH_USER_NOT_AUTHENTICATED);
                }

                final JsonNode userInfo = objectMapper.readTree(response.body().string());
                return UUID.fromString(userInfo.get("id").asText());
            }
        }
        catch (IOException | IllegalArgumentException exception) {
            throw new DatabaseAccessException(Constants.ErrorMessages.AUTH_USER_NOT_AUTHENTICATED);
        }
    }
}
