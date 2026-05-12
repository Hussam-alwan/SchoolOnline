package com.bootcamp.onlineschool.integration;

import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.model.Department;
import com.bootcamp.onlineschool.model.Student;
import com.bootcamp.onlineschool.model.Teacher;
import com.bootcamp.onlineschool.service.CourseService;
import com.bootcamp.onlineschool.service.DepartmentService;
import com.bootcamp.onlineschool.service.StudentService;
import com.bootcamp.onlineschool.service.TeacherService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Service Integration Tests")
public class ServiceIntegrationTest {

    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        // Clean — just create
        departmentService.createDepartment("D001", "Computer Science", "Dr. Smith", 10000.0);

        teacherService.addTeacher(new Teacher("T001", "Alice", "alice@school.com", "Computer Science", 5));
        teacherService.addTeacher(new Teacher("T002", "Bob", "bob@school.com", "Computer Science", 3));
        departmentService.assignTeacherToDepartment("D001", "T001");
        departmentService.assignTeacherToDepartment("D001", "T002");

        courseService.createCourse("C001", "Java Basics", 3, "Alice", 30);
        courseService.createCourse("C002", "Data Structures", 4, "Bob", 25);

        studentService.addStudent(new Student("S001", "Charlie", "charlie@school.com", 3.5));
        studentService.addStudent(new Student("S002", "Diana", "diana@school.com", 3.8));
    }

    @AfterEach
    void tearDown() {
        // Delete everything that exists — handles any extras tests created too
        courseService.getAllCourses().forEach(c -> courseService.deleteCourse(c.getCourseId()));
        studentService.getAllStudents().forEach(s -> studentService.removeStudent(s.getStudentId()));
        teacherService.getAllTeachers().forEach(t -> teacherService.removeTeacher(t.getTeacherId()));
        departmentService.getAllDepartments().forEach(d -> departmentService.deleteDepartment(d.getId()));
    }

    @Test
    @DisplayName("Should create a complete academic structure successfully")
    public void createCourseStructure() {
        courseService.enrollStudent("C001");

        Department foundDepartment = departmentService.getDepartmentById("D001");
        assertEquals("Computer Science", foundDepartment.getName());
        assertEquals(2, foundDepartment.getTeacherCount());

        Teacher foundTeacher = teacherService.findTeacherById("T001");
        assertEquals("Alice", foundTeacher.getName());
        assertEquals("Computer Science", foundTeacher.getDepartment());

        Course foundCourse = courseService.getCourseById("C001");
        assertEquals("Java Basics", foundCourse.getCourseName());
        assertEquals(1, foundCourse.getEnrolledStudents());

        Student foundStudent = studentService.findStudentById("S001");
        assertEquals("Charlie", foundStudent.getName());
    }

    @Test
    @DisplayName("Should find all teacher belong to a specific department")
    public void findTeacherBelongToDepartment() {
        List<Teacher> foundTeachers = teacherService.findTeachersByDepartment("Computer Science");
        Department foundDepartment = departmentService.getDepartmentById("D001");

        assertEquals(2, foundTeachers.size());
        assertEquals(2, foundDepartment.getTeacherCount());
        assertTrue(foundTeachers.stream().anyMatch(t -> t.getName().equals("Alice")));
        assertTrue(foundTeachers.stream().anyMatch(t -> t.getName().equals("Bob")));
    }

    @Test
    @DisplayName("Should find all courses taught by a specific teacher")
    public void findCoursesTaughtByTeacher() {
        List<Course> foundCourses = courseService.getAllCourses().stream()
                .filter(c -> c.getInstructor().equals("Alice"))
                .toList();

        assertEquals(1, foundCourses.size());
        assertTrue(foundCourses.stream().anyMatch(c -> c.getCourseName().equals("Java Basics")));
    }

    @Test
    @DisplayName("Should find all students enrolled in a specific course")
    public void findStudentsEnrolledInCourse() {
        courseService.enrollStudent("C001");
        courseService.enrollStudent("C002");

        Department foundDepartment = departmentService.getDepartmentById("D001");
        List<String> teachers =foundDepartment.getTeachersId().stream().map(
                id->teacherService.findTeacherById(id).getName()
        ).toList();

        int totalEnrolled = courseService.getAllCourses().stream()
                .filter(c -> teachers.contains(c.getInstructor()))
                .mapToInt(Course::getEnrolledStudents)
                .sum();

        assertEquals(2, totalEnrolled);
    }

    @Test
    @DisplayName("Should update course instructor when teacher is removed")
    public void updateCourseInstructor() {
        teacherService.removeTeacher("T001");
        Department foundDepartment = departmentService.getDepartmentById("D001");
        departmentService.removeTeacherFromDepartment("D001", "T001");
        Course foundCourse = courseService.getCourseById("C001");
        foundCourse.setInstructor("Bob");

        assertThrows(TeacherService.TeacherNotFoundException.class, () -> teacherService.findTeacherById("T001"));
        assertEquals("Bob", foundCourse.getInstructor());
        assertEquals(1, foundDepartment.getTeacherCount());
    }

    @Test
    @DisplayName("Should clear teacher department references when department is deleted")
    public void clearTeacherDepartmentReferences() {
        List<String> teacherIds = departmentService.getDepartmentById("D001").getTeachersId();

        teacherIds.forEach(id->teacherService.findTeacherById(id).setDepartment(null));
        departmentService.deleteDepartment("D001");


        assertThrows(DepartmentService.DepartmentNotFoundException.class, () -> departmentService.getDepartmentById("D001"));
        assertNull(teacherService.findTeacherById("T001").getDepartment());
    }

    @Test
    @DisplayName("Should maintain consistent counts across all services")
    public void maintainConsistentCounts() {
        assertEquals(2, teacherService.getAllTeachers().size());
        assertEquals(1, departmentService.getAllDepartments().size());
        assertEquals(2, courseService.getAllCourses().size());
        assertEquals(2, studentService.getAllStudents().size());

        departmentService.deleteDepartment("D001");
        courseService.deleteCourse("C001");
        studentService.removeStudent("S001");
        teacherService.removeTeacher("T001");


        assertEquals(1, teacherService.getAllTeachers().size());
        assertEquals(0, departmentService.getAllDepartments().size());
        assertEquals(1, courseService.getAllCourses().size());
        assertEquals(1, studentService.getAllStudents().size());
    }

    @Test
    @DisplayName("Should throw exceptions for invalid operations across services")
    public void invalidOperations() {
        assertThrows(DepartmentService.DepartmentNotFoundException.class, () -> departmentService.getDepartmentById("D999"));
        assertThrows(TeacherService.TeacherNotFoundException.class, () -> teacherService.findTeacherById("T999"));
        assertThrows(CourseService.CourseNotFoundException.class, () -> courseService.getCourseById("C999"));
        assertThrows(StudentService.StudentNotFoundException.class, () -> studentService.findStudentById("S999"));

        assertThrows(DepartmentService.DepartmentAlreadyExistsException.class, () -> departmentService.createDepartment("D001", "Duplicate Department", "Dr. Duplicate", 5000.0));
        assertThrows(CourseService.CourseAlreadyExistsException.class, () -> courseService.createCourse("C001", "Duplicate Course", 3, "Alice", 30));
    }

}
