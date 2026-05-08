package com.bootcamp.onlineschool;

import com.bootcamp.onlineschool.comparator.StudentEmailDomainComparator;
import com.bootcamp.onlineschool.comparator.StudentGpaNameComparator;
import com.bootcamp.onlineschool.comparator.StudentNameComparator;
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
        studentMap.put(student.getId(), student);
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
     * Find a student by email (case-insensitive)
     */
    public Student findByEmail(String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }
        return students.stream()
                .filter(s -> s.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
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
                .sorted(new StudentNameComparator())
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
                .filter(s -> s.getGpa() > threshold)
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

    /**
     * Find students by GPA range (inclusive)
     */
    public List<Student> findStudentsByGpaRange(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("Min GPA cannot be greater than max GPA");
        }
        return students.stream()
                .filter(s -> s.getGpa() >= min && s.getGpa() <= max)
                .collect(Collectors.toList());
    }

    /**
     * Find students by email domain
     */
    public List<Student> findStudentsByEmailDomain(String domain) {
        if (domain == null || domain.isEmpty()) {
            return new ArrayList<>();
        }
        return students.stream()
                .filter(s -> s.getEmail().endsWith(domain))
                .collect(Collectors.toList());
    }

    public List<Student> getAllStudentsSorted(Comparator<Student> comparator) {
        return students.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public List<Student> getAllStudentsSortedByGpaName() {
        return students.stream()
                .sorted(new StudentGpaNameComparator())
                .collect(Collectors.toList());
    }

    public List<Student> getAllStudentsSortedByEmailDomain() {
        return students.stream()
                .sorted(new StudentEmailDomainComparator())
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getGpaDistribution() {
        Map<String, Integer> distribution = new LinkedHashMap<>();
        distribution.put("A", 0);
        distribution.put("B", 0);
        distribution.put("C", 0);
        distribution.put("D", 0);
        distribution.put("F", 0);

        for (Student student : students) {
            double gpa = student.getGpa();
            if (gpa >= 3.7) {
                distribution.put("A", distribution.get("A") + 1);
            } else if (gpa >= 2.7) {
                distribution.put("B", distribution.get("B") + 1);
            } else if (gpa >= 1.7) {
                distribution.put("C", distribution.get("C") + 1);
            } else if (gpa >= 1.0) {
                distribution.put("D", distribution.get("D") + 1);
            } else {
                distribution.put("F", distribution.get("F") + 1);
            }
        }
        return distribution;
    }

    public List<Student> getTopNStudents(int n) {
        if (n<=0) return new ArrayList<>();
        return students.stream()
                .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public List<Student> getStudentsByGpaPercentile(double percentile) {
        if (students.isEmpty()) return new ArrayList<>();
        if (percentile < 0 || percentile > 100) return new ArrayList<>();

        double threshold = (percentile / 100.0) * 4.0;
        return students.stream()
                .filter(s -> s.getGpa() >= threshold)
                .collect(Collectors.toList());
    }
}