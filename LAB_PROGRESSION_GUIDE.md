# Online School Project - Lab Progression Guide

This project is structured as a series of progressive labs, each building on the previous one. Each lab is available on a separate GitHub branch and is fully runnable independently.

## Lab Overview

### Lab 1: Java Fundamentals ✅ COMPLETE
**Branch:** `lab/java-1-fundamentals`

**Focus:** Core Java programming concepts without any framework

**What You'll Learn:**
- Classes and Objects
- Encapsulation (private fields, public getters/setters)
- Constructors and constructor overloading
- Collections (ArrayList, HashMap)
- Iteration and Streams API
- Lambda expressions
- Sorting and filtering
- String manipulation
- Exception handling
- JUnit 5 testing basics

**Key Components:**
- `Student.java` - Domain model with validation
- `StudentRegistry.java` - Collection management with Streams
- 19 unit tests covering all functionality

**How to Run:**
```bash
# Compile
mvn clean compile

# Run tests
mvn test

# Run application
mvn exec:java -Dexec.mainClass="com.bootcamp.onlineschool.OnlineSchoolApplication"
```

**Expected Output:**
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

**Test Results:** 19/19 tests passing ✅

---

### Lab 2: JUnit & Testing ✅ COMPLETE
**Branch:** `lab/junit-2-testing`

**Focus:** Advanced testing concepts and best practices

**What You'll Learn:**
- JUnit 5 advanced features and annotations
- Nested test classes for organization
- Parameterized tests with multiple data sources
- Repeated tests for stress testing
- Test fixtures and lifecycle management
- Assertion methods and complex assertions
- Test naming conventions and display names
- Edge cases and boundary testing
- Best practices for maintainable tests

**Key Components:**
- `Course.java` - New domain model for testing
- `CourseTest.java` - 24 tests with nested classes and parameterized tests
- `StudentRegistryAdvancedTest.java` - 24 tests with advanced patterns

**Test Results:** 48/48 tests passing ✅

---

### Lab 3: Spring Boot Basics ✅ COMPLETE
**Branch:** `lab/springboot-3-basics`

**Focus:** Introduction to Spring Boot framework and dependency injection

**What You'll Learn:**
- Spring Boot auto-configuration
- Dependency injection and IoC (Inversion of Control)
- @SpringBootApplication annotation
- @Service and @Configuration annotations
- @Bean creation and management
- Service layer pattern
- Spring Boot testing with @SpringBootTest
- Component scanning
- Exception handling in services

**Key Components:**
- `OnlineSchoolApplication.java` - Spring Boot main class
- `AppConfig.java` - Spring configuration with @Bean
- `StudentService.java` - Service layer with dependency injection
- `CourseService.java` - Service layer with in-memory storage
- `StudentServiceTest.java` - 9 Spring Boot integration tests
- `CourseServiceTest.java` - 11 Spring Boot integration tests

**Test Results:** 20/20 tests passing ✅

---

### Lab 4: Database & SQL (Coming Soon)
**Branch:** `lab/database-4-sql`

**Focus:** Database design and SQL fundamentals

**Topics:**
- Relational database concepts
- SQL queries (SELECT, INSERT, UPDATE, DELETE)
- Joins and relationships
- Indexes and optimization
- H2 in-memory database setup
- Database initialization scripts

---

### Lab 5: ORM & JPA (Coming Soon)
**Branch:** `lab/orm-5-jpa`

**Focus:** Object-Relational Mapping with JPA/Hibernate

**Topics:**
- JPA entities and annotations
- Entity relationships (One-to-Many, Many-to-Many)
- Repository pattern
- CRUD operations
- Query methods
- Entity lifecycle

---

### Lab 6: Backend API (Coming Soon)
**Branch:** `lab/api-6-backend`

**Focus:** RESTful API development with Spring Boot

**Topics:**
- REST principles
- Spring MVC controllers
- Request/response handling
- HTTP methods and status codes
- Error handling and validation
- API documentation with Swagger/OpenAPI

---

### Lab 7: Maven & Build Tools (Coming Soon)
**Branch:** `lab/maven-7-build`

**Focus:** Maven project management and build automation

**Topics:**
- Maven project structure
- pom.xml configuration
- Dependency management
- Build profiles
- Plugin configuration
- Multi-module projects

---

### Lab 8: Frontend HTML & CSS (Coming Soon)
**Branch:** `lab/frontend-8-html-css`

**Focus:** Frontend basics with HTML and CSS

**Topics:**
- HTML5 structure
- CSS styling and layouts
- Responsive design
- CSS Grid and Flexbox
- Form design
- Accessibility

---

### Lab 9: ReactJS (Coming Soon)
**Branch:** `lab/react-9-frontend`

**Focus:** Modern frontend development with React

**Topics:**
- React components and JSX
- State and props
- Hooks (useState, useEffect)
- Component lifecycle
- API integration
- Routing with React Router
- Material-UI components

---

### Lab 10: Full Stack Integration (Coming Soon)
**Branch:** `lab/fullstack-10-integration`

**Focus:** Complete full-stack application

**Topics:**
- Frontend-backend integration
- CORS configuration
- Authentication and authorization
- Deployment considerations
- Performance optimization
- Security best practices

---

## How to Switch Between Labs

```bash
# View all available branches
git branch -a

# Switch to a specific lab
git checkout lab/java-1-fundamentals
git checkout lab/junit-2-testing
# ... etc

# Create a new branch from main
git checkout main
git pull origin main
git checkout -b lab/new-lab-name
```

## Project Structure

Each lab maintains a consistent structure:

```
.
├── pom.xml                          # Maven configuration
├── LAB_X_DESCRIPTION.md             # Lab-specific documentation
├── src/
│   ├── main/java/com/bootcamp/onlineschool/
│   │   ├── model/                   # Domain models
│   │   ├── service/                 # Business logic (added in later labs)
│   │   ├── repository/              # Data access (added in later labs)
│   │   ├── controller/              # REST endpoints (added in later labs)
│   │   └── ...
│   └── test/java/com/bootcamp/onlineschool/
│       └── ...                      # Corresponding tests
└── online-school-frontend/          # React frontend (added in later labs)
```

## Prerequisites

- Java 21 LTS
- Maven 3.6+
- Git
- IDE (IntelliJ IDEA, VS Code, or Eclipse)

## Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/smilar/SchoolOnline.git
   cd SchoolOnline
   ```

2. **Start with Lab 1:**
   ```bash
   git checkout lab/java-1-fundamentals
   ```

3. **Build and run:**
   ```bash
   mvn clean install
   mvn test
   mvn exec:java -Dexec.mainClass="com.bootcamp.onlineschool.OnlineSchoolApplication"
   ```

4. **Read the lab documentation:**
   ```bash
   cat LAB_1_JAVA_FUNDAMENTALS.md
   ```

## Progression Path

Follow the labs in order for a complete learning experience:

```
Lab 1: Java Fundamentals
    ↓
Lab 2: JUnit & Testing
    ↓
Lab 3: Spring Boot Basics
    ↓
Lab 4: Database & SQL
    ↓
Lab 5: ORM & JPA
    ↓
Lab 6: Backend API
    ↓
Lab 7: Maven & Build Tools
    ↓
Lab 8: Frontend HTML & CSS
    ↓
Lab 9: ReactJS
    ↓
Lab 10: Full Stack Integration
```

## Exercises and Challenges

Each lab includes exercises to reinforce learning. Check the lab-specific documentation for:
- Coding exercises
- Challenges
- Extension activities
- Real-world scenarios

## Support and Resources

- **Lab Documentation:** See `LAB_X_*.md` files
- **Code Examples:** Available in each branch
- **Tests:** Comprehensive test suites demonstrate expected behavior
- **Comments:** Code includes detailed comments explaining concepts

## Next Steps

After completing Lab 1, proceed to **Lab 2: JUnit & Testing** to learn advanced testing concepts and best practices.

---

**Last Updated:** November 21, 2025  
**Project:** Online School Management System  
**Version:** 1.0.0
