# Online School Backend - Lab 3: Spring Boot Basics

A Spring Boot backend service designed to teach fundamental Spring Boot concepts, REST API development, and service layer architecture.

## Lab Overview

Lab 3 focuses on Spring Boot fundamentals without database integration. This lab covers:

- Spring Boot application setup and configuration
- Dependency injection and component scanning
- REST API development with Spring Web
- Service layer architecture
- Request/response handling
- Exception handling and error responses
- Student and course management services
- Integration testing with Spring Boot Test

## Technology Stack

- **Java:** 21
- **Spring Boot:** 3.2.0
  - spring-boot-starter-web: 3.2.0 (REST APIs and embedded Tomcat)
  - spring-boot-starter-test: 3.2.0 (Testing framework)
- **Spring Framework:** 6.1.1 (included with Spring Boot 3.2.0)
- **Embedded Server:** Tomcat 10.1.13
- **Testing Libraries (included with spring-boot-starter-test):**
  - JUnit 5: 5.9.3
  - Mockito: 5.2.0
  - AssertJ: 3.24.2
  - Hamcrest: 2.2
- **Maven:** 3.6 or higher
- **Maven Plugins:**
  - spring-boot-maven-plugin: 3.2.0
  - maven-compiler-plugin: 3.11.0
  - maven-surefire-plugin: 3.1.2

## Getting Started

### Prerequisites

- Java 21 LTS or higher
- Maven 3.6 or higher

### Running the Application

Start the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Running Tests

Run all tests:
```bash
mvn clean test
```

Run specific test class:
```bash
mvn test -Dtest=StudentServiceTest
```

Run with verbose output:
```bash
mvn test -X
```

## Project Structure

```
src/
├── main/
│   ├── java/com/bootcamp/onlineschool/
│   │   ├── OnlineSchoolApplication.java
│   │   ├── controller/
│   │   │   ├── StudentController.java
│   │   │   └── CourseController.java
│   │   ├── service/
│   │   │   ├── StudentService.java
│   │   │   └── CourseService.java
│   │   ├── entity/
│   │   │   ├── Student.java
│   │   │   ├── Course.java
│   │   │   ├── Teacher.java
│   │   │   └── User.java
│   │   ├── dto/
│   │   │   ├── StudentDTO.java
│   │   │   └── CourseDTO.java
│   │   ├── exception/
│   │   │   └── GlobalExceptionHandler.java
│   │   └── config/
│   │       └── AppConfig.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/bootcamp/onlineschool/
        └── service/
            ├── StudentServiceTest.java
            └── CourseServiceTest.java
```

## Key Spring Boot Concepts Covered

### Core Concepts
- **Spring Application Context:** Dependency injection container
- **Component Scanning:** Auto-discovery of Spring components
- **Dependency Injection:** Constructor and field injection
- **Bean Lifecycle:** Creation, initialization, and destruction

### REST API Development
- **@RestController:** REST endpoint definition
- **@RequestMapping/@GetMapping/@PostMapping:** HTTP method mapping
- **@PathVariable/@RequestParam:** Request parameter handling
- **@RequestBody/@ResponseBody:** Request/response serialization
- **HTTP Status Codes:** Proper response status handling

### Service Layer
- **@Service:** Service component annotation
- **Business Logic:** Separation of concerns
- **Data Transfer Objects (DTOs):** Request/response models
- **Exception Handling:** Custom exceptions and global handlers

### Testing
- **@SpringBootTest:** Integration testing
- **@MockBean:** Mocking Spring beans
- **TestRestTemplate:** REST client for testing
- **Assertions:** Validating responses and behavior

## Test Coverage

- **StudentServiceTest:** 9 tests covering student service operations
- **CourseServiceTest:** 11 tests covering course service operations

**Total:** 20 tests

## Building the Project

Build the project:
```bash
mvn clean build
```

Build without running tests:
```bash
mvn clean build -DskipTests
```

Compile only:
```bash
mvn clean compile
```

## Lab Progression

This is **Lab 3** of the bootcamp curriculum:

- **Lab 1:** Java Fundamentals - Core Java and OOP
- **Lab 2:** JUnit Testing - Advanced testing patterns
- **Lab 3:** Spring Boot Basics - REST APIs and services (current)
- **Lab 4:** Database & SQL - JPA and database operations

To switch to other labs:
```bash
# Lab 1
git checkout lab/java-1-fundamentals

# Lab 2
git checkout lab/junit-2-testing

# Lab 4
git checkout lab/database-4-sql
```