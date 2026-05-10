package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.TeacherRegistry;
import com.bootcamp.onlineschool.model.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("TeacherService Test")
class TeacherServiceTest {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherRegistry teacherRegistry;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacherRegistry.clear();
        teacher=new Teacher("T001", "John Doe", "Mathematics@shool.edu","Math",2);
    }

    @Test
    @DisplayName("Should add a teacher and increase count to 1")
    public void addTeacher() {
        teacherService.addTeacher(teacher);
        assertEquals(1, teacherService.getTotalTeachers());
    }

    @Test
    @DisplayName("Should throw exception when adding null teacher")
    public void addNullTeacher() {
        assertThrows(TeacherService.TeacherNotFoundException.class, () -> teacherService.addTeacher(null));
    }

    @Test
    @DisplayName("Should find teacher by ID")
    public void findTeacherById() {
        teacherService.addTeacher(teacher);

        Teacher found = teacherService.findTeacherById(teacher.getTeacherId());

        assertEquals(teacher.getTeacherId(), found.getTeacherId());
        assertEquals(teacher.getName(), found.getName());
    }

    @Test
    @DisplayName("Should throw exception when teacher ID not found")
    public void findTeacherByIdNotFound() {
        assertThrows(TeacherService.TeacherNotFoundException.class, () -> teacherService.findTeacherById("NonExistentID"));
    }

    @Test
    @DisplayName("Should return all teachers")
    public void getAllTeachers() {
        teacherService.addTeacher(teacher);
        teacherService.addTeacher(new Teacher("T002", "Bob Jones", "bob@school.edu", "Science", 2));
        teacherService.addTeacher(new Teacher("T003", "Carol White", "carol@school.edu", "Math", 7));

        List<Teacher> teachers = teacherService.getAllTeachers();

        assertEquals(3, teachers.size());

    }

    @Test
    @DisplayName("Should find teachers by department")
    public void findTeachersByDepartment() {
        teacherService.addTeacher(teacher);
        teacherService.addTeacher(new Teacher("T002", "Bob Jones", "bob@school.edu", "Science", 2));

        List<Teacher> teachers = teacherService.findTeachersByDepartment("Math");

        assertEquals(1, teachers.size());
    }

    @Test
    @DisplayName("Should return empty list when no teachers match")
    public void findTeachersByDepartmentNotFound() {
        teacherService.addTeacher(teacher);

        List<Teacher> teachers = teacherService.findTeachersByDepartment("NonExistentDepartment");

        assertEquals(0, teachers.size());
    }

    @Test
    @DisplayName("Should remove teacher and decrease count")
    public void removeTeacher() {
        teacherService.addTeacher(teacher);
        teacherService.removeTeacher(teacher.getTeacherId());

        assertEquals(0, teacherService.getTotalTeachers());
    }

    @Test
    @DisplayName("Should return total number of teachers")
    public void getTotalTeachers() {
        teacherService.addTeacher(teacher);
        teacherService.addTeacher(new Teacher("T002", "Bob Jones", "bob@school.edu", "Science", 2));

        int expectedTotalTeachers = teacherService.getTotalTeachers();

        assertEquals(expectedTotalTeachers, teacherService.getTotalTeachers());
    }

    @Test
    @DisplayName("Should throw exception when finding removed teacher by ID")
    public void testRemovedTeacherCannotBeFound() {
        teacherService.addTeacher(new Teacher("T001", "Alice Smith", "alice@school.edu", "Math", 5));
        teacherService.removeTeacher("T001");

        assertThrows(TeacherService.TeacherNotFoundException.class, () -> teacherService.findTeacherById("T001"));
    }
}