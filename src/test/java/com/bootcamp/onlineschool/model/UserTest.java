package com.bootcamp.onlineschool.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Class Tests")
public class UserTest {

    private User student;
    private User teacher;

    @BeforeEach
    public void setUp() {
        student = new Student("STU001", "John Doe", "john@school1.edu", 20);
        teacher = new Teacher("TCH001", "Dr. Smith", "smith@school1.edu", "Computer Science");
    }

    @Test
    @DisplayName("Should get id correctly")
    public void testGetId() {
        assertEquals("STU001", student.getId());
        assertEquals("TCH001", teacher.getId());
    }

    @Test
    @DisplayName("Should get name correctly")
    public void testGetName() {
        assertEquals("John Doe", student.getName());
        assertEquals("Dr. Smith", teacher.getName());
    }

    @Test
    @DisplayName("Should get email correctly")
    public void testGetEmail() {
        assertEquals("john@school1.edu", student.getEmail());
        assertEquals("smith@school1.edu", teacher.getEmail());
    }

    @Test
    @DisplayName("Should update name through setter")
    public void testSetName() {
        student.setName("Jane Doe");
        assertEquals("Jane Doe", student.getName());
    }

    @Test
    @DisplayName("Should update email through setter")
    public void testSetEmail() {
        student.setEmail("jane@school1.edu");
        assertEquals("jane@school1.edu", student.getEmail());
    }

    @Test
    @DisplayName("Should return correct role for each user type")
    public void testGetRole() {
        assertEquals("Student", student.getRole());
        assertEquals("Teacher", teacher.getRole());
    }

    @Test
    @DisplayName("Should validate email correctly")
    public void testIsValidEmail() {
        assertTrue(student.isValidEmail());
        assertTrue(teacher.isValidEmail());
    }

    @Test
    @DisplayName("Should reject invalid email")
    public void testIsInvalidEmail() {
        student.setEmail("john@gmail.com");
        assertFalse(student.isValidEmail());
    }

    @Test
    @DisplayName("Should compare users by ID")
    public void testEqualsAndHashCode() {
        User student2 = new Student("STU001", "Different Name", "different@school1.edu");
        User student3 = new Student("STU002", "John Doe", "john@school1.edu");

        assertEquals(student, student2);
        assertNotEquals(student, student3);
        assertEquals(student.hashCode(), student2.hashCode());
    }

    @Test
    @DisplayName("Should include id, name, email and role in toString")
    public void testToString() {
        String result = student.toString();
        assertTrue(result.contains("STU001"));
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("john@school1.edu"));
        assertTrue(result.contains("Student"));
    }
}