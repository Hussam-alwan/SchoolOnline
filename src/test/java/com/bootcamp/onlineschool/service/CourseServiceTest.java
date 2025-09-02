package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.dto.CourseDTO;
import com.bootcamp.onlineschool.entity.Course;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.exception.ValidationException;
import com.bootcamp.onlineschool.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course testCourse;
    private CourseDTO testCourseDTO;

    @BeforeEach
    void setUp() {
        testCourse = new Course("Mathematics 101", "Introduction to Mathematics", 3, 45);
        testCourse.setId(1L);
        testCourse.setCreatedAt(LocalDateTime.now());
        testCourse.setUpdatedAt(LocalDateTime.now());
        testCourse.setRegistrations(new HashSet<>());

        testCourseDTO = new CourseDTO("Mathematics 101", "Introduction to Mathematics", 3, 45);
        testCourseDTO.setId(1L);
    }

    @Test
    void create_WhenValidData_ShouldCreateCourse() {
        // Given
        when(courseRepository.existsByName("Mathematics 101")).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        // When
        CourseDTO result = courseService.create(testCourseDTO);

        // Then
        assertNotNull(result);
        assertEquals("Mathematics 101", result.getName());
        assertEquals("Introduction to Mathematics", result.getDescription());
        assertEquals(3, result.getCredits());
        assertEquals(45, result.getDuration());
        verify(courseRepository).existsByName("Mathematics 101");
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void create_WhenDuplicateName_ShouldThrowValidationException() {
        // Given
        when(courseRepository.existsByName("Mathematics 101")).thenReturn(true);

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> courseService.create(testCourseDTO)
        );
        assertEquals("Course name already exists: Mathematics 101", exception.getMessage());
        verify(courseRepository).existsByName("Mathematics 101");
        verify(courseRepository, never()).save(any());
    }

    @Test
    void create_WhenNullData_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> courseService.create(null)
        );
        assertEquals("Course data cannot be null", exception.getMessage());
        verify(courseRepository, never()).save(any());
    }

    @Test
    void create_WhenNullCredits_ShouldThrowValidationException() {
        // Given
        testCourseDTO.setCredits(null);

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> courseService.create(testCourseDTO)
        );
        assertEquals("Credits is required", exception.getMessage());
        verify(courseRepository, never()).save(any());
    }

    @Test
    void create_WhenInvalidCredits_ShouldThrowValidationException() {
        // Given
        testCourseDTO.setCredits(0);

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> courseService.create(testCourseDTO)
        );
        assertEquals("Credits must be at least 1", exception.getMessage());
        verify(courseRepository, never()).save(any());
    }

    @Test
    void findAll_ShouldReturnAllCourses() {
        // Given
        List<Course> courses = Arrays.asList(testCourse);
        when(courseRepository.findAll()).thenReturn(courses);

        // When
        List<CourseDTO> result = courseService.findAll();

        // Then
        assertEquals(1, result.size());
        assertEquals("Mathematics 101", result.get(0).getName());
        verify(courseRepository).findAll();
    }

    @Test
    void findById_WhenCourseExists_ShouldReturnCourse() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        // When
        CourseDTO result = courseService.findById(1L);

        // Then
        assertEquals("Mathematics 101", result.getName());
        assertEquals(3, result.getCredits());
        verify(courseRepository).findById(1L);
    }

    @Test
    void findById_WhenCourseNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> courseService.findById(1L)
        );
        assertEquals("Course not found with id: '1'", exception.getMessage());
        verify(courseRepository).findById(1L);
    }

    @Test
    void findByName_WhenValidName_ShouldReturnCourse() {
        // Given
        when(courseRepository.findByName("Mathematics 101")).thenReturn(Optional.of(testCourse));

        // When
        Optional<CourseDTO> result = courseService.findByName("Mathematics 101");

        // Then
        assertTrue(result.isPresent());
        assertEquals("Mathematics 101", result.get().getName());
        verify(courseRepository).findByName("Mathematics 101");
    }

    @Test
    void findByName_WhenNullName_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> courseService.findByName(null)
        );
        assertEquals("Course name cannot be null or empty", exception.getMessage());
        verify(courseRepository, never()).findByName(any());
    }

    @Test
    void findByCredits_WhenValidCredits_ShouldReturnCourses() {
        // Given
        List<Course> courses = Arrays.asList(testCourse);
        when(courseRepository.findByCredits(3)).thenReturn(courses);

        // When
        List<CourseDTO> result = courseService.findByCredits(3);

        // Then
        assertEquals(1, result.size());
        assertEquals("Mathematics 101", result.get(0).getName());
        verify(courseRepository).findByCredits(3);
    }

    @Test
    void findByCredits_WhenNullCredits_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> courseService.findByCredits(null)
        );
        assertEquals("Credits cannot be null", exception.getMessage());
        verify(courseRepository, never()).findByCredits(any());
    }

    @Test
    void findByCreditsOrDuration_WhenValidData_ShouldReturnCourses() {
        // Given
        List<Course> courses = Arrays.asList(testCourse);
        when(courseRepository.findCoursesByCreditsOrDuration(3, 45)).thenReturn(courses);

        // When
        List<CourseDTO> result = courseService.findByCreditsOrDuration(3, 45);

        // Then
        assertEquals(1, result.size());
        assertEquals("Mathematics 101", result.get(0).getName());
        verify(courseRepository).findCoursesByCreditsOrDuration(3, 45);
    }

    @Test
    void findByCreditsOrDuration_WhenBothNull_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> courseService.findByCreditsOrDuration(null, null)
        );
        assertEquals("At least one of credits or duration must be provided", exception.getMessage());
        verify(courseRepository, never()).findCoursesByCreditsOrDuration(any(), any());
    }

    @Test
    void update_WhenValidData_ShouldUpdateCourse() {
        // Given
        CourseDTO updatedDTO = new CourseDTO("Physics 101", "Introduction to Physics", 4, 60);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(courseRepository.existsByName("Physics 101")).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        // When
        CourseDTO result = courseService.update(1L, updatedDTO);

        // Then
        assertEquals("Physics 101", testCourse.getName());
        assertEquals("Introduction to Physics", testCourse.getDescription());
        assertEquals(4, testCourse.getCredits());
        assertEquals(60, testCourse.getDuration());
        verify(courseRepository).findById(1L);
        verify(courseRepository).save(testCourse);
    }

    @Test
    void deleteById_WhenCourseHasNoRegistrations_ShouldDeleteCourse() {
        // Given
        testCourse.setRegistrations(new HashSet<>());
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        // When
        courseService.deleteById(1L);

        // Then
        verify(courseRepository).findById(1L);
        verify(courseRepository).delete(testCourse);
    }

    @Test
    void deleteById_WhenCourseNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> courseService.deleteById(1L)
        );
        assertEquals("Course not found with id: '1'", exception.getMessage());
        verify(courseRepository).findById(1L);
        verify(courseRepository, never()).delete(any());
    }

    @Test
    void existsByName_WhenValidName_ShouldReturnTrue() {
        // Given
        when(courseRepository.existsByName("Mathematics 101")).thenReturn(true);

        // When
        boolean result = courseService.existsByName("Mathematics 101");

        // Then
        assertTrue(result);
        verify(courseRepository).existsByName("Mathematics 101");
    }

    @Test
    void findByMinCredits_WhenValidCredits_ShouldReturnCourses() {
        // Given
        List<Course> courses = Arrays.asList(testCourse);
        when(courseRepository.findByCreditsGreaterThanEqual(3)).thenReturn(courses);

        // When
        List<CourseDTO> result = courseService.findByMinCredits(3);

        // Then
        assertEquals(1, result.size());
        assertEquals("Mathematics 101", result.get(0).getName());
        verify(courseRepository).findByCreditsGreaterThanEqual(3);
    }

    @Test
    void findByMaxDuration_WhenValidDuration_ShouldReturnCourses() {
        // Given
        List<Course> courses = Arrays.asList(testCourse);
        when(courseRepository.findByDurationLessThanEqual(60)).thenReturn(courses);

        // When
        List<CourseDTO> result = courseService.findByMaxDuration(60);

        // Then
        assertEquals(1, result.size());
        assertEquals("Mathematics 101", result.get(0).getName());
        verify(courseRepository).findByDurationLessThanEqual(60);
    }
}