# Door Management System

A full-stack application for managing doors and user access within a company. This system allows administrators to manage doors, users, and access relationships through a clean and intuitive interface.

## Features

### Core Features
- **Door Management**: List all doors, create new doors, view door details
- **User Management**: List all users, create new users
- **Access Management**: Assign users to doors (grant access), remove access, view which users have access to each door
- Pagination for doors and users lists
- Search/filtering functionality
- Error handling with user feedback
- Responsive UI with Tailwind CSS

## Technology Stack

### Backend
- **Spring Boot 3.2.0** (Java 17)
- **Maven** for dependency management
- **PostgreSQL** database
- **JPA/Hibernate** for ORM
- **Flyway** for database migrations
- **Lombok** for reducing boilerplate
- **Spring Boot Actuator** for monitoring
- **Swagger/OpenAPI** for API documentation

### Frontend
- **Angular 21** with standalone components
- **TypeScript**
- **Tailwind CSS** for styling
- **RxJS** for reactive programming

### Infrastructure
- **Docker Compose** for PostgreSQL database


## Prerequisites

- **Java 17+**
- **Maven 3.6+**
- **Node.js 18+** and **npm**
- **Docker** and **Docker Compose**
- **Angular CLI** (install globally: `npm install -g @angular/cli`)

## Setup Instructions

### 1. Start the Database

Navigate to the backend directory and start PostgreSQL using Docker Compose:

```bash
cd door-management-back
docker-compose up -d
```

Or from the root directory:

```bash
cd door-management-back && docker-compose up -d
```

This will start PostgreSQL on `localhost:5432` with:
- Database: `door_management`
- Username: `postgres`
- Password: `postgres`

### 2. Run the Backend

Navigate to the backend directory:

```bash
cd door-management-back
```

Run the Spring Boot application:

```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

The Flyway migrations will run automatically on startup, creating the necessary database tables.

### 3. Run the Frontend

Open a new terminal and navigate to the frontend directory:

```bash
cd door-management-front
```

Install dependencies (if not already installed):

```bash
npm install
```

Start the development server:

```bash
npm start
```

The frontend will be available at `http://localhost:4200`

## Design Decisions

### Architecture
1. **Layered Architecture**: Clear separation between controller, service, and repository layers for maintainability
2. **DTO Pattern**: Using DTOs to decouple API contracts from entities, allowing for future API changes without affecting the domain model
3. **Many-to-Many Relationship**: Using a separate `Access` entity (junction table) instead of a simple join table for better control and potential future fields (e.g., access granted date, expiration)

### Backend
1. **Flyway Migrations**: Version-controlled database schema changes for clean evolution
2. **RESTful API**: Following REST conventions with proper HTTP methods and status codes
3. **Validation**: Using Bean Validation for request validation
4. **Swagger/OpenAPI**: Using Swagger/OpenAPI for API documentation

### Frontend
1. **Standalone Components**: Using modern Angular standalone component architecture (no NgModules)
2. **Reactive Forms**: Using Angular reactive forms with validation
3. **Signals**: Using Angular signals for reactive state management
4. **Lazy Loading**: Components are lazy-loaded for better performance
5. **Tailwind CSS**: Utility-first CSS framework for rapid UI development

### Testing
1. **Unit Tests**: Service layer tests with Mockito
2. **Integration Tests**: Controller tests with MockMvc
3. **Test Coverage**: Focused on business logic and API endpoints