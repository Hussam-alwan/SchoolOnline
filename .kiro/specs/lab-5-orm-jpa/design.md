# Lab 5: ORM & JPA - Design Document

## Overview

Lab 5 implements Object-Relational Mapping (ORM) using JPA and Hibernate with Spring Data JPA. The design focuses on mapping Java objects to database tables, managing entity relationships, implementing the repository pattern, and performing CRUD operations. The system uses H2 in-memory database for development and testing, with automatic schema generation and initialization.

## Architecture

### Layered Architecture

```
┌─────────────────────────────────────────┐
│         Test Layer                      │
│  (Repository Tests, Service Tests)      │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│         Service Layer                   │
│  (StudentService, CourseService)        │
│  - Business logic                       │
│  - Transaction management               │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Repository Layer                   │
│  (StudentRepository, CourseRepository)  │
│  - Data access abstraction              │
│  - Query methods                        │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Persistence Layer                  │
│  (JPA/Hibernate)                        │
│  - Entity mapping                       │
│  - Transaction management               │
│  - Database operations                  │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Database Layer                     │
│  (H2 In-Memory Database)                │
│  - Table storage                        │
│  - Data persistence                     │
└─────────────────────────────────────────┘
```

### Key Design Patterns

1. **Repository Pattern:** Abstracts data access logic behind a clean interface
2. **Entity Pattern:** Maps database tables to Java objects
3. **Service Pattern:** Encapsulates business logic and transaction management
4. **DTO Pattern:** Transfers data between layers without exposing entities
5. **Cascade Pattern:** Manages related entity operations automatically

## Components and Interfaces

### Entity Components

#### Student Entity
- **Responsibility:** Represents a student in the system
- **Relationships:** Many-to-Many with Course
- **Annotations:** @Entity, @Table, @Id, @GeneratedValue, @Column, @ManyToMany
- **Fields:** id, name, email, gpa, courses
- **Validation:** @NotNull, @Email, @Min, @Max

#### Course Entity
- **Responsibility:** Represents a course in the system
- **Relationships:** Many-to-Many with Student
- **Annotations:** @Entity, @Table, @Id, @GeneratedValue, @Column, @ManyToMany
- **Fields:** id, code, title, description, credits, students
- **Validation:** @NotNull, @NotBlank, @Min, @Max

### Repository Components

#### StudentRepository Interface
```java
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    List<Student> findByNameContainingIgnoreCase(String name);
    List<Student> findByCourses_Id(Long courseId);
    @Query("SELECT s FROM Student s WHERE s.gpa >= :minGpa")
    List<Student> findStudentsWithMinGpa(@Param("minGpa") Double minGpa);
}
```

#### CourseRepository Interface
```java
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCode(String code);
    List<Course> findByTitleContainingIgnoreCase(String title);
    @Query("SELECT c FROM Course c WHERE c.credits >= :minCredits")
    List<Course> findCoursesByMinCredits(@Param("minCredits") Integer minCredits);
}
```

### Service Components

#### StudentService
- **Responsibility:** Business logic for student operations
- **Methods:** 
  - registerStudent(StudentDTO): Student
  - getStudentById(Long): Optional<Student>
  - getAllStudents(): List<Student>
  - updateStudent(Long, StudentDTO): Student
  - deleteStudent(Long): void
  - enrollInCourse(Long studentId, Long courseId): void
  - searchByName(String): List<Student>
  - getStudentsByGpa(Double): List<Student>

#### CourseService
- **Responsibility:** Business logic for course operations
- **Methods:**
  - createCourse(CourseDTO): Course
  - getCourseById(Long): Optional<Course>
  - getAllCourses(): List<Course>
  - updateCourse(Long, CourseDTO): Course
  - deleteCourse(Long): void
  - getEnrolledStudents(Long courseId): List<Student>
  - searchByTitle(String): List<Course>

## Data Models

### Student Entity Structure
```
Student
├── id: Long (Primary Key, Auto-generated)
├── name: String (Not Null, Max 100)
├── email: String (Not Null, Unique, Valid Email)
├── gpa: Double (Min 0.0, Max 4.0)
├── createdAt: LocalDateTime (Auto-set)
├── updatedAt: LocalDateTime (Auto-updated)
└── courses: Set<Course> (Many-to-Many relationship)
```

### Course Entity Structure
```
Course
├── id: Long (Primary Key, Auto-generated)
├── code: String (Not Null, Unique, Max 10)
├── title: String (Not Null, Max 100)
├── description: String (Max 500)
├── credits: Integer (Min 1, Max 4)
├── createdAt: LocalDateTime (Auto-set)
├── updatedAt: LocalDateTime (Auto-updated)
└── students: Set<Student> (Many-to-Many relationship)
```

### Many-to-Many Relationship
```
Student (1) ──────────────── (M) StudentCourse (Join Table) ──────────────── (M) Course (1)
  - id                          - student_id (FK)                              - id
  - name                        - course_id (FK)                               - code
  - email                       - enrolled_date                                - title
  - gpa                                                                        - credits
```

### Database Schema
```sql
CREATE TABLE student (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    gpa DOUBLE DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE course (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(10) NOT NULL UNIQUE,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    credits INTEGER DEFAULT 3,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE student_course (
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    enrolled_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
);
```

## Correctness Properties

A property is a characteristic or behavior that should hold true across all valid executions of a system—essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.

### Property 1: Entity Persistence Round Trip
*For any* valid Student entity, when persisted to the database and then retrieved by ID, the retrieved entity should have identical values for all fields (name, email, gpa).

**Validates: Requirements 1.5, 5.2**

### Property 2: Repository Save Returns Generated ID
*For any* new Student entity without an ID, when saved through the repository, the returned entity should have a non-null, positive ID assigned.

**Validates: Requirements 1.2, 3.5**

### Property 3: FindByEmail Query Correctness
*For any* Student entity persisted with a specific email, calling findByEmail with that email should return an Optional containing that exact student.

**Validates: Requirements 4.1, 4.5**

### Property 4: FindAll Returns All Persisted Students
*For any* set of Student entities persisted to the database, calling findAll should return a list containing all persisted students with no duplicates.

**Validates: Requirements 3.3, 4.5**

### Property 5: Delete Removes Entity
*For any* persisted Student entity, after calling delete with its ID, calling findById should return an empty Optional.

**Validates: Requirements 3.5, 5.4**

### Property 6: Many-to-Many Relationship Persistence
*For any* Student and Course entities, when a student is enrolled in a course and both are persisted, retrieving the student should include the course in its courses collection.

**Validates: Requirements 2.1, 2.5**

### Property 7: Bidirectional Relationship Consistency
*For any* Student-Course relationship, if a student is in a course's students collection, then that course must be in the student's courses collection.

**Validates: Requirements 2.4, 6.4**

### Property 8: Custom Query Method Accuracy
*For any* set of students with varying GPAs, calling findStudentsWithMinGpa with a threshold should return only students whose GPA is greater than or equal to the threshold.

**Validates: Requirements 4.2, 4.3**

### Property 9: Email Uniqueness Constraint
*For any* two Student entities with the same email, attempting to persist both should result in a constraint violation exception on the second save.

**Validates: Requirements 9.2, 7.5**

### Property 10: Validation Prevents Invalid Persistence
*For any* Student entity with invalid data (null name, invalid email format, GPA outside 0-4 range), attempting to persist should raise a validation exception.

**Validates: Requirements 9.1, 9.3**

### Property 11: Transaction Rollback on Error
*For any* transaction that attempts to persist invalid data, if a constraint violation occurs, all changes in that transaction should be rolled back and the database should remain unchanged.

**Validates: Requirements 7.3, 7.5**

### Property 12: Cascade Delete Relationship Cleanup
*For any* Course entity deleted from the database, the student_course join table entries should be removed, but Student entities should remain unaffected.

**Validates: Requirements 6.1, 6.3**

## Error Handling

### Entity Validation Errors
- **Scenario:** Invalid entity data (null fields, constraint violations)
- **Handling:** Throw `ConstraintViolationException` with detailed error messages
- **Recovery:** Validation should occur before persistence attempt

### Database Constraint Violations
- **Scenario:** Duplicate email, unique constraint violations
- **Handling:** Catch `DataIntegrityViolationException` and translate to domain exception
- **Recovery:** Provide user-friendly error message indicating the constraint violation

### Entity Not Found
- **Scenario:** Attempting to retrieve non-existent entity
- **Handling:** Return empty `Optional` from repository methods
- **Recovery:** Service layer should handle empty Optional and throw appropriate exception

### Transaction Failures
- **Scenario:** Database connection loss, deadlocks
- **Handling:** Automatic rollback via Spring's transaction management
- **Recovery:** Retry logic or user notification depending on operation

### Relationship Integrity Errors
- **Scenario:** Orphaned entities, cascade operation failures
- **Handling:** Enforce cascade rules and foreign key constraints
- **Recovery:** Validate relationships before operations

## Testing Strategy

### Unit Testing Approach
- Test entity validation and constraints
- Test service business logic in isolation using mocks
- Test repository query methods with test data
- Test error handling and exception scenarios
- Use `@MockBean` to mock repository dependencies in service tests

### Property-Based Testing Approach
- Use **Hypothesis** (Python) or **QuickCheck** (Haskell) equivalent for Java: **jqwik**
- Configure each property-based test to run minimum 100 iterations
- Generate random valid Student and Course entities
- Test properties across various input combinations
- Verify invariants hold for all generated test cases

### Integration Testing Approach
- Use `@DataJpaTest` for repository testing with real database
- Use `@SpringBootTest` for full application context testing
- Test entity relationships and cascade operations
- Test transaction management and rollback scenarios
- Use H2 in-memory database for fast test execution

### Test Organization
```
src/test/java/com/bootcamp/onlineschool/
├── entity/
│   ├── StudentEntityTest.java
│   └── CourseEntityTest.java
├── repository/
│   ├── StudentRepositoryTest.java
│   └── CourseRepositoryTest.java
├── service/
│   ├── StudentServiceTest.java
│   └── CourseServiceTest.java
└── integration/
    └── StudentCourseIntegrationTest.java
```

### Test Coverage Goals
- **Entity Tests:** 15+ tests covering validation and constraints
- **Repository Tests:** 20+ tests covering CRUD and query methods
- **Service Tests:** 18+ tests covering business logic
- **Integration Tests:** 12+ tests covering end-to-end scenarios
- **Property-Based Tests:** 12 properties with 100+ iterations each
- **Total:** 75+ tests with comprehensive coverage

### Testing Best Practices
- Each test should be independent and isolated
- Use `@Transactional` on repository tests for automatic rollback
- Create test fixtures for common entity setup
- Use descriptive test names indicating what is being tested
- Mock external dependencies in service tests
- Test both happy path and error scenarios
- Verify database state after operations
