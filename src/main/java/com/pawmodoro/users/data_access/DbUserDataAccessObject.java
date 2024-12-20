package com.pawmodoro.users.data_access;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.pawmodoro.constants.Constants;
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
            result = error.getString(Constants.JsonFields.MSG_FIELD);
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
            final String userId = authResponse.getJSONObject(Constants.JsonFields.USER_FIELD)
                .getString(Constants.JsonFields.ID_FIELD);
            this.accessToken = authResponse.getString(Constants.JsonFields.ACCESS_TOKEN_FIELD);

            // Create the profile with both username and email
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
        catch (IOException exception) {
            throw new DatabaseAccessException(Constants.ErrorMessages.DB_FAILED_ACCESS, exception);
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

        try (Response response = client.newCall(request).execute()) {
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
        this.accessToken = authResponse.getString(Constants.JsonFields.ACCESS_TOKEN_FIELD);

        // The auth response already contains the user's email
        final JSONObject userInfo = authResponse.getJSONObject(Constants.JsonFields.USER_FIELD);
        final String userId = userInfo.getString(Constants.JsonFields.ID_FIELD);
        final String userEmail = userInfo.getString(Constants.JsonFields.EMAIL_FIELD);

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

            // Parse the array response from Supabase
            final JSONArray profiles = new JSONArray(responseBody);
            if (profiles.length() == 0) {
                throw new UserNotFoundException(Constants.ErrorMessages.AUTH_PROFILE_NOT_FOUND);
            }

            final JSONObject userProfile = profiles.getJSONObject(0);
            final String username = userProfile.getString(Constants.JsonFields.USERNAME_FIELD);
            return userFactory.create(username, userEmail);
        }
        catch (IOException exception) {
            throw new DatabaseAccessException(Constants.ErrorMessages.DB_FAILED_ACCESS, exception);
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
    public void changePassword(User user, String newPassword) throws DatabaseAccessException {
        if (accessToken == null) {
            throw new DatabaseAccessException(Constants.ErrorMessages.AUTH_USER_NOT_AUTHENTICATED);
        }

        final JSONObject jsonBody = new JSONObject()
            .put(Constants.JsonFields.PASSWORD_FIELD, newPassword);
        final Request request = buildPasswordResetRequest(jsonBody);

        try {
            final Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new DatabaseAccessException(Constants.ErrorMessages.AUTH_FAILED_PASSWORD_UPDATE);
            }
        }
        catch (IOException exception) {
            throw new DatabaseAccessException(Constants.ErrorMessages.DB_FAILED_ACCESS, exception);
        }
    }

    private Request buildPasswordResetRequest(JSONObject jsonBody) {
        final RequestBody body = RequestBody.create(
            jsonBody.toString(),
            MediaType.parse(Constants.Http.CONTENT_TYPE_JSON));

        return new Request.Builder()
            .url(apiUrl + Constants.Endpoints.AUTH_PASSWORD_RESET)
            .put(body)
            .addHeader(Constants.Http.AUTH_HEADER, Constants.Http.BEARER_PREFIX + accessToken)
            .addHeader(Constants.Http.API_KEY_HEADER, apiKey)
            .addHeader(Constants.Http.CONTENT_TYPE_HEADER, Constants.Http.CONTENT_TYPE_JSON)
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
            .url(apiUrl + Constants.Endpoints.AUTH_SIGNOUT_ENDPOINT)
            .post(RequestBody.create("", MediaType.parse(Constants.Http.CONTENT_TYPE_JSON)))
            .addHeader(Constants.Http.AUTH_HEADER, Constants.Http.BEARER_PREFIX + accessToken)
            .addHeader(Constants.Http.API_KEY_HEADER, apiKey)
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
