package com.bootcamp.onlineschool.repository;

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
@DisplayName("TeacherRepository Tests")
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    private Teacher saveTeacher(String name, String email, String department,
                                Integer years, Double salary) {
        Teacher teacher = new Teacher(name, email, department, years, salary);
        return teacherRepository.save(teacher);
    }

    @Test
    @DisplayName("save should persist teacher with generated id")
    void saveAssignsId() {
        Teacher saved = saveTeacher("Alice", "alice@school.edu", "Math", 3, 50000.0);
        assertNotNull(saved.getId());
    }

    @Test
    @DisplayName("findByEmail should return the matching teacher")
    void findByEmailReturnsTeacher() {
        saveTeacher("Bob", "bob@school.edu", "Physics", 7, 70000.0);
        Optional<Teacher> found = teacherRepository.findByEmail("bob@school.edu");
        assertTrue(found.isPresent());
        assertEquals("Bob", found.get().getName());
    }

    @Test
    @DisplayName("findByEmail returns empty for unknown email")
    void findByEmailReturnsEmpty() {
        assertTrue(teacherRepository.findByEmail("ghost@school.edu").isEmpty());
    }

    @Test
    @DisplayName("findByDepartment returns all teachers in the department")
    void findByDepartmentReturnsAll() {
        saveTeacher("A", "a@school.edu", "Math", 1, 30000.0);
        saveTeacher("B", "b@school.edu", "Math", 2, 40000.0);
        saveTeacher("C", "c@school.edu", "Physics", 3, 50000.0);

        List<Teacher> math = teacherRepository.findByDepartmentName("Math");
        assertEquals(2, math.size());
    }

    @Test
    @DisplayName("findByYearsOfExperienceGreaterThanEqual filters by experience")
    void findByYearsGreaterThanEqual() {
        saveTeacher("A", "a@school.edu", "Math", 1, 30000.0);
        saveTeacher("B", "b@school.edu", "Math", 5, 40000.0);
        saveTeacher("C", "c@school.edu", "Physics", 10, 50000.0);

        List<Teacher> result = teacherRepository.findByYearsOfExperienceGreaterThanEqual(5);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("findBySalaryBetween filters within range")
    void findBySalaryBetween() {
        saveTeacher("A", "a@school.edu", "Math", 1, 30000.0);
        saveTeacher("B", "b@school.edu", "Math", 5, 50000.0);
        saveTeacher("C", "c@school.edu", "Physics", 10, 80000.0);

        List<Teacher> result = teacherRepository.findBySalaryBetween(40000.0, 60000.0);
        assertEquals(1, result.size());
        assertEquals("B", result.getFirst().getName());
    }

    @Test
    @DisplayName("findByIdWithCourses eagerly fetches courses")
    void findByIdWithCourses() {
        Teacher teacher = saveTeacher("A", "a@school.edu", "Math", 1, 30000.0);
        teacher.addCourse(new Course("Algebra", 3));
        teacher.addCourse(new Course("Geometry", 3));
        teacherRepository.save(teacher);

        Optional<Teacher> result = teacherRepository.findByIdWithCourses(teacher.getId());
        assertTrue(result.isPresent());
        assertEquals(2, result.get().getCourses().size());
    }

    @Test
    @DisplayName("findTeachersWithMinCourses returns teachers having at least N courses")
    void findTeachersWithMinCourses() {
        Teacher a = saveTeacher("A", "a@school.edu", "Math", 1, 30000.0);
        a.addCourse(new Course("C1", 3));
        a.addCourse(new Course("C2", 3));
        teacherRepository.save(a);

        Teacher b = saveTeacher("B", "b@school.edu", "Math", 1, 30000.0);
        b.addCourse(new Course("C3", 3));
        teacherRepository.save(b);

        List<Teacher> result = teacherRepository.findTeachersWithMinCourses(2);
        assertEquals(1, result.size());
        assertEquals("A", result.getFirst().getName());
    }

    @Test
    @DisplayName("getAverageSalaryByDepartment returns the average")
    void getAverageSalaryByDepartment() {
        saveTeacher("A", "a@school.edu", "Math", 1, 40000.0);
        saveTeacher("B", "b@school.edu", "Math", 5, 60000.0);
        saveTeacher("C", "c@school.edu", "Physics", 10, 100000.0);

        Double avg = teacherRepository.getAverageSalaryByDepartment("Math");
        assertNotNull(avg);
        assertEquals(50000.0, avg, 0.01);
    }

    @Test
    @DisplayName("getAverageSalaryByDepartment returns null when no teachers match")
    void getAverageSalaryReturnsNullWhenEmpty() {
        Double avg = teacherRepository.getAverageSalaryByDepartment("Nothing");
        assertNull(avg);
    }

    @Test
    @DisplayName("deleting a teacher cascades to its courses (orphanRemoval)")
    void deleteCascadesToCourses() {
        Teacher teacher = saveTeacher("A", "a@school.edu", "Math", 1, 30000.0);
        teacher.addCourse(new Course("C1", 3));
        teacher.addCourse(new Course("C2", 3));
        Teacher saved = teacherRepository.save(teacher);

        teacherRepository.delete(saved);
        teacherRepository.flush();

        assertEquals(0, teacherRepository.count());
        assertEquals(0, courseRepository.count());
    }

    @Test
    @DisplayName("removing a course from the set deletes the course (orphanRemoval)")
    void orphanRemovalDeletesCourse() {
        Teacher teacher = saveTeacher("A", "a@school.edu", "Math", 1, 30000.0);
        Course c1 = new Course("C1", 3);
        Course c2 = new Course("C2", 3);
        teacher.addCourse(c1);
        teacher.addCourse(c2);
        Teacher saved = teacherRepository.save(teacher);
        teacherRepository.flush();

        assertEquals(2, courseRepository.count());

        saved.removeCourse(c1);
        teacherRepository.save(saved);
        teacherRepository.flush();

        assertEquals(1, courseRepository.count());
    }

    @Test
    @DisplayName("unique email constraint prevents duplicates")
    void uniqueEmailConstraint() {
        saveTeacher("A", "dup@school.edu", "Math", 1, 30000.0);
        assertThrows(Exception.class, () -> {
            saveTeacher("B", "dup@school.edu", "Physics", 2, 40000.0);
            teacherRepository.flush();
        });
    }
}
