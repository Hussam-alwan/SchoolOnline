package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.dto.CourseDTO;
import com.bootcamp.onlineschool.model.Course;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

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
    
    /**
     * Create a new course
     */
    public Course createCourse(String courseId, String courseName, int credits, 
                               String instructor, int maxStudents) {
        if (courses.containsKey(courseId)) {
            throw new CourseAlreadyExistsException("Course already exists: " + courseId);
        }
        
        Course course = new Course(courseId, courseName, credits, instructor, maxStudents);
        courses.put(courseId, course);
        return course;
    }
    
    /**
     * Get course by ID
     */
    public Course getCourseById(String courseId) {
        Course course = courses.get(courseId);
        if (course == null) {
            throw new CourseNotFoundException("Course not found: " + courseId);
        }
        return course;
    }
    
    /**
     * Get all courses
     */
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }
    
    /**
     * Enroll student in course
     */
    public boolean enrollStudent(String courseId) {
        Course course = getCourseById(courseId);
        return course.enrollStudent();
    }
    
    /**
     * Unenroll student from course
     */
    public boolean unenrollStudent(String courseId) {
        Course course = getCourseById(courseId);
        return course.unenrollStudent();
    }
    
    /**
     * Get available courses (not full)
     */
    public List<Course> getAvailableCourses() {
        return courses.values().stream()
                .filter(c -> !c.isFull())
                .toList();
    }
    
    /**
     * Update course instructor
     */
    public void updateInstructor(String courseId, String newInstructor) {
        Course course = getCourseById(courseId);
        course.setInstructor(newInstructor);
    }
    
    /**
     * Delete course
     */
    public boolean deleteCourse(String courseId) {
        return courses.remove(courseId) != null;
    }
    
    /**
     * Get total number of courses
     */
    public int getTotalCourses() {
        return courses.size();
    }

    public CourseDTO createCourseDTO(CourseDTO dto) {
        Course created = createCourse(
                dto.getId(),
                dto.getCourseName(),
                dto.getCredits(),
                dto.getInstructor(),
                dto.getMaxStudents()
        );
        return toDTO(created);
    }

    public List<CourseDTO> getAllCoursesDTO() {
        return getAllCourses().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CourseDTO getCourseByIdDTO(String courseId) {
        return toDTO(getCourseById(courseId));
    }

    public CourseDTO updateCourse(String courseId, CourseDTO dto) {
        Course existing = getCourseById(courseId);
        existing.setCourseName(dto.getCourseName());
        existing.setCredits(dto.getCredits());
        existing.setInstructor(dto.getInstructor());
        existing.setMaxStudents(dto.getMaxStudents());
        return toDTO(existing);
    }

    public void deleteCourseOrThrow(String courseId) {
        if (!deleteCourse(courseId)) {
            throw new CourseNotFoundException("Course not found: " + courseId);
        }
    }

    public CourseDTO enrollStudentDTO(String courseId) {
        Course course = getCourseById(courseId);
        if (course.isFull()) {
            throw new CourseFullException(
                    "Course '" + courseId + "' is full (" +
                            course.getMaxStudents() + "/" + course.getMaxStudents() + " seats taken)");
        }
        course.enrollStudent();
        return toDTO(course);
    }

    public CourseDTO unenrollStudentDTO(String courseId) {
        Course course = getCourseById(courseId);
        if (course.getEnrolledStudents() <= 0) {
            throw new NoStudentsEnrolledException(
                    "Course '" + courseId + "' has no enrolled students to remove");
        }
        course.unenrollStudent();
        return toDTO(course);
    }

    public List<CourseDTO> getAvailableCoursesDTO() {
        return getAvailableCourses().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<CourseDTO> getCoursesByInstructor(String instructorName) {
        return courses.values().stream()
                .filter(c -> c.getInstructor().equalsIgnoreCase(instructorName))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<CourseDTO> searchCourses(String name, Integer minCredits) {
        return courses.values().stream()
                .filter(c -> name == null ||
                        c.getCourseName().toLowerCase().contains(name.toLowerCase()))
                .filter(c -> minCredits == null || c.getCredits() >= minCredits)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    CourseDTO toDTO(Course course) {
        return new CourseDTO(
                course.getCourseId(),
                course.getCourseName(),
                course.getCredits(),
                course.getInstructor(),
                course.getMaxStudents(),
                course.getEnrolledStudents()
        );
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

    public static class CourseFullException extends RuntimeException {
        public CourseFullException(String message) { super(message); }
    }

    public static class NoStudentsEnrolledException extends RuntimeException {
        public NoStudentsEnrolledException(String message) { super(message); }
    }

}
