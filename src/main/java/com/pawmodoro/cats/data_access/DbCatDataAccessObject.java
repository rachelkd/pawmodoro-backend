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
import com.pawmodoro.cats.entity.CatFactory;
import com.pawmodoro.cats.service.create_cat.CreateCatDataAccessInterface;
import com.pawmodoro.cats.service.get_all_cats.GetAllCatsDataAccessInterface;
import com.pawmodoro.constants.Constants;
import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;
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
public class DbCatDataAccessObject implements GetAllCatsDataAccessInterface, CreateCatDataAccessInterface {
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
    public Cat saveCat(Cat cat) throws DatabaseAccessException {
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
                throw new DatabaseAccessException(
                    String.format(Constants.ErrorMessages.DB_FAILED_RETRIEVE_CATS, response.message()));
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
                String.format(Constants.ErrorMessages.DB_FAILED_RETRIEVE_CATS, exception.getMessage()));
        }
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
}
