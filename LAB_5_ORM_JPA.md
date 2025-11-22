# Lab 5: ORM & JPA - Object-Relational Mapping with Hibernate

## Overview

Lab 5 introduces Object-Relational Mapping (ORM) using JPA (Java Persistence API) and Hibernate. This lab teaches how to map Java objects to database tables, manage entity relationships, implement the repository pattern, and perform CRUD operations using Spring Data JPA. Students will learn enterprise-level data persistence patterns and transaction management.

## Learning Objectives

By completing this lab, you will understand:

- **JPA Entity Configuration:** How to map Java classes to database tables using annotations
- **Entity Relationships:** How to model One-to-Many, Many-to-One, Many-to-Many, and One-to-One relationships
- **Spring Data JPA:** How to use repositories for data access without boilerplate code
- **Query Methods:** How to create custom queries using method naming conventions and JPQL
- **Entity Lifecycle:** How entities transition through different states (transient, managed, removed)
- **Transaction Management:** How to ensure data consistency with @Transactional
- **Cascade Operations:** How to manage related entity operations automatically
- **Validation:** How to apply constraints to entities and prevent invalid data persistence
- **Testing:** How to test repositories and services with @DataJpaTest and @SpringBootTest
- **Database Schema:** How to manage database schema generation and initialization

## Technology Stack

- **Java:** 21
- **Spring Boot:** 3.2.0
  - spring-boot-starter-data-jpa: 3.2.0 (JPA and Hibernate)
  - spring-boot-starter-web: 3.2.0 (REST APIs)
  - spring-boot-starter-validation: 3.2.0 (Bean validation)
  - spring-boot-starter-test: 3.2.0 (Testing)
- **Hibernate:** 6.2.13 (included with Spring Boot)
- **H2 Database:** 2.2.224 (in-memory database)
- **Jakarta Persistence API:** 3.1.0 (JPA specification)
- **Maven:** 3.6 or higher

## Project Structure

```
src/
├── main/
│   ├── java/com/bootcamp/onlineschool/
│   │   ├── OnlineSchoolApplication.java
│   │   ├── entity/
│   │   │   ├── Student.java
│   │   │   └── Course.java
│   │   ├── repository/
│   │   │   ├── StudentRepository.java
│   │   │   └── CourseRepository.java
│   │   ├── service/
│   │   │   ├── StudentService.java
│   │   │   └── CourseService.java
│   │   ├── dto/
│   │   │   ├── StudentDTO.java
│   │   │   └── CourseDTO.java
│   │   └── config/
│   │       └── AppConfig.java
│   └── resources/
│       ├── application.properties
│       ├── application-dev.properties
│       └── data.sql
└── test/
    ├── java/com/bootcamp/onlineschool/
    │   ├── entity/
    │   │   ├── StudentEntityTest.java
    │   │   └── CourseEntityTest.java
    │   ├── repository/
    │   │   ├── StudentRepositoryTest.java
    │   │   └── CourseRepositoryTest.java
    │   ├── service/
    │   │   ├── StudentServiceTest.java
    │   │   └── CourseServiceTest.java
    │   └── integration/
    │       └── StudentCourseIntegrationTest.java
    └── resources/
        └── application-test.properties
```

## Key Concepts

### 1. JPA Entities

Entities are Java classes that represent tables in the database:

```java
@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    @NotNull
    private String name;
    
    @Column(nullable = false, unique = true)
    @Email
    private String email;
    
    @Column
    @Min(0.0)
    @Max(4.0)
    private Double gpa;
    
    @ManyToMany
    @JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();
}
```

### 2. Entity Relationships

**Many-to-Many Relationship:**
- A student can enroll in multiple courses
- A course can have multiple students
- Uses a join table to manage the relationship

```java
// Student side
@ManyToMany
@JoinTable(name = "student_course", ...)
private Set<Course> courses;

// Course side
@ManyToMany(mappedBy = "courses")
private Set<Student> students;
```

### 3. Spring Data JPA Repositories

Repositories provide CRUD operations and custom query methods:

```java
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    List<Student> findByNameContainingIgnoreCase(String name);
    List<Student> findByCourses_Id(Long courseId);
    
    @Query("SELECT s FROM Student s WHERE s.gpa >= :minGpa")
    List<Student> findStudentsWithMinGpa(@Param("minGpa") Double minGpa);
}
```

### 4. Service Layer

Services encapsulate business logic and manage transactions:

```java
@Service
@Transactional
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    
    public Student registerStudent(StudentDTO dto) {
        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setGpa(dto.getGpa());
        return studentRepository.save(student);
    }
    
    @Transactional(readOnly = true)
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }
}
```

### 5. Entity Lifecycle

Entities go through different states:

- **Transient:** Created but not persisted
- **Managed:** Persisted and tracked by persistence context
- **Detached:** Was managed but no longer tracked
- **Removed:** Marked for deletion

### 6. Transaction Management

`@Transactional` ensures atomic operations:

```java
@Transactional
public void enrollStudentInCourse(Long studentId, Long courseId) {
    Student student = studentRepository.findById(studentId).orElseThrow();
    Course course = courseRepository.findById(courseId).orElseThrow();
    student.getCourses().add(course);
    // Changes are automatically persisted on transaction commit
}
```

### 7. Validation

Constraints prevent invalid data from being persisted:

```java
@Entity
public class Student {
    @NotNull
    @Size(min = 1, max = 100)
    private String name;
    
    @Email
    private String email;
    
    @Min(0.0)
    @Max(4.0)
    private Double gpa;
}
```

### 8. Query Methods

Spring Data JPA generates queries from method names:

```java
// Method naming conventions
findByEmail(String email)                    // WHERE email = ?
findByNameContainingIgnoreCase(String name) // WHERE LOWER(name) LIKE ?
findByCourses_Id(Long courseId)             // WHERE courses.id = ?
findByGpaGreaterThanEqual(Double gpa)       // WHERE gpa >= ?
```

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

### Accessing H2 Console

Access the H2 database console at: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave blank)

### Running Tests

```bash
# Run all tests
mvn clean test

# Run specific test class
mvn test -Dtest=StudentRepositoryTest

# Run with verbose output
mvn test -X
```

## Test Coverage

### Entity Tests (15+ tests)
- Entity creation and field mapping
- Validation constraints
- Relationship configuration

### Repository Tests (20+ tests)
- CRUD operations (Create, Read, Update, Delete)
- Custom query methods
- Relationship queries
- Empty result handling

### Service Tests (18+ tests)
- Business logic operations
- Transaction management
- Error handling
- Service integration

### Integration Tests (12+ tests)
- End-to-end workflows
- Complex queries
- Relationship operations
- Cascade behavior

### Property-Based Tests (12 properties)
- Entity persistence round trip
- Repository save with ID generation
- Query method accuracy
- Relationship consistency
- Validation enforcement
- Transaction rollback

**Total:** 75+ tests with comprehensive coverage

## Common Tasks

### Creating an Entity

```java
@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
}
```

### Creating a Repository

```java
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    List<Student> findByNameContainingIgnoreCase(String name);
}
```

### Creating a Service

```java
@Service
@Transactional
public class StudentService {
    @Autowired
    private StudentRepository repository;
    
    public Student save(Student student) {
        return repository.save(student);
    }
    
    @Transactional(readOnly = true)
    public Optional<Student> findById(Long id) {
        return repository.findById(id);
    }
}
```

### Testing a Repository

```java
@DataJpaTest
public class StudentRepositoryTest {
    @Autowired
    private StudentRepository repository;
    
    @Test
    public void testSaveAndRetrieve() {
        Student student = new Student();
        student.setName("John Doe");
        student.setEmail("john@example.com");
        
        Student saved = repository.save(student);
        Optional<Student> found = repository.findById(saved.getId());
        
        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getName());
    }
}
```

## Troubleshooting

### Issue: "No qualifying bean of type 'StudentRepository' found"
**Solution:** Ensure the repository interface extends `JpaRepository` and is in a package scanned by Spring.

### Issue: "Validation failed for query for method public abstract java.util.Optional"
**Solution:** Check that your custom @Query method has correct JPQL syntax and parameter names match.

### Issue: "Could not write JSON: failed to lazily initialize a collection"
**Solution:** Use `@Transactional(readOnly = true)` on service methods or configure lazy loading strategy.

### Issue: "Duplicate entry for key 'email'"
**Solution:** The email already exists in the database. Check for unique constraints and handle duplicates appropriately.

### Issue: "Transaction rolled back because it has been marked as rollback-only"
**Solution:** A constraint violation or exception occurred. Check the logs for the root cause and fix the data.

### Issue: "No property found for type Student"
**Solution:** Ensure entity class is properly annotated with @Entity and has proper getters/setters.

## Best Practices

1. **Use DTOs for API responses** - Don't expose entities directly
2. **Apply @Transactional at service layer** - Not on repositories
3. **Use @DataJpaTest for repository tests** - Faster than full context
4. **Validate entities before persistence** - Use validation annotations
5. **Use custom queries for complex logic** - Don't load all data and filter in Java
6. **Configure cascade carefully** - Avoid unintended deletions
7. **Use lazy loading for large collections** - Improves performance
8. **Test relationships thoroughly** - Bidirectional consistency is important
9. **Handle empty Optional properly** - Use orElseThrow() or orElse()
10. **Monitor SQL queries** - Enable logging to see generated SQL

## Configuration

### Application Properties

```properties
# JPA Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# H2 Console
spring.h2.console.enabled=true

# Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Logging
logging.level.com.bootcamp.onlineschool=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

## Lab Progression

This is **Lab 5** of the bootcamp curriculum:

- **Lab 1:** Java Fundamentals - Core Java and OOP ✅
- **Lab 2:** JUnit Testing - Advanced testing patterns ✅
- **Lab 3:** Spring Boot Basics - REST APIs and services ✅
- **Lab 4:** Database & SQL - SQL fundamentals (coming soon)
- **Lab 5:** ORM & JPA - Object-Relational Mapping (current)
- **Lab 6:** Backend API - RESTful API development (coming soon)
- **Lab 7:** Maven & Build Tools - Build automation (coming soon)
- **Lab 8:** Frontend HTML & CSS - Web basics (coming soon)
- **Lab 9:** ReactJS - Modern frontend (coming soon)
- **Lab 10:** Full Stack Integration - Complete application (coming soon)

## Switching Between Labs

```bash
# View all available branches
git branch -a

# Switch to Lab 5
git checkout lab/orm-5-jpa

# Switch to other labs
git checkout lab/java-1-fundamentals
git checkout lab/junit-2-testing
git checkout lab/springboot-3-basics
git checkout lab/api-6-backend
git checkout lab/maven-7-build
```

## Resources

- [JPA Documentation](https://jakarta.ee/specifications/persistence/)
- [Hibernate Documentation](https://hibernate.org/orm/documentation/)
- [Spring Data JPA Reference](https://spring.io/projects/spring-data-jpa)
- [Spring Boot Data JPA Guide](https://spring.io/guides/gs/accessing-data-jpa/)
- [H2 Database Documentation](https://www.h2database.com/)
- [Jakarta Persistence API](https://jakarta.ee/specifications/persistence/3.1/)

## Next Steps

1. Review the requirements in `.kiro/specs/lab-5-orm-jpa/requirements.md`
2. Study the design in `.kiro/specs/lab-5-orm-jpa/design.md`
3. Execute tasks from `.kiro/specs/lab-5-orm-jpa/tasks.md`
4. Run tests frequently: `mvn clean test`
5. Verify the application runs: `mvn spring-boot:run`
6. Access H2 console: `http://localhost:8080/h2-console`

---

**Last Updated:** November 22, 2025  
**Lab:** 5 - ORM & JPA  
**Status:** Specification Complete, Ready for Implementation
