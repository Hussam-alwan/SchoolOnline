package com.bootcamp.onlineschool.dto;

import com.bootcamp.onlineschool.entity.Course;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Schema(description = "Course data transfer object representing course information")
public class CourseDTO {

    @Schema(description = "Unique identifier for the course", example = "1")
    private Long id;

    @Schema(description = "Name of the course", example = "Introduction to Java", required = true)
    @NotBlank(message = "Course name is required")
    @Size(min = 2, max = 100, message = "Course name must be between 2 and 100 characters")
    private String name;

    @Schema(description = "Detailed description of the course", example = "A comprehensive introduction to Java programming language")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Schema(description = "Number of credits for the course", example = "3", required = true)
    @NotNull(message = "Credits is required")
    @Min(value = 1, message = "Credits must be at least 1")
    private Integer credits;



    @Schema(description = "List of class IDs where this course is offered", example = "[1, 2, 3]")
    private Set<Long> classIds = new HashSet<>();
    
    @Schema(description = "List of registration IDs for this course", example = "[1, 2, 3]")
    private Set<Long> registrationIds = new HashSet<>();

    @Schema(description = "Timestamp when the course was created", example = "2023-01-01T10:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the course was last updated", example = "2023-01-01T10:00:00")
    private LocalDateTime updatedAt;

    // Default constructor
    public CourseDTO() {}

    // Constructor with required fields
    public CourseDTO(String name, String description, Integer credits) {
        this.name = name;
        this.description = description;
        this.credits = credits;
    }

    // Constructor with all fields
    public CourseDTO(Long id, String name, String description, Integer credits,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.credits = credits;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Set<Long> getClassIds() {
        return classIds;
    }

    public void setClassIds(Set<Long> classIds) {
        this.classIds = classIds;
    }

    public Set<Long> getRegistrationIds() {
        return registrationIds;
    }

    public void setRegistrationIds(Set<Long> registrationIds) {
        this.registrationIds = registrationIds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Utility method to convert from Entity to DTO
    public static CourseDTO fromEntity(Course course) {
        if (course == null) {
            return null;
        }

        CourseDTO dto = new CourseDTO(
            course.getCourseName(),
            null,
            course.getCredits()
        );
        dto.setId(course.getId());
        dto.setCreatedAt(course.getCreatedAt());
        dto.setUpdatedAt(course.getUpdatedAt());

        return dto;
    }

    // Utility method to convert from DTO to Entity (for updates)
    public Course toEntity() {
        Course course = new Course();
        course.setId(this.id);
        course.setCourseName(this.name);
        course.setCredits(this.credits);
        return course;
    }

    @Override
    public String toString() {
        return "CourseDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", credits=" + credits +
                ", classIds=" + classIds +
                ", registrationIds=" + registrationIds +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}