# Online School Backend - Lab 2: JUnit Testing

A comprehensive testing-focused bootcamp project designed to teach advanced JUnit 5 testing patterns, test organization, and testing best practices.

## Lab Overview

Lab 2 focuses on advanced testing techniques and patterns without Spring Boot or database dependencies. This lab covers:

- JUnit 5 fundamentals and advanced features
- Nested test classes for test organization
- Parameterized tests with multiple input sources
- Repeated tests for stress testing
- Test fixtures and setup/teardown
- Assertions and test matchers
- Student registry system with comprehensive tests

## Technology Stack

- **Java:** 21
- **JUnit 5:** 5.10.0 (Testing framework)
  - junit-jupiter-api: 5.10.0
  - junit-jupiter-engine: 5.10.0
- **Spring Boot:** 3.2.0 (Parent POM only)
- **Maven Compiler Plugin:** 3.11.0
- **Maven Surefire Plugin:** 3.1.2 (Test runner)
- **Maven:** 3.6 or higher

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher

### Running Tests

Run all tests:
```bash
mvn clean test
```

Run specific test class:
```bash
mvn test -Dtest=StudentRegistryAdvancedTest
```

Run specific nested test class:
```bash
mvn test -Dtest=StudentRegistryAdvancedTest$ParameterizedTestsDemo
```

Run with verbose output:
```bash
mvn test -X
```

## Project Structure

```
src/
├── main/
│   └── java/com/bootcamp/onlineschool/
│       ├── model/
│       │   ├── Student.java
│       │   ├── Course.java
│       │   ├── Teacher.java
│       │   └── User.java
│       └── registry/
│           └── StudentRegistry.java
└── test/
    └── java/com/bootcamp/onlineschool/
        ├── StudentRegistryAdvancedTest.java
        │   ├── StateManagementTests
        │   ├── ErrorHandlingTests
        │   ├── StatisticsTests
        │   ├── SortingTests
        │   ├── SearchAndFilterTests
        │   ├── RepeatedTestsDemo
        │   └── ParameterizedTestsDemo
        └── model/
            └── CourseTest.java
                ├── EnrollmentTests
                ├── CourseStatusTests
                ├── CourseInformationTests
                └── EdgeCaseTests
```

## Key Testing Concepts Covered

### JUnit 5 Features
- **Nested Test Classes:** Organize related tests using @Nested annotation
- **Parameterized Tests:** Run same test with multiple input values using @ParameterizedTest
- **Repeated Tests:** Execute tests multiple times using @RepeatedTest
- **Display Names:** Custom test names with @DisplayName
- **Test Lifecycle:** @BeforeEach, @AfterEach, @BeforeAll, @AfterAll

### Testing Patterns
- **Arrange-Act-Assert (AAA):** Structured test organization
- **Test Fixtures:** Reusable test data and setup
- **Edge Case Testing:** Boundary conditions and special cases
- **Error Handling:** Testing exception scenarios
- **State Management:** Testing object state transitions

### Advanced Assertions
- Standard JUnit assertions (assertEquals, assertTrue, etc.)
- Collection assertions (assertAll, assertIterableEquals)
- Exception assertions (assertThrows)
- Conditional assertions (assumeTrue, assumeThat)

## Test Coverage

- **StudentRegistryAdvancedTest:** 24 tests across 7 nested test classes
  - StateManagementTests: 2 tests
  - ErrorHandlingTests: 3 tests
  - StatisticsTests: 2 tests
  - SortingTests: 2 tests
  - SearchAndFilterTests: 3 tests
  - RepeatedTestsDemo: 5 tests
  - ParameterizedTestsDemo: 7 tests

- **CourseTest:** 24 tests across 4 nested test classes
  - EnrollmentTests: 11 tests
  - CourseStatusTests: 6 tests
  - CourseInformationTests: 3 tests
  - EdgeCaseTests: 4 tests

**Total:** 48 tests

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

This is **Lab 2** of the bootcamp curriculum:

- **Lab 1:** Java Fundamentals - Core Java and OOP
- **Lab 2:** JUnit Testing - Advanced testing patterns (current)
- **Lab 3:** Spring Boot Basics - REST APIs and services
- **Lab 4:** Database & SQL - JPA and database operations

To switch to other labs:
```bash
# Lab 1
git checkout lab/java-1-fundamentals

# Lab 3
git checkout lab/springboot-3-basics

# Lab 4
git checkout lab/database-4-sql
```