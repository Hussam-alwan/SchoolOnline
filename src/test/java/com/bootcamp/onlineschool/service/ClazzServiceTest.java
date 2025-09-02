package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.dto.ClazzDTO;
import com.bootcamp.onlineschool.entity.Clazz;
import com.bootcamp.onlineschool.entity.Teacher;
import com.bootcamp.onlineschool.entity.Student;
import com.bootcamp.onlineschool.entity.Course;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.exception.ValidationException;
import com.bootcamp.onlineschool.repository.ClazzRepository;
import com.bootcamp.onlineschool.repository.TeacherRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClazzServiceTest {

    @Mock
    private ClazzRepository clazzRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private ClazzService clazzService;

    private Clazz testClazz;
    private ClazzDTO testClazzDTO;
    private Teacher testTeacher;
    private Student testStudent;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        testTeacher = new Teacher("John Smith", "john.smith@example.com", "EMP001", "Mathematics", LocalDate.of(2020, 1, 15));
        testTeacher.setId(1L);

        testClazz = new Clazz("Math 101", "Fall", 2023, 30, testTeacher);
        testClazz.setId(1L);
        testClazz.setCreatedAt(LocalDateTime.now());
        testClazz.setUpdatedAt(LocalDateTime.now());
        testClazz.setStudents(new HashSet<>());
        testClazz.setCourses(new HashSet<>());

        testClazzDTO = new ClazzDTO("Math 101", "Fall", 2023, 30, 1L);
        testClazzDTO.setId(1L);

        testStudent = new Student("Jane Doe", "jane.doe@example.com", "STU001", LocalDate.of(2023, 1, 15));
        testStudent.setId(1L);

        testCourse = new Course("Mathematics 101", "Introduction to Mathematics", 3, 45);
        testCourse.setId(1L);
    }

    @Test
    void create_WhenValidData_ShouldCreateClazz() {
        // Given
        when(clazzRepository.existsByName("Math 101")).thenReturn(false);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));
        when(clazzRepository.save(any(Clazz.class))).thenReturn(testClazz);

        // When
        ClazzDTO result = clazzService.create(testClazzDTO);

        // Then
        assertNotNull(result);
        assertEquals("Math 101", result.getName());
        assertEquals("Fall", result.getSemester());
        assertEquals(2023, result.getYear());
        assertEquals(30, result.getMaxCapacity());
        assertEquals(1L, result.getTeacherId());
        verify(clazzRepository).existsByName("Math 101");
        verify(teacherRepository).findById(1L);
        verify(clazzRepository).save(any(Clazz.class));
    }

    @Test
    void create_WhenDuplicateName_ShouldThrowValidationException() {
        // Given
        when(clazzRepository.existsByName("Math 101")).thenReturn(true);

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> clazzService.create(testClazzDTO)
        );
        assertEquals("Class name already exists: Math 101", exception.getMessage());
        verify(clazzRepository).existsByName("Math 101");
        verify(clazzRepository, never()).save(any());
    }

    @Test
    void create_WhenTeacherNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(clazzRepository.existsByName("Math 101")).thenReturn(false);
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> clazzService.create(testClazzDTO)
        );
        assertEquals("Teacher not found with id: '1'", exception.getMessage());
        verify(clazzRepository).existsByName("Math 101");
        verify(teacherRepository).findById(1L);
        verify(clazzRepository, never()).save(any());
    }

    @Test
    void create_WhenNullData_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> clazzService.create(null)
        );
        assertEquals("Class data cannot be null", exception.getMessage());
        verify(clazzRepository, never()).save(any());
    }

    @Test
    void create_WhenInvalidYear_ShouldThrowValidationException() {
        // Given
        testClazzDTO.setYear(1999);

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> clazzService.create(testClazzDTO)
        );
        assertEquals("Year must be at least 2000", exception.getMessage());
        verify(clazzRepository, never()).save(any());
    }

    @Test
    void findAll_ShouldReturnAllClasses() {
        // Given
        List<Clazz> classes = Arrays.asList(testClazz);
        when(clazzRepository.findAll()).thenReturn(classes);

        // When
        List<ClazzDTO> result = clazzService.findAll();

        // Then
        assertEquals(1, result.size());
        assertEquals("Math 101", result.get(0).getName());
        verify(clazzRepository).findAll();
    }

    @Test
    void findById_WhenClazzExists_ShouldReturnClazz() {
        // Given
        when(clazzRepository.findById(1L)).thenReturn(Optional.of(testClazz));

        // When
        ClazzDTO result = clazzService.findById(1L);

        // Then
        assertEquals("Math 101", result.getName());
        assertEquals("Fall", result.getSemester());
        verify(clazzRepository).findById(1L);
    }

    @Test
    void findById_WhenClazzNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(clazzRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> clazzService.findById(1L)
        );
        assertEquals("Class not found with id: '1'", exception.getMessage());
        verify(clazzRepository).findById(1L);
    }

    @Test
    void findByName_WhenValidName_ShouldReturnClazz() {
        // Given
        when(clazzRepository.findByName("Math 101")).thenReturn(Optional.of(testClazz));

        // When
        Optional<ClazzDTO> result = clazzService.findByName("Math 101");

        // Then
        assertTrue(result.isPresent());
        assertEquals("Math 101", result.get().getName());
        verify(clazzRepository).findByName("Math 101");
    }

    @Test
    void findByName_WhenNullName_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> clazzService.findByName(null)
        );
        assertEquals("Class name cannot be null or empty", exception.getMessage());
        verify(clazzRepository, never()).findByName(any());
    }

    @Test
    void findBySemesterAndYear_WhenValidData_ShouldReturnClasses() {
        // Given
        List<Clazz> classes = Arrays.asList(testClazz);
        when(clazzRepository.findClassesBySemesterAndYear("Fall", 2023)).thenReturn(classes);

        // When
        List<ClazzDTO> result = clazzService.findBySemesterAndYear("Fall", 2023);

        // Then
        assertEquals(1, result.size());
        assertEquals("Math 101", result.get(0).getName());
        verify(clazzRepository).findClassesBySemesterAndYear("Fall", 2023);
    }

    @Test
    void findBySemesterAndYear_WhenNullSemester_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> clazzService.findBySemesterAndYear(null, 2023)
        );
        assertEquals("Semester cannot be null or empty", exception.getMessage());
        verify(clazzRepository, never()).findClassesBySemesterAndYear(any(), any());
    }

    @Test
    void update_WhenValidData_ShouldUpdateClazz() {
        // Given
        ClazzDTO updatedDTO = new ClazzDTO("Physics 101", "Spring", 2024, 25, 1L);
        when(clazzRepository.findById(1L)).thenReturn(Optional.of(testClazz));
        when(clazzRepository.existsByName("Physics 101")).thenReturn(false);
        when(clazzRepository.save(any(Clazz.class))).thenReturn(testClazz);

        // When
        ClazzDTO result = clazzService.update(1L, updatedDTO);

        // Then
        assertEquals("Physics 101", testClazz.getName());
        assertEquals("Spring", testClazz.getSemester());
        assertEquals(2024, testClazz.getYear());
        assertEquals(25, testClazz.getMaxCapacity());
        verify(clazzRepository).findById(1L);
        verify(clazzRepository).save(testClazz);
    }

    @Test
    void addStudent_WhenValidData_ShouldAddStudent() {
        // Given
        when(clazzRepository.findById(1L)).thenReturn(Optional.of(testClazz));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(clazzRepository.save(any(Clazz.class))).thenReturn(testClazz);

        // When
        ClazzDTO result = clazzService.addStudent(1L, 1L);

        // Then
        assertNotNull(result);
        verify(clazzRepository).findById(1L);
        verify(studentRepository).findById(1L);
        verify(clazzRepository).save(testClazz);
    }

    @Test
    void addStudent_WhenClazzNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(clazzRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> clazzService.addStudent(1L, 1L)
        );
        assertEquals("Class not found with id: '1'", exception.getMessage());
        verify(clazzRepository).findById(1L);
        verify(studentRepository, never()).findById(any());
    }

    @Test
    void addStudent_WhenStudentNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(clazzRepository.findById(1L)).thenReturn(Optional.of(testClazz));
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> clazzService.addStudent(1L, 1L)
        );
        assertEquals("Student not found with id: '1'", exception.getMessage());
        verify(clazzRepository).findById(1L);
        verify(studentRepository).findById(1L);
    }

    @Test
    void addCourse_WhenValidData_ShouldAddCourse() {
        // Given
        when(clazzRepository.findById(1L)).thenReturn(Optional.of(testClazz));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(clazzRepository.save(any(Clazz.class))).thenReturn(testClazz);

        // When
        ClazzDTO result = clazzService.addCourse(1L, 1L);

        // Then
        assertNotNull(result);
        verify(clazzRepository).findById(1L);
        verify(courseRepository).findById(1L);
        verify(clazzRepository).save(testClazz);
    }

    @Test
    void deleteById_WhenClazzHasNoStudentsOrCourses_ShouldDeleteClazz() {
        // Given
        testClazz.setStudents(new HashSet<>());
        testClazz.setCourses(new HashSet<>());
        when(clazzRepository.findById(1L)).thenReturn(Optional.of(testClazz));

        // When
        clazzService.deleteById(1L);

        // Then
        verify(clazzRepository).findById(1L);
        verify(clazzRepository).delete(testClazz);
    }

    @Test
    void deleteById_WhenClazzHasStudents_ShouldThrowValidationException() {
        // Given
        testClazz.addStudent(testStudent);
        when(clazzRepository.findById(1L)).thenReturn(Optional.of(testClazz));

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> clazzService.deleteById(1L)
        );
        assertEquals("Cannot delete class with enrolled students. Please remove students first.", exception.getMessage());
        verify(clazzRepository).findById(1L);
        verify(clazzRepository, never()).delete(any());
    }

    @Test
    void deleteById_WhenClazzNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(clazzRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> clazzService.deleteById(1L)
        );
        assertEquals("Class not found with id: '1'", exception.getMessage());
        verify(clazzRepository).findById(1L);
        verify(clazzRepository, never()).delete(any());
    }

    @Test
    void existsByName_WhenValidName_ShouldReturnTrue() {
        // Given
        when(clazzRepository.existsByName("Math 101")).thenReturn(true);

        // When
        boolean result = clazzService.existsByName("Math 101");

        // Then
        assertTrue(result);
        verify(clazzRepository).existsByName("Math 101");
    }

    @Test
    void findByTeacherId_WhenValidTeacherId_ShouldReturnClasses() {
        // Given
        List<Clazz> classes = Arrays.asList(testClazz);
        when(clazzRepository.findByTeacherId(1L)).thenReturn(classes);

        // When
        List<ClazzDTO> result = clazzService.findByTeacherId(1L);

        // Then
        assertEquals(1, result.size());
        assertEquals("Math 101", result.get(0).getName());
        verify(clazzRepository).findByTeacherId(1L);
    }

    @Test
    void findByTeacherId_WhenNullTeacherId_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> clazzService.findByTeacherId(null)
        );
        assertEquals("Teacher ID cannot be null", exception.getMessage());
        verify(clazzRepository, never()).findByTeacherId(any());
    }
}