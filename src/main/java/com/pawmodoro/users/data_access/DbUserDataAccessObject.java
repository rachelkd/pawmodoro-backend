package com.pawmodoro.users.data_access;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.pawmodoro.core.DatabaseAccessException;
import com.pawmodoro.users.entity.User;
import com.pawmodoro.users.entity.UserFactory;
import com.pawmodoro.users.entity.UserNotFoundException;
import com.pawmodoro.users.service.login.LoginUserDataAccessInterface;
import com.pawmodoro.users.service.signup.SignupUserDataAccessInterface;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import use_case.adoption.AdoptionDataAccessInterface;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;

/**
 * Data access object for user authentication and management using Supabase.
 */
@Repository
public class DbUserDataAccessObject implements SignupUserDataAccessInterface,
    LoginUserDataAccessInterface,
    ChangePasswordUserDataAccessInterface,
    LogoutUserDataAccessInterface,
    AdoptionDataAccessInterface {

    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String API_KEY_HEADER = "apikey";
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String EMPTY_ARRAY = "[]";
    private static final String ID_FIELD = "id";
    private static final String EMAIL_FIELD = "email";
    private static final String PASSWORD_FIELD = "password";
    private static final String USERNAME_FIELD = "username";
    private static final String ACCESS_TOKEN_FIELD = "access_token";
    private static final String USER_FIELD = "user";
    private static final String PREFER_HEADER = "Prefer";
    private static final String PREFER_REPRESENTATION = "return=representation";
    private static final String PREFER_MINIMAL = "return=minimal";
    private static final String MSG_FIELD = "msg";
    private static final String QUERY_START = "?";
    private static final String QUERY_EQUALS = "=eq.";

    private static final String AUTH_SIGNUP_ENDPOINT = "/auth/v1/signup";
    private static final String AUTH_SIGNIN_ENDPOINT = "/auth/v1/token?grant_type=password";
    private static final String AUTH_SIGNOUT_ENDPOINT = "/auth/v1/logout";
    private static final String AUTH_PASSWORD_RESET = "/auth/v1/user/password";
    private static final String USER_PROFILES_ENDPOINT = "/rest/v1/user_profiles";
    private static final String AUTH_USERS_ENDPOINT = "/auth/v1/user";

    private final OkHttpClient client;
    private final String apiUrl;
    private final String apiKey;
    private final UserFactory userFactory;
    private String currentUsername;
    private String accessToken;

    /**
     * Creates a new DbUserDataAccessObject.
     * @param userFactory factory for creating User objects
     * @param apiUrl the Supabase API URL
     * @param apiKey the Supabase API key
     */
    public DbUserDataAccessObject(
        UserFactory userFactory,
        @Value("${supabase.url}")
        String apiUrl,
        @Value("${supabase.key}")
        String apiKey) {
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
            result = error.getString(MSG_FIELD);
        }
        catch (JSONException exception) {
            result = jsonResponse;
        }
        return result;
    }

    @Override
    public void save(User user, String password) throws DatabaseAccessException {
        try {
            // First create the auth user
            final JSONObject authResponse = createAuthUser(user.getEmail(), password);
            final String userId = authResponse.getJSONObject(USER_FIELD).getString(ID_FIELD);
            this.accessToken = authResponse.getString(ACCESS_TOKEN_FIELD);

            // Create the profile with both username and email
            final JSONObject profileBody = new JSONObject()
                .put(ID_FIELD, userId)
                .put(USERNAME_FIELD, user.getName())
                .put(EMAIL_FIELD, user.getEmail());

            final RequestBody body = RequestBody.create(
                profileBody.toString(),
                MediaType.parse(CONTENT_TYPE_JSON));

            final Request request = new Request.Builder()
                .url(apiUrl + USER_PROFILES_ENDPOINT)
                .post(body)
                .addHeader(API_KEY_HEADER, apiKey)
                .addHeader(AUTH_HEADER, BEARER_PREFIX + accessToken)
                .addHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
                .addHeader(PREFER_HEADER, PREFER_MINIMAL)
                .build();

            try (Response response = client.newCall(request).execute()) {
                final String responseBody = response.body().string();
                if (!response.isSuccessful()) {
                    throw new DatabaseAccessException("Failed to create user profile: " + responseBody);
                }
            }
        }
        catch (DatabaseAccessException | IOException exception) {
            throw new DatabaseAccessException(exception.getMessage());
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
            .put(EMAIL_FIELD, email)
            .put(PASSWORD_FIELD, password);

        final RequestBody body = RequestBody.create(
            authBody.toString(),
            MediaType.parse(CONTENT_TYPE_JSON));

        final Request request = new Request.Builder()
            .url(apiUrl + AUTH_SIGNUP_ENDPOINT)
            .post(body)
            .addHeader(API_KEY_HEADER, apiKey)
            .addHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
            .build();

        try (Response response = client.newCall(request).execute()) {
            final String responseBody = response.body().string();
            if (!response.isSuccessful()) {
                throw new DatabaseAccessException(parseErrorMessage(responseBody));
            }
            return new JSONObject(responseBody);
        }
        catch (IOException exception) {
            throw new DatabaseAccessException("Failed to create auth user: " + exception.getMessage());
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
            .put(EMAIL_FIELD, email)
            .put(PASSWORD_FIELD, password);

        final RequestBody body = RequestBody.create(
            authBody.toString(),
            MediaType.parse(CONTENT_TYPE_JSON));

        final Request request = new Request.Builder()
            .url(apiUrl + AUTH_SIGNIN_ENDPOINT)
            .post(body)
            .addHeader(API_KEY_HEADER, apiKey)
            .addHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
            .build();

        try (Response response = client.newCall(request).execute()) {
            final String responseBody = response.body().string();
            if (!response.isSuccessful()) {
                throw new UserNotFoundException(parseErrorMessage(responseBody));
            }
            return new JSONObject(responseBody);
        }
        catch (IOException exception) {
            throw new DatabaseAccessException("Failed to authenticate user: " + exception.getMessage());
        }
    }

    @Override
    public User get(String username) throws DatabaseAccessException, UserNotFoundException {
        final Request request = new Request.Builder()
            .url(apiUrl + USER_PROFILES_ENDPOINT + QUERY_START + USERNAME_FIELD + QUERY_EQUALS + username)
            .get()
            .addHeader(API_KEY_HEADER, apiKey)
            .addHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
            .addHeader(PREFER_HEADER, PREFER_REPRESENTATION)
            .build();

        try {
            final Response response = client.newCall(request).execute();
            final String responseBody = response.body().string();

            if (!response.isSuccessful() || responseBody.equals(EMPTY_ARRAY)) {
                throw new UserNotFoundException("User not found: " + username);
            }

            // Parse the array response from Supabase
            final JSONArray profiles = new JSONArray(responseBody);
            if (profiles.length() == 0) {
                throw new UserNotFoundException("User not found: " + username);
            }

            final JSONObject userProfile = profiles.getJSONObject(0);
            final String email = userProfile.getString(EMAIL_FIELD);

            return userFactory.create(username, email);
        }
        catch (IOException exception) {
            throw new DatabaseAccessException("Failed to access database", exception);
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
    public User authenticate(String email, String password) throws DatabaseAccessException, UserNotFoundException {
        final JSONObject authResponse = authenticateUser(email, password);
        this.accessToken = authResponse.getString(ACCESS_TOKEN_FIELD);

        // The auth response already contains the user's email
        final JSONObject userInfo = authResponse.getJSONObject(USER_FIELD);
        final String userId = userInfo.getString(ID_FIELD);
        final String userEmail = userInfo.getString(EMAIL_FIELD);

        final Request request = new Request.Builder()
            .url(apiUrl + USER_PROFILES_ENDPOINT + QUERY_START + ID_FIELD + QUERY_EQUALS + userId)
            .get()
            .addHeader(API_KEY_HEADER, apiKey)
            .addHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
            .addHeader(PREFER_HEADER, PREFER_REPRESENTATION)
            .build();

        try {
            final Response response = client.newCall(request).execute();
            final String responseBody = response.body().string();

            if (!response.isSuccessful() || responseBody.equals(EMPTY_ARRAY)) {
                throw new UserNotFoundException("User profile not found");
            }

            // Parse the array response from Supabase
            final JSONArray profiles = new JSONArray(responseBody);
            if (profiles.length() == 0) {
                throw new UserNotFoundException("User profile not found");
            }

            final JSONObject userProfile = profiles.getJSONObject(0);
            final String username = userProfile.getString(USERNAME_FIELD);
            return userFactory.create(username, userEmail);
        }
        catch (IOException exception) {
            throw new DatabaseAccessException("Failed to get user profile", exception);
        }
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    @Override
    public boolean existsByName(String username) throws DatabaseAccessException {
        final Request request = new Request.Builder()
            .url(apiUrl + USER_PROFILES_ENDPOINT + QUERY_START + USERNAME_FIELD + QUERY_EQUALS + username)
            .get()
            .addHeader(API_KEY_HEADER, apiKey)
            .addHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
            .addHeader(PREFER_HEADER, PREFER_REPRESENTATION)
            .build();

        try {
            final Response response = client.newCall(request).execute();
            final String responseBody = response.body().string();
            return response.isSuccessful() && !responseBody.equals(EMPTY_ARRAY);
        }
        catch (IOException exception) {
            throw new DatabaseAccessException("Failed to check user existence", exception);
        }
    }

    @Override
    public void changePassword(User user, String newPassword) throws DatabaseAccessException {
        if (accessToken == null) {
            throw new DatabaseAccessException("User not authenticated");
        }

        final JSONObject jsonBody = new JSONObject().put(PASSWORD_FIELD, newPassword);
        final Request request = buildPasswordResetRequest(jsonBody);

        try {
            final Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new DatabaseAccessException("Failed to update password");
            }
        }
        catch (IOException exception) {
            throw new DatabaseAccessException("Failed to access database", exception);
        }
    }

    private Request buildPasswordResetRequest(JSONObject jsonBody) {
        final RequestBody body = RequestBody.create(
            jsonBody.toString(),
            MediaType.parse(CONTENT_TYPE_JSON));

        return new Request.Builder()
            .url(apiUrl + AUTH_PASSWORD_RESET)
            .put(body)
            .addHeader(AUTH_HEADER, BEARER_PREFIX + accessToken)
            .addHeader(API_KEY_HEADER, apiKey)
            .addHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON)
            .build();
    }

    @Override
    public boolean logout() {
        boolean success = false;
        if (accessToken != null) {
            final Request request = buildLogoutRequest();
            success = executeLogoutRequest(request);
        }
        return success;
    }

    private Request buildLogoutRequest() {
        return new Request.Builder()
            .url(apiUrl + AUTH_SIGNOUT_ENDPOINT)
            .post(RequestBody.create("", MediaType.parse(CONTENT_TYPE_JSON)))
            .addHeader(AUTH_HEADER, BEARER_PREFIX + accessToken)
            .addHeader(API_KEY_HEADER, apiKey)
            .build();
    }

    private boolean executeLogoutRequest(Request request) {
        boolean result = false;
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                this.accessToken = null;
                result = true;
            }
        }
        catch (IOException exception) {
            // Log error if needed
        }
        return result;
    }

    /**
     * Gets the access token from the most recent successful authentication.
     * @return the Supabase access token
     */
    @Override
    public String getAccessToken() {
        return accessToken;
    }
}
