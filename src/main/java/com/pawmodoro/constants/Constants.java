package com.pawmodoro.constants;

import java.awt.Font;

/**
 * Constants used throughout the program.
 */
public final class Constants {
    // Global constants
    public static final String FONT_FAMILY = Font.SANS_SERIF;
    // Timer constants
    public static final int DEFAULT_WORK_MINUTES = 1;
    public static final int DEFAULT_BREAK_MINUTES = 1;
    public static final int MINUTES_TO_SECONDS = 60;
    public static final int SECONDS_TO_MILLIS = 1000;
    public static final long SECONDS_TO_MILLIS1 = 1000L;
    public static final int DEFAULT_WORK_DURATION_MS = DEFAULT_WORK_MINUTES * MINUTES_TO_SECONDS * SECONDS_TO_MILLIS;
    public static final int DEFAULT_BREAK_DURATION_MS = DEFAULT_BREAK_MINUTES * MINUTES_TO_SECONDS * SECONDS_TO_MILLIS;

    public static final String STATUS_STOPPED = "STOPPED";
    public static final String STATUS_RUNNING = "RUNNING";
    public static final String INTERVAL_WORK = "WORK";
    public static final String INTERVAL_BREAK = "BREAK";

    public static final int TIMER_FONT_SIZE = 48;
    public static final int TIMER_VERTICAL_SPACING = 20;
    public static final String TIME_FORMAT_PATTERN = "mm:ss";

    // UI Constants
    public static final int COMPONENT_VERTICAL_SPACING = 10;
    public static final int POPUP_FONT_SIZE = 14;

    // BreakSession and StudySession Constants
    public static final int DEFAULT_BUTTON_SIZE_W = 150;
    public static final int DEFAULT_BUTTON_SIZE_H = 40;

    // Cat Constants
    public static final int MAX_AMOUNT_OF_CATS = 4;

    // Cat Facts API Constants
    public static final String CAT_FACT_API_URL = "https://catfact.ninja/fact";
    public static final String CAT_FACT_ERROR_NETWORK = "Could not fetch cat fact. Check your internet connection!";
    public static final String CAT_FACT_ERROR_API = "Could not fetch cat fact. Try again later!";
    public static final int CAT_FACT_MAX_LENGTH = 50;

    // Happiness points for study session lengths
    public static final int POINTS_FOR_LESS_EQUAL_20 = 10;
    public static final int POINTS_FOR_BETWEEN_21_AND_40 = 20;
    public static final int POINTS_FOR_BETWEEN_41_AND_59 = 35;
    public static final int POINTS_FOR_60 = 40;

    // Hunger decreases for study session lengths
    public static final int HUNGER_FOR_LESS_EQUAL_20 = 10;
    public static final int HUNGER_FOR_BETWEEN_21_AND_40 = 20;
    public static final int HUNGER_FOR_BETWEEN_41_AND_59 = 25;
    public static final int HUNGER_FOR_60 = 30;

    // Study intervals for points
    public static final int MINUTES_20 = 20;
    public static final int MINUTES_40 = 40;
    public static final int MINUTES_60 = 60;

    // Cat Image constants
    public static final int CAT_IMAGE_MAX_WIDTH = 300;
    public static final int CAT_IMAGE_MAX_HEIGHT = 150;
    public static final int CAT_IMAGE_SIZE = 300;

    // Signup, Login, Loggedin view model constants
    public static final int SPACING = 40;
    public static final int TITLE = 25;
    public static final int INSET = 10;

    public static final int DISPLAY_CAT_REFRESH_BUTTON_WIDTH = 100;
    public static final int DISPLAY_CAT_REFRESH_BUTTON_HEIGHT = 30;

    // Display cat stats view constants
    public static final int DISPLAY_CAT_STATS_PADDING = 10;
    public static final int DISPLAY_CAT_STATS_FONT_SIZE = 16;
    public static final int DISPLAY_CAT_STATS_IMAGE_SIZE = 128;
    public static final int DISPLAY_CAT_STATS_MAX_WIDTH = 360;
    public static final int DISPLAY_CAT_STATS_HEIGHT = 360;

    // Set up study session view constants
    public static final int SETUP_SESSION_NORMAL_FONT_SIZE = 16;
    public static final int SETUP_SESSION_TITLE_FONT_SIZE = 20;
    // Adoption view constants
    public static final int ADOPTION_VIEW_WIDTH = 360;
    public static final int ADOPTION_VIEW_HEIGHT = 360;

    // Runaway cat view constants
    public static final int RUNAWAYCAT_VIEW_WIDTH = 380;
    public static final int RUNAWAYCAT_VIEW_HEIGHT = 380;

    // Cat sprite constants
    public static final int CAT_SPRITE_BASE_SIZE = 32;
    public static final int CAT_SPRITE_SCALE = 2;
    public static final int CAT_SPRITE_DISPLAY_SIZE = CAT_SPRITE_BASE_SIZE * CAT_SPRITE_SCALE;

    // Cat fact view constants
    public static final int CAT_FACT_TEXT_WIDTH = 240;
    public static final String CAT_FACT_TITLE = "Did You Know?";
    public static final String CAT_FACT_LOADING = "Loading cat fact...";
    public static final String CAT_FACT_ERROR_PREFIX = "Error: ";
    public static final int CAT_FACT_FONT_SIZE = 14;

    // Music Player constants
    public static final int DEFAULT_VOLUME = 50;

    // Common UI strings
    public static final String LOADING = "Loading...";
    public static final String NO_IMAGE_AVAILABLE = "No Image Available";
    public static final String ERROR = "Error";
    public static final String IMAGE_LOAD_ERROR = "Failed to load image";
    public static final String SUCCESS = "Success";
    public static final String CANCEL = "Cancel";
    public static final String OK = "OK";

    public static final String DEFAULT_CAT_NAME = "Pawmo";

    // Adoption label
    public static final String ADOPTION_LABEL = "Adopt a new cat!";

    // Adoption panel
    public static final int COLUMN_SIZE = 15;

    private Constants() {
        // Prevent instantiation
    }
}
