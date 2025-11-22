# Lab 4: Database & SQL

## Overview
This lab introduces database concepts, SQL fundamentals, and JPA/Hibernate ORM. Building on Labs 1-3, you'll learn how to persist data using Spring Data JPA with an H2 in-memory database.

## Learning Objectives
- ✅ Relational database concepts
- ✅ SQL queries (SELECT, INSERT, UPDATE, DELETE)
- ✅ JPA entity mapping with annotations
- ✅ Spring Data JPA repositories
- ✅ Custom query methods with @Query
- ✅ Database schema design
- ✅ H2 in-memory database setup
- ✅ Entity lifecycle callbacks
- ✅ Transaction management
- ✅ Repository testing with @DataJpaTest

## Project Structure

```
src/main/java/com/bootcamp/onlineschool/
├── entity/
│   ├── StudentEntity.java        # NEW: JPA entity
│   └── CourseEntity.java         # NEW: JPA entity
└── repository/
    ├── StudentRepository.java    # NEW: Spring Data JPA
    └── CourseRepository.java     # NEW: Spring Data JPA

src/main/resources/
├── schema.sql                    # NEW: Database schema
├── data.sql                      # NEW: Sample data
└── application.properties        # NEW: Database config

src/test/java/com/bootcamp/onlineschool/
└── repository/
    ├── StudentRepositoryTest.java  # NEW: Repository tests
    └── CourseRepositoryTest.java   # NEW: Repository tests
```

## Key Concepts

### 1. JPA Entity Mapping

```java
@Entity
@Table(name = "students")
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "student_id", unique = true, nullable = false)
    private String studentId;
}
```

### 2. Spring Data JPA Repository

```java
@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> findByStudentId(String studentId);
    
    @Query("SELECT s FROM StudentEntity s WHERE s.gpa >= :gpaThreshold")
    List<StudentEntity> findHighAchievers(@Param("gpaThreshold") Double gpaThreshold);
}
```

### 3. Database Schema

```sql
CREATE TABLE students (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    gpa DOUBLE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

### 4. Entity Lifecycle Callbacks

```java
@PrePersist
protected void onCreate() {
    createdAt = LocalDateTime.now();
}

@PreUpdate
protected void onUpdate() {
    updatedAt = LocalDateTime.now();
}
```

## New Components in Lab 4

### StudentEntity.java
JPA entity for student persistence:
- Primary key with auto-generation
- Column constraints and validation
- Lifecycle callbacks
- Business methods

### CourseEntity.java
JPA entity for course persistence:
- Entity relationships setup
- Enrollment management
- Temporal data handling
- Query support

### StudentRepository.java
Spring Data JPA repository:
- CRUD operations
- Custom query methods
- JPQL queries
- Named parameters

### CourseRepository.java
Spring Data JPA repository:
- Custom finder methods
- Complex queries
- Aggregation functions
- Filtering and sorting

### schema.sql
Database schema definition:
- Table creation
- Constraints and indexes
- Relationships

### data.sql
Sample data initialization:
- Test data for students
- Test data for courses

## Running the Application

### Compile the project
```bash
mvn clean compile
```

### Run all tests
```bash
mvn test
```

### Run specific test class
```bash
mvn test -Dtest=StudentRepositoryTest
mvn test -Dtest=CourseRepositoryTest
```

### Run the application
```bash
mvn spring-boot:run
```

### Access H2 Console
```
http://localhost:8080/h2-console
```

## Test Results

**Expected Output:**
```
[INFO] Running com.bootcamp.onlineschool.repository.StudentRepositoryTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0

[INFO] Running com.bootcamp.onlineschool.repository.CourseRepositoryTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0

[INFO] Results:
[INFO] Tests run: 16, Failures: 0, Errors: 0, Skipped: 0
```

**Total: 16 new tests passing ✅**
**Combined with Labs 1-3: 103 tests passing ✅**

## SQL Queries

### Basic SELECT
```sql
SELECT * FROM students WHERE gpa >= 3.8;
SELECT name, email FROM students ORDER BY name;
```

### Aggregation
```sql
SELECT AVG(gpa) FROM students;
SELECT COUNT(*) FROM courses WHERE enrolled_students < max_students;
```

### Joins (for future use)
```sql
SELECT s.name, c.course_name 
FROM students s
JOIN registrations r ON s.id = r.student_id
JOIN courses c ON r.course_id = c.id;
```

## Best Practices

1. **Entity Design**
   - Use meaningful column names
   - Add appropriate constraints
   - Include audit fields (created_at, updated_at)

2. **Repository Methods**
   - Use derived query methods when possible
   - Use @Query for complex queries
   - Keep queries focused and efficient

3. **Testing**
   - Use @DataJpaTest for repository tests
   - Test both happy path and edge cases
   - Verify database constraints

4. **Performance**
   - Add indexes on frequently queried columns
   - Use pagination for large result sets
   - Avoid N+1 query problems

## Exercises

1. **Add Teacher Entity** - Create a TeacherEntity with JPA mapping
2. **Create TeacherRepository** - Implement custom query methods
3. **Add Relationships** - Create @OneToMany and @ManyToMany relationships
4. **Write Complex Queries** - Implement advanced JPQL queries
5. **Add Validation** - Implement entity validation with @Valid

## Next Steps

After completing Lab 4, proceed to **Lab 5: ORM & JPA** to learn about entity relationships and advanced ORM concepts.

## Summary

Lab 4 introduces database persistence using Spring Data JPA. Key takeaways:

- JPA entities map Java objects to database tables
- Spring Data JPA eliminates boilerplate repository code
- Custom queries use JPQL for type-safe database access
- Entity lifecycle callbacks handle audit fields
- @DataJpaTest enables focused repository testing

**Total Tests in Lab 4: 16 ✅**
**Combined Total: 103 ✅**
