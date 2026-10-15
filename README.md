# 🎓 Student CRUD API (Spring Boot)

A beginner-friendly **REST API** built with **Spring Boot 3.2.4**, **Spring Data JPA** and the **H2 in-memory database**. It performs full **CRUD** (Create, Read, Update, Delete) on students, plus **PATCH** for partial updates, and ships with interactive **Swagger UI** documentation.

> Built as a hands-on practice project while learning Spring Boot: layered architecture, dependency injection, transactions and REST conventions.

---


## ✨ Features

- Full CRUD on a `Student` resource
- **PATCH** support for partial updates (only the non-null fields you send are changed)
- Clean **Controller → Service → Repository** layering, each in its own package
- Constructor-based **dependency injection**
- Declarative transactions with `@Transactional` (including **dirty checking** for updates)
- Proper HTTP status codes (`201`, `204`, `404`, ...)
- Custom exception mapped to `404 Not Found`
- **Swagger UI / OpenAPI** documentation with grouped, described endpoints
- Zero setup database (H2 in-memory), with the SQL printed in the console

---

## 🛠 Tech Stack

| Technology | Purpose |
|---|---|
| Java 22 (17+ works) | Language |
| Spring Boot 3.2.4 | Application framework |
| Spring Web (MVC) | REST controllers, embedded Tomcat, Jackson (JSON) |
| Spring Data JPA | Repository layer, Hibernate ORM |
| H2 Database | In-memory database |
| springdoc-openapi (2.x) | Swagger UI and OpenAPI docs |
| Lombok | Boilerplate reduction (getters and setters) |
| Maven (wrapper included) | Build tool |
| Postman | API testing |

---

## 🏗 Architecture

```
Client (Postman / Swagger UI / Frontend)
        │  HTTP + JSON
        ▼
┌─────────────────────────┐
│  StudentController      │  Handles HTTP: URLs, JSON, status codes
└───────────┬─────────────┘
            ▼
┌─────────────────────────┐
│  StudentService         │  Business logic + @Transactional
└───────────┬─────────────┘
            ▼
┌─────────────────────────┐
│  StudentRepository      │  Data access (Spring Data JPA, auto-generated)
└───────────┬─────────────┘
            ▼
      H2 Database (students table)
```

**Rules followed:** dependencies point downward only (Controller → Service → Repository). The controller never touches the repository directly, and the service contains no HTTP types.

---

## 📁 Project Structure

```
crud/
├── pom.xml
├── mvnw / mvnw.cmd                          # Maven wrapper (no Maven install needed)
└── src/
    └── main/
        ├── java/com/example/crud/
        │   ├── crudApplication.java                 # Entry point (@SpringBootApplication)
        │   ├── controller/
        │   │   └── StudentController.java           # REST endpoints + Swagger annotations
        │   ├── services/
        │   │   └── StudentService.java              # Business logic + transactions
        │   ├── repository/
        │   │   └── StudentRepository.java           # JpaRepository interface
        │   ├── entity/
        │   │   └── Student.java                     # JPA entity (maps to "students" table)
        │   ├── exceptions/
        │   │   └── StudentNotFoundException.java    # Custom exception -> HTTP 404
        │   └── configuration/
        │       └── OpenApiConfig.java               # Swagger / OpenAPI metadata
        └── resources/
            └── application.properties               # App configuration
```

> Every package sits **under** the root package `com.example.crud`, which is where Spring Boot starts component scanning.

---

## 🚀 Getting Started

### Prerequisites

- **JDK 17 or newer** (project tested on JDK 22)
- **Git**
- An IDE such as IntelliJ IDEA (optional)

### Run locally

```bash
# 1. Clone the repository
git clone https://github.com/K-Nishant-18/SpringBoot_Practice.git

# 2. Go into the folder that contains pom.xml
cd SpringBoot_Practice

# 3. Run the application
./mvnw spring-boot:run        # Linux / macOS
mvnw.cmd spring-boot:run      # Windows
```

Or open the project in IntelliJ and run `CrudApplication`.

When you see this in the console, the API is ready:

```
Tomcat started on port 8080 (http)
Started CrudApplication in X seconds
```

Base URL: **http://localhost:8080**

### Build a runnable jar

```bash
./mvnw clean package
java -jar target/crud-0.0.1-SNAPSHOT.jar
```

---

## ⚙️ Configuration

`src/main/resources/application.properties`

```properties
spring.application.name=student-crud
server.port=8080

spring.datasource.url=jdbc:h2:mem:studentdb
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
```

| Property | Meaning |
|---|---|
| `server.port` | Port Tomcat listens on |
| `spring.datasource.url` | In-memory H2 database named `studentdb` |
| `ddl-auto=create-drop` | Tables are created at startup and dropped at shutdown |
| `show-sql=true` | Prints generated SQL to the console |
| `open-in-view=false` | Avoids keeping the DB session open for the whole request |

> ⚠️ **Data is not persisted.** The database lives in memory, so all data is lost when the app stops.

### Switching to MySQL (optional)

1. Replace the H2 dependency with `com.mysql:mysql-connector-j` (scope `runtime`).
2. Update the datasource properties:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/school
   spring.datasource.username=root
   spring.datasource.password=${DB_PASSWORD}
   spring.jpa.hibernate.ddl-auto=update
   ```
3. No Java code changes are needed.

---

## 📖 API Documentation (Swagger)

With the app running, open the interactive docs and try every endpoint from the browser:

| Link | What it is |
|---|---|
| http://localhost:8080/swagger-ui.html | Swagger UI (click an endpoint → **Try it out** → **Execute**) |
| http://localhost:8080/v3/api-docs | Raw OpenAPI specification (JSON) |

Endpoints are grouped under **Student Management**, and each one has a summary and description defined with `@Tag` and `@Operation`. The API title, version and contact details come from `OpenApiConfig` (`@OpenAPIDefinition`).

---

## 📡 API Reference

Base path: `/api/students`

| Method | Endpoint | Description | Success | Errors |
|---|---|---|---|---|
| `POST` | `/api/students` | Create a student | `201 Created` | `400`, `415`, `500` (duplicate email) |
| `GET` | `/api/students` | List all students | `200 OK` | - |
| `GET` | `/api/students/{id}` | Get one student | `200 OK` | `400` (bad id), `404` |
| `PUT` | `/api/students/{id}` | Replace a student (send all fields) | `200 OK` | `404`, `500` |
| `PATCH` | `/api/students/{id}` | Partially update a student | `200 OK` | `404` |
| `DELETE` | `/api/students/{id}` | Delete a student | `204 No Content` | `404` |

### Student model

| Field | Type | Constraints |
|---|---|---|
| `id` | Long | Primary key, auto-generated |
| `name` | String | Required, max 100 chars |
| `email` | String | Required, unique, max 100 chars |
| `age` | Integer | Optional |

### Examples

**Create** — `POST /api/students`
```json
{
  "name": "Asha",
  "email": "asha@example.com",
  "age": 21
}
```
Response `201 Created` (with header `Location: /api/students/1`):
```json
{
  "id": 1,
  "name": "Asha",
  "email": "asha@example.com",
  "age": 21
}
```

**Get all** — `GET /api/students`
```json
[
  { "id": 1, "name": "Asha", "email": "asha@example.com", "age": 21 }
]
```

**Update (PUT)** — `PUT /api/students/1` (all fields required)
```json
{
  "name": "Asha Kumari",
  "email": "asha@example.com",
  "age": 22
}
```

**Partial update (PATCH)** — `PATCH /api/students/1` (only the fields to change)
```json
{
  "age": 25
}
```

**Delete** — `DELETE /api/students/1` → `204 No Content` (empty body)

### PUT vs PATCH

| | PUT | PATCH |
|---|---|---|
| Meaning | Replace the whole resource | Change only some fields |
| Body | All fields | Only the fields to change |
| Idempotent | Yes | Not guaranteed |
| Missing field | Becomes `null` (DB rejects `name`/`email`) | Left unchanged |

---

## 🧪 Testing with Postman

1. Create a collection named **Student CRUD** with a variable `baseUrl = http://localhost:8080`.
2. For `POST`, `PUT` and `PATCH`: **Body → raw → JSON** (this sets `Content-Type: application/json`).
3. Test the happy paths first, then the error cases:

| Scenario | Expected |
|---|---|
| `GET /api/students/99` | `404` |
| `DELETE /api/students/99` | `404` |
| `PATCH /api/students/99` | `404` |
| POST without JSON content type | `415` |
| POST with malformed JSON | `400` |
| `GET /api/students/abc` | `400` |
| POST the same email twice | `500` (see limitations) |

After every `PUT` or `PATCH`, call `GET /api/students/{id}` again to confirm the change was really saved in the database.

---

## 🧠 Key Concepts Used

| Concept | Where |
|---|---|
| `@SpringBootApplication` (auto-configuration + component scan) | `CrudApplication` |
| `@Entity`, `@Id`, `@GeneratedValue`, `@Column` | `Student` |
| Lombok `@Getter` / `@Setter` (instead of `@Data` on entities) | `Student` |
| Spring Data JPA derived repository (no implementation class) | `StudentRepository` |
| Constructor injection (no `@Autowired` needed for a single constructor) | Service and Controller |
| `@Transactional` and `readOnly = true` | `StudentService` |
| **Dirty checking** (updates without calling `save()`) | `update` and `patch` |
| `Optional.orElseThrow` | `getById` |
| `@ResponseStatus` on an exception → HTTP 404 | `StudentNotFoundException` |
| `@RestController`, `@RequestMapping`, `@PathVariable`, `@RequestBody` | `StudentController` |
| `ResponseEntity.created(...)` for `201` + `Location` header | `create` endpoint |
| `@Tag`, `@Operation`, `@OpenAPIDefinition` (OpenAPI docs) | Controller and `OpenApiConfig` |
| `@Configuration` class | `OpenApiConfig` |

---

## ⚠️ Known Limitations

This is a learning project, so some production concerns are intentionally left out:

- **Entities are exposed directly** in the API (also used as request bodies for POST, PUT and PATCH). A client can still try to send an `id` in the JSON. Real projects use DTOs.
- **No validation** (`@Valid`, `@NotBlank`, `@Email`). Empty names are accepted.
- **Duplicate email returns `500`** instead of `409 Conflict`.
- **PATCH cannot set a field to `null`** (`null` means "not sent").
- **No consistent JSON error body** (no global `@RestControllerAdvice` yet).
- **No pagination**; `GET /api/students` returns everything.
- **No authentication or authorization.**
- **No automated tests** beyond the generated context-load test.
- **In-memory database**, so data is lost on restart.

---

## 🗺 Roadmap

- [x] Full CRUD + PATCH
- [x] Swagger / OpenAPI documentation
- [ ] DTOs for request and response (stop exposing entities)
- [ ] Bean Validation with `@Valid`
- [ ] Global exception handling with `@RestControllerAdvice` (consistent JSON errors)
- [ ] `409 Conflict` for duplicate emails
- [ ] Pagination and sorting with `Pageable`
- [ ] Search and filtering (e.g. by name)
- [ ] MySQL/PostgreSQL with `dev` and `prod` profiles + Flyway migrations
- [ ] Unit tests (Mockito) and slice tests (`@WebMvcTest`, `@DataJpaTest`)
- [ ] Dockerfile and docker-compose
- [ ] Spring Security with JWT

---

## 🩺 Troubleshooting

| Problem | Fix |
|---|---|
| `Port 8080 was already in use` | Stop the other app or change `server.port` |
| `Unsupported class file major version` | Make sure `java.version` in `pom.xml` matches your JDK |
| `415 Unsupported Media Type` | Send header `Content-Type: application/json` |
| All fields are `null` in the controller | Missing `@RequestBody` or wrong content type |
| Update or PATCH doesn't persist | Check `@Transactional` is present on the service method |
| `No qualifying bean ... StudentRepository` | A class is outside `com.example.crud`, or a stereotype annotation is missing |
| Swagger UI shows a 404 | Check the `springdoc-openapi-starter-webmvc-ui` dependency (2.x for Boot 3.x) and try `/swagger-ui/index.html` |
| `HikariPool ... Thread starvation or clock leap` warning | Harmless; caused by the laptop sleeping or long idle time |

Tip: when a stack trace is long, read the **last `Caused by`** line first.

---

## 👤 Author

**Kumar Nishant**
Full Stack Developer (Java, Spring Boot, JavaScript, MySQL) — Bhagalpur, Bihar

[GitHub](https://github.com/K-Nishant-18) · kumar.nishant.eng@gmail.com

⭐ If this project helped you learn Spring Boot, consider giving it a star!
