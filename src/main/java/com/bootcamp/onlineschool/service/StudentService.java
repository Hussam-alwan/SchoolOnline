package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.StudentRegistry;
import com.bootcamp.onlineschool.config.SchoolProperties;
import com.bootcamp.onlineschool.model.Student;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * StudentService demonstrates Spring Boot service layer
 * 
 * Demonstrates:
 * - @Service annotation for dependency injection
 * - Business logic encapsulation
 * - Service layer pattern
 * - Dependency injection
 */
@Service
public class StudentService {
    
    private final StudentRegistry studentRegistry;
    private final SchoolProperties schoolProperties;
    
    /**
     * Constructor injection - Spring automatically injects StudentRegistry
     */
    public StudentService(StudentRegistry studentRegistry, SchoolProperties schoolProperties) {
        this.studentRegistry = studentRegistry;
        this.schoolProperties = schoolProperties;
    }

    /**
     * Gets high achievers - result is cached
     */
    @Cacheable(value = "students", key = "'highAchievers'")
    public List<Student> getHighAchievers() {
        double threshold = schoolProperties.getDefaultGpaThreshold();
        return studentRegistry.getStudentsWithHighGpa(threshold);
    }
    /**
     * Add a new student
     */
    @CacheEvict(value = "students", allEntries = true)
    public void addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        studentRegistry.addStudent(student);
    }
    
    /**
     * Get all students
     */
    @Cacheable(value = "students", key = "'allStudents'")
    public List<Student> getAllStudents() {
        return studentRegistry.getAllStudentsSortedByName();
    }
    
    /**
     * Find student by ID
     */
    @Cacheable(value = "students", key = "#studentId")
    public Student findStudentById(String studentId) {
        Student student = studentRegistry.findStudentById(studentId);
        if (student == null) {
            throw new StudentNotFoundException("Student not found: " + studentId);
        }
        return student;
    }
    
    /**
     * Find students by name
     */
    @Cacheable(value = "students", key = "'name_' + #name")
    public List<Student> findStudentsByName(String name) {
        return studentRegistry.findStudentsByName(name);
    }
    
    /**
     * Get students with high GPA
     */
    @Cacheable(value = "students", key = "'gpa_' + #gpaThreshold")
    public List<Student> getHighAchievers(double gpaThreshold) {
        return studentRegistry.getStudentsWithHighGpa(gpaThreshold);
    }
    
    /**
     * Remove a student
     */
    @CacheEvict(value = "students", allEntries = true)
    public boolean removeStudent(String studentId) {
        return studentRegistry.removeStudent(studentId);
    }
    
    /**
     * Get total number of students
     */
    @Cacheable(value = "students", key = "'totalCount'")
    public int getTotalStudents() {
        return studentRegistry.getStudentCount();
    }
    
    /**
     * Get average GPA
     */
    @Cacheable(value = "students", key = "'averageGpa'")
    public double getAverageGpa() {
        return studentRegistry.getAverageGpa();
    }
    
    /**
     * Custom exception for student not found
     */
    public static class StudentNotFoundException extends RuntimeException {
        public StudentNotFoundException(String message) {
            super(message);
        }
    }
}
