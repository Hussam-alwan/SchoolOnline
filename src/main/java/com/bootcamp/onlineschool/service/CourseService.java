package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.dto.CourseDTO;
import com.bootcamp.onlineschool.entity.Course;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.exception.ValidationException;
import com.bootcamp.onlineschool.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing Course entities
 * Provides CRUD operations and business logic validation
 */
@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Create a new course
     * @param courseDTO the course data
     * @return the created course
     * @throws ValidationException if validation fails
     */
    public CourseDTO create(CourseDTO courseDTO) {
        validateCourseForCreation(courseDTO);
        
        Course course = courseDTO.toEntity();
        Course savedCourse = courseRepository.save(course);
        return CourseDTO.fromEntity(savedCourse);
    }

    /**
     * Retrieve all courses
     * @return List of all courses as DTOs
     */
    @Transactional(readOnly = true)
    public List<CourseDTO> findAll() {
        return courseRepository.findAll().stream()
                .map(CourseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find course by ID
     * @param id the course ID
     * @return the course DTO if found
     * @throws ResourceNotFoundException if course not found
     */
    @Transactional(readOnly = true)
    public CourseDTO findById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        return CourseDTO.fromEntity(course);
    }

    /**
     * Find course by name
     * @param name the course name
     * @return Optional containing the course DTO if found
     */
    @Transactional(readOnly = true)
    public Optional<CourseDTO> findByName(String name) {
        validateCourseName(name);
        return courseRepository.findByName(name)
                .map(CourseDTO::fromEntity);
    }

    /**
     * Find courses by credits
     * @param credits the number of credits
     * @return List of courses with the specified credits
     */
    @Transactional(readOnly = true)
    public List<CourseDTO> findByCredits(Integer credits) {
        validateCredits(credits);
        return courseRepository.findByCredits(credits).stream()
                .map(CourseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find courses by duration
     * @param duration the duration in hours
     * @return List of courses with the specified duration
     */
    @Transactional(readOnly = true)
    public List<CourseDTO> findByDuration(Integer duration) {
        validateDuration(duration);
        return courseRepository.findByDuration(duration).stream()
                .map(CourseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find courses by credits OR duration
     * @param credits the number of credits
     * @param duration the duration in hours
     * @return List of courses matching either criteria
     */
    @Transactional(readOnly = true)
    public List<CourseDTO> findByCreditsOrDuration(Integer credits, Integer duration) {
        if (credits != null) {
            validateCredits(credits);
        }
        if (duration != null) {
            validateDuration(duration);
        }
        if (credits == null && duration == null) {
            throw new ValidationException("At least one of credits or duration must be provided");
        }
        
        return courseRepository.findCoursesByCreditsOrDuration(credits, duration).stream()
                .map(CourseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find courses by minimum credits
     * @param minCredits the minimum number of credits
     * @return List of courses with at least the specified credits
     */
    @Transactional(readOnly = true)
    public List<CourseDTO> findByMinCredits(Integer minCredits) {
        validateCredits(minCredits);
        return courseRepository.findByCreditsGreaterThanEqual(minCredits).stream()
                .map(CourseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Find courses by maximum duration
     * @param maxDuration the maximum duration
     * @return List of courses with duration less than or equal to the specified value
     */
    @Transactional(readOnly = true)
    public List<CourseDTO> findByMaxDuration(Integer maxDuration) {
        validateDuration(maxDuration);
        return courseRepository.findByDurationLessThanEqual(maxDuration).stream()
                .map(CourseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Update an existing course
     * @param id the course ID
     * @param courseDTO the updated course data
     * @return the updated course DTO
     * @throws ResourceNotFoundException if course not found
     * @throws ValidationException if validation fails
     */
    public CourseDTO update(Long id, CourseDTO courseDTO) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        
        validateCourseForUpdate(courseDTO, existingCourse);
        
        // Update fields
        existingCourse.setName(courseDTO.getName());
        existingCourse.setDescription(courseDTO.getDescription());
        existingCourse.setCredits(courseDTO.getCredits());
        existingCourse.setDuration(courseDTO.getDuration());
        
        Course savedCourse = courseRepository.save(existingCourse);
        return CourseDTO.fromEntity(savedCourse);
    }

    /**
     * Delete course by ID
     * @param id the course ID
     * @throws ResourceNotFoundException if course not found
     * @throws ValidationException if course has registrations
     */
    public void deleteById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        
        // Check if course has registrations
        if (!course.getRegistrations().isEmpty()) {
            throw new ValidationException("Cannot delete course with existing registrations. Please remove registrations first.");
        }
        
        courseRepository.delete(course);
    }

    /**
     * Check if course exists by name
     * @param name the course name
     * @return true if course exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        validateCourseName(name);
        return courseRepository.existsByName(name);
    }

    // Private validation methods

    private void validateCourseForCreation(CourseDTO courseDTO) {
        if (courseDTO == null) {
            throw new ValidationException("Course data cannot be null");
        }

        validateRequiredFields(courseDTO);

        // Check for duplicate course name
        if (courseRepository.existsByName(courseDTO.getName())) {
            throw new ValidationException("Course name already exists: " + courseDTO.getName());
        }
    }

    private void validateCourseForUpdate(CourseDTO courseDTO, Course existingCourse) {
        if (courseDTO == null) {
            throw new ValidationException("Course data cannot be null");
        }

        validateRequiredFields(courseDTO);

        // Check if name is being changed to an existing one
        if (!existingCourse.getName().equals(courseDTO.getName())) {
            if (courseRepository.existsByName(courseDTO.getName())) {
                throw new ValidationException("Course name already exists: " + courseDTO.getName());
            }
        }
    }

    private void validateRequiredFields(CourseDTO courseDTO) {
        if (courseDTO.getName() == null || courseDTO.getName().trim().isEmpty()) {
            throw new ValidationException("Course name is required");
        }

        if (courseDTO.getCredits() == null) {
            throw new ValidationException("Credits is required");
        }

        if (courseDTO.getDuration() == null) {
            throw new ValidationException("Duration is required");
        }

        validateCredits(courseDTO.getCredits());
        validateDuration(courseDTO.getDuration());
    }

    private void validateCourseName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Course name cannot be null or empty");
        }
    }

    private void validateCredits(Integer credits) {
        if (credits == null) {
            throw new ValidationException("Credits cannot be null");
        }
        if (credits < 1) {
            throw new ValidationException("Credits must be at least 1");
        }
    }

    private void validateDuration(Integer duration) {
        if (duration == null) {
            throw new ValidationException("Duration cannot be null");
        }
        if (duration < 1) {
            throw new ValidationException("Duration must be at least 1");
        }
    }
}