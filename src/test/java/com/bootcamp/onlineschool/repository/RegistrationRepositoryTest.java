package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Registration;
import com.bootcamp.onlineschool.entity.Student;
import com.bootcamp.onlineschool.entity.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RegistrationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RegistrationRepository registrationRepository;

    private Student testStudent1;
    private Student testStudent2;
    private Course testCourse1;
    private Course testCourse2;
    private Registration testRegistration1;
    private Registration testRegistration2;
    private Registration testRegistration3;

    @BeforeEach
    void setUp() {
        testStudent1 = new Student("John Doe", "john.doe@example.com", "STU001", LocalDate.of(2023, 1, 15));
        testStudent2 = new Student("Jane Smith", "jane.smith@example.com", "STU002", LocalDate.of(2023, 2, 20));
        testCourse1 = new Course("Java Programming", "Introduction to Java programming language", 3, 40);
        testCourse2 = new Course("Data Structures", "Fundamental data structures and algorithms", 4, 60);
        
        entityManager.persistAndFlush(testStudent1);
        entityManager.persistAndFlush(testStudent2);
        entityManager.persistAndFlush(testCourse1);
        entityManager.persistAndFlush(testCourse2);
        
        testRegistration1 = new Registration(LocalDate.of(2023, 8, 15), "ACTIVE", testStudent1, testCourse1);
        testRegistration1.setGrade("A");
        testRegistration2 = new Registration(LocalDate.of(2023, 8, 20), "COMPLETED", testStudent1, testCourse2);
        testRegistration2.setGrade("B+");
        testRegistration3 = new Registration(LocalDate.of(2023, 9, 1), "ACTIVE", testStudent2, testCourse1);
        
        entityManager.persistAndFlush(testRegistration1);
        entityManager.persistAndFlush(testRegistration2);
        entityManager.persistAndFlush(testRegistration3);
    }

    @Test
    void findByStudent_ShouldReturnRegistrationsForStudent() {
        // When
        List<Registration> student1Registrations = registrationRepository.findByStudent(testStudent1);

        // Then
        assertThat(student1Registrations).hasSize(2);
        assertThat(student1Registrations).extracting(Registration::getStatus)
                .containsExactlyInAnyOrder("ACTIVE", "COMPLETED");
    }

    @Test
    void findByStudentId_ShouldReturnRegistrationsForStudentId() {
        // When
        List<Registration> student2Registrations = registrationRepository.findByStudentId(testStudent2.getId());

        // Then
        assertThat(student2Registrations).hasSize(1);
        assertThat(student2Registrations.get(0).getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void findByCourse_ShouldReturnRegistrationsForCourse() {
        // When
        List<Registration> javaRegistrations = registrationRepository.findByCourse(testCourse1);

        // Then
        assertThat(javaRegistrations).hasSize(2);
        assertThat(javaRegistrations).extracting(r -> r.getStudent().getStudentId())
                .containsExactlyInAnyOrder("STU001", "STU002");
    }

    @Test
    void findByCourseId_ShouldReturnRegistrationsForCourseId() {
        // When
        List<Registration> dataStructuresRegistrations = registrationRepository.findByCourseId(testCourse2.getId());

        // Then
        assertThat(dataStructuresRegistrations).hasSize(1);
        assertThat(dataStructuresRegistrations.get(0).getStudent().getStudentId()).isEqualTo("STU001");
    }

    @Test
    void findByStatus_ShouldReturnRegistrationsWithStatus() {
        // When
        List<Registration> activeRegistrations = registrationRepository.findByStatus("ACTIVE");

        // Then
        assertThat(activeRegistrations).hasSize(2);
        assertThat(activeRegistrations).extracting(r -> r.getStudent().getStudentId())
                .containsExactlyInAnyOrder("STU001", "STU002");
    }

    @Test
    void findByStudentAndCourse_ShouldReturnRegistration_WhenExists() {
        // When
        Optional<Registration> registration = registrationRepository.findByStudentAndCourse(testStudent1, testCourse1);

        // Then
        assertThat(registration).isPresent();
        assertThat(registration.get().getStatus()).isEqualTo("ACTIVE");
        assertThat(registration.get().getGrade()).isEqualTo("A");
    }

    @Test
    void findByStudentAndCourse_ShouldReturnEmpty_WhenNotExists() {
        // When
        Optional<Registration> registration = registrationRepository.findByStudentAndCourse(testStudent2, testCourse2);

        // Then
        assertThat(registration).isEmpty();
    }

    @Test
    void findByStudentIdAndCourseId_ShouldReturnRegistration_WhenExists() {
        // When
        Optional<Registration> registration = registrationRepository.findByStudentIdAndCourseId(testStudent1.getId(), testCourse2.getId());

        // Then
        assertThat(registration).isPresent();
        assertThat(registration.get().getStatus()).isEqualTo("COMPLETED");
        assertThat(registration.get().getGrade()).isEqualTo("B+");
    }

    @Test
    void existsByStudentAndCourse_ShouldReturnTrue_WhenExists() {
        // When
        boolean exists = registrationRepository.existsByStudentAndCourse(testStudent1, testCourse1);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByStudentAndCourse_ShouldReturnFalse_WhenNotExists() {
        // When
        boolean exists = registrationRepository.existsByStudentAndCourse(testStudent2, testCourse2);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void findRegistrationsByStatus_ShouldReturnRegistrationsWithStatus() {
        // When
        List<Registration> completedRegistrations = registrationRepository.findRegistrationsByStatus("COMPLETED");

        // Then
        assertThat(completedRegistrations).hasSize(1);
        assertThat(completedRegistrations.get(0).getStudent().getStudentId()).isEqualTo("STU001");
    }

    @Test
    void findByStatusIgnoreCase_ShouldReturnRegistrationsIgnoringCase() {
        // When
        List<Registration> activeRegistrations = registrationRepository.findByStatusIgnoreCase("active");

        // Then
        assertThat(activeRegistrations).hasSize(2);
    }

    @Test
    void findRegistrationsWithGrades_ShouldReturnRegistrationsWithGrades() {
        // When
        List<Registration> registrationsWithGrades = registrationRepository.findRegistrationsWithGrades();

        // Then
        assertThat(registrationsWithGrades).hasSize(2);
        assertThat(registrationsWithGrades).extracting(Registration::getGrade)
                .containsExactlyInAnyOrder("A", "B+");
    }

    @Test
    void findRegistrationsWithoutGrades_ShouldReturnRegistrationsWithoutGrades() {
        // When
        List<Registration> registrationsWithoutGrades = registrationRepository.findRegistrationsWithoutGrades();

        // Then
        assertThat(registrationsWithoutGrades).hasSize(1);
        assertThat(registrationsWithoutGrades.get(0).getStudent().getStudentId()).isEqualTo("STU002");
    }
}