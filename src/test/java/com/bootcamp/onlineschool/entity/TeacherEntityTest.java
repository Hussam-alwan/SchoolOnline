package com.bootcamp.onlineschool.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Teacher Entity Tests")
class TeacherEntityTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    private Teacher buildValidTeacher() {
        return new Teacher("John Doe", "john@school.edu", "Math", 5, 60000.0);
    }

    @Test
    @DisplayName("valid teacher should have no violations")
    void validTeacherPassesValidation() {
        Teacher teacher = buildValidTeacher();
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("blank name should fail @Size validation")
    void blankNameFailsValidation() {
        Teacher teacher = buildValidTeacher();
        teacher.setName("");
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("invalid email format should fail @Email validation")
    void invalidEmailFailsValidation() {
        Teacher teacher = buildValidTeacher();
        teacher.setEmail("not-an-email");
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("negative years of experience should fail @Min(0)")
    void negativeYearsFailsValidation() {
        Teacher teacher = buildValidTeacher();
        teacher.setYearsOfExperience(-1);
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("negative salary should fail @Min(0)")
    void negativeSalaryFailsValidation() {
        Teacher teacher = buildValidTeacher();
        teacher.setSalary(-100.0);
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("addCourse should link both sides of the relationship")
    void addCourseLinksBothSides() {
        Teacher teacher = buildValidTeacher();
        Course course = new Course("Algebra 101", 3);

        teacher.addCourse(course);

        assertEquals(1, teacher.getCourses().size());
        assertSame(teacher, course.getTeacher());
        assertTrue(teacher.getCourses().contains(course));
    }

    @Test
    @DisplayName("removeCourse should detach the course from the teacher")
    void removeCourseClearsBothSides() {
        Teacher teacher = buildValidTeacher();
        Course course = new Course("Algebra 101", 3);
        teacher.addCourse(course);

        teacher.removeCourse(course);

        assertTrue(teacher.getCourses().isEmpty());
        assertNull(course.getTeacher());
    }

    @Test
    @DisplayName("equals/hashCode are based on email")
    void equalsAndHashCodeBasedOnEmail() {
        Teacher a = new Teacher("A", "same@school.edu", "Math", 1, 100.0);
        Teacher b = new Teacher("B", "same@school.edu", "Physics", 9, 999.0);
        Teacher c = new Teacher("A", "other@school.edu", "Math", 1, 100.0);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    @DisplayName("toString should not include courses (no circular reference)")
    void toStringDoesNotIncludeCourses() {
        Teacher teacher = buildValidTeacher();
        teacher.addCourse(new Course("Algebra 101", 3));
        String text = teacher.toString();
        assertFalse(text.toLowerCase().contains("courses"));
    }

    @Test
    @DisplayName("courses set should be initialized to empty HashSet")
    void coursesInitializedAsEmptySet() {
        Teacher teacher = new Teacher();
        assertNotNull(teacher.getCourses());
        assertTrue(teacher.getCourses().isEmpty());
    }
}
