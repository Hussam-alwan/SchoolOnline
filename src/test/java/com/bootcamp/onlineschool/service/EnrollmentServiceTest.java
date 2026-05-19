package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.entity.Enrollment;
import com.bootcamp.onlineschool.entity.EnrollmentStatus;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.model.Student;
import com.bootcamp.onlineschool.repository.CourseRepository;
import com.bootcamp.onlineschool.repository.EnrollmentRepository;
import com.bootcamp.onlineschool.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("EnrollmentService Tests")
class EnrollmentServiceTest {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Student student;
    private Course course;

    @BeforeEach
    void setUp() {
        enrollmentRepository.deleteAll();
        studentRepository.deleteAll();
        courseRepository.deleteAll();

        student = studentRepository.save(new Student("STU1", "Alice", "alice@school.edu"));
        course = courseRepository.save(new Course("Algebra", 3));
    }

    @Test
    @DisplayName("enrollStudent creates an ENROLLED enrollment")
    void enrollStudentCreates() {
        Enrollment e = enrollmentService.enrollStudent(student.getId(), course.getId());
        assertNotNull(e.getId());
        assertEquals(EnrollmentStatus.ENROLLED, e.getStatus());
        assertNotNull(e.getEnrollmentDate());
    }

    @Test
    @DisplayName("enrollStudent throws when student missing")
    void enrollMissingStudent() {
        assertThrows(ResourceNotFoundException.class,
                () -> enrollmentService.enrollStudent(9999L, course.getId()));
    }

    @Test
    @DisplayName("enrollStudent throws when course missing")
    void enrollMissingCourse() {
        assertThrows(ResourceNotFoundException.class,
                () -> enrollmentService.enrollStudent(student.getId(), 9999L));
    }

    @Test
    @DisplayName("enrollStudent throws on duplicate enrollment")
    void duplicateEnrollmentFails() {
        enrollmentService.enrollStudent(student.getId(), course.getId());
        assertThrows(EnrollmentService.DuplicateEnrollmentException.class,
                () -> enrollmentService.enrollStudent(student.getId(), course.getId()));
    }

    @Test
    @DisplayName("dropCourse sets status to DROPPED")
    void dropCourseUpdatesStatus() {
        Enrollment e = enrollmentService.enrollStudent(student.getId(), course.getId());

        Enrollment dropped = enrollmentService.dropCourse(e.getId());

        assertEquals(EnrollmentStatus.DROPPED, dropped.getStatus());
    }

    @Test
    @DisplayName("dropCourse throws when enrollment missing")
    void dropMissingEnrollment() {
        assertThrows(ResourceNotFoundException.class,
                () -> enrollmentService.dropCourse(9999L));
    }

    @Test
    @DisplayName("completeEnrollment sets status, grade, and completion date")
    void completeEnrollmentUpdates() {
        Enrollment e = enrollmentService.enrollStudent(student.getId(), course.getId());

        Enrollment completed = enrollmentService.completeEnrollment(e.getId(), "A");

        assertEquals(EnrollmentStatus.COMPLETED, completed.getStatus());
        assertEquals("A", completed.getGrade());
        assertNotNull(completed.getCompletionDate());
    }

    @Test
    @DisplayName("completeEnrollment rejects invalid grade")
    void completeRejectsInvalidGrade() {
        Enrollment e = enrollmentService.enrollStudent(student.getId(), course.getId());

        assertThrows(IllegalArgumentException.class,
                () -> enrollmentService.completeEnrollment(e.getId(), "Z"));
    }

    @Test
    @DisplayName("withdrawEnrollment sets status to WITHDRAWN")
    void withdrawSetsStatus() {
        Enrollment e = enrollmentService.enrollStudent(student.getId(), course.getId());

        Enrollment withdrawn = enrollmentService.withdrawEnrollment(e.getId());

        assertEquals(EnrollmentStatus.WITHDRAWN, withdrawn.getStatus());
    }

    @Test
    @DisplayName("getStudentEnrollments returns all enrollments for student")
    void getStudentEnrollments() {
        Course course2 = courseRepository.save(new Course("Physics", 4));
        enrollmentService.enrollStudent(student.getId(), course.getId());
        enrollmentService.enrollStudent(student.getId(), course2.getId());

        List<Enrollment> result = enrollmentService.getStudentEnrollments(student.getId());

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("getCourseEnrollments returns all enrollments for course")
    void getCourseEnrollments() {
        Student student2 = studentRepository.save(new Student("STU2", "Bob", "bob@school.edu"));
        enrollmentService.enrollStudent(student.getId(), course.getId());
        enrollmentService.enrollStudent(student2.getId(), course.getId());

        List<Enrollment> result = enrollmentService.getCourseEnrollments(course.getId());

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("countByStatus reports count")
    void countByStatusReports() {
        enrollmentService.enrollStudent(student.getId(), course.getId());

        assertEquals(1L, enrollmentService.countByStatus(EnrollmentStatus.ENROLLED));
        assertEquals(0L, enrollmentService.countByStatus(EnrollmentStatus.DROPPED));
    }
}
