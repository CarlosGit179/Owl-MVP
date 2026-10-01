# 🦉 Owl

A library management REST API built with Java and Spring Boot. The MVP supports books, students, and loans, with PostgreSQL persistence and JWT based authentication and authorization.

## Features

- Register, find, update, and delete books and students
- Create, list, and return loans
- Find loans by student or status, and list the authenticated student's loans
- Automatically track book availability

## Business rules

- A book must be available before it can be loaned.
- A student can have only one active loan at a time.
- A book becomes unavailable when loaned and available again when returned.
- Loan dates are recorded automatically; the due date is one month later.
- Requests involving records that do not exist are rejected.

## Technology

- Java 21
- Spring Boot, Spring Web, Spring Data JPA, Spring Security
- PostgreSQL and Hibernate
- Maven, JUnit 5, and Mockito

## Architecture

The application uses a layered architecture:

- **Controllers** receive HTTP requests and expose API endpoints.
- **Services** implement application logic and business rules.
- **Repositories** provide persistence through Spring Data JPA.
- **Entities** represent books, students, loans, and users.

## Run locally

Requirements: Java 21+, Maven, and PostgreSQL.

1. Clone the repository and enter its directory:

   ```shell
   git clone <REPOSITORY-URL>
   cd Owl
   ```

2. Configure the PostgreSQL connection in `src/main/resources/application.properties`.
3. Start the application:

   ```shell
   mvn spring-boot:run
   ```

The application runs on port `8080` by default. Hibernate creates or updates database structures according to the application configuration.

## API

- `/books`: book registration and search, updates, and deletion
- `/students`: student registration and search, updates, and deletion
- `/loans`: loan creation, searches, listing, and returns
- `/auth`: authentication and registration

OpenAPI/Swagger documentation is configured by the application.

## Tests

Run the automated test suite with:

```shell
mvn test
```

The tests cover service behavior, business rules, and HTTP controller responses.

## Roadmap

Potential future work includes Docker and CI/CD improvements, expanded API documentation, book reservations, advanced loan history, overdue tracking, fines, and return notifications.

## Status

The MVP's core book, student, and loan management features are implemented. Roadmap items are outside the current MVP scope.
