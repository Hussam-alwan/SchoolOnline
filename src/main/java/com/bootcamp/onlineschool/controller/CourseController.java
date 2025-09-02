package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.dto.CourseDTO;
import com.bootcamp.onlineschool.service.CourseService;
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
 * REST Controller for Course entity operations
 * Provides CRUD endpoints and course management functionality
 */
@RestController
@RequestMapping("/api/courses")
@Tag(name = "Course Management", description = "APIs for managing courses in the online school system")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * Create a new course
     * @param courseDTO the course data
     * @return the created course
     */
    @Operation(summary = "Create a new course", description = "Creates a new course in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Course created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO courseDTO) {
        CourseDTO createdCourse = courseService.create(courseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    /**
     * Get all courses
     * @return List of all courses
     */
    @Operation(summary = "Get all courses", description = "Retrieves a list of all courses in the system")
    @ApiResponse(responseCode = "200", description = "Courses retrieved successfully")
    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<CourseDTO> courses = courseService.findAll();
        return ResponseEntity.ok(courses);
    }

    /**
     * Get course by ID
     * @param id the course ID
     * @return the course if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
        CourseDTO course = courseService.findById(id);
        return ResponseEntity.ok(course);
    }

    /**
     * Get course by name
     * @param name the course name
     * @return the course if found
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<CourseDTO> getCourseByName(@PathVariable String name) {
        Optional<CourseDTO> course = courseService.findByName(name);
        return course.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get courses by credits
     * @param credits the number of credits
     * @return List of courses with the specified credits
     */
    @GetMapping("/credits/{credits}")
    public ResponseEntity<List<CourseDTO>> getCoursesByCredits(@PathVariable Integer credits) {
        List<CourseDTO> courses = courseService.findByCredits(credits);
        return ResponseEntity.ok(courses);
    }

    /**
     * Get courses by duration
     * @param duration the duration in hours
     * @return List of courses with the specified duration
     */
    @GetMapping("/duration/{duration}")
    public ResponseEntity<List<CourseDTO>> getCoursesByDuration(@PathVariable Integer duration) {
        List<CourseDTO> courses = courseService.findByDuration(duration);
        return ResponseEntity.ok(courses);
    }

    /**
     * Get courses by credits OR duration
     * @param credits the number of credits (optional)
     * @param duration the duration in hours (optional)
     * @return List of courses matching either criteria
     */
    @GetMapping("/search")
    public ResponseEntity<List<CourseDTO>> getCoursesByCreditsOrDuration(
            @RequestParam(required = false) Integer credits,
            @RequestParam(required = false) Integer duration) {
        List<CourseDTO> courses = courseService.findByCreditsOrDuration(credits, duration);
        return ResponseEntity.ok(courses);
    }

    /**
     * Get courses by minimum credits
     * @param minCredits the minimum number of credits
     * @return List of courses with at least the specified credits
     */
    @GetMapping("/min-credits/{minCredits}")
    public ResponseEntity<List<CourseDTO>> getCoursesByMinCredits(@PathVariable Integer minCredits) {
        List<CourseDTO> courses = courseService.findByMinCredits(minCredits);
        return ResponseEntity.ok(courses);
    }

    /**
     * Get courses by maximum duration
     * @param maxDuration the maximum duration
     * @return List of courses with duration less than or equal to the specified value
     */
    @GetMapping("/max-duration/{maxDuration}")
    public ResponseEntity<List<CourseDTO>> getCoursesByMaxDuration(@PathVariable Integer maxDuration) {
        List<CourseDTO> courses = courseService.findByMaxDuration(maxDuration);
        return ResponseEntity.ok(courses);
    }

    /**
     * Update an existing course
     * @param id the course ID
     * @param courseDTO the updated course data
     * @return the updated course
     */
    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDTO courseDTO) {
        CourseDTO updatedCourse = courseService.update(id, courseDTO);
        return ResponseEntity.ok(updatedCourse);
    }

    /**
     * Delete course by ID
     * @param id the course ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if course exists by name
     * @param name the course name
     * @return true if course exists, false otherwise
     */
    @GetMapping("/exists/name/{name}")
    public ResponseEntity<Boolean> existsByName(@PathVariable String name) {
        boolean exists = courseService.existsByName(name);
        return ResponseEntity.ok(exists);
    }
}