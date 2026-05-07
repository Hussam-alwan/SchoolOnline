package com.bootcamp.onlineschool;

import com.bootcamp.onlineschool.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for StudentRegistry class
 * Demonstrates:
 * - Testing collections
 * - Testing search and filter operations
 * - Testing sorting
 * - Testing edge cases
 */
@DisplayName("StudentRegistry Class Tests")
public class StudentRegistryTest {
    
    private StudentRegistry registry;
    private Student student1;
    private Student student2;
    private Student student3;
    
    @BeforeEach
    public void setUp() {
        registry = new StudentRegistry();
        student1 = new Student("STU001", "Alice Johnson", "alice@school.edu", 3.8,17);
        student2 = new Student("STU002", "Bob Smith", "bob@school.edu", 3.5,18);
        student3 = new Student("STU003", "Charlie Brown", "charlie@school.edu", 3.9,19);
    }
    
    @Test
    @DisplayName("Should add students to registry")
    public void testAddStudent() {
        registry.addStudent(student1);
        assertEquals(1, registry.getStudentCount());
        
        registry.addStudent(student2);
        assertEquals(2, registry.getStudentCount());
    }
    
    @Test
    @DisplayName("Should throw exception when adding null student")
    public void testAddNullStudent() {
        assertThrows(IllegalArgumentException.class, () -> registry.addStudent(null));
    }
    
    @Test
    @DisplayName("Should throw exception when adding student with invalid email")
    public void testAddStudentWithInvalidEmail() {
        Student invalidStudent = new Student("STU004", "Invalid", "invalid-email");
        assertThrows(IllegalArgumentException.class, () -> registry.addStudent(invalidStudent));
    }
    
    @Test
    @DisplayName("Should find student by ID")
    public void testFindStudentById() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        
        Student found = registry.findStudentById("STU001");
        assertNotNull(found);
        assertEquals("Alice Johnson", found.getName());
        
        Student notFound = registry.findStudentById("STU999");
        assertNull(notFound);
    }
    
    @Test
    @DisplayName("Should find students by name")
    public void testFindStudentsByName() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);
        
        List<Student> results = registry.findStudentsByName("Charlie");
        assertEquals(1, results.size());
        assertEquals("Charlie Brown", results.get(0).getName());
        
        List<Student> allResults = registry.findStudentsByName("a");
        assertEquals(2, allResults.size()); // Alice and Charlie
    }
    
    @Test
    @DisplayName("Should remove student from registry")
    public void testRemoveStudent() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        assertEquals(2, registry.getStudentCount());
        
        boolean removed = registry.removeStudent("STU001");
        assertTrue(removed);
        assertEquals(1, registry.getStudentCount());
        
        boolean notRemoved = registry.removeStudent("STU999");
        assertFalse(notRemoved);
    }
    
    @Test
    @DisplayName("Should sort students by name")
    public void testSortByName() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);
        
        List<Student> sorted = registry.getAllStudentsSortedByName();
        assertEquals(3, sorted.size());
        assertEquals("Alice Johnson", sorted.get(0).getName());
        assertEquals("Bob Smith", sorted.get(1).getName());
        assertEquals("Charlie Brown", sorted.get(2).getName());
    }
    
    @Test
    @DisplayName("Should sort students by GPA descending")
    public void testSortByGpa() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);
        
        List<Student> sorted = registry.getAllStudentsSortedByGpa();
        assertEquals(3, sorted.size());
        assertEquals(3.9, sorted.get(0).getGpa());
        assertEquals(3.8, sorted.get(1).getGpa());
        assertEquals(3.5, sorted.get(2).getGpa());
    }
    
    @Test
    @DisplayName("Should filter students by GPA threshold")
    public void testGetStudentsWithHighGpa() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);
        
        List<Student> highGpa = registry.getStudentsWithHighGpa(3.7);
        assertEquals(2, highGpa.size());
        
        List<Student> veryHighGpa = registry.getStudentsWithHighGpa(3.9);
        assertEquals(1, veryHighGpa.size());
    }
    
    @Test
    @DisplayName("Should calculate average GPA")
    public void testGetAverageGpa() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);
        
        double average = registry.getAverageGpa();
        assertEquals((3.8 + 3.5 + 3.9) / 3, average, 0.01);
    }
    
    @Test
    @DisplayName("Should return 0 average GPA for empty registry")
    public void testGetAverageGpaEmpty() {
        assertEquals(0.0, registry.getAverageGpa());
    }
    
    @Test
    @DisplayName("Should clear all students")
    public void testClear() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        assertEquals(2, registry.getStudentCount());
        
        registry.clear();
        assertEquals(0, registry.getStudentCount());
    }
}
