# Lab 6: Backend API - Implementation Plan

## Overview
This implementation plan converts the Lab 6 Backend API design into actionable coding tasks. Each task builds incrementally on previous tasks, with no orphaned code. Tasks are organized to validate core functionality early through tests.

---

## Implementation Tasks

- [ ] 1. Set up project structure and dependencies
  - Update pom.xml with spring-boot-starter-web and springdoc-openapi dependencies
  - Add spring-boot-starter-validation for input validation
  - Add springdoc-openapi-starter-webmvc-ui for Swagger UI
  - Create directory structure for controllers, exceptions, and DTOs
  - _Requirements: 1.1, 6.1_

- [ ] 2. Create DTO classes for API requests and responses
  - [ ] 2.1 Create StudentDTO class
    - Add fields: id, name, email, gpa
    - Add validation annotations: @NotNull, @Email, @Min, @Max
    - Add constructors and getters/setters
    - _Requirements: 3.1, 4.1, 4.2_

  - [ ] 2.2 Create CourseDTO class
    - Add fields: id, code, title, description, credits
    - Add validation annotations: @NotNull, @NotBlank, @Min, @Max
    - Add constructors and getters/setters
    - _Requirements: 3.1, 4.1, 4.2_

  - [ ] 2.3 Create ErrorResponse class
    - Add fields: timestamp, status, error, message, path, fieldErrors
    - Add constructors and getters/setters
    - _Requirements: 5.5_

- [ ] 3. Create global exception handler
  - [ ] 3.1 Implement GlobalExceptionHandler class
    - Add @ControllerAdvice annotation
    - Implement handleEntityNotFound() for 404 errors
    - Implement handleConstraintViolation() for 409 errors
    - Implement handleMethodArgumentNotValid() for 400 validation errors
    - Implement handleHttpMessageNotReadable() for 400 bad request
    - Implement handleException() for 500 errors
    - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5_

  - [ ] 3.2 Write property test for exception handling
    - **Property 7: Not Found Returns 404**
    - **Validates: Requirements 1.3, 2.5, 5.1**

- [ ] 4. Create StudentController with CRUD operations
  - [ ] 4.1 Implement StudentController class
    - Add @RestController annotation with @RequestMapping("/api/students")
    - Implement GET /api/students - return all students with HTTP 200
    - Implement GET /api/students/{id} - return student by ID or 404
    - Implement POST /api/students - create student and return 201 with Location header
    - Implement PUT /api/students/{id} - update student and return 200
    - Implement DELETE /api/students/{id} - delete student and return 204
    - Add @Autowired StudentService dependency
    - _Requirements: 1.1, 1.2, 1.3, 1.4, 1.5, 7.1, 7.2, 7.3, 7.4, 7.5_

  - [ ] 4.2 Write property test for GET all students
    - **Property 1: GET All Returns All Resources**
    - **Validates: Requirements 7.1, 10.1**

- [ ] 5. Create CourseController with CRUD operations
  - [ ] 5.1 Implement CourseController class
    - Add @RestController annotation with @RequestMapping("/api/courses")
    - Implement GET /api/courses - return all courses with HTTP 200
    - Implement GET /api/courses/{id} - return course by ID or 404
    - Implement POST /api/courses - create course and return 201 with Location header
    - Implement PUT /api/courses/{id} - update course and return 200
    - Implement DELETE /api/courses/{id} - delete course and return 204
    - Add @Autowired CourseService dependency
    - _Requirements: 1.1, 8.1, 8.2, 8.3, 8.4, 8.5_

  - [ ] 5.2 Write property test for GET all courses
    - **Property 1: GET All Returns All Resources**
    - **Validates: Requirements 8.1, 10.1**

- [ ] 6. Checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 7. Add enrollment endpoints to StudentController
  - [ ] 7.1 Add enrollment operations to StudentController
    - Implement POST /api/students/{studentId}/courses/{courseId} - enroll student
    - Implement DELETE /api/students/{studentId}/courses/{courseId} - withdraw student
    - Implement GET /api/students/{studentId}/courses - get student's courses
    - Add error handling for duplicate enrollment (409)
    - _Requirements: 9.1, 9.2, 9.3, 9.5_

  - [ ] 7.2 Add GET enrolled students to CourseController
    - Implement GET /api/courses/{courseId}/students - get enrolled students
    - _Requirements: 9.4_

  - [ ] 7.3 Write property test for enrollment operations
    - **Property 9: Enrollment Creates Relationship**
    - **Validates: Requirements 9.1, 9.5**

- [ ] 8. Add search and filter operations
  - [ ] 8.1 Add search parameters to StudentController
    - Add @RequestParam for name search
    - Add @RequestParam for minGpa filter
    - Implement filtering logic in service layer
    - _Requirements: 10.1, 10.2_

  - [ ] 8.2 Add search parameters to CourseController
    - Add @RequestParam for title search
    - Add @RequestParam for credits filter
    - Implement filtering logic in service layer
    - _Requirements: 10.3, 10.4_

  - [ ] 8.3 Write property test for search filtering
    - **Property 11: Search Filters Results Correctly**
    - **Validates: Requirements 10.1, 10.2**

- [ ] 9. Add pagination and sorting support
  - [ ] 9.1 Add pagination to StudentController
    - Add @RequestParam for page and size
    - Add @RequestParam for sort
    - Use Spring Data Pageable interface
    - Return PageResponse with pagination metadata
    - _Requirements: 11.1, 11.2, 11.3, 11.4_

  - [ ] 9.2 Add pagination to CourseController
    - Add @RequestParam for page and size
    - Add @RequestParam for sort
    - Use Spring Data Pageable interface
    - Return PageResponse with pagination metadata
    - _Requirements: 11.1, 11.2, 11.3, 11.4_

  - [ ] 9.3 Write property test for pagination
    - **Property 12: Pagination Returns Correct Page**
    - **Validates: Requirements 11.1, 11.3**

- [ ] 10. Checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 11. Add Swagger/OpenAPI documentation
  - [ ] 11.1 Configure Swagger in application.properties
    - Set springdoc.api.title
    - Set springdoc.api.description
    - Set springdoc.api.version
    - _Requirements: 6.1, 6.2_

  - [ ] 11.2 Add Swagger annotations to StudentController
    - Add @Operation annotations to all endpoints
    - Add @ApiResponse annotations for status codes
    - Add @Parameter annotations for parameters
    - _Requirements: 6.3, 6.4, 6.5_

  - [ ] 11.3 Add Swagger annotations to CourseController
    - Add @Operation annotations to all endpoints
    - Add @ApiResponse annotations for status codes
    - Add @Parameter annotations for parameters
    - _Requirements: 6.3, 6.4, 6.5_

- [ ] 12. Create controller tests
  - [ ] 12.1 Implement StudentControllerTest with @WebMvcTest
    - Test GET /api/students returns 200 with all students
    - Test GET /api/students/{id} returns 200 with correct student
    - Test GET /api/students/{id} returns 404 for non-existent ID
    - Test POST /api/students creates student and returns 201
    - Test POST /api/students with invalid data returns 400
    - Test PUT /api/students/{id} updates student and returns 200
    - Test DELETE /api/students/{id} deletes student and returns 204
    - _Requirements: 12.2, 12.3, 12.4, 12.5_

  - [ ] 12.2 Implement CourseControllerTest with @WebMvcTest
    - Test GET /api/courses returns 200 with all courses
    - Test GET /api/courses/{id} returns 200 with correct course
    - Test GET /api/courses/{id} returns 404 for non-existent ID
    - Test POST /api/courses creates course and returns 201
    - Test POST /api/courses with invalid data returns 400
    - Test PUT /api/courses/{id} updates course and returns 200
    - Test DELETE /api/courses/{id} deletes course and returns 204
    - _Requirements: 12.2, 12.3, 12.4, 12.5_

  - [ ] 12.3 Write property test for POST creates resource
    - **Property 3: POST Creates Resource with Generated ID**
    - **Validates: Requirements 7.3, 1.4, 2.2**

- [ ] 13. Create exception handler tests
  - [ ] 13.1 Implement GlobalExceptionHandlerTest
    - Test EntityNotFoundException returns 404
    - Test ConstraintViolationException returns 409
    - Test MethodArgumentNotValidException returns 400
    - Test generic Exception returns 500
    - Test error response includes all required fields
    - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5_

  - [ ] 13.2 Write property test for constraint violation
    - **Property 8: Constraint Violation Returns 409**
    - **Validates: Requirements 4.3, 5.2**

- [ ] 14. Create integration tests
  - [ ] 14.1 Implement ApiIntegrationTest with @SpringBootTest
    - Test complete student CRUD workflow
    - Test complete course CRUD workflow
    - Test student enrollment workflow
    - Test search and filter operations
    - Test pagination and sorting
    - Test error handling end-to-end
    - _Requirements: 13.2, 13.3, 13.4, 13.5_

  - [ ] 14.2 Write property test for complete workflow
    - **Property 10: Enrollment Retrieval Returns Correct Courses**
    - **Validates: Requirements 9.3, 10.1**

- [ ] 15. Add CORS configuration
  - [ ] 15.1 Create CorsConfig class
    - Add @Configuration annotation
    - Implement WebMvcConfigurer
    - Configure CORS for all origins or specific origins
    - Allow all HTTP methods
    - Allow credentials if needed
    - _Requirements: 14.1, 14.2, 14.3, 14.5_

  - [ ] 15.2 Write property test for CORS headers
    - **Property 14: CORS Configuration**
    - **Validates: Requirements 14.1, 14.2**

- [ ] 16. Checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 17. Add response wrapper and standardization
  - [ ] 17.1 Create ApiResponse wrapper class
    - Add fields: success, message, data, timestamp
    - Add constructors for success and error responses
    - _Requirements: 3.2, 5.5_

  - [ ] 17.2 Update controllers to use ApiResponse
    - Wrap all successful responses in ApiResponse
    - Ensure consistent response format
    - _Requirements: 3.2, 5.5_

- [ ] 18. Add request validation annotations
  - [ ] 18.1 Add validation to StudentDTO
    - Add @NotNull to required fields
    - Add @Email to email field
    - Add @Min/@Max to GPA field
    - _Requirements: 4.1, 4.2, 4.5_

  - [ ] 18.2 Add validation to CourseDTO
    - Add @NotNull to required fields
    - Add @NotBlank to string fields
    - Add @Min/@Max to credits field
    - _Requirements: 4.1, 4.2, 4.5_

  - [ ] 18.3 Write property test for validation
    - **Property 6: Invalid Input Returns 400 Bad Request**
    - **Validates: Requirements 4.1, 4.2, 4.5**

- [ ] 19. Create comprehensive API tests
  - [ ] 19.1 Test all HTTP status codes
    - Test 200 OK responses
    - Test 201 Created responses
    - Test 204 No Content responses
    - Test 400 Bad Request responses
    - Test 404 Not Found responses
    - Test 409 Conflict responses
    - _Requirements: 2.1, 2.2, 2.3, 2.4, 2.5_

  - [ ] 19.2 Write property test for PUT updates resource
    - **Property 4: PUT Updates Resource Correctly**
    - **Validates: Requirements 7.4, 1.5, 2.1**

  - [ ] 19.3 Write property test for DELETE removes resource
    - **Property 5: DELETE Removes Resource**
    - **Validates: Requirements 7.5, 2.4**

- [ ] 20. Final Checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 21. Create Lab 6 documentation
  - [ ] 21.1 Create LAB_6_BACKEND_API.md documentation file
    - Document REST API principles
    - Document all endpoints with examples
    - Document request/response formats
    - Document error handling
    - Document Swagger documentation
    - Include code examples
    - Include troubleshooting guide
    - _Requirements: All_

- [ ] 22. Final verification and cleanup
  - [ ] 22.1 Verify all tests pass (60+ tests)
    - Run full test suite
    - Verify code coverage
    - Check for any warnings or issues
    - _Requirements: All_

  - [ ] 22.2 Verify application runs successfully
    - Start application with mvn spring-boot:run
    - Verify Swagger UI is accessible at /swagger-ui.html
    - Test API endpoints with curl or Postman
    - Verify error handling works correctly
    - _Requirements: 1.1, 6.1, 6.2_

  - [ ] 22.3 Commit and push to GitHub
    - Commit all changes with descriptive message
    - Push to lab/api-6-backend branch
    - _Requirements: All_
