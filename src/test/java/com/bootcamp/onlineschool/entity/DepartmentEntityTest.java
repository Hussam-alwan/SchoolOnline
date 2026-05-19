package com.bootcamp.onlineschool.entity;

import com.bootcamp.onlineschool.model.Course;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Department Entity Tests")
class DepartmentEntityTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    private Department buildValidDepartment() {
        return new Department("Mathematics", "MATH", 100000.0, "Building A");
    }

    @Test
    @DisplayName("valid department passes validation")
    void validDepartmentPasses() {
        Set<ConstraintViolation<Department>> violations = validator.validate(buildValidDepartment());
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("blank name fails @Size validation")
    void blankNameFails() {
        Department d = buildValidDepartment();
        d.setName("");
        Set<ConstraintViolation<Department>> violations = validator.validate(d);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("code longer than 10 chars fails")
    void codeTooLongFails() {
        Department d = buildValidDepartment();
        d.setCode("ABCDEFGHIJK");
        Set<ConstraintViolation<Department>> violations = validator.validate(d);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("negative budget fails @Min(0)")
    void negativeBudgetFails() {
        Department d = buildValidDepartment();
        d.setBudget(-1.0);
        Set<ConstraintViolation<Department>> violations = validator.validate(d);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("addTeacher links both sides")
    void addTeacherLinksBothSides() {
        Department d = buildValidDepartment();
        Teacher t = new Teacher("Alice", "alice@school.edu", "Math", 5, 50000.0);

        d.addTeacher(t);

        assertEquals(1, d.getTeacherCount());
        assertSame(d, t.getDepartment());
    }

    @Test
    @DisplayName("removeTeacher clears both sides")
    void removeTeacherClearsBothSides() {
        Department d = buildValidDepartment();
        Teacher t = new Teacher("Alice", "alice@school.edu", "Math", 5, 50000.0);
        d.addTeacher(t);

        d.removeTeacher(t);

        assertEquals(0, d.getTeacherCount());
        assertNull(t.getDepartment());
    }

    @Test
    @DisplayName("addCourse links both sides")
    void addCourseLinksBothSides() {
        Department d = buildValidDepartment();
        Course c = new Course("Algebra", 3);

        d.addCourse(c);

        assertEquals(1, d.getCourseCount());
        assertSame(d, c.getDepartment());
    }

    @Test
    @DisplayName("removeCourse clears both sides")
    void removeCourseClearsBothSides() {
        Department d = buildValidDepartment();
        Course c = new Course("Algebra", 3);
        d.addCourse(c);

        d.removeCourse(c);

        assertEquals(0, d.getCourseCount());
        assertNull(c.getDepartment());
    }

    @Test
    @DisplayName("increaseBudget adds to current budget")
    void increaseBudgetWorks() {
        Department d = buildValidDepartment();
        d.increaseBudget(5000.0);
        assertEquals(105000.0, d.getBudget(), 0.001);
    }

    @Test
    @DisplayName("increaseBudget rejects negative amount")
    void increaseBudgetRejectsNegative() {
        Department d = buildValidDepartment();
        assertThrows(IllegalArgumentException.class, () -> d.increaseBudget(-1.0));
    }

    @Test
    @DisplayName("decreaseBudget subtracts from current budget")
    void decreaseBudgetWorks() {
        Department d = buildValidDepartment();
        d.decreaseBudget(25000.0);
        assertEquals(75000.0, d.getBudget(), 0.001);
    }

    @Test
    @DisplayName("decreaseBudget throws when result would go below zero")
    void decreaseBudgetFloorsAtZero() {
        Department d = buildValidDepartment();
        assertThrows(IllegalStateException.class, () -> d.decreaseBudget(150000.0));
    }

    @Test
    @DisplayName("equals/hashCode based on code")
    void equalsBasedOnCode() {
        Department a = new Department("Math", "MATH", 100000.0, "A");
        Department b = new Department("Mathematics", "MATH", 999.0, "B");
        Department c = new Department("Math", "PHYS", 100000.0, "A");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }
}
