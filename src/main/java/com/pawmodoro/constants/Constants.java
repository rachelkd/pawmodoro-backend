package com.pawmodoro.constants;

/**
 * Constants used throughout the program.
 * Contains HTTP headers, API endpoints, JSON field names, and error messages used in API requests and responses.
 */
public final class Constants {
    private Constants() {
        // Prevent instantiation
    }

    /**
     * HTTP related constants for headers, content types, and query parameters.
     */
    public static final class Http {
        public static final String CONTENT_TYPE_JSON = "application/json";
        public static final String CONTENT_TYPE_HEADER = "Content-Type";
        public static final String API_KEY_HEADER = "apikey";
        public static final String AUTH_HEADER = "Authorization";
        public static final String BEARER_PREFIX = "Bearer ";
        public static final String PREFER_HEADER = "Prefer";
        public static final String PREFER_REPRESENTATION = "return=representation";
        public static final String PREFER_MINIMAL = "return=minimal";
        public static final String QUERY_START = "?";
        public static final String QUERY_EQUALS = "=eq.";
        public static final String SELECT_PARAM = "select=";
        public static final String LIMIT_PARAM = "limit=";
        public static final String AND_OPERATOR = "&";

        private Http() {
            // Prevent instantiation
        }
    }

    /**
     * JSON field names used in API requests and responses for data serialization.
     */
    public static final class JsonFields {
        // User-related fields
        public static final String ID_FIELD = "id";
        public static final String EMAIL_FIELD = "email";
        public static final String PASSWORD_FIELD = "password";
        public static final String USERNAME_FIELD = "username";
        public static final String ACCESS_TOKEN_FIELD = "access_token";
        public static final String REFRESH_TOKEN_FIELD = "refresh_token";
        public static final String USER_FIELD = "user";
        public static final String MSG_FIELD = "msg";
        public static final String EMPTY_ARRAY = "[]";
        public static final String EXPIRES_IN_FIELD = "expires_in";
        public static final String EXPIRES_AT_FIELD = "expires_at";

        // Cat-related fields
        public static final String CAT_NAME = "cat_name";
        public static final String OWNER_USERNAME = "owner_username";
        public static final String HAPPINESS_LEVEL = "happiness_level";
        public static final String HUNGER_LEVEL = "hunger_level";
        public static final String IMAGE_FILE_NAME = "image_file_name";
        public static final String CAT_NAME_LOWER = "cat_name_lower";
        public static final String OWNER_USERNAME_LOWER = "owner_username_lower";

        // Settings-related fields
        public static final String FOCUS_DURATION = "focus_duration";
        public static final String SHORT_BREAK_DURATION = "short_break_duration";
        public static final String LONG_BREAK_DURATION = "long_break_duration";
        public static final String AUTO_START_BREAKS = "auto_start_breaks";
        public static final String AUTO_START_FOCUS = "auto_start_focus";

        private JsonFields() {
            // Prevent instantiation
        }
    }

    /**
     * Supabase API endpoints for authentication and data access operations.
     */
    public static final class Endpoints {
        public static final String AUTH_SIGNUP_ENDPOINT = "/auth/v1/signup";
        public static final String AUTH_SIGNIN_ENDPOINT = "/auth/v1/token?grant_type=password";
        public static final String AUTH_SIGNOUT_ENDPOINT = "/auth/v1/logout";
        public static final String AUTH_PASSWORD_RESET = "/auth/v1/user/password";
        public static final String AUTH_REFRESH_ENDPOINT = "/auth/v1/token?grant_type=refresh_token";
        public static final String USER_PROFILES_ENDPOINT = "/rest/v1/user_profiles";
        public static final String USER_SETTINGS_ENDPOINT = "/rest/v1/user_settings";
        public static final String AUTH_USERS_ENDPOINT = "/auth/v1/user";
        public static final String CATS_ENDPOINT = "/rest/v1/cats";
        public static final String LOGIN_LOOKUP_ENDPOINT = "/rest/v1/login_lookup";

        private Endpoints() {
            // Prevent instantiation
        }
    }

    /**
     * Error messages used throughout the application.
     */
    public static final class ErrorMessages {
        // Format strings
        public static final String ERROR_MESSAGE_FORMAT = ": %s";

        // User Not Found errors
        public static final String AUTH_USER_NOT_FOUND = "User not found: %s";
        public static final String AUTH_PROFILE_NOT_FOUND = "User profile not found";

        // Authentication errors
        public static final String AUTH_USER_NOT_AUTHENTICATED = "User not authenticated";
        public static final String AUTH_FAILED_PASSWORD_UPDATE = "Failed to update password";
        public static final String AUTH_TOKEN_REQUIRED = "Authorization token is required";
        public static final String AUTH_TOKEN_INVALID = "Invalid or expired access token";

        // Database access errors
        public static final String DB_FAILED_ACCESS = "Failed to access database";
        public static final String DB_FAILED_CREATE_PROFILE = "Failed to create user profile: %s";
        public static final String DB_FAILED_CREATE_SETTINGS = "Failed to create user settings: %s";
        public static final String DB_FAILED_RETRIEVE_CATS = "Failed to retrieve cats: %s";
        public static final String DB_FAILED_SAVE_CAT = "Failed to save cat: %s";
        public static final String DB_FAILED_CHECK_CAT_EXISTS = "Failed to check cat existence: %s";
        public static final String DB_DUPLICATE_CAT_NAME = "A cat with this name already exists for this user";

        // Cat update errors
        public static final String CAT_UPDATE_UNAUTHORIZED = "You are not authorized to update this cat";
        public static final String CAT_NOT_FOUND = "Cat not found with the name %s for user %s";
        public static final String CAT_UPDATE_NO_DATA = "No data returned after updating cat %s for user %s";
        public static final String CAT_UPDATE_PARSE_ERROR = "Failed to parse update response for cat %s: %s";
        public static final String CAT_UPDATE_FAILED = "Failed to update cat: %s";
        public static final String CAT_CREATE_FAILED = "Failed to create cat: %s";
        public static final String CAT_CREATE_UNAUTHORIZED = "You are not authorized to create a cat for user: %s";

        private ErrorMessages() {
            // Prevent instantiation
        }
    }

    /**
     * Validation constants used for input validation throughout the application.
     */
    public static final class ValidationConstants {
        public static final int MIN_USERNAME_LENGTH = 3;
        public static final int MAX_USERNAME_LENGTH = 50;
        public static final int MIN_PASSWORD_LENGTH = 6;
        public static final int MAX_PASSWORD_LENGTH = 50;
        public static final int MAX_CAT_NAME_LENGTH = 20;
        public static final String LETTERS_ONLY_PATTERN = "^[a-zA-Z]+$";
        public static final String CAT_IMAGE_PATTERN = "^cat-[1-5]\\.png$";

        private ValidationConstants() {
            // Prevent instantiation
        }
    }

    /**
     * Constants for cat stats.
     */
    public static final class CatStats {
        /** Base percentage increase for happiness after a study session (5%). */
        public static final double BASE_HAPPINESS_PERCENTAGE = 0.05;

        /** Additional percentage for cats with low happiness (below threshold) (10%). */
        public static final double LOW_HAPPINESS_BONUS_PERCENTAGE = 0.10;

        /** Threshold below which cats get bonus happiness increase. */
        public static final int LOW_HAPPINESS_THRESHOLD = 30;

        /** Maximum happiness level a cat can have. */
        public static final int MAX_HAPPINESS_LEVEL = 100;

        /** Minimum happiness increase per study session. */
        public static final int MIN_HAPPINESS_INCREASE = 2;

        /** Percentage by which to decrease cat stats when skipping a session (10%). */
        public static final double SKIP_PENALTY_PERCENTAGE = 0.1;

        private CatStats() {
            // Prevent instantiation
        }
    }
}
