package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Course;
import com.bootcamp.onlineschool.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Repository Integration Tests")
public class RepositoryIntegrationTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void setup() {

        studentRepository.save(new Student("STU001", "Alice Johnson", "alice@school.edu", 3.9));
        studentRepository.save(new Student("STU002", "Bob Smith", "bob@school.edu", 3.5));
        studentRepository.save(new Student("STU003", "Carol White", "carol@gmail.com", 2.8));
        studentRepository.save(new Student("STU004", "David Brown", "david@gmail.com", 3.7));
        studentRepository.save(new Student("STU005", "Eve Davis", "eve@school.edu", 3.2));

        Student s1 = studentRepository.findByStudentId("STU001").get();
        s1.setEnrollmentDate(LocalDate.of(2021, 9, 1));
        studentRepository.save(s1);

        Student s2 = studentRepository.findByStudentId("STU002").get();
        s2.setEnrollmentDate(LocalDate.of(2021, 9, 1));
        studentRepository.save(s2);

        Student s3 = studentRepository.findByStudentId("STU003").get();
        s3.setEnrollmentDate(LocalDate.of(2022, 9, 1));
        studentRepository.save(s3);

        Student s4 = studentRepository.findByStudentId("STU004").get();
        s4.setEnrollmentDate(LocalDate.of(2022, 9, 1));
        studentRepository.save(s4);

        Student s5 = studentRepository.findByStudentId("STU005").get();
        s5.setEnrollmentDate(LocalDate.of(2023, 9, 1));
        studentRepository.save(s5);

        Course c1 = new Course("CS101", "Java Basics", 3, "Dr. Smith", 30);
        c1.setEnrolledStudents(28);
        courseRepository.save(c1);

        Course c2 = new Course("CS102", "Data Structures", 4, "Dr. Brown", 25);
        c2.setEnrolledStudents(10);
        courseRepository.save(c2);

        Course c3 = new Course("CS103", "Web Development", 3, "Dr. Smith", 20);
        c3.setEnrolledStudents(20); // 100% full
        courseRepository.save(c3);
    }

    @Test
    @DisplayName("Should find students enrolled in 2021")
    void shouldFindStudentsByEnrollmentYear() {
        List<Student> students = studentRepository.findStudentsByEnrollmentYear(2021);

        assertEquals(2, students.size());
        assertTrue(students.stream().anyMatch(s -> s.getName().equals("Alice Johnson")));
        assertTrue(students.stream().anyMatch(s -> s.getName().equals("Bob Smith")));
    }

    @Test
    @DisplayName("Should find students with GPA between 3.5 and 3.9")
    void shouldFindStudentsByGpaRange() {
        List<Student> students = studentRepository.findStudentsByGpaRange(3.5, 3.9);

        assertEquals(3, students.size());
        assertTrue(students.stream().anyMatch(s -> s.getName().equals("Alice Johnson")));
        assertTrue(students.stream().anyMatch(s -> s.getName().equals("Bob Smith")));
        assertTrue(students.stream().anyMatch(s -> s.getName().equals("David Brown")));
    }

    @Test
    @DisplayName("Should count students enrolled in 2022")
    void shouldCountStudentsByEnrollmentYear() {
        Long count = studentRepository.countStudentsByEnrollmentYear(2022);

        assertEquals(2L, count);
    }

    @Test
    @DisplayName("Should find students with school.edu email domain")
    void shouldFindStudentsByEmailDomain() {
        List<Student> students = studentRepository.findStudentsByDomain("school.edu");

        assertEquals(3, students.size());
        assertTrue(students.stream().anyMatch(s -> s.getName().equals("Alice Johnson")));
        assertTrue(students.stream().anyMatch(s -> s.getName().equals("Bob Smith")));
        assertTrue(students.stream().anyMatch(s -> s.getName().equals("Eve Davis")));
    }

    @Test
    @DisplayName("Should return top 3 students by GPA")
    void shouldFindTopStudentsByGpa() {
        List<Student> top3 = studentRepository.findStudentsOrderByGpaDesc(PageRequest.of(0, 3));

        assertEquals(3, top3.size());
        assertEquals("Alice Johnson", top3.get(0).getName()); // GPA 3.9
        assertEquals("David Brown", top3.get(1).getName());   // GPA 3.7
        assertEquals("Bob Smith", top3.get(2).getName());     // GPA 3.5
    }

    @Test
    @DisplayName("Should find courses with enrollment rate above 0.8")
    void shouldFindCoursesAboveEnrollmentThreshold() {
        List<Course> courses = courseRepository.findCoursesWithThreshold(0.8);

        assertEquals(2, courses.size());
        assertTrue(courses.stream().anyMatch(c -> c.getCourseId().equals("CS101")));
        assertTrue(courses.stream().anyMatch(c -> c.getCourseId().equals("CS103")));
    }

    @Test
    @DisplayName("Should find courses ordered by enrollment descending")
    void shouldFindCoursesByPopularity() {
        List<Course> courses = courseRepository.findCoursesWithOrderByEnrolledStudents();

        assertEquals(3, courses.size());
        assertEquals("CS101", courses.get(0).getCourseId());
        assertEquals("CS103", courses.get(1).getCourseId());
        assertEquals("CS102", courses.get(2).getCourseId());
    }

    @Test
    @DisplayName("Should get total enrollment across all courses")
    void shouldGetTotalEnrollment() {
        Long total = courseRepository.getTotalEnrollment();

        assertEquals(58L, total);
    }

    @Test
    @DisplayName("Should find available courses")
    void shouldFindAvailableCourses() {
        List<Course> available = courseRepository.findAvailableCourses();

        assertEquals(2, available.size());
        assertTrue(available.stream().anyMatch(c -> c.getCourseId().equals("CS101")));
        assertTrue(available.stream().anyMatch(c -> c.getCourseId().equals("CS102")));
    }

    @Test
    @DisplayName("Should return empty list for enrollment year with no students")
    void shouldReturnEmptyForUnknownEnrollmentYear() {
        List<Student> students = studentRepository.findStudentsByEnrollmentYear(1999);

        assertTrue(students.isEmpty());
    }

    @Test
    @DisplayName("Should find full courses")
    void shouldFindFullCourses() {
        List<Course> fullCourses = courseRepository.findFullCourses();

        assertEquals(1, fullCourses.size());
        assertEquals("CS103", fullCourses.getFirst().getCourseId());
    }
}