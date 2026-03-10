# Lab 4: Database & SQL - Homework Assignment

## Overview

This homework assignment reinforces the JPA, Hibernate, and Spring Data concepts you learned in Lab 4. You'll practice entity mapping, repository development, custom queries, and database testing.

## Learning Objectives

By completing this homework, you will:
- Create JPA entities with proper annotations and constraints
- Implement Spring Data JPA repositories with custom queries
- Write JPQL queries for complex data retrieval
- Use @DataJpaTest for repository testing
- Handle entity relationships and lifecycle callbacks
- Apply database best practices and naming conventions

## Prerequisites

- Completed Lab 4: Database & SQL
- Understanding of JPA/Hibernate annotations
- Familiarity with Spring Data JPA repositories
- Knowledge of JPQL (Java Persistence Query Language)
- Understanding of relational database concepts

---

## Assignment 1: Teacher Entity and Repository

### Task
Create a complete `Teacher` entity with JPA mapping and a repository with custom queries.

### Requirements

1. **Create Teacher entity** in `src/main/java/com/bootcamp/onlineschool/entity/Teacher.java`:
   - Annotate with `@Entity` and `@Table(name = "teachers")`
   - Fields:
     - `id` (Long) - Primary key with auto-generation
     - `teacherId` (String) - Unique identifier, max 20 chars
     - `name` (String) - Not null, max 100 chars
     - `email` (String) - Not null, unique, max 100 chars
     - `department` (String) - Not null, max 50 chars
     - `yearsOfExperience` (Integer) - Not null
     - `salary` (Double) - Not null
     - `hireDate` (LocalDate) - Not null
     - `createdAt` (LocalDateTime) - Not null, not updatable
     - `updatedAt` (LocalDateTime)
   - Use `@Column` annotations with proper constraints
   - Implement lifecycle callbacks: `@PrePersist` and `@PreUpdate`
   - Override `toString()`, `equals()`, and `hashCode()`
   - Add validation in setter methods (e.g., yearsOfExperience >= 0)

2. **Create TeacherRepository** in `src/main/java/com/bootcamp/onlineschool/repository/TeacherRepository.java`:
   - Extend `JpaRepository<Teacher, Long>`
   - Annotate with `@Repository`
   - Implement custom query methods:
     - `findByTeacherId(String)` - Find by teacher ID
     - `findByEmail(String)` - Find by email
     - `findByDepartment(String)` - Find all teachers in department
     - `findByYearsOfExperienceGreaterThanEqual(Integer)` - Find experienced teachers
     - `findBySalaryBetween(Double, Double)` - Find teachers in salary range
   - Implement JPQL queries:
     - `@Query` to find all teachers sorted by name
     - `@Query` to find teachers hired after a specific date
     - `@Query` to count teachers by department
     - `@Query` to calculate average salary by department
     - `@Query` to find top N highest paid teachers

3. **Create test class** `TeacherRepositoryTest.java`:
   - Use `@DataJpaTest` annotation
   - Inject TeacherRepository with `@Autowired`
   - Use `@BeforeEach` to clear repository
   - Write minimum 12 tests covering:
     - Save and retrieve operations
     - Finding by various fields
     - Custom query methods
     - JPQL queries
     - Update operations
     - Delete operations
     - Edge cases (empty results, null handling)

### Expected Deliverables
- Teacher.java (JPA entity with all annotations)
- TeacherRepository.java (repository with custom queries)
- TeacherRepositoryTest.java (minimum 12 tests)

---

## Assignment 2: Department Entity with Relationships

### Task
Create a `Department` entity with proper JPA mapping and implement a repository with advanced queries.

### Requirements

1. **Create Department entity** in `src/main/java/com/bootcamp/onlineschool/entity/Department.java`:
   - Annotate with `@Entity` and `@Table(name = "departments")`
   - Fields:
     - `id` (Long) - Primary key with auto-generation
     - `departmentId` (String) - Unique identifier, max 20 chars
     - `name` (String) - Not null, unique, max 100 chars
     - `head` (String) - Department head name, max 100 chars
     - `budget` (Double) - Not null
     - `location` (String) - Building/room, max 100 chars
     - `establishedDate` (LocalDate)
     - `createdAt` (LocalDateTime) - Not null, not updatable
     - `updatedAt` (LocalDateTime)
   - Use proper column constraints
   - Implement lifecycle callbacks
   - Override `toString()`, `equals()`, and `hashCode()`
   - Add business methods:
     - `isWithinBudget(Double amount)` - Check if amount is within budget
     - `increaseBudget(Double amount)` - Increase budget
     - `decreaseBudget(Double amount)` - Decrease budget (with validation)

2. **Create DepartmentRepository** in `src/main/java/com/bootcamp/onlineschool/repository/DepartmentRepository.java`:
   - Extend `JpaRepository<Department, Long>`
   - Annotate with `@Repository`
   - Implement query methods:
     - `findByDepartmentId(String)` - Find by department ID
     - `findByName(String)` - Find by name
     - `findByBudgetGreaterThan(Double)` - Find departments with budget above threshold
     - `findByBudgetBetween(Double, Double)` - Find departments in budget range
     - `findByEstablishedDateAfter(LocalDate)` - Find recently established departments
   - Implement JPQL queries:
     - `@Query` to find all departments sorted by budget descending
     - `@Query` to calculate total budget across all departments
     - `@Query` to find departments with budget above average
     - `@Query` to count departments by location
     - `@Query` to find departments established in a specific year

3. **Create test class** `DepartmentRepositoryTest.java`:
   - Use `@DataJpaTest` annotation
   - Write minimum 12 tests covering:
     - CRUD operations
     - Query methods
     - JPQL queries
     - Business method validation
     - Budget calculations
     - Date-based queries
     - Edge cases

### Expected Deliverables
- Department.java (JPA entity)
- DepartmentRepository.java (repository with queries)
- DepartmentRepositoryTest.java (minimum 12 tests)

---

## Assignment 3: Advanced Queries and Aggregations

### Task
Extend existing repositories with complex queries and create integration tests.

### Requirements

1. **Extend StudentRepository** with additional queries:
   - `@Query` to find students enrolled in a specific year
   - `@Query` to find students with GPA in a specific range
   - `@Query` to get student count by enrollment year
   - `@Query` to find students with email domain (e.g., @school.edu)
   - `@Query` with pagination to get top N students by GPA

2. **Extend CourseRepository** with additional queries:
   - `@Query` to find courses with available seats
   - `@Query` to find courses by instructor name
   - `@Query` to calculate total enrolled students across all courses
   - `@Query` to find courses with enrollment rate above threshold
   - `@Query` to find most popular courses (highest enrollment)

3. **Create RepositoryIntegrationTest.java**:
   - Use `@DataJpaTest` annotation
   - Inject multiple repositories
   - Write minimum 10 tests covering:
     - Cross-repository queries
     - Data consistency across entities
     - Complex filtering and sorting
     - Aggregation queries
     - Transaction behavior
     - Cascade operations

### Expected Deliverables
- Updated StudentRepository.java with new queries
- Updated CourseRepository.java with new queries
- RepositoryIntegrationTest.java (minimum 10 tests)

---

## Bonus Challenges (Optional)

### Challenge 1: Entity Relationships
Implement a one-to-many relationship:
- Add `@OneToMany` relationship from Department to Teacher
- Add `@ManyToOne` relationship from Teacher to Department
- Update repositories to support relationship queries
- Write tests for relationship operations
- Test cascade operations (persist, merge, remove)

### Challenge 2: Native SQL Queries
Create native SQL queries in repositories:
- Use `@Query(nativeQuery = true)` for complex queries
- Implement queries that can't be expressed in JPQL
- Compare performance with JPQL equivalents
- Write tests to verify native query results

### Challenge 3: Custom Repository Implementation
Create a custom repository implementation:
- Define a custom interface with advanced query methods
- Implement the interface with EntityManager
- Use Criteria API for dynamic queries
- Integrate custom repository with Spring Data JPA
- Write tests for custom repository methods

### Challenge 4: Database Initialization
Create database initialization scripts:
- Create `schema.sql` with DDL statements
- Create `data.sql` with sample data
- Configure application to use scripts on startup
- Write tests to verify data is loaded correctly

---

## Running Your Tests

### Run all tests:
```bash
mvn clean test
```

### Run specific test class:
```bash
mvn test -Dtest=TeacherRepositoryTest
mvn test -Dtest=DepartmentRepositoryTest
mvn test -Dtest=RepositoryIntegrationTest
```

### Run tests with H2 console enabled:
```bash
mvn spring-boot:run
```
Then access: `http://localhost:8080/h2-console`

### Run tests with verbose output:
```bash
mvn test -X
```

---

## Submission Checklist

Before submitting, ensure you have:

- [ ] All entity classes with proper JPA annotations
- [ ] All repository interfaces with custom queries
- [ ] All test classes with @DataJpaTest
- [ ] All tests passing (`mvn clean test` shows 0 failures)
- [ ] Minimum test counts met (12 + 12 + 10 = 34 tests)
- [ ] Proper use of JPA annotations (@Entity, @Table, @Column, @Id)
- [ ] JPQL queries with @Query annotation
- [ ] Lifecycle callbacks (@PrePersist, @PreUpdate)
- [ ] JavaDoc comments for all public methods
- [ ] Code follows Java naming conventions
- [ ] Database constraints properly defined

### Expected Test Results
Your test execution should show:
```
[INFO] Tests run: 34+, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## Code Quality Guidelines

### JPA Entity Best Practices

1. **Entity Mapping**
   ```java
   @Entity
   @Table(name = "teachers")
   public class Teacher {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;
       
       @Column(name = "teacher_id", unique = true, nullable = false, length = 20)
       private String teacherId;
       
       @Column(name = "name", nullable = false, length = 100)
       private String name;
       
       @Column(name = "email", nullable = false, unique = true, length = 100)
       private String email;
       
       @Column(name = "created_at", nullable = false, updatable = false)
       private LocalDateTime createdAt;
       
       @PrePersist
       protected void onCreate() {
           createdAt = LocalDateTime.now();
       }
   }
   ```

2. **Repository with Custom Queries**
   ```java
   @Repository
   public interface TeacherRepository extends JpaRepository<Teacher, Long> {
       
       // Query method
       Optional<Teacher> findByTeacherId(String teacherId);
       
       // JPQL query
       @Query("SELECT t FROM Teacher t WHERE t.yearsOfExperience >= :years ORDER BY t.name")
       List<Teacher> findExperiencedTeachers(@Param("years") Integer years);
       
       // Aggregation query
       @Query("SELECT AVG(t.salary) FROM Teacher t WHERE t.department = :dept")
       Double getAverageSalaryByDepartment(@Param("dept") String department);
   }
   ```

3. **Repository Testing**
   ```java
   @DataJpaTest
   @DisplayName("TeacherRepository Tests")
   public class TeacherRepositoryTest {
       
       @Autowired
       private TeacherRepository teacherRepository;
       
       @BeforeEach
       public void setUp() {
           teacherRepository.deleteAll();
       }
       
       @Test
       @DisplayName("Should find teacher by teacher ID")
       public void testFindByTeacherId() {
           // Arrange
           Teacher teacher = new Teacher("T001", "John Doe", "john@school.edu");
           teacherRepository.save(teacher);
           
           // Act
           Optional<Teacher> found = teacherRepository.findByTeacherId("T001");
           
           // Assert
           assertTrue(found.isPresent());
           assertEquals("John Doe", found.get().getName());
       }
   }
   ```

### Naming Conventions
- Entity classes: `Teacher.java`, `Department.java`
- Repository interfaces: `TeacherRepository.java`, `DepartmentRepository.java`
- Test classes: `TeacherRepositoryTest.java`
- Table names: lowercase with underscores (e.g., `teachers`, `departments`)
- Column names: lowercase with underscores (e.g., `teacher_id`, `created_at`)

### JPQL Query Guidelines
- Use entity names, not table names
- Use field names, not column names
- Use named parameters (`:paramName`)
- Always use `@Param` annotation for clarity
- Order results when appropriate
- Consider performance for large datasets

---

## Common Pitfalls to Avoid

- ❌ Forgetting `@Entity` annotation on entity classes
- ❌ Not specifying `@Table(name = "...")` for table mapping
- ❌ Missing `@Column` constraints (nullable, unique, length)
- ❌ Not implementing `equals()` and `hashCode()` based on business key
- ❌ Forgetting `@Repository` annotation on repository interfaces
- ❌ Using table/column names instead of entity/field names in JPQL
- ❌ Not using `@Param` annotation with named parameters
- ❌ Not clearing repository in `@BeforeEach` for test isolation
- ❌ Testing implementation details instead of behavior
- ❌ Not handling Optional properly in tests

---

## Tips for Success

1. **Start with entities** - Get the mapping right before creating repositories
2. **Test incrementally** - Test each query method as you create it
3. **Use H2 console** - Verify data structure and query results
4. **Follow examples** - Reference Student and Course entities
5. **Read error messages** - Hibernate provides detailed error information
6. **Use proper constraints** - Define nullable, unique, length constraints
7. **Implement lifecycle callbacks** - Use @PrePersist and @PreUpdate
8. **Write descriptive queries** - Use meaningful parameter names
9. **Test edge cases** - Empty results, null values, boundary conditions
10. **Keep tests isolated** - Clear data in @BeforeEach

---

## Resources

### Documentation
- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [JPA Annotations](https://docs.oracle.com/javaee/7/api/javax/persistence/package-summary.html)
- [JPQL Language Reference](https://docs.oracle.com/javaee/7/tutorial/persistence-querylanguage.htm)
- [H2 Database Documentation](https://www.h2database.com/html/main.html)
- Lab 4 README.md - Review database concepts

### Example Code
- `Student.java` - Entity with JPA annotations and lifecycle callbacks
- `Course.java` - Entity with business methods
- `StudentRepository.java` - Repository with custom queries and JPQL
- `StudentRepositoryTest.java` - Repository testing with @DataJpaTest

### Key Concepts
- JPA Entity Mapping
- Spring Data JPA Repositories
- JPQL (Java Persistence Query Language)
- Entity Lifecycle Callbacks
- Database Testing with @DataJpaTest

---

## Questions and Support

If you encounter issues:

1. **Check entity mapping** - Verify @Entity, @Table, @Column annotations
2. **Verify H2 console** - Check if tables are created correctly
3. **Review JPQL syntax** - Ensure entity/field names are correct
4. **Check test configuration** - Verify @DataJpaTest is present
5. **Read Hibernate logs** - Enable SQL logging in application.properties
6. **Test queries manually** - Use H2 console to test SQL
7. **Consult documentation** - Spring Data JPA docs are comprehensive
8. **Ask for help** - Reach out during office hours or on the discussion forum

### Enable SQL Logging
Add to `application-test.properties`:
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

---

**Remember: Understanding JPA and database concepts is crucial for building robust, data-driven applications!** 🚀
