package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.dto.DepartmentDTO;
import com.bootcamp.onlineschool.entity.Department;
import com.bootcamp.onlineschool.entity.Teacher;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.repository.CourseRepository;
import com.bootcamp.onlineschool.repository.DepartmentRepository;
import com.bootcamp.onlineschool.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("DepartmentService Tests")
class DepartmentServiceTest {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    private DepartmentDTO sampleDto() {
        return new DepartmentDTO("Mathematics", "MATH", 100000.0, "Building A");
    }

    @Test
    @DisplayName("createDepartment persists and returns with id")
    void createDepartmentPersists() {
        Department created = departmentService.createDepartment(sampleDto());
        assertNotNull(created.getId());
        assertEquals("MATH", created.getCode());
    }

    @Test
    @DisplayName("getDepartmentById returns the persisted department")
    void getDepartmentByIdReturns() {
        Department created = departmentService.createDepartment(sampleDto());
        Department found = departmentService.getDepartmentById(created.getId());
        assertEquals(created.getId(), found.getId());
    }

    @Test
    @DisplayName("getDepartmentById throws on missing id")
    void getDepartmentByIdThrows() {
        assertThrows(ResourceNotFoundException.class,
                () -> departmentService.getDepartmentById(9999L));
    }

    @Test
    @DisplayName("updateDepartment updates fields")
    void updateDepartmentUpdates() {
        Department created = departmentService.createDepartment(sampleDto());
        DepartmentDTO update = new DepartmentDTO("Math Renamed", "MATH", 150000.0, "Building B");

        Department updated = departmentService.updateDepartment(created.getId(), update);

        assertEquals("Math Renamed", updated.getName());
        assertEquals(150000.0, updated.getBudget(), 0.001);
        assertEquals("Building B", updated.getLocation());
    }

    @Test
    @DisplayName("deleteDepartment removes the entity")
    void deleteDepartmentRemoves() {
        Department created = departmentService.createDepartment(sampleDto());
        departmentService.deleteDepartment(created.getId());
        assertFalse(departmentRepository.existsById(created.getId()));
    }

    @Test
    @DisplayName("assignTeacherToDepartment links them")
    void assignTeacher() {
        Department d = departmentService.createDepartment(sampleDto());
        Teacher t = teacherRepository.save(
                new Teacher("Alice", "alice@school.edu", "Math", 3, 50000.0));

        departmentService.assignTeacherToDepartment(d.getId(), t.getId());

        Optional<Department> reloaded = departmentService.getDepartmentWithTeachers(d.getId());
        assertTrue(reloaded.isPresent());
        assertEquals(1, reloaded.get().getTeachers().size());
    }

    @Test
    @DisplayName("assignTeacher throws when teacher missing")
    void assignTeacherMissing() {
        Department d = departmentService.createDepartment(sampleDto());
        assertThrows(ResourceNotFoundException.class,
                () -> departmentService.assignTeacherToDepartment(d.getId(), 9999L));
    }

    @Test
    @DisplayName("removeTeacherFromDepartment detaches the teacher")
    void removeTeacher() {
        Department d = departmentService.createDepartment(sampleDto());
        Teacher t = teacherRepository.save(
                new Teacher("Alice", "alice@school.edu", "Math", 3, 50000.0));
        departmentService.assignTeacherToDepartment(d.getId(), t.getId());

        departmentService.removeTeacherFromDepartment(d.getId(), t.getId());

        Optional<Department> reloaded = departmentService.getDepartmentWithTeachers(d.getId());
        assertTrue(reloaded.isPresent());
        assertEquals(0, reloaded.get().getTeachers().size());
    }

    @Test
    @DisplayName("assignCourseToDepartment links them")
    void assignCourse() {
        Department d = departmentService.createDepartment(sampleDto());
        Course c = courseRepository.save(new Course("Algebra", 3));

        departmentService.assignCourseToDepartment(d.getId(), c.getId());

        Optional<Department> reloaded = departmentService.getDepartmentWithCourses(d.getId());
        assertTrue(reloaded.isPresent());
        assertEquals(1, reloaded.get().getCourses().size());
    }

    @Test
    @DisplayName("removeCourseFromDepartment detaches the course")
    void removeCourse() {
        Department d = departmentService.createDepartment(sampleDto());
        Course c = courseRepository.save(new Course("Algebra", 3));
        departmentService.assignCourseToDepartment(d.getId(), c.getId());

        departmentService.removeCourseFromDepartment(d.getId(), c.getId());

        Optional<Department> reloaded = departmentService.getDepartmentWithCourses(d.getId());
        assertTrue(reloaded.isPresent());
        assertEquals(0, reloaded.get().getCourses().size());
    }

    @Test
    @DisplayName("getTotalBudget sums across departments")
    void totalBudget() {
        departmentService.createDepartment(new DepartmentDTO("A", "A1", 50000.0, "X"));
        departmentService.createDepartment(new DepartmentDTO("B", "B1", 75000.0, "Y"));

        assertEquals(125000.0, departmentService.getTotalBudget(), 0.001);
    }
}
