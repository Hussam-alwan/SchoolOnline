package com.bootcamp.onlineschool.model;

import com.bootcamp.onlineschool.entity.Department;
import com.bootcamp.onlineschool.entity.Enrollment;
import com.bootcamp.onlineschool.entity.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Course class demonstrating:
 * - Encapsulation
 * - Validation
 * - Object equality
 */
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", unique = true, length = 50)
    private String courseId;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "course_name", nullable = false, length = 100)
    private String courseName;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer credits;

    @Column(length = 100)
    private String instructor;

    @Column(name = "max_students")
    private Integer maxStudents;

    @Column(name = "enrolled_students")
    private Integer enrolledStudents = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Enrollment> enrollments = new HashSet<>();

    public Course() {
    }

    public Course(String courseName, Integer credits) {
        this.courseName = courseName;
        this.credits = credits;
    }

    public Course(String courseId, String courseName, int credits, String instructor, int maxStudents) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.instructor = instructor;
        this.maxStudents = maxStudents;
        this.enrolledStudents = 0;
    }

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

    public int getEnrolledStudents() {
        return enrolledStudents == null ? 0 : enrolledStudents;
    }

    public void setEnrolledStudents(Integer enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(Set<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
        enrollment.setCourse(this);
    }

    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
        enrollment.setCourse(null);
    }

    public int getAvailableSeats() {
        if (maxStudents == null) return Integer.MAX_VALUE;
        return maxStudents - getEnrolledStudents();
    }

    public boolean isFull() {
        if (maxStudents == null) return false;
        return getEnrolledStudents() >= maxStudents;
    }

    public boolean enrollStudent() {
        if (isFull()) {
            return false;
        }
        enrolledStudents = getEnrolledStudents() + 1;
        return true;
    }
    
    public boolean unenrollStudent() {
        if (getEnrolledStudents() <= 0) {
            return false;
        }
        enrolledStudents = getEnrolledStudents() - 1;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        if (id != null && course.id != null) {
            return Objects.equals(id, course.id);
        }
        return Objects.equals(courseId, course.courseId)
                && Objects.equals(courseName, course.courseName);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : Objects.hash(courseId, courseName);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", credits=" + credits +
                ", instructor='" + instructor + '\'' +
                ", enrolled=" + getEnrolledStudents() +
                "/" + maxStudents +
                '}';
    }
}
