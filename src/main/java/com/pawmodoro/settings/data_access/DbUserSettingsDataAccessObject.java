package com.pawmodoro.settings.data_access;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.pawmodoro.constants.Constants;
import com.pawmodoro.core.AuthenticationException;
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

    // Default settings values
    private static final int DEFAULT_FOCUS_DURATION = 25;
    private static final int DEFAULT_SHORT_BREAK = 5;
    private static final int DEFAULT_LONG_BREAK = 15;
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
        @Value("${supabase.url}") String apiUrl,
        @Value("${supabase.key}") String apiKey) {
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
            .url(apiUrl + Constants.Endpoints.USER_PROFILES_ENDPOINT + Constants.Http.QUERY_START
                + Constants.JsonFields.USERNAME_FIELD + Constants.Http.QUERY_EQUALS + username)
            .get()
            .addHeader(Constants.Http.API_KEY_HEADER, apiKey)
            .addHeader(Constants.Http.AUTH_HEADER, Constants.Http.BEARER_PREFIX + accessToken)
            .addHeader(Constants.Http.CONTENT_TYPE_HEADER, Constants.Http.CONTENT_TYPE_JSON)
            .build();
    }

    private String executeUserProfileRequest(Request request,
        String username) throws UserNotFoundException, DatabaseAccessException {
        try (Response response = client.newCall(request).execute()) {
            if (response.code() == HttpStatus.UNAUTHORIZED.value()) {
                throw new AuthenticationException(Constants.ErrorMessages.AUTH_TOKEN_INVALID);
            }
            else if (!response.isSuccessful()) {
                throw new DatabaseAccessException(
                    String.format(
                        Constants.ErrorMessages.DB_FAILED_ACCESS + Constants.ErrorMessages.ERROR_MESSAGE_FORMAT,
                        response.body().string()));
            }

            final String responseBody = response.body().string();
            if (responseBody.equals(Constants.JsonFields.EMPTY_ARRAY)) {
                throw new UserNotFoundException(
                    String.format(Constants.ErrorMessages.AUTH_USER_NOT_FOUND, username));
            }

            final JSONArray profiles = new JSONArray(responseBody);
            return profiles.getJSONObject(0).getString(Constants.JsonFields.ID_FIELD);
        }
        catch (IOException exception) {
            throw new DatabaseAccessException(
                String.format(
                    Constants.ErrorMessages.DB_FAILED_ACCESS + Constants.ErrorMessages.ERROR_MESSAGE_FORMAT,
                    exception.getMessage()));
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
            .url(apiUrl + Constants.Endpoints.USER_SETTINGS_ENDPOINT + Constants.Http.QUERY_START
                + Constants.JsonFields.ID_FIELD + Constants.Http.QUERY_EQUALS + userId)
            .get()
            .addHeader(Constants.Http.API_KEY_HEADER, apiKey)
            .addHeader(Constants.Http.AUTH_HEADER, Constants.Http.BEARER_PREFIX + accessToken)
            .addHeader(Constants.Http.CONTENT_TYPE_HEADER, Constants.Http.CONTENT_TYPE_JSON)
            .addHeader(Constants.Http.PREFER_HEADER, Constants.Http.PREFER_REPRESENTATION)
            .build();
    }

    private UserSettings executeUserSettingsRequest(Request request,
        String username) throws DatabaseAccessException {
        try (Response response = client.newCall(request).execute()) {
            if (response.code() == HttpStatus.UNAUTHORIZED.value()) {
                throw new AuthenticationException(Constants.ErrorMessages.AUTH_TOKEN_INVALID);
            }
            else if (!response.isSuccessful()) {
                throw new DatabaseAccessException(
                    String.format(
                        Constants.ErrorMessages.DB_FAILED_ACCESS + Constants.ErrorMessages.ERROR_MESSAGE_FORMAT,
                        response.body().string()));
            }

            final String responseBody = response.body().string();
            return parseUserSettings(responseBody, username);
        }
        catch (IOException exception) {
            throw new DatabaseAccessException(Constants.ErrorMessages.DB_FAILED_ACCESS + ": " + exception.getMessage());
        }
    }

    private UserSettings parseUserSettings(String responseBody, String username) {
        final UserSettings settings;
        if (responseBody.equals(Constants.JsonFields.EMPTY_ARRAY)) {
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
            DEFAULT_FOCUS_DURATION,
            DEFAULT_SHORT_BREAK,
            DEFAULT_LONG_BREAK,
            DEFAULT_AUTO_START_BREAKS,
            DEFAULT_AUTO_START_FOCUS);
    }

    private UserSettings createUserSettings(String username, JSONObject settings) {
        return new UserSettings(
            username,
            settings.getInt(Constants.JsonFields.FOCUS_DURATION),
            settings.getInt(Constants.JsonFields.SHORT_BREAK_DURATION),
            settings.getInt(Constants.JsonFields.LONG_BREAK_DURATION),
            settings.getBoolean(Constants.JsonFields.AUTO_START_BREAKS),
            settings.getBoolean(Constants.JsonFields.AUTO_START_FOCUS));
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
            .put(Constants.JsonFields.ID_FIELD, id)
            .put(Constants.JsonFields.FOCUS_DURATION, userSettings.getFocusDuration())
            .put(Constants.JsonFields.SHORT_BREAK_DURATION, userSettings.getShortBreakDuration())
            .put(Constants.JsonFields.LONG_BREAK_DURATION, userSettings.getLongBreakDuration())
            .put(Constants.JsonFields.AUTO_START_BREAKS, userSettings.isAutoStartBreaks())
            .put(Constants.JsonFields.AUTO_START_FOCUS, userSettings.isAutoStartFocus());

        final RequestBody body = RequestBody.create(
            requestBody.toString(),
            MediaType.parse(Constants.Http.CONTENT_TYPE_JSON));

        return new Request.Builder()
            .url(apiUrl + Constants.Endpoints.USER_SETTINGS_ENDPOINT + Constants.Http.QUERY_START
                + Constants.JsonFields.ID_FIELD + Constants.Http.QUERY_EQUALS + id)
            .patch(body)
            .addHeader(Constants.Http.API_KEY_HEADER, apiKey)
            .addHeader(Constants.Http.AUTH_HEADER, Constants.Http.BEARER_PREFIX + accessToken)
            .addHeader(Constants.Http.CONTENT_TYPE_HEADER, Constants.Http.CONTENT_TYPE_JSON)
            .addHeader(Constants.Http.PREFER_HEADER, Constants.Http.PREFER_REPRESENTATION)
            .build();
    }

    private void executeUpdateSettingsRequest(Request request) throws DatabaseAccessException {
        try (Response response = client.newCall(request).execute()) {
            if (response.code() == HttpStatus.UNAUTHORIZED.value()) {
                throw new AuthenticationException(Constants.ErrorMessages.AUTH_TOKEN_INVALID);
            }
            else if (!response.isSuccessful()) {
                throw new DatabaseAccessException(
                    String.format(
                        Constants.ErrorMessages.DB_FAILED_ACCESS + Constants.ErrorMessages.ERROR_MESSAGE_FORMAT,
                        response.body().string()));
            }
        }
        catch (IOException exception) {
            throw new DatabaseAccessException(Constants.ErrorMessages.DB_FAILED_ACCESS + ": " + exception.getMessage());
        }
    }
}
