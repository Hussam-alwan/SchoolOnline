# Lab 1: Java Fundamentals

## Overview
This lab focuses on core Java concepts without any framework dependencies. Students will learn fundamental Java programming principles through a simple Student Management System.

## Learning Objectives
- ✅ Classes and Objects
- ✅ Encapsulation (private fields, public getters/setters)
- ✅ Constructors and constructor overloading
- ✅ Collections (ArrayList, HashMap)
- ✅ Iteration and Streams API
- ✅ Lambda expressions
- ✅ Sorting and filtering
- ✅ String manipulation
- ✅ Exception handling
- ✅ Unit testing with JUnit 5

## Project Structure

```
src/main/java/com/bootcamp/onlineschool/
├── OnlineSchoolApplication.java      # Main entry point
├── StudentRegistry.java               # Collection management
└── model/
    └── Student.java                   # Domain model

src/test/java/com/bootcamp/onlineschool/
├── StudentRegistryTest.java           # Registry tests
└── model/
    └── StudentTest.java               # Student tests
```

## Key Classes

### Student.java
Demonstrates:
- Encapsulation with private fields
- Constructor overloading
- Getters and setters with validation
- toString(), equals(), and hashCode() methods
- Email validation

### StudentRegistry.java
Demonstrates:
- ArrayList for storing objects
- HashMap for fast lookups
- Streams API for filtering and sorting
- Lambda expressions
- Functional programming concepts

## Running the Application

### Compile the project
```bash
mvn clean compile
```

### Run the main application
```bash
mvn exec:java -Dexec.mainClass="com.bootcamp.onlineschool.OnlineSchoolApplication"
```

### Run all tests
```bash
mvn test
```

### Run specific test class
```bash
mvn test -Dtest=StudentTest
mvn test -Dtest=StudentRegistryTest
```

## Expected Output

When running the application, you should see:
```
=== Online School - Lab 1: Java Fundamentals ===

All Students:
Student{id='STU001', name='Alice Johnson', email='alice@school.edu', gpa=0.00}
Student{id='STU002', name='Bob Smith', email='bob@school.edu', gpa=0.00}
Student{id='STU003', name='Charlie Brown', email='charlie@school.edu', gpa=0.00}

Searching for student with ID 'STU002':
Found: Student{id='STU002', name='Bob Smith', email='bob@school.edu', gpa=0.00}

Total students: 3
```

## Test Coverage

### StudentTest (10 tests)
- Student creation
- Email validation
- GPA setter/getter
- Invalid GPA handling
- Student information updates
- toString() representation
- equals() and hashCode() comparison

### StudentRegistryTest (13 tests)
- Adding students
- Null student validation
- Invalid email validation
- Finding students by ID
- Finding students by name
- Removing students
- Sorting by name
- Sorting by GPA
- Filtering by GPA threshold
- Average GPA calculation
- Clearing registry

**Total: 23 unit tests**

## Exercises for Students

1. **Add a Course class** with properties: courseId, courseName, credits
2. **Extend StudentRegistry** to support course enrollment
3. **Add validation** for student ID format (must start with "STU")
4. **Implement Comparable** interface for Student to enable natural sorting
5. **Add a GradeBook class** to track student grades
6. **Create custom exceptions** for business logic errors
7. **Add file I/O** to save/load students from a file
8. **Implement a search feature** with multiple criteria

## Key Concepts Covered

### Object-Oriented Programming
- Classes and objects
- Encapsulation
- Constructors
- Methods

### Collections Framework
- ArrayList
- HashMap
- Iteration
- Streams API

### Functional Programming
- Lambda expressions
- Method references
- Functional interfaces

### Testing
- JUnit 5 annotations
- Assertions
- Test setup and teardown
- Test organization

## Dependencies
- Java 21
- JUnit 5.10.0
- Maven 3.6+

## Next Steps
After completing Lab 1, proceed to **Lab 2: JUnit & Testing** for more advanced testing concepts.
