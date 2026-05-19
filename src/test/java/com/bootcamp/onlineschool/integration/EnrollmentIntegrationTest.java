package com.bootcamp.onlineschool.integration;

import com.bootcamp.onlineschool.entity.Enrollment;
import com.bootcamp.onlineschool.entity.EnrollmentStatus;
import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.model.Student;
import com.bootcamp.onlineschool.repository.CourseRepository;
import com.bootcamp.onlineschool.repository.EnrollmentRepository;
import com.bootcamp.onlineschool.repository.StudentRepository;
import com.bootcamp.onlineschool.service.EnrollmentService;
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
@DisplayName("Enrollment Integration Tests")
class EnrollmentIntegrationTest {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        enrollmentRepository.deleteAll();
        studentRepository.deleteAll();
        courseRepository.deleteAll();
    }

    private Student saveStudent(String studentId, String name, String email) {
        return studentRepository.save(new Student(studentId, name, email));
    }

    private Course saveCourse(String name, int credits) {
        return courseRepository.save(new Course(name, credits));
    }

    @Test
    @DisplayName("Full lifecycle: enroll then complete with grade")
    void enrollThenComplete() {
        Student s = saveStudent("STU1", "Alice", "alice@school.edu");
        Course c = saveCourse("Algebra", 3);

        Enrollment e = enrollmentService.enrollStudent(s.getId(), c.getId());
        Enrollment completed = enrollmentService.completeEnrollment(e.getId(), "A");

        assertEquals(EnrollmentStatus.COMPLETED, completed.getStatus());
        assertEquals("A", completed.getGrade());
    }

    @Test
    @DisplayName("Enrolling 3 students in same course produces 3 enrollments")
    void threeStudentsOneCourse() {
        Student s1 = saveStudent("STU1", "Alice", "alice@school.edu");
        Student s2 = saveStudent("STU2", "Bob", "bob@school.edu");
        Student s3 = saveStudent("STU3", "Carol", "carol@school.edu");
        Course c = saveCourse("Algebra", 3);

        enrollmentService.enrollStudent(s1.getId(), c.getId());
        enrollmentService.enrollStudent(s2.getId(), c.getId());
        enrollmentService.enrollStudent(s3.getId(), c.getId());

        List<Enrollment> course = enrollmentService.getCourseEnrollments(c.getId());
        assertEquals(3, course.size());
    }

    @Test
    @DisplayName("One student in multiple courses")
    void oneStudentMultipleCourses() {
        Student s = saveStudent("STU1", "Alice", "alice@school.edu");
        Course c1 = saveCourse("Algebra", 3);
        Course c2 = saveCourse("Physics", 4);
        Course c3 = saveCourse("Chemistry", 3);

        enrollmentService.enrollStudent(s.getId(), c1.getId());
        enrollmentService.enrollStudent(s.getId(), c2.getId());
        enrollmentService.enrollStudent(s.getId(), c3.getId());

        List<Enrollment> student = enrollmentService.getStudentEnrollments(s.getId());
        assertEquals(3, student.size());
    }

    @Test
    @DisplayName("Mixed statuses count correctly")
    void mixedStatusCount() {
        Student s = saveStudent("STU1", "Alice", "alice@school.edu");
        Course c1 = saveCourse("Algebra", 3);
        Course c2 = saveCourse("Physics", 4);
        Course c3 = saveCourse("Chemistry", 3);

        Enrollment e1 = enrollmentService.enrollStudent(s.getId(), c1.getId());
        Enrollment e2 = enrollmentService.enrollStudent(s.getId(), c2.getId());
        Enrollment e3 = enrollmentService.enrollStudent(s.getId(), c3.getId());

        enrollmentService.completeEnrollment(e1.getId(), "A");
        enrollmentService.dropCourse(e2.getId());

        assertEquals(1L, enrollmentService.countByStatus(EnrollmentStatus.ENROLLED));
        assertEquals(1L, enrollmentService.countByStatus(EnrollmentStatus.COMPLETED));
        assertEquals(1L, enrollmentService.countByStatus(EnrollmentStatus.DROPPED));
    }

    @Test
    @DisplayName("Dropping a course preserves the enrollment row")
    void dropPreservesRow() {
        Student s = saveStudent("STU1", "Alice", "alice@school.edu");
        Course c = saveCourse("Algebra", 3);

        Enrollment e = enrollmentService.enrollStudent(s.getId(), c.getId());
        enrollmentService.dropCourse(e.getId());

        assertEquals(1, enrollmentRepository.count());
    }

    @Test
    @DisplayName("Deleting a student cascades to enrollments")
    void deleteStudentCascades() {
        Student s = saveStudent("STU1", "Alice", "alice@school.edu");
        Course c = saveCourse("Algebra", 3);
        enrollmentService.enrollStudent(s.getId(), c.getId());
        enrollmentRepository.flush();

        studentRepository.deleteById(s.getId());
        studentRepository.flush();

        assertEquals(0, enrollmentRepository.count());
    }

    @Test
    @DisplayName("Deleting a course cascades to enrollments")
    void deleteCourseCascades() {
        Student s = saveStudent("STU1", "Alice", "alice@school.edu");
        Course c = saveCourse("Algebra", 3);
        enrollmentService.enrollStudent(s.getId(), c.getId());
        enrollmentRepository.flush();

        courseRepository.deleteById(c.getId());
        courseRepository.flush();

        assertEquals(0, enrollmentRepository.count());
    }

    @Test
    @DisplayName("Re-enrolling after dropping creates a new row attempt that fails (unique constraint)")
    void duplicateEnrollmentBlocked() {
        Student s = saveStudent("STU1", "Alice", "alice@school.edu");
        Course c = saveCourse("Algebra", 3);

        Enrollment e = enrollmentService.enrollStudent(s.getId(), c.getId());
        enrollmentService.dropCourse(e.getId());

        assertThrows(EnrollmentService.DuplicateEnrollmentException.class,
                () -> enrollmentService.enrollStudent(s.getId(), c.getId()));
    }
}
