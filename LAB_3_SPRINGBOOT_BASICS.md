# Lab 3: Spring Boot Basics

## Overview
This lab introduces Spring Boot framework, focusing on dependency injection, service layer pattern, and Spring Boot configuration. Building on Labs 1 and 2, you'll learn how to structure applications using Spring's powerful features.

## Learning Objectives
- ✅ Spring Boot auto-configuration
- ✅ Dependency injection and IoC (Inversion of Control)
- ✅ @SpringBootApplication annotation
- ✅ @Service and @Configuration annotations
- ✅ @Bean creation and management
- ✅ Service layer pattern
- ✅ Spring Boot testing with @SpringBootTest
- ✅ Component scanning
- ✅ Application properties and configuration
- ✅ Exception handling in services

## Project Structure

```
src/main/java/com/bootcamp/onlineschool/
├── OnlineSchoolApplication.java      # Spring Boot main class
├── model/
│   ├── Student.java                  # From Lab 1
│   └── Course.java                   # From Lab 2
├── StudentRegistry.java              # From Lab 1
├── service/
│   ├── StudentService.java           # NEW: Service layer
│   └── CourseService.java            # NEW: Service layer
└── config/
    └── AppConfig.java                # NEW: Spring configuration

src/test/java/com/bootcamp/onlineschool/
├── model/
│   ├── StudentTest.java              # From Lab 1
│   └── CourseTest.java               # From Lab 2
├── StudentRegistryTest.java          # From Lab 1
├── StudentRegistryAdvancedTest.java  # From Lab 2
└── service/
    ├── StudentServiceTest.java       # NEW: Service tests
    └── CourseServiceTest.java        # NEW: Service tests
```

## Key Concepts

### 1. Spring Boot Application

```java
@SpringBootApplication
public class OnlineSchoolApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineSchoolApplication.class, args);
    }
}
```

**What it does:**
- Enables auto-configuration
- Enables component scanning
- Allows defining additional configuration on the class

### 2. Dependency Injection

```java
@Service
public class StudentService {
    private final StudentRegistry studentRegistry;
    
    // Constructor injection
    public StudentService(StudentRegistry studentRegistry) {
        this.studentRegistry = studentRegistry;
    }
}
```

**Benefits:**
- Loose coupling
- Easy testing
- Flexible configuration
- Automatic dependency resolution

### 3. @Service Annotation

```java
@Service
public class StudentService {
    // Business logic here
}
```

**What it does:**
- Marks class as a service component
- Enables automatic bean creation
- Makes it available for dependency injection

### 4. @Configuration and @Bean

```java
@Configuration
public class AppConfig {
    @Bean
    public StudentRegistry studentRegistry() {
        return new StudentRegistry();
    }
}
```

**What it does:**
- Defines configuration class
- Creates beans that can be injected
- Centralizes bean creation logic

### 5. Spring Boot Testing

```java
@SpringBootTest
public class StudentServiceTest {
    @Autowired
    private StudentService studentService;
    
    @Test
    public void testAddStudent() {
        // Test with full Spring context
    }
}
```

**Features:**
- Full application context loaded
- All beans available for injection
- Integration testing
- Real service behavior

## New Components in Lab 3

### StudentService.java
Service layer for student management:
- Add/remove students
- Find students by ID or name
- Get high achievers
- Calculate statistics
- Custom exceptions

### CourseService.java
Service layer for course management:
- Create/delete courses
- Enroll/unenroll students
- Get available courses
- Update course information
- In-memory storage

### AppConfig.java
Spring configuration:
- Bean creation
- Dependency configuration
- Application setup

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
mvn test -Dtest=StudentServiceTest
mvn test -Dtest=CourseServiceTest
```

### Run the application
```bash
mvn spring-boot:run
```

### Build and run JAR
```bash
mvn clean package
java -jar target/online-school-0.0.1-SNAPSHOT.jar
```

## Test Results

**Expected Output:**
```
[INFO] Running com.bootcamp.onlineschool.service.StudentServiceTest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0

[INFO] Running com.bootcamp.onlineschool.service.CourseServiceTest
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0

[INFO] Results:
[INFO] Tests run: 20, Failures: 0, Errors: 0, Skipped: 0
```

**Total: 20 new tests passing ✅**
**Combined with Labs 1-2: 87 tests passing ✅**

## Best Practices

### 1. Constructor Injection
```java
// Good: Constructor injection
@Service
public class StudentService {
    private final StudentRegistry registry;
    
    public StudentService(StudentRegistry registry) {
        this.registry = registry;
    }
}

// Avoid: Field injection
@Service
public class StudentService {
    @Autowired
    private StudentRegistry registry;
}
```

### 2. Service Layer Pattern
```java
// Controller → Service → Repository/Data Access
// Separation of concerns
// Testable business logic
// Reusable services
```

### 3. Exception Handling
```java
@Service
public class StudentService {
    public Student findStudentById(String id) {
        Student student = registry.findStudentById(id);
        if (student == null) {
            throw new StudentNotFoundException("Student not found: " + id);
        }
        return student;
    }
}
```

### 4. Testing with Spring
```java
@SpringBootTest
public class StudentServiceTest {
    @Autowired
    private StudentService studentService;
    
    @BeforeEach
    public void setUp() {
        // Setup before each test
    }
    
    @Test
    public void testBusinessLogic() {
        // Test with full Spring context
    }
}
```

## Exercises

### Exercise 1: Add a TeacherService
Create a new TeacherService with:
- Add/remove teachers
- Find teachers by ID
- Get teachers by department
- Update teacher information

### Exercise 2: Create a RegistrationService
Implement a service for course registrations:
- Register student in course
- Drop course
- Get student's courses
- Get course's students

### Exercise 3: Add Application Properties
Create `application.properties`:
```properties
spring.application.name=online-school
server.port=8080
logging.level.root=INFO
```

### Exercise 4: Implement Custom Configuration
Create configuration for:
- Default course capacity
- Maximum students per class
- Application settings

### Exercise 5: Add Service Validation
Enhance services with:
- Input validation
- Business rule enforcement
- Meaningful error messages

## Advanced Topics

### 1. Profiles
```java
@Configuration
@Profile("dev")
public class DevConfig {
    // Development configuration
}

@Configuration
@Profile("prod")
public class ProdConfig {
    // Production configuration
}
```

### 2. Conditional Beans
```java
@Bean
@ConditionalOnProperty(name = "feature.enabled", havingValue = "true")
public MyService myService() {
    return new MyService();
}
```

### 3. Application Events
```java
@Component
public class StudentEventListener {
    @EventListener
    public void onStudentAdded(StudentAddedEvent event) {
        // Handle event
    }
}
```

## Common Pitfalls to Avoid

1. **Circular Dependencies**
   - Avoid A depends on B, B depends on A
   - Use constructor injection to catch at startup

2. **Mixing Concerns**
   - Keep business logic in services
   - Keep data access in repositories
   - Keep presentation in controllers

3. **Not Using Dependency Injection**
   - Use @Autowired or constructor injection
   - Don't create objects with `new`

4. **Ignoring Spring Lifecycle**
   - Use @PostConstruct and @PreDestroy
   - Understand bean scopes

5. **Poor Exception Handling**
   - Create custom exceptions
   - Provide meaningful error messages
   - Handle exceptions appropriately

## Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Framework Guide](https://spring.io/guides)
- [Dependency Injection](https://spring.io/guides/gs/dependency-injection/)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)

## Next Steps

After completing Lab 3, proceed to **Lab 4: Database & SQL** to learn about database design and SQL fundamentals.

## Summary

Lab 3 introduces Spring Boot and dependency injection, fundamental concepts for enterprise Java development. Key takeaways:

- Spring Boot simplifies application setup and configuration
- Dependency injection promotes loose coupling and testability
- Service layer pattern separates business logic from data access
- @SpringBootTest enables integration testing with full Spring context
- Constructor injection is preferred over field injection
- Custom exceptions improve error handling

**Total Tests in Lab 3: 20 ✅**
**Combined Total: 87 ✅**
