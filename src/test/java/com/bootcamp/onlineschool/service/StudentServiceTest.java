package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.StudentRegistry;
import com.bootcamp.onlineschool.dto.StudentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
        studentService.addStudent(new StudentDTO("STU001", "Alice", "alice@school.edu", 3.5));

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
        studentService.addStudent(new StudentDTO("STU001", "Alice", "alice@school.edu", 3.5));

        StudentDTO found = studentService.getStudentById("STU001");
        assertNotNull(found);
        assertEquals("Alice", found.getName());
    }
    
    @Test
    @DisplayName("Should throw exception when student not found")
    public void testFindNonExistentStudent() {
        assertThrows(StudentService.StudentNotFoundException.class,
            () -> studentService.getStudentById("NONEXISTENT"));
    }
    
    @Test
    @DisplayName("Should get all students sorted by name")
    public void testGetAllStudents() {
        studentService.addStudent(new StudentDTO("STU003", "Charlie", "charlie@school.edu", 3.0));
        studentService.addStudent(new StudentDTO("STU001", "Alice", "alice@school.edu", 3.7));
        studentService.addStudent(new StudentDTO("STU002", "Bob", "bob@school.edu", 3.4));

        List<StudentDTO> students = studentService.getAllStudents();
        assertEquals(3, students.size());
        assertEquals("Alice", students.get(0).getName());
        assertEquals("Bob", students.get(1).getName());
        assertEquals("Charlie", students.get(2).getName());
    }

    @Test
    @DisplayName("Should find students by name")
    public void testFindStudentsByName() {
        studentService.addStudent(new StudentDTO("STU001", "Alice Johnson", "alice@school.edu", 3.5));
        studentService.addStudent(new StudentDTO("STU002", "Bob Smith", "bob@school.edu", 3.2));
        studentService.addStudent(new StudentDTO("STU003", "Charlie Brown", "charlie@school.edu", 3.8));

        List<StudentDTO> results = studentService.findStudentsByName("Charlie");
        assertEquals(1, results.size());
        assertEquals("Charlie Brown", results.getFirst().getName());
    }

    @Test
    @DisplayName("Should get high achievers")
    public void testGetHighAchievers() {
        studentService.addStudent(new StudentDTO("STU001", "Alice", "alice@school.edu", 3.8));
        studentService.addStudent(new StudentDTO("STU002", "Bob", "bob@school.edu", 3.5));
        studentService.addStudent(new StudentDTO("STU003", "Charlie", "charlie@school.edu", 3.9));

        List<StudentDTO> highAchievers = studentService.getHighAchievers(3.8);
        assertEquals(2, highAchievers.size());
    }

    @Test
    @DisplayName("Should update student")
    public void testUpdateStudent() {
        studentService.addStudent(new StudentDTO("STU001", "Alice", "alice@school.edu", 3.5));

        StudentDTO updated = studentService.updateStudent("STU001",
                new StudentDTO("STU001", "Alice Updated", "alice.new@school.edu", 3.9));

        assertEquals("Alice Updated", updated.getName());
        assertEquals(3.9, updated.getGpa());
    }

    @Test
    @DisplayName("Should throw when updating non-existent student")
    public void testUpdateNonExistentStudent() {
        assertThrows(StudentService.StudentNotFoundException.class,
                () -> studentService.updateStudent("NONE",
                        new StudentDTO("NONE", "X", "x@school.edu", 3.0)));
    }

    @Test
    @DisplayName("Should remove student")
    public void testRemoveStudent() {
        studentService.addStudent(new StudentDTO("STU001", "Alice", "alice@school.edu", 3.5));
        assertEquals(1, studentService.getTotalStudents());

        studentService.deleteStudent("STU001");
        assertEquals(0, studentService.getTotalStudents());
    }

    @Test
    @DisplayName("Should throw when deleting non-existent student")
    public void testDeleteNonExistentStudent() {
        assertThrows(StudentService.StudentNotFoundException.class,
                () -> studentService.deleteStudent("NONE"));
    }

    @Test
    @DisplayName("Should calculate average GPA")
    public void testGetAverageGpa() {
        studentService.addStudent(new StudentDTO("STU001", "Alice", "alice@school.edu", 3.6));
        studentService.addStudent(new StudentDTO("STU002", "Bob", "bob@school.edu", 3.4));
        studentService.addStudent(new StudentDTO("STU003", "Charlie", "charlie@school.edu", 3.8));

        double average = studentService.getAverageGpa();
        assertEquals((3.6 + 3.4 + 3.8) / 3, average, 0.01);
    }
}
