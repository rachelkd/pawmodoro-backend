package com.pawmodoro.cats.data_access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.entity.CatFactory;
import com.pawmodoro.cats.service.get_all_cats.GetAllCatsDataAccessInterface;
import com.pawmodoro.constants.Constants;
import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Repository implementation for retrieving cats from the database.
 * @see GetAllCatsDataAccessInterface
 */
@Repository
public class CatRetrievalDataAccess extends AbstractCatDataAccess implements GetAllCatsDataAccessInterface {
    private final CatFactory catFactory;

    public CatRetrievalDataAccess(
        CatFactory catFactory,
        @Value("${supabase.url}") String apiUrl,
        @Value("${supabase.key}") String apiKey) {
        super(apiUrl, apiKey);
        this.catFactory = catFactory;
    }

    @Override
    public Collection<Cat> getCatsByOwner(String ownerUsername) throws DatabaseAccessException {
        final String authToken = getAndValidateAuthToken();
        final Collection<Cat> cats = new ArrayList<>();

        final Request supabaseRequest = new Request.Builder()
            .url(getApiUrl() + Constants.Endpoints.CATS_ENDPOINT + Constants.Http.QUERY_START
                + Constants.JsonFields.OWNER_USERNAME + Constants.Http.QUERY_EQUALS + ownerUsername)
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
}
