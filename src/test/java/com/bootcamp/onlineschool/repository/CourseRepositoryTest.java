package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CourseRepository tests demonstrating Spring Data JPA repository testing
 * 
 * Demonstrates:
 * - @DataJpaTest for repository testing
 * - Custom query methods
 * - Database operations
 * - Transaction management
 */
@DataJpaTest
@DisplayName("CourseRepository Tests")
public class CourseRepositoryTest {
    
    @Autowired
    private CourseRepository courseRepository;
    
    @BeforeEach
    public void setUp() {
        courseRepository.deleteAll();
    }
    
    @Test
    @DisplayName("Should save and retrieve course")
    public void testSaveAndRetrieve() {
        Course course = new Course("CS101", "Java Basics", 3, "Dr. Smith", 30);
        
        Course saved = courseRepository.save(course);
        assertNotNull(saved.getId());
        
        Optional<Course> found = courseRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Java Basics", found.get().getCourseName());
    }
    
    @Test
    @DisplayName("Should find course by course ID")
    public void testFindByCourseId() {
        Course course = new Course("CS101", "Java Basics", 3, "Dr. Smith", 30);
        courseRepository.save(course);
        
        Optional<Course> found = courseRepository.findByCourseId("CS101");
        assertTrue(found.isPresent());
        assertEquals("Java Basics", found.get().getCourseName());
    }
    
    @Test
    @DisplayName("Should find courses by instructor")
    public void testFindByInstructor() {
        courseRepository.save(new Course("CS101", "Java Basics", 3, "Dr. Smith", 30));
        courseRepository.save(new Course("CS102", "Advanced Java", 4, "Dr. Smith", 25));
        courseRepository.save(new Course("CS103", "Web Dev", 3, "Dr. Brown", 20));
        
        List<Course> courses = courseRepository.findByInstructor("Dr. Smith");
        assertEquals(2, courses.size());
    }
    
    @Test
    @DisplayName("Should find available courses")
    public void testFindAvailableCourses() {
        Course full = new Course("CS101", "Java Basics", 3, "Dr. Smith", 1);
        full.enrollStudent();
        courseRepository.save(full);
        
        courseRepository.save(new Course("CS102", "Advanced Java", 4, "Dr. Brown", 30));
        
        List<Course> available = courseRepository.findAvailableCourses();
        assertEquals(1, available.size());
        assertEquals("CS102", available.get(0).getCourseId());
    }
    
    @Test
    @DisplayName("Should find full courses")
    public void testFindFullCourses() {
        Course full = new Course("CS101", "Java Basics", 3, "Dr. Smith", 1);
        full.enrollStudent();
        courseRepository.save(full);
        
        courseRepository.save(new Course("CS102", "Advanced Java", 4, "Dr. Brown", 30));
        
        List<Course> fullCourses = courseRepository.findFullCourses();
        assertEquals(1, fullCourses.size());
        assertEquals("CS101", fullCourses.get(0).getCourseId());
    }
    
    @Test
    @DisplayName("Should find courses with minimum seats")
    public void testFindCoursesWithMinimumSeats() {
        Course course1 = new Course("CS101", "Java Basics", 3, "Dr. Smith", 30);
        course1.enrollStudent();
        course1.enrollStudent();
        courseRepository.save(course1);
        
        courseRepository.save(new Course("CS102", "Advanced Java", 4, "Dr. Brown", 25));
        
        List<Course> courses = courseRepository.findCoursesWithMinimumSeats(20);
        assertEquals(2, courses.size());
    }
    
    @Test
    @DisplayName("Should count available courses")
    public void testCountAvailableCourses() {
        Course full = new Course("CS101", "Java Basics", 3, "Dr. Smith", 1);
        full.enrollStudent();
        courseRepository.save(full);
        
        courseRepository.save(new Course("CS102", "Advanced Java", 4, "Dr. Brown", 30));
        courseRepository.save(new Course("CS103", "Web Dev", 3, "Dr. Johnson", 20));
        
        Long count = courseRepository.countAvailableCourses();
        assertEquals(2L, count);
    }
    
    @Test
    @DisplayName("Should get total enrollment")
    public void testGetTotalEnrollment() {
        Course course1 = new Course("CS101", "Java Basics", 3, "Dr. Smith", 30);
        course1.enrollStudent();
        course1.enrollStudent();
        courseRepository.save(course1);
        
        Course course2 = new Course("CS102", "Advanced Java", 4, "Dr. Brown", 25);
        course2.enrollStudent();
        courseRepository.save(course2);
        
        Long total = courseRepository.getTotalEnrollment();
        assertEquals(3L, total);
    }
    
    @Test
    @DisplayName("Should update course")
    public void testUpdateCourse() {
        Course course = new Course("CS101", "Java Basics", 3, "Dr. Smith", 30);
        Course saved = courseRepository.save(course);
        
        saved.setInstructor("Dr. Johnson");
        courseRepository.save(saved);
        
        Optional<Course> updated = courseRepository.findById(saved.getId());
        assertTrue(updated.isPresent());
        assertEquals("Dr. Johnson", updated.get().getInstructor());
    }
}
