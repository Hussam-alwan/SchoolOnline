package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.entity.Enrollment;
import com.bootcamp.onlineschool.entity.EnrollmentStatus;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.model.Student;
import com.bootcamp.onlineschool.repository.CourseRepository;
import com.bootcamp.onlineschool.repository.EnrollmentRepository;
import com.bootcamp.onlineschool.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository,
                             CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public Enrollment enrollStudent(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        enrollmentRepository.findByStudent_IdAndCourse_Id(studentId, courseId)
                .ifPresent(e -> {
                    throw new DuplicateEnrollmentException(
                            "Student " + studentId + " is already enrolled in course " + courseId);
                });

        Enrollment enrollment = new Enrollment(student, course, LocalDate.now(), EnrollmentStatus.ENROLLED);
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment dropCourse(Long enrollmentId) {
        Enrollment enrollment = getEnrollment(enrollmentId);
        enrollment.setStatus(EnrollmentStatus.DROPPED);
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment completeEnrollment(Long enrollmentId, String grade) {
        if (grade == null || !grade.matches("[ABCDF]")) {
            throw new IllegalArgumentException("Grade must be one of A, B, C, D, F");
        }
        Enrollment enrollment = getEnrollment(enrollmentId);
        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        enrollment.setGrade(grade);
        enrollment.setCompletionDate(LocalDate.now());
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment withdrawEnrollment(Long enrollmentId) {
        Enrollment enrollment = getEnrollment(enrollmentId);
        enrollment.setStatus(EnrollmentStatus.WITHDRAWN);
        return enrollmentRepository.save(enrollment);
    }

    @Transactional(readOnly = true)
    public Enrollment getEnrollment(Long enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + enrollmentId));
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getStudentEnrollments(Long studentId) {
        return enrollmentRepository.findByStudent_Id(studentId);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getCourseEnrollments(Long courseId) {
        return enrollmentRepository.findByCourse_Id(courseId);
    }

    @Transactional(readOnly = true)
    public long countByStatus(EnrollmentStatus status) {
        return enrollmentRepository.countByStatus(status);
    }

    public static class DuplicateEnrollmentException extends RuntimeException {
        public DuplicateEnrollmentException(String message) {
            super(message);
        }
    }
}
