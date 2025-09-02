package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Teacher;
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
class TeacherRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher testTeacher1;
    private Teacher testTeacher2;
    private Teacher testTeacher3;

    @BeforeEach
    void setUp() {
        testTeacher1 = new Teacher("Prof. Wilson", "prof.wilson@example.com", "EMP001", "Computer Science", LocalDate.of(2020, 8, 1));
        testTeacher2 = new Teacher("Dr. Johnson", "dr.johnson@example.com", "EMP002", "Mathematics", LocalDate.of(2019, 9, 15));
        testTeacher3 = new Teacher("Prof. Davis", "prof.davis@example.com", "EMP003", "Computer Science", LocalDate.of(2021, 1, 10));
        
        entityManager.persistAndFlush(testTeacher1);
        entityManager.persistAndFlush(testTeacher2);
        entityManager.persistAndFlush(testTeacher3);
    }

    @Test
    void findByEmployeeId_ShouldReturnTeacher_WhenEmployeeIdExists() {
        // When
        Optional<Teacher> foundTeacher = teacherRepository.findByEmployeeId("EMP001");

        // Then
        assertThat(foundTeacher).isPresent();
        assertThat(foundTeacher.get().getName()).isEqualTo("Prof. Wilson");
        assertThat(foundTeacher.get().getEmployeeId()).isEqualTo("EMP001");
    }

    @Test
    void findByEmployeeId_ShouldReturnEmpty_WhenEmployeeIdDoesNotExist() {
        // When
        Optional<Teacher> foundTeacher = teacherRepository.findByEmployeeId("EMP999");

        // Then
        assertThat(foundTeacher).isEmpty();
    }

    @Test
    void existsByEmployeeId_ShouldReturnTrue_WhenEmployeeIdExists() {
        // When
        boolean exists = teacherRepository.existsByEmployeeId("EMP002");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmployeeId_ShouldReturnFalse_WhenEmployeeIdDoesNotExist() {
        // When
        boolean exists = teacherRepository.existsByEmployeeId("EMP999");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void findByEmail_ShouldReturnTeacher_WhenEmailExists() {
        // When
        Optional<Teacher> foundTeacher = teacherRepository.findByEmail("dr.johnson@example.com");

        // Then
        assertThat(foundTeacher).isPresent();
        assertThat(foundTeacher.get().getName()).isEqualTo("Dr. Johnson");
        assertThat(foundTeacher.get().getDepartment()).isEqualTo("Mathematics");
    }

    @Test
    void findTeachersByDepartment_ShouldReturnTeachersInDepartment() {
        // When
        List<Teacher> csTeachers = teacherRepository.findTeachersByDepartment("Computer Science");

        // Then
        assertThat(csTeachers).hasSize(2);
        assertThat(csTeachers).extracting(Teacher::getName)
                .containsExactlyInAnyOrder("Prof. Wilson", "Prof. Davis");
    }

    @Test
    void findTeachersByDepartment_ShouldReturnEmptyList_WhenNoDepartmentMatch() {
        // When
        List<Teacher> physicsTeachers = teacherRepository.findTeachersByDepartment("Physics");

        // Then
        assertThat(physicsTeachers).isEmpty();
    }

    @Test
    void findTeachersByDepartmentIgnoreCase_ShouldReturnTeachersIgnoringCase() {
        // When
        List<Teacher> csTeachers = teacherRepository.findTeachersByDepartmentIgnoreCase("computer science");

        // Then
        assertThat(csTeachers).hasSize(2);
        assertThat(csTeachers).extracting(Teacher::getName)
                .containsExactlyInAnyOrder("Prof. Wilson", "Prof. Davis");
    }

    @Test
    void findTeachersByDepartmentIgnoreCase_ShouldReturnTeachersWithMixedCase() {
        // When
        List<Teacher> mathTeachers = teacherRepository.findTeachersByDepartmentIgnoreCase("MATHEMATICS");

        // Then
        assertThat(mathTeachers).hasSize(1);
        assertThat(mathTeachers.get(0).getName()).isEqualTo("Dr. Johnson");
    }
}