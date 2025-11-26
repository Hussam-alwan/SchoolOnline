# Online School Backend - Lab 1: Java Fundamentals

A Java-based bootcamp project designed to teach core Java concepts, object-oriented programming principles, and fundamental data structures.

## Lab Overview

Lab 1 focuses on Java fundamentals without Spring Boot or database dependencies. This lab covers:

- Core Java concepts and syntax
- Object-oriented programming (OOP) principles
- Classes, inheritance, and polymorphism
- Collections and data structures
- Unit testing with JUnit 5
- Student registry system implementation

## Technology Stack

- **Java:** 21
- **JUnit 5:** 5.10.0 (Testing framework)
- **Maven Compiler Plugin:** 3.11.0
- **Maven Surefire Plugin:** 3.1.2 (Test runner)
- **Maven:** 3.6 or higher

## Getting Started

### Prerequisites

- Java 21 LTS or higher
- Maven 3.6 or higher

### Running Tests

Run all tests:
```bash
mvn clean test
```

Run specific test class:
```bash
mvn test -Dtest=StudentTest
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
│       │   ├── Teacher.java
│       │   └── User.java
│       └── registry/
│           └── StudentRegistry.java
└── test/
    └── java/com/bootcamp/onlineschool/
        ├── model/
        │   └── StudentTest.java
        └── StudentRegistryTest.java
```

## Key Concepts Covered

### Object-Oriented Programming
- **Encapsulation:** Private fields with public getters/setters
- **Inheritance:** User base class with Student and Teacher subclasses
- **Polymorphism:** Method overriding and interface implementation
- **Abstraction:** Abstract classes and interfaces

### Collections & Data Structures
- ArrayList for dynamic collections
- HashMap for key-value storage
- Streams API for functional programming
- Sorting and filtering operations

### Testing
- Unit testing with JUnit 5
- Test organization with nested test classes
- Parameterized tests
- Assertions and test fixtures

## Test Coverage

- **StudentTest:** 7 tests covering Student model functionality
- **StudentRegistryTest:** 12 tests covering registry operations

**Total:** 19 tests

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

This is **Lab 1** of the bootcamp curriculum:

- **Lab 1:** Java Fundamentals (current)
- **Lab 2:** JUnit Testing - Advanced testing patterns
- **Lab 3:** Spring Boot Basics - REST APIs and services
- **Lab 4:** Database & SQL - JPA and database operations

To switch to other labs:
```bash
# Lab 2
git checkout lab/junit-2-testing

# Lab 3
git checkout lab/springboot-3-basics

# Lab 4
git checkout lab/database-4-sql
```