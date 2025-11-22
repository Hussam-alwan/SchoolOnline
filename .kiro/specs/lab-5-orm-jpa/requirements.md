# Lab 5: ORM & JPA - Requirements Document

## Introduction

Lab 5 introduces Object-Relational Mapping (ORM) using JPA (Java Persistence API) and Hibernate. Students will learn how to map Java objects to database tables, manage entity relationships, implement the repository pattern, and perform CRUD operations using Spring Data JPA. This lab builds on the Spring Boot foundation from Lab 3 and adds persistent data storage capabilities.

## Glossary

- **JPA (Java Persistence API):** A Java specification for accessing, persisting, and managing data between Java objects and relational databases
- **Hibernate:** An open-source ORM framework that implements the JPA specification
- **Entity:** A Java class annotated with @Entity that represents a table in the database
- **Repository:** A Spring Data interface that provides CRUD operations and custom query methods for entities
- **Relationship:** An association between two entities (One-to-Many, Many-to-One, Many-to-Many, One-to-One)
- **Cascade:** A setting that determines how operations on a parent entity affect related child entities
- **Lazy Loading:** Deferring the loading of related entities until they are explicitly accessed
- **Eager Loading:** Loading related entities immediately when the parent entity is loaded
- **Query Method:** A Spring Data method that generates SQL queries based on method naming conventions
- **JPQL:** Java Persistence Query Language, an object-oriented query language for JPA entities
- **DTO (Data Transfer Object):** An object used to transfer data between layers without exposing entity details

## Requirements

### Requirement 1: JPA Entity Configuration and Mapping

**User Story:** As a developer, I want to configure JPA entities with proper annotations and mappings, so that Java objects are correctly persisted to the database.

#### Acceptance Criteria

1. WHEN a Student entity is created THEN the system SHALL map it to a database table with @Entity annotation and @Table name specification
2. WHEN a Student entity is persisted THEN the system SHALL automatically generate a unique ID using @GeneratedValue strategy
3. WHEN a Student entity is created THEN the system SHALL include all required fields (id, name, email, gpa) with appropriate @Column annotations
4. WHEN a Course entity is created THEN the system SHALL map it to a database table with proper entity configuration
5. WHEN an entity is loaded from the database THEN the system SHALL correctly deserialize all fields with their proper data types

### Requirement 2: Entity Relationships and Associations

**User Story:** As a developer, I want to define relationships between entities, so that the system can model complex business associations.

#### Acceptance Criteria

1. WHEN a Student enrolls in a Course THEN the system SHALL establish a Many-to-Many relationship between Student and Course entities
2. WHEN a Course is created THEN the system SHALL maintain a list of enrolled students through the relationship
3. WHEN a Student is deleted THEN the system SHALL handle the relationship cleanup appropriately without orphaning data
4. WHEN a relationship is defined THEN the system SHALL specify the join table name and foreign key columns explicitly
5. WHEN entities are loaded THEN the system SHALL correctly populate relationship collections with related entities

### Requirement 3: Spring Data JPA Repository Implementation

**User Story:** As a developer, I want to use Spring Data JPA repositories for data access, so that I can perform CRUD operations without writing boilerplate code.

#### Acceptance Criteria

1. WHEN a StudentRepository is created THEN the system SHALL extend JpaRepository<Student, Long> to provide CRUD operations
2. WHEN findById is called THEN the system SHALL return an Optional containing the Student if found
3. WHEN findAll is called THEN the system SHALL return a list of all students from the database
4. WHEN save is called with a new Student THEN the system SHALL persist the student and return the saved entity with generated ID
5. WHEN delete is called THEN the system SHALL remove the student from the database

### Requirement 4: Custom Query Methods and JPQL

**User Story:** As a developer, I want to create custom query methods in repositories, so that I can retrieve data based on specific criteria.

#### Acceptance Criteria

1. WHEN findByEmail is called THEN the system SHALL return an Optional containing the Student with matching email
2. WHEN findByNameContainingIgnoreCase is called THEN the system SHALL return a list of students whose names contain the search term (case-insensitive)
3. WHEN a custom @Query method is used THEN the system SHALL execute the provided JPQL query correctly
4. WHEN findByCourseId is called on StudentRepository THEN the system SHALL return all students enrolled in the specified course
5. WHEN a query returns no results THEN the system SHALL return an empty Optional or empty list appropriately

### Requirement 5: Entity Lifecycle and Persistence Context

**User Story:** As a developer, I want to understand entity lifecycle management, so that I can properly manage entity states and persistence operations.

#### Acceptance Criteria

1. WHEN an entity is created but not saved THEN the system SHALL maintain it in transient state
2. WHEN an entity is saved to the database THEN the system SHALL transition it to managed state
3. WHEN a managed entity is modified THEN the system SHALL track changes and persist them on transaction commit
4. WHEN an entity is deleted THEN the system SHALL transition it to removed state
5. WHEN a transaction completes THEN the system SHALL flush pending changes to the database

### Requirement 6: Cascade Operations and Relationship Management

**User Story:** As a developer, I want to configure cascade operations, so that related entities are properly managed during parent operations.

#### Acceptance Criteria

1. WHEN a Course is deleted THEN the system SHALL handle the Many-to-Many relationship appropriately without cascading to students
2. WHEN a Student is saved with new courses THEN the system SHALL persist the relationship without requiring explicit course saves
3. WHEN cascade is configured THEN the system SHALL apply the specified operation (PERSIST, MERGE, REMOVE) to related entities
4. WHEN a relationship is bidirectional THEN the system SHALL maintain consistency on both sides of the relationship
5. WHEN orphan removal is configured THEN the system SHALL delete child entities when removed from parent collections

### Requirement 7: Data Persistence and Transaction Management

**User Story:** As a developer, I want to manage transactions and ensure data consistency, so that database operations are atomic and reliable.

#### Acceptance Criteria

1. WHEN a save operation is performed THEN the system SHALL wrap it in a transaction automatically
2. WHEN multiple operations are performed THEN the system SHALL commit all changes atomically or rollback on error
3. WHEN a transaction fails THEN the system SHALL rollback all changes and maintain data consistency
4. WHEN @Transactional is applied to a service method THEN the system SHALL manage the transaction lifecycle automatically
5. WHEN a database constraint is violated THEN the system SHALL throw an appropriate exception and rollback the transaction

### Requirement 8: Repository Testing with JPA

**User Story:** As a developer, I want to test repository operations, so that I can verify data persistence and retrieval work correctly.

#### Acceptance Criteria

1. WHEN a repository test is executed THEN the system SHALL use @DataJpaTest to load only JPA components
2. WHEN a student is saved in a test THEN the system SHALL persist it to the test database and retrieve it successfully
3. WHEN a custom query method is tested THEN the system SHALL verify it returns correct results for various inputs
4. WHEN a relationship is tested THEN the system SHALL verify that related entities are correctly associated
5. WHEN a test completes THEN the system SHALL rollback changes to maintain test isolation

### Requirement 9: Entity Validation and Constraints

**User Story:** As a developer, I want to apply validation constraints to entities, so that invalid data cannot be persisted to the database.

#### Acceptance Criteria

1. WHEN a Student entity is created with invalid email THEN the system SHALL prevent persistence and raise a validation error
2. WHEN a Student entity is created with null name THEN the system SHALL prevent persistence due to @NotNull constraint
3. WHEN a Student entity is created with GPA outside valid range THEN the system SHALL prevent persistence due to @Min/@Max constraints
4. WHEN validation is triggered THEN the system SHALL provide clear error messages indicating which fields failed validation
5. WHEN a valid entity is created THEN the system SHALL allow persistence without validation errors

### Requirement 10: Database Initialization and Schema Management

**User Story:** As a developer, I want to manage database schema and initialization, so that the database is properly set up for the application.

#### Acceptance Criteria

1. WHEN the application starts THEN the system SHALL automatically create database tables based on entity definitions
2. WHEN the application starts THEN the system SHALL execute initialization scripts to populate test data
3. WHEN schema generation is configured THEN the system SHALL use the specified strategy (create, create-drop, update, validate)
4. WHEN the H2 database is used THEN the system SHALL store data in memory for testing purposes
5. WHEN the application stops THEN the system SHALL clean up database resources appropriately
