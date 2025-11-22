# Lab 6: Backend API - Design Document

## Overview

Lab 6 implements a complete RESTful API for the online school system using Spring Boot and Spring MVC. The design focuses on creating REST endpoints for CRUD operations, implementing proper HTTP status codes, validating input data, handling errors globally, and documenting the API with Swagger/OpenAPI. The API provides endpoints for managing students, courses, and student-course enrollments with comprehensive error handling and validation.

## Architecture

### REST API Layered Architecture

```
┌─────────────────────────────────────────┐
│         Client Layer                    │
│  (Web Browser, Mobile App, etc.)        │
└──────────────┬──────────────────────────┘
               │ HTTP Requests/Responses
┌──────────────▼──────────────────────────┐
│      REST Controller Layer              │
│  (StudentController, CourseController)  │
│  - HTTP method mapping                  │
│  - Request/response handling            │
│  - Status code management               │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Service Layer                      │
│  (StudentService, CourseService)        │
│  - Business logic                       │
│  - Transaction management               │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Repository Layer                   │
│  (StudentRepository, CourseRepository)  │
│  - Data access                          │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Database Layer                     │
│  (H2 In-Memory Database)                │
└─────────────────────────────────────────┘
```

### REST API Design Principles

1. **Resource-Oriented:** URLs represent resources, not actions
2. **HTTP Methods:** Use GET, POST, PUT, DELETE, PATCH appropriately
3. **Status Codes:** Return correct HTTP status codes
4. **Stateless:** Each request contains all information needed
5. **Representation:** Resources are represented in JSON format
6. **Error Handling:** Consistent error response format
7. **Validation:** Input validation before processing
8. **Documentation:** API is self-documenting with Swagger

## Components and Interfaces

### REST Controllers

#### StudentController
- **Base Path:** `/api/students`
- **Endpoints:**
  - `GET /api/students` - Get all students (with pagination, sorting, filtering)
  - `GET /api/students/{id}` - Get student by ID
  - `POST /api/students` - Create new student
  - `PUT /api/students/{id}` - Update student
  - `DELETE /api/students/{id}` - Delete student
  - `GET /api/students/{id}/courses` - Get student's courses
  - `POST /api/students/{studentId}/courses/{courseId}` - Enroll in course
  - `DELETE /api/students/{studentId}/courses/{courseId}` - Withdraw from course

#### CourseController
- **Base Path:** `/api/courses`
- **Endpoints:**
  - `GET /api/courses` - Get all courses (with pagination, sorting, filtering)
  - `GET /api/courses/{id}` - Get course by ID
  - `POST /api/courses` - Create new course
  - `PUT /api/courses/{id}` - Update course
  - `DELETE /api/courses/{id}` - Delete course
  - `GET /api/courses/{id}/students` - Get enrolled students

### Global Exception Handler

#### GlobalExceptionHandler
- **Responsibility:** Handle all exceptions globally
- **Methods:**
  - handleEntityNotFound() - Returns 404
  - handleConstraintViolation() - Returns 409
  - handleMethodArgumentNotValid() - Returns 400
  - handleHttpMessageNotReadable() - Returns 400
  - handleException() - Returns 500

### Error Response Model

```java
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private Map<String, String> fieldErrors; // For validation errors
}
```

## Data Models

### API Request/Response DTOs

#### StudentDTO
```
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "gpa": 3.75
}
```

#### CourseDTO
```
{
  "id": 1,
  "code": "CS101",
  "title": "Introduction to Computer Science",
  "description": "Learn the basics of programming",
  "credits": 3
}
```

#### EnrollmentDTO
```
{
  "studentId": 1,
  "courseId": 1,
  "enrolledDate": "2025-01-15T10:30:00"
}
```

### API Response Wrapper

```java
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
}
```

### Pagination Response

```java
public class PageResponse<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
}
```

## API Endpoints Reference

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

## Correctness Properties

A property is a characteristic or behavior that should hold true across all valid executions of a system—essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.

### Property 1: GET All Returns All Resources
*For any* set of students persisted in the database, calling GET /api/students should return a list containing all persisted students with no duplicates.

**Validates: Requirements 7.1, 10.1**

### Property 2: GET By ID Returns Correct Resource
*For any* student persisted with a specific ID, calling GET /api/students/{id} should return that exact student with matching data.

**Validates: Requirements 7.2, 1.3**

### Property 3: POST Creates Resource with Generated ID
*For any* valid StudentDTO, calling POST /api/students should create a student, return HTTP 201, and include a non-null positive ID in response.

**Validates: Requirements 7.3, 1.4, 2.2**

### Property 4: PUT Updates Resource Correctly
*For any* persisted student, calling PUT /api/students/{id} with updated data should modify the student and return HTTP 200 with updated values.

**Validates: Requirements 7.4, 1.5, 2.1**

### Property 5: DELETE Removes Resource
*For any* persisted student, calling DELETE /api/students/{id} should remove the student and return HTTP 204, and subsequent GET should return 404.

**Validates: Requirements 7.5, 2.4**

### Property 6: Invalid Input Returns 400 Bad Request
*For any* StudentDTO with invalid data (null name, invalid email, GPA outside range), calling POST /api/students should return HTTP 400 with validation error details.

**Validates: Requirements 4.1, 4.2, 4.5**

### Property 7: Not Found Returns 404
*For any* non-existent student ID, calling GET /api/students/{id} should return HTTP 404 with error message.

**Validates: Requirements 1.3, 2.5, 5.1**

### Property 8: Constraint Violation Returns 409
*For any* StudentDTO with duplicate email, calling POST /api/students should return HTTP 409 Conflict with error message.

**Validates: Requirements 4.3, 5.2**

### Property 9: Enrollment Creates Relationship
*For any* student and course, calling POST /api/students/{studentId}/courses/{courseId} should create the enrollment and return HTTP 200.

**Validates: Requirements 9.1, 9.5**

### Property 10: Enrollment Retrieval Returns Correct Courses
*For any* student enrolled in courses, calling GET /api/students/{id}/courses should return all enrolled courses.

**Validates: Requirements 9.3, 10.1**

### Property 11: Search Filters Results Correctly
*For any* search query with name parameter, calling GET /api/students?name=X should return only students whose names contain X (case-insensitive).

**Validates: Requirements 10.1, 10.2**

### Property 12: Pagination Returns Correct Page
*For any* page and size parameters, calling GET /api/students?page=0&size=10 should return correct page of results with accurate total count.

**Validates: Requirements 11.1, 11.3**

## Error Handling

### HTTP Status Codes

- **200 OK:** Successful GET, PUT, or POST (with response body)
- **201 Created:** Successful POST with Location header
- **204 No Content:** Successful DELETE
- **400 Bad Request:** Invalid input or malformed request
- **404 Not Found:** Resource not found
- **409 Conflict:** Constraint violation or duplicate resource
- **415 Unsupported Media Type:** Invalid content type
- **500 Internal Server Error:** Unexpected server error

### Exception Mapping

| Exception | Status | Message |
|-----------|--------|---------|
| EntityNotFoundException | 404 | Resource not found |
| ConstraintViolationException | 409 | Constraint violation |
| MethodArgumentNotValidException | 400 | Validation failed |
| HttpMessageNotReadableException | 400 | Invalid request body |
| Exception | 500 | Internal server error |

### Error Response Format

```json
{
  "timestamp": "2025-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/students",
  "fieldErrors": {
    "email": "Invalid email format",
    "name": "Name cannot be null"
  }
}
```

## Testing Strategy

### Unit Testing Approach
- Test controller methods with @WebMvcTest
- Mock service layer dependencies
- Test request/response handling
- Test error scenarios and status codes
- Test input validation

### Integration Testing Approach
- Use @SpringBootTest with TestRestTemplate
- Test complete API workflows
- Test error handling end-to-end
- Test relationship operations
- Test with real database

### API Testing Approach
- Test all CRUD operations
- Test search and filter operations
- Test pagination and sorting
- Test error responses
- Test status codes and headers

### Test Organization
```
src/test/java/com/bootcamp/onlineschool/
├── controller/
│   ├── StudentControllerTest.java
│   └── CourseControllerTest.java
├── exception/
│   └── GlobalExceptionHandlerTest.java
└── integration/
    └── ApiIntegrationTest.java
```

### Test Coverage Goals
- **Controller Tests:** 25+ tests covering all endpoints
- **Exception Handler Tests:** 10+ tests covering error scenarios
- **Integration Tests:** 15+ tests covering complete workflows
- **Property-Based Tests:** 12 properties with 100+ iterations each
- **Total:** 60+ tests with comprehensive coverage

### Testing Best Practices
- Each test should be independent and isolated
- Use descriptive test names indicating what is being tested
- Mock service layer in controller tests
- Use real database in integration tests
- Test both happy path and error scenarios
- Verify response status, headers, and body
- Test validation error messages
