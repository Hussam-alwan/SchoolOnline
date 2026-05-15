package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.StudentRegistry;
import com.bootcamp.onlineschool.dto.StudentDTO;
import com.bootcamp.onlineschool.model.Student;
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

    /**
     * Constructor injection - Spring automatically injects StudentRegistry
     */
    public StudentService(StudentRegistry studentRegistry) {
        this.studentRegistry = studentRegistry;
    }

    public StudentDTO addStudent(StudentDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        Student entity = toEntity(dto);
        studentRegistry.addStudent(entity);
        return toDTO(entity);
    }
    
    /**
     * Get all students
     */
    public List<StudentDTO> getAllStudents() {
        return studentRegistry.getAllStudentsSortedByName().stream()
                .map(this::toDTO)
                .toList();
    }
    
    /**
     * Find student by ID
     */
    public StudentDTO getStudentById(String studentId) {
        Student student = studentRegistry.findStudentById(studentId);
        if (student == null) {
            throw new StudentNotFoundException("Student not found: " + studentId);
        }
        return toDTO(student);
    }
    /**
     * Find students by name
     */
    public List<StudentDTO> findStudentsByName(String name) {
        return studentRegistry.findStudentsByName(name).stream()
                .map(this::toDTO)
                .toList();
    }

    public StudentDTO updateStudent(String studentId, StudentDTO dto) {
        Student existing = studentRegistry.findStudentById(studentId);
        if (existing == null) {
            throw new StudentNotFoundException("Student not found: " + studentId);
        }
        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setGpa(dto.getGpa());
        return toDTO(existing);
    }
    
    /**
     * Get students with high GPA
     */
    public void deleteStudent(String studentId) {
        boolean removed = studentRegistry.removeStudent(studentId);
        if (!removed) {
            throw new StudentNotFoundException("Student not found: " + studentId);
        }
    }
    
    /**
     * Remove a student
     */
    public List<StudentDTO> getHighAchievers(double gpaThreshold) {
        return studentRegistry.getStudentsWithHighGpa(gpaThreshold).stream()
                .map(this::toDTO)
                .toList();
    }
    
    /**
     * Get total number of students
     */
    public int getTotalStudents() {
        return studentRegistry.getStudentCount();
    }
    
    /**
     * Get average GPA
     */
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

    private StudentDTO toDTO(Student student) {
        return new StudentDTO(
                student.getStudentId(),
                student.getName(),
                student.getEmail(),
                student.getGpa()
        );
    }

    private Student toEntity(StudentDTO dto) {
        return new Student(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getGpa()
        );
    }
}
