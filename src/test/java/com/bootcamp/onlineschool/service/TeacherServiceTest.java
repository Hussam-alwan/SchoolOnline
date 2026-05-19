package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.dto.TeacherDTO;
import com.bootcamp.onlineschool.entity.Course;
import com.bootcamp.onlineschool.entity.Teacher;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.repository.CourseRepository;
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
@DisplayName("TeacherService Tests")
class TeacherServiceTest {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    private TeacherDTO sampleDto() {
        return new TeacherDTO("Alice", "alice@school.edu", "Math", 4, 55000.0);
    }

    @Test
    @DisplayName("createTeacher persists and assigns an id")
    void createTeacherPersists() {
        Teacher created = teacherService.createTeacher(sampleDto());
        assertNotNull(created.getId());
        assertEquals("alice@school.edu", created.getEmail());
    }

    @Test
    @DisplayName("getTeacherById returns the persisted teacher")
    void getTeacherByIdReturnsTeacher() {
        Teacher created = teacherService.createTeacher(sampleDto());
        Teacher found = teacherService.getTeacherById(created.getId());
        assertEquals(created.getId(), found.getId());
    }

    @Test
    @DisplayName("getTeacherById throws when id is unknown")
    void getTeacherByIdThrowsWhenMissing() {
        assertThrows(ResourceNotFoundException.class, () -> teacherService.getTeacherById(9999L));
    }

    @Test
    @DisplayName("assignCourseToTeacher links the teacher to the course")
    void assignCourseToTeacher() {
        Teacher teacher = teacherService.createTeacher(sampleDto());
        Course course = courseRepository.save(new Course("Algebra", 3));

        teacherService.assignCourseToTeacher(teacher.getId(), course.getId());

        Optional<Teacher> reloaded = teacherService.getTeacherWithCourses(teacher.getId());
        assertTrue(reloaded.isPresent());
        assertEquals(1, reloaded.get().getCourses().size());
    }

    @Test
    @DisplayName("assignCourseToTeacher throws when teacher does not exist")
    void assignCourseThrowsWhenTeacherMissing() {
        Course course = courseRepository.save(new Course("Algebra", 3));
        assertThrows(ResourceNotFoundException.class,
                () -> teacherService.assignCourseToTeacher(9999L, course.getId()));
    }

    @Test
    @DisplayName("assignCourseToTeacher throws when course does not exist")
    void assignCourseThrowsWhenCourseMissing() {
        Teacher teacher = teacherService.createTeacher(sampleDto());
        assertThrows(ResourceNotFoundException.class,
                () -> teacherService.assignCourseToTeacher(teacher.getId(), 9999L));
    }

    @Test
    @DisplayName("removeCourseFromTeacher detaches the course")
    void removeCourseFromTeacher() {
        Teacher teacher = teacherService.createTeacher(sampleDto());
        Course course = courseRepository.save(new Course("Algebra", 3));
        teacherService.assignCourseToTeacher(teacher.getId(), course.getId());

        teacherService.removeCourseFromTeacher(teacher.getId(), course.getId());

        Optional<Teacher> reloaded = teacherService.getTeacherWithCourses(teacher.getId());
        assertTrue(reloaded.isPresent());
        assertEquals(0, reloaded.get().getCourses().size());
    }

    @Test
    @DisplayName("getTeacherWithCourses uses JOIN FETCH and returns courses")
    void getTeacherWithCoursesFetchesCourses() {
        Teacher teacher = teacherService.createTeacher(sampleDto());
        Course course = courseRepository.save(new Course("Algebra", 3));
        teacherService.assignCourseToTeacher(teacher.getId(), course.getId());

        Optional<Teacher> result = teacherService.getTeacherWithCourses(teacher.getId());
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getCourses().size());
    }

    @Test
    @DisplayName("deleteTeacher removes the teacher")
    void deleteTeacherRemoves() {
        Teacher teacher = teacherService.createTeacher(sampleDto());
        teacherService.deleteTeacher(teacher.getId());
        assertFalse(teacherRepository.existsById(teacher.getId()));
    }

    @Test
    @DisplayName("deleteTeacher throws when id is unknown")
    void deleteTeacherThrowsWhenMissing() {
        assertThrows(ResourceNotFoundException.class, () -> teacherService.deleteTeacher(9999L));
    }

    @Test
    @DisplayName("deleteTeacher cascades to assigned courses")
    void deleteTeacherCascadesToCourses() {
        Teacher teacher = teacherService.createTeacher(sampleDto());
        Course course = courseRepository.save(new Course("Algebra", 3));
        teacherService.assignCourseToTeacher(teacher.getId(), course.getId());

        teacherService.deleteTeacher(teacher.getId());

        assertEquals(0, courseRepository.count());
    }
}
