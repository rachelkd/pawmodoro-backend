package com.pawmodoro.settings.service.update_user_settings;

import lombok.Getter;

/**
 * Input data for updating user settings.
 * Contains all the necessary information to update a user's settings.
 */
@Getter
public class UpdateUserSettingsInputData {
    private final String username;
    private final String accessToken;
    private final int focusDuration;
    private final int shortBreakDuration;
    private final int longBreakDuration;
    private final boolean autoStartBreaks;
    private final boolean autoStartFocus;

    /**
     * Constructs an UpdateUserSettingsInputData object.
     * @param username the username of the user whose settings to update
     * @param accessToken the access token for authentication
     * @param focusDuration the focus duration in minutes
     * @param shortBreakDuration the short break duration in minutes
     * @param longBreakDuration the long break duration in minutes
     * @param autoStartBreaks whether breaks should start automatically
     * @param autoStartFocus whether focus sessions should start automatically
     */
    public UpdateUserSettingsInputData(
        String username,
        String accessToken,
        int focusDuration,
        int shortBreakDuration,
        int longBreakDuration,
        boolean autoStartBreaks,
        boolean autoStartFocus) {
        this.username = username;
        this.accessToken = accessToken;
        this.focusDuration = focusDuration;
        this.shortBreakDuration = shortBreakDuration;
        this.longBreakDuration = longBreakDuration;
        this.autoStartBreaks = autoStartBreaks;
        this.autoStartFocus = autoStartFocus;
    }
}
