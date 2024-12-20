package com.pawmodoro.settings.service.get_user_settings;

/**
 * Input data for the GetUserSettings use case.
 * Contains the username and access token needed to retrieve user settings.
 */
public class GetUserSettingsInputData {
    private final String username;
    private final String accessToken;

    /**
     * Constructs a GetUserSettingsInputData object.
     * @param username the username of the user whose settings to retrieve
     * @param accessToken the access token for authentication
     */
    public GetUserSettingsInputData(String username, String accessToken) {
        this.username = username;
        this.accessToken = accessToken;
    }

    /**
     * Gets the username.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the access token.
     * @return the access token
     */
    public String getAccessToken() {
        return accessToken;
    }
}
