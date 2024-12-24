package com.pawmodoro.users.data_access;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.pawmodoro.constants.Constants;
import com.pawmodoro.core.AuthenticationException;
import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.users.entity.AuthenticatedUser;
import com.pawmodoro.users.entity.AuthenticationToken;
import com.pawmodoro.users.entity.User;
import com.pawmodoro.users.entity.UserFactory;
import com.pawmodoro.users.entity.UserNotFoundException;
import com.pawmodoro.users.service.login.LoginUserDataAccessInterface;
import com.pawmodoro.users.service.logout.LogoutUserDataAccessInterface;
import com.pawmodoro.users.service.refresh.RefreshTokenDataAccessInterface;
import com.pawmodoro.users.service.signup.SignupUserDataAccessInterface;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Data access object for user authentication and management using Supabase.
 */
@Repository
public class DbUserDataAccessObject implements SignupUserDataAccessInterface,
    LoginUserDataAccessInterface, LogoutUserDataAccessInterface, RefreshTokenDataAccessInterface {

    private final OkHttpClient client;
    private final String apiUrl;
    private final String apiKey;
    private final UserFactory userFactory;

    /**
     * Creates a new DbUserDataAccessObject.
     * @param userFactory factory for creating User objects
     * @param apiUrl the Supabase API URL
     * @param apiKey the Supabase API key
     */
    public DbUserDataAccessObject(
        UserFactory userFactory,
        @Value("${supabase.url}") String apiUrl,
        @Value("${supabase.key}") String apiKey) {
        this.userFactory = userFactory;
        this.client = new OkHttpClient().newBuilder().build();
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    /**
     * Parses the error message from a JSON response.
     * @param jsonResponse the JSON response
     * @return the error message
     */
    private String parseErrorMessage(String jsonResponse) {
        String result = "";
        try {
            final JSONObject error = new JSONObject(jsonResponse);
            result = error.getString(Constants.JsonFields.MSG_FIELD);
        }
        catch (JSONException exception) {
            result = jsonResponse;
        }
        return result;
    }

    @Override
    public AuthenticationToken save(User user, String password) throws DatabaseAccessException {
        try {
            // First create the auth user
            final JSONObject authResponse = createAuthUser(user.getEmail(), password);
            final String userId = authResponse.getJSONObject(Constants.JsonFields.USER_FIELD)
                .getString(Constants.JsonFields.ID_FIELD);
            final String accessToken = authResponse.getString(Constants.JsonFields.ACCESS_TOKEN_FIELD);
            final String refreshToken = authResponse.getString(Constants.JsonFields.REFRESH_TOKEN_FIELD);
            final int expiresIn = authResponse.getInt(Constants.JsonFields.EXPIRES_IN_FIELD);
            final long expiresAt = authResponse.getLong(Constants.JsonFields.EXPIRES_AT_FIELD);

            // Create the user profile and settings
            createUserProfile(user, userId, accessToken);
            createUserSettings(userId, accessToken);

            return new AuthenticationToken(accessToken, refreshToken, expiresIn, expiresAt);
        }
        catch (IOException exception) {
            throw new DatabaseAccessException(Constants.ErrorMessages.DB_FAILED_ACCESS, exception);
        }
    }

    /**
     * Creates a user profile in the database.
     * This is called automatically during user creation.
     * @param user the user entity containing profile information
     * @param userId the ID of the user from auth.users
     * @param accessToken the access token for authentication
     * @throws IOException if there is an error communicating with the database
     * @throws DatabaseAccessException if the profile creation fails
     */
    private void createUserProfile(User user, String userId,
        String accessToken) throws IOException, DatabaseAccessException {
        final JSONObject profileBody = new JSONObject()
            .put(Constants.JsonFields.ID_FIELD, userId)
            .put(Constants.JsonFields.USERNAME_FIELD, user.getName())
            .put(Constants.JsonFields.EMAIL_FIELD, user.getEmail());

        final RequestBody body = RequestBody.create(
            profileBody.toString(),
            MediaType.parse(Constants.Http.CONTENT_TYPE_JSON));

        final Request request = new Request.Builder()
            .url(apiUrl + Constants.Endpoints.USER_PROFILES_ENDPOINT)
            .post(body)
            .addHeader(Constants.Http.API_KEY_HEADER, apiKey)
            .addHeader(Constants.Http.AUTH_HEADER, Constants.Http.BEARER_PREFIX + accessToken)
            .addHeader(Constants.Http.CONTENT_TYPE_HEADER, Constants.Http.CONTENT_TYPE_JSON)
            .addHeader(Constants.Http.PREFER_HEADER, Constants.Http.PREFER_MINIMAL)
            .build();

        final Response response = client.newCall(request).execute();
        final String responseBody = response.body().string();

        if (!response.isSuccessful()) {
            throw new DatabaseAccessException(
                String.format(Constants.ErrorMessages.DB_FAILED_CREATE_PROFILE, responseBody));
        }
    }

    /**
     * Creates default user settings for a new user.
     * This is called automatically during user creation.
     * The database table has default values for all columns except id.
     * @param userId the ID of the user to create settings for
     * @param accessToken the access token for authentication
     * @throws IOException if there is an error communicating with the database
     * @throws DatabaseAccessException if the settings creation fails
     */
    private void createUserSettings(String userId, String accessToken) throws IOException, DatabaseAccessException {
        final JSONObject settingsBody = new JSONObject()
            .put(Constants.JsonFields.ID_FIELD, userId);

        final RequestBody body = RequestBody.create(
            settingsBody.toString(),
            MediaType.parse(Constants.Http.CONTENT_TYPE_JSON));

        final Request request = new Request.Builder()
            .url(apiUrl + Constants.Endpoints.USER_SETTINGS_ENDPOINT)
            .post(body)
            .addHeader(Constants.Http.API_KEY_HEADER, apiKey)
            .addHeader(Constants.Http.AUTH_HEADER, Constants.Http.BEARER_PREFIX + accessToken)
            .addHeader(Constants.Http.CONTENT_TYPE_HEADER, Constants.Http.CONTENT_TYPE_JSON)
            .addHeader(Constants.Http.PREFER_HEADER, Constants.Http.PREFER_MINIMAL)
            .build();

        final Response response = client.newCall(request).execute();
        final String responseBody = response.body().string();

        if (!response.isSuccessful()) {
            throw new DatabaseAccessException(
                String.format(Constants.ErrorMessages.DB_FAILED_CREATE_SETTINGS, responseBody));
        }
    }

    /**
     * Creates a new auth user in Supabase.
     * @param email the user's email
     * @param password the user's password
     * @return JSONObject containing the response from Supabase auth
     * @throws DatabaseAccessException if the user creation fails
     */
    private JSONObject createAuthUser(String email, String password) throws DatabaseAccessException {
        final JSONObject authBody = new JSONObject()
            .put(Constants.JsonFields.EMAIL_FIELD, email)
            .put(Constants.JsonFields.PASSWORD_FIELD, password);

        final RequestBody body = RequestBody.create(
            authBody.toString(),
            MediaType.parse(Constants.Http.CONTENT_TYPE_JSON));

        final Request request = new Request.Builder()
            .url(apiUrl + Constants.Endpoints.AUTH_SIGNUP_ENDPOINT)
            .post(body)
            .addHeader(Constants.Http.API_KEY_HEADER, apiKey)
            .addHeader(Constants.Http.CONTENT_TYPE_HEADER, Constants.Http.CONTENT_TYPE_JSON)
            .build();

        try {
            final Response response = client.newCall(request).execute();
            final String responseBody = response.body().string();
            if (!response.isSuccessful()) {
                throw new DatabaseAccessException(parseErrorMessage(responseBody));
            }
            return new JSONObject(responseBody);
        }
        catch (IOException exception) {
            throw new DatabaseAccessException(Constants.ErrorMessages.DB_FAILED_ACCESS, exception);
        }
    }

    /**
     * Authenticates a user with Supabase and returns their profile.
     * @param email the user's email
     * @param password the user's password
     * @return JSONObject containing the auth response
     * @throws DatabaseAccessException if authentication fails
     * @throws UserNotFoundException if the user is not found
     */
    private JSONObject authenticateUser(String email,
        String password) throws DatabaseAccessException, UserNotFoundException {
        final JSONObject authBody = new JSONObject()
            .put(Constants.JsonFields.EMAIL_FIELD, email)
            .put(Constants.JsonFields.PASSWORD_FIELD, password);

        final RequestBody body = RequestBody.create(
            authBody.toString(),
            MediaType.parse(Constants.Http.CONTENT_TYPE_JSON));

        final Request request = new Request.Builder()
            .url(apiUrl + Constants.Endpoints.AUTH_SIGNIN_ENDPOINT)
            .post(body)
            .addHeader(Constants.Http.API_KEY_HEADER, apiKey)
            .addHeader(Constants.Http.CONTENT_TYPE_HEADER, Constants.Http.CONTENT_TYPE_JSON)
            .build();

        try (Response response = client.newCall(request).execute()) {
            final String responseBody = response.body().string();
            if (!response.isSuccessful()) {
                throw new UserNotFoundException(parseErrorMessage(responseBody));
            }
            return new JSONObject(responseBody);
        }
        catch (IOException exception) {
            throw new DatabaseAccessException(Constants.ErrorMessages.DB_FAILED_ACCESS, exception);
        }
    }

    /**
     * Authenticates and retrieves a user by their email and password.
     * @param email the user's email
     * @param password the user's password
     * @return the authenticated User
     * @throws DatabaseAccessException if authentication fails
     * @throws UserNotFoundException if the user is not found
     */
    @Override
    public AuthenticatedUser authenticate(String email,
        String password) throws DatabaseAccessException, UserNotFoundException {
        final JSONObject authResponse = authenticateUser(email, password);

        // Extract tokens and expiration info
        final String accessToken = authResponse.getString(Constants.JsonFields.ACCESS_TOKEN_FIELD);
        final String refreshToken = authResponse.getString(Constants.JsonFields.REFRESH_TOKEN_FIELD);
        final int expiresIn = authResponse.getInt(Constants.JsonFields.EXPIRES_IN_FIELD);
        final long expiresAt = authResponse.getLong(Constants.JsonFields.EXPIRES_AT_FIELD);
        final AuthenticationToken tokens = new AuthenticationToken(
            accessToken, refreshToken, expiresIn, expiresAt);

        // Get user info
        final JSONObject userInfo = authResponse.getJSONObject(Constants.JsonFields.USER_FIELD);
        final String userId = userInfo.getString(Constants.JsonFields.ID_FIELD);
        final String userEmail = userInfo.getString(Constants.JsonFields.EMAIL_FIELD);

        // Get username from profile
        final String username = getUsernameFromProfile(userId);
        final User user = userFactory.create(username, userEmail);

        return new AuthenticatedUser(user, tokens);
    }

    private String getUsernameFromProfile(String userId) throws DatabaseAccessException, UserNotFoundException {
        final Request request = new Request.Builder()
            .url(apiUrl + Constants.Endpoints.USER_PROFILES_ENDPOINT + Constants.Http.QUERY_START
                + Constants.JsonFields.ID_FIELD + Constants.Http.QUERY_EQUALS + userId)
            .get()
            .addHeader(Constants.Http.API_KEY_HEADER, apiKey)
            .addHeader(Constants.Http.CONTENT_TYPE_HEADER, Constants.Http.CONTENT_TYPE_JSON)
            .addHeader(Constants.Http.PREFER_HEADER, Constants.Http.PREFER_REPRESENTATION)
            .build();

        try {
            final Response response = client.newCall(request).execute();
            final String responseBody = response.body().string();

            if (!response.isSuccessful() || responseBody.equals(Constants.JsonFields.EMPTY_ARRAY)) {
                throw new UserNotFoundException(Constants.ErrorMessages.AUTH_PROFILE_NOT_FOUND);
            }

            final JSONArray profiles = new JSONArray(responseBody);
            if (profiles.length() == 0) {
                throw new UserNotFoundException(Constants.ErrorMessages.AUTH_PROFILE_NOT_FOUND);
            }

            final JSONObject userProfile = profiles.getJSONObject(0);
            return userProfile.getString(Constants.JsonFields.USERNAME_FIELD);
        }
        catch (IOException exception) {
            throw new DatabaseAccessException(Constants.ErrorMessages.DB_FAILED_ACCESS, exception);
        }
    }

    @Override
    public boolean existsByName(String username) throws DatabaseAccessException {
        final Request request = new Request.Builder()
            .url(apiUrl + Constants.Endpoints.USER_PROFILES_ENDPOINT + Constants.Http.QUERY_START
                + Constants.JsonFields.USERNAME_FIELD + Constants.Http.QUERY_EQUALS + username)
            .get()
            .addHeader(Constants.Http.API_KEY_HEADER, apiKey)
            .addHeader(Constants.Http.CONTENT_TYPE_HEADER, Constants.Http.CONTENT_TYPE_JSON)
            .addHeader(Constants.Http.PREFER_HEADER, Constants.Http.PREFER_REPRESENTATION)
            .build();

        try {
            final Response response = client.newCall(request).execute();
            final String responseBody = response.body().string();
            return response.isSuccessful() && !responseBody.equals(Constants.JsonFields.EMPTY_ARRAY);
        }
        catch (IOException exception) {
            throw new DatabaseAccessException(Constants.ErrorMessages.DB_FAILED_ACCESS, exception);
        }
    }

    @Override
    public User get(String username) throws DatabaseAccessException, UserNotFoundException {
        final Request request = new Request.Builder()
            .url(apiUrl + Constants.Endpoints.USER_PROFILES_ENDPOINT + Constants.Http.QUERY_START
                + Constants.JsonFields.USERNAME_FIELD + Constants.Http.QUERY_EQUALS + username)
            .get()
            .addHeader(Constants.Http.API_KEY_HEADER, apiKey)
            .addHeader(Constants.Http.CONTENT_TYPE_HEADER, Constants.Http.CONTENT_TYPE_JSON)
            .addHeader(Constants.Http.PREFER_HEADER, Constants.Http.PREFER_REPRESENTATION)
            .build();

        try {
            final Response response = client.newCall(request).execute();
            final String responseBody = response.body().string();

            if (!response.isSuccessful() || responseBody.equals(Constants.JsonFields.EMPTY_ARRAY)) {
                throw new UserNotFoundException(
                    String.format(Constants.ErrorMessages.AUTH_USER_NOT_FOUND, username));
            }

            // Parse the array response from Supabase
            final JSONArray profiles = new JSONArray(responseBody);
            if (profiles.length() == 0) {
                throw new UserNotFoundException(
                    String.format(Constants.ErrorMessages.AUTH_USER_NOT_FOUND, username));
            }

            final JSONObject userProfile = profiles.getJSONObject(0);
            final String email = userProfile.getString(Constants.JsonFields.EMAIL_FIELD);

            return userFactory.create(username, email);
        }
        catch (IOException exception) {
            throw new DatabaseAccessException(Constants.ErrorMessages.DB_FAILED_ACCESS, exception);
        }
    }

    @Override
    public boolean logout(String accessToken) {
        boolean result = false;

        if (accessToken != null) {
            final Request request = new Request.Builder()
                .url(apiUrl + Constants.Endpoints.AUTH_SIGNOUT_ENDPOINT)
                .post(RequestBody.create("", MediaType.parse(Constants.Http.CONTENT_TYPE_JSON)))
                .addHeader(Constants.Http.AUTH_HEADER, Constants.Http.BEARER_PREFIX + accessToken)
                .addHeader(Constants.Http.API_KEY_HEADER, apiKey)
                .build();

            try (Response response = client.newCall(request).execute()) {
                result = response.isSuccessful();
            }
            catch (IOException exception) {
                result = false;
            }
        }

        return result;
    }

    @Override
    public AuthenticationToken refreshTokens(
        String refreshToken) throws DatabaseAccessException, UserNotFoundException {
        final JSONObject refreshBody = new JSONObject()
            .put(Constants.JsonFields.REFRESH_TOKEN_FIELD, refreshToken);

        final RequestBody body = RequestBody.create(
            refreshBody.toString(),
            MediaType.parse(Constants.Http.CONTENT_TYPE_JSON));

        final Request request = new Request.Builder()
            .url(apiUrl + Constants.Endpoints.AUTH_REFRESH_ENDPOINT)
            .post(body)
            .addHeader(Constants.Http.API_KEY_HEADER, apiKey)
            .addHeader(Constants.Http.CONTENT_TYPE_HEADER, Constants.Http.CONTENT_TYPE_JSON)
            .build();

        try (Response response = client.newCall(request).execute()) {
            final String responseBody = response.body().string();
            if (!response.isSuccessful()) {
                if (response.code() == HttpStatus.BAD_REQUEST.value()) {
                    // Handle refresh token errors (already used or not found)
                    throw new AuthenticationException(parseErrorMessage(responseBody));
                }
                if (response.code() == HttpStatus.UNAUTHORIZED.value()) {
                    throw new UserNotFoundException(parseErrorMessage(responseBody));
                }
                throw new DatabaseAccessException(Constants.ErrorMessages.DB_FAILED_ACCESS);
            }

            final JSONObject tokens = new JSONObject(responseBody);
            return new AuthenticationToken(
                tokens.getString(Constants.JsonFields.ACCESS_TOKEN_FIELD),
                tokens.getString(Constants.JsonFields.REFRESH_TOKEN_FIELD),
                tokens.getInt(Constants.JsonFields.EXPIRES_IN_FIELD),
                tokens.getLong(Constants.JsonFields.EXPIRES_AT_FIELD));
        }
        catch (IOException exception) {
            throw new DatabaseAccessException(Constants.ErrorMessages.DB_FAILED_ACCESS, exception);
        }
    }

    @Override
    public boolean existsByEmail(String email) throws DatabaseAccessException {
        final Request request = new Request.Builder()
            .url(apiUrl + Constants.Endpoints.USER_PROFILES_ENDPOINT + Constants.Http.QUERY_START
                + Constants.JsonFields.EMAIL_FIELD + Constants.Http.QUERY_EQUALS + email)
            .get()
            .addHeader(Constants.Http.API_KEY_HEADER, apiKey)
            .addHeader(Constants.Http.CONTENT_TYPE_HEADER, Constants.Http.CONTENT_TYPE_JSON)
            .addHeader(Constants.Http.PREFER_HEADER, Constants.Http.PREFER_REPRESENTATION)
            .build();

        try {
            final Response response = client.newCall(request).execute();
            final String responseBody = response.body().string();
            return response.isSuccessful() && !responseBody.equals(Constants.JsonFields.EMPTY_ARRAY);
        }
        catch (IOException exception) {
            throw new DatabaseAccessException(Constants.ErrorMessages.DB_FAILED_ACCESS, exception);
        }
    }
}
