package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.dto.CourseDTO;
import com.bootcamp.onlineschool.service.CourseService;
import com.bootcamp.onlineschool.service.CourseService.CourseAlreadyExistsException;
import com.bootcamp.onlineschool.service.CourseService.CourseFullException;
import com.bootcamp.onlineschool.service.CourseService.CourseNotFoundException;
import com.bootcamp.onlineschool.service.CourseService.NoStudentsEnrolledException;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO dto) {
        CourseDTO created = courseService.createCourseDTO(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(toModel(created));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<CourseDTO>> getAllCourses(
            @RequestParam(required = false) String name) {

        List<CourseDTO> result = (name != null)
                ? courseService.searchCourses(name, null)
                : courseService.getAllCoursesDTO();

        return ResponseEntity.ok(toCollectionModel(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable String id) {
        return ResponseEntity.ok(toModel(courseService.getCourseByIdDTO(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(
            @PathVariable String id,
            @Valid @RequestBody CourseDTO dto) {

        return ResponseEntity.ok(toModel(courseService.updateCourse(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        courseService.deleteCourseOrThrow(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/enroll")
    public ResponseEntity<CourseDTO> enrollStudent(@PathVariable String id) {
        return ResponseEntity.ok(toModel(courseService.enrollStudentDTO(id)));
    }

    @PostMapping("/{id}/unenroll")
    public ResponseEntity<CourseDTO> unenrollStudent(@PathVariable String id) {
        return ResponseEntity.ok(toModel(courseService.unenrollStudentDTO(id)));
    }

    @GetMapping("/available")
    public ResponseEntity<CollectionModel<CourseDTO>> getAvailableCourses() {
        return ResponseEntity.ok(toCollectionModel(courseService.getAvailableCoursesDTO()));
    }

    @GetMapping("/instructor/{name}")
    public ResponseEntity<CollectionModel<CourseDTO>> getCoursesByInstructor(@PathVariable String name) {
        return ResponseEntity.ok(toCollectionModel(courseService.getCoursesByInstructor(name)));
    }

    @GetMapping("/search")
    public ResponseEntity<CollectionModel<CourseDTO>> searchCourses(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minCredits) {

        return ResponseEntity.ok(toCollectionModel(courseService.searchCourses(name, minCredits)));
    }


    private CourseDTO toModel(CourseDTO dto) {
        dto.removeLinks(); // keep idempotent so links are never duplicated
        dto.add(linkTo(methodOn(CourseController.class).getCourseById(dto.getId())).withSelfRel());
        dto.add(linkTo(CourseController.class).withRel("courses"));
        dto.add(linkTo(methodOn(CourseController.class).enrollStudent(dto.getId())).withRel("enroll"));
        dto.add(linkTo(methodOn(CourseController.class).unenrollStudent(dto.getId())).withRel("unenroll"));
        return dto;
    }


    private CollectionModel<CourseDTO> toCollectionModel(List<CourseDTO> dtos) {
        dtos.forEach(this::toModel);
        return CollectionModel.of(dtos, linkTo(CourseController.class).withSelfRel());
    }


    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<String> handleNotFound(CourseNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(CourseAlreadyExistsException.class)
    public ResponseEntity<String> handleConflict(CourseAlreadyExistsException ex) {
        return ResponseEntity.status(409).body(ex.getMessage());
    }

    @ExceptionHandler({CourseFullException.class, NoStudentsEnrolledException.class})
    public ResponseEntity<String> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }

}
