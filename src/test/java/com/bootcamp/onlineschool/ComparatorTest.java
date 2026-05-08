package com.bootcamp.onlineschool;

import com.bootcamp.onlineschool.comparator.StudentEmailDomainComparator;
import com.bootcamp.onlineschool.comparator.StudentGpaNameComparator;
import com.bootcamp.onlineschool.comparator.StudentNameComparator;
import com.bootcamp.onlineschool.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Comparator Tests")
public class ComparatorTest {

    private StudentRegistry registry;
    private Student student1;
    private Student student2;
    private Student student3;
    private Student student4;

    @BeforeEach
    public void setUp() {
        registry = new StudentRegistry();
        student1 = new Student("STU001", "Charlie Brown", "charlie@school.edu", 3.8, 20);
        student2 = new Student("STU002", "Alice Johnson", "alice@school.edu", 3.8, 22);
        student3 = new Student("STU003", "Bob Smith", "bob@school.edu", 3.9, 21);
        student4 = new Student("STU004", "Diana Prince", "diana@school.edu", 3.5, 23);

        registry.addStudent(student1);
        registry.addStudent(student2);
        registry.addStudent(student3);
        registry.addStudent(student4);
    }

    // StudentNameComparator tests
    @Test
    @DisplayName("Should sort students by name case-insensitive")
    public void testNameComparator() {
        List<Student> sorted = registry.getAllStudentsSorted(new StudentNameComparator());
        assertEquals("Alice Johnson", sorted.get(0).getName());
        assertEquals("Bob Smith", sorted.get(1).getName());
        assertEquals("Charlie Brown", sorted.get(2).getName());
        assertEquals("Diana Prince", sorted.get(3).getName());
    }

    @Test
    @DisplayName("Should sort single student by name")
    public void testNameComparatorSingleStudent() {
        StudentRegistry singleRegistry = new StudentRegistry();
        singleRegistry.addStudent(student1);
        List<Student> sorted = singleRegistry.getAllStudentsSorted(new StudentNameComparator());
        assertEquals(1, sorted.size());
        assertEquals("Charlie Brown", sorted.get(0).getName());
    }

    @Test
    @DisplayName("Should return empty list for empty registry")
    public void testNameComparatorEmptyRegistry() {
        StudentRegistry emptyRegistry = new StudentRegistry();
        List<Student> sorted = emptyRegistry.getAllStudentsSorted(new StudentNameComparator());
        assertEquals(0, sorted.size());
    }

    // StudentGpaNameComparator tests
    @Test
    @DisplayName("Should sort students by GPA descending then name ascending")
    public void testGpaNameComparator() {
        List<Student> sorted = registry.getAllStudentsSorted(new StudentGpaNameComparator());
        assertEquals("Bob Smith", sorted.get(0).getName());
        assertEquals("Alice Johnson", sorted.get(1).getName());
        assertEquals("Charlie Brown", sorted.get(2).getName());
        assertEquals("Diana Prince", sorted.get(3).getName());
    }

    @Test
    @DisplayName("Should sort by name when GPA is equal")
    public void testGpaNameComparatorTieBreaking() {
        List<Student> sorted = registry.getAllStudentsSorted(new StudentGpaNameComparator());
        assertEquals(3.8, sorted.get(1).getGpa());
        assertEquals(3.8, sorted.get(2).getGpa());
        assertTrue(sorted.get(1).getName().compareToIgnoreCase(sorted.get(2).getName()) < 0);
    }

    @Test
    @DisplayName("Should sort single student by GPA and name")
    public void testGpaNameComparatorSingleStudent() {
        StudentRegistry singleRegistry = new StudentRegistry();
        singleRegistry.addStudent(student1);
        List<Student> sorted = singleRegistry.getAllStudentsSorted(new StudentGpaNameComparator());
        assertEquals(1, sorted.size());
    }

    // StudentEmailDomainComparator tests
    @Test
    @DisplayName("Should sort students by email domain then name ascending")
    public void testEmailDomainComparator() {
        List<Student> sorted = registry.getAllStudentsSorted(new StudentEmailDomainComparator());
        assertEquals("Alice Johnson", sorted.get(0).getName());
        assertEquals("Bob Smith", sorted.get(1).getName());
        assertEquals("Charlie Brown", sorted.get(2).getName());
        assertEquals("Diana Prince", sorted.get(3).getName());
    }

    @Test
    @DisplayName("Should return empty list for empty registry with domain comparator")
    public void testEmailDomainComparatorEmptyRegistry() {
        StudentRegistry emptyRegistry = new StudentRegistry();
        List<Student> sorted = emptyRegistry.getAllStudentsSorted(new StudentEmailDomainComparator());
        assertEquals(0, sorted.size());
    }

    @Test
    @DisplayName("Should sort single student with domain comparator")
    public void testEmailDomainComparatorSingleStudent() {
        StudentRegistry singleRegistry = new StudentRegistry();
        singleRegistry.addStudent(student1);
        List<Student> sorted = singleRegistry.getAllStudentsSorted(new StudentEmailDomainComparator());
        assertEquals(1, sorted.size());
    }

    // getAllStudentsSorted with lambda tests
    @Test
    @DisplayName("Should sort using lambda comparator")
    public void testGetAllStudentsSortedWithLambda() {
        List<Student> sorted = registry.getAllStudentsSorted(
                (s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
        assertEquals("Alice Johnson", sorted.get(0).getName());
        assertEquals("Bob Smith", sorted.get(1).getName());
    }
}