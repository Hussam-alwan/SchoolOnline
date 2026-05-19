package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;

    /**
     * Create a new course
     */
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course createCourse(String courseId, String courseName, int credits,
                               String instructor, int maxStudents) {
        if (courseRepository.existsByCourseId(courseId)) {
            throw new CourseAlreadyExistsException("Course already exists: " + courseId);
        }
        Course course = new Course(courseId, courseName, credits, instructor, maxStudents);
        return courseRepository.save(course);
    }
    
    /**
     * Get course by ID
     */
    @Transactional(readOnly = true)
    public Course getCourseById(String courseId) {
        return courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found: " + courseId));
    }
    
    /**
     * Get all courses
     */
    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    /**
     * Enroll student in course
     */
    public boolean enrollStudent(String courseId) {
        Course course = getCourseById(courseId);
        boolean enrolled = course.enrollStudent();
        if (enrolled) {
            courseRepository.save(course);
        }
        return enrolled;
    }
    
    /**
     * Unenroll student from course
     */
    public boolean unenrollStudent(String courseId) {
        Course course = getCourseById(courseId);
        boolean unenrolled = course.unenrollStudent();
        if (unenrolled) {
            courseRepository.save(course);
        }
        return unenrolled;
    }

    /**
     * Get available courses (not full)
     */
    @Transactional(readOnly = true)
    public List<Course> getAvailableCourses() {
        return courseRepository.findAll().stream()
                .filter(c -> !c.isFull())
                .toList();
    }
    
    /**
     * Update course instructor
     */
    public void updateInstructor(String courseId, String newInstructor) {
        Course course = getCourseById(courseId);
        course.setInstructor(newInstructor);
        courseRepository.save(course);
    }
    
    /**
     * Delete course
     */
    public boolean deleteCourse(String courseId) {
        return courseRepository.findByCourseId(courseId)
                .map(c -> {
                    courseRepository.delete(c);
                    return true;
                })
                .orElse(false);
    }
    
    /**
     * Get total number of courses
     */
    @Transactional(readOnly = true)
    public int getTotalCourses() {
        return (int) courseRepository.count();
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
