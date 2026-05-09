package com.bootcamp.onlineschool;

import com.bootcamp.onlineschool.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

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
         student1 = new Student("STU001", "Alice Johnson", "alice@school1.edu", 4.0, 17);
         student2 = new Student("STU002", "Bob Smith",     "bob@school2.edu",   3.5, 18);
         student3 = new Student("STU003", "Charlie Brown", "charlie@school3.edu", 3.9, 19);
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

        assertNull(registry.findStudentById("STU999"));
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

        assertTrue(registry.removeStudent("STU001"));
        assertEquals(1, registry.getStudentCount());

        assertFalse(registry.removeStudent("STU999"));
    }
    
    @Test
    @DisplayName("Should sort students by name")
    public void testSortByName() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);

        List<Student> sorted = registry.getAllStudentsSortedByName();
        assertEquals(3, sorted.size());
        assertEquals("Alice Johnson",  sorted.get(0).getName());
        assertEquals("Bob Smith",      sorted.get(1).getName());
        assertEquals("Charlie Brown",  sorted.get(2).getName());
    }

    @Test
    @DisplayName("Should sort students by GPA descending")
    public void testSortByGpa() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);

        List<Student> sorted = registry.getAllStudentsSortedByGpa();
        assertEquals(3, sorted.size());
        assertEquals(4.0, sorted.get(0).getGpa()); // ✅ Alice
        assertEquals(3.9, sorted.get(1).getGpa()); // ✅ Charlie
        assertEquals(3.5, sorted.get(2).getGpa()); // ✅ Bob
    }

    @Test
    @DisplayName("Should filter students by GPA threshold")
    public void testGetStudentsWithHighGpa() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);

        assertEquals(2, registry.getStudentsWithHighGpa(3.7).size());
        assertEquals(1, registry.getStudentsWithHighGpa(3.9).size());
    }

    @Test
    @DisplayName("Should calculate average GPA")
    public void testGetAverageGpa() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);

        assertEquals((4.0 + 3.5 + 3.9) / 3, registry.getAverageGpa(), 0.01);
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

    @Test
    @DisplayName("Should find student by exact email")
    public void testFindByEmail() {
        registry.addStudent(student1);
        registry.addStudent(student2);

        Student found = registry.findByEmail("alice@school1.edu");
        assertNotNull(found);
        assertEquals(student1.getEmail(), found.getEmail());
    }

    @Test
    @DisplayName("Should find student by email case-insensitive")
    public void testFindByEmailCaseInsensitive() {
        registry.addStudent(student1);

        Student found = registry.findByEmail("ALICE@SCHOOL1.EDU");
        assertNotNull(found);
        assertEquals(student1.getEmail(), found.getEmail());
    }

    @Test
    @DisplayName("Should return null for non-existent email")
    public void testFindByEmailNotFound() {
        registry.addStudent(student1);
        assertNull(registry.findByEmail("notfound@school1.edu"));
    }

    @Test
    @DisplayName("Should return null for null email")
    public void testFindByEmailNull() {
        assertNull(registry.findByEmail(null));
    }

    @Test
    @DisplayName("Should return null for empty email")
    public void testFindByEmailEmpty() {
        assertNull(registry.findByEmail(""));
    }

    @Test
    @DisplayName("Should find students by GPA range")
    public void testFindByGpaRange() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);

        assertEquals(2, registry.findStudentsByGpaRange(3.9, 4.0).size()); // Charlie(3.9) and Alice(4.0)
    }

    @Test
    @DisplayName("Should match all students in full GPA range")
    public void testMatchesAllStudent() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);

        assertEquals(3, registry.findStudentsByGpaRange(0.0, 4.0).size());
    }

    @Test
    @DisplayName("Should return empty list when no matches")
    public void testNoMatches() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);

        assertEquals(0, registry.findStudentsByGpaRange(0.0, 1.0).size());
    }

    @Test
    @DisplayName("Should throw exception when min GPA is greater than max GPA")
    public void testMinGPAGreaterThanMaxGPA() {
        assertThrows(IllegalArgumentException.class, () ->
                registry.findStudentsByGpaRange(3.6, 2.0));
    }

    @Test
    @DisplayName("Should find students by email domain")
    public void testFindStudentsByEmailDomain() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);

        assertEquals(1, registry.findStudentsByEmailDomain("school1.edu").size());
        assertEquals(1, registry.findStudentsByEmailDomain("school2.edu").size());
        assertEquals(1, registry.findStudentsByEmailDomain("school3.edu").size());
    }

    @Test
    @DisplayName("Should return empty list for null domain")
    public void testFindStudentsByEmailDomainNull() {
        registry.addStudent(student1);
        assertEquals(0, registry.findStudentsByEmailDomain(null).size());
    }

    @Test
    @DisplayName("Should return empty list for empty registry")
    public void testFindStudentsByGpaRangeEmptyRegistry() {
        assertEquals(0, registry.findStudentsByGpaRange(0.0, 4.0).size());
    }

    @Test
    @DisplayName("Should return correct GPA distribution")
    public void testGpaDistribution() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);

        Map<String, Integer> dist = registry.getGpaDistribution();
        assertEquals(2, dist.get("A"));
        assertEquals(1, dist.get("B"));
        assertEquals(0, dist.get("C"));
        assertEquals(0, dist.get("D"));
        assertEquals(0, dist.get("F"));
    }

    @Test
    @DisplayName("Should count multiple students in same grade")
    public void testGpaDistributionMultipleSameGrade() {
        StudentRegistry r = new StudentRegistry();
        r.addStudent(new Student("S1", "Alice",   "alice@school1.edu",   4.0, 20));
        r.addStudent(new Student("S2", "Bob",     "bob@school2.edu",     3.8, 21));
        r.addStudent(new Student("S3", "Charlie", "charlie@school3.edu", 3.7, 22));
        assertEquals(3, r.getGpaDistribution().get("A"));
    }

    @Test
    @DisplayName("Should return top 3 students by GPA")
    public void testGetTopStudents() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);
        List<Student> top = registry.getTopNStudents(3);
        assertEquals(3, top.size());
        assertEquals("Alice Johnson",  top.get(0).getName());
        assertEquals("Charlie Brown",  top.get(1).getName());
        assertEquals("Bob Smith",      top.get(2).getName());
    }

    @Test
    @DisplayName("Should return correct students at 50th percentile")
    public void testGpaPercentile50() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);
        List<Student> result = registry.getStudentsByGpaPercentile(50);
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Should return top student at 100th percentile")
    public void testGpaPercentile100() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);
        List<Student> result = registry.getStudentsByGpaPercentile(100);
        assertEquals(1, result.size());
        assertEquals("Alice Johnson", result.get(0).getName());
    }

    @Test
    @DisplayName("Should return empty list for empty registry")
    public void testGpaPercentileEmptyRegistry() {
        assertEquals(0, new StudentRegistry().getStudentsByGpaPercentile(50).size());
    }

    @Test
    @DisplayName("Should return empty list for invalid percentile")
    public void testGpaPercentileInvalid() {
        assertEquals(0, registry.getStudentsByGpaPercentile(-1).size());
        assertEquals(0, registry.getStudentsByGpaPercentile(101).size());
    }

    @Test
    @DisplayName("Should return correct students at 75th percentile")
    public void testGpaPercentile75() {
        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);
        List<Student> result = registry.getStudentsByGpaPercentile(75);
        assertEquals(3, result.size());
    }
}