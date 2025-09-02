package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.dto.TeacherDTO;
import com.bootcamp.onlineschool.entity.Teacher;
import com.bootcamp.onlineschool.entity.Clazz;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.exception.ValidationException;
import com.bootcamp.onlineschool.repository.TeacherRepository;
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
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private ClazzRepository clazzRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher testTeacher;
    private TeacherDTO testTeacherDTO;
    private Clazz testClazz;

    @BeforeEach
    void setUp() {
        testTeacher = new Teacher("John Smith", "john.smith@example.com", "EMP001", "Mathematics", LocalDate.of(2020, 1, 15));
        testTeacher.setId(1L);
        testTeacher.setCreatedAt(LocalDateTime.now());
        testTeacher.setUpdatedAt(LocalDateTime.now());

        testTeacherDTO = new TeacherDTO("John Smith", "john.smith@example.com", "EMP001", "Mathematics", LocalDate.of(2020, 1, 15));
        testTeacherDTO.setId(1L);

        testClazz = new Clazz("Math 101", "Fall", 2023, 30, testTeacher);
        testClazz.setId(1L);
        testClazz.setStudents(new HashSet<>());
    }

    @Test
    void create_WhenValidData_ShouldCreateTeacher() {
        // Given
        when(teacherRepository.existsByEmployeeId("EMP001")).thenReturn(false);
        when(teacherRepository.findByEmail("john.smith@example.com")).thenReturn(Optional.empty());
        when(teacherRepository.save(any(Teacher.class))).thenReturn(testTeacher);

        // When
        TeacherDTO result = teacherService.create(testTeacherDTO);

        // Then
        assertNotNull(result);
        assertEquals("John Smith", result.getName());
        assertEquals("john.smith@example.com", result.getEmail());
        assertEquals("EMP001", result.getEmployeeId());
        assertEquals("Mathematics", result.getDepartment());
        verify(teacherRepository).existsByEmployeeId("EMP001");
        verify(teacherRepository).findByEmail("john.smith@example.com");
        verify(teacherRepository).save(any(Teacher.class));
    }

    @Test
    void create_WhenDuplicateEmployeeId_ShouldThrowValidationException() {
        // Given
        when(teacherRepository.existsByEmployeeId("EMP001")).thenReturn(true);

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> teacherService.create(testTeacherDTO)
        );
        assertEquals("Employee ID already exists: EMP001", exception.getMessage());
        verify(teacherRepository).existsByEmployeeId("EMP001");
        verify(teacherRepository, never()).save(any());
    }

    @Test
    void create_WhenDuplicateEmail_ShouldThrowValidationException() {
        // Given
        when(teacherRepository.existsByEmployeeId("EMP001")).thenReturn(false);
        when(teacherRepository.findByEmail("john.smith@example.com")).thenReturn(Optional.of(testTeacher));

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> teacherService.create(testTeacherDTO)
        );
        assertEquals("Email already exists: john.smith@example.com", exception.getMessage());
        verify(teacherRepository).existsByEmployeeId("EMP001");
        verify(teacherRepository).findByEmail("john.smith@example.com");
        verify(teacherRepository, never()).save(any());
    }

    @Test
    void create_WhenNullData_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> teacherService.create(null)
        );
        assertEquals("Teacher data cannot be null", exception.getMessage());
        verify(teacherRepository, never()).save(any());
    }

    @Test
    void create_WhenFutureHireDate_ShouldThrowValidationException() {
        // Given
        testTeacherDTO.setHireDate(LocalDate.now().plusDays(1));

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> teacherService.create(testTeacherDTO)
        );
        assertEquals("Hire date cannot be in the future", exception.getMessage());
        verify(teacherRepository, never()).save(any());
    }

    @Test
    void findAll_ShouldReturnAllTeachers() {
        // Given
        List<Teacher> teachers = Arrays.asList(testTeacher);
        when(teacherRepository.findAll()).thenReturn(teachers);

        // When
        List<TeacherDTO> result = teacherService.findAll();

        // Then
        assertEquals(1, result.size());
        assertEquals("John Smith", result.get(0).getName());
        verify(teacherRepository).findAll();
    }

    @Test
    void findById_WhenTeacherExists_ShouldReturnTeacher() {
        // Given
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));

        // When
        TeacherDTO result = teacherService.findById(1L);

        // Then
        assertEquals("John Smith", result.getName());
        assertEquals("EMP001", result.getEmployeeId());
        verify(teacherRepository).findById(1L);
    }

    @Test
    void findById_WhenTeacherNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> teacherService.findById(1L)
        );
        assertEquals("Teacher not found with id: '1'", exception.getMessage());
        verify(teacherRepository).findById(1L);
    }

    @Test
    void findByEmployeeId_WhenValidEmployeeId_ShouldReturnTeacher() {
        // Given
        when(teacherRepository.findByEmployeeId("EMP001")).thenReturn(Optional.of(testTeacher));

        // When
        Optional<TeacherDTO> result = teacherService.findByEmployeeId("EMP001");

        // Then
        assertTrue(result.isPresent());
        assertEquals("John Smith", result.get().getName());
        verify(teacherRepository).findByEmployeeId("EMP001");
    }

    @Test
    void findByEmployeeId_WhenNullEmployeeId_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> teacherService.findByEmployeeId(null)
        );
        assertEquals("Employee ID cannot be null or empty", exception.getMessage());
        verify(teacherRepository, never()).findByEmployeeId(any());
    }

    @Test
    void findByDepartment_WhenValidDepartment_ShouldReturnTeachers() {
        // Given
        List<Teacher> teachers = Arrays.asList(testTeacher);
        when(teacherRepository.findTeachersByDepartment("Mathematics")).thenReturn(teachers);

        // When
        List<TeacherDTO> result = teacherService.findByDepartment("Mathematics");

        // Then
        assertEquals(1, result.size());
        assertEquals("John Smith", result.get(0).getName());
        verify(teacherRepository).findTeachersByDepartment("Mathematics");
    }

    @Test
    void findByDepartment_WhenNullDepartment_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> teacherService.findByDepartment(null)
        );
        assertEquals("Department cannot be null or empty", exception.getMessage());
        verify(teacherRepository, never()).findTeachersByDepartment(any());
    }

    @Test
    void update_WhenValidData_ShouldUpdateTeacher() {
        // Given
        TeacherDTO updatedDTO = new TeacherDTO("Jane Smith", "jane.smith@example.com", "EMP002", "Physics", LocalDate.of(2021, 2, 15));
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));
        when(teacherRepository.existsByEmployeeId("EMP002")).thenReturn(false);
        when(teacherRepository.findByEmail("jane.smith@example.com")).thenReturn(Optional.empty());
        when(teacherRepository.save(any(Teacher.class))).thenReturn(testTeacher);

        // When
        TeacherDTO result = teacherService.update(1L, updatedDTO);

        // Then
        assertEquals("Jane Smith", testTeacher.getName());
        assertEquals("jane.smith@example.com", testTeacher.getEmail());
        assertEquals("EMP002", testTeacher.getEmployeeId());
        assertEquals("Physics", testTeacher.getDepartment());
        verify(teacherRepository).findById(1L);
        verify(teacherRepository).save(testTeacher);
    }

    @Test
    void assignToClass_WhenValidData_ShouldAssignTeacher() {
        // Given
        // Create a separate class without a teacher assigned
        Clazz unassignedClazz = new Clazz();
        unassignedClazz.setId(2L);
        unassignedClazz.setName("Physics 101");
        unassignedClazz.setSemester("Spring");
        unassignedClazz.setYear(2024);
        unassignedClazz.setMaxCapacity(25);
        unassignedClazz.setStudents(new HashSet<>());
        
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));
        when(clazzRepository.findById(2L)).thenReturn(Optional.of(unassignedClazz));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(testTeacher);

        // When
        TeacherDTO result = teacherService.assignToClass(1L, 2L);

        // Then
        assertNotNull(result);
        verify(teacherRepository).findById(1L);
        verify(clazzRepository).findById(2L);
        verify(teacherRepository).save(testTeacher);
    }

    @Test
    void assignToClass_WhenTeacherNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> teacherService.assignToClass(1L, 1L)
        );
        assertEquals("Teacher not found with id: '1'", exception.getMessage());
        verify(teacherRepository).findById(1L);
        verify(clazzRepository, never()).findById(any());
    }

    @Test
    void assignToClass_WhenClassNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));
        when(clazzRepository.findById(2L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> teacherService.assignToClass(1L, 2L)
        );
        assertEquals("Class not found with id: '2'", exception.getMessage());
        verify(teacherRepository).findById(1L);
        verify(clazzRepository).findById(2L);
    }

    @Test
    void assignToClass_WhenAlreadyAssigned_ShouldThrowValidationException() {
        // Given
        testTeacher.addClazz(testClazz);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));
        when(clazzRepository.findById(1L)).thenReturn(Optional.of(testClazz));

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> teacherService.assignToClass(1L, 1L)
        );
        assertEquals("Teacher is already assigned to class: Math 101", exception.getMessage());
        verify(teacherRepository).findById(1L);
        verify(clazzRepository).findById(1L);
        verify(teacherRepository, never()).save(any());
    }

    @Test
    void deleteById_WhenTeacherHasNoClasses_ShouldDeleteTeacher() {
        // Given
        testTeacher.setClasses(new HashSet<>());
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));

        // When
        teacherService.deleteById(1L);

        // Then
        verify(teacherRepository).findById(1L);
        verify(teacherRepository).delete(testTeacher);
    }

    @Test
    void deleteById_WhenTeacherHasClasses_ShouldThrowValidationException() {
        // Given
        testTeacher.addClazz(testClazz);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> teacherService.deleteById(1L)
        );
        assertEquals("Cannot delete teacher with assigned classes. Please reassign classes first.", exception.getMessage());
        verify(teacherRepository).findById(1L);
        verify(teacherRepository, never()).delete(any());
    }

    @Test
    void deleteById_WhenTeacherNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> teacherService.deleteById(1L)
        );
        assertEquals("Teacher not found with id: '1'", exception.getMessage());
        verify(teacherRepository).findById(1L);
        verify(teacherRepository, never()).delete(any());
    }

    @Test
    void existsByEmployeeId_WhenValidEmployeeId_ShouldReturnTrue() {
        // Given
        when(teacherRepository.existsByEmployeeId("EMP001")).thenReturn(true);

        // When
        boolean result = teacherService.existsByEmployeeId("EMP001");

        // Then
        assertTrue(result);
        verify(teacherRepository).existsByEmployeeId("EMP001");
    }

    @Test
    void removeFromClass_WhenValidData_ShouldRemoveTeacher() {
        // Given
        testTeacher.addClazz(testClazz);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));
        when(clazzRepository.findById(1L)).thenReturn(Optional.of(testClazz));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(testTeacher);

        // When
        TeacherDTO result = teacherService.removeFromClass(1L, 1L);

        // Then
        assertNotNull(result);
        verify(teacherRepository).findById(1L);
        verify(clazzRepository).findById(1L);
        verify(teacherRepository).save(testTeacher);
    }
}