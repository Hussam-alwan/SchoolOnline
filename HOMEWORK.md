# Lab 3: Spring Boot Basics - Homework Assignment

## Overview

This homework assignment reinforces the Spring Boot fundamentals you learned in Lab 3. You'll practice dependency injection, service layer development, configuration management, and Spring Boot testing.

## Learning Objectives

By completing this homework, you will:
- Create Spring Boot services with proper annotations
- Implement dependency injection using constructor injection
- Configure Spring beans using @Configuration and @Bean
- Write integration tests with @SpringBootTest
- Handle exceptions with custom exception classes
- Apply service layer patterns and best practices

## Prerequisites

- Completed Lab 3: Spring Boot Basics
- Understanding of Spring Boot annotations (@Service, @Configuration, @Bean)
- Familiarity with dependency injection concepts
- Knowledge of JUnit 5 testing from Lab 2

---

## Assignment 1: Teacher Service Implementation

### Task
Create a complete `TeacherService` with Spring Boot integration and comprehensive testing.

### Requirements

1. **Create Teacher entity** in `src/main/java/com/bootcamp/onlineschool/model/Teacher.java`:
   - Fields: `id`, `name`, `email`, `department`, `yearsOfExperience`
   - Constructor with validation
   - Getters and setters
   - Override `toString()` and `equals()`/`hashCode()`

2. **Create TeacherRegistry** in `src/main/java/com/bootcamp/onlineschool/TeacherRegistry.java`:
   - Use `HashMap<String, Teacher>` for storage
   - Methods:
     - `addTeacher(Teacher)` - add new teacher
     - `findTeacherById(String)` - find by ID
     - `getAllTeachers()` - return all teachers
     - `removeTeacher(String)` - remove by ID
     - `findTeachersByDepartment(String)` - filter by department
     - `getExperiencedTeachers(int)` - filter by years of experience
     - `getTeacherCount()` - return total count
     - `clear()` - remove all teachers

3. **Configure TeacherRegistry bean** in `AppConfig.java`:
   - Add `@Bean` method to create TeacherRegistry instance
   - Ensure proper Spring configuration

4. **Create TeacherService** in `src/main/java/com/bootcamp/onlineschool/service/TeacherService.java`:
   - Annotate with `@Service`
   - Use constructor injection for TeacherRegistry
   - Implement methods:
     - `addTeacher(Teacher)` - with null validation
     - `findTeacherById(String)` - throw TeacherNotFoundException if not found
     - `getAllTeachers()` - return all teachers
     - `findTeachersByDepartment(String)` - filter by department
     - `getExperiencedTeachers(int)` - filter by experience
     - `removeTeacher(String)` - remove teacher
     - `getTotalTeachers()` - return count
   - Create custom exception: `TeacherNotFoundException`

5. **Create test class** `TeacherServiceTest.java`:
   - Use `@SpringBootTest` annotation
   - Inject TeacherService and TeacherRegistry with `@Autowired`
   - Use `@BeforeEach` to clear registry
   - Write minimum 10 tests covering:
     - Adding teachers
     - Finding teachers by ID
     - Finding teachers by department
     - Getting experienced teachers
     - Removing teachers
     - Exception scenarios
     - Edge cases

### Expected Deliverables
- Teacher.java (entity)
- TeacherRegistry.java (data management)
- Updated AppConfig.java (bean configuration)
- TeacherService.java (service layer)
- TeacherServiceTest.java (minimum 10 tests)

---

## Assignment 2: Department Service with Advanced Features

### Task
Create a `DepartmentService` that manages departments and their associated teachers.

### Requirements

1. **Create Department entity** in `src/main/java/com/bootcamp/onlineschool/model/Department.java`:
   - Fields: `id`, `name`, `head` (teacher name), `budget`, `teacherIds` (List<String>)
   - Constructor with validation
   - Methods:
     - `addTeacherId(String)` - add teacher to department
     - `removeTeacherId(String)` - remove teacher from department
     - `getTeacherCount()` - return number of teachers
     - `isWithinBudget(double)` - check if amount is within budget

2. **Create DepartmentService** in `src/main/java/com/bootcamp/onlineschool/service/DepartmentService.java`:
   - Use `@Service` annotation
   - Use in-memory `HashMap<String, Department>` for storage
   - Implement methods:
     - `createDepartment(String id, String name, String head, double budget)`
     - `getDepartmentById(String)` - throw DepartmentNotFoundException if not found
     - `getAllDepartments()` - return all departments
     - `assignTeacherToDepartment(String deptId, String teacherId)`
     - `removeTeacherFromDepartment(String deptId, String teacherId)`
     - `getDepartmentsByBudgetRange(double min, double max)`
     - `getTotalBudget()` - sum of all department budgets
     - `deleteDepartment(String)` - remove department
   - Create custom exceptions:
     - `DepartmentNotFoundException`
     - `DepartmentAlreadyExistsException`

3. **Create test class** `DepartmentServiceTest.java`:
   - Use `@SpringBootTest` annotation
   - Write minimum 12 tests covering:
     - Creating departments
     - Finding departments
     - Assigning/removing teachers
     - Budget calculations
     - Filtering by budget range
     - Exception handling
     - Edge cases (empty departments, duplicate IDs)

### Expected Deliverables
- Department.java (entity)
- DepartmentService.java (service with in-memory storage)
- DepartmentServiceTest.java (minimum 12 tests)

---

## Assignment 3: Integration Testing and Service Interaction

### Task
Create tests that demonstrate interaction between multiple services.

### Requirements

1. **Create integration test class** `ServiceIntegrationTest.java`:
   - Use `@SpringBootTest` annotation
   - Inject multiple services: StudentService, CourseService, TeacherService, DepartmentService
   - Use `@BeforeEach` to set up test data across services

2. **Write integration tests** (minimum 8 tests):
   - Test creating a complete academic structure:
     - Create department
     - Create teachers and assign to department
     - Create courses with teacher assignments
     - Enroll students in courses
   - Test cross-service queries:
     - Find all teachers in a department
     - Find all courses taught by a specific teacher
     - Find all students in courses of a department
   - Test cascading operations:
     - Remove teacher and verify course updates
     - Remove department and verify teacher updates
   - Test data consistency:
     - Verify counts across services
     - Verify relationships are maintained

3. **Test scenarios to cover**:
   - Complete workflow: Department → Teachers → Courses → Students
   - Data validation across services
   - Exception handling in multi-service operations
   - State consistency after complex operations

### Expected Deliverables
- ServiceIntegrationTest.java (minimum 8 integration tests)

---

## Bonus Challenges (Optional)

### Challenge 1: Application Properties Configuration
Create custom configuration using `application.properties`:
- Add custom properties for:
  - Maximum students per course
  - Maximum teachers per department
  - Default GPA threshold for high achievers
- Create a `@ConfigurationProperties` class to read these values
- Inject configuration into services
- Write tests to verify configuration is loaded correctly

### Challenge 2: Service with Caching
Implement a simple caching mechanism:
- Create a `CachedStudentService` that wraps StudentService
- Cache frequently accessed students in a HashMap
- Implement cache invalidation on updates
- Write tests to verify caching behavior
- Measure performance improvement

### Challenge 3: Event-Driven Architecture
Implement a simple event system:
- Create event classes: `StudentEnrolledEvent`, `TeacherAssignedEvent`
- Create an `EventPublisher` service
- Create an `EventListener` service that logs events
- Integrate events into existing services
- Write tests to verify events are published and received

---

## Running Your Application and Tests

### Start the Spring Boot application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Run all tests:
```bash
mvn clean test
```

### Run specific test class:
```bash
mvn test -Dtest=TeacherServiceTest
mvn test -Dtest=DepartmentServiceTest
mvn test -Dtest=ServiceIntegrationTest
```

### Run with verbose output:
```bash
mvn test -X
```

### Build the project:
```bash
mvn clean install
```

---

## Submission Checklist

Before submitting, ensure you have:

- [ ] All entity classes (Teacher.java, Department.java)
- [ ] TeacherRegistry.java with proper data management
- [ ] Updated AppConfig.java with TeacherRegistry bean
- [ ] All service classes (TeacherService.java, DepartmentService.java)
- [ ] All test classes with proper annotations
- [ ] All tests passing (`mvn clean test` shows 0 failures)
- [ ] Minimum test counts met (10 + 12 + 8 = 30 tests)
- [ ] Proper use of Spring Boot annotations
- [ ] Constructor injection for dependencies
- [ ] Custom exceptions for error handling
- [ ] JavaDoc comments for all public methods
- [ ] Code follows Java naming conventions

### Expected Test Results
Your test execution should show:
```
[INFO] Tests run: 30+, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## Code Quality Guidelines

### Spring Boot Best Practices

1. **Service Layer**
   ```java
   @Service
   public class TeacherService {
       private final TeacherRegistry teacherRegistry;
       
       // Constructor injection (preferred)
       public TeacherService(TeacherRegistry teacherRegistry) {
           this.teacherRegistry = teacherRegistry;
       }
       
       public void addTeacher(Teacher teacher) {
           // Validation
           if (teacher == null) {
               throw new IllegalArgumentException("Teacher cannot be null");
           }
           // Business logic
           teacherRegistry.addTeacher(teacher);
       }
   }
   ```

2. **Configuration**
   ```java
   @Configuration
   public class AppConfig {
       @Bean
       public TeacherRegistry teacherRegistry() {
           return new TeacherRegistry();
       }
   }
   ```

3. **Testing**
   ```java
   @SpringBootTest
   @DisplayName("TeacherService Tests")
   public class TeacherServiceTest {
       @Autowired
       private TeacherService teacherService;
       
       @BeforeEach
       public void setUp() {
           // Clear state before each test
       }
       
       @Test
       @DisplayName("Should add teacher successfully")
       public void testAddTeacher() {
           // Arrange
           Teacher teacher = new Teacher("T001", "John Doe", "john@school.edu");
           
           // Act
           teacherService.addTeacher(teacher);
           
           // Assert
           assertEquals(1, teacherService.getTotalTeachers());
       }
   }
   ```

4. **Exception Handling**
   ```java
   public static class TeacherNotFoundException extends RuntimeException {
       public TeacherNotFoundException(String message) {
           super(message);
       }
   }
   ```

### Naming Conventions
- Service classes: `EntityNameService.java`
- Test classes: `EntityNameServiceTest.java`
- Bean methods: `entityName()` (camelCase)
- Exception classes: `EntityNameNotFoundException`

---

## Common Pitfalls to Avoid

- ❌ Forgetting `@Service` annotation on service classes
- ❌ Using field injection instead of constructor injection
- ❌ Not clearing state in `@BeforeEach` for tests
- ❌ Forgetting `@SpringBootTest` annotation on test classes
- ❌ Not handling null values in service methods
- ❌ Creating beans without `@Bean` annotation
- ❌ Not throwing exceptions for error conditions
- ❌ Testing implementation details instead of behavior
- ❌ Not using `@Autowired` for dependency injection in tests

---

## Tips for Success

1. **Start with Assignment 1** - Build foundation before moving to complex scenarios
2. **Test as you go** - Run tests after implementing each method
3. **Follow the examples** - Reference StudentService and CourseService implementations
4. **Use constructor injection** - It's the recommended approach in Spring Boot
5. **Write tests first** - Consider writing tests before implementation (TDD)
6. **Keep services focused** - Each service should have a single responsibility
7. **Handle exceptions properly** - Create custom exceptions for domain-specific errors
8. **Clear state between tests** - Use @BeforeEach to ensure test isolation

---

## Resources

### Documentation
- [Spring Boot Reference Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Framework Core](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- Lab 3 README.md - Review Spring Boot concepts

### Example Code
- `StudentService.java` - Service layer example with dependency injection
- `CourseService.java` - Service with in-memory storage and exception handling
- `AppConfig.java` - Configuration class with bean definitions
- `StudentServiceTest.java` - Integration testing with @SpringBootTest

### Key Concepts
- Dependency Injection and Inversion of Control (IoC)
- Service Layer Pattern
- Spring Bean Lifecycle
- Integration Testing with Spring Boot

---

## Questions and Support

If you encounter issues:

1. **Check the application starts** - Run `mvn spring-boot:run` to verify configuration
2. **Review error messages** - Spring provides detailed error information
3. **Verify annotations** - Ensure @Service, @Configuration, @Bean are present
4. **Check dependencies** - Ensure pom.xml has required Spring Boot starters
5. **Debug tests** - Use print statements or debugger to understand failures
6. **Consult documentation** - Spring Boot docs are comprehensive
7. **Ask for help** - Reach out during office hours or on the discussion forum

---

**Remember: Spring Boot simplifies Java development, but understanding the fundamentals of dependency injection and service architecture is key to success!** 🚀
