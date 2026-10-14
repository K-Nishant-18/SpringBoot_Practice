# 🎓 Student CRUD API (Spring Boot)

A beginner-friendly **REST API** built with **Spring Boot 3.2.4**, **Spring Data JPA** and the **H2 in-memory database**. It performs full **CRUD** (Create, Read, Update, Delete) on students, plus **PATCH** for partial updates.

> Built as a hands-on practice project while learning Spring Boot: layered architecture, dependency injection, transactions and REST conventions.


---

## ✨ Features

- Full CRUD on a `Student` resource
- **PATCH** support for partial updates (only the fields you send are changed)
- Clean **Controller → Service → Repository** layering
- Constructor-based **dependency injection**
- Declarative transactions with `@Transactional`
- Proper HTTP status codes (`201`, `204`, `404`, ...)
- Custom exception mapped to `404 Not Found`
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
| Lombok | Boilerplate reduction (available in the project) |
| Maven (wrapper included) | Build tool |
| Postman | API testing |

---

## 🏗 Architecture

```
Client (Postman / Browser / Frontend)
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
├── mvnw / mvnw.cmd                     # Maven wrapper (no Maven install needed)
└── src/
    └── main/
        ├── java/com/example/crud/
        │   ├── CrudApplication.java            # Entry point (@SpringBootApplication)
        │   ├── Student.java                    # JPA entity (maps to "students" table)
        │   ├── StudentPatchRequest.java        # DTO (record) for PATCH
        │   ├── StudentRepository.java          # JpaRepository interface
        │   ├── StudentService.java             # Business logic + transactions
        │   ├── StudentController.java          # REST endpoints
        │   └── StudentNotFoundException.java   # Custom exception -> HTTP 404
        └── resources/
            └── application.properties          # App configuration
```

---

## 🚀 Getting Started

### Prerequisites

- **JDK 17 or newer** (project tested on JDK 22)
- **Git**
- An IDE such as IntelliJ IDEA (optional)

### Run locally

```bash
# 1. Clone the repository
git clone <https://github.com/K-Nishant-18/SpringBoot_Practice>
cd crud

# 2. Run the application
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
spring.datasource.username=sa
spring.datasource.password=

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

## 📡 API Reference

Base path: `/api/students`

| Method | Endpoint | Description | Success | Errors |
|---|---|---|---|---|
| `POST` | `/api/students` | Create a student | `201 Created` | `400`, `415`, `500` (duplicate email) |
| `GET` | `/api/students` | List all students | `200 OK` | - |
| `GET` | `/api/students/{id}` | Get one student | `200 OK` | `404` |
| `PUT` | `/api/students/{id}` | Replace a student (send all fields) | `200 OK` | `404`, `500` |
| `PATCH` | `/api/students/{id}` | Partially update a student | `200 OK` | `404` |
| `DELETE` | `/api/students/{id}` | Delete a student | `204 No Content` | `404` |

### Student model

| Field | Type | Constraints |
|---|---|---|
| `id` | Long | Primary key, auto-generated |
| `name` | String | Required, max 100 chars |
| `email` | String | Required, unique |
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

### cURL quick test

```bash
# Create
curl -i -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"name":"Asha","email":"asha@example.com","age":21}'

# Read all / one
curl http://localhost:8080/api/students
curl http://localhost:8080/api/students/1

# Patch
curl -X PATCH http://localhost:8080/api/students/1 \
  -H "Content-Type: application/json" \
  -d '{"age":25}'

# Delete
curl -i -X DELETE http://localhost:8080/api/students/1
```

---

## 🧪 Testing with Postman

1. Create a collection named **Student CRUD** with a variable `baseUrl = http://localhost:8080`.
2. For `POST`, `PUT` and `PATCH`: **Body → raw → JSON** (this sets `Content-Type: application/json`).
3. Test the happy paths first, then the error cases:

| Scenario | Expected |
|---|---|
| `GET /api/students/99` | `404` |
| `DELETE /api/students/99` | `404` |
| POST without JSON content type | `415` |
| POST with malformed JSON | `400` |
| `GET /api/students/abc` | `400` |
| POST the same email twice | `500` (see limitations) |

---

## 🧠 Key Concepts Used

| Concept | Where |
|---|---|
| `@SpringBootApplication` (auto-configuration + component scan) | `CrudApplication` |
| `@Entity`, `@Id`, `@GeneratedValue`, `@Column` | `Student` |
| Spring Data JPA derived repository (no implementation class) | `StudentRepository` |
| Constructor injection (no `@Autowired` needed for a single constructor) | Service and Controller |
| `@Transactional` and `readOnly = true` | `StudentService` |
| **Dirty checking** (updates without calling `save()`) | `update` and `patch` |
| `Optional.orElseThrow` | `getById` |
| `@ResponseStatus` on an exception → HTTP 404 | `StudentNotFoundException` |
| `@RestController`, `@RequestMapping`, `@PathVariable`, `@RequestBody` | `StudentController` |
| `ResponseEntity.created(...)` for `201` + `Location` header | `create` endpoint |
| Java `record` as a DTO | `StudentPatchRequest` |

---

## ⚠️ Known Limitations

This is a learning project, so some production concerns are intentionally left out:

- **Entities are exposed directly** in the API. Real projects should use DTOs.
- **No validation** (`@Valid`, `@NotBlank`, `@Email`). Empty names are accepted.
- **Duplicate email returns `500`** instead of `409 Conflict`.
- **PATCH cannot set a field to `null`** (`null` means "not sent").
- **No pagination**; `GET /api/students` returns everything.
- **No authentication or authorization.**
- **No automated tests** beyond the generated context-load test.
- **In-memory database**, so data is lost on restart.

---

## 🗺 Roadmap

- [ ] DTOs for request and response (stop exposing entities)
- [ ] Bean Validation with `@Valid`
- [ ] Global exception handling with `@RestControllerAdvice` (consistent JSON errors)
- [ ] `409 Conflict` for duplicate emails
- [ ] Pagination and sorting with `Pageable`
- [ ] Search and filtering (e.g. by name)
- [ ] MySQL/PostgreSQL with `dev` and `prod` profiles + Flyway migrations
- [ ] Unit tests (Mockito) and slice tests (`@WebMvcTest`, `@DataJpaTest`)
- [ ] Swagger/OpenAPI documentation
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
| Update doesn't persist | Check `@Transactional` is present on the service method |
| `HikariPool ... Thread starvation or clock leap` warning | Harmless; caused by the laptop sleeping or long idle time |

Tip: when a stack trace is long, read the **last `Caused by`** line first.

---

## 👤 Author

**Kumar Nishant**
Full Stack Developer (Java, Spring Boot, JavaScript, MySQL) — Bhagalpur, Bihar

⭐ If this project helped you learn Spring Boot, consider giving it a star!
