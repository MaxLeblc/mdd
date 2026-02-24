# MDD - Monde de Dév

A social network platform for developers to share, discover, and discuss technology topics.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)

## Overview

MDD (Monde de Dév) is a full-stack social network application built for developers. It allows users to subscribe to technology topics, create posts, comment on discussions, and manage their profile preferences.

## Features

- **User Authentication**
  - JWT-based secure authentication
  - User registration and login
  - Automatic authentication after registration

- **Topics Management**
  - Browse available technology topics
  - Subscribe/Unsubscribe to topics of interest
  - Personalized topic feed

- **Posts & Discussions**
  - Create posts under specific topics
  - View posts from subscribed topics
  - Sort posts by date (ascending/descending)
  - Detailed post view with comments

- **Comments**
  - Add comments to posts
  - View all comments on a post
  - Author information for each comment

- **User Profile**
  - Update username and email
  - Change password
  - View and manage topic subscriptions

## Tech Stack

### Backend

- **Java 21**
- **Spring Boot 4.0.2**
  - Spring Web
  - Spring Data JPA
  - Spring Security
- **PostgreSQL 17** (Database)
- **JWT** (io.jsonwebtoken 0.12.6)
- **MapStruct 1.6.3** (DTO mapping)
- **Lombok** (Boilerplate reduction)
- **Swagger/OpenAPI** (API documentation)
- **Maven** (Build tool)

### Frontend

- **Angular 21** (Standalone components)
- **Angular Material 21.1.3** (UI components)
- **RxJS 7.8** (Reactive programming)
- **TypeScript 5.9**
- **Vitest 4.0** (Testing)

### DevOps

- **Docker Compose** (Database containerization)
- **PostgreSQL Alpine** (Lightweight database image)

## Architecture

The application follows a **3-tier architecture**:

1. **Presentation Layer** (Frontend)
   - Angular 21 with standalone components
   - Reactive forms and signals
   - Material Design UI
   - JWT interceptor for authenticated requests

2. **Application Layer** (Backend)
   - RESTful API endpoints
   - JWT authentication & authorization
   - Business logic in service layer
   - DTO pattern for request/response handling

3. **Data Layer**
   - PostgreSQL relational database
   - JPA/Hibernate ORM
   - Entity relationships (User, Topic, Post, Comment)

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21** or higher
- **Node.js 18+** and **npm 11+**
- **Maven 3.8+**
- **Docker** and **Docker Compose**
- **Git**

## Installation

### 1. Clone the repository

```bash
git clone <repository-url>
cd mdd
```

### 2. Backend Setup

```bash
cd backend
mvn clean install
```

### 3. Frontend Setup

```bash
cd frontend
npm install
```

## ⚙️ Configuration

### Backend Configuration

Create a `.env` file in the project root directory:

```env
# Database Configuration
DATABASE_URL=jdbc:postgresql://localhost:5432/mdd
DATABASE_USERNAME=your_postgres_user
DATABASE_PASSWORD=your_postgres_password
DATABASE_PORT=5432:5432

# JWT Configuration
JWT_SECRET=your_jwt_secret_key_here_at_least_256_bits
JWT_EXPIRATION=86400000
```

> **Security Note**: Never commit your `.env` file. The JWT secret should be a strong, randomly generated key.

### Frontend Configuration

The frontend environment configuration is located in `frontend/src/environments/`:

- `environment.ts` - Development configuration
- `environment.prod.ts` - Production configuration

Default API URL: `http://localhost:8080`

## Running the Application

### 1. Start the Database

```bash
docker-compose up -d
```

This will start a PostgreSQL database container on port 5432.

### 2. Start the Backend

```bash
cd backend
mvn spring-boot:run
```

The backend API will be available at `http://localhost:8080`

### 3. Start the Frontend

```bash
cd frontend
npm start
```

The frontend application will be available at `http://localhost:4200`

## API Documentation

Once the backend is running, you can access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### Main API Endpoints

#### Authentication

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and receive JWT token

#### Users

- `GET /api/users/{id}` - Get user profile
- `PUT /api/users/{id}` - Update user profile
- `POST /api/users/{id}/subscribe/{topicId}` - Subscribe to a topic
- `POST /api/users/{id}/unsubscribe/{topicId}` - Unsubscribe from a topic

#### Topics

- `GET /api/topics` - Get all available topics

#### Posts

- `GET /api/posts` - Get all posts
- `GET /api/posts/feed` - Get posts from subscribed topics
- `GET /api/posts/{id}` - Get a specific post
- `POST /api/posts` - Create a new post

#### Comments

- `GET /api/posts/{postId}/comments` - Get all comments for a post
- `POST /api/posts/{postId}/comments` - Add a comment to a post

## Project Structure

```
mdd/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/openclassrooms/mdd/
│   │   │   │   ├── config/          # Configuration classes
│   │   │   │   ├── controllers/     # REST controllers
│   │   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── mappers/         # MapStruct mappers
│   │   │   │   ├── models/          # JPA entities
│   │   │   │   ├── repository/      # JPA repositories
│   │   │   │   ├── security/        # Security configuration & JWT
│   │   │   │   └── services/        # Business logic
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/                    # Unit tests
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   │   ├── core/               # Core functionality (interceptors)
│   │   │   ├── features/           # Feature modules (components)
│   │   │   │   ├── auth/           # Authentication components
│   │   │   │   ├── header/         # Header component
│   │   │   │   ├── home/           # Home page
│   │   │   │   ├── posts/          # Post list and details
│   │   │   │   ├── topics/         # Topic list
│   │   │   │   └── user-profil/    # User profile
│   │   │   ├── interfaces/         # TypeScript interfaces
│   │   │   ├── models/             # Data models
│   │   │   ├── services/           # HTTP services
│   │   │   └── shared/             # Shared components
│   │   ├── environments/           # Environment configurations
│   │   └── styles.scss             # Global styles
│   └── package.json
│
├── cours/                          # Course documentation (French)
├── docker-compose.yml              # Database container configuration
└── README.md
```

## Database Schema

### Main Entities

- **User**: User accounts with authentication credentials
- **Topic**: Technology topics available for subscription
- **Post**: User-created posts under specific topics
- **Comment**: Comments on posts
- **User-Topic Subscription**: Many-to-many relationship

## Security

- Passwords are encrypted using BCrypt
- JWT tokens for stateless authentication
- Token expiration management
- Automatic logout on token expiration (401 errors)
- Protected API endpoints requiring authentication

## License

This project is part of an OpenClassrooms training program.
