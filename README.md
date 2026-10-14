# Student Management REST API

A learning project demonstrating a layered REST API built with **Java 21**, **Spring Boot 3.2.4**, **Spring Web**, **Spring Data JPA**, **Hibernate**, and an **H2 in-memory database**. The API supports creating, retrieving, updating, partially updating, and deleting student records. Interactive API documentation is provided by **Springdoc OpenAPI / Swagger UI**.

> This repository is a practice project, not a production-ready service. Read the limitations section before using it as a production template.

## Features

- RESTful CRUD endpoints for students.
- Partial updates with `PATCH` (non-null request fields are applied).
- Layered architecture: Controller → Service → Repository.
- Constructor-based dependency injection.
- Transaction annotations in the service layer.
- Custom `StudentNotFoundException` mapped to HTTP `404`.
- `201 Created` with a `Location` header when a student is created.
- `204 No Content` on successful deletion.
- Automatically generated OpenAPI documentation and Swagger UI.
- H2 in-memory database for local practice with no database server installation.

## Tech stack

| Technology | Purpose |
|---|---|
| Java 21 | Language (configured in Maven) |
| Spring Boot 3.2.4 | Application framework |
| Spring Web MVC | REST endpoints and JSON handling |
| Spring Data JPA / Hibernate | Persistence and ORM |
| H2 Database | In-memory database |
| Springdoc OpenAPI 2.5.0 | OpenAPI specification and Swagger UI |
| Lombok | Generates entity getters, setters and other boilerplate |
| Maven Wrapper | Reproducible Maven commands |

## Project structure

The runnable Maven project is nested inside `crud/crud`.

```text
SpringBoot_Practice/
├── README.md
└── crud/
    └── crud/
        ├── pom.xml
        ├── mvnw
        ├── mvnw.cmd
        └── src/
            ├── main/
            │   ├── java/com/example/crud/
            │   │   ├── CrudApplication.java
            │   │   ├── Controller/
            │   │   │   └── StudentController.java
            │   │   ├── Entity/
            │   │   │   └── Student.java
            │   │   ├── Repository/
            │   │   │   └── StudentRepository.java
            │   │   ├── Services/
            │   │   │   └── StudentService.java
            │   │   └── Exceptions/
            │   │       └── StudentNotFoundException.java
            │   └── resources/
            │       └── application.properties
            └── test/
                └── java/com/example/crud/
                    └── CrudApplicationTests.java
```

## Architecture

```text
HTTP client (Swagger UI, curl, Postman, frontend)
                  |
                  v
        StudentController
       HTTP and JSON handling
                  |
                  v
          StudentService
          Application logic
                  |
                  v
        StudentRepository
          Spring Data JPA
                  |
                  v
             H2 database
```

The controller handles HTTP requests, the service coordinates application logic, and the repository performs persistence operations. Keep these responsibilities separate as the project grows.

## Getting started

### Prerequisites

- JDK 21 (the version configured in `pom.xml`).
- Git.
- An IDE such as IntelliJ IDEA is optional.

### Clone the repository

```bash
git clone https://github.com/K-Nishant-18/SpringBoot_Practice.git
cd SpringBoot_Practice/crud/crud
```

### Run the application

Linux / macOS:

```bash
./mvnw spring-boot:run
```

Windows PowerShell / Command Prompt:

```powershell
.\mvnw.cmd spring-boot:run
```

The server is configured to use port `8080`.

### Build and test

```bash
# Run tests
./mvnw test

# Create the executable jar
./mvnw clean package

# Run the packaged application
java -jar target/crud-0.0.1-SNAPSHOT.jar
```

On Windows, use `mvnw.cmd` instead of `./mvnw`.

## Swagger UI and OpenAPI

After the application starts, open:

- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

Swagger UI lets you inspect the endpoints, review their request and response schemas, and send requests from the browser. OpenAPI JSON is the machine-readable API specification.

The documented endpoint group is **Student Management**. Controller methods use OpenAPI annotations such as `@Tag` and `@Operation` to provide readable names and descriptions. Documentation annotations describe endpoints; they do not add request validation or implement new business behavior.

## API reference

Base URL: `http://localhost:8080`  
Base path: `/api/students`

| Method | Endpoint | Purpose | Success response |
|---|---|---|---|
| `POST` | `/api/students` | Create a student | `201 Created` with response body and `Location` header |
| `GET` | `/api/students` | List all students | `200 OK` with a JSON array |
| `GET` | `/api/students/{id}` | Retrieve one student | `200 OK` with a JSON object |
| `PUT` | `/api/students/{id}` | Update a student's name, email and age | `200 OK` with updated object |
| `PATCH` | `/api/students/{id}` | Update only non-null fields in the request | `200 OK` with updated object |
| `DELETE` | `/api/students/{id}` | Delete a student | `204 No Content` |

Unknown student IDs result in `404 Not Found` through `StudentNotFoundException`. Other error handling may depend on Spring and database behavior; see Known limitations.

### Student fields

| Field | Type | Behavior |
|---|---|---|
| `id` | `Long` | Auto-generated primary key |
| `name` | `String` | Database column is non-null; maximum length 100 |
| `email` | `String` | Database column is non-null, unique, maximum length 100 |
| `age` | `Integer` | Optional |

The entity's database constraints are not the same as request validation. The project does not currently use Bean Validation annotations such as `@NotBlank` or `@Email`.

## Request examples

Use `Content-Type: application/json` for POST, PUT and PATCH requests.

### Create a student

```http
POST /api/students
Content-Type: application/json
```

```json
{
  "name": "Asha",
  "email": "asha@example.com",
  "age": 21
}
```

The controller returns `201 Created`, the saved student in the response body, and a `Location` header pointing to the new student resource.

### Get all students

```http
GET /api/students
```

### Get one student

```http
GET /api/students/1
```

### Update a student (PUT)

```http
PUT /api/students/1
Content-Type: application/json
```

```json
{
  "name": "Asha Kumari",
  "email": "asha@example.com",
  "age": 22
}
```

The current service assigns all three fields from the request. Send values for all editable fields when using PUT.

### Partially update a student (PATCH)

```http
PATCH /api/students/1
Content-Type: application/json
```

```json
{
  "age": 25
}
```

The service updates only fields whose values are non-null. With the current entity-based request, an omitted field and an explicitly supplied `null` are indistinguishable, so PATCH cannot explicitly clear a field to null.

### Delete a student

```http
DELETE /api/students/1
```

A successful deletion returns `204 No Content` and an empty response body.

## Quick testing with curl

Run the application first, then execute these commands. They use the same local API base URL.

```bash
# Create
curl -i -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"name":"Asha","email":"asha@example.com","age":21}'

# List all
curl -i http://localhost:8080/api/students

# Fetch student ID 1
curl -i http://localhost:8080/api/students/1

# Update
curl -i -X PUT http://localhost:8080/api/students/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Asha Kumari","email":"asha@example.com","age":22}'

# Partially update
curl -i -X PATCH http://localhost:8080/api/students/1 \
  -H "Content-Type: application/json" \
  -d '{"age":25}'

# Delete
curl -i -X DELETE http://localhost:8080/api/students/1
```

For a Windows shell that does not support Unix-style line continuations, run each command on one line or use PowerShell's backtick continuation syntax.

## Configuration

The application configuration lives in `src/main/resources/application.properties`:

```properties
spring.application.name=student-crud
server.port=8080

spring.datasource.url=jdbc:h2:mem:studentdb
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

| Setting | Meaning |
|---|---|
| `server.port=8080` | Runs the web server on port 8080 |
| `jdbc:h2:mem:studentdb` | Uses an in-memory H2 database |
| `ddl-auto=create-drop` | Creates schema on startup and drops it when the application shuts down |
| `show-sql=true` | Prints SQL statements to the application console |

**Data is temporary.** The in-memory database is recreated when the application restarts, and records are not retained.

## Key Spring concepts demonstrated

- `@SpringBootApplication` and component scanning.
- `@RestController`, `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping`, `@PatchMapping`, and `@DeleteMapping`.
- Constructor-based dependency injection.
- JPA entity mapping with `@Entity`, `@Id`, `@GeneratedValue`, and `@Column`.
- Spring Data `JpaRepository`.
- Transaction demarcation with `@Transactional`.
- Entity updates via JPA dirty checking within a transaction.
- Exception-to-HTTP mapping with `@ResponseStatus`.
- OpenAPI documentation with Springdoc and Swagger UI.

## Known limitations and next improvements

This is a learning project. Important gaps to address before production use:

- **No Bean Validation:** add request DTOs with `@NotBlank`, `@Email`, `@Size`, and `@Valid`.
- **Duplicate-email errors are not mapped to a deliberate client error:** translate unique-constraint violations to a consistent response such as `409 Conflict`.
- **PATCH uses the entity as its request body:** use a dedicated patch DTO to define updateable fields and, if needed, support explicitly clearing values.
- **Entities are returned directly:** consider separate request and response DTOs.
- **No pagination or sorting:** `GET /api/students` returns every student.
- **No authentication or authorization.**
- **Only basic generated test scaffolding is present:** add service and controller tests.
- **In-memory persistence:** use a persistent database for data that must survive restarts.
- **Duplicate dependencies in the current POM:** H2 and Spring Boot Test are each declared twice; remove duplicates as a cleanup.

## Troubleshooting

| Issue | What to check |
|---|---|
| Port 8080 is occupied | Stop the other process or change `server.port`. |
| Swagger UI does not load | Confirm the application started and the Springdoc starter is present; inspect startup logs. |
| API returns `415 Unsupported Media Type` | Send `Content-Type: application/json` with JSON request bodies. |
| A student ID returns `404` | Confirm the ID exists; the in-memory database starts empty. |
| Data disappears after restart | Expected with `jdbc:h2:mem:studentdb`. |
| Java version error | Use JDK 21, matching `java.version` in `pom.xml`. |

## Author

**Kumar Nishant**  
GitHub: [K-Nishant-18](https://github.com/K-Nishant-18)

If this project helps you learn Spring Boot, feel free to ⭐ the repository.
