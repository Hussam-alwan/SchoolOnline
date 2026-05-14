package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

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
        testTeacher1.setYearsOfExperience(5);
        testTeacher1.setSalary(60000.0);

        testTeacher2 = new Teacher("Dr. Johnson", "dr.johnson@example.com", "EMP002", "Mathematics", LocalDate.of(2019, 9, 15));
        testTeacher2.setYearsOfExperience(10);
        testTeacher2.setSalary(80000.0);

        testTeacher3 = new Teacher("Prof. Davis", "prof.davis@example.com", "EMP003", "Computer Science", LocalDate.of(2021, 1, 10));
        testTeacher3.setYearsOfExperience(3);
        testTeacher3.setSalary(55000.0);

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

    // ---------------------------------------------------------------
    // Homework Assignment 1 — additional repository tests
    // ---------------------------------------------------------------

    @Test
    void findByDepartment_ShouldReturnTeachersInDepartment() {
        List<Teacher> csTeachers = teacherRepository.findByDepartment("Computer Science");

        assertThat(csTeachers).hasSize(2);
        assertThat(csTeachers).extracting(Teacher::getEmployeeId)
                .containsExactlyInAnyOrder("EMP001", "EMP003");
    }

    @Test
    void findByYearsOfExperienceGreaterThanEqual_ShouldReturnExperiencedTeachers() {
        List<Teacher> experienced = teacherRepository.findByYearsOfExperienceGreaterThanEqual(5);

        assertThat(experienced).hasSize(2);
        assertThat(experienced).extracting(Teacher::getName)
                .containsExactlyInAnyOrder("Prof. Wilson", "Dr. Johnson");
    }

    @Test
    void findBySalaryBetween_ShouldReturnTeachersInSalaryRange() {
        List<Teacher> midRange = teacherRepository.findBySalaryBetween(50000.0, 70000.0);

        assertThat(midRange).hasSize(2);
        assertThat(midRange).extracting(Teacher::getEmployeeId)
                .containsExactlyInAnyOrder("EMP001", "EMP003");
    }

    @Test
    void findAllOrderByNameAsc_ShouldReturnTeachersSortedByName() {
        List<Teacher> sorted = teacherRepository.findAllOrderByNameAsc();

        assertThat(sorted).hasSize(3);
        assertThat(sorted).extracting(Teacher::getName)
                .containsExactly("Dr. Johnson", "Prof. Davis", "Prof. Wilson");
    }

    @Test
    void findHiredAfter_ShouldReturnTeachersHiredAfterGivenDate() {
        List<Teacher> recent = teacherRepository.findHiredAfter(LocalDate.of(2020, 1, 1));

        assertThat(recent).hasSize(2);
        assertThat(recent).extracting(Teacher::getEmployeeId)
                .containsExactlyInAnyOrder("EMP001", "EMP003");
    }

    @Test
    void countByDepartment_ShouldReturnCorrectCount() {
        long csCount = teacherRepository.countByDepartment("Computer Science");
        long mathCount = teacherRepository.countByDepartment("Mathematics");
        long physicsCount = teacherRepository.countByDepartment("Physics");

        assertThat(csCount).isEqualTo(2);
        assertThat(mathCount).isEqualTo(1);
        assertThat(physicsCount).isZero();
    }

    @Test
    void getAverageSalaryByDepartment_ShouldReturnAverageSalary() {
        Double csAverage = teacherRepository.getAverageSalaryByDepartment("Computer Science");

        // (60000 + 55000) / 2 = 57500
        assertThat(csAverage).isEqualTo(57500.0);
    }

    @Test
    void findTopHighestPaid_ShouldReturnTeachersSortedBySalaryDesc() {
        List<Teacher> top2 = teacherRepository.findTopHighestPaid(PageRequest.of(0, 2));

        assertThat(top2).hasSize(2);
        assertThat(top2.get(0).getEmployeeId()).isEqualTo("EMP002");
        assertThat(top2.get(1).getEmployeeId()).isEqualTo("EMP001");
    }

    @Test
    void save_ShouldPersistNewTeacherWithGeneratedId() {
        Teacher newTeacher = new Teacher("Dr. Newton", "newton@example.com",
                "EMP004", "Physics", LocalDate.of(2022, 3, 1), 2, 50000.0);

        Teacher saved = teacherRepository.save(newTeacher);

        assertThat(saved.getId()).isNotNull();
        assertThat(teacherRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void update_ShouldPersistFieldChanges() {
        Teacher teacher = teacherRepository.findByEmployeeId("EMP001").orElseThrow();
        teacher.setSalary(65000.0);
        teacher.setYearsOfExperience(6);
        teacherRepository.saveAndFlush(teacher);

        Teacher reloaded = teacherRepository.findByEmployeeId("EMP001").orElseThrow();
        assertThat(reloaded.getSalary()).isEqualTo(65000.0);
        assertThat(reloaded.getYearsOfExperience()).isEqualTo(6);
    }

    @Test
    void delete_ShouldRemoveTeacherFromRepository() {
        Teacher teacher = teacherRepository.findByEmployeeId("EMP002").orElseThrow();
        teacherRepository.delete(teacher);
        teacherRepository.flush();

        assertThat(teacherRepository.findByEmployeeId("EMP002")).isEmpty();
        assertThat(teacherRepository.count()).isEqualTo(2);
    }

    @Test
    void findBySalaryBetween_ShouldReturnEmptyList_WhenNoTeachersMatch() {
        List<Teacher> nobody = teacherRepository.findBySalaryBetween(100000.0, 200000.0);

        assertThat(nobody).isEmpty();
    }

    @Test
    void getAverageSalaryByDepartment_ShouldReturnNull_WhenDepartmentHasNoTeachers() {
        Double average = teacherRepository.getAverageSalaryByDepartment("Physics");

        assertThat(average).isNull();
    }
}