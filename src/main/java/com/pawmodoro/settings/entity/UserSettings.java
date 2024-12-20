package com.pawmodoro.settings.entity;

/**
 * Represents the user settings for a Pawmodoro user.
 */
public class UserSettings {
    public static final int DEFAULT_FOCUS_DURATION = 25;
    public static final int DEFAULT_SHORT_BREAK_DURATION = 5;
    public static final int DEFAULT_LONG_BREAK_DURATION = 15;

    private final String username;
    private int focusDuration;
    private int shortBreakDuration;
    private int longBreakDuration;
    private boolean autoStartBreaks;
    private boolean autoStartFocus;

    public UserSettings(String username, int focusDuration, int shortBreakDuration,
        int longBreakDuration, boolean autoStartBreaks,
        boolean autoStartFocus) {
        this.username = username;
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
    public String getUsername() {
        return username;
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

    /**
     * Builder class for creating UserSettings objects.
     */
    public static class Builder {
        private final String username;
        private int focusDuration = DEFAULT_FOCUS_DURATION;
        private int shortBreakDuration = DEFAULT_SHORT_BREAK_DURATION;
        private int longBreakDuration = DEFAULT_LONG_BREAK_DURATION;
        private boolean autoStartBreaks;
        private boolean autoStartFocus;

        public Builder(String username) {
            this.username = username;
        }

        /**
         * Sets the focus duration.
         * @param newFocusDuration the focus duration to set
         * @return the builder instance
         */
        public Builder focusDuration(int newFocusDuration) {
            this.focusDuration = newFocusDuration;
            return this;
        }

        /**
         * Sets the short break duration.
         * @param newShortBreakDuration the short break duration to set
         * @return the builder instance
         */
        public Builder shortBreakDuration(int newShortBreakDuration) {
            this.shortBreakDuration = newShortBreakDuration;
            return this;
        }

        /**
         * Sets the long break duration.
         * @param newLongBreakDuration the long break duration to set
         * @return the builder instance
         */
        public Builder longBreakDuration(int newLongBreakDuration) {
            this.longBreakDuration = newLongBreakDuration;
            return this;
        }

        /**
         * Sets the auto start breaks flag.
         * @param newAutoStartBreaks the auto start breaks flag to set
         * @return the builder instance
         */
        public Builder autoStartBreaks(boolean newAutoStartBreaks) {
            this.autoStartBreaks = newAutoStartBreaks;
            return this;
        }

        /**
         * Sets the auto start focus flag.
         * @param newAutoStartFocus the auto start focus flag to set
         * @return the builder instance
         */
        public Builder autoStartFocus(boolean newAutoStartFocus) {
            this.autoStartFocus = newAutoStartFocus;
            return this;
        }

        /**
         * Builds the UserSettings object.
         * @return the UserSettings object
         */
        public UserSettings build() {
            return new UserSettings(
                username,
                focusDuration,
                shortBreakDuration,
                longBreakDuration,
                autoStartBreaks,
                autoStartFocus);
        }
    }
}
