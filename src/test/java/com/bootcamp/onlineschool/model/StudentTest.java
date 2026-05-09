package com.bootcamp.onlineschool.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Student Class Tests")
public class StudentTest {

    private Student student;

    @BeforeEach
    public void setUp() {
        student = new Student("STU001", "Alice Johnson", "alice@school.edu", 3.8);
    }

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create student with 3-arg constructor and default GPA")
        public void testCreateStudentBasicConstructor() {
            Student s = new Student("STU002", "Bob Smith", "bob@school.edu");

            assertNotNull(s);
            assertEquals("STU002", s.getStudentId());
            assertEquals("Bob Smith", s.getName());
            assertEquals("bob@school.edu", s.getEmail());
            assertEquals(0.0, s.getGpa());
        }

        @Test
        @DisplayName("Should create student with all fields constructor")
        public void testCreateStudentFullConstructor() {
            assertNotNull(student);
            assertEquals("STU001", student.getStudentId());
            assertEquals("Alice Johnson", student.getName());
            assertEquals("alice@school.edu", student.getEmail());
            assertEquals(3.8, student.getGpa());
        }

        @ParameterizedTest
        @CsvSource({
                "STU001, Alice,   alice@school.edu,   3.8",
                "STU002, Bob,     bob@school.edu,     0.0",
                "STU003, Charlie, charlie@school.edu, 4.0",
                "STU004, Diana,   diana@school.edu,   2.5"
        })
        @DisplayName("Should create students with various valid data")
        public void testCreateStudentsVariousData(String id, String name, String email, double gpa) {
            Student s = new Student(id, name, email, gpa);

            assertEquals(id, s.getStudentId());
            assertEquals(name.trim(), s.getName());
            assertEquals(email.trim(), s.getEmail());
            assertEquals(gpa, s.getGpa());
        }
    }

    // ─────────────────────────────────────────────
    @Nested
    @DisplayName("GPA Validation Tests")
    class GpaValidationTests {

        @ParameterizedTest
        @ValueSource(doubles = {0.0, 1.0, 2.0, 2.5, 3.5, 3.8, 4.0})
        @DisplayName("Should accept valid GPA values")
        public void testValidGpaValues(double gpa) {
            student.setGpa(gpa);
            assertEquals(gpa, student.getGpa());
        }

        @ParameterizedTest
        @ValueSource(doubles = {-0.1, -1.0, 4.1, 5.0, 10.0})
        @DisplayName("Should throw exception for invalid GPA values")
        public void testInvalidGpaValues(double invalidGpa) {
            assertThrows(IllegalArgumentException.class, () -> student.setGpa(invalidGpa));
        }

        @Test
        @DisplayName("Should keep old GPA when invalid value is set")
        public void testGpaNotChangedAfterInvalidSet() {
            double originalGpa = student.getGpa();
            assertThrows(IllegalArgumentException.class, () -> student.setGpa(5.0));
            assertEquals(originalGpa, student.getGpa());
        }

        @Test
        @DisplayName("Should accept boundary GPA values 0.0 and 4.0")
        public void testGpaBoundaryValues() {
            student.setGpa(0.0);
            assertEquals(0.0, student.getGpa());

            student.setGpa(4.0);
            assertEquals(4.0, student.getGpa());
        }
    }

    // ─────────────────────────────────────────────
    @Nested
    @DisplayName("Email Validation Tests")
    class EmailValidationTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "alice@school.edu",
                "bob@gmail.com",
                "user@domain.org",
                "name@company.co"
        })
        @DisplayName("Should return true for valid emails")
        public void testValidEmails(String email) {
            student.setEmail(email);
            assertTrue(student.isValidEmail());
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "invalidEmail",
                "missing-at-sign.com",
                "nothing@domain",
                "@nodomain"
        })
        @DisplayName("Should return false for invalid emails")
        public void testInvalidEmails(String email) {
            student.setEmail(email);
            assertFalse(student.isValidEmail());
        }

        @Test
        @DisplayName("Should return false for null email")
        public void testNullEmail() {
            student.setEmail(null);
            assertFalse(student.isValidEmail());
        }
    }

    // ─────────────────────────────────────────────
    @Nested
    @DisplayName("Setters Tests")
    class SettersTests {

        @Test
        @DisplayName("Should update student name")
        public void testSetName() {
            student.setName("Alice Smith");
            assertEquals("Alice Smith", student.getName());
        }

        @Test
        @DisplayName("Should update student ID")
        public void testSetStudentId() {
            student.setStudentId("STU999");
            assertEquals("STU999", student.getStudentId());
        }

        @Test
        @DisplayName("Should update student email")
        public void testSetEmail() {
            student.setEmail("newemail@school.edu");
            assertEquals("newemail@school.edu", student.getEmail());
        }
    }

    // ─────────────────────────────────────────────
    @Nested
    @DisplayName("toString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should contain all student fields in toString")
        public void testToStringContainsAllFields() {
            String result = student.toString();

            assertTrue(result.contains("STU001"));
            assertTrue(result.contains("Alice Johnson"));
            assertTrue(result.contains("alice@school.edu"));
            assertTrue(result.contains("3.80"));
        }

        @Test
        @DisplayName("Should match expected toString format")
        public void testToStringFormat() {
            String result = student.toString();
            assertEquals(
                    "Student{id='STU001', name='Alice Johnson', email='alice@school.edu', gpa=3.80}",
                    result
            );
        }
    }

    // ─────────────────────────────────────────────
    @Nested
    @DisplayName("equals and hashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal when same studentId")
        public void testEqualsSameId() {
            Student other = new Student("STU001", "Different Name", "different@school.edu", 2.0);
            assertEquals(student, other);
        }

        @Test
        @DisplayName("Should not be equal when different studentId")
        public void testNotEqualsDifferentId() {
            Student other = new Student("STU999", "Alice Johnson", "alice@school.edu", 3.8);
            assertNotEquals(student, other);
        }

        @Test
        @DisplayName("Should not be equal to null")
        public void testNotEqualsNull() {
            assertNotEquals(null, student);
        }

        @Test
        @DisplayName("Should have same hashCode for equal students")
        public void testHashCodeEqualStudents() {
            Student other = new Student("STU001", "Different Name", "other@school.edu", 1.0);
            assertEquals(student.hashCode(), other.hashCode());
        }

        @Test
        @DisplayName("Should have different hashCode for different students")
        public void testHashCodeDifferentStudents() {
            Student other = new Student("STU999", "Alice Johnson", "alice@school.edu", 3.8);
            assertNotEquals(student.hashCode(), other.hashCode());
        }
    }

    @Nested
    @DisplayName("State Management Tests")
    class StateManagementTests {

        @Test
        @DisplayName("Should reflect all changes after multiple updates")
        public void testMultipleStateChanges() {
            student.setName("Updated Name");
            student.setEmail("updated@school.edu");
            student.setGpa(4.0);

            assertEquals("Updated Name", student.getName());
            assertEquals("updated@school.edu", student.getEmail());
            assertEquals(4.0, student.getGpa());
        }

        @Test
        @DisplayName("Should maintain studentId as identity through updates")
        public void testIdentityPreservedThroughUpdates() {
            String originalId = student.getStudentId();

            student.setName("New Name");
            student.setEmail("new@school.edu");
            student.setGpa(1.0);

            assertEquals(originalId, student.getStudentId());
        }
    }
}