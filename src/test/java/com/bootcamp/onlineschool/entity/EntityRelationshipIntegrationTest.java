package com.bootcamp.onlineschool.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EntityRelationshipIntegrationTest {

    private Student student;
    private Teacher teacher;
    private Course course;
    private Clazz clazz;
    private Registration registration;

    @BeforeEach
    void setUp() {
        // Create entities for testing relationships
        student = new Student("John Doe", "john@example.com", "STU001", LocalDate.of(2023, 1, 15));
        teacher = new Teacher("Jane Smith", "jane@example.com", "EMP001", "Computer Science", LocalDate.of(2020, 8, 1));
        course = new Course("Java Programming", "Introduction to Java", 3, 40);
        clazz = new Clazz("Java 101", "Fall", 2023, 30, teacher);
        registration = new Registration(LocalDate.now(), "ENROLLED", student, course);
    }

    @Test
    void testStudentClazzManyToManyRelationship() {
        // Add student to class using helper method
        clazz.addStudent(student);
        
        // Verify bidirectional relationship
        assertTrue(student.getClasses().contains(clazz));
        assertTrue(clazz.getStudents().contains(student));
        assertEquals(1, student.getClasses().size());
        assertEquals(1, clazz.getStudents().size());
    }

    @Test
    void testCourseClazzManyToManyRelationship() {
        // Add course to class using helper method
        clazz.addCourse(course);
        
        // Verify bidirectional relationship
        assertTrue(course.getClasses().contains(clazz));
        assertTrue(clazz.getCourses().contains(course));
        assertEquals(1, course.getClasses().size());
        assertEquals(1, clazz.getCourses().size());
    }

    @Test
    void testTeacherClazzOneToManyRelationship() {
        // Verify the relationship was established during setup
        assertEquals(teacher, clazz.getTeacher());
        assertTrue(teacher.getClasses().contains(clazz));
        assertEquals(1, teacher.getClasses().size());
    }

    @Test
    void testStudentRegistrationOneToManyRelationship() {
        // Add registration to student using helper method
        student.addRegistration(registration);
        
        // Verify the relationship
        assertEquals(student, registration.getStudent());
        assertTrue(student.getRegistrations().contains(registration));
        assertEquals(1, student.getRegistrations().size());
    }

    @Test
    void testCourseRegistrationOneToManyRelationship() {
        // Add registration to course using helper method
        course.addRegistration(registration);
        
        // Verify the relationship
        assertEquals(course, registration.getCourse());
        assertTrue(course.getRegistrations().contains(registration));
        assertEquals(1, course.getRegistrations().size());
    }

    @Test
    void testHelperMethods() {
        // Create a new teacher with a class
        Teacher newTeacher = new Teacher("Bob Johnson", "bob@example.com", "EMP002", "Mathematics", LocalDate.of(2019, 9, 1));
        Clazz newClazz = new Clazz("Math 101", "Spring", 2024, 25, newTeacher);
        
        // Add class to teacher using helper method
        newTeacher.addClazz(newClazz);
        
        // Verify relationship was established
        assertTrue(newTeacher.getClasses().contains(newClazz));
        assertEquals(newTeacher, newClazz.getTeacher());
        assertEquals(1, newTeacher.getClasses().size());
    }

    @Test
    void testRemoveRelationships() {
        // Add relationships first
        student.addRegistration(registration);
        course.addRegistration(registration);
        clazz.addStudent(student);
        clazz.addCourse(course);
        
        // Verify relationships are established
        assertEquals(1, student.getRegistrations().size());
        assertEquals(1, course.getRegistrations().size());
        assertEquals(1, student.getClasses().size());
        assertEquals(1, course.getClasses().size());
        
        // Remove relationships
        student.removeRegistration(registration);
        course.removeRegistration(registration);
        clazz.removeStudent(student);
        clazz.removeCourse(course);
        
        // Verify relationships are removed
        assertEquals(0, student.getRegistrations().size());
        assertEquals(0, course.getRegistrations().size());
        assertEquals(0, student.getClasses().size());
        assertEquals(0, course.getClasses().size());
        assertNull(registration.getStudent());
        assertNull(registration.getCourse());
    }

    @Test
    void testComplexRelationshipScenario() {
        // Create a complex scenario with multiple relationships
        clazz.addStudent(student);
        clazz.addCourse(course);
        student.addRegistration(registration);
        course.addRegistration(registration);
        
        // Verify all relationships
        // Student-Class relationship
        assertTrue(student.getClasses().contains(clazz));
        assertTrue(clazz.getStudents().contains(student));
        
        // Course-Class relationship
        assertTrue(course.getClasses().contains(clazz));
        assertTrue(clazz.getCourses().contains(course));
        
        // Student has registration for the course
        assertTrue(student.getRegistrations().stream()
            .anyMatch(r -> r.getCourse().equals(course)));
        
        // Course has registration from the student
        assertTrue(course.getRegistrations().stream()
            .anyMatch(r -> r.getStudent().equals(student)));
        
        // Teacher-Class relationship
        assertEquals(teacher, clazz.getTeacher());
        assertTrue(teacher.getClasses().contains(clazz));
    }
}