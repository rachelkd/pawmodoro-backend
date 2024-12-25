package com.pawmodoro.cats.data_access;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.pawmodoro.constants.Constants;
import com.pawmodoro.core.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * Abstract base class for cat-related data access operations.
 * Provides common functionality for authentication and HTTP client operations.
 */
public abstract class AbstractCatDataAccess {
    protected static final MediaType JSON = MediaType.get(Constants.Http.CONTENT_TYPE_JSON);
    private final OkHttpClient client;
    private final String apiUrl;
    private final String apiKey;

    protected AbstractCatDataAccess(@Value("${supabase.url}") String apiUrl, @Value("${supabase.key}") String apiKey) {
        this.client = new OkHttpClient().newBuilder().build();
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
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

    protected String getAndValidateAuthToken() throws AuthenticationException {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
            .getRequest();
        final String authToken = request.getHeader(Constants.Http.AUTH_HEADER);
        if (authToken == null || !authToken.startsWith(Constants.Http.BEARER_PREFIX)) {
            throw new AuthenticationException(Constants.ErrorMessages.AUTH_TOKEN_REQUIRED);
        }
        return authToken;
    }

    protected String buildCatQueryUrl(String catName, String ownerUsername) {
        return getApiUrl() + Constants.Endpoints.CATS_ENDPOINT
            + Constants.Http.QUERY_START
            + Constants.JsonFields.CAT_NAME + Constants.Http.QUERY_EQUALS + catName
            + Constants.Http.AND_OPERATOR
            + Constants.JsonFields.OWNER_USERNAME + Constants.Http.QUERY_EQUALS + ownerUsername;
    }
}
