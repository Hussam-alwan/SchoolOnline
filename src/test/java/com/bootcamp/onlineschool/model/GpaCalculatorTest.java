package com.bootcamp.onlineschool.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.Map;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GPA Calculator Tests")
public class GpaCalculatorTest {

    private Student student;
    private Course course1;
    private Course course2;
    private Course course3;

    @BeforeEach
    public void setUp() {
        student = new Student("STU001", "John Doe", "john@school.edu", 20);
        course1 = new Course("CS101", "Introduction to Programming", 3);
        course2 = new Course("CS102", "Data Structures", 4);
        course3 = new Course("CS103", "Algorithms", 3);
    }

    @Test
    @DisplayName("Should calculate perfect GPA with all A's")
    public void testPerfectGpa() {
        Map<Course, String> grades = new HashMap<>();
        grades.put(course1, "A");
        grades.put(course2, "A");
        grades.put(course3, "A");

        student.calculateGpa(grades);
        assertEquals(4.0, student.getGpa(), 0.01);
    }

    @Test
    @DisplayName("Should calculate GPA with mixed grades")
    public void testMixedGrades() {
        Map<Course, String> grades = new HashMap<>();
        grades.put(course1, "A"); // 3 credits * 4.0 = 12.0
        grades.put(course2, "B"); // 4 credits * 3.0 = 12.0
        grades.put(course3, "C"); // 3 credits * 2.0 = 6.0
        // total = 30.0 / 10 credits = 3.0

        student.calculateGpa(grades);
        assertEquals(3.0, student.getGpa(), 0.01);
    }

    @Test
    @DisplayName("Should calculate GPA with F grade")
    public void testGpaWithF() {
        Map<Course, String> grades = new HashMap<>();
        grades.put(course1, "F"); // 3 credits * 0.0 = 0.0
        grades.put(course2, "A"); // 4 credits * 4.0 = 16.0
        // total = 16.0 / 7 credits = 2.28

        student.calculateGpa(grades);
        assertEquals(16.0 / 7, student.getGpa(), 0.01);
    }

    @Test
    @DisplayName("Should throw exception for invalid grade")
    public void testInvalidGrade() {
        Map<Course, String> grades = new HashMap<>();
        grades.put(course1, "Z");

        assertThrows(IllegalArgumentException.class, () -> student.calculateGpa(grades));
    }

    @Test
    @DisplayName("Should not change GPA for empty grade map")
    public void testEmptyGradeMap() {
        student.setGpa(3.5);
        student.calculateGpa(new HashMap<>());
        assertEquals(3.5, student.getGpa(), 0.01);
    }
    @Test
    @DisplayName("Should not change GPA for null grade map")
    public void testNullGradeMap() {
        student.setGpa(3.5);
        student.calculateGpa(null);
        assertEquals(3.5, student.getGpa(), 0.01);
    }


    @Test
    @DisplayName("Should calculate GPA with different credit values")
    public void testDifferentCredits() {
        Course heavyCourse = new Course("CS200", "Heavy Course", 6);
        Map<Course, String> grades = new HashMap<>();
        grades.put(heavyCourse, "B"); // 6 credits * 3.0 = 18.0
        grades.put(course1, "A");     // 3 credits * 4.0 = 12.0
        // total = 30.0 / 9 credits = 3.33

        student.calculateGpa(grades);
        assertEquals(30.0 / 9, student.getGpa(), 0.01);
    }

    @Test
    @DisplayName("Should update student GPA field after calculation")
    public void testGpaFieldUpdated() {
        Map<Course, String> grades = new HashMap<>();
        grades.put(course1, "A");

        assertEquals(0.0, student.getGpa());
        student.calculateGpa(grades);
        assertNotEquals(0.0, student.getGpa());
    }
}
