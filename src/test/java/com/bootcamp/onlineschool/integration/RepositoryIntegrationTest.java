package com.bootcamp.onlineschool.integration;

import com.bootcamp.onlineschool.entity.*;
import com.bootcamp.onlineschool.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for repository layer focusing on custom query methods
 * and advanced database operations (Requirements 6.1-6.6)
 */
@DataJpaTest
@ActiveProfiles("test")
class RepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ClazzRepository clazzRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    private Student testStudent;
    private Teacher testTeacher;
    private Course testCourse;
    private Clazz testClazz;
    private Registration testRegistration;

    @BeforeEach
    void setUp() {
        // Create test entities
        testTeacher = new Teacher();
        testTeacher.setName("Repository Test Teacher");
        testTeacher.setEmail("repo.teacher@test.edu");
        testTeacher.setEmployeeId("REPO001");
        testTeacher.setDepartment("Computer Science");
        testTeacher.setHireDate(LocalDate.of(2020, 1, 15));
        testTeacher = entityManager.persistAndFlush(testTeacher);

        testStudent = new Student();
        testStudent.setName("Repository Test Student");
        testStudent.setEmail("repo.student@test.edu");
        testStudent.setStudentId("REPO001");
        testStudent.setEnrollmentDate(LocalDate.of(2023, 9, 1));
        testStudent = entityManager.persistAndFlush(testStudent);

        testCourse = new Course();
        testCourse.setName("Repository Test Course");
        testCourse.setDescription("A course for testing repository operations");
        testCourse.setCredits(3);
        testCourse.setDuration(16);
        testCourse = entityManager.persistAndFlush(testCourse);

        testClazz = new Clazz();
        testClazz.setName("REPO-101");
        testClazz.setSemester("Fall");
        testClazz.setYear(2023);
        testClazz.setMaxCapacity(25);
        testClazz.setTeacher(testTeacher);
        testClazz = entityManager.persistAndFlush(testClazz);

        testRegistration = new Registration();
        testRegistration.setStudent(testStudent);
        testRegistration.setCourse(testCourse);
        testRegistration.setRegistrationDate(LocalDate.now());
        testRegistration.setStatus("ENROLLED");
        testRegistration = entityManager.persistAndFlush(testRegistration);

        // Establish relationships
        testStudent.addClazz(testClazz);
        testClazz.addCourse(testCourse);
        entityManager.flush();
    }

    /**
     * Test StudentRepository custom query methods (Requirement 6.1)
     */
    @Test
    void testStudentRepository_CustomQueryMethods() {
        // Test findStudentsByClazz
        List<Student> studentsInClass = studentRepository.findStudentsByClazz(testClazz);
        assertThat(studentsInClass).hasSize(1);
        assertThat(studentsInClass.get(0).getId()).isEqualTo(testStudent.getId());

        // Test findStudentsByClazzId
        List<Student> studentsInClassById = studentRepository.findStudentsByClazzId(testClazz.getId());
        assertThat(studentsInClassById).hasSize(1);
        assertThat(studentsInClassById.get(0).getId()).isEqualTo(testStudent.getId());

        // Test findByStudentId
        Optional<Student> studentByStudentId = studentRepository.findByStudentId("REPO001");
        assertThat(studentByStudentId).isPresent();
        assertThat(studentByStudentId.get().getId()).isEqualTo(testStudent.getId());

        // Test existsByStudentId
        boolean exists = studentRepository.existsByStudentId("REPO001");
        assertThat(exists).isTrue();

        boolean notExists = studentRepository.existsByStudentId("NONEXISTENT");
        assertThat(notExists).isFalse();

        // Test findByEmail
        Optional<Student> studentByEmail = studentRepository.findByEmail("repo.student@test.edu");
        assertThat(studentByEmail).isPresent();
        assertThat(studentByEmail.get().getId()).isEqualTo(testStudent.getId());
    }

    /**
     * Test TeacherRepository custom query methods (Requirement 6.2)
     */
    @Test
    void testTeacherRepository_CustomQueryMethods() {
        // Test findTeachersByDepartment
        List<Teacher> csTeachers = teacherRepository.findTeachersByDepartment("Computer Science");
        assertThat(csTeachers).hasSize(1);
        assertThat(csTeachers.get(0).getId()).isEqualTo(testTeacher.getId());

        // Test findByEmployeeId
        Optional<Teacher> teacherByEmployeeId = teacherRepository.findByEmployeeId("REPO001");
        assertThat(teacherByEmployeeId).isPresent();
        assertThat(teacherByEmployeeId.get().getId()).isEqualTo(testTeacher.getId());

        // Test existsByEmployeeId
        boolean exists = teacherRepository.existsByEmployeeId("REPO001");
        assertThat(exists).isTrue();

        // Test findByEmail
        Optional<Teacher> teacherByEmail = teacherRepository.findByEmail("repo.teacher@test.edu");
        assertThat(teacherByEmail).isPresent();
        assertThat(teacherByEmail.get().getId()).isEqualTo(testTeacher.getId());
    }

    /**
     * Test CourseRepository custom query methods (Requirement 6.3)
     */
    @Test
    void testCourseRepository_CustomQueryMethods() {
        // Test findByCredits
        List<Course> coursesByCredits = courseRepository.findByCredits(3);
        assertThat(coursesByCredits).hasSize(1);
        assertThat(coursesByCredits.get(0).getId()).isEqualTo(testCourse.getId());

        // Test findByDuration
        List<Course> coursesByDuration = courseRepository.findByDuration(16);
        assertThat(coursesByDuration).hasSize(1);
        assertThat(coursesByDuration.get(0).getId()).isEqualTo(testCourse.getId());

        // Test findCoursesByCreditsOrDuration
        List<Course> coursesByCreditsOrDuration = courseRepository.findCoursesByCreditsOrDuration(3, 16);
        assertThat(coursesByCreditsOrDuration).hasSize(1);
        assertThat(coursesByCreditsOrDuration.get(0).getId()).isEqualTo(testCourse.getId());

        // Test findByName
        Optional<Course> courseByName = courseRepository.findByName("Repository Test Course");
        assertThat(courseByName).isPresent();
        assertThat(courseByName.get().getId()).isEqualTo(testCourse.getId());
    }

    /**
     * Test ClazzRepository custom query methods (Requirement 6.4)
     */
    @Test
    void testClazzRepository_CustomQueryMethods() {
        // Test findBySemesterAndYear
        List<Clazz> fallClasses = clazzRepository.findBySemesterAndYear("Fall", 2023);
        assertThat(fallClasses).hasSize(1);
        assertThat(fallClasses.get(0).getId()).isEqualTo(testClazz.getId());

        // Test findByTeacher
        List<Clazz> classesByTeacher = clazzRepository.findByTeacher(testTeacher);
        assertThat(classesByTeacher).hasSize(1);
        assertThat(classesByTeacher.get(0).getId()).isEqualTo(testClazz.getId());

        // Test findByTeacherId
        List<Clazz> classesByTeacherId = clazzRepository.findByTeacherId(testTeacher.getId());
        assertThat(classesByTeacherId).hasSize(1);
        assertThat(classesByTeacherId.get(0).getId()).isEqualTo(testClazz.getId());

        // Test findBySemester
        List<Clazz> semesterClasses = clazzRepository.findBySemester("Fall");
        assertThat(semesterClasses).hasSize(1);
        assertThat(semesterClasses.get(0).getId()).isEqualTo(testClazz.getId());

        // Test findByYear
        List<Clazz> yearClasses = clazzRepository.findByYear(2023);
        assertThat(yearClasses).hasSize(1);
        assertThat(yearClasses.get(0).getId()).isEqualTo(testClazz.getId());
    }

    /**
     * Test RegistrationRepository custom query methods (Requirement 6.5, 6.6)
     */
    @Test
    void testRegistrationRepository_CustomQueryMethods() {
        // Test findByStatus
        List<Registration> enrolledRegistrations = registrationRepository.findByStatus("ENROLLED");
        assertThat(enrolledRegistrations).hasSize(1);
        assertThat(enrolledRegistrations.get(0).getId()).isEqualTo(testRegistration.getId());

        // Test findRegistrationsByStatus (custom query)
        List<Registration> enrolledByCustomQuery = registrationRepository.findRegistrationsByStatus("ENROLLED");
        assertThat(enrolledByCustomQuery).hasSize(1);
        assertThat(enrolledByCustomQuery.get(0).getId()).isEqualTo(testRegistration.getId());

        // Test findByStudent
        List<Registration> studentRegistrations = registrationRepository.findByStudent(testStudent);
        assertThat(studentRegistrations).hasSize(1);
        assertThat(studentRegistrations.get(0).getId()).isEqualTo(testRegistration.getId());

        // Test findByStudentId
        List<Registration> registrationsByStudentId = registrationRepository.findByStudentId(testStudent.getId());
        assertThat(registrationsByStudentId).hasSize(1);
        assertThat(registrationsByStudentId.get(0).getId()).isEqualTo(testRegistration.getId());

        // Test findByCourse
        List<Registration> courseRegistrations = registrationRepository.findByCourse(testCourse);
        assertThat(courseRegistrations).hasSize(1);
        assertThat(courseRegistrations.get(0).getId()).isEqualTo(testRegistration.getId());

        // Test findByCourseId
        List<Registration> registrationsByCourseId = registrationRepository.findByCourseId(testCourse.getId());
        assertThat(registrationsByCourseId).hasSize(1);
        assertThat(registrationsByCourseId.get(0).getId()).isEqualTo(testRegistration.getId());

        // Test findByStudentAndCourse
        Optional<Registration> regByStudentAndCourse = registrationRepository.findByStudentAndCourse(testStudent, testCourse);
        assertThat(regByStudentAndCourse).isPresent();
        assertThat(regByStudentAndCourse.get().getId()).isEqualTo(testRegistration.getId());

        // Test existsByStudentAndCourse
        boolean exists = registrationRepository.existsByStudentAndCourse(testStudent, testCourse);
        assertThat(exists).isTrue();

        // Test findRegistrationsWithoutGrades
        List<Registration> ungradedRegistrations = registrationRepository.findRegistrationsWithoutGrades();
        assertThat(ungradedRegistrations).hasSize(1);
        assertThat(ungradedRegistrations.get(0).getId()).isEqualTo(testRegistration.getId());

        // Add grade and test findRegistrationsWithGrades
        testRegistration.setGrade("A");
        entityManager.persistAndFlush(testRegistration);

        List<Registration> gradedRegistrations = registrationRepository.findRegistrationsWithGrades();
        assertThat(gradedRegistrations).hasSize(1);
        assertThat(gradedRegistrations.get(0).getId()).isEqualTo(testRegistration.getId());
        assertThat(gradedRegistrations.get(0).getGrade()).isEqualTo("A");
    }

    /**
     * Test entity relationship integrity and cascade operations
     */
    @Test
    void testEntityRelationshipIntegrity() {
        // Test bidirectional relationships
        assertThat(testStudent.getClasses()).contains(testClazz);
        assertThat(testClazz.getStudents()).contains(testStudent);

        assertThat(testClazz.getCourses()).contains(testCourse);
        assertThat(testCourse.getClasses()).contains(testClazz);

        assertThat(testClazz.getTeacher()).isEqualTo(testTeacher);

        assertThat(testRegistration.getStudent()).isEqualTo(testStudent);
        assertThat(testRegistration.getCourse()).isEqualTo(testCourse);

        // Test relationship removal
        testStudent.removeClazz(testClazz);
        entityManager.flush();

        assertThat(testStudent.getClasses()).doesNotContain(testClazz);
        assertThat(testClazz.getStudents()).doesNotContain(testStudent);

        // Test relationship re-establishment
        testStudent.addClazz(testClazz);
        entityManager.flush();

        assertThat(testStudent.getClasses()).contains(testClazz);
        assertThat(testClazz.getStudents()).contains(testStudent);
    }

    /**
     * Test complex queries and edge cases
     */
    @Test
    void testComplexQueriesAndEdgeCases() {
        // Create additional test data for complex scenarios
        Student student2 = new Student();
        student2.setName("Second Student");
        student2.setEmail("student2@test.edu");
        student2.setStudentId("REPO002");
        student2.setEnrollmentDate(LocalDate.of(2023, 9, 1));
        student2 = entityManager.persistAndFlush(student2);

        Course course2 = new Course();
        course2.setName("Second Course");
        course2.setDescription("Another test course");
        course2.setCredits(4);
        course2.setDuration(12);
        course2 = entityManager.persistAndFlush(course2);

        // Test multiple students in same class
        student2.addClazz(testClazz);
        entityManager.flush();

        List<Student> studentsInClass = studentRepository.findStudentsByClazzId(testClazz.getId());
        assertThat(studentsInClass).hasSize(2);

        // Test multiple courses with same credits
        List<Course> coursesWithSameCredits = courseRepository.findByCredits(3);
        assertThat(coursesWithSameCredits).hasSize(1);

        // Test case-insensitive status search
        Registration registration2 = new Registration();
        registration2.setStudent(student2);
        registration2.setCourse(course2);
        registration2.setRegistrationDate(LocalDate.now());
        registration2.setStatus("enrolled"); // lowercase
        entityManager.persistAndFlush(registration2);

        List<Registration> enrolledCaseInsensitive = registrationRepository.findByStatusIgnoreCase("ENROLLED");
        assertThat(enrolledCaseInsensitive).hasSize(2);

        // Test empty results
        List<Teacher> nonExistentDepartment = teacherRepository.findTeachersByDepartment("Non-existent Department");
        assertThat(nonExistentDepartment).isEmpty();

        List<Clazz> futureClasses = clazzRepository.findBySemesterAndYear("Spring", 2025);
        assertThat(futureClasses).isEmpty();
    }
}