package com.bootcamp.onlineschool.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    private Validator validator;
    private Student validStudent;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        validStudent = new Student(
                "John Doe",
                "john.doe@example.com",
                "STU001",
                LocalDate.of(2023, 1, 15)
        );
    }

    @Test
    void testValidStudent() {
        Set<ConstraintViolation<Student>> violations = validator.validate(validStudent);
        assertTrue(violations.isEmpty(), "Valid student should have no validation errors");
    }

    @Test
    void testStudentIdRequired() {
        validStudent.setStudentId(null);
        Set<ConstraintViolation<Student>> violations = validator.validate(validStudent);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Student ID is required")));
    }

    @Test
    void testStudentIdBlank() {
        validStudent.setStudentId("");
        Set<ConstraintViolation<Student>> violations = validator.validate(validStudent);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Student ID is required")));
    }

    @Test
    void testStudentIdTooShort() {
        validStudent.setStudentId("AB");
        Set<ConstraintViolation<Student>> violations = validator.validate(validStudent);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Student ID must be between 3 and 20 characters")));
    }

    @Test
    void testStudentIdTooLong() {
        validStudent.setStudentId("A".repeat(21));
        Set<ConstraintViolation<Student>> violations = validator.validate(validStudent);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Student ID must be between 3 and 20 characters")));
    }

    @Test
    void testEnrollmentDateRequired() {
        validStudent.setEnrollmentDate(null);
        Set<ConstraintViolation<Student>> violations = validator.validate(validStudent);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Enrollment date is required")));
    }

    @Test
    void testEnrollmentDateInFuture() {
        validStudent.setEnrollmentDate(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<Student>> violations = validator.validate(validStudent);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Enrollment date must be in the past")));
    }

    @Test
    void testInheritedValidation() {
        // Test inherited name validation
        validStudent.setName("");
        Set<ConstraintViolation<Student>> violations = validator.validate(validStudent);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Name is required")));
    }

    @Test
    void testInheritedEmailValidation() {
        // Test inherited email validation
        validStudent.setEmail("invalid-email");
        Set<ConstraintViolation<Student>> violations = validator.validate(validStudent);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Email should be valid")));
    }

    @Test
    void testConstructorWithAllFields() {
        Student student = new Student("Jane Smith", "jane@example.com", "STU002", LocalDate.of(2023, 2, 1));
        
        assertEquals("Jane Smith", student.getName());
        assertEquals("jane@example.com", student.getEmail());
        assertEquals("STU002", student.getStudentId());
        assertEquals(LocalDate.of(2023, 2, 1), student.getEnrollmentDate());
    }

    @Test
    void testDefaultConstructor() {
        Student student = new Student();
        assertNotNull(student);
        assertNull(student.getName());
        assertNull(student.getEmail());
        assertNull(student.getStudentId());
        assertNull(student.getEnrollmentDate());
    }

    @Test
    void testEqualsAndHashCode() {
        Student student1 = new Student("John Doe", "john@example.com", "STU001", LocalDate.of(2023, 1, 15));
        Student student2 = new Student("Jane Smith", "jane@example.com", "STU001", LocalDate.of(2023, 2, 1));
        Student student3 = new Student("Bob Johnson", "bob@example.com", "STU002", LocalDate.of(2023, 1, 15));
        
        // Students with same studentId should be equal
        assertEquals(student1, student2);
        assertEquals(student1.hashCode(), student2.hashCode());
        
        // Students with different studentId should not be equal
        assertNotEquals(student1, student3);
    }

    @Test
    void testToString() {
        String toString = validStudent.toString();
        assertTrue(toString.contains("Student{"));
        assertTrue(toString.contains("name='John Doe'"));
        assertTrue(toString.contains("email='john.doe@example.com'"));
        assertTrue(toString.contains("studentId='STU001'"));
        assertTrue(toString.contains("enrollmentDate=2023-01-15"));
    }
}