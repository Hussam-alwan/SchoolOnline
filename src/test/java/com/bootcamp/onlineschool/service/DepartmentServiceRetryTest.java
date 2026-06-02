package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.entity.Department;
import com.bootcamp.onlineschool.repository.CourseRepository;
import com.bootcamp.onlineschool.repository.DepartmentRepository;
import com.bootcamp.onlineschool.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DepartmentService Optimistic Lock Retry Tests")
class DepartmentServiceRetryTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private CourseRepository courseRepository;

    private DepartmentService departmentService;

    @BeforeEach
    void setUp() {
        departmentService = new DepartmentService(
                departmentRepository, teacherRepository, courseRepository);
    }

    private Department sampleDepartment() {
        Department dept = new Department("Mathematics", "MATH", 100_000.0, "Building A");
        dept.setId(1L);
        return dept;
    }

    @Test
    @DisplayName("Retry succeeds once a transient optimistic-lock failure clears")
    void retrySucceedsAfterTransientFailure() {
        Department dept = sampleDepartment();
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));
        when(departmentRepository.saveAndFlush(any(Department.class)))
                .thenThrow(new ObjectOptimisticLockingFailureException(Department.class, 1L))
                .thenReturn(dept);

        Department result = departmentService.updateBudgetWithRetry(1L, 250_000.0, 3);

        assertEquals(250_000.0, result.getBudget(), 0.001);
        // First attempt failed, second attempt succeeded -> two saves, two reads.
        verify(departmentRepository, times(2)).saveAndFlush(any(Department.class));
        verify(departmentRepository, times(2)).findById(1L);
    }

    @Test
    @DisplayName("Retry gives up and rethrows after exhausting all attempts")
    void retryGivesUpAfterMaxAttempts() {
        Department dept = sampleDepartment();
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));
        when(departmentRepository.saveAndFlush(any(Department.class)))
                .thenThrow(new ObjectOptimisticLockingFailureException(Department.class, 1L));

        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> departmentService.updateBudgetWithRetry(1L, 250_000.0, 3));

        verify(departmentRepository, times(3)).saveAndFlush(any(Department.class));
    }

    @Test
    @DisplayName("A successful first attempt does not retry")
    void noRetryWhenFirstAttemptSucceeds() {
        Department dept = sampleDepartment();
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));
        when(departmentRepository.saveAndFlush(any(Department.class))).thenReturn(dept);

        departmentService.updateBudgetWithRetry(1L, 250_000.0, 3);

        verify(departmentRepository, times(1)).saveAndFlush(any(Department.class));
    }

    @Test
    @DisplayName("maxAttempts below 1 is rejected")
    void invalidMaxAttemptsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> departmentService.updateBudgetWithRetry(1L, 250_000.0, 0));
        verifyNoInteractions(departmentRepository);
    }
}
