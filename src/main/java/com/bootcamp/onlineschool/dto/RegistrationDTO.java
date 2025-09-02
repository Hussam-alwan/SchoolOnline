package com.bootcamp.onlineschool.dto;

import com.bootcamp.onlineschool.entity.Registration;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Registration data transfer object representing student course registration")
public class RegistrationDTO {

    @Schema(description = "Unique identifier for the registration", example = "1")
    private Long id;

    @Schema(description = "Date when the student registered for the course", example = "2023-01-15", required = true)
    @NotNull(message = "Registration date is required")
    @PastOrPresent(message = "Registration date cannot be in the future")
    private LocalDate registrationDate;

    @Schema(description = "Current status of the registration", example = "ACTIVE", required = true)
    @NotBlank(message = "Status is required")
    @Size(max = 20, message = "Status must not exceed 20 characters")
    private String status;

    @Schema(description = "Grade received for the course", example = "A")
    @Size(max = 5, message = "Grade must not exceed 5 characters")
    private String grade;

    @Schema(description = "ID of the student who registered", example = "1", required = true)
    @NotNull(message = "Student is required")
    private Long studentId;

    @Schema(description = "ID of the course being registered for", example = "1", required = true)
    @NotNull(message = "Course is required")
    private Long courseId;

    @Schema(description = "Name of the student who registered", example = "John Doe")
    private String studentName;
    
    @Schema(description = "Name of the course being registered for", example = "Introduction to Java")
    private String courseName;

    @Schema(description = "Timestamp when the registration was created", example = "2023-01-01T10:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the registration was last updated", example = "2023-01-01T10:00:00")
    private LocalDateTime updatedAt;

    // Default constructor
    public RegistrationDTO() {}

    // Constructor with required fields
    public RegistrationDTO(LocalDate registrationDate, String status, Long studentId, Long courseId) {
        this.registrationDate = registrationDate;
        this.status = status;
        this.studentId = studentId;
        this.courseId = courseId;
    }

    // Constructor with all fields
    public RegistrationDTO(Long id, LocalDate registrationDate, String status, String grade,
                          Long studentId, Long courseId, String studentName, String courseName,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.registrationDate = registrationDate;
        this.status = status;
        this.grade = grade;
        this.studentId = studentId;
        this.courseId = courseId;
        this.studentName = studentName;
        this.courseName = courseName;
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

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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
    public static RegistrationDTO fromEntity(Registration registration) {
        if (registration == null) {
            return null;
        }

        return new RegistrationDTO(
            registration.getId(),
            registration.getRegistrationDate(),
            registration.getStatus(),
            registration.getGrade(),
            registration.getStudent() != null ? registration.getStudent().getId() : null,
            registration.getCourse() != null ? registration.getCourse().getId() : null,
            registration.getStudent() != null ? registration.getStudent().getName() : null,
            registration.getCourse() != null ? registration.getCourse().getName() : null,
            registration.getCreatedAt(),
            registration.getUpdatedAt()
        );
    }

    // Utility method to convert from DTO to Entity (for updates)
    public Registration toEntity() {
        Registration registration = new Registration();
        registration.setId(this.id);
        registration.setRegistrationDate(this.registrationDate);
        registration.setStatus(this.status);
        registration.setGrade(this.grade);
        registration.setCreatedAt(this.createdAt);
        registration.setUpdatedAt(this.updatedAt);
        // Note: Student and Course relationships should be set separately in service layer
        return registration;
    }

    @Override
    public String toString() {
        return "RegistrationDTO{" +
                "id=" + id +
                ", registrationDate=" + registrationDate +
                ", status='" + status + '\'' +
                ", grade='" + grade + '\'' +
                ", studentId=" + studentId +
                ", courseId=" + courseId +
                ", studentName='" + studentName + '\'' +
                ", courseName='" + courseName + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}