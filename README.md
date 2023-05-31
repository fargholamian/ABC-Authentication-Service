# ABC-Authentication-Service

Authentication Service 

## Overview

This project is an authentication service implemented using Spring Boot, specifically version 3.1. It provides a set of REST APIs for user registration, login, and user information retrieval. The service uses MySQL as the underlying database to store user information such as username, password, and user roles. The project utilizes various Spring frameworks and libraries including Spring Web, Spring Data JPA, and Spring Security. The recommended MySQL version for this project is 8.

## Functionality

The authentication service offers the following REST APIs:

1. **Register User** - `POST /api/auth/register`: This API allows users to create a new account by providing necessary registration details. The request body should follow the `RegistrationRequest` format.

    ```
    curl --location --request POST 'http://127.0.0.1:8081/api/auth/register' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "username" : "admin",
        "password" : "admin",
        "confirm_password": "admin"
    }'
    ```

2. **User Login** - `POST /api/auth/login`: Users can authenticate themselves and obtain a JWT (JSON Web Token) access token by using this API. The request body should adhere to the `AuthenticationRequest` format. The response includes the generated access token and a refresh token.
    ```
    curl --location --request POST 'http://localhost:8081/api/auth/login' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "username" : "admin",
    "password" : "admin"
    }'
    ```
3. **Get User Information** - `GET /api/user`: Other services can utilize this API to verify a JWT access token and retrieve user information. The endpoint requires a valid JWT token in the request header. Upon successful verification, it returns the requested user information.
    ```
    curl --location --request GET 'http://localhost:8081/api/user/' \
    --header 'Authorization: Bearer AUTH_TOKEN \
    ```

## Project Structure

The project follows a standard Spring Boot project structure:

- `controller`: This directory contains the controller classes responsible for handling requests. The `AuthenticationController` class handles registration and login requests, while the `UserController` class handles requests for retrieving user information. Additionally, the `GenericExceptionHandler` class serves as a generic exception handler for runtime exceptions.

- `enum`: The enumerations related to the project are stored here. The `TokenType` enumeration specifies the type of token (Auth_token or Refresh_token). Note that both tokens are generated, but the refresh_token flow is not implemented at the moment.

- `model`: All the Plain Old Java Object (PoJo) classes reside here. The `ApiError` class represents a generic exception response. The `AuthenticationRequest` class defines the structure of the login request body. The `RegistrationRequest` class defines the structure of the registration request body. The `AuthResponse` class contains the `auth_token` and `refresh_token` fields, serving as the response for login and registration requests. Finally, the `User` class represents the user model.

- `repo`: This directory includes the user repository model, which extends the `JpaRepository` interface.

- `security`: All the security-related classes and helper methods can be found here. The `SecurityConfig` class is responsible for configuring the AuthenticationManager and SecurityFilterChain. Both `/api/auth/login` and `/api/auth/register` endpoints are allowed without a token. Any other endpoint should have a valid JWT token; otherwise, access will be rejected. The `SecurityAuthenticationEntryPoint` class serves as a generic handler for any exception during security checks.

- `services`: All the service classes are located here. The `AuthenticationService` is responsible for extracting user information from the JWT token and providing services for login requests. The `UserService` handles user registration and implements the `UserDetailsService`, which is required by the Security chain.


## Prerequisites

To run this project, ensure that you have the following software installed:

- Java Development Kit (JDK) 17 or higher
- Gradle
- MySQL (tested with version 8)

## Getting Started
Please see the instruction in [abc-orchestrator](https://github.com/fargholamian/abc-orchestrator) which will help you to run all the services together.

Anyway, If you want to run this project independently, follow the steps below to set up and run the project:

1. Clone the repository to your local machine.
2. Open a terminal and navigate to the project's root directory.
3. Customize the application configurations in `application.yaml` to match your environment (e.g., database connection details).
4. Build and run the project using Maven:
   ```
   ./gradlew bootRun
   ```
The service will now be running and listening on port 8081.



