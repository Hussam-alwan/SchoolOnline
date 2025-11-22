package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CourseService tests demonstrating Spring Boot service testing
 * 
 * Demonstrates:
 * - @SpringBootTest for integration testing
 * - Service dependency injection
 * - Testing business logic
 * - Exception handling in services
 */
@SpringBootTest
@DisplayName("CourseService Tests")
public class CourseServiceTest {
    
    @Autowired
    private CourseService courseService;
    
    @BeforeEach
    public void setUp() {
        // Clear courses before each test by deleting all existing courses
        courseService.getAllCourses().stream()
            .map(c -> c.getCourseId())
            .forEach(id -> courseService.deleteCourse(id));
    }
    
    @Test
    @DisplayName("Should create course successfully")
    public void testCreateCourse() {
        Course course = courseService.createCourse("CS101", "Java Basics", 3, "Dr. Smith", 30);
        
        assertNotNull(course);
        assertEquals("CS101", course.getCourseId());
        assertEquals("Java Basics", course.getCourseName());
        assertEquals(1, courseService.getTotalCourses());
    }
    
    @Test
    @DisplayName("Should throw exception when creating duplicate course")
    public void testCreateDuplicateCourse() {
        courseService.createCourse("CS101", "Java Basics", 3, "Dr. Smith", 30);
        
        assertThrows(CourseService.CourseAlreadyExistsException.class, 
            () -> courseService.createCourse("CS101", "Different Name", 4, "Dr. Brown", 25));
    }
    
    @Test
    @DisplayName("Should get course by ID")
    public void testGetCourseById() {
        courseService.createCourse("CS101", "Java Basics", 3, "Dr. Smith", 30);
        
        Course course = courseService.getCourseById("CS101");
        assertNotNull(course);
        assertEquals("Java Basics", course.getCourseName());
    }
    
    @Test
    @DisplayName("Should throw exception when course not found")
    public void testGetNonExistentCourse() {
        assertThrows(CourseService.CourseNotFoundException.class, 
            () -> courseService.getCourseById("NONEXISTENT"));
    }
    
    @Test
    @DisplayName("Should get all courses")
    public void testGetAllCourses() {
        courseService.createCourse("CS101", "Java Basics", 3, "Dr. Smith", 30);
        courseService.createCourse("CS102", "Advanced Java", 4, "Dr. Brown", 25);
        courseService.createCourse("CS103", "Web Development", 3, "Dr. Johnson", 20);
        
        List<Course> courses = courseService.getAllCourses();
        assertEquals(3, courses.size());
    }
    
    @Test
    @DisplayName("Should enroll student in course")
    public void testEnrollStudent() {
        courseService.createCourse("CS101", "Java Basics", 3, "Dr. Smith", 30);
        
        boolean enrolled = courseService.enrollStudent("CS101");
        assertTrue(enrolled);
        
        Course course = courseService.getCourseById("CS101");
        assertEquals(1, course.getEnrolledStudents());
    }
    
    @Test
    @DisplayName("Should not enroll when course is full")
    public void testEnrollWhenFull() {
        courseService.createCourse("CS101", "Java Basics", 3, "Dr. Smith", 1);
        
        // Fill the course
        courseService.enrollStudent("CS101");
        
        // Try to enroll another
        boolean enrolled = courseService.enrollStudent("CS101");
        assertFalse(enrolled);
    }
    
    @Test
    @DisplayName("Should unenroll student from course")
    public void testUnenrollStudent() {
        courseService.createCourse("CS101", "Java Basics", 3, "Dr. Smith", 30);
        courseService.enrollStudent("CS101");
        
        boolean unenrolled = courseService.unenrollStudent("CS101");
        assertTrue(unenrolled);
        
        Course course = courseService.getCourseById("CS101");
        assertEquals(0, course.getEnrolledStudents());
    }
    
    @Test
    @DisplayName("Should get available courses")
    public void testGetAvailableCourses() {
        courseService.createCourse("CS101", "Java Basics", 3, "Dr. Smith", 1);
        courseService.createCourse("CS102", "Advanced Java", 4, "Dr. Brown", 30);
        
        // Fill CS101
        courseService.enrollStudent("CS101");
        
        List<Course> available = courseService.getAvailableCourses();
        assertEquals(1, available.size());
        assertEquals("CS102", available.get(0).getCourseId());
    }
    
    @Test
    @DisplayName("Should update instructor")
    public void testUpdateInstructor() {
        courseService.createCourse("CS101", "Java Basics", 3, "Dr. Smith", 30);
        
        courseService.updateInstructor("CS101", "Dr. Johnson");
        
        Course course = courseService.getCourseById("CS101");
        assertEquals("Dr. Johnson", course.getInstructor());
    }
    
    @Test
    @DisplayName("Should delete course")
    public void testDeleteCourse() {
        courseService.createCourse("CS101", "Java Basics", 3, "Dr. Smith", 30);
        assertEquals(1, courseService.getTotalCourses());
        
        boolean deleted = courseService.deleteCourse("CS101");
        assertTrue(deleted);
        assertEquals(0, courseService.getTotalCourses());
    }
}
