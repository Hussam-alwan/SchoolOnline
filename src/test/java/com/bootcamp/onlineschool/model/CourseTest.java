package com.bootcamp.onlineschool.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Course Enrollment Tests")
public class CourseTest {

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

    // Course class tests
    @Test
    @DisplayName("Should create course with valid data")
    public void testCourseCreation() {
        assertNotNull(course1);
        assertEquals("CS101", course1.getCourseId());
        assertEquals("Introduction to Programming", course1.getName());
        assertEquals(3, course1.getCredits());
    }

    @Test
    @DisplayName("Should update course fields through setters")
    public void testCourseSetters() {
        course1.setCourseId("CS999");
        course1.setName("New Course");
        course1.setCredits(5);

        assertEquals("CS999", course1.getCourseId());
        assertEquals("New Course", course1.getName());
        assertEquals(5, course1.getCredits());
    }

    @Test
    @DisplayName("Should compare courses by all fields")
    public void testCourseEqualsAndHashCode() {
        Course sameCourse = new Course("CS101", "Introduction to Programming", 3);
        Course differentCourse = new Course("CS999", "Other Course", 3);

        assertEquals(course1, sameCourse);
        assertNotEquals(course1, differentCourse);
        assertEquals(course1.hashCode(), sameCourse.hashCode());
    }

    @Test
    @DisplayName("Should generate correct toString representation")
    public void testCourseToString() {
        String result = course1.toString();
        assertTrue(result.contains("CS101"));
        assertTrue(result.contains("Introduction to Programming"));
        assertTrue(result.contains("3"));
    }

    // Enrollment tests
    @Test
    @DisplayName("Should enroll student in a course")
    public void testEnrollInCourse() {
        student.enrollInCourse(course1);
        assertEquals(1, student.getEnrolledCourses().size());
        assertTrue(student.getEnrolledCourses().contains(course1));
    }

    @Test
    @DisplayName("Should not enroll duplicate course")
    public void testNoDuplicateEnrollment() {
        student.enrollInCourse(course1);
        student.enrollInCourse(course1);
        assertEquals(1, student.getEnrolledCourses().size());
    }

    @Test
    @DisplayName("Should not enroll null course")
    public void testEnrollNullCourse() {
        student.enrollInCourse(null);
        assertEquals(0, student.getEnrolledCourses().size());
    }

    @Test
    @DisplayName("Should drop an enrolled course")
    public void testDropCourse() {
        student.enrollInCourse(course1);
        student.enrollInCourse(course2);

        student.dropCourse("CS101");
        assertEquals(1, student.getEnrolledCourses().size());
        assertFalse(student.getEnrolledCourses().contains(course1));
    }

    @Test
    @DisplayName("Should do nothing when dropping non-existent course")
    public void testDropNonExistentCourse() {
        student.enrollInCourse(course1);
        student.dropCourse("CS999");
        assertEquals(1, student.getEnrolledCourses().size());
    }

    @Test
    @DisplayName("Should calculate total credits correctly")
    public void testGetTotalCredits() {
        student.enrollInCourse(course1); // 3 credits
        student.enrollInCourse(course2); // 4 credits
        student.enrollInCourse(course3); // 3 credits
        assertEquals(10, student.getTotalCredits());
    }

    @Test
    @DisplayName("Should return 0 total credits when no courses enrolled")
    public void testGetTotalCreditsEmpty() {
        assertEquals(0, student.getTotalCredits());
    }

    @Test
    @DisplayName("Should update total credits after dropping course")
    public void testTotalCreditsAfterDrop() {
        student.enrollInCourse(course1); // 3 credits
        student.enrollInCourse(course2); // 4 credits
        student.dropCourse("CS101");
        assertEquals(4, student.getTotalCredits());
    }
}