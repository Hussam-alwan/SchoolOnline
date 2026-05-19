package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Enrollment;
import com.bootcamp.onlineschool.entity.EnrollmentStatus;
import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@DisplayName("EnrollmentRepository Tests")
class EnrollmentRepositoryTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Student student1;
    private Student student2;
    private Course course1;
    private Course course2;

    @BeforeEach
    void setUp() {
        enrollmentRepository.deleteAll();
        studentRepository.deleteAll();
        courseRepository.deleteAll();

        student1 = studentRepository.save(new Student("STU1", "Alice", "alice@school.edu"));
        student2 = studentRepository.save(new Student("STU2", "Bob", "bob@school.edu"));
        course1 = courseRepository.save(new Course("Algebra", 3));
        course2 = courseRepository.save(new Course("Physics", 4));
    }

    private Enrollment enroll(Student s, Course c, LocalDate date, EnrollmentStatus status) {
        return enrollmentRepository.save(new Enrollment(s, c, date, status));
    }

    @Test
    @DisplayName("save persists with generated id")
    void savePersists() {
        Enrollment e = enroll(student1, course1, LocalDate.of(2026, 1, 15), EnrollmentStatus.ENROLLED);
        assertNotNull(e.getId());
    }

    @Test
    @DisplayName("findByStudent_Id returns enrollments for that student")
    void findByStudentId() {
        enroll(student1, course1, LocalDate.now(), EnrollmentStatus.ENROLLED);
        enroll(student1, course2, LocalDate.now(), EnrollmentStatus.ENROLLED);
        enroll(student2, course1, LocalDate.now(), EnrollmentStatus.ENROLLED);

        List<Enrollment> result = enrollmentRepository.findByStudent_Id(student1.getId());
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("findByCourse_Id returns enrollments for that course")
    void findByCourseId() {
        enroll(student1, course1, LocalDate.now(), EnrollmentStatus.ENROLLED);
        enroll(student2, course1, LocalDate.now(), EnrollmentStatus.ENROLLED);
        enroll(student1, course2, LocalDate.now(), EnrollmentStatus.ENROLLED);

        List<Enrollment> result = enrollmentRepository.findByCourse_Id(course1.getId());
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("findByStatus filters by status")
    void findByStatus() {
        enroll(student1, course1, LocalDate.now(), EnrollmentStatus.ENROLLED);
        enroll(student1, course2, LocalDate.now(), EnrollmentStatus.COMPLETED);
        enroll(student2, course1, LocalDate.now(), EnrollmentStatus.DROPPED);

        List<Enrollment> enrolled = enrollmentRepository.findByStatus(EnrollmentStatus.ENROLLED);
        assertEquals(1, enrolled.size());
    }

    @Test
    @DisplayName("findByStudent_IdAndCourse_Id returns the matching enrollment")
    void findByStudentAndCourse() {
        enroll(student1, course1, LocalDate.now(), EnrollmentStatus.ENROLLED);

        Optional<Enrollment> result = enrollmentRepository
                .findByStudent_IdAndCourse_Id(student1.getId(), course1.getId());
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("findByStudent_IdAndCourse_Id returns empty when no enrollment")
    void findByStudentAndCourseEmpty() {
        Optional<Enrollment> result = enrollmentRepository
                .findByStudent_IdAndCourse_Id(student1.getId(), course1.getId());
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findByEnrollmentDateBetween filters by date range")
    void findByDateRange() {
        enroll(student1, course1, LocalDate.of(2026, 1, 10), EnrollmentStatus.ENROLLED);
        enroll(student1, course2, LocalDate.of(2026, 2, 15), EnrollmentStatus.ENROLLED);
        enroll(student2, course1, LocalDate.of(2026, 3, 20), EnrollmentStatus.ENROLLED);

        List<Enrollment> result = enrollmentRepository.findByEnrollmentDateBetween(
                LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 28));
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findByGrade filters by grade")
    void findByGrade() {
        Enrollment e1 = enroll(student1, course1, LocalDate.now(), EnrollmentStatus.COMPLETED);
        e1.setGrade("A");
        enrollmentRepository.save(e1);

        Enrollment e2 = enroll(student2, course1, LocalDate.now(), EnrollmentStatus.COMPLETED);
        e2.setGrade("B");
        enrollmentRepository.save(e2);

        List<Enrollment> result = enrollmentRepository.findByGrade("A");
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("countByStatus returns count of enrollments in status")
    void countByStatus() {
        enroll(student1, course1, LocalDate.now(), EnrollmentStatus.ENROLLED);
        enroll(student1, course2, LocalDate.now(), EnrollmentStatus.ENROLLED);
        enroll(student2, course1, LocalDate.now(), EnrollmentStatus.COMPLETED);

        long count = enrollmentRepository.countByStatus(EnrollmentStatus.ENROLLED);
        assertEquals(2L, count);
    }

    @Test
    @DisplayName("findByCourseIdAndStatus filters by both")
    void findByCourseIdAndStatus() {
        enroll(student1, course1, LocalDate.now(), EnrollmentStatus.ENROLLED);
        enroll(student2, course1, LocalDate.now(), EnrollmentStatus.COMPLETED);
        enroll(student1, course2, LocalDate.now(), EnrollmentStatus.ENROLLED);

        List<Enrollment> result = enrollmentRepository.findByCourseIdAndStatus(
                course1.getId(), EnrollmentStatus.ENROLLED);
        assertEquals(1, result.size());
        assertEquals(student1.getId(), result.get(0).getStudent().getId());
    }

    @Test
    @DisplayName("unique (student, course) constraint prevents duplicates")
    void uniqueConstraint() {
        enroll(student1, course1, LocalDate.now(), EnrollmentStatus.ENROLLED);
        assertThrows(Exception.class, () -> {
            enroll(student1, course1, LocalDate.now(), EnrollmentStatus.ENROLLED);
            enrollmentRepository.flush();
        });
    }

    @Test
    @DisplayName("deleting student cascades to enrollments (orphanRemoval)")
    void deleteStudentCascades() {
        enroll(student1, course1, LocalDate.now(), EnrollmentStatus.ENROLLED);
        enroll(student1, course2, LocalDate.now(), EnrollmentStatus.ENROLLED);
        enrollmentRepository.flush();
        assertEquals(2, enrollmentRepository.count());

        studentRepository.delete(student1);
        studentRepository.flush();

        assertEquals(0, enrollmentRepository.count());
    }
}
