package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.dto.StudentDTO;
import com.bootcamp.onlineschool.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Student entity operations
 * Provides CRUD endpoints and class enrollment management
 */
@RestController
@RequestMapping("/api/students")
@Tag(name = "Student Management", description = "APIs for managing students and their class enrollments")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Create a new student
     * @param studentDTO the student data
     * @return the created student
     */
    @Operation(summary = "Create a new student", description = "Creates a new student in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Student created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        StudentDTO createdStudent = studentService.create(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    /**
     * Get all students
     * @return List of all students
     */
    @Operation(summary = "Get all students", description = "Retrieves a list of all students in the system")
    @ApiResponse(responseCode = "200", description = "Students retrieved successfully")
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentService.findAll();
        return ResponseEntity.ok(students);
    }

    /**
     * Get student by ID
     * @param id the student ID
     * @return the student if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        StudentDTO student = studentService.findById(id);
        return ResponseEntity.ok(student);
    }

    /**
     * Get student by student ID
     * @param studentId the student ID
     * @return the student if found
     */
    @GetMapping("/studentId/{studentId}")
    public ResponseEntity<StudentDTO> getStudentByStudentId(@PathVariable String studentId) {
        Optional<StudentDTO> student = studentService.findByStudentId(studentId);
        return student.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get student by email
     * @param email the email address
     * @return the student if found
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<StudentDTO> getStudentByEmail(@PathVariable String email) {
        Optional<StudentDTO> student = studentService.findByEmail(email);
        return student.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get students by class ID
     * @param clazzId the class ID
     * @return List of students enrolled in the class
     */
    @GetMapping("/class/{clazzId}")
    public ResponseEntity<List<StudentDTO>> getStudentsByClassId(@PathVariable Long clazzId) {
        List<StudentDTO> students = studentService.findStudentsByClazzId(clazzId);
        return ResponseEntity.ok(students);
    }

    /**
     * Update an existing student
     * @param id the student ID
     * @param studentDTO the updated student data
     * @return the updated student
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDTO studentDTO) {
        StudentDTO updatedStudent = studentService.update(id, studentDTO);
        return ResponseEntity.ok(updatedStudent);
    }

    /**
     * Delete student by ID
     * @param id the student ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Enroll student in a class
     * @param studentId the student ID
     * @param clazzId the class ID
     * @return the updated student
     */
    @Operation(summary = "Enroll student in class", description = "Enrolls a student in a specific class")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student enrolled successfully"),
        @ApiResponse(responseCode = "404", description = "Student or class not found"),
        @ApiResponse(responseCode = "400", description = "Enrollment failed due to business rules")
    })
    @PostMapping("/{studentId}/enroll/{clazzId}")
    public ResponseEntity<StudentDTO> enrollInClass(
        @Parameter(description = "Student ID") @PathVariable Long studentId, 
        @Parameter(description = "Class ID") @PathVariable Long clazzId) {
        StudentDTO updatedStudent = studentService.enrollInClass(studentId, clazzId);
        return ResponseEntity.ok(updatedStudent);
    }

    /**
     * Remove student from a class
     * @param studentId the student ID
     * @param clazzId the class ID
     * @return the updated student
     */
    @DeleteMapping("/{studentId}/enroll/{clazzId}")
    public ResponseEntity<StudentDTO> removeFromClass(@PathVariable Long studentId, @PathVariable Long clazzId) {
        StudentDTO updatedStudent = studentService.removeFromClass(studentId, clazzId);
        return ResponseEntity.ok(updatedStudent);
    }

    /**
     * Check if student exists by student ID
     * @param studentId the student ID
     * @return true if student exists, false otherwise
     */
    @GetMapping("/exists/studentId/{studentId}")
    public ResponseEntity<Boolean> existsByStudentId(@PathVariable String studentId) {
        boolean exists = studentService.existsByStudentId(studentId);
        return ResponseEntity.ok(exists);
    }
}