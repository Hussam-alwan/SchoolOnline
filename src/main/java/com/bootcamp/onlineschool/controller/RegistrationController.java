package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.dto.RegistrationDTO;
import com.bootcamp.onlineschool.service.RegistrationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Registration entity operations
 * Provides CRUD endpoints and student registration management functionality
 */
@RestController
@RequestMapping("/api/registrations")
@Tag(name = "Registration Management", description = "APIs for managing student course registrations and grades")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * Create a new registration
     * @param registrationDTO the registration data
     * @return the created registration
     */
    @PostMapping
    public ResponseEntity<RegistrationDTO> createRegistration(@Valid @RequestBody RegistrationDTO registrationDTO) {
        RegistrationDTO createdRegistration = registrationService.create(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRegistration);
    }

    /**
     * Register a student for a course
     * @param studentId the student ID
     * @param courseId the course ID
     * @return the created registration
     */
    @PostMapping("/register/student/{studentId}/course/{courseId}")
    public ResponseEntity<RegistrationDTO> registerStudentForCourse(
            @PathVariable Long studentId, 
            @PathVariable Long courseId) {
        RegistrationDTO registration = registrationService.registerStudentForCourse(studentId, courseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(registration);
    }

    /**
     * Get all registrations
     * @return List of all registrations
     */
    @GetMapping
    public ResponseEntity<List<RegistrationDTO>> getAllRegistrations() {
        List<RegistrationDTO> registrations = registrationService.findAll();
        return ResponseEntity.ok(registrations);
    }

    /**
     * Get registration by ID
     * @param id the registration ID
     * @return the registration if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<RegistrationDTO> getRegistrationById(@PathVariable Long id) {
        RegistrationDTO registration = registrationService.findById(id);
        return ResponseEntity.ok(registration);
    }

    /**
     * Get registrations by student ID
     * @param studentId the student ID
     * @return List of registrations for the student
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<RegistrationDTO>> getRegistrationsByStudentId(@PathVariable Long studentId) {
        List<RegistrationDTO> registrations = registrationService.findByStudentId(studentId);
        return ResponseEntity.ok(registrations);
    }

    /**
     * Get registrations by course ID
     * @param courseId the course ID
     * @return List of registrations for the course
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<RegistrationDTO>> getRegistrationsByCourseId(@PathVariable Long courseId) {
        List<RegistrationDTO> registrations = registrationService.findByCourseId(courseId);
        return ResponseEntity.ok(registrations);
    }

    /**
     * Get registrations by status
     * @param status the registration status
     * @return List of registrations with the specified status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<RegistrationDTO>> getRegistrationsByStatus(@PathVariable String status) {
        List<RegistrationDTO> registrations = registrationService.findByStatus(status);
        return ResponseEntity.ok(registrations);
    }

    /**
     * Get registration by student and course
     * @param studentId the student ID
     * @param courseId the course ID
     * @return the registration if found
     */
    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<RegistrationDTO> getRegistrationByStudentAndCourse(
            @PathVariable Long studentId, 
            @PathVariable Long courseId) {
        Optional<RegistrationDTO> registration = registrationService.findByStudentAndCourse(studentId, courseId);
        return registration.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get registrations with grades
     * @return List of registrations that have grades assigned
     */
    @GetMapping("/with-grades")
    public ResponseEntity<List<RegistrationDTO>> getRegistrationsWithGrades() {
        List<RegistrationDTO> registrations = registrationService.findRegistrationsWithGrades();
        return ResponseEntity.ok(registrations);
    }

    /**
     * Get registrations without grades
     * @return List of registrations that don't have grades assigned
     */
    @GetMapping("/without-grades")
    public ResponseEntity<List<RegistrationDTO>> getRegistrationsWithoutGrades() {
        List<RegistrationDTO> registrations = registrationService.findRegistrationsWithoutGrades();
        return ResponseEntity.ok(registrations);
    }

    /**
     * Update an existing registration
     * @param id the registration ID
     * @param registrationDTO the updated registration data
     * @return the updated registration
     */
    @PutMapping("/{id}")
    public ResponseEntity<RegistrationDTO> updateRegistration(
            @PathVariable Long id, 
            @Valid @RequestBody RegistrationDTO registrationDTO) {
        RegistrationDTO updatedRegistration = registrationService.update(id, registrationDTO);
        return ResponseEntity.ok(updatedRegistration);
    }

    /**
     * Update registration status
     * @param id the registration ID
     * @param status the new status
     * @return the updated registration
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<RegistrationDTO> updateRegistrationStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        RegistrationDTO updatedRegistration = registrationService.updateStatus(id, status);
        return ResponseEntity.ok(updatedRegistration);
    }

    /**
     * Update registration grade
     * @param id the registration ID
     * @param grade the new grade
     * @return the updated registration
     */
    @PatchMapping("/{id}/grade")
    public ResponseEntity<RegistrationDTO> updateRegistrationGrade(
            @PathVariable Long id, 
            @RequestParam String grade) {
        RegistrationDTO updatedRegistration = registrationService.updateGrade(id, grade);
        return ResponseEntity.ok(updatedRegistration);
    }

    /**
     * Delete registration by ID
     * @param id the registration ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {
        registrationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Drop a student from a course (update status to DROPPED)
     * @param studentId the student ID
     * @param courseId the course ID
     * @return the updated registration
     */
    @PatchMapping("/drop/student/{studentId}/course/{courseId}")
    public ResponseEntity<RegistrationDTO> dropStudentFromCourse(
            @PathVariable Long studentId, 
            @PathVariable Long courseId) {
        RegistrationDTO updatedRegistration = registrationService.dropStudentFromCourse(studentId, courseId);
        return ResponseEntity.ok(updatedRegistration);
    }
}