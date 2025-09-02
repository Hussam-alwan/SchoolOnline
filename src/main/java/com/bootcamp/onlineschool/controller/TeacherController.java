package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.dto.TeacherDTO;
import com.bootcamp.onlineschool.service.TeacherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Teacher entity operations
 * Provides CRUD endpoints and class management
 */
@RestController
@RequestMapping("/api/teachers")
@Tag(name = "Teacher Management", description = "APIs for managing teachers and their class assignments")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    /**
     * Create a new teacher
     * @param teacherDTO the teacher data
     * @return the created teacher
     */
    @PostMapping
    public ResponseEntity<TeacherDTO> createTeacher(@Valid @RequestBody TeacherDTO teacherDTO) {
        TeacherDTO createdTeacher = teacherService.create(teacherDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeacher);
    }

    /**
     * Get all teachers
     * @return List of all teachers
     */
    @GetMapping
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        List<TeacherDTO> teachers = teacherService.findAll();
        return ResponseEntity.ok(teachers);
    }

    /**
     * Get teacher by ID
     * @param id the teacher ID
     * @return the teacher if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable Long id) {
        TeacherDTO teacher = teacherService.findById(id);
        return ResponseEntity.ok(teacher);
    }

    /**
     * Get teacher by employee ID
     * @param employeeId the employee ID
     * @return the teacher if found
     */
    @GetMapping("/employeeId/{employeeId}")
    public ResponseEntity<TeacherDTO> getTeacherByEmployeeId(@PathVariable String employeeId) {
        Optional<TeacherDTO> teacher = teacherService.findByEmployeeId(employeeId);
        return teacher.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get teacher by email
     * @param email the email address
     * @return the teacher if found
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<TeacherDTO> getTeacherByEmail(@PathVariable String email) {
        Optional<TeacherDTO> teacher = teacherService.findByEmail(email);
        return teacher.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get teachers by department
     * @param department the department name
     * @return List of teachers in the department
     */
    @GetMapping("/department/{department}")
    public ResponseEntity<List<TeacherDTO>> getTeachersByDepartment(@PathVariable String department) {
        List<TeacherDTO> teachers = teacherService.findByDepartment(department);
        return ResponseEntity.ok(teachers);
    }

    /**
     * Get teachers by department (case-insensitive)
     * @param department the department name
     * @return List of teachers in the department
     */
    @GetMapping("/department/{department}/ignoreCase")
    public ResponseEntity<List<TeacherDTO>> getTeachersByDepartmentIgnoreCase(@PathVariable String department) {
        List<TeacherDTO> teachers = teacherService.findByDepartmentIgnoreCase(department);
        return ResponseEntity.ok(teachers);
    }

    /**
     * Update an existing teacher
     * @param id the teacher ID
     * @param teacherDTO the updated teacher data
     * @return the updated teacher
     */
    @PutMapping("/{id}")
    public ResponseEntity<TeacherDTO> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherDTO teacherDTO) {
        TeacherDTO updatedTeacher = teacherService.update(id, teacherDTO);
        return ResponseEntity.ok(updatedTeacher);
    }

    /**
     * Delete teacher by ID
     * @param id the teacher ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Assign teacher to a class
     * @param teacherId the teacher ID
     * @param clazzId the class ID
     * @return the updated teacher
     */
    @PostMapping("/{teacherId}/assign/{clazzId}")
    public ResponseEntity<TeacherDTO> assignToClass(@PathVariable Long teacherId, @PathVariable Long clazzId) {
        TeacherDTO updatedTeacher = teacherService.assignToClass(teacherId, clazzId);
        return ResponseEntity.ok(updatedTeacher);
    }

    /**
     * Remove teacher from a class
     * @param teacherId the teacher ID
     * @param clazzId the class ID
     * @return the updated teacher
     */
    @DeleteMapping("/{teacherId}/assign/{clazzId}")
    public ResponseEntity<TeacherDTO> removeFromClass(@PathVariable Long teacherId, @PathVariable Long clazzId) {
        TeacherDTO updatedTeacher = teacherService.removeFromClass(teacherId, clazzId);
        return ResponseEntity.ok(updatedTeacher);
    }

    /**
     * Check if teacher exists by employee ID
     * @param employeeId the employee ID
     * @return true if teacher exists, false otherwise
     */
    @GetMapping("/exists/employeeId/{employeeId}")
    public ResponseEntity<Boolean> existsByEmployeeId(@PathVariable String employeeId) {
        boolean exists = teacherService.existsByEmployeeId(employeeId);
        return ResponseEntity.ok(exists);
    }
}