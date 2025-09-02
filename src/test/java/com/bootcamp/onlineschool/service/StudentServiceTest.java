package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.dto.StudentDTO;
import com.bootcamp.onlineschool.entity.Student;
import com.bootcamp.onlineschool.entity.Clazz;
import com.bootcamp.onlineschool.entity.Teacher;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.exception.ValidationException;
import com.bootcamp.onlineschool.repository.StudentRepository;
import com.bootcamp.onlineschool.repository.ClazzRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ClazzRepository clazzRepository;

    @InjectMocks
    private StudentService studentService;

    private Student testStudent;
    private StudentDTO testStudentDTO;
    private Clazz testClazz;

    @BeforeEach
    void setUp() {
        testStudent = new Student("John Doe", "john.doe@example.com", "STU001", LocalDate.of(2023, 1, 15));
        testStudent.setId(1L);
        testStudent.setCreatedAt(LocalDateTime.now());
        testStudent.setUpdatedAt(LocalDateTime.now());

        testStudentDTO = new StudentDTO("John Doe", "john.doe@example.com", "STU001", LocalDate.of(2023, 1, 15));
        testStudentDTO.setId(1L);

        // Create a mock teacher for the class
        Teacher mockTeacher = new Teacher("Jane Smith", "jane.smith@example.com", "EMP001", "Mathematics", LocalDate.of(2020, 1, 15));
        mockTeacher.setId(1L);
        
        testClazz = new Clazz("Math 101", "Fall", 2023, 30, mockTeacher);
        testClazz.setId(1L);
        testClazz.setStudents(new HashSet<>());
    }

    @Test
    void create_WhenValidData_ShouldCreateStudent() {
        // Given
        when(studentRepository.existsByStudentId("STU001")).thenReturn(false);
        when(studentRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // When
        StudentDTO result = studentService.create(testStudentDTO);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("STU001", result.getStudentId());
        verify(studentRepository).existsByStudentId("STU001");
        verify(studentRepository).findByEmail("john.doe@example.com");
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void create_WhenDuplicateStudentId_ShouldThrowValidationException() {
        // Given
        when(studentRepository.existsByStudentId("STU001")).thenReturn(true);

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> studentService.create(testStudentDTO)
        );
        assertEquals("Student ID already exists: STU001", exception.getMessage());
        verify(studentRepository).existsByStudentId("STU001");
        verify(studentRepository, never()).save(any());
    }

    @Test
    void create_WhenDuplicateEmail_ShouldThrowValidationException() {
        // Given
        when(studentRepository.existsByStudentId("STU001")).thenReturn(false);
        when(studentRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(testStudent));

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> studentService.create(testStudentDTO)
        );
        assertEquals("Email already exists: john.doe@example.com", exception.getMessage());
        verify(studentRepository).existsByStudentId("STU001");
        verify(studentRepository).findByEmail("john.doe@example.com");
        verify(studentRepository, never()).save(any());
    }

    @Test
    void create_WhenNullData_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> studentService.create(null)
        );
        assertEquals("Student data cannot be null", exception.getMessage());
        verify(studentRepository, never()).save(any());
    }

    @Test
    void create_WhenFutureEnrollmentDate_ShouldThrowValidationException() {
        // Given
        testStudentDTO.setEnrollmentDate(LocalDate.now().plusDays(1));

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> studentService.create(testStudentDTO)
        );
        assertEquals("Enrollment date cannot be in the future", exception.getMessage());
        verify(studentRepository, never()).save(any());
    }

    @Test
    void findAll_ShouldReturnAllStudents() {
        // Given
        List<Student> students = Arrays.asList(testStudent);
        when(studentRepository.findAll()).thenReturn(students);

        // When
        List<StudentDTO> result = studentService.findAll();

        // Then
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(studentRepository).findAll();
    }

    @Test
    void findById_WhenStudentExists_ShouldReturnStudent() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        // When
        StudentDTO result = studentService.findById(1L);

        // Then
        assertEquals("John Doe", result.getName());
        assertEquals("STU001", result.getStudentId());
        verify(studentRepository).findById(1L);
    }

    @Test
    void findById_WhenStudentNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> studentService.findById(1L)
        );
        assertEquals("Student not found with id: '1'", exception.getMessage());
        verify(studentRepository).findById(1L);
    }

    @Test
    void findByStudentId_WhenValidStudentId_ShouldReturnStudent() {
        // Given
        when(studentRepository.findByStudentId("STU001")).thenReturn(Optional.of(testStudent));

        // When
        Optional<StudentDTO> result = studentService.findByStudentId("STU001");

        // Then
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        verify(studentRepository).findByStudentId("STU001");
    }

    @Test
    void findByStudentId_WhenNullStudentId_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> studentService.findByStudentId(null)
        );
        assertEquals("Student ID cannot be null or empty", exception.getMessage());
        verify(studentRepository, never()).findByStudentId(any());
    }

    @Test
    void update_WhenValidData_ShouldUpdateStudent() {
        // Given
        StudentDTO updatedDTO = new StudentDTO("Jane Doe", "jane.doe@example.com", "STU002", LocalDate.of(2023, 2, 15));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(studentRepository.existsByStudentId("STU002")).thenReturn(false);
        when(studentRepository.findByEmail("jane.doe@example.com")).thenReturn(Optional.empty());
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // When
        StudentDTO result = studentService.update(1L, updatedDTO);

        // Then
        assertEquals("Jane Doe", testStudent.getName());
        assertEquals("jane.doe@example.com", testStudent.getEmail());
        assertEquals("STU002", testStudent.getStudentId());
        verify(studentRepository).findById(1L);
        verify(studentRepository).save(testStudent);
    }

    @Test
    void enrollInClass_WhenValidData_ShouldEnrollStudent() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(clazzRepository.findById(1L)).thenReturn(Optional.of(testClazz));
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // When
        StudentDTO result = studentService.enrollInClass(1L, 1L);

        // Then
        assertNotNull(result);
        verify(studentRepository).findById(1L);
        verify(clazzRepository).findById(1L);
        verify(studentRepository).save(testStudent);
    }

    @Test
    void enrollInClass_WhenStudentNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> studentService.enrollInClass(1L, 1L)
        );
        assertEquals("Student not found with id: '1'", exception.getMessage());
        verify(studentRepository).findById(1L);
        verify(clazzRepository, never()).findById(any());
    }

    @Test
    void enrollInClass_WhenClassNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(clazzRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> studentService.enrollInClass(1L, 1L)
        );
        assertEquals("Class not found with id: '1'", exception.getMessage());
        verify(studentRepository).findById(1L);
        verify(clazzRepository).findById(1L);
    }

    @Test
    void enrollInClass_WhenAlreadyEnrolled_ShouldThrowValidationException() {
        // Given
        testStudent.addClazz(testClazz);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(clazzRepository.findById(1L)).thenReturn(Optional.of(testClazz));

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> studentService.enrollInClass(1L, 1L)
        );
        assertEquals("Student is already enrolled in class: Math 101", exception.getMessage());
        verify(studentRepository).findById(1L);
        verify(clazzRepository).findById(1L);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void deleteById_WhenStudentExists_ShouldDeleteStudent() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        // When
        studentService.deleteById(1L);

        // Then
        verify(studentRepository).findById(1L);
        verify(studentRepository).delete(testStudent);
    }

    @Test
    void deleteById_WhenStudentNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> studentService.deleteById(1L)
        );
        assertEquals("Student not found with id: '1'", exception.getMessage());
        verify(studentRepository).findById(1L);
        verify(studentRepository, never()).delete(any());
    }

    @Test
    void existsByStudentId_WhenValidStudentId_ShouldReturnTrue() {
        // Given
        when(studentRepository.existsByStudentId("STU001")).thenReturn(true);

        // When
        boolean result = studentService.existsByStudentId("STU001");

        // Then
        assertTrue(result);
        verify(studentRepository).existsByStudentId("STU001");
    }

    @Test
    void findStudentsByClazzId_WhenValidClazzId_ShouldReturnStudents() {
        // Given
        List<Student> students = Arrays.asList(testStudent);
        when(studentRepository.findStudentsByClazzId(1L)).thenReturn(students);

        // When
        List<StudentDTO> result = studentService.findStudentsByClazzId(1L);

        // Then
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(studentRepository).findStudentsByClazzId(1L);
    }

    @Test
    void findStudentsByClazzId_WhenNullClazzId_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> studentService.findStudentsByClazzId(null)
        );
        assertEquals("Class ID cannot be null", exception.getMessage());
        verify(studentRepository, never()).findStudentsByClazzId(any());
    }
}