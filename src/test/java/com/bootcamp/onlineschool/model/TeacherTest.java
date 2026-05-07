package com.bootcamp.onlineschool.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Teacher Class Tests")
public class TeacherTest {

    private Teacher teacher;
    private Course course1;
    private Course course2;

    @BeforeEach
    public void setUp() {
        teacher = new Teacher("TCH001", "Dr. Smith", "smith@school.edu", "Computer Science");
        course1 = new Course("CS101", "Introduction to Programming", 3);
        course2 = new Course("CS102", "Data Structures", 4);
    }

    @Test
    @DisplayName("Should create teacher with valid data")
    public void testTeacherCreation() {
        assertNotNull(teacher);
        assertEquals("TCH001", teacher.getId());
        assertEquals("Dr. Smith", teacher.getName());
        assertEquals("smith@school.edu", teacher.getEmail());
        assertEquals("Computer Science", teacher.getDepartment());
    }

    @Test
    @DisplayName("Should return Teacher role")
    public void testTeacherRole() {
        assertEquals("Teacher", teacher.getRole());
    }

    @Test
    @DisplayName("Should treat Teacher as User")
    public void testTeacherIsUser() {
        User user = teacher;
        assertEquals("TCH001", user.getId());
        assertEquals("Teacher", user.getRole());
    }

    @Test
    @DisplayName("Should validate email for Teacher")
    public void testTeacherEmailValidation() {
        assertTrue(teacher.isValidEmail());

        Teacher invalidTeacher = new Teacher("TCH002", "Dr. Jones", "jones@gmail.com", "Math");
        assertFalse(invalidTeacher.isValidEmail());
    }

    @Test
    @DisplayName("Should assign course to teacher")
    public void testAssignCourse() {
        teacher.assignCourse(course1);
        assertEquals(1, teacher.getCoursesTaught().size());
        assertTrue(teacher.getCoursesTaught().contains(course1));
    }

    @Test
    @DisplayName("Should not assign duplicate course")
    public void testNoDuplicateCourse() {
        teacher.assignCourse(course1);
        teacher.assignCourse(course1);
        assertEquals(1, teacher.getCoursesTaught().size());
    }

    @Test
    @DisplayName("Should not assign null course")
    public void testAssignNullCourse() {
        teacher.assignCourse(null);
        assertEquals(0, teacher.getCoursesTaught().size());
    }

    @Test
    @DisplayName("Should remove course from teacher")
    public void testRemoveCourse() {
        teacher.assignCourse(course1);
        teacher.assignCourse(course2);
        teacher.removeCourse("CS101");
        assertEquals(1, teacher.getCoursesTaught().size());
        assertFalse(teacher.getCoursesTaught().contains(course1));
    }

    @Test
    @DisplayName("Should do nothing when removing non-existent course")
    public void testRemoveNonExistentCourse() {
        teacher.assignCourse(course1);
        teacher.removeCourse("CS999");
        assertEquals(1, teacher.getCoursesTaught().size());
    }

    @Test
    @DisplayName("Should include role in toString")
    public void testToStringContainsRole() {
        assertTrue(teacher.toString().contains("Teacher"));
        assertTrue(teacher.toString().contains("Computer Science"));
    }
}