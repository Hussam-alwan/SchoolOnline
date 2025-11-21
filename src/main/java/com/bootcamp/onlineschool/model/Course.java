package com.bootcamp.onlineschool.model;

import java.util.Objects;

/**
 * Course class demonstrating:
 * - Encapsulation
 * - Validation
 * - Object equality
 */
public class Course {
    private String courseId;
    private String courseName;
    private int credits;
    private String instructor;
    private int maxStudents;
    private int enrolledStudents;
    
    public Course(String courseId, String courseName, int credits, String instructor, int maxStudents) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.instructor = instructor;
        this.maxStudents = maxStudents;
        this.enrolledStudents = 0;
    }
    
    public String getCourseId() {
        return courseId;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public int getCredits() {
        return credits;
    }
    
    public String getInstructor() {
        return instructor;
    }
    
    public int getMaxStudents() {
        return maxStudents;
    }
    
    public int getEnrolledStudents() {
        return enrolledStudents;
    }
    
    public int getAvailableSeats() {
        return maxStudents - enrolledStudents;
    }
    
    public boolean isFull() {
        return enrolledStudents >= maxStudents;
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
    
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
    
    @Override
    public String toString() {
        return String.format("Course{id='%s', name='%s', credits=%d, instructor='%s', enrolled=%d/%d}",
                courseId, courseName, credits, instructor, enrolledStudents, maxStudents);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseId, course.courseId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }
}
