package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.dto.RegistrationDTO;
import com.bootcamp.onlineschool.entity.Registration;
import com.bootcamp.onlineschool.entity.Student;
import com.bootcamp.onlineschool.entity.Course;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.exception.ValidationException;
import com.bootcamp.onlineschool.repository.RegistrationRepository;
import com.bootcamp.onlineschool.repository.StudentRepository;
import com.bootcamp.onlineschool.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing Registration entities
 * Provides CRUD operations, student-course registration logic, and business logic validation
 */
@Service
@Transactional
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    // Registration status constants
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_DROPPED = "DROPPED";
    public static final String STATUS_PENDING = "PENDING";

    @Autowired
    public RegistrationService(RegistrationRepository registrationRepository,
                              StudentRepository studentRepository,
                              CourseRepository courseRepository) {
        this.registrationRepository = registrationRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Create a new registration
     * @param registrationDTO the registration data
     * @return the created registration
     * @throws ValidationException if validation fails
     */
    public RegistrationDTO create(RegistrationDTO registrationDTO) {
        validateRegistrationForCreation(registrationDTO);
        
        // Get student and course
        Student student = studentRepository.findById(registrationDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", registrationDTO.getStudentId()));
        
        Course course = courseRepository.findById(registrationDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", registrationDTO.getCourseId()));
        
        // Check for duplicate registration
        if (registrationRepository.existsByStudentAndCourse(student, course)) {
            throw new ValidationException("Student is already registered for this course");
        }
        
        Registration registration = registrationDTO.toEntity();
        registration.setStudent(student);
        registration.setCourse(course);
        
        Registration savedRegistration = registrationRepository.save(registration);
        return RegistrationDTO.fromEntity(savedRegistration);
    }

    /**
     * Register a student for a course
     * @param studentId the student ID
     * @param courseId the course ID
     * @return the created registration
     * @throws ValidationException if validation fails
     */
    public RegistrationDTO registerStudentForCourse(Long studentId, Long courseId) {
        if (studentId == null) {
            throw new ValidationException("Student ID cannot be null");
        }
        if (courseId == null) {
            throw new ValidationException("Course ID cannot be null");
        }
        
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
        
        // Check for duplicate registration
        if (registrationRepository.existsByStudentAndCourse(student, course)) {
            throw new ValidationException("Student is already registered for this course");
        }
        
        Registration registration = new Registration(LocalDate.now(), STATUS_ACTIVE, student, course);
        Registration savedRegistration = registrationRepository.save(registration);
        return RegistrationDTO.fromEntity(savedRegistration);
    }

    /**
     * Retrieve all registrations
     * @return List of all registrations as DTOs
     */
    @Transactional(readOnly = true)
    public List<RegistrationDTO> findAll() {
        return registrationRepository.findAll().stream()
                .map(RegistrationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find registration by ID
     * @param id the registration ID
     * @return the registration DTO if found
     * @throws ResourceNotFoundException if registration not found
     */
    @Transactional(readOnly = true)
    public RegistrationDTO findById(Long id) {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registration", "id", id));
        return RegistrationDTO.fromEntity(registration);
    }

    /**
     * Find registrations by student ID
     * @param studentId the student ID
     * @return List of registrations for the student
     */
    @Transactional(readOnly = true)
    public List<RegistrationDTO> findByStudentId(Long studentId) {
        if (studentId == null) {
            throw new ValidationException("Student ID cannot be null");
        }
        return registrationRepository.findByStudentId(studentId).stream()
                .map(RegistrationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find registrations by course ID
     * @param courseId the course ID
     * @return List of registrations for the course
     */
    @Transactional(readOnly = true)
    public List<RegistrationDTO> findByCourseId(Long courseId) {
        if (courseId == null) {
            throw new ValidationException("Course ID cannot be null");
        }
        return registrationRepository.findByCourseId(courseId).stream()
                .map(RegistrationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find registrations by status
     * @param status the registration status
     * @return List of registrations with the specified status
     */
    @Transactional(readOnly = true)
    public List<RegistrationDTO> findByStatus(String status) {
        validateStatus(status);
        return registrationRepository.findByStatus(status).stream()
                .map(RegistrationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find registration by student and course
     * @param studentId the student ID
     * @param courseId the course ID
     * @return Optional containing the registration if found
     */
    @Transactional(readOnly = true)
    public Optional<RegistrationDTO> findByStudentAndCourse(Long studentId, Long courseId) {
        if (studentId == null) {
            throw new ValidationException("Student ID cannot be null");
        }
        if (courseId == null) {
            throw new ValidationException("Course ID cannot be null");
        }
        return registrationRepository.findByStudentIdAndCourseId(studentId, courseId)
                .map(RegistrationDTO::fromEntity);
    }

    /**
     * Find registrations with grades
     * @return List of registrations that have grades assigned
     */
    @Transactional(readOnly = true)
    public List<RegistrationDTO> findRegistrationsWithGrades() {
        return registrationRepository.findRegistrationsWithGrades().stream()
                .map(RegistrationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find registrations without grades
     * @return List of registrations that don't have grades assigned
     */
    @Transactional(readOnly = true)
    public List<RegistrationDTO> findRegistrationsWithoutGrades() {
        return registrationRepository.findRegistrationsWithoutGrades().stream()
                .map(RegistrationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Update an existing registration
     * @param id the registration ID
     * @param registrationDTO the updated registration data
     * @return the updated registration DTO
     * @throws ResourceNotFoundException if registration not found
     * @throws ValidationException if validation fails
     */
    public RegistrationDTO update(Long id, RegistrationDTO registrationDTO) {
        Registration existingRegistration = registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registration", "id", id));
        
        validateRegistrationForUpdate(registrationDTO, existingRegistration);
        
        // Update fields
        existingRegistration.setRegistrationDate(registrationDTO.getRegistrationDate());
        existingRegistration.setStatus(registrationDTO.getStatus());
        existingRegistration.setGrade(registrationDTO.getGrade());
        
        // Update student and course if changed
        if (!existingRegistration.getStudent().getId().equals(registrationDTO.getStudentId())) {
            Student newStudent = studentRepository.findById(registrationDTO.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student", "id", registrationDTO.getStudentId()));
            existingRegistration.setStudent(newStudent);
        }
        
        if (!existingRegistration.getCourse().getId().equals(registrationDTO.getCourseId())) {
            Course newCourse = courseRepository.findById(registrationDTO.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course", "id", registrationDTO.getCourseId()));
            existingRegistration.setCourse(newCourse);
        }
        
        Registration savedRegistration = registrationRepository.save(existingRegistration);
        return RegistrationDTO.fromEntity(savedRegistration);
    }

    /**
     * Update registration status
     * @param id the registration ID
     * @param status the new status
     * @return the updated registration DTO
     * @throws ResourceNotFoundException if registration not found
     * @throws ValidationException if status is invalid
     */
    public RegistrationDTO updateStatus(Long id, String status) {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registration", "id", id));
        
        validateStatus(status);
        registration.setStatus(status);
        
        Registration savedRegistration = registrationRepository.save(registration);
        return RegistrationDTO.fromEntity(savedRegistration);
    }

    /**
     * Update registration grade
     * @param id the registration ID
     * @param grade the new grade
     * @return the updated registration DTO
     * @throws ResourceNotFoundException if registration not found
     * @throws ValidationException if grade is invalid
     */
    public RegistrationDTO updateGrade(Long id, String grade) {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registration", "id", id));
        
        validateGrade(grade);
        registration.setGrade(grade);
        
        Registration savedRegistration = registrationRepository.save(registration);
        return RegistrationDTO.fromEntity(savedRegistration);
    }

    /**
     * Delete registration by ID
     * @param id the registration ID
     * @throws ResourceNotFoundException if registration not found
     */
    public void deleteById(Long id) {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registration", "id", id));
        
        registrationRepository.delete(registration);
    }

    /**
     * Drop a student from a course (update status to DROPPED)
     * @param studentId the student ID
     * @param courseId the course ID
     * @return the updated registration DTO
     * @throws ResourceNotFoundException if registration not found
     */
    public RegistrationDTO dropStudentFromCourse(Long studentId, Long courseId) {
        Registration registration = registrationRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found for student " + studentId + " and course " + courseId));
        
        registration.setStatus(STATUS_DROPPED);
        Registration savedRegistration = registrationRepository.save(registration);
        return RegistrationDTO.fromEntity(savedRegistration);
    }

    // Private validation methods

    private void validateRegistrationForCreation(RegistrationDTO registrationDTO) {
        if (registrationDTO == null) {
            throw new ValidationException("Registration data cannot be null");
        }

        validateRequiredFields(registrationDTO);
    }

    private void validateRegistrationForUpdate(RegistrationDTO registrationDTO, Registration existingRegistration) {
        if (registrationDTO == null) {
            throw new ValidationException("Registration data cannot be null");
        }

        validateRequiredFields(registrationDTO);

        // Check for duplicate registration if student or course is being changed
        if (!existingRegistration.getStudent().getId().equals(registrationDTO.getStudentId()) ||
            !existingRegistration.getCourse().getId().equals(registrationDTO.getCourseId())) {
            
            Student student = studentRepository.findById(registrationDTO.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student", "id", registrationDTO.getStudentId()));
            
            Course course = courseRepository.findById(registrationDTO.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course", "id", registrationDTO.getCourseId()));
            
            Optional<Registration> existingReg = registrationRepository.findByStudentAndCourse(student, course);
            if (existingReg.isPresent() && !existingReg.get().getId().equals(existingRegistration.getId())) {
                throw new ValidationException("Student is already registered for this course");
            }
        }
    }

    private void validateRequiredFields(RegistrationDTO registrationDTO) {
        if (registrationDTO.getRegistrationDate() == null) {
            throw new ValidationException("Registration date is required");
        }

        if (registrationDTO.getRegistrationDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Registration date cannot be in the future");
        }

        if (registrationDTO.getStatus() == null || registrationDTO.getStatus().trim().isEmpty()) {
            throw new ValidationException("Status is required");
        }

        if (registrationDTO.getStudentId() == null) {
            throw new ValidationException("Student ID is required");
        }

        if (registrationDTO.getCourseId() == null) {
            throw new ValidationException("Course ID is required");
        }

        validateStatus(registrationDTO.getStatus());
        
        if (registrationDTO.getGrade() != null) {
            validateGrade(registrationDTO.getGrade());
        }
    }

    private void validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new ValidationException("Status cannot be null or empty");
        }
        
        String upperStatus = status.toUpperCase();
        if (!upperStatus.equals(STATUS_ACTIVE) && !upperStatus.equals(STATUS_COMPLETED) && 
            !upperStatus.equals(STATUS_DROPPED) && !upperStatus.equals(STATUS_PENDING)) {
            throw new ValidationException("Invalid status. Valid statuses are: ACTIVE, COMPLETED, DROPPED, PENDING");
        }
    }

    private void validateGrade(String grade) {
        if (grade != null && grade.length() > 5) {
            throw new ValidationException("Grade must not exceed 5 characters");
        }
    }
}