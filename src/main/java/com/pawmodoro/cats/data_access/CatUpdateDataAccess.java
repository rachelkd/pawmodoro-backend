package com.pawmodoro.cats.data_access;

import java.io.IOException;
import java.util.Collection;
import java.util.function.ToIntFunction;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.entity.CatFactory;
import com.pawmodoro.cats.entity.NoCatsFoundException;
import com.pawmodoro.cats.service.decrease_cat_stats_on_skip.DecreaseCatStatsOnSkipDataAccessInterface;
import com.pawmodoro.cats.service.update_cat_happiness.UpdateCatHappinessDataAccessInterface;
import com.pawmodoro.cats.service.update_cat_hunger.UpdateCatHungerDataAccessInterface;
import com.pawmodoro.constants.Constants;
import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.core.ForbiddenAccessException;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Repository implementation for updating cats in the database.
 * @see UpdateCatHungerDataAccessInterface
 * @see UpdateCatHappinessDataAccessInterface
 * @see DecreaseCatStatsOnSkipDataAccessInterface
 */
@Repository
public class CatUpdateDataAccess extends AbstractCatDataAccess implements
    UpdateCatHungerDataAccessInterface,
    UpdateCatHappinessDataAccessInterface,
    DecreaseCatStatsOnSkipDataAccessInterface {

    private static final MediaType JSON = MediaType.parse(Constants.Http.CONTENT_TYPE_JSON);
    private final CatFactory catFactory;
    private final CatRetrievalDataAccess catRetrieval;
    private final CatDeletionDataAccess catDeletion;

    public CatUpdateDataAccess(
        CatFactory catFactory,
        CatRetrievalDataAccess catRetrieval,
        CatDeletionDataAccess catDeletion,
        @Value("${supabase.url}") String apiUrl,
        @Value("${supabase.key}") String apiKey) {
        super(apiUrl, apiKey);
        this.catFactory = catFactory;
        this.catRetrieval = catRetrieval;
        this.catDeletion = catDeletion;
    }

    @Override
    public String getUsernameFromToken(String token) throws DatabaseAccessException {
        // The token contains the user's ID, and Supabase RLS will automatically filter
        // to show only the current user's profile
        final Request request = new Request.Builder()
            .url(getApiUrl() + Constants.Endpoints.USER_PROFILES_ENDPOINT + Constants.Http.QUERY_START
                + Constants.Http.SELECT_PARAM + Constants.JsonFields.USERNAME_FIELD)
            .get()
            .addHeader(Constants.Http.API_KEY_HEADER, getApiKey())
            .addHeader(Constants.Http.AUTH_HEADER, Constants.Http.BEARER_PREFIX + token)
            .build();

        try {
            final Response response = getClient().newCall(request).execute();
            final String responseBody = response.body().string();

            if (!response.isSuccessful()) {
                if (response.code() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new AuthenticationException(Constants.ErrorMessages.AUTH_TOKEN_INVALID);
                }
                else {
                    throw new DatabaseAccessException(
                        String.format(Constants.ErrorMessages.DB_FAILED_ACCESS, responseBody));
                }
            }

            // Response should contain exactly one row due to RLS and database triggers
            final JSONArray profiles = new JSONArray(responseBody);
            assert profiles.length() == 1 : "Expected exactly one user profile due to RLS and database triggers";
            return profiles.getJSONObject(0).getString(Constants.JsonFields.USERNAME_FIELD);
        }
        catch (IOException exception) {
            throw new DatabaseAccessException(
                String.format(Constants.ErrorMessages.DB_FAILED_ACCESS, exception.getMessage()));
        }
    }

    @Override
    public Collection<Cat> getCatsByOwner(String ownerUsername) throws DatabaseAccessException {
        return catRetrieval.getCatsByOwner(ownerUsername);
    }

    @Override
    public void deleteCat(String catName, String ownerUsername) throws DatabaseAccessException {
        catDeletion.deleteCat(catName, ownerUsername);
    }

    @Override
    public Cat updateHunger(String catName, String ownerUsername,
        int changeAmount) throws DatabaseAccessException, NoCatsFoundException {
        return updateCatAttribute(
            catName,
            ownerUsername,
            Constants.JsonFields.HUNGER_LEVEL,
            changeAmount,
            Cat::getHungerLevel);
    }

    @Override
    public Cat updateHappiness(String catName, String ownerUsername,
        int changeAmount) throws DatabaseAccessException, NoCatsFoundException {
        return updateCatAttribute(
            catName,
            ownerUsername,
            Constants.JsonFields.HAPPINESS_LEVEL,
            changeAmount,
            Cat::getHappinessLevel);
    }

    private Cat updateCatAttribute(
        String catName,
        String ownerUsername,
        String attributeName,
        int changeAmount,
        ToIntFunction<Cat> getCurrentValue) throws DatabaseAccessException, NoCatsFoundException {

        final String authToken = getAndValidateAuthToken();

        // Get current cat
        final Cat currentCat = catRetrieval.getCatsByOwner(ownerUsername).stream()
            .filter(cat -> cat.getName().equals(catName))
            .findFirst()
            .orElseThrow(() -> {
                throw new NoCatsFoundException(
                    String.format(Constants.ErrorMessages.CAT_NOT_FOUND, catName, ownerUsername));
            });

        // Calculate new value
        final int newValue = Math.clamp((long) getCurrentValue.applyAsInt(currentCat) + changeAmount, 0, 100);

        // Create request body
        final JSONObject requestBody = new JSONObject();
        requestBody.put(attributeName, newValue);

        // Build the request URL with query parameters to update the specific cat
        final String queryUrl = buildCatQueryUrl(catName, ownerUsername);

        // Create the request with proper headers and body
        final Request supabaseRequest = new Request.Builder()
            .url(queryUrl)
            .patch(RequestBody.create(requestBody.toString(), JSON))
            .addHeader(Constants.Http.API_KEY_HEADER, getApiKey())
            .addHeader(Constants.Http.AUTH_HEADER, authToken)
            .addHeader(Constants.Http.PREFER_HEADER, Constants.Http.PREFER_REPRESENTATION)
            .build();

        try {
            final Response response = getClient().newCall(supabaseRequest).execute();
            final String responseBody = response.body().string();

            if (!response.isSuccessful()) {
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

            // Parse the response and create a new cat entity
            final JSONObject catJson = new JSONArray(responseBody).getJSONObject(0);
            return catFactory.create(
                catJson.getString(Constants.JsonFields.CAT_NAME),
                catJson.getString(Constants.JsonFields.OWNER_USERNAME),
                catJson.getInt(Constants.JsonFields.HAPPINESS_LEVEL),
                catJson.getInt(Constants.JsonFields.HUNGER_LEVEL),
                catJson.getString(Constants.JsonFields.IMAGE_FILE_NAME));
        }
        catch (final IOException exception) {
            throw new DatabaseAccessException(
                String.format(Constants.ErrorMessages.CAT_UPDATE_FAILED, exception.getMessage()));
        }
    }
}
