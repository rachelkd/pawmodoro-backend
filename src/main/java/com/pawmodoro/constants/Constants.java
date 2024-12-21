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
        public static final String USER_FIELD = "user";
        public static final String MSG_FIELD = "msg";
        public static final String EMPTY_ARRAY = "[]";

        // Cat-related fields
        public static final String CAT_NAME = "cat_name";
        public static final String OWNER_USERNAME = "owner_username";
        public static final String HAPPINESS_LEVEL = "happiness_level";
        public static final String HUNGER_LEVEL = "hunger_level";
        public static final String IMAGE_FILE_NAME = "image_file_name";

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
        public static final String USER_PROFILES_ENDPOINT = "/rest/v1/user_profiles";
        public static final String USER_SETTINGS_ENDPOINT = "/rest/v1/user_settings";
        public static final String AUTH_USERS_ENDPOINT = "/auth/v1/user";
        public static final String CATS_ENDPOINT = "/rest/v1/cats";

        private Endpoints() {
            // Prevent instantiation
        }
    }

    /**
     * Error messages used throughout the application.
     */
    public static final class ErrorMessages {
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
        public static final String DB_FAILED_RETRIEVE_CATS = "Failed to retrieve cats: %s";

        private ErrorMessages() {
            // Prevent instantiation
        }
    }

    /**
     * HTTP status codes used in the application.
     */
    public static final class StatusCodes {
        public static final int UNAUTHORIZED = 401;

        private StatusCodes() {
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

        private ValidationConstants() {
            // Prevent instantiation
        }
    }
}
