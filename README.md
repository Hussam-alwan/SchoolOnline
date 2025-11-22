# Online School Backend

A Spring Boot backend service for an online school management system designed to teach bootcamp students.

## Features

- Student and Teacher management (both inherit from User entity)
- Course and Class management
- Student registration system
- RESTful APIs for all entities
- H2 in-memory database for development
- Comprehensive API documentation with Swagger

## Technology Stack

- **Java:** 21
- **Spring Boot:** 3.2.0
- **Spring Data JPA:** 3.2.0 (included with Spring Boot)
- **Spring Web:** 6.1.1 (included with Spring Boot)
- **Spring Validation:** 6.1.1 (included with Spring Boot)
- **H2 Database:** 2.2.224 (runtime)
- **SpringDoc OpenAPI:** 2.2.0 (Swagger/OpenAPI UI)
- **JUnit 5:** 5.9.3 (included with Spring Boot Test)
- **Mockito:** 5.2.0 (included with Spring Boot Test)
- **Maven:** 3.6 or higher

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

### H2 Database Console

Access the H2 database console at: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

### Database Configuration

The application uses H2 in-memory database for development and testing:

- **Development:** In-memory database that resets on application restart
- **Testing:** Separate in-memory database for test isolation
- **Schema:** Auto-generated from JPA entities (Hibernate DDL)
- **Sample Data:** Loaded from `data.sql` on application startup

### API Documentation

Once the application is running, access the Swagger UI at:
`http://localhost:8080/swagger-ui.html`

## Project Structure

```
src/
├── main/
│   ├── java/com/bootcamp/onlineschool/
│   │   ├── OnlineSchoolApplication.java
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── entity/
│   │   │   ├── User.java (base entity)
│   │   │   ├── Student.java
│   │   │   ├── Teacher.java
│   │   │   ├── Course.java
│   │   │   ├── StudentEntity.java
│   │   │   ├── CourseEntity.java
│   │   │   ├── Clazz.java
│   │   │   └── Registration.java
│   │   ├── dto/
│   │   ├── exception/
│   │   └── config/
│   └── resources/
│       ├── application.properties
│       ├── application-test.properties
│       ├── schema.sql
│       └── data.sql
└── test/
    ├── java/com/bootcamp/onlineschool/
    │   ├── repository/
    │   ├── service/
    │   ├── entity/
    │   └── exception/
    └── resources/
        └── test-data.sql
```

## Lab Progression

This project is structured as a progressive bootcamp curriculum:

- **Lab 1: Java Fundamentals** - Core Java concepts, OOP principles, and basic data structures
- **Lab 2: JUnit Testing** - Unit testing with JUnit 5, nested tests, parameterized tests, and advanced testing patterns
- **Lab 3: Spring Boot Basics** - Spring Boot application setup, dependency injection, service layer, and REST APIs
- **Lab 4: Database & SQL** - Database design, JPA entity mapping, Spring Data repositories, and SQL queries

## Running Tests

Run all tests:
```bash
mvn clean test
```

Run specific test class:
```bash
mvn test -Dtest=StudentRepositoryTest
```

Run tests for a specific lab:
```bash
# Lab 1
git checkout lab/java-1-fundamentals
mvn clean test

# Lab 2
git checkout lab/junit-2-testing
mvn clean test

# Lab 3
git checkout lab/springboot-3-basics
mvn clean test

# Lab 4
git checkout lab/database-4-sql
mvn clean test
```

## Building the Project

Build the project:
```bash
mvn clean build
```

Build without running tests:
```bash
mvn clean build -DskipTests
```

## Dependencies Overview

### Core Dependencies
- **spring-boot-starter-web:** REST API support and embedded Tomcat server
- **spring-boot-starter-data-jpa:** JPA/Hibernate ORM framework
- **spring-boot-starter-validation:** Bean validation with Hibernate Validator

### Database
- **h2:** In-memory relational database for development and testing

### API Documentation
- **springdoc-openapi-starter-webmvc-ui:** Swagger UI and OpenAPI 3.0 documentation

### Testing
- **spring-boot-starter-test:** JUnit 5, Mockito, AssertJ, and other testing utilities

## Test Coverage

- **Lab 1:** 19 tests (Java fundamentals and student registry)
- **Lab 2:** 48 tests (JUnit testing patterns and advanced test features)
- **Lab 3:** 20 tests (Spring Boot service layer)
- **Lab 4:** 18 tests (JPA repositories and database operations)

**Total:** 105 tests across all labs