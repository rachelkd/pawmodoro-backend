package com.pawmodoro.cats.data_access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.entity.CatAlreadyExistsException;
import com.pawmodoro.cats.entity.CatFactory;
import com.pawmodoro.cats.entity.NoCatsFoundException;
import com.pawmodoro.cats.service.create_cat.CreateCatDataAccessInterface;
import com.pawmodoro.cats.service.delete_cat.DeleteCatDataAccessInterface;
import com.pawmodoro.cats.service.get_all_cats.GetAllCatsDataAccessInterface;
import com.pawmodoro.constants.Constants;
import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.core.ForbiddenAccessException;
import jakarta.servlet.http.HttpServletRequest;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Database implementation of cat-related data access interface using Supabase.
 * This class handles all cat-related database operations including retrieving cats by owner.
 */
@Repository
public class DbCatDataAccessObject implements GetAllCatsDataAccessInterface, CreateCatDataAccessInterface,
    DeleteCatDataAccessInterface {
    private static final MediaType JSON = MediaType.get(Constants.Http.CONTENT_TYPE_JSON);
    private final OkHttpClient client;
    private final String apiUrl;
    private final String apiKey;
    private final CatFactory catFactory;

    /**
     * Creates a new DbCatDataAccessObject.
     * @param catFactory the factory to create Cat entities
     * @param apiUrl the Supabase API URL
     * @param apiKey the Supabase API key
     */
    public DbCatDataAccessObject(
        CatFactory catFactory,
        @Value("${supabase.url}") String apiUrl,
        @Value("${supabase.key}") String apiKey) {
        this.catFactory = catFactory;
        this.client = new OkHttpClient().newBuilder().build();
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    private String getAndValidateAuthToken() throws AuthenticationException {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
            .getRequest();
        final String authToken = request.getHeader(Constants.Http.AUTH_HEADER);
        if (authToken == null || !authToken.startsWith(Constants.Http.BEARER_PREFIX)) {
            throw new AuthenticationException(Constants.ErrorMessages.AUTH_TOKEN_REQUIRED);
        }
        return authToken;
    }

    @Override
    public Collection<Cat> getCatsByOwner(String ownerUsername) throws DatabaseAccessException {
        // Get the authorization token from the current request
        final String authToken = getAndValidateAuthToken();

        final Collection<Cat> cats = new ArrayList<>();
        final Request supabaseRequest = new Request.Builder()
            .url(apiUrl + Constants.Endpoints.CATS_ENDPOINT + Constants.Http.QUERY_START
                + Constants.JsonFields.OWNER_USERNAME + Constants.Http.QUERY_EQUALS + ownerUsername)
            .get()
            .addHeader(Constants.Http.API_KEY_HEADER, apiKey)
            .addHeader(Constants.Http.AUTH_HEADER, authToken)
            .build();

        try {
            final Response response = client.newCall(supabaseRequest).execute();
            final String responseBody = response.body().string();

            if (!response.isSuccessful()) {
                if (response.code() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new AuthenticationException(Constants.ErrorMessages.AUTH_TOKEN_INVALID);
                }
                throw new DatabaseAccessException(
                    String.format(Constants.ErrorMessages.DB_FAILED_RETRIEVE_CATS, response.message()));
            }

            if (!responseBody.equals(Constants.JsonFields.EMPTY_ARRAY)) {
                final JSONArray jsonArray = new JSONArray(responseBody);
                for (int i = 0; i < jsonArray.length(); i++) {
                    final JSONObject catJson = jsonArray.getJSONObject(i);
                    final Cat cat = catFactory.create(
                        catJson.getString(Constants.JsonFields.CAT_NAME),
                        catJson.getString(Constants.JsonFields.OWNER_USERNAME),
                        catJson.getInt(Constants.JsonFields.HAPPINESS_LEVEL),
                        catJson.getInt(Constants.JsonFields.HUNGER_LEVEL),
                        catJson.getString(Constants.JsonFields.IMAGE_FILE_NAME));
                    cats.add(cat);
                }
            }
        }
        catch (final IOException exception) {
            throw new DatabaseAccessException(
                String.format(Constants.ErrorMessages.DB_FAILED_RETRIEVE_CATS, exception.getMessage()));
        }
        return cats;
    }

    @Override
    public Cat saveCat(Cat cat) throws DatabaseAccessException, CatAlreadyExistsException {
        // Get the authorization token from the current request
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
            .url(apiUrl + Constants.Endpoints.CATS_ENDPOINT)
            .post(RequestBody.create(requestBody.toString(), JSON))
            .addHeader(Constants.Http.CONTENT_TYPE_HEADER, Constants.Http.CONTENT_TYPE_JSON)
            .addHeader(Constants.Http.API_KEY_HEADER, apiKey)
            .addHeader(Constants.Http.AUTH_HEADER, authToken)
            .addHeader(Constants.Http.PREFER_HEADER, Constants.Http.PREFER_REPRESENTATION)
            .build();

        try {
            final Response response = client.newCall(supabaseRequest).execute();
            final String responseBody = response.body().string();

            if (!response.isSuccessful()) {
                if (response.code() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new AuthenticationException(Constants.ErrorMessages.AUTH_TOKEN_INVALID);
                }
                else if (response.code() == HttpStatus.FORBIDDEN.value()) {
                    throw new ForbiddenAccessException(
                        "You are not authorized to create a cat for user: " + cat.getOwnerUsername());
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

            // Parse the response and create a new Cat entity
            final JSONArray jsonArray = new JSONArray(responseBody);
            if (jsonArray.length() == 0) {
                throw new DatabaseAccessException("Failed to create cat: No data returned");
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

    @Override
    public void deleteCat(String catName, String ownerUsername) throws DatabaseAccessException {
        // Get the authorization token from the current request
        final String authToken = getAndValidateAuthToken();

        // Build the request URL with query parameters to delete the specific cat
        final String queryUrl = apiUrl + Constants.Endpoints.CATS_ENDPOINT
            + Constants.Http.QUERY_START
            + Constants.JsonFields.CAT_NAME + Constants.Http.QUERY_EQUALS + catName
            + Constants.Http.AND_OPERATOR
            + Constants.JsonFields.OWNER_USERNAME + Constants.Http.QUERY_EQUALS + ownerUsername;

        // Create the request with proper headers
        final Request supabaseRequest = new Request.Builder()
            .url(queryUrl)
            .delete()
            .addHeader(Constants.Http.API_KEY_HEADER, apiKey)
            .addHeader(Constants.Http.AUTH_HEADER, authToken)
            .addHeader(Constants.Http.PREFER_HEADER, Constants.Http.PREFER_MINIMAL)
            .build();

        try {
            final Response response = client.newCall(supabaseRequest).execute();

            if (!response.isSuccessful()) {
                final String responseBody = response.body().string();

                if (response.code() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new AuthenticationException(Constants.ErrorMessages.AUTH_TOKEN_INVALID);
                }
                else if (response.code() == HttpStatus.FORBIDDEN.value()) {
                    throw new ForbiddenAccessException("You are not authorized to delete this cat");
                }
                else if (response.code() == HttpStatus.NOT_FOUND.value()) {
                    throw new NoCatsFoundException(
                        String.format("Cat not found with the name %s for user %s", catName, ownerUsername));
                }

                throw new DatabaseAccessException(
                    String.format("Failed to delete cat: %s", responseBody));
            }

        }
        catch (final IOException exception) {
            throw new DatabaseAccessException(
                String.format("Failed to delete cat: %s", exception.getMessage()));
        }
    }

    @Override
    public boolean catExistsByNameAndOwnerUsername(String catName,
        String ownerUsername) throws DatabaseAccessException {
        // Get the authorization token from the current request
        final String authToken = getAndValidateAuthToken();

        // Build the request URL with query parameters to check for existence
        final String queryUrl = apiUrl + Constants.Endpoints.CATS_ENDPOINT
            + Constants.Http.QUERY_START + Constants.Http.SELECT_PARAM + Constants.JsonFields.ID_FIELD
            + Constants.Http.AND_OPERATOR + Constants.JsonFields.CAT_NAME + Constants.Http.QUERY_EQUALS + catName
            + Constants.Http.AND_OPERATOR + Constants.JsonFields.OWNER_USERNAME + Constants.Http.QUERY_EQUALS
            + ownerUsername
            + Constants.Http.AND_OPERATOR + Constants.Http.LIMIT_PARAM + "1";

        final Request supabaseRequest = new Request.Builder()
            .url(queryUrl)
            .get()
            .addHeader(Constants.Http.API_KEY_HEADER, apiKey)
            .addHeader(Constants.Http.AUTH_HEADER, authToken)
            .build();

        try {
            final Response response = client.newCall(supabaseRequest).execute();
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

}
