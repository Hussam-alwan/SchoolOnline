package com.bootcamp.onlineschool.dto;

import com.bootcamp.onlineschool.entity.Clazz;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Schema(description = "Class data transfer object representing class information")
public class ClazzDTO {

    @Schema(description = "Unique identifier for the class", example = "1")
    private Long id;

    @Schema(description = "Name of the class", example = "Java Programming 101", required = true)
    @NotBlank(message = "Class name is required")
    @Size(min = 2, max = 100, message = "Class name must be between 2 and 100 characters")
    private String name;

    @Schema(description = "Semester when the class is offered", example = "Fall", required = true)
    @NotBlank(message = "Semester is required")
    @Size(min = 2, max = 20, message = "Semester must be between 2 and 20 characters")
    private String semester;

    @Schema(description = "Year when the class is offered", example = "2023", required = true)
    @NotNull(message = "Year is required")
    @Min(value = 2000, message = "Year must be at least 2000")
    private Integer year;

    @Schema(description = "Maximum number of students allowed in the class", example = "30", required = true)
    @NotNull(message = "Max capacity is required")
    @Min(value = 1, message = "Max capacity must be at least 1")
    private Integer maxCapacity;

    @Schema(description = "ID of the teacher assigned to this class", example = "1", required = true)
    @NotNull(message = "Teacher is required")
    private Long teacherId;

    @Schema(description = "Name of the teacher assigned to this class", example = "Dr. Smith")
    private String teacherName; // For display purposes

    @Schema(description = "List of student IDs enrolled in this class", example = "[1, 2, 3]")
    private Set<Long> studentIds = new HashSet<>();
    
    @Schema(description = "List of course IDs offered in this class", example = "[1, 2, 3]")
    private Set<Long> courseIds = new HashSet<>();

    @Schema(description = "Timestamp when the class was created", example = "2023-01-01T10:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the class was last updated", example = "2023-01-01T10:00:00")
    private LocalDateTime updatedAt;

    // Default constructor
    public ClazzDTO() {}

    // Constructor with required fields
    public ClazzDTO(String name, String semester, Integer year, Integer maxCapacity, Long teacherId) {
        this.name = name;
        this.semester = semester;
        this.year = year;
        this.maxCapacity = maxCapacity;
        this.teacherId = teacherId;
    }

    // Constructor with all fields
    public ClazzDTO(Long id, String name, String semester, Integer year, Integer maxCapacity,
                   Long teacherId, String teacherName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.year = year;
        this.maxCapacity = maxCapacity;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
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

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Set<Long> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(Set<Long> studentIds) {
        this.studentIds = studentIds;
    }

    public Set<Long> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(Set<Long> courseIds) {
        this.courseIds = courseIds;
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
    public static ClazzDTO fromEntity(Clazz clazz) {
        if (clazz == null) {
            return null;
        }

        ClazzDTO dto = new ClazzDTO(
            clazz.getId(),
            clazz.getName(),
            clazz.getSemester(),
            clazz.getYear(),
            clazz.getMaxCapacity(),
            clazz.getTeacher() != null ? clazz.getTeacher().getId() : null,
            clazz.getTeacher() != null ? clazz.getTeacher().getName() : null,
            clazz.getCreatedAt(),
            clazz.getUpdatedAt()
        );

        // Convert student relationships to IDs
        if (clazz.getStudents() != null) {
            clazz.getStudents().forEach(student -> dto.getStudentIds().add(student.getId()));
        }

        // Convert course relationships to IDs
        if (clazz.getCourses() != null) {
            clazz.getCourses().forEach(course -> dto.getCourseIds().add(course.getId()));
        }

        return dto;
    }

    // Utility method to convert from DTO to Entity (for updates)
    public Clazz toEntity() {
        Clazz clazz = new Clazz();
        clazz.setId(this.id);
        clazz.setName(this.name);
        clazz.setSemester(this.semester);
        clazz.setYear(this.year);
        clazz.setMaxCapacity(this.maxCapacity);
        clazz.setCreatedAt(this.createdAt);
        clazz.setUpdatedAt(this.updatedAt);
        // Note: Teacher relationship should be set separately in service layer
        return clazz;
    }

    @Override
    public String toString() {
        return "ClazzDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", semester='" + semester + '\'' +
                ", year=" + year +
                ", maxCapacity=" + maxCapacity +
                ", teacherId=" + teacherId +
                ", teacherName='" + teacherName + '\'' +
                ", studentIds=" + studentIds +
                ", courseIds=" + courseIds +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}