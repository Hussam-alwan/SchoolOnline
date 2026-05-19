package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Department;
import com.bootcamp.onlineschool.entity.Teacher;
import com.bootcamp.onlineschool.model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@DisplayName("DepartmentRepository Tests")
class DepartmentRepositoryTest {

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

    private Department save(String name, String code, double budget) {
        return departmentRepository.save(new Department(name, code, budget, "Building"));
    }

    @Test
    @DisplayName("save assigns id")
    void saveAssignsId() {
        Department d = save("Math", "MATH", 100000.0);
        assertNotNull(d.getId());
    }

    @Test
    @DisplayName("findByCode returns the department")
    void findByCode() {
        save("Math", "MATH", 100000.0);
        Optional<Department> result = departmentRepository.findByCode("MATH");
        assertTrue(result.isPresent());
        assertEquals("Math", result.get().getName());
    }

    @Test
    @DisplayName("findByCode returns empty for unknown")
    void findByCodeEmpty() {
        assertTrue(departmentRepository.findByCode("GHOST").isEmpty());
    }

    @Test
    @DisplayName("findByName returns the department")
    void findByName() {
        save("Physics", "PHYS", 80000.0);
        Optional<Department> result = departmentRepository.findByName("Physics");
        assertTrue(result.isPresent());
        assertEquals("PHYS", result.get().getCode());
    }

    @Test
    @DisplayName("findByBudgetGreaterThan filters by budget")
    void findByBudgetGreaterThan() {
        save("A", "A1", 50000.0);
        save("B", "B1", 100000.0);
        save("C", "C1", 200000.0);

        List<Department> result = departmentRepository.findByBudgetGreaterThan(80000.0);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("findByIdWithTeachers eagerly fetches teachers")
    void findByIdWithTeachers() {
        Department d = save("Math", "MATH", 100000.0);
        Teacher t1 = new Teacher("A", "a@school.edu", "Math", 1, 30000.0);
        Teacher t2 = new Teacher("B", "b@school.edu", "Math", 5, 50000.0);
        d.addTeacher(t1);
        d.addTeacher(t2);
        departmentRepository.save(d);

        Optional<Department> result = departmentRepository.findByIdWithTeachers(d.getId());
        assertTrue(result.isPresent());
        assertEquals(2, result.get().getTeachers().size());
    }

    @Test
    @DisplayName("findByIdWithCourses eagerly fetches courses")
    void findByIdWithCourses() {
        Department d = save("Math", "MATH", 100000.0);
        d.addCourse(new Course("Algebra", 3));
        d.addCourse(new Course("Geometry", 3));
        departmentRepository.save(d);

        Optional<Department> result = departmentRepository.findByIdWithCourses(d.getId());
        assertTrue(result.isPresent());
        assertEquals(2, result.get().getCourses().size());
    }

    @Test
    @DisplayName("countTeachersByDepartment returns size of teachers")
    void countTeachersByDepartment() {
        Department d = save("Math", "MATH", 100000.0);
        d.addTeacher(new Teacher("A", "a@school.edu", "Math", 1, 30000.0));
        d.addTeacher(new Teacher("B", "b@school.edu", "Math", 5, 50000.0));
        d.addTeacher(new Teacher("C", "c@school.edu", "Math", 9, 70000.0));
        departmentRepository.save(d);

        Integer count = departmentRepository.countTeachersByDepartment(d.getId());
        assertEquals(3, count);
    }

    @Test
    @DisplayName("getTotalBudget sums all budgets")
    void getTotalBudget() {
        save("A", "A1", 50000.0);
        save("B", "B1", 100000.0);
        save("C", "C1", 200000.0);

        Double total = departmentRepository.getTotalBudget();
        assertEquals(350000.0, total, 0.001);
    }

    @Test
    @DisplayName("getTotalBudget returns 0 when no departments")
    void getTotalBudgetEmpty() {
        Double total = departmentRepository.getTotalBudget();
        assertEquals(0.0, total, 0.001);
    }

    @Test
    @DisplayName("unique code constraint prevents duplicates")
    void uniqueCodeConstraint() {
        save("Math", "MATH", 100000.0);
        assertThrows(Exception.class, () -> {
            save("Mathematics", "MATH", 50000.0);
            departmentRepository.flush();
        });
    }

    @Test
    @DisplayName("unique name constraint prevents duplicates")
    void uniqueNameConstraint() {
        save("Math", "MATH", 100000.0);
        assertThrows(Exception.class, () -> {
            save("Math", "MTH", 50000.0);
            departmentRepository.flush();
        });
    }

    @Test
    @DisplayName("deleting a department cascades to teachers")
    void deleteCascadesToTeachers() {
        Department d = save("Math", "MATH", 100000.0);
        d.addTeacher(new Teacher("A", "a@school.edu", "Math", 1, 30000.0));
        d.addTeacher(new Teacher("B", "b@school.edu", "Math", 5, 50000.0));
        Department saved = departmentRepository.save(d);

        departmentRepository.delete(saved);
        departmentRepository.flush();

        assertEquals(0, teacherRepository.count());
        assertEquals(0, departmentRepository.count());
    }
}
