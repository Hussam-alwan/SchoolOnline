package com.bootcamp.onlineschool.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for Student class
 * Demonstrates:
 * - Test setup with @BeforeEach
 * - Assertions (assertEquals, assertTrue, assertThrows, etc.)
 * - Test naming conventions
 * - Test organization
 */
@DisplayName("Student Class Tests")
public class StudentTest {
    
    private Student student;
    
    @BeforeEach
    public void setUp() {
        student = new Student("STU001", "John Doe", "john@school.edu");
    }
    
    @Test
    @DisplayName("Should create student with valid data")
    public void testStudentCreation() {
        assertNotNull(student);
        assertEquals("STU001", student.getStudentId());
        assertEquals("John Doe", student.getName());
        assertEquals("john@school.edu", student.getEmail());
        assertEquals(0.0, student.getGpa());
    }
    
    @Test
    @DisplayName("Should validate email format")
    public void testEmailValidation() {
        assertTrue(student.isValidEmail());
        
        Student invalidStudent = new Student("STU002", "Jane Doe", "invalid-email");
        assertFalse(invalidStudent.isValidEmail());
    }

    @ParameterizedTest
    @DisplayName("Should accept valid  emails")
    @ValueSource(strings = {"student@school.edu", "john.doe@school.edu"})
    public void testValidEmail(String email) {
        Student s = new Student("STU002", "Jane Doe", email);
        assertTrue(s.isValidEmail());
    }

    @ParameterizedTest
    @DisplayName("Should reject invalid emails")
    @ValueSource(strings = {"student@gmail.com", "student@school.com", "invalid"})
    public void testInvalidEmail(String email) {
        Student s = new Student("STU002", "Jane Doe", email);
        assertFalse(s.isValidEmail());
    }

    @Test
    @DisplayName("Should set and get GPA correctly")
    public void testGpaSetterGetter() {
        student.setGpa(3.5);
        assertEquals(3.5, student.getGpa());
        
        student.setGpa(4.0);
        assertEquals(4.0, student.getGpa());
        
        student.setGpa(0.0);
        assertEquals(0.0, student.getGpa());
    }

    @Test
    @DisplayName("Should throw exception for invalid GPA")
    public void testInvalidGpa() {
        assertThrows(IllegalArgumentException.class, () -> student.setGpa(4.5));
        assertThrows(IllegalArgumentException.class, () -> student.setGpa(-1.0));
    }

    @Test
    @DisplayName("Should update student information")
    public void testUpdateStudentInfo() {
        student.setName("Jane Doe");
        student.setEmail("jane@school.edu");

        assertEquals("Jane Doe", student.getName());
        assertEquals("jane@school.edu", student.getEmail());
    }

    @Test
    @DisplayName("Should generate correct toString representation")
    public void testToString() {
        student.setGpa(3.8);
        String result = student.toString();

        assertTrue(result.contains("STU001"));
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("john@school.edu"));
        assertTrue(result.contains("3.80"));
    }

    @Test
    @DisplayName("Should compare students by ID")
    public void testEqualsAndHashCode() {
        Student student2 = new Student("STU001", "Different Name", "different@school.edu");
        Student student3 = new Student("STU002", "John Doe", "john@school.edu");

        assertEquals(student, student2);
        assertNotEquals(student, student3);
        assertEquals(student.hashCode(), student2.hashCode());
    }

    @ParameterizedTest
    @DisplayName("Should accept valid age values")
    @ValueSource(ints = {16, 50, 100})
    public void testValidAge(int age) {
        Student s = new Student("STU002", "Jane Doe", "jane@school.edu", age);
        assertEquals(age, s.getAge());
    }

    @ParameterizedTest
    @DisplayName("Should throw exception for invalid age in constructor")
    @ValueSource(ints = {15, 101, -5})
    public void testInvalidAgeInConstructor(int age) {
        assertThrows(IllegalArgumentException.class, () ->
                new Student("STU002", "Jane Doe", "jane@school.edu", age));
    }

    @ParameterizedTest
    @DisplayName("Should accept valid age values through setter")
    @ValueSource(ints = {16, 50, 100})
    public void testSetAgeValid(int age) {
        Student s = new Student("STU002", "Jane Doe", "jane@school.edu", 20);
        s.setAge(age);
        assertEquals(age, s.getAge());
    }

    @ParameterizedTest
    @DisplayName("Should throw exception for invalid age through setter")
    @ValueSource(ints = {15, 101, -5})
    public void testSetAgeInvalid(int age) {
        Student s = new Student("STU002", "Jane Doe", "jane@school.edu", 20);
        assertThrows(IllegalArgumentException.class, () -> s.setAge(age));
    }
}
