package com.bootcamp.onlineschool.dto;

import com.bootcamp.onlineschool.entity.EnrollmentStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class EnrollmentDTO {

    @NotNull
    private Long studentId;

    @NotNull
    private Long courseId;

    @NotNull
    private LocalDate enrollmentDate;

    private EnrollmentStatus status;

    private String grade;

    public EnrollmentDTO() {
    }

    public EnrollmentDTO(Long studentId, Long courseId, LocalDate enrollmentDate,
                         EnrollmentStatus status) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
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

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
