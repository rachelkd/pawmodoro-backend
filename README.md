# Pawmodoro Backend API

This is the backend service for the Pawmodoro application, a gamified study timer with virtual pet care mechanics.

## Tech Stack
- Java 21
- Spring Boot 3.4.0
- Supabase (Database)

## API Endpoints

### Authentication

#### Login
```http
POST /api/users/login
Content-Type: application/json
```

**Request Body:**
```json
{
    "username": "string",
    "password": "string"
}
```

**Responses:**
- `200 OK`: Login successful
```json
{
    "accessToken": "string",
    "refreshToken": "string",
    "username": "string"
}
```
- `401 UNAUTHORIZED`: Invalid credentials
```json
{
    "message": "Wrong password"
}
```
- `400 BAD_REQUEST`: Invalid input
```json
{
    "username": "Username can only contain letters, numbers, dots, underscores, and hyphens"
}
```

#### Signup
```http
POST /api/users/signup
Content-Type: application/json
```

**Request Body:**
```json
{
    "username": "string",
    "email": "string",
    "password": "string",
    "confirmPassword": "string"
}
```

**Responses:**
- `200 OK`: Signup successful
```json
{
    "success": true,
    "token": "string",
    "message": "User registered successfully",
    "username": "string"
}
```
- `400 BAD_REQUEST`: Invalid input or user already exists
```json
{
    "message": "Username already taken"
}
```

#### Logout
```http
POST /api/users/logout
Authorization: Bearer <token>
```

**Responses:**
- `200 OK`: Logout successful
```json
{
    "success": true,
    "message": "Successfully logged out"
}
```
- `401 UNAUTHORIZED`: Invalid or expired token
```json
{
    "message": "Invalid or expired access token"
}
```

#### Refresh Token
```http
POST /api/users/refresh
Content-Type: application/json
```

**Request Body:**
```json
{
    "refreshToken": "string"
}
```

**Responses:**
- `200 OK`: Token refresh successful
```json
{
    "accessToken": "string",
    "refreshToken": "string",
    "expiresIn": number,
    "expiresAt": number
}
```
- `404 NOT FOUND`: Invalid refresh token
```json
{
    "message": "Invalid Refresh Token: Refresh Token Not Found"
}
```

```json
{
    "message": "Invalid Refresh Token: Already Used"
}
```

### User Settings

#### Get User Settings
```http
GET /api/settings/{username}
Authorization: Bearer <token>
```

**Responses:**
- `200 OK`: Successfully retrieved settings
```json
{
    "username": "string",
    "focusDuration": number,
    "shortBreakDuration": number,
    "longBreakDuration": number,
    "autoStartBreaks": boolean,
    "autoStartFocus": boolean
}
```
- `401 UNAUTHORIZED`: Invalid or expired token
```json
{
    "message": "Invalid or expired access token"
}
```
- `404 NOT_FOUND`: User not found
```json
{
    "message": "User not found: username"
}
```

#### Update User Settings
```http
PUT /api/settings/{username}
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**
```json
{
    "focusDuration": number,
    "shortBreakDuration": number,
    "longBreakDuration": number,
    "autoStartBreaks": boolean,
    "autoStartFocus": boolean
}
```

**Responses:**
- `200 OK`: Settings updated successfully
```json
{
    "username": "string",
    "focusDuration": number,
    "shortBreakDuration": number,
    "longBreakDuration": number,
    "autoStartBreaks": boolean,
    "autoStartFocus": boolean
}
```
- `401 UNAUTHORIZED`: Invalid or expired token
```json
{
    "message": "Invalid or expired access token"
}
```
- `404 NOT_FOUND`: User not found
```json
{
    "message": "User not found: username"
}
```

### Cats

#### Get All Cats
```http
GET /api/cats/user/{username}
Authorization: Bearer <token>
```

**Responses:**
- `200 OK`: Successfully retrieved cats
```json
{
    "success": true,
    "cats": [
        {
            "name": "string",
            "ownerUsername": "string",
            "happinessLevel": number,
            "hungerLevel": number,
            "imageFileName": "string"
        }
    ],
    "message": "Successfully retrieved cats"
}
```
- `400 BAD_REQUEST`: Invalid username
```json
{
    "message": "Username cannot be null or empty"
}
```
- `401 UNAUTHORIZED`: Missing or invalid token
```json
{
    "message": "Authorization token is required"
}
```

## Authentication
The API uses Supabase for authentication. All endpoints except login and signup require:
1. A valid Supabase session token in the `Authorization` header
2. The Supabase project's anon key in the `apikey` header

## Database
The application uses Supabase with the following main tables:
- `auth.users`: Authentication data
- `public.user_profiles`: User profile information
- `public.cats`: Virtual pet data
- `public.user_settings`: User preferences and settings

## Running Locally
1. Ensure Java 21 is installed
2. Set up environment variables:
   - `SUPABASE_URL`
   - `SUPABASE_KEY`
3. Run the application:
```bash
./mvnw spring-boot:run
```

The server will start on `http://localhost:8080`
