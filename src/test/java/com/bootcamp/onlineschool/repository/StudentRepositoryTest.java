package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Student;
import com.bootcamp.onlineschool.entity.Teacher;
import com.bootcamp.onlineschool.entity.Clazz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class StudentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    private Student testStudent1;
    private Student testStudent2;
    private Teacher testTeacher;
    private Clazz testClazz;

    @BeforeEach
    void setUp() {
        testStudent1 = new Student("John Doe", "john.doe@example.com", "STU001", LocalDate.of(2023, 1, 15));
        testStudent2 = new Student("Jane Smith", "jane.smith@example.com", "STU002", LocalDate.of(2023, 2, 20));
        testTeacher = new Teacher("Prof. Wilson", "prof.wilson@example.com", "EMP001", "Computer Science", LocalDate.of(2020, 8, 1));
        
        entityManager.persistAndFlush(testStudent1);
        entityManager.persistAndFlush(testStudent2);
        entityManager.persistAndFlush(testTeacher);
        
        testClazz = new Clazz("Java Programming", "Fall", 2023, 30, testTeacher);
        entityManager.persistAndFlush(testClazz);
        
        // Add students to class
        testClazz.addStudent(testStudent1);
        entityManager.persistAndFlush(testClazz);
    }

    @Test
    void findByStudentId_ShouldReturnStudent_WhenStudentIdExists() {
        // When
        Optional<Student> foundStudent = studentRepository.findByStudentId("STU001");

        // Then
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getName()).isEqualTo("John Doe");
        assertThat(foundStudent.get().getStudentId()).isEqualTo("STU001");
    }

    @Test
    void findByStudentId_ShouldReturnEmpty_WhenStudentIdDoesNotExist() {
        // When
        Optional<Student> foundStudent = studentRepository.findByStudentId("STU999");

        // Then
        assertThat(foundStudent).isEmpty();
    }

    @Test
    void existsByStudentId_ShouldReturnTrue_WhenStudentIdExists() {
        // When
        boolean exists = studentRepository.existsByStudentId("STU002");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByStudentId_ShouldReturnFalse_WhenStudentIdDoesNotExist() {
        // When
        boolean exists = studentRepository.existsByStudentId("STU999");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void findByEmail_ShouldReturnStudent_WhenEmailExists() {
        // When
        Optional<Student> foundStudent = studentRepository.findByEmail("jane.smith@example.com");

        // Then
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getName()).isEqualTo("Jane Smith");
        assertThat(foundStudent.get().getStudentId()).isEqualTo("STU002");
    }

    @Test
    void findStudentsByClazz_ShouldReturnStudentsInClass() {
        // When
        List<Student> studentsInClass = studentRepository.findStudentsByClazz(testClazz);

        // Then
        assertThat(studentsInClass).hasSize(1);
        assertThat(studentsInClass.get(0).getStudentId()).isEqualTo("STU001");
    }

    @Test
    void findStudentsByClazzId_ShouldReturnStudentsInClass() {
        // When
        List<Student> studentsInClass = studentRepository.findStudentsByClazzId(testClazz.getId());

        // Then
        assertThat(studentsInClass).hasSize(1);
        assertThat(studentsInClass.get(0).getStudentId()).isEqualTo("STU001");
    }

    @Test
    void findStudentsByClazz_ShouldReturnEmptyList_WhenNoStudentsInClass() {
        // Given
        Clazz emptyClazz = new Clazz("Empty Class", "Spring", 2024, 25, testTeacher);
        entityManager.persistAndFlush(emptyClazz);

        // When
        List<Student> studentsInClass = studentRepository.findStudentsByClazz(emptyClazz);

        // Then
        assertThat(studentsInClass).isEmpty();
    }
}