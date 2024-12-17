package com.pawmodoro.cats.data_access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.pawmodoro.cats.entity.Cat;
import com.pawmodoro.cats.entity.CatFactory;
import com.pawmodoro.cats.entity.NoCatsFoundException;
import com.pawmodoro.cats.service.get_all_cats.GetAllCatsDataAccessInterface;
import com.pawmodoro.core.DatabaseAccessException;

import jakarta.servlet.http.HttpServletRequest;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Database implementation of CatDataAccessInterface using Supabase.
 */
@Repository
public class DbCatDataAccessObject implements GetAllCatsDataAccessInterface {
    private static final String API_KEY_HEADER = "apikey";
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String PREFER_HEADER = "Prefer";
    private static final String PREFER_RETURN_MINIMAL = "return=minimal";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String EMPTY_JSON_ARRAY = "[]";

    private static final String CATS_ENDPOINT = "/rest/v1/cats";
    private static final String NAME_QUERY = "?cat_name=eq.";
    private static final String OWNER_QUERY = "&owner_username=eq.";

    private static final String NAME_COLUMN = "cat_name";
    private static final String OWNER_USERNAME_COLUMN = "owner_username";
    private static final String HAPPINESS_LEVEL_COLUMN = "happiness_level";
    private static final String HUNGER_LEVEL_COLUMN = "hunger_level";
    private static final String IMAGE_FILE_NAME_COLUMN = "image_file_name";

    private final OkHttpClient client = new OkHttpClient().newBuilder().build();
    private final String apiUrl;
    private final String apiKey;
    private final CatFactory catFactory;

    /**
     * Creates a new DbCatDataAccessObject.
     * @param catFactory the factory to create Cat entities
     */
    public DbCatDataAccessObject(
        CatFactory catFactory,
        @Value("${supabase.url}")
        String apiUrl,
        @Value("${supabase.key}")
        String apiKey) {
        this.catFactory = catFactory;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    @Override
    public Collection<Cat> getCatsByOwner(String ownerUsername) throws DatabaseAccessException {
        // Get the authorization token from the current request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
            .getRequest();
        String authToken = request.getHeader(AUTH_HEADER);

        if (authToken == null || !authToken.startsWith(BEARER_PREFIX)) {
            throw new DatabaseAccessException("Authorization token is required");
        }

        final Collection<Cat> cats = new ArrayList<>();
        final Request supabaseRequest = new Request.Builder()
            .url(apiUrl + CATS_ENDPOINT + "?owner_username=eq." + ownerUsername)
            .get()
            .addHeader(API_KEY_HEADER, apiKey)
            .addHeader(AUTH_HEADER, authToken)
            .build();

        try {
            final Response response = client.newCall(supabaseRequest).execute();
            final String responseBody = response.body().string();

            if (response.isSuccessful() && !responseBody.equals(EMPTY_JSON_ARRAY)) {
                final JSONArray jsonArray = new JSONArray(responseBody);
                for (int i = 0; i < jsonArray.length(); i++) {
                    final JSONObject catJson = jsonArray.getJSONObject(i);
                    final Cat cat = catFactory.create(
                        catJson.getString(NAME_COLUMN),
                        catJson.getString(OWNER_USERNAME_COLUMN),
                        catJson.getInt(HAPPINESS_LEVEL_COLUMN),
                        catJson.getInt(HUNGER_LEVEL_COLUMN),
                        catJson.getString(IMAGE_FILE_NAME_COLUMN));
                    cats.add(cat);
                }
            }
        }
        catch (final IOException exception) {
            throw new DatabaseAccessException("Failed to retrieve cats: " + exception.getMessage());
        }
        return cats;
    }
}
