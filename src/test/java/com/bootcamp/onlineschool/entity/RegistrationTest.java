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

class RegistrationTest {

    private Validator validator;
    private Student student;
    private Course course;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        // Create test student and course
        student = new Student("John Doe", "john@example.com", "STU001", LocalDate.of(2023, 1, 15));
        course = new Course("Java Programming", "Introduction to Java", 3, 40);
    }

    @Test
    void testValidRegistration() {
        Registration registration = new Registration(
            LocalDate.now(),
            "ENROLLED",
            student,
            course
        );

        Set<ConstraintViolation<Registration>> violations = validator.validate(registration);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testRegistrationDateRequired() {
        Registration registration = new Registration(
            null,
            "ENROLLED",
            student,
            course
        );

        Set<ConstraintViolation<Registration>> violations = validator.validate(registration);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Registration date is required")));
    }

    @Test
    void testRegistrationDateCannotBeFuture() {
        Registration registration = new Registration(
            LocalDate.now().plusDays(1),
            "ENROLLED",
            student,
            course
        );

        Set<ConstraintViolation<Registration>> violations = validator.validate(registration);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Registration date cannot be in the future")));
    }

    @Test
    void testStatusRequired() {
        Registration registration = new Registration(
            LocalDate.now(),
            null,
            student,
            course
        );

        Set<ConstraintViolation<Registration>> violations = validator.validate(registration);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Status is required")));
    }

    @Test
    void testStatusBlank() {
        Registration registration = new Registration(
            LocalDate.now(),
            "",
            student,
            course
        );

        Set<ConstraintViolation<Registration>> violations = validator.validate(registration);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Status is required")));
    }

    @Test
    void testStatusTooLong() {
        Registration registration = new Registration(
            LocalDate.now(),
            "THIS_STATUS_IS_TOO_LONG_FOR_THE_FIELD",
            student,
            course
        );

        Set<ConstraintViolation<Registration>> violations = validator.validate(registration);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Status must not exceed 20 characters")));
    }

    @Test
    void testGradeTooLong() {
        Registration registration = new Registration(
            LocalDate.now(),
            "ENROLLED",
            student,
            course
        );
        registration.setGrade("A+++++");

        Set<ConstraintViolation<Registration>> violations = validator.validate(registration);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Grade must not exceed 5 characters")));
    }

    @Test
    void testStudentRequired() {
        Registration registration = new Registration(
            LocalDate.now(),
            "ENROLLED",
            null,
            course
        );

        Set<ConstraintViolation<Registration>> violations = validator.validate(registration);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Student is required")));
    }

    @Test
    void testCourseRequired() {
        Registration registration = new Registration(
            LocalDate.now(),
            "ENROLLED",
            student,
            null
        );

        Set<ConstraintViolation<Registration>> violations = validator.validate(registration);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().contains("Course is required")));
    }

    @Test
    void testGettersAndSetters() {
        Registration registration = new Registration();
        LocalDate registrationDate = LocalDate.now();
        String status = "ENROLLED";
        String grade = "A";

        registration.setRegistrationDate(registrationDate);
        registration.setStatus(status);
        registration.setGrade(grade);
        registration.setStudent(student);
        registration.setCourse(course);

        assertEquals(registrationDate, registration.getRegistrationDate());
        assertEquals(status, registration.getStatus());
        assertEquals(grade, registration.getGrade());
        assertEquals(student, registration.getStudent());
        assertEquals(course, registration.getCourse());
    }

    @Test
    void testEqualsAndHashCode() {
        Registration registration1 = new Registration(
            LocalDate.now(),
            "ENROLLED",
            student,
            course
        );
        Registration registration2 = new Registration(
            LocalDate.now(),
            "ENROLLED",
            student,
            course
        );

        // Test equals with same object
        assertEquals(registration1, registration1);

        // Test equals with null
        assertNotEquals(registration1, null);

        // Test equals with different class
        assertNotEquals(registration1, "not a registration");

        // Test equals with different objects but no IDs
        assertNotEquals(registration1, registration2);

        // Test hashCode consistency
        assertEquals(registration1.hashCode(), registration1.hashCode());
    }

    @Test
    void testToString() {
        Registration registration = new Registration(
            LocalDate.of(2023, 9, 1),
            "ENROLLED",
            student,
            course
        );
        registration.setGrade("A");

        String toString = registration.toString();
        assertTrue(toString.contains("Registration{"));
        assertTrue(toString.contains("registrationDate=2023-09-01"));
        assertTrue(toString.contains("status='ENROLLED'"));
        assertTrue(toString.contains("grade='A'"));
    }
}