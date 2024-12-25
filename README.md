# Pawmodoro Backend API

This is the backend service for the Pawmodoro application.

## Table of Contents

- [Tech Stack](#tech-stack)
- [API Endpoints](#api-endpoints)
    - [Authentication](#authentication)
        - [Login](#login)
        - [Signup](#signup)
        - [Logout](#logout)
        - [Refresh Token](#refresh-token)
    - [User Settings](#user-settings)
        - [Get User Settings](#get-user-settings)
        - [Update User Settings](#update-user-settings)
    - [Cats](#cats)
        - [Get All Cats](#get-all-cats)
        - [Create Cat](#create-cat)
        - [Delete Cat](#delete-cat)
        - [Update Cat Happiness](#update-cat-happiness)
        - [Update Cat Hunger](#update-cat-hunger)
- [Authentication Details](#authentication)
- [Database](#database)
- [Running Locally](#running-locally)

## Tech Stack

- Java 21
- Spring Boot 3.4.1
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
GET /api/cats/{username}
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

#### Create Cat

```http
POST /api/cats/{username}
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**

```json
{
    "name": "string",  // Required, letters only, 1-20 chars
    "imageFileName": "string"  // Optional, must match pattern "cat-[1-5].png"
}
```

**Responses:**

- `201 CREATED`: Cat created successfully

```json
{
    "catName": "string",
    "ownerUsername": "string",
    "imageFileName": "string"
}
```

- `400 BAD_REQUEST`: Invalid input

```json
{
    "name": "Cat name must contain only letters"
}
```

```json
{
    "imageFileName": "Image file name must be in format 'cat-[1-5].png'"
}
```

- `401 UNAUTHORIZED`: Missing or invalid token

```json
{
    "message": "Authorization token is required"
}
```

- `403 FORBIDDEN`: Not authorized to create a cat for this user

```json
{
    "message": "You are not authorized to create a cat for user 'string'"
}
```

- `409 CONFLICT`: Cat name already exists for user

```json
{
    "message": "A cat with this name already exists for this user"
}
```

#### Delete Cat

```http
DELETE /api/cats/{username}
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**

```json
{
    "name": "string"  // Required, letters only, 1-20 chars
}
```

**Responses:**

- `200 OK`: Cat deleted successfully

```json
{
    "message": "Cat deleted successfully",
    "catName": "string"
}
```

- `400 BAD_REQUEST`: Invalid input

```json
{
    "name": "Cat name must be between 1 and 20 characters"
}
```

- `401 UNAUTHORIZED`: Missing or invalid token

```json
{
    "message": "Authorization token is required"
}
```

#### Update Cat Happiness

```http
PUT /api/cats/{ownerUsername}/{catName}/happiness
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**

```json
{
    "changeAmount": number  // Required, positive or negative integer
}
```

**Responses:**

- `200 OK`: Happiness updated successfully

```json
{
    "catName": "string",
    "ownerUsername": "string",
    "happinessLevel": number,
    "hungerLevel": number,
    "imageFileName": "string"
}
```

- `400 BAD_REQUEST`: Invalid input

```json
{
    "changeAmount": "Change amount is required"
}
```

- `401 UNAUTHORIZED`: Missing or invalid token

```json
{
    "message": "Authorization token is required"
}
```

- `403 FORBIDDEN`: Not authorized to update this cat

```json
{
    "message": "You are not authorized to update this cat's happiness level"
}
```

- `404 NOT_FOUND`: Cat not found

```json
{
    "message": "Cat not found with the name 'string' for user 'string'"
}
```

#### Update Cat Hunger

```http
PUT /api/cats/{ownerUsername}/{catName}/hunger
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**

```json
{
    "changeAmount": number  // Required, positive or negative integer
}
```

**Responses:**

- `200 OK`: Hunger updated successfully

```json
{
    "catName": "string",
    "ownerUsername": "string",
    "happinessLevel": number,
    "hungerLevel": number,
    "imageFileName": "string"
}
```

- `400 BAD_REQUEST`: Invalid input

```json
{
    "changeAmount": "Change amount is required"
}
```

- `401 UNAUTHORIZED`: Missing or invalid token

```json
{
    "message": "Authorization token is required"
}
```

- `403 FORBIDDEN`: Not authorized to update this cat

```json
{
    "message": "You are not authorized to update this cat's hunger level"
}
```

- `404 NOT_FOUND`: Cat not found

```json
{
    "message": "Cat not found with the name 'string' for user 'string'"
}
```

## Authentication Details

The API uses Supabase for authentication. All endpoints except login and signup require:

1. A valid Supabase session token in the `Authorization` header
2. The Supabase project's anon key in the `apikey` header

## Database

The application uses Supabase with the following main tables:

- `auth.users`: Authentication data
- `public.user_profiles`: User profile information
- `public.cats`: Virtual pet data
- `public.user_settings`: User preferences and settings
- `public.user_statistics`: Logs of user activity

## Running Locally

1. Ensure Java 21 is installed
2. Set up environment variables:
    - `SUPABASE_URL`
    - `SUPABASE_ANON_KEY`
3. Run the application:

```bash
./mvnw spring-boot:run
```

The server will start on `http://localhost:8080`
