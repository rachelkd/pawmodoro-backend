package com.pawmodoro.cats.data_access;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.pawmodoro.cats.entity.NoCatsFoundException;
import com.pawmodoro.cats.service.delete_cat.DeleteCatDataAccessInterface;
import com.pawmodoro.constants.Constants;
import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.core.ForbiddenAccessException;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Repository implementation for deleting cats from the database.
 * @see DeleteCatDataAccessInterface
 */
@Repository
public class CatDeletionDataAccess extends AbstractCatDataAccess implements DeleteCatDataAccessInterface {

    public CatDeletionDataAccess(
        @Value("${supabase.url}") String apiUrl,
        @Value("${supabase.key}") String apiKey) {
        super(apiUrl, apiKey);
    }

    @Override
    public void deleteCat(String catName, String ownerUsername) throws DatabaseAccessException {
        final String authToken = getAndValidateAuthToken();

        // Build the request URL with query parameters to delete the specific cat
        final String queryUrl = buildCatQueryUrl(catName, ownerUsername);

        // Create the request with proper headers
        final Request supabaseRequest = new Request.Builder()
            .url(queryUrl)
            .delete()
            .addHeader(Constants.Http.API_KEY_HEADER, getApiKey())
            .addHeader(Constants.Http.AUTH_HEADER, authToken)
            .addHeader(Constants.Http.PREFER_HEADER, Constants.Http.PREFER_MINIMAL)
            .build();

        try {
            final Response response = getClient().newCall(supabaseRequest).execute();

            if (!response.isSuccessful()) {
                final String responseBody = response.body().string();

                if (response.code() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new AuthenticationException(Constants.ErrorMessages.AUTH_TOKEN_INVALID);
                }
                else if (response.code() == HttpStatus.FORBIDDEN.value()) {
                    throw new ForbiddenAccessException(Constants.ErrorMessages.CAT_UPDATE_UNAUTHORIZED);
                }
                else if (response.code() == HttpStatus.NOT_FOUND.value()) {
                    throw new NoCatsFoundException(
                        String.format(Constants.ErrorMessages.CAT_NOT_FOUND, catName, ownerUsername));
                }

                throw new DatabaseAccessException(
                    String.format(Constants.ErrorMessages.CAT_UPDATE_FAILED, responseBody));
            }
        }
        catch (final IOException exception) {
            throw new DatabaseAccessException(
                String.format(Constants.ErrorMessages.CAT_UPDATE_FAILED, exception.getMessage()));
        }
    }
}
