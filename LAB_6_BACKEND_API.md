# Lab 6: Backend API - RESTful API Development with Spring Boot

## Overview

Lab 6 focuses on building RESTful APIs using Spring Boot and Spring MVC. This lab teaches how to create REST endpoints, handle HTTP requests and responses, implement proper HTTP status codes, validate input data, handle errors gracefully, and document APIs using Swagger/OpenAPI. Students will learn how to build production-ready REST APIs for the online school system.

## Learning Objectives

By completing this lab, you will understand:

- **REST Principles:** How to design RESTful APIs following REST conventions
- **HTTP Methods:** How to use GET, POST, PUT, DELETE, PATCH appropriately
- **HTTP Status Codes:** How to return correct status codes (200, 201, 204, 400, 404, 409, 500)
- **Request/Response Handling:** How to serialize and deserialize JSON data
- **Input Validation:** How to validate request data and return clear error messages
- **Global Exception Handling:** How to handle errors consistently across the API
- **API Documentation:** How to document APIs using Swagger/OpenAPI
- **CRUD Operations:** How to implement complete CRUD operations for resources
- **Relationships:** How to handle relationships between resources (enrollment)
- **Search and Filtering:** How to implement search and filter operations
- **Pagination and Sorting:** How to implement pagination and sorting for large datasets
- **CORS Configuration:** How to configure CORS for cross-origin requests

## Technology Stack

- **Java:** 21
- **Spring Boot:** 3.2.0
  - spring-boot-starter-web: 3.2.0 (REST APIs and embedded Tomcat)
  - spring-boot-starter-validation: 3.2.0 (Input validation)
  - spring-boot-starter-test: 3.2.0 (Testing)
- **Spring Framework:** 6.1.1 (included with Spring Boot)
- **Embedded Server:** Tomcat 10.1.13
- **Swagger/OpenAPI:** springdoc-openapi-starter-webmvc-ui: 2.0.2
- **Testing Libraries:**
  - JUnit 5: 5.9.3
  - Mockito: 5.2.0
  - AssertJ: 3.24.2
- **Maven:** 3.6 or higher

## Project Structure

```
src/
├── main/
│   ├── java/com/bootcamp/onlineschool/
│   │   ├── OnlineSchoolApplication.java
│   │   ├── controller/
│   │   │   ├── StudentController.java
│   │   │   └── CourseController.java
│   │   ├── service/
│   │   │   ├── StudentService.java
│   │   │   └── CourseService.java
│   │   ├── repository/
│   │   │   ├── StudentRepository.java
│   │   │   └── CourseRepository.java
│   │   ├── entity/
│   │   │   ├── Student.java
│   │   │   └── Course.java
│   │   ├── dto/
│   │   │   ├── StudentDTO.java
│   │   │   ├── CourseDTO.java
│   │   │   └── ErrorResponse.java
│   │   ├── exception/
│   │   │   └── GlobalExceptionHandler.java
│   │   └── config/
│   │       ├── AppConfig.java
│   │       └── CorsConfig.java
│   └── resources/
│       ├── application.properties
│       ├── application-dev.properties
│       └── data.sql
└── test/
    ├── java/com/bootcamp/onlineschool/
    │   ├── controller/
    │   │   ├── StudentControllerTest.java
    │   │   └── CourseControllerTest.java
    │   ├── exception/
    │   │   └── GlobalExceptionHandlerTest.java
    │   └── integration/
    │       └── ApiIntegrationTest.java
    └── resources/
        └── application-test.properties
```

## Key Concepts

### 1. REST Controllers

REST controllers handle HTTP requests and return responses:

```java
@RestController
@RequestMapping("/api/students")
public class StudentController {
    @Autowired
    private StudentService studentService;
    
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO dto) {
        StudentDTO created = studentService.createStudent(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(
        @PathVariable Long id,
        @Valid @RequestBody StudentDTO dto) {
        return ResponseEntity.ok(studentService.updateStudent(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
```

### 2. HTTP Status Codes

Proper status codes indicate operation results:

- **200 OK:** Successful GET, PUT, or POST (with response body)
- **201 Created:** Successful POST with Location header
- **204 No Content:** Successful DELETE
- **400 Bad Request:** Invalid input or malformed request
- **404 Not Found:** Resource not found
- **409 Conflict:** Constraint violation or duplicate resource
- **500 Internal Server Error:** Unexpected server error

### 3. Input Validation

Validate request data before processing:

```java
public class StudentDTO {
    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;
    
    @Email(message = "Email must be valid")
    private String email;
    
    @Min(value = 0, message = "GPA must be at least 0.0")
    @Max(value = 4, message = "GPA must be at most 4.0")
    private Double gpa;
}
```

### 4. Global Exception Handling

Handle all exceptions consistently:

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(
        EntityNotFoundException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(
        MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Validation Failed",
            "Input validation failed",
            request.getRequestURI(),
            fieldErrors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
```

### 5. API Documentation with Swagger

Document APIs automatically:

```java
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Online School API")
                .version("1.0.0")
                .description("REST API for Online School Management System"));
    }
}
```

Access Swagger UI at: `http://localhost:8080/swagger-ui.html`

### 6. Search and Filtering

Implement search and filter operations:

```java
@GetMapping
public ResponseEntity<List<StudentDTO>> searchStudents(
    @RequestParam(required = false) String name,
    @RequestParam(required = false) Double minGpa) {
    return ResponseEntity.ok(studentService.searchStudents(name, minGpa));
}
```

### 7. Pagination and Sorting

Handle large datasets efficiently:

```java
@GetMapping
public ResponseEntity<Page<StudentDTO>> getStudents(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(defaultValue = "id") String sort) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
    return ResponseEntity.ok(studentService.getStudents(pageable));
}
```

### 8. CORS Configuration

Allow cross-origin requests:

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
```

## API Endpoints

### Student Endpoints

| Method | Path | Status | Description |
|--------|------|--------|-------------|
| GET | /api/students | 200 | Get all students |
| GET | /api/students/{id} | 200/404 | Get student by ID |
| POST | /api/students | 201 | Create new student |
| PUT | /api/students/{id} | 200/404 | Update student |
| DELETE | /api/students/{id} | 204/404 | Delete student |
| GET | /api/students/{id}/courses | 200/404 | Get student's courses |

### Course Endpoints

| Method | Path | Status | Description |
|--------|------|--------|-------------|
| GET | /api/courses | 200 | Get all courses |
| GET | /api/courses/{id} | 200/404 | Get course by ID |
| POST | /api/courses | 201 | Create new course |
| PUT | /api/courses/{id} | 200/404 | Update course |
| DELETE | /api/courses/{id} | 204/404 | Delete course |
| GET | /api/courses/{id}/students | 200/404 | Get enrolled students |

### Enrollment Endpoints

| Method | Path | Status | Description |
|--------|------|--------|-------------|
| POST | /api/students/{studentId}/courses/{courseId} | 200/404/409 | Enroll student |
| DELETE | /api/students/{studentId}/courses/{courseId} | 204/404 | Withdraw student |

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher

### Running the Application

```bash
# Build the project
mvn clean build

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Accessing Swagger UI

Access the interactive API documentation at: `http://localhost:8080/swagger-ui.html`

### Running Tests

```bash
# Run all tests
mvn clean test

# Run specific test class
mvn test -Dtest=StudentControllerTest

# Run with verbose output
mvn test -X
```

## Test Coverage

### Controller Tests (25+ tests)
- Test all CRUD endpoints
- Test HTTP status codes
- Test request/response handling
- Test error scenarios

### Exception Handler Tests (10+ tests)
- Test 404 Not Found
- Test 400 Bad Request
- Test 409 Conflict
- Test 500 Internal Server Error

### Integration Tests (15+ tests)
- Test complete workflows
- Test error handling end-to-end
- Test relationship operations
- Test search and filter

### Property-Based Tests (12 properties)
- GET all returns all resources
- GET by ID returns correct resource
- POST creates resource with ID
- PUT updates resource correctly
- DELETE removes resource
- Invalid input returns 400
- Not found returns 404
- Constraint violation returns 409
- Enrollment creates relationship
- Enrollment retrieval returns courses
- Search filters results correctly
- Pagination returns correct page

**Total:** 60+ tests with comprehensive coverage

## Common Tasks

### Creating a REST Endpoint

```java
@GetMapping("/{id}")
public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
    return studentService.getStudentById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
}
```

### Handling Validation Errors

```java
@PostMapping
public ResponseEntity<StudentDTO> createStudent(
    @Valid @RequestBody StudentDTO dto) {
    StudentDTO created = studentService.createStudent(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
}
```

### Testing an Endpoint

```java
@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private StudentService studentService;
    
    @Test
    public void testGetAllStudents() throws Exception {
        mockMvc.perform(get("/api/students"))
            .andExpect(status().isOk());
    }
}
```

## Troubleshooting

### Issue: "404 Not Found"
**Solution:** Verify the endpoint path is correct and the resource exists in the database.

### Issue: "400 Bad Request - Validation failed"
**Solution:** Check the request body matches the DTO structure and all required fields are present.

### Issue: "409 Conflict - Duplicate entry"
**Solution:** The resource already exists. Check for unique constraints and handle duplicates appropriately.

### Issue: "CORS error in browser"
**Solution:** Configure CORS in CorsConfig to allow requests from your frontend domain.

### Issue: "Swagger UI not loading"
**Solution:** Verify springdoc-openapi dependency is added and application is running.

## Best Practices

1. **Use DTOs for API responses** - Don't expose entities directly
2. **Validate input data** - Use @Valid and validation annotations
3. **Return appropriate status codes** - Use correct HTTP status codes
4. **Handle errors gracefully** - Use global exception handler
5. **Document APIs** - Use Swagger/OpenAPI annotations
6. **Use meaningful error messages** - Help clients understand what went wrong
7. **Implement pagination** - For large datasets
8. **Use search and filtering** - Allow clients to query data
9. **Configure CORS properly** - Allow cross-origin requests safely
10. **Test thoroughly** - Use both unit and integration tests

## Configuration

### Application Properties

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/

# Swagger Configuration
springdoc.api.title=Online School API
springdoc.api.description=REST API for Online School Management System
springdoc.api.version=1.0.0
springdoc.swagger-ui.path=/swagger-ui.html

# Logging
logging.level.com.bootcamp.onlineschool=DEBUG
logging.level.org.springframework.web=INFO
```

## Lab Progression

This is **Lab 6** of the bootcamp curriculum:

- **Lab 1:** Java Fundamentals - Core Java and OOP ✅
- **Lab 2:** JUnit Testing - Advanced testing patterns ✅
- **Lab 3:** Spring Boot Basics - REST APIs and services ✅
- **Lab 4:** Database & SQL - SQL fundamentals (coming soon)
- **Lab 5:** ORM & JPA - Object-Relational Mapping (coming soon)
- **Lab 6:** Backend API - RESTful API development (current)
- **Lab 7:** Maven & Build Tools - Build automation (coming soon)
- **Lab 8:** Frontend HTML & CSS - Web basics (coming soon)
- **Lab 9:** ReactJS - Modern frontend (coming soon)
- **Lab 10:** Full Stack Integration - Complete application (coming soon)

## Switching Between Labs

```bash
# View all available branches
git branch -a

# Switch to Lab 6
git checkout lab/api-6-backend

# Switch to other labs
git checkout lab/java-1-fundamentals
git checkout lab/junit-2-testing
git checkout lab/springboot-3-basics
git checkout lab/orm-5-jpa
git checkout lab/maven-7-build
```

## Resources

- [Spring Boot REST Documentation](https://spring.io/guides/gs/rest-service/)
- [Spring MVC Documentation](https://spring.io/projects/spring-framework)
- [Swagger/OpenAPI Documentation](https://swagger.io/)
- [HTTP Status Codes](https://httpwg.org/specs/rfc7231.html#status.codes)
- [REST API Best Practices](https://restfulapi.net/)
- [Spring Data REST](https://spring.io/projects/spring-data-rest)

## Next Steps

1. Review the requirements in `.kiro/specs/lab-6-backend-api/requirements.md`
2. Study the design in `.kiro/specs/lab-6-backend-api/design.md`
3. Execute tasks from `.kiro/specs/lab-6-backend-api/tasks.md`
4. Build the project: `mvn clean build`
5. Run the application: `mvn spring-boot:run`
6. Access Swagger UI: `http://localhost:8080/swagger-ui.html`
7. Test endpoints with curl or Postman

---

**Last Updated:** November 22, 2025  
**Lab:** 6 - Backend API  
**Status:** Specification Complete, Ready for Implementation
