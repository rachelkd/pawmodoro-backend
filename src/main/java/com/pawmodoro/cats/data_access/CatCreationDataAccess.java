package com.pawmodoro.cats.data_access;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.entity.CatAlreadyExistsException;
import com.pawmodoro.cats.entity.CatFactory;
import com.pawmodoro.cats.service.create_cat.CreateCatDataAccessInterface;
import com.pawmodoro.constants.Constants;
import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.core.ForbiddenAccessException;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Repository implementation for creating cats in the database.
 * @see CreateCatDataAccessInterface
 */
@Repository
public class CatCreationDataAccess extends AbstractCatDataAccess implements CreateCatDataAccessInterface {
    private final CatFactory catFactory;

    public CatCreationDataAccess(
        CatFactory catFactory,
        @Value("${supabase.url}") String apiUrl,
        @Value("${supabase.key}") String apiKey) {
        super(apiUrl, apiKey);
        this.catFactory = catFactory;
    }

    @Override
    public boolean catExistsByNameAndOwnerUsername(String catName,
        String ownerUsername) throws DatabaseAccessException {
        final String authToken = getAndValidateAuthToken();

        // Build the request URL with query parameters to check for existence
        final String queryUrl = getApiUrl() + Constants.Endpoints.CATS_ENDPOINT
            + Constants.Http.QUERY_START + Constants.Http.SELECT_PARAM + Constants.JsonFields.ID_FIELD
            + Constants.Http.AND_OPERATOR + Constants.JsonFields.CAT_NAME + Constants.Http.QUERY_EQUALS + catName
            + Constants.Http.AND_OPERATOR + Constants.JsonFields.OWNER_USERNAME + Constants.Http.QUERY_EQUALS
            + ownerUsername
            + Constants.Http.AND_OPERATOR + Constants.Http.LIMIT_PARAM + "1";

        final Request supabaseRequest = new Request.Builder()
            .url(queryUrl)
            .get()
            .addHeader(Constants.Http.API_KEY_HEADER, getApiKey())
            .addHeader(Constants.Http.AUTH_HEADER, authToken)
            .build();

        try {
            final Response response = getClient().newCall(supabaseRequest).execute();
            final String responseBody = response.body().string();

            if (!response.isSuccessful()) {
                if (response.code() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new AuthenticationException(Constants.ErrorMessages.AUTH_TOKEN_INVALID);
                }
                throw new DatabaseAccessException(
                    String.format(Constants.ErrorMessages.DB_FAILED_CHECK_CAT_EXISTS, response.message()));
            }

            // If the response is an empty array, the cat doesn't exist
            // If it's not empty, the cat exists
            final JSONArray jsonArray = new JSONArray(responseBody);
            return jsonArray.length() > 0;
        }
        catch (final IOException exception) {
            throw new DatabaseAccessException(
                String.format(Constants.ErrorMessages.DB_FAILED_CHECK_CAT_EXISTS, exception.getMessage()));
        }
    }

    @Override
    public Cat saveCat(Cat cat) throws DatabaseAccessException, CatAlreadyExistsException {
        final String authToken = getAndValidateAuthToken();

        // Create JSON request body
        final JSONObject requestBody = new JSONObject();
        requestBody.put(Constants.JsonFields.CAT_NAME, cat.getName());
        requestBody.put(Constants.JsonFields.OWNER_USERNAME, cat.getOwnerUsername());
        requestBody.put(Constants.JsonFields.HAPPINESS_LEVEL, cat.getHappinessLevel());
        requestBody.put(Constants.JsonFields.HUNGER_LEVEL, cat.getHungerLevel());
        requestBody.put(Constants.JsonFields.IMAGE_FILE_NAME, cat.getImageFileName());

        // Create the request with proper headers
        final Request supabaseRequest = new Request.Builder()
            .url(getApiUrl() + Constants.Endpoints.CATS_ENDPOINT)
            .post(RequestBody.create(requestBody.toString(), JSON))
            .addHeader(Constants.Http.CONTENT_TYPE_HEADER, Constants.Http.CONTENT_TYPE_JSON)
            .addHeader(Constants.Http.API_KEY_HEADER, getApiKey())
            .addHeader(Constants.Http.AUTH_HEADER, authToken)
            .addHeader(Constants.Http.PREFER_HEADER, Constants.Http.PREFER_REPRESENTATION)
            .build();

        try {
            final Response response = getClient().newCall(supabaseRequest).execute();
            final String responseBody = response.body().string();

            if (!response.isSuccessful()) {
                handleCreateError(response, responseBody, cat);
            }

            // Parse the response and create a new Cat entity
            final JSONArray jsonArray = new JSONArray(responseBody);
            if (jsonArray.length() == 0) {
                throw new DatabaseAccessException(
                    String.format(Constants.ErrorMessages.CAT_CREATE_FAILED, "No data returned"));
            }

            final JSONObject catJson = jsonArray.getJSONObject(0);
            return catFactory.create(
                catJson.getString(Constants.JsonFields.CAT_NAME),
                catJson.getString(Constants.JsonFields.OWNER_USERNAME),
                catJson.getInt(Constants.JsonFields.HAPPINESS_LEVEL),
                catJson.getInt(Constants.JsonFields.HUNGER_LEVEL),
                catJson.getString(Constants.JsonFields.IMAGE_FILE_NAME));
        }
        catch (final IOException exception) {
            throw new DatabaseAccessException(
                String.format(Constants.ErrorMessages.DB_FAILED_SAVE_CAT, exception.getMessage()));
        }
    }

    private void handleCreateError(Response response, String responseBody,
        Cat cat) throws DatabaseAccessException, CatAlreadyExistsException {
        if (response.code() == HttpStatus.UNAUTHORIZED.value()) {
            throw new AuthenticationException(Constants.ErrorMessages.AUTH_TOKEN_INVALID);
        }
        else if (response.code() == HttpStatus.FORBIDDEN.value()) {
            throw new ForbiddenAccessException(
                String.format(Constants.ErrorMessages.CAT_CREATE_UNAUTHORIZED, cat.getOwnerUsername()));
        }

        // Parse the error message from the response body
        final JSONObject errorJson = new JSONObject(responseBody);
        final String errorMessage = errorJson.getString("message");

        if (response.code() == HttpStatus.CONFLICT.value()) {
            throw new CatAlreadyExistsException(Constants.ErrorMessages.DB_DUPLICATE_CAT_NAME);
        }

        throw new DatabaseAccessException(
            String.format(Constants.ErrorMessages.DB_FAILED_SAVE_CAT, errorMessage));
    }
}
