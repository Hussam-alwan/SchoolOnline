package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.dto.StudentDTO;
import com.bootcamp.onlineschool.service.StudentService;
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
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO dto) {
        StudentDTO created = studentService.addStudent(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(toModel(created));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<StudentDTO>> getAllStudents(
            @RequestParam(required = false) String name) {
        List<StudentDTO> students = (name != null && !name.isBlank())
                ? studentService.findStudentsByName(name)
                : studentService.getAllStudents();
        return ResponseEntity.ok(toCollectionModel(students));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable String id) {
        return ResponseEntity.ok(toModel(studentService.getStudentById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(
            @PathVariable String id,
            @Valid @RequestBody StudentDTO dto) {
        return ResponseEntity.ok(toModel(studentService.updateStudent(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/high-achievers")
    public ResponseEntity<CollectionModel<StudentDTO>> getHighAchievers(
            @RequestParam(defaultValue = "3.5") Double gpa) {
        return ResponseEntity.ok(toCollectionModel(studentService.getHighAchievers(gpa)));
    }

    /**
     * Attaches hypermedia links to a single student: a self link, a link back to
     * the students collection, and a related link to the high-achievers collection.
     */
    private StudentDTO toModel(StudentDTO dto) {
        dto.removeLinks(); // keep idempotent so links are never duplicated
        dto.add(linkTo(methodOn(StudentController.class).getStudentById(dto.getId())).withSelfRel());
        dto.add(linkTo(StudentController.class).withRel("students"));
        dto.add(linkTo(methodOn(StudentController.class).getHighAchievers(3.5)).withRel("high-achievers"));
        return dto;
    }

    /**
     * Wraps a list of students in a CollectionModel: each item gets its own links
     * (via {@link #toModel}) and the collection itself gets a self link.
     */
    private CollectionModel<StudentDTO> toCollectionModel(List<StudentDTO> dtos) {
        dtos.forEach(this::toModel);
        return CollectionModel.of(dtos, linkTo(StudentController.class).withSelfRel());
    }
}
