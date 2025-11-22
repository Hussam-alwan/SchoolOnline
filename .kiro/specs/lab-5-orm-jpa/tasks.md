# Lab 5: ORM & JPA - Implementation Plan

## Overview
This implementation plan converts the Lab 5 ORM & JPA design into actionable coding tasks. Each task builds incrementally on previous tasks, with no orphaned code. Tasks are organized to validate core functionality early through tests.

---

## Implementation Tasks

- [ ] 1. Set up project structure and dependencies
  - Update pom.xml with Spring Data JPA and H2 database dependencies
  - Add validation dependencies (spring-boot-starter-validation)
  - Configure Maven compiler and surefire plugins
  - Create directory structure for entities, repositories, services, and tests
  - _Requirements: 1.1, 10.1_

- [ ] 2. Create Student and Course entity classes
  - [ ] 2.1 Implement Student entity with JPA annotations
    - Create Student class with @Entity and @Table annotations
    - Add fields: id, name, email, gpa with appropriate @Column annotations
    - Add @Id and @GeneratedValue for auto-increment ID
    - Add @ManyToMany relationship to Course with join table configuration
    - Add validation annotations: @NotNull, @Email, @Min, @Max
    - Add createdAt and updatedAt fields with @CreationTimestamp and @UpdateTimestamp
    - _Requirements: 1.1, 1.2, 1.3, 9.1, 9.2, 9.3_

  - [ ] 2.2 Implement Course entity with JPA annotations
    - Create Course class with @Entity and @Table annotations
    - Add fields: id, code, title, description, credits with appropriate @Column annotations
    - Add @Id and @GeneratedValue for auto-increment ID
    - Add @ManyToMany relationship to Student with join table configuration
    - Add validation annotations: @NotNull, @NotBlank, @Min, @Max
    - Add createdAt and updatedAt fields with @CreationTimestamp and @UpdateTimestamp
    - _Requirements: 1.4, 1.3, 9.1, 9.2, 9.3_

  - [ ]* 2.3 Write property test for entity persistence round trip
    - **Property 1: Entity Persistence Round Trip**
    - **Validates: Requirements 1.5, 5.2**

- [ ] 3. Create repository interfaces
  - [ ] 3.1 Implement StudentRepository interface
    - Extend JpaRepository<Student, Long>
    - Add findByEmail(String email): Optional<Student>
    - Add findByNameContainingIgnoreCase(String name): List<Student>
    - Add findByCourses_Id(Long courseId): List<Student>
    - Add @Query method findStudentsWithMinGpa(Double minGpa): List<Student>
    - _Requirements: 3.1, 4.1, 4.2, 4.3, 4.4_

  - [ ] 3.2 Implement CourseRepository interface
    - Extend JpaRepository<Course, Long>
    - Add findByCode(String code): Optional<Course>
    - Add findByTitleContainingIgnoreCase(String title): List<Course>
    - Add @Query method findCoursesByMinCredits(Integer minCredits): List<Course>
    - _Requirements: 3.1, 4.1, 4.2, 4.3_

  - [ ]* 3.3 Write property test for repository save with ID generation
    - **Property 2: Repository Save Returns Generated ID**
    - **Validates: Requirements 1.2, 3.5**

- [ ] 4. Checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 5. Create service layer classes
  - [ ] 5.1 Implement StudentService with business logic
    - Create StudentService class with @Service annotation
    - Inject StudentRepository and CourseRepository
    - Implement registerStudent(StudentDTO): Student
    - Implement getStudentById(Long): Optional<Student>
    - Implement getAllStudents(): List<Student>
    - Implement updateStudent(Long, StudentDTO): Student
    - Implement deleteStudent(Long): void
    - Implement enrollInCourse(Long studentId, Long courseId): void
    - Implement searchByName(String): List<Student>
    - Implement getStudentsByGpa(Double): List<Student>
    - Add @Transactional annotation to methods that modify data
    - _Requirements: 5.1, 5.3, 7.1, 7.4_

  - [ ] 5.2 Implement CourseService with business logic
    - Create CourseService class with @Service annotation
    - Inject CourseRepository and StudentRepository
    - Implement createCourse(CourseDTO): Course
    - Implement getCourseById(Long): Optional<Course>
    - Implement getAllCourses(): List<Course>
    - Implement updateCourse(Long, CourseDTO): Course
    - Implement deleteCourse(Long): void
    - Implement getEnrolledStudents(Long courseId): List<Student>
    - Implement searchByTitle(String): List<Course>
    - Add @Transactional annotation to methods that modify data
    - _Requirements: 5.1, 5.3, 7.1, 7.4_

  - [ ]* 5.3 Write property test for findByEmail query correctness
    - **Property 3: FindByEmail Query Correctness**
    - **Validates: Requirements 4.1, 4.5**

- [ ] 6. Create DTO classes for data transfer
  - [ ] 6.1 Create StudentDTO class
    - Add fields: id, name, email, gpa
    - Add constructors and getters/setters
    - _Requirements: 1.1_

  - [ ] 6.2 Create CourseDTO class
    - Add fields: id, code, title, description, credits
    - Add constructors and getters/setters
    - _Requirements: 1.4_

- [ ] 7. Configure database and application properties
  - [ ] 7.1 Update application.properties for JPA configuration
    - Set spring.jpa.hibernate.ddl-auto=create-drop for development
    - Set spring.jpa.show-sql=true for debugging
    - Set spring.jpa.properties.hibernate.format_sql=true
    - Set spring.h2.console.enabled=true
    - Set spring.datasource.url=jdbc:h2:mem:testdb
    - _Requirements: 10.1, 10.3, 10.4_

  - [ ] 7.2 Create data initialization script (data.sql)
    - Add INSERT statements for sample Student data
    - Add INSERT statements for sample Course data
    - Add INSERT statements for student_course relationships
    - _Requirements: 10.2_

- [ ] 8. Checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 9. Create repository tests
  - [ ] 9.1 Implement StudentRepositoryTest with @DataJpaTest
    - Test save and retrieve student
    - Test findByEmail returns correct student
    - Test findByEmail returns empty for non-existent email
    - Test findByNameContainingIgnoreCase with various search terms
    - Test findByCourses_Id returns all students in course
    - Test findStudentsWithMinGpa returns only students above threshold
    - Test delete removes student from database
    - Test findAll returns all persisted students
    - _Requirements: 8.1, 8.2, 8.3, 8.4, 8.5_

  - [ ] 9.2 Implement CourseRepositoryTest with @DataJpaTest
    - Test save and retrieve course
    - Test findByCode returns correct course
    - Test findByCode returns empty for non-existent code
    - Test findByTitleContainingIgnoreCase with various search terms
    - Test findCoursesByMinCredits returns only courses with sufficient credits
    - Test delete removes course from database
    - Test findAll returns all persisted courses
    - _Requirements: 8.1, 8.2, 8.3, 8.4, 8.5_

  - [ ]* 9.3 Write property test for findAll returns all persisted students
    - **Property 4: FindAll Returns All Persisted Students**
    - **Validates: Requirements 3.3, 4.5**

- [ ] 10. Create entity validation tests
  - [ ] 10.1 Implement StudentValidationTest
    - Test that null name prevents persistence
    - Test that invalid email prevents persistence
    - Test that GPA < 0.0 prevents persistence
    - Test that GPA > 4.0 prevents persistence
    - Test that valid student persists successfully
    - Test validation error messages are clear
    - _Requirements: 9.1, 9.2, 9.3, 9.4, 9.5_

  - [ ] 10.2 Implement CourseValidationTest
    - Test that null code prevents persistence
    - Test that null title prevents persistence
    - Test that credits < 1 prevents persistence
    - Test that credits > 4 prevents persistence
    - Test that valid course persists successfully
    - Test validation error messages are clear
    - _Requirements: 9.1, 9.2, 9.3, 9.4, 9.5_

  - [ ]* 10.3 Write property test for delete removes entity
    - **Property 5: Delete Removes Entity**
    - **Validates: Requirements 3.5, 5.4**

- [ ] 11. Checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 12. Create relationship and cascade tests
  - [ ] 12.1 Implement StudentCourseRelationshipTest
    - Test many-to-many relationship establishment
    - Test bidirectional relationship consistency
    - Test student enrollment in course
    - Test course contains enrolled students
    - Test cascade delete behavior (course deletion doesn't delete students)
    - Test relationship cleanup on student deletion
    - _Requirements: 2.1, 2.2, 2.3, 2.4, 2.5, 6.1, 6.4_

  - [ ]* 12.2 Write property test for many-to-many relationship persistence
    - **Property 6: Many-to-Many Relationship Persistence**
    - **Validates: Requirements 2.1, 2.5**

  - [ ]* 12.3 Write property test for bidirectional relationship consistency
    - **Property 7: Bidirectional Relationship Consistency**
    - **Validates: Requirements 2.4, 6.4**

  - [ ]* 12.4 Write property test for cascade delete relationship cleanup
    - **Property 12: Cascade Delete Relationship Cleanup**
    - **Validates: Requirements 6.1, 6.3**

- [ ] 13. Create service layer tests
  - [ ] 13.1 Implement StudentServiceTest with @SpringBootTest
    - Mock StudentRepository and CourseRepository
    - Test registerStudent creates and persists student
    - Test getStudentById returns correct student
    - Test getAllStudents returns all students
    - Test updateStudent modifies student correctly
    - Test deleteStudent removes student
    - Test enrollInCourse adds course to student
    - Test searchByName returns matching students
    - Test getStudentsByGpa returns students above threshold
    - _Requirements: 5.1, 5.3, 7.1, 7.4_

  - [ ] 13.2 Implement CourseServiceTest with @SpringBootTest
    - Mock CourseRepository and StudentRepository
    - Test createCourse creates and persists course
    - Test getCourseById returns correct course
    - Test getAllCourses returns all courses
    - Test updateCourse modifies course correctly
    - Test deleteCourse removes course
    - Test getEnrolledStudents returns all enrolled students
    - Test searchByTitle returns matching courses
    - _Requirements: 5.1, 5.3, 7.1, 7.4_

  - [ ]* 13.3 Write property test for custom query method accuracy
    - **Property 8: Custom Query Method Accuracy**
    - **Validates: Requirements 4.2, 4.3**

- [ ] 14. Create transaction and constraint tests
  - [ ] 14.1 Implement TransactionManagementTest
    - Test that save operations are wrapped in transactions
    - Test that multiple operations are atomic
    - Test transaction rollback on constraint violation
    - Test that @Transactional manages transaction lifecycle
    - Test that database remains consistent after failed transaction
    - _Requirements: 7.1, 7.2, 7.3, 7.4, 7.5_

  - [ ]* 14.2 Write property test for email uniqueness constraint
    - **Property 9: Email Uniqueness Constraint**
    - **Validates: Requirements 9.2, 7.5**

  - [ ]* 14.3 Write property test for validation prevents invalid persistence
    - **Property 10: Validation Prevents Invalid Persistence**
    - **Validates: Requirements 9.1, 9.3**

  - [ ]* 14.4 Write property test for transaction rollback on error
    - **Property 11: Transaction Rollback on Error**
    - **Validates: Requirements 7.3, 7.5**

- [ ] 15. Checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 16. Create integration tests
  - [ ] 16.1 Implement StudentCourseIntegrationTest
    - Test complete student registration workflow
    - Test student enrollment in multiple courses
    - Test course with multiple enrolled students
    - Test student update with course changes
    - Test student deletion with relationship cleanup
    - Test course deletion with relationship cleanup
    - Test complex queries across relationships
    - _Requirements: 2.1, 2.2, 2.3, 2.4, 2.5, 5.1, 5.3, 7.1, 7.4_

- [ ] 17. Create application configuration
  - [ ] 17.1 Create AppConfig class for Spring configuration
    - Add any custom bean configurations if needed
    - Configure transaction management
    - _Requirements: 1.1, 7.1_

- [ ] 18. Create main application class
  - [ ] 18.1 Create OnlineSchoolApplication class
    - Add @SpringBootApplication annotation
    - Add main method to start application
    - Add CommandLineRunner to demonstrate functionality
    - _Requirements: 1.1, 10.1_

- [ ] 19. Final Checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 20. Create Lab 5 documentation
  - [ ] 20.1 Create LAB_5_ORM_JPA.md documentation file
    - Document JPA entity configuration
    - Document repository pattern implementation
    - Document service layer architecture
    - Document relationship management
    - Document transaction management
    - Document testing strategies
    - Include code examples for each concept
    - Include troubleshooting guide
    - _Requirements: All_

- [ ] 21. Final verification and cleanup
  - [ ] 21.1 Verify all tests pass (75+ tests)
    - Run full test suite
    - Verify code coverage
    - Check for any warnings or issues
    - _Requirements: All_

  - [ ] 21.2 Verify application runs successfully
    - Start application with mvn spring-boot:run
    - Verify H2 console is accessible
    - Verify sample data is loaded
    - Test basic CRUD operations
    - _Requirements: 10.1, 10.2, 10.4, 10.5_

  - [ ] 21.3 Commit and push to GitHub
    - Commit all changes with descriptive message
    - Push to lab/orm-5-jpa branch
    - _Requirements: All_
