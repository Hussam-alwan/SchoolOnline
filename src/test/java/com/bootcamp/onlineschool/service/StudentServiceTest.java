package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.StudentRegistry;
import com.bootcamp.onlineschool.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StudentService tests demonstrating Spring Boot testing
 * 
 * Demonstrates:
 * - @SpringBootTest annotation
 * - Dependency injection in tests
 * - Service layer testing
 * - Integration testing with Spring context
 */
@SpringBootTest
@DisplayName("StudentService Tests")
public class StudentServiceTest {
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private StudentRegistry studentRegistry;
    
    @BeforeEach
    public void setUp() {
        // Clear registry before each test
        studentRegistry.clear();
    }
    
    @Test
    @DisplayName("Should add student successfully")
    public void testAddStudent() {
        Student student = new Student("STU001", "Alice", "alice@school.edu");
        studentService.addStudent(student);
        
        assertEquals(1, studentService.getTotalStudents());
    }
    
    @Test
    @DisplayName("Should throw exception when adding null student")
    public void testAddNullStudent() {
        assertThrows(IllegalArgumentException.class, () -> studentService.addStudent(null));
    }
    
    @Test
    @DisplayName("Should find student by ID")
    public void testFindStudentById() {
        Student student = new Student("STU001", "Alice", "alice@school.edu");
        studentService.addStudent(student);
        
        Student found = studentService.findStudentById("STU001");
        assertNotNull(found);
        assertEquals("Alice", found.getName());
    }
    
    @Test
    @DisplayName("Should throw exception when student not found")
    public void testFindNonExistentStudent() {
        assertThrows(StudentService.StudentNotFoundException.class, 
            () -> studentService.findStudentById("NONEXISTENT"));
    }
    
    @Test
    @DisplayName("Should get all students sorted by name")
    public void testGetAllStudents() {
        studentService.addStudent(new Student("STU003", "Charlie", "charlie@school.edu"));
        studentService.addStudent(new Student("STU001", "Alice", "alice@school.edu"));
        studentService.addStudent(new Student("STU002", "Bob", "bob@school.edu"));
        
        List<Student> students = studentService.getAllStudents();
        assertEquals(3, students.size());
        assertEquals("Alice", students.get(0).getName());
        assertEquals("Bob", students.get(1).getName());
        assertEquals("Charlie", students.get(2).getName());
    }
    
    @Test
    @DisplayName("Should find students by name")
    public void testFindStudentsByName() {
        studentService.addStudent(new Student("STU001", "Alice Johnson", "alice@school.edu"));
        studentService.addStudent(new Student("STU002", "Bob Smith", "bob@school.edu"));
        studentService.addStudent(new Student("STU003", "Charlie Brown", "charlie@school.edu"));
        
        List<Student> results = studentService.findStudentsByName("Charlie");
        assertEquals(1, results.size());
        assertEquals("Charlie Brown", results.get(0).getName());
    }
    
    @Test
    @DisplayName("Should get high achievers")
    public void testGetHighAchievers() {
        studentService.addStudent(new Student("STU001", "Alice", "alice@school.edu", 3.8));
        studentService.addStudent(new Student("STU002", "Bob", "bob@school.edu", 3.5));
        studentService.addStudent(new Student("STU003", "Charlie", "charlie@school.edu", 3.9));
        
        List<Student> highAchievers = studentService.getHighAchievers(3.8);
        assertEquals(2, highAchievers.size());
    }
    
    @Test
    @DisplayName("Should remove student")
    public void testRemoveStudent() {
        studentService.addStudent(new Student("STU001", "Alice", "alice@school.edu"));
        assertEquals(1, studentService.getTotalStudents());
        
        boolean removed = studentService.removeStudent("STU001");
        assertTrue(removed);
        assertEquals(0, studentService.getTotalStudents());
    }
    
    @Test
    @DisplayName("Should calculate average GPA")
    public void testGetAverageGpa() {
        studentService.addStudent(new Student("STU001", "Alice", "alice@school.edu", 3.6));
        studentService.addStudent(new Student("STU002", "Bob", "bob@school.edu", 3.4));
        studentService.addStudent(new Student("STU003", "Charlie", "charlie@school.edu", 3.8));
        
        double average = studentService.getAverageGpa();
        assertEquals((3.6 + 3.4 + 3.8) / 3, average, 0.01);
    }
}
