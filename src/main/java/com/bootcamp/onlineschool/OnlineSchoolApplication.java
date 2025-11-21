package com.bootcamp.onlineschool;

import com.bootcamp.onlineschool.model.Student;

/**
 * Lab 1: Java Fundamentals
 * 
 * This is a simple Java application demonstrating core Java concepts:
 * - Classes and Objects
 * - Encapsulation
 * - Collections
 * - String manipulation
 * - Basic algorithms
 */
public class OnlineSchoolApplication {
    
    public static void main(String[] args) {
        System.out.println("=== Online School - Lab 1: Java Fundamentals ===\n");
        
        // Example: Create and display students
        StudentRegistry registry = new StudentRegistry();
        
        // Add some students
        Student student1 = new Student("STU001", "Alice Johnson", "alice@school.edu");
        Student student2 = new Student("STU002", "Bob Smith", "bob@school.edu");
        Student student3 = new Student("STU003", "Charlie Brown", "charlie@school.edu");
        
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);
        
        // Display all students
        System.out.println("All Students:");
        registry.displayAllStudents();
        
        // Search for a student
        System.out.println("\nSearching for student with ID 'STU002':");
        Student found = registry.findStudentById("STU002");
        if (found != null) {
            System.out.println("Found: " + found);
        }
        
        // Count students
        System.out.println("\nTotal students: " + registry.getStudentCount());
    }
}
