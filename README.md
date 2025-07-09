# 🏡 Airbnb Backend (Spring Boot)

## 📘 Project Overview

This project is a backend API for an Airbnb-like platform built with **Spring Boot**. It allows **hosts** to create and manage listings (houses), while **users** can browse and book available houses. The platform supports **email/password login** as well as **OAuth login** via **GitHub** and **Google**. Upon important actions like registration or booking, users receive **email notifications**.
![Java Version](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen)
![Build](https://img.shields.io/badge/Build-Maven-blueviolet)
![License](https://img.shields.io/badge/License-MIT-yellow)
![Dockerized](https://img.shields.io/badge/Docker-Supported-blue)
![Status](https://img.shields.io/badge/Status-Production%20Ready-green)

---

## ✨ Features

### 🔐 Authentication & Authorization

- Register and login with **email and password**
- OAuth2 login with **Google** and **GitHub**
- Role-based access control (Admin, Host, User)
- JWT-based authentication

### 🏘 Host Functionality

- Create new house listings
- Edit or delete their own listings
- View bookings made on their properties

### 🧳 User Functionality

- Browse available houses
- Book a house
- View their booking history

### 📧 Email Notifications

- Welcome email upon successful registration
- Booking confirmation email
---

## 🛠️ Tech Stack

| Layer              | Technology                                       |
|-------------------|--------------------------------------------------|
| **Language**       | Java 21                                          |
| **Framework**      | Spring Boot 3.5.3                                |
| **ORM**            | Spring Data JPA, Hibernate                       |
| **Database**       | PostgreSQL                                       |
| **Authentication** | Spring Security + OAuth2 (Google, GitHub)       |
| **Authorization**  | JWT (JJWT library)                               |
| **Validation**     | Hibernate Validator (JSR-380)                    |
| **Email Service**  | Spring Boot Starter Mail (`JavaMailSender`)     |
| **Template Engine**| Thymeleaf (used for email templates)            |
| **HTTP Clients**   | Spring Cloud OpenFeign                          |
| **API Docs**       | Swagger / Springdoc OpenAPI *(optional)*        |
| **Build Tool**     | Maven                                            |
| **Testing**        | JUnit 5, Spring Boot Test, Spring Security Test |
| **Utilities**      | Lombok                                           |


## 🚀 Starter Guide

### ✅ Prerequisites

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/)
- Java 21 JDK
- Maven

### 🔨 Build Tool

- **Maven** using `spring-boot-maven-plugin`

---

## ⚙️ Environment Configuration (`application-prod.yaml`)

| Category        | Key                           | Description                                                             |
|----------------|-------------------------------|-------------------------------------------------------------------------|
| **Profile**     | `ACTIVE_PROFILE`              | Active Spring profile (`dev`, `prod`, etc.)                             |
| **Google OAuth**| `GOOGLE_CLIENT_ID`            | Google OAuth client ID                                                  |
|                 | `GOOGLE_CLIENT_SECRET`        | Google OAuth client secret                                              |
| **GitHub OAuth**| `GITHUB_CLIENT_ID`            | GitHub OAuth client ID                                                  |
|                 | `GITHUB_CLIENT_SECRET`        | GitHub OAuth client secret                                              |
| **App Config**  | `APP_PORT`                    | Port the application will run on                                        |
| **Database**    | `POSTGRES_SERVICE`            | PostgreSQL service hostname (e.g., `localhost`, Docker service name)   |
|                 | `POSTGRES_PORT`               | PostgreSQL port (default: `5432`)                                       |
|                 | `POSTGRES_DB`                 | PostgreSQL database name                                                |
|                 | `POSTGRES_USER`               | PostgreSQL username                                                     |
|                 | `POSTGRES_PASS`               | PostgreSQL password                                                     |
| **JPA**         | `DDL_AUTO`                    | JPA schema generation strategy (`update`, `validate`, `create`, etc.)   |
| **Redirects**   | `REDIRECT_URL`                | Base redirect URL for OAuth flows                                       |
|                 | `REDIRECT_VERIFY`             | Redirect URL after account verification                                |
|                 | `ACCOUNT_CREATED_URL`         | URL used in account verification email                                  |
| **JWT**         | `JWT_EXP_ACCESS_TOKEN`        | Access token expiration time (e.g., `3600000` ms)                       |
|                 | `JWT_EXP_REFRESH_TOKEN`       | Refresh token expiration time (e.g., `604800000` ms)                    |
|                 | `JWT_SECRET`                  | Secret key used to sign JWT tokens                                      |
| **CORS**        | `ORIGINS`                     | Allowed origins (comma-separated URLs)                                  |
| **Admin User**  | `APP_EMAIL`                   | Default admin email address                                             |
|                 | `APP_PASSWORD`                | Default admin password                                                  |
|                 | `APP_USERNAME`                | Default admin username                                                  |
| **Email (SMTP)**| `MAIL_PORT`                   | SMTP server port                                                        |
|                 | `MAIL_HOST`                   | SMTP server hostname (e.g., `smtp.gmail.com`)                           |
|                 | `MAIL_USERNAME`               | Email address used for sending mails                                    |
|                 | `MAIL_PASSWORD`               | Email password or app-specific token                                    |
|                 | `MAIL_ENABLE`                 | Whether email sending is enabled (`true` or `false`)                    |
| **API Keys**    | `PEXEL_KEY`                   | API key for fetching images from Pexels                                 |

### 🧪 Sample `application.yaml`
```yaml
spring:
  application:
    name: airbnb-backend
  profiles:
    active: ${ACTIVE_PROFILE:dev}
  output:
    ansi:
      enabled: ALWAYS
  security:
    oauth2:
      client:
        registration:
          github:
            client-id: ${GITHUB_CLIENT_ID}
            client-secret: ${GITHUB_CLIENT_SECRET}
            scope: user:email
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
  datasource:
    url: jdbc:postgresql://${POSTGRES_SERVICE}:${POSTGRES_PORT}/${POSTGRES_DB}
    username: ${POSTGRES_USER}
    password: ${POSTGRES_PASS}
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: ${DDL_AUTO:create-drop}
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
  mail:
    host: ${MAIL_HOST}
    port: ${MAIL_PORT}
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
    default-encoding: UTF-8
    properties:
      mail:
        mime:
          charset: UTF-8
        smtp:
          writetimeout: 10000
          connectiontimeout: 10000
          timeout: 10000
          auth: true
          starttls:
            enable: true
            required: true
server:
  port: ${APP_PORT:8080}


custom:
  redirect-url: ${REDIRECT_URL}
  redirect-verify: ${REDIRECT_VERIFY}
  created-account: ${ACCOUNT_CREATED_URL}
  pexel-key: ${PEXEL_KEY}
  jwt:
    jwt-access-exp: ${JWT_EXP_ACCESS_TOKEN}
    jwt-refresh-exp: ${JWT_EXP_REFRESH_TOKEN}
    jwt-secret: ${JWT_SECRET}
  cors:
    origins: ${ORIGINS}
  admin:
    app-email: ${APP_EMAIL}
    app-password: ${APP_PASSWORD}
    app-username: ${APP_USERNAME}

  mail:
    enabled: ${MAIL_ENABLE}

```

## 🐳 Docker Compose Setup


| Variable             | Description                                           | Example Value                                         |
|----------------------|-------------------------------------------------------|-------------------------------------------------------|
| `POSTGRES_PASS`      | Password for the PostgreSQL database                 | `password`                                            |
| `POSTGRES_USER`      | Username for the PostgreSQL database                 | `admin`                                               |
| `POSTGRES_DB`        | Name of the PostgreSQL database                      | `airbnbDb`                                            |
| `POSTGRES_EXTERNAL_PORT` | External port mapped to PostgreSQL container     | `7000`                                                |
| `POSTGRES_INTERNAL_PORT` | Internal port PostgreSQL listens on              | `5432`                                                |
| `POSTGRES_SERVICE`   | Hostname or container name of the PostgreSQL service | `postgres_db_sc`                                      |
| `POSTGRES_PORT`      | Alias for internal port used by Spring               | `${POSTGRES_INTERNAL_PORT}`                           |
| `PG_EMAIL`           | Email for PgAdmin login                              | `example@gmail.com`                                   |
| `PG_PASS`            | Password for PgAdmin login                           | `password`                                            |
| `PG_EXTERNAL_PORT`   | External port mapped to PgAdmin container            | `8000`                                                |
| `PG_INTERNAL_PORT`   | Internal port PgAdmin listens on                     | `80`                                                  |
| `APP_PORT`           | Internal port Spring Boot app listens on             | `9090`                                                |
| `APP_PORT_EXTERNAL`  | External port mapped to Spring Boot container        | `9090`                                                |
| `ACTIVE_PROFILE`     | Active Spring profile                                | `prod`                                                |
| `GOOGLE_CLIENT_ID`   | Google OAuth client ID                               | `client`                                              |
| `GOOGLE_CLIENT_SECRET` | Google OAuth client secret                         | `secret`                                              |
| `GITHUB_CLIENT_ID`   | GitHub OAuth client ID                               | `client`                                              |
| `GITHUB_CLIENT_SECRET` | GitHub OAuth client secret                         | `secret`                                              |
| `DDL_AUTO`           | JPA schema generation strategy                       | `create`                                              |
| `REDIRECT_URL`       | Redirect URI after login (frontend)                  | `http://localhost:4200/logged-in`                     |
| `REDIRECT_VERIFY`    | Redirect URI after account verification              | `http://localhost:4200`                               |
| `ACCOUNT_CREATED_URL`| URL used for email account verification              | `http://localhost:9090/api/v1/users/verify?token=`    |
| `JWT_EXP_ACCESS_TOKEN` | JWT access token expiration (ms)                   | `3600000`                                             |
| `JWT_EXP_REFRESH_TOKEN`| JWT refresh token expiration (ms)                 | `8600000`                                             |
| `JWT_SECRET`         | Secret key used to sign JWT tokens                   | `your-secret-key`                                     |
| `ORIGINS`            | Allowed CORS origins (comma-separated)               | `http://localhost:4200,http://localhost:4300`         |
| `APP_EMAIL`          | Default admin email address                          | `example@gmail.com`                                   |
| `APP_PASSWORD`       | Default admin password                               | `password`                                            |
| `APP_USERNAME`       | Default admin username                               | `admin`                                               |
| `MAIL_PORT`          | SMTP server port                                     | `587`                                                 |
| `MAIL_HOST`          | SMTP server host                                     | `smtp.gmail.com`                                      |
| `MAIL_USERNAME`      | SMTP username (email sender)                         | `da`                                                  |
| `MAIL_PASSWORD`      | SMTP password or app password                        | `some key given by google or email service you using` |
| `MAIL_ENABLE`        | Enable or disable mail sending (`true/false`)        | `true`                                                |
| `PEXEL_KEY`          | API key for accessing Pexels image API               | `your-pexels-key`                                     |

---

### Important
- You **must create a `.env` file** in the root directory of your project.
- This `.env` file should define all the variables listed above.
- Docker Compose will automatically load these variables from the `.env` file during deployment.

### 🧪 Sample of docker-compose.yml
```yaml
services:
  postgres_db_sc:
    image: postgres
    restart: always
    environment:
      POSTGRES_PASSWORD: ${POSTGRES_PASS}
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_DB: ${POSTGRES_DB}
    ports:
      - "${POSTGRES_EXTERNAL_PORT}:${POSTGRES_INTERNAL_PORT}"
    volumes:
      - ./postgres-init:/docker-entrypoint-initdb.d
    networks:
      - airBnbNetwork
  pgadmin_sc:
    image: dpage/pgadmin4
    restart: always
    environment:
      PGADMIN_DEFAULT_EMAIL: ${PG_EMAIL}
      PGADMIN_DEFAULT_PASSWORD: ${PG_PASS}
    ports:
      - "${PG_EXTERNAL_PORT}:${PG_INTERNAL_PORT}"
    depends_on:
      - postgres_db_sc
    networks:
      - airBnbNetwork
  airbnb-spring-boot-app_sc:
    build: .
    depends_on:
      - postgres_db_sc
    ports:
      - "${APP_PORT_EXTERNAL}:${APP_PORT}"
    networks:
      - airBnbNetwork
    env_file:
      - ${ENV_FILE}

networks:
  airBnbNetwork:
    driver: bridge
```
## 🧪 Running the Application

You can start the  Airbnb backend using a simple script or manually with
Docker Compose.

---

### ✅ Recommended: Use the `prod.sh` Script

After building your project with Maven:
```bash
mvn clean install
```
This script will:
- Load environment variables from `.env`
- Use Docker Compose to spin up all required services
- Build and start the application in production mode

> **Note:** Make sure the `prod.sh` script is executable. If not, run:
>
> ```bash
> chmod +x prod.sh
> ```
## 🛠 Alternative: Manual Docker Compose Command

If you prefer running the application manually without the `prod.sh` script:

```bash
ENV_FILE=.env docker-compose --env-file .env up --build
```
## 🔧 Local Development (No Docker)

If you'd rather run the app directly on your machine:

1. Start PostgreSQL manually or via Docker.
2. Set up environment variables via `application-dev.yml` or your IDE.
3. Run the application:

```bash
mvn spring-boot:run
```
# ✅ REST API Documentation

## 👤 User API Endpoints

| HTTP Method | Endpoint                            | Description                                                       | Request Body           | Response Type                                  |
|-------------|-------------------------------------|-------------------------------------------------------------------|------------------------|-----------------------------------------------|
| `GET`       | `/api/v1/users`                     | Retrieve all registered users                                     | None                   | `HttpResponse<List<User>>`                    |
| `GET`       | `/api/v1/users/{userId}`            | Retrieve a single user by ID                                      | None                   | `HttpResponse<User>`                          |
| `POST`      | `/api/v1/users/register`            | Register a new user                                               | `UserDto`              | `HttpResponse<User>`                          |
| `POST`      | `/api/v1/users/login`               | Authenticate user and return JWT tokens                           | `LoginDto`             | `HttpResponse<Map<String, String>>`           |
| `GET`       | `/api/v1/users/auth`                | Get currently authenticated user                                  | None                   | `HttpResponse<User>`                          |
| `PATCH`     | `/api/v1/users`                     | Update authenticated user's profile                               | `UpdateUserDTO`        | `HttpResponse<User>`                          |
| `DELETE`    | `/api/v1/users`                     | Delete currently authenticated user's account                     | None                   | `HttpResponse<Void>`                          |
| `GET`       | `/api/v1/users/verify?token=...`    | Verify user account via token and redirect to frontend            | Query Param `token`    | Redirect (302)                                |
| `GET`       | `/api/v1/users/refresh/token`       | Refresh JWT access token using refresh token                      | Header: Bearer Token   | JSON stream to `HttpServletResponse`          |



## 🏡 Airbnb Listings API Endpoints

| HTTP Method | Endpoint                            | Description                                           | Request Body / Params                                 | Response Type                                |
|-------------|-------------------------------------|-------------------------------------------------------|--------------------------------------------------------|---------------------------------------------|
| `GET`       | `/api/v1/listings`                  | Retrieve all Airbnb listings                          | None                                                   | `HttpResponse<List<AirBnbResponseDto>>`     |
| `GET`       | `/api/v1/listings/{listingId}`      | Retrieve a single listing by ID                       | Path Variable: `listingId`                             | `HttpResponse<AirBnbResponseDto>`           |
| `POST`      | `/api/v1/listings`                  | Create a new listing with images                      | Multipart: `ListingDTO`, `main` (image), `gallery[]`   | `HttpResponse<AirBnbResponseDto>`           |
| `PATCH`     | `/api/v1/listings/{listingId}`      | Update an existing listing (partial, including images)| Multipart: `PartialListingUpdateDTO`, `main`, `gallery[]` | `HttpResponse<AirBnbResponseDto>`       |
| `DELETE`    | `/api/v1/listings/{listingId}`      | Delete a listing by ID                                | Path Variable: `listingId`                             | `HttpResponse<Void>`                        |


## 📅 Booking API Endpoints

| HTTP Method | Endpoint                 | Description                             | Request Body / Params            | Response Type                          |
|-------------|--------------------------|---------------------------------------|---------------------------------|--------------------------------------|
| `POST`      | `/api/v1/bookings`        | Create a new booking                   | `BookingDTO`                    | `HttpResponse<BookingResponseDTO>`   |
| `GET`       | `/api/v1/bookings`        | Retrieve all bookings                  | None                           | `HttpResponse<List<BookingResponseDTO>>` |
| `GET`       | `/api/v1/bookings/{id}`   | Retrieve a booking by ID               | Path Variable: `id`             | `HttpResponse<BookingResponseDTO>`   |

## 👤 Host API Endpoint

| HTTP Method | Endpoint         | Description                          | Request Body       | Response Type              |
|-------------|------------------|--------------------------------------|--------------------|----------------------------|
| `POST`      | `/api/v1/host`   | Register a user as a host            | `BecomeHostDto`    | `HttpResponse<Object>`     |


## 📨 Contact API Endpoint

| HTTP Method | Endpoint           | Description                          | Request Body     | Response Type          |
|-------------|--------------------|--------------------------------------|------------------|------------------------|
| `POST`      | `/api/v1/contact`  | Submit a contact form/message        | `ContactDto`     | `HttpResponse<Object>` |


