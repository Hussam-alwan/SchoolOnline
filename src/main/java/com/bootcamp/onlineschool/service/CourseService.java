package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.config.SchoolProperties;
import com.bootcamp.onlineschool.model.Course;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CourseService demonstrates Spring Boot service with in-memory storage
 * 
 * Demonstrates:
 * - @Service annotation
 * - In-memory data management
 * - Business logic methods
 * - Exception handling
 */
@Service
public class CourseService {
    
    private final Map<String, Course> courses = new HashMap<>();
    private SchoolProperties schoolProperties;

    public CourseService(SchoolProperties schoolProperties) {
        this.schoolProperties = schoolProperties;
    }



    /**
     * Create a new course
     */
    @CacheEvict(value = "courses", allEntries = true)
    public Course createCourse(String courseId, String courseName, int credits, String instructor) {
        if (courses.containsKey(courseId)) {
            throw new CourseAlreadyExistsException("Course already exists: " + courseId);
        }

        int maxStudents = schoolProperties.getMaxStudentsPerCourse();
        Course course = new Course(courseId, courseName, credits, instructor, maxStudents);
        courses.put(courseId, course);
        return course;
    }
    
    /**
     * Get course by ID
     */
    @Cacheable(value = "courses", key = "#courseId")
    public Course getCourseById(String courseId) {
        Course course = courses.get(courseId);
        if (course == null) {
            throw new CourseNotFoundException("Course not found: " + courseId);
        }
        return course;
    }
    
    /**
     * Get all courses - cached
     */
    @Cacheable(value = "courses", key = "'allCourses'")
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }
    
    /**
     * Enroll student in course
     */
    @CacheEvict(value = "courses", allEntries = true)
    public boolean enrollStudent(String courseId) {
        Course course = getCourseById(courseId);
        return course.enrollStudent();
    }
    
    /**
     * Unenroll student from course
     */
    @CacheEvict(value = "courses", allEntries = true)
    public boolean unenrollStudent(String courseId) {
        Course course = getCourseById(courseId);
        return course.unenrollStudent();
    }
    
    /**
     * Get available courses (not full)
     */
    @Cacheable(value = "courses", key = "'availableCourses'")
    public List<Course> getAvailableCourses() {
        return courses.values().stream()
                .filter(c -> !c.isFull())
                .toList();
    }
    
    /**
     * Update course instructor
     */
    @CacheEvict(value = "courses", allEntries = true)
    public void updateInstructor(String courseId, String newInstructor) {
        Course course = getCourseById(courseId);
        course.setInstructor(newInstructor);
    }
    
    /**
     * Delete course
     */
    @CacheEvict(value = "courses", allEntries = true)
    public boolean deleteCourse(String courseId) {
        return courses.remove(courseId) != null;
    }
    
    /**
     * Get total number of courses
     */
    @Cacheable(value = "courses", key = "'totalCount'")
    public int getTotalCourses() {
        return courses.size();
    }
    
    /**
     * Custom exception for course not found
     */
    public static class CourseNotFoundException extends RuntimeException {
        public CourseNotFoundException(String message) {
            super(message);
        }
    }
    
    /**
     * Custom exception for course already exists
     */
    public static class CourseAlreadyExistsException extends RuntimeException {
        public CourseAlreadyExistsException(String message) {
            super(message);
        }
    }
}
