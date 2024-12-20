package com.pawmodoro.settings.data_access;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.settings.entity.UserSettings;
import com.pawmodoro.settings.service.get_user_settings.GetUserSettingsDataAccessInterface;
import com.pawmodoro.settings.service.update_user_settings.UpdateUserSettingsDataAccessInterface;
import com.pawmodoro.users.entity.UserNotFoundException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Data access object for user settings in Supabase database.
 * This class handles the persistence of user settings while respecting
 * Row Level Security (RLS) policies configured in Supabase.
 */
@Repository
public class DbUserSettingsDataAccessObject
    implements GetUserSettingsDataAccessInterface, UpdateUserSettingsDataAccessInterface {

    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String API_KEY_HEADER = "apikey";
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String USER_SETTINGS_ENDPOINT = "/rest/v1/user_settings";
    private static final String USER_PROFILES_ENDPOINT = "/rest/v1/user_profiles";
    private static final String PREFER_HEADER = "Prefer";
    private static final String PREFER_RETURN = "return=representation";
    private static final String EMPTY_ARRAY = "[]";
    private static final String DATABASE_ACCESS_ERROR = "Failed to access database: ";
    private static final int HTTP_UNAUTHORIZED = 401;

    // Default settings values
    private static final boolean DEFAULT_AUTO_START_BREAKS = false;
    private static final boolean DEFAULT_AUTO_START_FOCUS = false;

    private final OkHttpClient client;
    private final String apiUrl;
    private final String apiKey;

    /**
     * Constructs a new DbUserSettingsDataAccessObject with the necessary Supabase configuration.
     * @param apiUrl the Supabase API URL
     * @param apiKey the Supabase API key
     */
    public DbUserSettingsDataAccessObject(
        @Value("${supabase.url}")
        String apiUrl,
        @Value("${supabase.key}")
        String apiKey) {
        this.client = new OkHttpClient().newBuilder().build();
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    /**
     * Gets the user ID from the user_profiles table.
     * @param username the username to look up
     * @param accessToken the Supabase access token
     * @return the user's ID
     * @throws UserNotFoundException if the user is not found
     * @throws DatabaseAccessException if there's an error accessing the database
     */
    private String getUserId(String username,
        String accessToken) throws UserNotFoundException, DatabaseAccessException {
        final Request request = buildUserProfileRequest(username, accessToken);
        return executeUserProfileRequest(request, username);
    }

    private Request buildUserProfileRequest(String username, String accessToken) {
        return new Request.Builder()
            .url(apiUrl + USER_PROFILES_ENDPOINT + "?username=eq." + username)
            .get()
            .addHeader(API_KEY_HEADER, apiKey)
            .addHeader(AUTH_HEADER, BEARER_PREFIX + accessToken)
            .addHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
            .build();
    }

    private String executeUserProfileRequest(Request request,
        String username) throws UserNotFoundException, DatabaseAccessException {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new DatabaseAccessException("Failed to get user profile: " + response.body().string());
            }

            final String responseBody = response.body().string();
            if (responseBody.equals(EMPTY_ARRAY)) {
                throw new UserNotFoundException("User not found: " + username);
            }

            final JSONArray profiles = new JSONArray(responseBody);
            return profiles.getJSONObject(0).getString("id");
        }
        catch (IOException exception) {
            throw new DatabaseAccessException(DATABASE_ACCESS_ERROR + exception.getMessage());
        }
    }

    @Override
    public UserSettings getUserSettings(String username,
        String accessToken) throws UserNotFoundException, DatabaseAccessException {
        final String userId = getUserId(username, accessToken);
        final Request request = buildUserSettingsRequest(userId, accessToken);
        return executeUserSettingsRequest(request, username);
    }

    private Request buildUserSettingsRequest(String userId, String accessToken) {
        return new Request.Builder()
            .url(apiUrl + USER_SETTINGS_ENDPOINT + "?id=eq." + userId)
            .get()
            .addHeader(API_KEY_HEADER, apiKey)
            .addHeader(AUTH_HEADER, BEARER_PREFIX + accessToken)
            .addHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
            .addHeader(PREFER_HEADER, PREFER_RETURN)
            .build();
    }

    private UserSettings executeUserSettingsRequest(Request request,
        String username) throws DatabaseAccessException {
        try (Response response = client.newCall(request).execute()) {
            if (response.code() == HTTP_UNAUTHORIZED) {
                throw new DatabaseAccessException("Request is unauthorized: " + response.body().string());
            }
            else if (!response.isSuccessful()) {
                throw new DatabaseAccessException("Failed to get user settings: " + response.body().string());
            }

            final String responseBody = response.body().string();
            return parseUserSettings(responseBody, username);
        }
        catch (IOException exception) {
            throw new DatabaseAccessException(DATABASE_ACCESS_ERROR + exception.getMessage());
        }
    }

    private UserSettings parseUserSettings(String responseBody, String username) {
        final UserSettings settings;
        if (responseBody.equals(EMPTY_ARRAY)) {
            settings = createDefaultSettings(username);
        }
        else {
            final JSONArray settingsArray = new JSONArray(responseBody);
            final JSONObject jsonSettings = settingsArray.getJSONObject(0);
            settings = createUserSettings(username, jsonSettings);
        }
        return settings;
    }

    private UserSettings createDefaultSettings(String username) {
        return new UserSettings(
            username,
            UserSettings.DEFAULT_FOCUS_DURATION,
            UserSettings.DEFAULT_SHORT_BREAK_DURATION,
            UserSettings.DEFAULT_LONG_BREAK_DURATION,
            DEFAULT_AUTO_START_BREAKS,
            DEFAULT_AUTO_START_FOCUS);
    }

    private UserSettings createUserSettings(String username, JSONObject settings) {
        return new UserSettings(
            username,
            settings.getInt("focus_duration"),
            settings.getInt("short_break_duration"),
            settings.getInt("long_break_duration"),
            settings.getBoolean("auto_start_breaks"),
            settings.getBoolean("auto_start_focus"));
    }

    @Override
    public void updateUserSettings(String username, String accessToken,
        UserSettings userSettings) throws UserNotFoundException, DatabaseAccessException {
        final String userId = getUserId(username, accessToken);
        final Request request = buildUpdateSettingsRequest(userId, accessToken, userSettings);
        executeUpdateSettingsRequest(request);
    }

    private Request buildUpdateSettingsRequest(String id, String accessToken, UserSettings userSettings) {
        final JSONObject requestBody = new JSONObject()
            .put("id", id)
            .put("focus_duration", userSettings.getFocusDuration())
            .put("short_break_duration", userSettings.getShortBreakDuration())
            .put("long_break_duration", userSettings.getLongBreakDuration())
            .put("auto_start_breaks", userSettings.isAutoStartBreaks())
            .put("auto_start_focus", userSettings.isAutoStartFocus());

        final RequestBody body = RequestBody.create(
            requestBody.toString(),
            MediaType.parse(CONTENT_TYPE_JSON));

        return new Request.Builder()
            .url(apiUrl + USER_SETTINGS_ENDPOINT + "?id=eq." + id)
            .patch(body)
            .addHeader(API_KEY_HEADER, apiKey)
            .addHeader(AUTH_HEADER, BEARER_PREFIX + accessToken)
            .addHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
            .addHeader(PREFER_HEADER, PREFER_RETURN)
            .build();
    }

    private void executeUpdateSettingsRequest(Request request) throws DatabaseAccessException {
        try (Response response = client.newCall(request).execute()) {
            if (response.code() == HTTP_UNAUTHORIZED) {
                throw new DatabaseAccessException("Request is unauthenticated: " + response.body().string());
            }
            else if (!response.isSuccessful()) {
                throw new DatabaseAccessException("Failed to update user settings: " + response.body().string());
            }
        }
        catch (IOException exception) {
            throw new DatabaseAccessException(DATABASE_ACCESS_ERROR + exception.getMessage());
        }
    }
}
