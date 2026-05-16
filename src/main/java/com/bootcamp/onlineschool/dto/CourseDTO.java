package com.bootcamp.onlineschool.dto;

import jakarta.validation.constraints.*;

public class CourseDTO {

    private String id;

    @NotBlank(message = "Course name is required")
    @Size(min = 2, max = 150, message = "Course name must be between 2 and 150 characters")
    private String courseName;

    @NotNull(message = "Credits are required")
    @Min(value = 1, message = "Credits must be at least 1")
    @Max(value = 6, message = "Credits must be at most 6")
    private Integer credits;

    @NotBlank(message = "Instructor name is required")
    @Size(min = 2, max = 100, message = "Instructor name must be between 2 and 100 characters")
    private String instructor;

    @NotNull(message = "Max students is required")
    @Min(value = 1, message = "Max students must be at least 1")
    @Max(value = 500, message = "Max students must be at most 500")
    private Integer maxStudents;

    private int enrolledStudents = 0;

    public CourseDTO() {}

    public CourseDTO(String id, String courseName, Integer credits,
                     String instructor, Integer maxStudents, int enrolledStudents) {
        this.id = id;
        this.courseName = courseName;
        this.credits = credits;
        this.instructor = instructor;
        this.maxStudents = maxStudents;
        this.enrolledStudents = enrolledStudents;
    }

    public CourseDTO(String id, String courseName, Integer credits,
                     String instructor, Integer maxStudents) {
        this(id, courseName, credits, instructor, maxStudents, 0);
    }

    public int getAvailableSeats() {
        if (maxStudents == null) return 0;
        return maxStudents - enrolledStudents;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public Integer getMaxStudents() { return maxStudents; }
    public void setMaxStudents(Integer maxStudents) { this.maxStudents = maxStudents; }

    public int getEnrolledStudents() { return enrolledStudents; }
    public void setEnrolledStudents(int enrolledStudents) { this.enrolledStudents = enrolledStudents; }

    @Override
    public String toString() {
        return "CourseDTO{id='" + id + "', courseName='" + courseName +
                "', credits=" + credits + ", instructor='" + instructor +
                "', maxStudents=" + maxStudents + ", enrolledStudents=" + enrolledStudents +
                ", availableSeats=" + getAvailableSeats() + "}";
    }
}