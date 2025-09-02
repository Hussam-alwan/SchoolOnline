package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.dto.RegistrationDTO;
import com.bootcamp.onlineschool.entity.Registration;
import com.bootcamp.onlineschool.entity.Student;
import com.bootcamp.onlineschool.entity.Course;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.exception.ValidationException;
import com.bootcamp.onlineschool.repository.RegistrationRepository;
import com.bootcamp.onlineschool.repository.StudentRepository;
import com.bootcamp.onlineschool.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private RegistrationService registrationService;

    private Registration testRegistration;
    private RegistrationDTO testRegistrationDTO;
    private Student testStudent;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        testStudent = new Student("Jane Doe", "jane.doe@example.com", "STU001", LocalDate.of(2023, 1, 15));
        testStudent.setId(1L);

        testCourse = new Course("Mathematics 101", "Introduction to Mathematics", 3, 45);
        testCourse.setId(1L);

        testRegistration = new Registration(LocalDate.of(2023, 9, 1), "ACTIVE", testStudent, testCourse);
        testRegistration.setId(1L);
        testRegistration.setCreatedAt(LocalDateTime.now());
        testRegistration.setUpdatedAt(LocalDateTime.now());

        testRegistrationDTO = new RegistrationDTO(LocalDate.of(2023, 9, 1), "ACTIVE", 1L, 1L);
        testRegistrationDTO.setId(1L);
    }

    @Test
    void create_WhenValidData_ShouldCreateRegistration() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(registrationRepository.existsByStudentAndCourse(testStudent, testCourse)).thenReturn(false);
        when(registrationRepository.save(any(Registration.class))).thenReturn(testRegistration);

        // When
        RegistrationDTO result = registrationService.create(testRegistrationDTO);

        // Then
        assertNotNull(result);
        assertEquals("ACTIVE", result.getStatus());
        assertEquals(1L, result.getStudentId());
        assertEquals(1L, result.getCourseId());
        verify(studentRepository).findById(1L);
        verify(courseRepository).findById(1L);
        verify(registrationRepository).existsByStudentAndCourse(testStudent, testCourse);
        verify(registrationRepository).save(any(Registration.class));
    }

    @Test
    void create_WhenDuplicateRegistration_ShouldThrowValidationException() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(registrationRepository.existsByStudentAndCourse(testStudent, testCourse)).thenReturn(true);

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> registrationService.create(testRegistrationDTO)
        );
        assertEquals("Student is already registered for this course", exception.getMessage());
        verify(studentRepository).findById(1L);
        verify(courseRepository).findById(1L);
        verify(registrationRepository).existsByStudentAndCourse(testStudent, testCourse);
        verify(registrationRepository, never()).save(any());
    }

    @Test
    void create_WhenStudentNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> registrationService.create(testRegistrationDTO)
        );
        assertEquals("Student not found with id: '1'", exception.getMessage());
        verify(studentRepository).findById(1L);
        verify(courseRepository, never()).findById(any());
        verify(registrationRepository, never()).save(any());
    }

    @Test
    void create_WhenCourseNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> registrationService.create(testRegistrationDTO)
        );
        assertEquals("Course not found with id: '1'", exception.getMessage());
        verify(studentRepository).findById(1L);
        verify(courseRepository).findById(1L);
        verify(registrationRepository, never()).save(any());
    }

    @Test
    void create_WhenNullData_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> registrationService.create(null)
        );
        assertEquals("Registration data cannot be null", exception.getMessage());
        verify(registrationRepository, never()).save(any());
    }

    @Test
    void create_WhenFutureRegistrationDate_ShouldThrowValidationException() {
        // Given
        testRegistrationDTO.setRegistrationDate(LocalDate.now().plusDays(1));

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> registrationService.create(testRegistrationDTO)
        );
        assertEquals("Registration date cannot be in the future", exception.getMessage());
        verify(registrationRepository, never()).save(any());
    }

    @Test
    void create_WhenInvalidStatus_ShouldThrowValidationException() {
        // Given
        testRegistrationDTO.setStatus("INVALID_STATUS");

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> registrationService.create(testRegistrationDTO)
        );
        assertEquals("Invalid status. Valid statuses are: ACTIVE, COMPLETED, DROPPED, PENDING", exception.getMessage());
        verify(registrationRepository, never()).save(any());
    }

    @Test
    void registerStudentForCourse_WhenValidData_ShouldCreateRegistration() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(registrationRepository.existsByStudentAndCourse(testStudent, testCourse)).thenReturn(false);
        when(registrationRepository.save(any(Registration.class))).thenReturn(testRegistration);

        // When
        RegistrationDTO result = registrationService.registerStudentForCourse(1L, 1L);

        // Then
        assertNotNull(result);
        assertEquals("ACTIVE", result.getStatus());
        assertEquals(1L, result.getStudentId());
        assertEquals(1L, result.getCourseId());
        verify(studentRepository).findById(1L);
        verify(courseRepository).findById(1L);
        verify(registrationRepository).existsByStudentAndCourse(testStudent, testCourse);
        verify(registrationRepository).save(any(Registration.class));
    }

    @Test
    void registerStudentForCourse_WhenNullStudentId_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> registrationService.registerStudentForCourse(null, 1L)
        );
        assertEquals("Student ID cannot be null", exception.getMessage());
        verify(registrationRepository, never()).save(any());
    }

    @Test
    void findAll_ShouldReturnAllRegistrations() {
        // Given
        List<Registration> registrations = Arrays.asList(testRegistration);
        when(registrationRepository.findAll()).thenReturn(registrations);

        // When
        List<RegistrationDTO> result = registrationService.findAll();

        // Then
        assertEquals(1, result.size());
        assertEquals("ACTIVE", result.get(0).getStatus());
        verify(registrationRepository).findAll();
    }

    @Test
    void findById_WhenRegistrationExists_ShouldReturnRegistration() {
        // Given
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(testRegistration));

        // When
        RegistrationDTO result = registrationService.findById(1L);

        // Then
        assertEquals("ACTIVE", result.getStatus());
        assertEquals(1L, result.getStudentId());
        assertEquals(1L, result.getCourseId());
        verify(registrationRepository).findById(1L);
    }

    @Test
    void findById_WhenRegistrationNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(registrationRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> registrationService.findById(1L)
        );
        assertEquals("Registration not found with id: '1'", exception.getMessage());
        verify(registrationRepository).findById(1L);
    }

    @Test
    void findByStudentId_WhenValidStudentId_ShouldReturnRegistrations() {
        // Given
        List<Registration> registrations = Arrays.asList(testRegistration);
        when(registrationRepository.findByStudentId(1L)).thenReturn(registrations);

        // When
        List<RegistrationDTO> result = registrationService.findByStudentId(1L);

        // Then
        assertEquals(1, result.size());
        assertEquals("ACTIVE", result.get(0).getStatus());
        verify(registrationRepository).findByStudentId(1L);
    }

    @Test
    void findByStudentId_WhenNullStudentId_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> registrationService.findByStudentId(null)
        );
        assertEquals("Student ID cannot be null", exception.getMessage());
        verify(registrationRepository, never()).findByStudentId(any());
    }

    @Test
    void findByStatus_WhenValidStatus_ShouldReturnRegistrations() {
        // Given
        List<Registration> registrations = Arrays.asList(testRegistration);
        when(registrationRepository.findByStatus("ACTIVE")).thenReturn(registrations);

        // When
        List<RegistrationDTO> result = registrationService.findByStatus("ACTIVE");

        // Then
        assertEquals(1, result.size());
        assertEquals("ACTIVE", result.get(0).getStatus());
        verify(registrationRepository).findByStatus("ACTIVE");
    }

    @Test
    void findByStatus_WhenNullStatus_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> registrationService.findByStatus(null)
        );
        assertEquals("Status cannot be null or empty", exception.getMessage());
        verify(registrationRepository, never()).findByStatus(any());
    }

    @Test
    void update_WhenValidData_ShouldUpdateRegistration() {
        // Given
        RegistrationDTO updatedDTO = new RegistrationDTO(LocalDate.of(2023, 9, 2), "COMPLETED", 1L, 1L);
        updatedDTO.setGrade("A");
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(testRegistration));
        when(registrationRepository.save(any(Registration.class))).thenReturn(testRegistration);

        // When
        RegistrationDTO result = registrationService.update(1L, updatedDTO);

        // Then
        assertEquals(LocalDate.of(2023, 9, 2), testRegistration.getRegistrationDate());
        assertEquals("COMPLETED", testRegistration.getStatus());
        assertEquals("A", testRegistration.getGrade());
        verify(registrationRepository).findById(1L);
        verify(registrationRepository).save(testRegistration);
    }

    @Test
    void updateStatus_WhenValidData_ShouldUpdateStatus() {
        // Given
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(testRegistration));
        when(registrationRepository.save(any(Registration.class))).thenReturn(testRegistration);

        // When
        RegistrationDTO result = registrationService.updateStatus(1L, "COMPLETED");

        // Then
        assertEquals("COMPLETED", testRegistration.getStatus());
        verify(registrationRepository).findById(1L);
        verify(registrationRepository).save(testRegistration);
    }

    @Test
    void updateGrade_WhenValidData_ShouldUpdateGrade() {
        // Given
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(testRegistration));
        when(registrationRepository.save(any(Registration.class))).thenReturn(testRegistration);

        // When
        RegistrationDTO result = registrationService.updateGrade(1L, "A");

        // Then
        assertEquals("A", testRegistration.getGrade());
        verify(registrationRepository).findById(1L);
        verify(registrationRepository).save(testRegistration);
    }

    @Test
    void deleteById_WhenRegistrationExists_ShouldDeleteRegistration() {
        // Given
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(testRegistration));

        // When
        registrationService.deleteById(1L);

        // Then
        verify(registrationRepository).findById(1L);
        verify(registrationRepository).delete(testRegistration);
    }

    @Test
    void deleteById_WhenRegistrationNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(registrationRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> registrationService.deleteById(1L)
        );
        assertEquals("Registration not found with id: '1'", exception.getMessage());
        verify(registrationRepository).findById(1L);
        verify(registrationRepository, never()).delete(any());
    }

    @Test
    void dropStudentFromCourse_WhenValidData_ShouldUpdateStatusToDropped() {
        // Given
        when(registrationRepository.findByStudentIdAndCourseId(1L, 1L)).thenReturn(Optional.of(testRegistration));
        when(registrationRepository.save(any(Registration.class))).thenReturn(testRegistration);

        // When
        RegistrationDTO result = registrationService.dropStudentFromCourse(1L, 1L);

        // Then
        assertEquals("DROPPED", testRegistration.getStatus());
        verify(registrationRepository).findByStudentIdAndCourseId(1L, 1L);
        verify(registrationRepository).save(testRegistration);
    }

    @Test
    void dropStudentFromCourse_WhenRegistrationNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(registrationRepository.findByStudentIdAndCourseId(1L, 1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> registrationService.dropStudentFromCourse(1L, 1L)
        );
        assertEquals("Registration not found for student 1 and course 1", exception.getMessage());
        verify(registrationRepository).findByStudentIdAndCourseId(1L, 1L);
        verify(registrationRepository, never()).save(any());
    }

    @Test
    void findRegistrationsWithGrades_ShouldReturnRegistrationsWithGrades() {
        // Given
        List<Registration> registrations = Arrays.asList(testRegistration);
        when(registrationRepository.findRegistrationsWithGrades()).thenReturn(registrations);

        // When
        List<RegistrationDTO> result = registrationService.findRegistrationsWithGrades();

        // Then
        assertEquals(1, result.size());
        verify(registrationRepository).findRegistrationsWithGrades();
    }

    @Test
    void findRegistrationsWithoutGrades_ShouldReturnRegistrationsWithoutGrades() {
        // Given
        List<Registration> registrations = Arrays.asList(testRegistration);
        when(registrationRepository.findRegistrationsWithoutGrades()).thenReturn(registrations);

        // When
        List<RegistrationDTO> result = registrationService.findRegistrationsWithoutGrades();

        // Then
        assertEquals(1, result.size());
        verify(registrationRepository).findRegistrationsWithoutGrades();
    }
}