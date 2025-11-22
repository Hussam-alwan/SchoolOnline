package com.bootcamp.onlineschool.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Course demonstrates JPA entity with relationships
 * 
 * Demonstrates:
 * - @Entity and @Table annotations
 * - Primary key generation
 * - Column constraints and validation
 * - Temporal data handling
 * - Entity lifecycle callbacks
 */
@Entity
@Table(name = "courses")
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "course_id", unique = true, nullable = false, length = 20)
    private String courseId;
    
    @Column(name = "course_name", nullable = false, length = 100)
    private String courseName;
    
    @Column(name = "credits", nullable = false)
    private Integer credits;
    
    @Column(name = "instructor", nullable = false, length = 100)
    private String instructor;
    
    @Column(name = "max_students", nullable = false)
    private Integer maxStudents;
    
    @Column(name = "enrolled_students", nullable = false)
    private Integer enrolledStudents;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Course() {
    }
    
    public Course(String courseId, String courseName, Integer credits, 
                       String instructor, Integer maxStudents) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.instructor = instructor;
        this.maxStudents = maxStudents;
        this.enrolledStudents = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Business methods
    public boolean isFull() {
        return enrolledStudents >= maxStudents;
    }
    
    public Integer getAvailableSeats() {
        return maxStudents - enrolledStudents;
    }
    
    public boolean enrollStudent() {
        if (isFull()) {
            return false;
        }
        enrolledStudents++;
        return true;
    }
    
    public boolean unenrollStudent() {
        if (enrolledStudents <= 0) {
            return false;
        }
        enrolledStudents--;
        return true;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCourseId() {
        return courseId;
    }
    
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public Integer getCredits() {
        return credits;
    }
    
    public void setCredits(Integer credits) {
        this.credits = credits;
    }
    
    public String getInstructor() {
        return instructor;
    }
    
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
    
    public Integer getMaxStudents() {
        return maxStudents;
    }
    
    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }
    
    public Integer getEnrolledStudents() {
        return enrolledStudents;
    }
    
    public void setEnrolledStudents(Integer enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    @Override
    public String toString() {
        return String.format("Course{id=%d, courseId='%s', name='%s', instructor='%s', enrolled=%d/%d}",
                id, courseId, courseName, instructor, enrolledStudents, maxStudents);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course that = (Course) o;
        return Objects.equals(courseId, that.courseId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }
}
