package com.bootcamp.onlineschool.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    private Validator validator;
    private Course validCourse;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        validCourse = new Course(
                "Introduction to Java",
                "A comprehensive course covering Java fundamentals",
                3,
                40
        );
    }

    @Test
    void testValidCourse() {
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        assertTrue(violations.isEmpty(), "Valid course should have no validation errors");
    }

    @Test
    void testCourseNameRequired() {
        validCourse.setName(null);
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Course name is required")));
    }

    @Test
    void testCourseNameBlank() {
        validCourse.setName("");
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Course name is required")));
    }

    @Test
    void testCourseNameTooShort() {
        validCourse.setName("A");
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Course name must be between 2 and 100 characters")));
    }

    @Test
    void testCourseNameTooLong() {
        validCourse.setName("A".repeat(101));
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Course name must be between 2 and 100 characters")));
    }

    @Test
    void testDescriptionTooLong() {
        validCourse.setDescription("A".repeat(501));
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Description must not exceed 500 characters")));
    }

    @Test
    void testDescriptionCanBeNull() {
        validCourse.setDescription(null);
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        
        // Should not have violations for null description
        assertFalse(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void testCreditsRequired() {
        validCourse.setCredits(null);
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Credits is required")));
    }

    @Test
    void testCreditsMinimumValue() {
        validCourse.setCredits(0);
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Credits must be at least 1")));
    }

    @Test
    void testDurationRequired() {
        validCourse.setDuration(null);
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Duration is required")));
    }

    @Test
    void testDurationMinimumValue() {
        validCourse.setDuration(0);
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Duration must be at least 1")));
    }

    @Test
    void testConstructorWithAllFields() {
        Course course = new Course("Advanced Java", "Deep dive into Java concepts", 4, 60);
        
        assertEquals("Advanced Java", course.getName());
        assertEquals("Deep dive into Java concepts", course.getDescription());
        assertEquals(4, course.getCredits());
        assertEquals(60, course.getDuration());
    }

    @Test
    void testDefaultConstructor() {
        Course course = new Course();
        assertNotNull(course);
        assertNull(course.getName());
        assertNull(course.getDescription());
        assertNull(course.getCredits());
        assertNull(course.getDuration());
    }

    @Test
    void testEqualsAndHashCode() {
        Course course1 = new Course("Java Basics", "Introduction to Java", 3, 40);
        Course course2 = new Course("Python Basics", "Introduction to Python", 3, 35);
        
        // Courses without IDs should not be equal
        assertNotEquals(course1, course2);
        
        // Test with same reference
        assertEquals(course1, course1);
        
        // Test with null
        assertNotEquals(course1, null);
        
        // Test with different class
        assertNotEquals(course1, "not a course");
    }

    @Test
    void testToString() {
        String toString = validCourse.toString();
        assertTrue(toString.contains("Course{"));
        assertTrue(toString.contains("name='Introduction to Java'"));
        assertTrue(toString.contains("description='A comprehensive course covering Java fundamentals'"));
        assertTrue(toString.contains("credits=3"));
        assertTrue(toString.contains("duration=40"));
    }

    @Test
    void testSettersAndGetters() {
        Course course = new Course();
        
        course.setName("Test Course");
        assertEquals("Test Course", course.getName());
        
        course.setDescription("Test Description");
        assertEquals("Test Description", course.getDescription());
        
        course.setCredits(5);
        assertEquals(5, course.getCredits());
        
        course.setDuration(80);
        assertEquals(80, course.getDuration());
        
        course.setId(1L);
        assertEquals(1L, course.getId());
    }
}