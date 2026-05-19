package com.bootcamp.onlineschool.entity;

import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.model.Student;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Enrollment Entity Tests")
class EnrollmentEntityTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    private Enrollment buildValid() {
        Student student = new Student("STU1", "Alice", "alice@school.edu");
        Course course = new Course("Algebra", 3);
        return new Enrollment(student, course, LocalDate.of(2026, 1, 15), EnrollmentStatus.ENROLLED);
    }

    @Test
    @DisplayName("valid enrollment passes validation")
    void validPasses() {
        Enrollment e = buildValid();
        Set<ConstraintViolation<Enrollment>> v = validator.validate(e);
        assertTrue(v.isEmpty(), v.toString());
    }

    @Test
    @DisplayName("missing student fails @NotNull")
    void missingStudentFails() {
        Enrollment e = buildValid();
        e.setStudent(null);
        Set<ConstraintViolation<Enrollment>> v = validator.validate(e);
        assertFalse(v.isEmpty());
    }

    @Test
    @DisplayName("missing course fails @NotNull")
    void missingCourseFails() {
        Enrollment e = buildValid();
        e.setCourse(null);
        Set<ConstraintViolation<Enrollment>> v = validator.validate(e);
        assertFalse(v.isEmpty());
    }

    @Test
    @DisplayName("missing enrollmentDate fails @NotNull")
    void missingDateFails() {
        Enrollment e = buildValid();
        e.setEnrollmentDate(null);
        Set<ConstraintViolation<Enrollment>> v = validator.validate(e);
        assertFalse(v.isEmpty());
    }

    @Test
    @DisplayName("missing status fails @NotNull")
    void missingStatusFails() {
        Enrollment e = buildValid();
        e.setStatus(null);
        Set<ConstraintViolation<Enrollment>> v = validator.validate(e);
        assertFalse(v.isEmpty());
    }

    @Test
    @DisplayName("invalid grade fails @Pattern")
    void invalidGradeFails() {
        Enrollment e = buildValid();
        e.setGrade("Z");
        Set<ConstraintViolation<Enrollment>> v = validator.validate(e);
        assertFalse(v.isEmpty());
    }

    @Test
    @DisplayName("valid grade A passes")
    void validGradePasses() {
        Enrollment e = buildValid();
        e.setGrade("A");
        Set<ConstraintViolation<Enrollment>> v = validator.validate(e);
        assertTrue(v.isEmpty());
    }

    @Test
    @DisplayName("EnrollmentStatus enum has four values")
    void enumValues() {
        assertEquals(4, EnrollmentStatus.values().length);
        assertNotNull(EnrollmentStatus.valueOf("ENROLLED"));
        assertNotNull(EnrollmentStatus.valueOf("COMPLETED"));
        assertNotNull(EnrollmentStatus.valueOf("DROPPED"));
        assertNotNull(EnrollmentStatus.valueOf("WITHDRAWN"));
    }

    @Test
    @DisplayName("completion fields can be set")
    void completionFieldsSettable() {
        Enrollment e = buildValid();
        e.setStatus(EnrollmentStatus.COMPLETED);
        e.setGrade("B");
        e.setCompletionDate(LocalDate.of(2026, 5, 1));

        assertEquals(EnrollmentStatus.COMPLETED, e.getStatus());
        assertEquals("B", e.getGrade());
        assertEquals(LocalDate.of(2026, 5, 1), e.getCompletionDate());
    }
}
