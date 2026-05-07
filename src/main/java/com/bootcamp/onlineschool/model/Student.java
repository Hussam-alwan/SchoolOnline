package com.bootcamp.onlineschool.model;

import java.util.Objects;

/**
 * Student class demonstrating:
 * - Encapsulation (private fields, public getters/setters)
 * - Constructor overloading
 * - toString() method
 * - equals() and hashCode() methods
 */
public class Student {
    private String studentId;
    private String name;
    private String email;
    private double gpa;
    private int age;

    private void validateAge(int age) {
        if (age < 16 || age > 100) {
            throw new IllegalArgumentException("Age must be between 16 and 100");
        }
    }

    // Constructor with required fields
    public Student(String studentId, String name, String email) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.gpa = 0.0;
        this.age = 0;
    }

    // Constructor with age (no gpa)
    public Student(String studentId, String name, String email, int age) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.gpa = 0.0;
        validateAge(age);
        this.age = age;
    }
    
    // Constructor with all fields
    public Student(String studentId, String name, String email, double gpa, int age) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        setGpa(gpa);
        validateAge(age);
        this.age = age;
    }
    
    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public double getGpa() {
        return gpa;
    }
    
    public void setGpa(double gpa) {
        if (gpa >= 0.0 && gpa <= 4.0) {
            this.gpa = gpa;
        } else {
            throw new IllegalArgumentException("GPA must be between 0.0 and 4.0");
        }
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        validateAge(age);
        this.age = age;
    }

    // Validate email format
    public boolean isValidEmail() {
        return email != null && email.contains("@") && email.contains(".");
    }
    
    @Override
    public String toString() {
        return String.format("Student{id='%s', name='%s', email='%s', gpa=%.2f, age=%d}",
                studentId, name, email, gpa, age);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(studentId, student.studentId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}
