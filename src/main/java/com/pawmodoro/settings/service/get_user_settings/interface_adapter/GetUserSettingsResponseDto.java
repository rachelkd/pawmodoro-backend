package com.pawmodoro.settings.service.get_user_settings.interface_adapter;

/**
 * Response DTO for the GetUserSettings use case.
 * Contains the user settings data to be returned to the client.
 * @param username the username of the user
 * @param focusDuration the focus duration in minutes
 * @param shortBreakDuration the short break duration in minutes
 * @param longBreakDuration the long break duration in minutes
 * @param autoStartBreaks whether breaks should start automatically
 * @param autoStartFocus whether focus sessions should start automatically
 */
public record GetUserSettingsResponseDto(
    String username,
    int focusDuration,
    int shortBreakDuration,
    int longBreakDuration,
    boolean autoStartBreaks,
    boolean autoStartFocus) {

}
