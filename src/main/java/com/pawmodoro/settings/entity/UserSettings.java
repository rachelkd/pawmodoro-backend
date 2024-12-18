package com.pawmodoro.settings.entity;

/**
 * Represents the user settings for a Pawmodoro user.
 * @param userId The ID of the user.
 * @param focusDuration The duration of the focus period in minutes.
 * @param shortBreakDuration The duration of the short break period in minutes.
 * @param longBreakDuration The duration of the long break period in minutes.
 * @param autoStartBreaks Whether to automatically start breaks.
 * @param autoStartFocus Whether to automatically start focus.
 */
public class UserSettings {
    private final String userId;
    private int focusDuration;
    private int shortBreakDuration;
    private int longBreakDuration;
    private boolean autoStartBreaks;
    private boolean autoStartFocus;

    public UserSettings(String userId, int focusDuration, int shortBreakDuration,
        int longBreakDuration, boolean autoStartBreaks,
        boolean autoStartFocus) {
        this.userId = userId;
        this.focusDuration = focusDuration;
        this.shortBreakDuration = shortBreakDuration;
        this.longBreakDuration = longBreakDuration;
        this.autoStartBreaks = autoStartBreaks;
        this.autoStartFocus = autoStartFocus;
    }

    // Setters with validation
    public void setFocusDuration(int focusDuration) {
        this.focusDuration = focusDuration;
    }

    public void setShortBreakDuration(int shortBreakDuration) {
        this.shortBreakDuration = shortBreakDuration;
    }

    public void setLongBreakDuration(int longBreakDuration) {
        this.longBreakDuration = longBreakDuration;
    }

    public void setAutoStartBreaks(boolean autoStartBreaks) {
        this.autoStartBreaks = autoStartBreaks;
    }

    public void setAutoStartFocus(boolean autoStartFocus) {
        this.autoStartFocus = autoStartFocus;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public int getFocusDuration() {
        return focusDuration;
    }

    public int getShortBreakDuration() {
        return shortBreakDuration;
    }

    public int getLongBreakDuration() {
        return longBreakDuration;
    }

    public boolean isAutoStartBreaks() {
        return autoStartBreaks;
    }

    public boolean isAutoStartFocus() {
        return autoStartFocus;
    }

    public static class Builder {
        private final String userId;
        private int focusDuration = 25;
        private int shortBreakDuration = 5;
        private int longBreakDuration = 15;
        private boolean autoStartBreaks = false;
        private boolean autoStartFocus = false;

        public Builder(String userId) {
            this.userId = userId;
        }

        public Builder focusDuration(int focusDuration) {
            this.focusDuration = focusDuration;
            return this;
        }

        public Builder shortBreakDuration(int shortBreakDuration) {
            this.shortBreakDuration = shortBreakDuration;
            return this;
        }

        public Builder longBreakDuration(int longBreakDuration) {
            this.longBreakDuration = longBreakDuration;
            return this;
        }

        public Builder autoStartBreaks(boolean autoStartBreaks) {
            this.autoStartBreaks = autoStartBreaks;
            return this;
        }

        public Builder autoStartFocus(boolean autoStartFocus) {
            this.autoStartFocus = autoStartFocus;
            return this;
        }

        public UserSettings build() {
            return new UserSettings(
                userId,
                focusDuration,
                shortBreakDuration,
                longBreakDuration,
                autoStartBreaks,
                autoStartFocus);
        }
    }
}
