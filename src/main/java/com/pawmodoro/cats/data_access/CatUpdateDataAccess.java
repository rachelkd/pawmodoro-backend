package com.pawmodoro.cats.data_access;

import java.io.IOException;
import java.util.function.ToIntFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.entity.CatFactory;
import com.pawmodoro.cats.entity.NoCatsFoundException;
import com.pawmodoro.cats.service.update_cat_happiness.UpdateCatHappinessDataAccessInterface;
import com.pawmodoro.cats.service.update_cat_hunger.UpdateCatHungerDataAccessInterface;
import com.pawmodoro.constants.Constants;
import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.core.ForbiddenAccessException;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Data access class for updating cat attributes.
 */
@Repository
public class CatUpdateDataAccess extends AbstractCatDataAccess
    implements UpdateCatHappinessDataAccessInterface, UpdateCatHungerDataAccessInterface {

    private final CatFactory catFactory;
    private final CatRetrievalDataAccess catRetrieval;

    public CatUpdateDataAccess(
        CatFactory catFactory,
        CatRetrievalDataAccess catRetrieval,
        @Value("${supabase.url}") String apiUrl,
        @Value("${supabase.key}") String apiKey) {
        super(apiUrl, apiKey);
        this.catFactory = catFactory;
        this.catRetrieval = catRetrieval;
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

        // Build request
        final Request supabaseRequest = new Request.Builder()
            .url(buildCatQueryUrl(catName, ownerUsername))
            .patch(RequestBody.create(requestBody.toString(), JSON))
            .addHeader(Constants.Http.CONTENT_TYPE_HEADER, Constants.Http.CONTENT_TYPE_JSON)
            .addHeader(Constants.Http.API_KEY_HEADER, getApiKey())
            .addHeader(Constants.Http.AUTH_HEADER, authToken)
            .addHeader(Constants.Http.PREFER_HEADER, Constants.Http.PREFER_REPRESENTATION)
            .build();

        return executeUpdateRequest(supabaseRequest, catName, ownerUsername);
    }

    private Cat executeUpdateRequest(Request request, String catName,
        String ownerUsername) throws DatabaseAccessException, NoCatsFoundException {
        try {
            final Response response = getClient().newCall(request).execute();
            final String responseBody = response.body().string();

            if (!response.isSuccessful()) {
                handleUpdateError(response, responseBody, catName, ownerUsername);
            }

            return parseUpdateResponse(responseBody);
        }
        catch (IOException exception) {
            throw new DatabaseAccessException(
                String.format(Constants.ErrorMessages.CAT_UPDATE_FAILED, exception.getMessage()));
        }
    }

    private void handleUpdateError(Response response, String responseBody, String catName,
        String ownerUsername) throws NoCatsFoundException, DatabaseAccessException {
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
        throw new DatabaseAccessException(String.format(Constants.ErrorMessages.CAT_UPDATE_FAILED, responseBody));
    }

    private Cat parseUpdateResponse(String responseBody) throws DatabaseAccessException {
        try {
            final JSONArray jsonArray = new JSONArray(responseBody);

            if (jsonArray.length() == 0) {
                throw new DatabaseAccessException("No data returned from update operation");
            }

            final JSONObject catJson = jsonArray.getJSONObject(0);
            return catFactory.create(
                catJson.getString(Constants.JsonFields.CAT_NAME),
                catJson.getString(Constants.JsonFields.OWNER_USERNAME),
                catJson.getInt(Constants.JsonFields.HAPPINESS_LEVEL),
                catJson.getInt(Constants.JsonFields.HUNGER_LEVEL),
                catJson.getString(Constants.JsonFields.IMAGE_FILE_NAME));
        }
        catch (JSONException exception) {
            throw new DatabaseAccessException(
                String.format(Constants.ErrorMessages.CAT_UPDATE_PARSE_ERROR, exception.getMessage()));
        }
    }
}
