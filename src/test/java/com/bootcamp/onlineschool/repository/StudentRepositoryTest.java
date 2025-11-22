package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StudentRepository tests demonstrating Spring Data JPA testing
 * 
 * Demonstrates:
 * - @DataJpaTest annotation
 * - Repository testing
 * - Custom query testing
 * - Database operations
 */
@DataJpaTest
@DisplayName("StudentRepository Tests")
public class StudentRepositoryTest {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @BeforeEach
    public void setUp() {
        studentRepository.deleteAll();
    }
    
    @Test
    @DisplayName("Should save and retrieve student")
    public void testSaveAndRetrieve() {
        Student student = new Student("STU001", "Alice", "alice@school.edu", 3.8);
        
        Student saved = studentRepository.save(student);
        assertNotNull(saved.getId());
        
        Optional<Student> found = studentRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getName());
    }
    
    @Test
    @DisplayName("Should find student by student ID")
    public void testFindByStudentId() {
        Student student = new Student("STU001", "Alice", "alice@school.edu");
        studentRepository.save(student);
        
        Optional<Student> found = studentRepository.findByStudentId("STU001");
        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getName());
    }
    
    @Test
    @DisplayName("Should find students by name")
    public void testFindByName() {
        studentRepository.save(new Student("STU001", "Alice Johnson", "alice@school.edu"));
        studentRepository.save(new Student("STU002", "Bob Smith", "bob@school.edu"));
        studentRepository.save(new Student("STU003", "Charlie Brown", "charlie@school.edu"));
        
        List<Student> results = studentRepository.findByNameContainingIgnoreCase("Charlie");
        assertEquals(1, results.size());
        assertEquals("Charlie Brown", results.get(0).getName());
    }
    
    @Test
    @DisplayName("Should find high achievers")
    public void testFindHighAchievers() {
        studentRepository.save(new Student("STU001", "Alice", "alice@school.edu", 3.8));
        studentRepository.save(new Student("STU002", "Bob", "bob@school.edu", 3.5));
        studentRepository.save(new Student("STU003", "Charlie", "charlie@school.edu", 3.9));
        
        List<Student> highAchievers = studentRepository.findHighAchievers(3.8);
        assertEquals(2, highAchievers.size());
        assertEquals(3.9, highAchievers.get(0).getGpa());
    }
    
    @Test
    @DisplayName("Should find all students sorted by name")
    public void testFindAllSortedByName() {
        studentRepository.save(new Student("STU003", "Charlie", "charlie@school.edu"));
        studentRepository.save(new Student("STU001", "Alice", "alice@school.edu"));
        studentRepository.save(new Student("STU002", "Bob", "bob@school.edu"));
        
        List<Student> sorted = studentRepository.findAllSortedByName();
        assertEquals(3, sorted.size());
        assertEquals("Alice", sorted.get(0).getName());
        assertEquals("Bob", sorted.get(1).getName());
        assertEquals("Charlie", sorted.get(2).getName());
    }
    
    @Test
    @DisplayName("Should count high achievers")
    public void testCountHighAchievers() {
        studentRepository.save(new Student("STU001", "Alice", "alice@school.edu", 3.8));
        studentRepository.save(new Student("STU002", "Bob", "bob@school.edu", 3.5));
        studentRepository.save(new Student("STU003", "Charlie", "charlie@school.edu", 3.9));
        
        Long count = studentRepository.countHighAchievers(3.8);
        assertEquals(2L, count);
    }
    
    @Test
    @DisplayName("Should calculate average GPA")
    public void testGetAverageGpa() {
        studentRepository.save(new Student("STU001", "Alice", "alice@school.edu", 3.6));
        studentRepository.save(new Student("STU002", "Bob", "bob@school.edu", 3.4));
        studentRepository.save(new Student("STU003", "Charlie", "charlie@school.edu", 3.8));
        
        Double average = studentRepository.getAverageGpa();
        assertNotNull(average);
        assertEquals((3.6 + 3.4 + 3.8) / 3, average, 0.01);
    }
    
    @Test
    @DisplayName("Should update student")
    public void testUpdateStudent() {
        Student student = new Student("STU001", "Alice", "alice@school.edu", 3.5);
        Student saved = studentRepository.save(student);
        
        saved.setGpa(3.8);
        studentRepository.save(saved);
        
        Optional<Student> updated = studentRepository.findById(saved.getId());
        assertTrue(updated.isPresent());
        assertEquals(3.8, updated.get().getGpa());
    }
    
    @Test
    @DisplayName("Should delete student")
    public void testDeleteStudent() {
        Student student = new Student("STU001", "Alice", "alice@school.edu");
        Student saved = studentRepository.save(student);
        
        studentRepository.deleteById(saved.getId());
        
        Optional<Student> deleted = studentRepository.findById(saved.getId());
        assertFalse(deleted.isPresent());
    }
}
