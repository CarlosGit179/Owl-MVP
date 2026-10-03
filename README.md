# 🦉 Owl

Owl is a REST API for library management. It lets administrators manage books, students, and loans, while students can sign in and view their own loans. The application is built with Java and Spring Boot, stores data in PostgreSQL, and uses JWT bearer tokens for authentication.

## Features

- Create, list, search, update, and delete books and students
- Create and return loans, and search loans by student or status
- Let an authenticated student view their own loans
- Track whether a book is available for loan
- Protect endpoints by role: administrators manage library records; students can access their personal loan list
- Explore the API through generated Swagger UI and OpenAPI documentation

## Business rules

- Only available books can be loaned.
- A student can have only one active loan at a time.
- A book becomes unavailable when loaned and available again when returned.
- The loan date is set when a loan is created; the due date is one month later.
- A return request applies to the student's active loan.
- A student account must be linked to an existing student record.

## Technology

- Java 21 and Spring Boot 4
- Spring Web MVC, Spring Data JPA, Spring Security, and Bean Validation
- PostgreSQL 16 and Hibernate
- JWT-based authentication with Auth0 Java JWT; BCrypt password hashing
- Springdoc OpenAPI / Swagger UI
- Maven, JUnit 5, and Spring Boot test libraries
- Docker and Docker Compose for containerized local development

## Run with Docker Compose

Requirements: Docker with the Compose plugin.

The Compose setup starts PostgreSQL and the Owl API. Before starting, create a `.env` file in the project root with administrator credentials:

```dotenv
APP_ADMIN_LOGIN=admin
ADMIN_PASSWORD=change-this-password
```

Start both services:

```shell
docker compose up --build
```

The API is available at `http://localhost:8080`; PostgreSQL is published on port `5432`. Compose waits for the database health check before starting the API. Data is stored in the `postgres-data` Docker volume.

The Compose file currently configures PostgreSQL with database `owl`, user `postgres`, and password `postgres123`. Change these values before using the setup beyond local development, and keep the database and administrator credentials private.

Stop the services with `docker compose down`. The database volume remains so data is available the next time you start the stack. To remove the volume and its data, use `docker compose down --volumes`.

## Run without Docker

Requirements: Java 21+, PostgreSQL, and either Maven or the included Maven Wrapper.

1. Create a PostgreSQL database and user, then set the following environment variables for the application:

   ```text
   DB_URL=jdbc:postgresql://localhost:5432/owl
   DB_USER=postgres
   DB_PASSWORD=your-database-password
   APP_ADMIN_LOGIN=admin
   ADMIN_PASSWORD=change-this-password
   JWT_SECRET=replace-with-a-long-random-secret
   ```

   `JWT_SECRET` is optional in the current configuration, but should be set to a private random value. The application uses the configured administrator credentials to create an admin account when it starts.

2. Start the application from the project root:

   ```shell
   ./mvnw spring-boot:run
   ```

   On Windows, use `mvnw.cmd spring-boot:run`, or run `mvn spring-boot:run` if Maven is installed.

The API listens on port `8080`. Hibernate is configured with `ddl-auto=update`, so it creates or updates database tables at startup.

## Authentication and first use

The configured admin account is created when the application starts if one does not already exist. Use its credentials to get a token:

```http
POST /auth/login
Content-Type: application/json

{
  "login": "admin",
  "password": "change-this-password"
}
```

The response contains a JWT. Send it on protected requests as `Authorization: Bearer <token>`. The admin can register a student record through `POST /students`, then create a student login linked to that record through `POST /auth/register`:

```http
POST /auth/register
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "login": "student1",
  "password": "student-password",
  "role": "STUDENT",
  "registrationNumber": "2024001"
}
```

Student accounts can sign in and use `GET /loans/me` to see their own loans. Book, student, and general loan management endpoints require an admin token.

## API endpoints

All endpoints are relative to `http://localhost:8080`. Except for login and the public documentation, requests require a bearer token with an authorized role.

| Method | Path | Description | Access |
| --- | --- | --- | --- |
| `POST` | `/auth/login` | Authenticate and receive a JWT | Public |
| `POST` | `/auth/register` | Create a student account linked to an existing student | Admin |
| `POST` | `/books` | Register a book | Admin |
| `GET` | `/books` | List books | Admin |
| `GET` | `/books/isbn/{isbn}` | Find a book by ISBN | Admin |
| `GET` | `/books/title/{title}` | Find books by title | Admin |
| `PUT` | `/books/{isbn}` | Update a book | Admin |
| `DELETE` | `/books/{isbn}` | Delete a book | Admin |
| `POST` | `/students` | Register a student | Admin |
| `GET` | `/students` | List students | Admin |
| `GET` | `/students/name/{name}` | Find students by name | Admin |
| `GET` | `/students/registration-number/{registrationNumber}` | Find a student by registration number | Admin |
| `PUT` | `/students/{registrationNumber}` | Update a student | Admin |
| `DELETE` | `/students/{registrationNumber}` | Delete a student | Admin |
| `POST` | `/loans?studentRegistrationNumber={number}&bookIsbn={isbn}` | Create a loan | Admin |
| `GET` | `/loans` | List all loans | Admin |
| `GET` | `/loans/registration-number/{registrationNumber}` | Find a student's loans | Admin |
| `GET` | `/loans/status/{status}` | Find loans by status (`ACTIVE` or `RETURNED`) | Admin |
| `PUT` | `/loans/{registrationNumber}/return` | Return the student's active loan | Admin |
| `GET` | `/loans/me` | List the authenticated student's loans | Student |

The book and student create/update endpoints accept JSON request bodies. See Swagger UI for the request and response schemas.

## API documentation

With the application running, open [Swagger UI](http://localhost:8080/swagger-ui/index.html). The OpenAPI document is available at `http://localhost:8080/v3/api-docs`.

## Tests

Run the automated suite with the Maven Wrapper:

```shell
./mvnw test
```

On Windows, run `mvnw.cmd test`. The suite includes service and controller tests for core behavior, business rules, and access control.

## Project structure

- **Controllers** map HTTP requests to application operations.
- **Services** apply business rules and coordinate workflows.
- **Repositories** access PostgreSQL through Spring Data JPA.
- **Entities** represent books, students, loans, and user accounts.
- **DTOs** define the API's request and response data.

## Project status

The MVP includes book, student, and loan management, JWT authentication, role-based access, Docker Compose, and generated API documentation. Possible future work includes CI/CD improvements, reservations, overdue tracking, fines, and return notifications.
