# Lab 6: Backend API - Requirements Document

## Introduction

Lab 6 focuses on building RESTful APIs using Spring Boot and Spring MVC. Students will learn how to create REST endpoints, handle HTTP requests and responses, implement proper HTTP status codes, validate input data, handle errors gracefully, and document APIs using Swagger/OpenAPI. This lab builds on the JPA foundation from Lab 5 and adds a complete REST API layer for the online school system.

## Glossary

- **REST (Representational State Transfer):** An architectural style for building web services using HTTP
- **HTTP Methods:** GET (retrieve), POST (create), PUT (update), DELETE (remove), PATCH (partial update)
- **HTTP Status Codes:** Standardized response codes (200 OK, 201 Created, 400 Bad Request, 404 Not Found, 500 Server Error)
- **@RestController:** Spring annotation that combines @Controller and @ResponseBody for REST endpoints
- **@RequestMapping:** Annotation to map HTTP requests to controller methods
- **@PathVariable:** Annotation to extract values from URL path
- **@RequestParam:** Annotation to extract query parameters from URL
- **@RequestBody:** Annotation to bind request body to method parameter
- **@ResponseStatus:** Annotation to set HTTP response status code
- **DTO (Data Transfer Object):** Object used to transfer data between API and client
- **Swagger/OpenAPI:** Tools for documenting and testing REST APIs
- **CORS (Cross-Origin Resource Sharing):** Mechanism to allow requests from different domains
- **Content Negotiation:** Process of selecting appropriate response format (JSON, XML)
- **Validation:** Process of checking input data against defined rules

## Requirements

### Requirement 1: REST Controller Configuration and Mapping

**User Story:** As a developer, I want to create REST controllers with proper HTTP method mappings, so that clients can interact with the API using standard HTTP verbs.

#### Acceptance Criteria

1. WHEN a StudentController is created THEN the system SHALL use @RestController annotation and map to /api/students base path
2. WHEN a GET request is made to /api/students THEN the system SHALL return all students with HTTP 200 status
3. WHEN a GET request is made to /api/students/{id} THEN the system SHALL return the student with matching ID or HTTP 404 if not found
4. WHEN a POST request is made to /api/students THEN the system SHALL create a new student and return HTTP 201 with Location header
5. WHEN a PUT request is made to /api/students/{id} THEN the system SHALL update the student and return HTTP 200 with updated data

### Requirement 2: HTTP Methods and Status Codes

**User Story:** As a developer, I want to use appropriate HTTP methods and status codes, so that the API follows REST conventions and clients understand the operation results.

#### Acceptance Criteria

1. WHEN a resource is successfully retrieved THEN the system SHALL return HTTP 200 OK
2. WHEN a resource is successfully created THEN the system SHALL return HTTP 201 Created with Location header
3. WHEN a resource is successfully updated THEN the system SHALL return HTTP 200 OK
4. WHEN a resource is successfully deleted THEN the system SHALL return HTTP 204 No Content
5. WHEN a resource is not found THEN the system SHALL return HTTP 404 Not Found

### Requirement 3: Request and Response Handling

**User Story:** As a developer, I want to handle request bodies and return appropriate response objects, so that data is properly serialized and deserialized.

#### Acceptance Criteria

1. WHEN a POST request contains a StudentDTO THEN the system SHALL deserialize it to a Student entity
2. WHEN a response is returned THEN the system SHALL serialize it to JSON format automatically
3. WHEN a request has invalid content type THEN the system SHALL return HTTP 415 Unsupported Media Type
4. WHEN a response is generated THEN the system SHALL include appropriate Content-Type header
5. WHEN a request body is missing required fields THEN the system SHALL return HTTP 400 Bad Request

### Requirement 4: Input Validation and Error Handling

**User Story:** As a developer, I want to validate input data and handle errors gracefully, so that invalid requests are rejected with clear error messages.

#### Acceptance Criteria

1. WHEN a POST request contains invalid email THEN the system SHALL return HTTP 400 with validation error message
2. WHEN a POST request contains null required field THEN the system SHALL return HTTP 400 with field error details
3. WHEN a database constraint is violated THEN the system SHALL return HTTP 409 Conflict with error message
4. WHEN an unexpected error occurs THEN the system SHALL return HTTP 500 Internal Server Error with error details
5. WHEN validation fails THEN the system SHALL provide clear error messages indicating which fields failed

### Requirement 5: Global Exception Handling

**User Story:** As a developer, I want to implement global exception handling, so that all errors are handled consistently across the API.

#### Acceptance Criteria

1. WHEN an EntityNotFoundException is thrown THEN the system SHALL return HTTP 404 with error message
2. WHEN a ConstraintViolationException is thrown THEN the system SHALL return HTTP 409 with constraint details
3. WHEN a MethodArgumentNotValidException is thrown THEN the system SHALL return HTTP 400 with validation errors
4. WHEN an unexpected exception is thrown THEN the system SHALL return HTTP 500 with error message
5. WHEN an error response is returned THEN the system SHALL include timestamp, status, message, and path

### Requirement 6: API Documentation with Swagger

**User Story:** As a developer, I want to document the API using Swagger/OpenAPI, so that clients can discover and understand available endpoints.

#### Acceptance Criteria

1. WHEN the application starts THEN the system SHALL generate Swagger documentation automatically
2. WHEN accessing /swagger-ui.html THEN the system SHALL display interactive API documentation
3. WHEN viewing API documentation THEN the system SHALL show all endpoints with HTTP methods and paths
4. WHEN viewing endpoint details THEN the system SHALL show request/response models and status codes
5. WHEN viewing endpoint details THEN the system SHALL show parameter descriptions and validation rules

### Requirement 7: CRUD Operations for Students

**User Story:** As a developer, I want to implement complete CRUD operations for students, so that clients can manage student data through the API.

#### Acceptance Criteria

1. WHEN a GET request is made to /api/students THEN the system SHALL return a list of all students
2. WHEN a GET request is made to /api/students/{id} THEN the system SHALL return the student with matching ID
3. WHEN a POST request is made to /api/students THEN the system SHALL create a new student and return it with generated ID
4. WHEN a PUT request is made to /api/students/{id} THEN the system SHALL update the student with matching ID
5. WHEN a DELETE request is made to /api/students/{id} THEN the system SHALL delete the student with matching ID

### Requirement 8: CRUD Operations for Courses

**User Story:** As a developer, I want to implement complete CRUD operations for courses, so that clients can manage course data through the API.

#### Acceptance Criteria

1. WHEN a GET request is made to /api/courses THEN the system SHALL return a list of all courses
2. WHEN a GET request is made to /api/courses/{id} THEN the system SHALL return the course with matching ID
3. WHEN a POST request is made to /api/courses THEN the system SHALL create a new course and return it with generated ID
4. WHEN a PUT request is made to /api/courses/{id} THEN the system SHALL update the course with matching ID
5. WHEN a DELETE request is made to /api/courses/{id} THEN the system SHALL delete the course with matching ID

### Requirement 9: Student-Course Enrollment Operations

**User Story:** As a developer, I want to implement enrollment operations, so that students can enroll in and withdraw from courses through the API.

#### Acceptance Criteria

1. WHEN a POST request is made to /api/students/{studentId}/courses/{courseId} THEN the system SHALL enroll the student in the course
2. WHEN a DELETE request is made to /api/students/{studentId}/courses/{courseId} THEN the system SHALL remove the student from the course
3. WHEN a GET request is made to /api/students/{studentId}/courses THEN the system SHALL return all courses the student is enrolled in
4. WHEN a GET request is made to /api/courses/{courseId}/students THEN the system SHALL return all students enrolled in the course
5. WHEN enrolling a student already enrolled THEN the system SHALL return HTTP 409 Conflict

### Requirement 10: Search and Filter Operations

**User Story:** As a developer, I want to implement search and filter operations, so that clients can query data based on specific criteria.

#### Acceptance Criteria

1. WHEN a GET request is made to /api/students?name=John THEN the system SHALL return students whose names contain "John"
2. WHEN a GET request is made to /api/students?minGpa=3.5 THEN the system SHALL return students with GPA >= 3.5
3. WHEN a GET request is made to /api/courses?title=Math THEN the system SHALL return courses whose titles contain "Math"
4. WHEN a GET request is made to /api/courses?credits=3 THEN the system SHALL return courses with exactly 3 credits
5. WHEN multiple query parameters are provided THEN the system SHALL apply all filters in combination

### Requirement 11: Pagination and Sorting

**User Story:** As a developer, I want to implement pagination and sorting, so that clients can retrieve large datasets efficiently.

#### Acceptance Criteria

1. WHEN a GET request includes page and size parameters THEN the system SHALL return paginated results
2. WHEN a GET request includes sort parameter THEN the system SHALL return results sorted by specified field
3. WHEN pagination is applied THEN the system SHALL include total count and page information in response
4. WHEN sort direction is specified THEN the system SHALL sort in ascending or descending order
5. WHEN invalid page or size is provided THEN the system SHALL return HTTP 400 with error message

### Requirement 12: API Testing

**User Story:** As a developer, I want to test API endpoints, so that I can verify they work correctly and handle errors properly.

#### Acceptance Criteria

1. WHEN a controller test is executed THEN the system SHALL use @WebMvcTest to load only web layer components
2. WHEN testing a GET endpoint THEN the system SHALL verify the response status and content
3. WHEN testing a POST endpoint THEN the system SHALL verify the resource is created and returned
4. WHEN testing error scenarios THEN the system SHALL verify appropriate error status and message
5. WHEN testing with invalid input THEN the system SHALL verify validation errors are returned

### Requirement 13: API Integration Testing

**User Story:** As a developer, I want to test the complete API flow, so that I can verify end-to-end functionality works correctly.

#### Acceptance Criteria

1. WHEN an integration test is executed THEN the system SHALL use @SpringBootTest with TestRestTemplate
2. WHEN testing a complete workflow THEN the system SHALL verify all operations work together correctly
3. WHEN testing error handling THEN the system SHALL verify errors are handled consistently
4. WHEN testing relationships THEN the system SHALL verify enrollment operations work correctly
5. WHEN testing with real database THEN the system SHALL verify data persistence and retrieval

### Requirement 14: CORS Configuration

**User Story:** As a developer, I want to configure CORS, so that frontend applications can make requests to the API from different domains.

#### Acceptance Criteria

1. WHEN a request is made from a different domain THEN the system SHALL include CORS headers in response
2. WHEN CORS is configured THEN the system SHALL allow specified origins to access the API
3. WHEN a preflight request is made THEN the system SHALL respond with appropriate CORS headers
4. WHEN CORS is not configured THEN the system SHALL reject cross-origin requests
5. WHEN credentials are needed THEN the system SHALL include appropriate CORS headers for authentication
