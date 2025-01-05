package com.pawmodoro.cats.data_access;

import org.springframework.beans.factory.annotation.Value;

import com.pawmodoro.constants.Constants;
import com.pawmodoro.core.AbstractDataAccess;

/**
 * Abstract base class for cat-related data access operations.
 * Provides common functionality for cat-specific operations.
 */
public abstract class AbstractCatDataAccess extends AbstractDataAccess {

    protected AbstractCatDataAccess(@Value("${supabase.url}") String apiUrl, @Value("${supabase.key}") String apiKey) {
        super(apiUrl, apiKey);
    }

    protected String buildCatQueryUrl(String catName, String ownerUsername) {
        return getApiUrl() + Constants.Endpoints.CATS_ENDPOINT
            + Constants.Http.QUERY_START
            + Constants.JsonFields.CAT_NAME + Constants.Http.QUERY_EQUALS + catName
            + Constants.Http.AND_OPERATOR
            + Constants.JsonFields.OWNER_USERNAME + Constants.Http.QUERY_EQUALS + ownerUsername;
    }
}
