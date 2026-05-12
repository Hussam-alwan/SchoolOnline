package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        Course course = courseService.createCourse("CS101", "Java Basics", 3, "Dr. Smith");
        
        assertNotNull(course);
        assertEquals("CS101", course.getCourseId());
        assertEquals("Java Basics", course.getCourseName());
        assertEquals(1, courseService.getTotalCourses());
    }
    
    @Test
    @DisplayName("Should throw exception when creating duplicate course")
    public void testCreateDuplicateCourse() {
        courseService.createCourse("CS101", "Java Basics", 3, "Dr. Smith");
        
        assertThrows(CourseService.CourseAlreadyExistsException.class, 
            () -> courseService.createCourse("CS101", "Different Name", 4, "Dr. Brown"));
    }
    
    @Test
    @DisplayName("Should get course by ID")
    public void testGetCourseById() {
        courseService.createCourse("CS101", "Java Basics", 3, "Dr. Smith");
        
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
        courseService.createCourse("CS101", "Java Basics", 3, "Dr. Smith");
        courseService.createCourse("CS102", "Advanced Java", 4, "Dr. Brown");
        courseService.createCourse("CS103", "Web Development", 3, "Dr. Johnson");
        
        List<Course> courses = courseService.getAllCourses();
        assertEquals(3, courses.size());
    }
    
    @Test
    @DisplayName("Should enroll student in course")
    public void testEnrollStudent() {
        courseService.createCourse("CS101", "Java Basics", 3, "Dr. Smith");
        
        boolean enrolled = courseService.enrollStudent("CS101");
        assertTrue(enrolled);
        
        Course course = courseService.getCourseById("CS101");
        assertEquals(1, course.getEnrolledStudents());
    }
    
    @Test
    @DisplayName("Should not enroll when course is full")
    public void testEnrollWhenFull() {
        Course course = courseService.createCourse("CS101", "Java Basics", 3, "Dr. Smith");
        for (int i = 0; i < course.getMaxStudents(); i++) {
            assertTrue(courseService.enrollStudent("CS101"));
        }
        boolean enrolled = courseService.enrollStudent("CS101");
        assertFalse(enrolled);
    }
    
    @Test
    @DisplayName("Should unenroll student from course")
    public void testUnenrollStudent() {
        courseService.createCourse("CS101", "Java Basics", 3, "Dr. Smith");
        courseService.enrollStudent("CS101");
        
        boolean unenrolled = courseService.unenrollStudent("CS101");
        assertTrue(unenrolled);
        
        Course course = courseService.getCourseById("CS101");
        assertEquals(0, course.getEnrolledStudents());
    }
    
    @Test
    @DisplayName("Should get available courses")
    public void testGetAvailableCourses() {
        Course cs101 = courseService.createCourse("CS101", "Java Basics", 3, "Dr. Smith");
        courseService.createCourse("CS102", "Advanced Java", 4, "Dr. Brown");
        
        // Fill CS101 to capacity so it is no longer available
        for (int i = 0; i < cs101.getMaxStudents(); i++) {
            assertTrue(courseService.enrollStudent("CS101"));
        }

        List<Course> available = courseService.getAvailableCourses();
        assertEquals(1, available.size());
        assertEquals("CS102", available.get(0).getCourseId());
    }
    
    @Test
    @DisplayName("Should update instructor")
    public void testUpdateInstructor() {
        courseService.createCourse("CS101", "Java Basics", 3, "Dr. Smith");
        
        courseService.updateInstructor("CS101", "Dr. Johnson");
        
        Course course = courseService.getCourseById("CS101");
        assertEquals("Dr. Johnson", course.getInstructor());
    }
    
    @Test
    @DisplayName("Should delete course")
    public void testDeleteCourse() {
        courseService.createCourse("CS101", "Java Basics", 3, "Dr. Smith");
        assertEquals(1, courseService.getTotalCourses());
        
        boolean deleted = courseService.deleteCourse("CS101");
        assertTrue(deleted);
        assertEquals(0, courseService.getTotalCourses());
    }
}
