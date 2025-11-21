package com.bootcamp.onlineschool;

import com.bootcamp.onlineschool.model.Student;
import java.util.*;
import java.util.stream.Collectors;

/**
 * StudentRegistry class demonstrating:
 * - Collections (ArrayList, HashMap)
 * - Iteration (for-each, Iterator)
 * - Streams API
 * - Lambda expressions
 * - Sorting
 */
public class StudentRegistry {
    private List<Student> students;
    private Map<String, Student> studentMap;
    
    public StudentRegistry() {
        this.students = new ArrayList<>();
        this.studentMap = new HashMap<>();
    }
    
    /**
     * Add a student to the registry
     */
    public void addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (!student.isValidEmail()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        students.add(student);
        studentMap.put(student.getStudentId(), student);
    }
    
    /**
     * Remove a student by ID
     */
    public boolean removeStudent(String studentId) {
        Student student = studentMap.remove(studentId);
        if (student != null) {
            students.remove(student);
            return true;
        }
        return false;
    }
    
    /**
     * Find a student by ID
     */
    public Student findStudentById(String studentId) {
        return studentMap.get(studentId);
    }
    
    /**
     * Find students by name (partial match)
     */
    public List<Student> findStudentsByName(String name) {
        return students.stream()
                .filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get all students sorted by name
     */
    public List<Student> getAllStudentsSortedByName() {
        return students.stream()
                .sorted(Comparator.comparing(Student::getName))
                .collect(Collectors.toList());
    }
    
    /**
     * Get all students sorted by GPA (descending)
     */
    public List<Student> getAllStudentsSortedByGpa() {
        return students.stream()
                .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                .collect(Collectors.toList());
    }
    
    /**
     * Get students with GPA above threshold
     */
    public List<Student> getStudentsWithHighGpa(double threshold) {
        return students.stream()
                .filter(s -> s.getGpa() >= threshold)
                .collect(Collectors.toList());
    }
    
    /**
     * Display all students
     */
    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students in registry");
            return;
        }
        students.forEach(System.out::println);
    }
    
    /**
     * Get total number of students
     */
    public int getStudentCount() {
        return students.size();
    }
    
    /**
     * Get average GPA
     */
    public double getAverageGpa() {
        if (students.isEmpty()) {
            return 0.0;
        }
        return students.stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0.0);
    }
    
    /**
     * Clear all students
     */
    public void clear() {
        students.clear();
        studentMap.clear();
    }
}
