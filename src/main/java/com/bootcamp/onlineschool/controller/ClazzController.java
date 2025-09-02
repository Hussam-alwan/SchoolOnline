package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.dto.ClazzDTO;
import com.bootcamp.onlineschool.service.ClazzService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Clazz entity operations
 * Provides CRUD endpoints and relationship management functionality
 */
@RestController
@RequestMapping("/api/classes")
@Tag(name = "Class Management", description = "APIs for managing classes and their relationships with students, teachers, and courses")
public class ClazzController {

    private final ClazzService clazzService;

    public ClazzController(ClazzService clazzService) {
        this.clazzService = clazzService;
    }

    /**
     * Create a new class
     * @param clazzDTO the class data
     * @return the created class
     */
    @PostMapping
    public ResponseEntity<ClazzDTO> createClass(@Valid @RequestBody ClazzDTO clazzDTO) {
        ClazzDTO createdClass = clazzService.create(clazzDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClass);
    }

    /**
     * Get all classes
     * @return List of all classes
     */
    @GetMapping
    public ResponseEntity<List<ClazzDTO>> getAllClasses() {
        List<ClazzDTO> classes = clazzService.findAll();
        return ResponseEntity.ok(classes);
    }

    /**
     * Get class by ID
     * @param id the class ID
     * @return the class if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClazzDTO> getClassById(@PathVariable Long id) {
        ClazzDTO clazz = clazzService.findById(id);
        return ResponseEntity.ok(clazz);
    }

    /**
     * Get class by name
     * @param name the class name
     * @return the class if found
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<ClazzDTO> getClassByName(@PathVariable String name) {
        Optional<ClazzDTO> clazz = clazzService.findByName(name);
        return clazz.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get classes by semester and year
     * @param semester the semester
     * @param year the year
     * @return List of classes in the specified semester and year
     */
    @GetMapping("/semester/{semester}/year/{year}")
    public ResponseEntity<List<ClazzDTO>> getClassesBySemesterAndYear(
            @PathVariable String semester, 
            @PathVariable Integer year) {
        List<ClazzDTO> classes = clazzService.findBySemesterAndYear(semester, year);
        return ResponseEntity.ok(classes);
    }

    /**
     * Get classes by teacher ID
     * @param teacherId the teacher ID
     * @return List of classes taught by the teacher
     */
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<ClazzDTO>> getClassesByTeacherId(@PathVariable Long teacherId) {
        List<ClazzDTO> classes = clazzService.findByTeacherId(teacherId);
        return ResponseEntity.ok(classes);
    }

    /**
     * Update an existing class
     * @param id the class ID
     * @param clazzDTO the updated class data
     * @return the updated class
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClazzDTO> updateClass(@PathVariable Long id, @Valid @RequestBody ClazzDTO clazzDTO) {
        ClazzDTO updatedClass = clazzService.update(id, clazzDTO);
        return ResponseEntity.ok(updatedClass);
    }

    /**
     * Delete class by ID
     * @param id the class ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        clazzService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Add student to class
     * @param classId the class ID
     * @param studentId the student ID
     * @return the updated class
     */
    @PostMapping("/{classId}/students/{studentId}")
    public ResponseEntity<ClazzDTO> addStudentToClass(
            @PathVariable Long classId, 
            @PathVariable Long studentId) {
        ClazzDTO updatedClass = clazzService.addStudent(classId, studentId);
        return ResponseEntity.ok(updatedClass);
    }

    /**
     * Remove student from class
     * @param classId the class ID
     * @param studentId the student ID
     * @return the updated class
     */
    @DeleteMapping("/{classId}/students/{studentId}")
    public ResponseEntity<ClazzDTO> removeStudentFromClass(
            @PathVariable Long classId, 
            @PathVariable Long studentId) {
        ClazzDTO updatedClass = clazzService.removeStudent(classId, studentId);
        return ResponseEntity.ok(updatedClass);
    }

    /**
     * Add course to class
     * @param classId the class ID
     * @param courseId the course ID
     * @return the updated class
     */
    @PostMapping("/{classId}/courses/{courseId}")
    public ResponseEntity<ClazzDTO> addCourseToClass(
            @PathVariable Long classId, 
            @PathVariable Long courseId) {
        ClazzDTO updatedClass = clazzService.addCourse(classId, courseId);
        return ResponseEntity.ok(updatedClass);
    }

    /**
     * Remove course from class
     * @param classId the class ID
     * @param courseId the course ID
     * @return the updated class
     */
    @DeleteMapping("/{classId}/courses/{courseId}")
    public ResponseEntity<ClazzDTO> removeCourseFromClass(
            @PathVariable Long classId, 
            @PathVariable Long courseId) {
        ClazzDTO updatedClass = clazzService.removeCourse(classId, courseId);
        return ResponseEntity.ok(updatedClass);
    }

    /**
     * Check if class exists by name
     * @param name the class name
     * @return true if class exists, false otherwise
     */
    @GetMapping("/exists/name/{name}")
    public ResponseEntity<Boolean> existsByName(@PathVariable String name) {
        boolean exists = clazzService.existsByName(name);
        return ResponseEntity.ok(exists);
    }
}