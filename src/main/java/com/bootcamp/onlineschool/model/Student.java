package com.bootcamp.onlineschool.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Student class demonstrating:
 * - Encapsulation (private fields, public getters/setters)
 * - Constructor overloading
 * - toString() method
 * - equals() and hashCode() methods
 */
public class Student extends User {

    private double gpa;
    private int age;
    List<Course> enrolledCourses=new ArrayList<>();

    private void validateAge(int age) {
        if (age < 16 || age > 100) {
            throw new IllegalArgumentException("Age must be between 16 and 100");
        }
    }

    // Constructor with required fields
    public Student(String studentId, String name, String email) {
        super(studentId, name, email);
        this.gpa = 0.0;
        this.age = 0;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    // Constructor with age (no gpa)
    public Student(String studentId, String name, String email, int age) {
        super(studentId, name, email);
        this.gpa = 0.0;
        validateAge(age);
        this.age = age;
    }
    
    // Constructor with all fields
    public Student(String studentId, String name, String email, double gpa, int age) {
        super(studentId, name, email);
        setGpa(gpa);
        validateAge(age);
        this.age = age;
    }
    
    // Getters and Setters
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



    @Override
    public String toString() {
        return String.format("Student{%s, gpa=%.2f, age=%d}",
                super.toString(), gpa, age);
    }

    public void enrollInCourse(Course course) {
        if (course != null && !enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
        }
    }

    public void dropCourse(String courseId) {
        for (Course course : enrolledCourses) {
            if (course.getCourseId().equals(courseId)) {
                enrolledCourses.remove(course);
                break;
            }
        }
    }
    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public int getTotalCredits() {
        return enrolledCourses.stream().mapToInt(Course::getCredits).sum();
    }

    public void calculateGpa(Map<Course,String> grades) {
        if (grades == null || grades.isEmpty()) {
            return;
        }

        double totalPoints = 0.0;
        int totalCredits = 0;

        for (var entry : grades.entrySet()) {
            Course course = entry.getKey();
            String grade = entry.getValue();

            double gradePoint = switch (grade){
                case "A"-> 4.0;
                case "B"-> 3.0;
                case "C"-> 2.0;
                case "D"-> 1.0;
                case "F"-> 0;
                default -> throw new IllegalArgumentException("Invalid grade " + grade);
            };
            totalPoints += gradePoint *  course.getCredits();
            totalCredits += course.getCredits();
        }

        if (totalCredits > 0) setGpa(totalPoints / totalCredits);

    }
}
