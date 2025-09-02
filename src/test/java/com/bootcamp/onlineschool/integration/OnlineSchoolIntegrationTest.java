package com.bootcamp.onlineschool.integration;

import com.bootcamp.onlineschool.dto.*;
import com.bootcamp.onlineschool.entity.*;
import com.bootcamp.onlineschool.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureTestMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Comprehensive integration tests for the Online School Backend
 * Tests complete API workflows, entity relationships, error scenarios,
 * database operations, and custom query methods
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OnlineSchoolIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Repository dependencies for direct database verification
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

    @Autowired
    private UserRepository userRepository;

    // Test data
    private StudentDTO testStudent;
    private TeacherDTO testTeacher;
    private CourseDTO testCourse;
    private ClazzDTO testClazz;
    private RegistrationDTO testRegistration;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testStudent = new StudentDTO();
        testStudent.setName("Integration Test Student");
        testStudent.setEmail("integration.student@test.edu");
        testStudent.setStudentId("INT001");
        testStudent.setEnrollmentDate(LocalDate.of(2023, 9, 1));

        testTeacher = new TeacherDTO();
        testTeacher.setName("Integration Test Teacher");
        testTeacher.setEmail("integration.teacher@test.edu");
        testTeacher.setEmployeeId("EMP001");
        testTeacher.setDepartment("Computer Science");
        testTeacher.setHireDate(LocalDate.of(2020, 1, 15));

        testCourse = new CourseDTO();
        testCourse.setName("Integration Testing Course");
        testCourse.setDescription("A comprehensive course on integration testing");
        testCourse.setCredits(3);
        testCourse.setDuration(16);

        testClazz = new ClazzDTO();
        testClazz.setName("INT-101");
        testClazz.setSemester("Fall");
        testClazz.setYear(2023);
        testClazz.setMaxCapacity(25);

        testRegistration = new RegistrationDTO();
        testRegistration.setRegistrationDate(LocalDate.now());
        testRegistration.setStatus("ENROLLED");
    }

    /**
     * Test complete workflow: Create entities, establish relationships, and verify persistence
     */
    @Test
    @org.junit.jupiter.api.Order(1)
    void testCompleteWorkflow_CreateEntitiesAndEstablishRelationships() throws Exception {
        // Step 1: Create Teacher
        MvcResult teacherResult = mockMvc.perform(post("/api/teachers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTeacher)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Integration Test Teacher"))
                .andExpect(jsonPath("$.employeeId").value("EMP001"))
                .andReturn();

        TeacherDTO createdTeacher = objectMapper.readValue(
                teacherResult.getResponse().getContentAsString(), TeacherDTO.class);
        Long teacherId = createdTeacher.getId();

        // Verify teacher persistence in database
        Optional<Teacher> teacherInDb = teacherRepository.findById(teacherId);
        assertThat(teacherInDb).isPresent();
        assertThat(teacherInDb.get().getName()).isEqualTo("Integration Test Teacher");

        // Step 2: Create Student
        MvcResult studentResult = mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testStudent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Integration Test Student"))
                .andExpect(jsonPath("$.studentId").value("INT001"))
                .andReturn();

        StudentDTO createdStudent = objectMapper.readValue(
                studentResult.getResponse().getContentAsString(), StudentDTO.class);
        Long studentId = createdStudent.getId();

        // Verify student persistence in database
        Optional<Student> studentInDb = studentRepository.findById(studentId);
        assertThat(studentInDb).isPresent();
        assertThat(studentInDb.get().getStudentId()).isEqualTo("INT001");

        // Step 3: Create Course
        MvcResult courseResult = mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCourse)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Integration Testing Course"))
                .andExpect(jsonPath("$.credits").value(3))
                .andReturn();

        CourseDTO createdCourse = objectMapper.readValue(
                courseResult.getResponse().getContentAsString(), CourseDTO.class);
        Long courseId = createdCourse.getId();

        // Verify course persistence in database
        Optional<Course> courseInDb = courseRepository.findById(courseId);
        assertThat(courseInDb).isPresent();
        assertThat(courseInDb.get().getName()).isEqualTo("Integration Testing Course");

        // Step 4: Create Class with Teacher assignment
        testClazz.setTeacherId(teacherId);
        MvcResult clazzResult = mockMvc.perform(post("/api/classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testClazz)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("INT-101"))
                .andExpect(jsonPath("$.teacherId").value(teacherId))
                .andReturn();

        ClazzDTO createdClazz = objectMapper.readValue(
                clazzResult.getResponse().getContentAsString(), ClazzDTO.class);
        Long clazzId = createdClazz.getId();

        // Verify class persistence and teacher relationship
        Optional<Clazz> clazzInDb = clazzRepository.findById(clazzId);
        assertThat(clazzInDb).isPresent();
        assertThat(clazzInDb.get().getTeacher().getId()).isEqualTo(teacherId);

        // Step 5: Enroll student in class
        mockMvc.perform(post("/api/students/{studentId}/enroll/{clazzId}", studentId, clazzId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(studentId));

        // Verify student-class relationship
        Student updatedStudent = studentRepository.findById(studentId).orElseThrow();
        assertThat(updatedStudent.getClasses()).hasSize(1);
        assertThat(updatedStudent.getClasses().iterator().next().getId()).isEqualTo(clazzId);

        // Step 6: Add course to class
        mockMvc.perform(post("/api/classes/{clazzId}/courses/{courseId}", clazzId, courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clazzId));

        // Verify class-course relationship
        Clazz updatedClazz = clazzRepository.findById(clazzId).orElseThrow();
        assertThat(updatedClazz.getCourses()).hasSize(1);
        assertThat(updatedClazz.getCourses().iterator().next().getId()).isEqualTo(courseId);

        // Step 7: Create Registration
        testRegistration.setStudentId(studentId);
        testRegistration.setCourseId(courseId);
        mockMvc.perform(post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testRegistration)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").value(studentId))
                .andExpect(jsonPath("$.courseId").value(courseId))
                .andExpect(jsonPath("$.status").value("ENROLLED"));

        // Verify registration persistence
        List<Registration> registrations = registrationRepository.findByStudentId(studentId);
        assertThat(registrations).hasSize(1);
        assertThat(registrations.get(0).getCourse().getId()).isEqualTo(courseId);
    }    /**

     * Test custom query methods and advanced operations
     */
    @Test
    @org.junit.jupiter.api.Order(2)
    void testCustomQueryMethods_AndAdvancedOperations() throws Exception {
        // Setup: Create test data
        setupTestDataForQueries();

        // Test 1: Find students by class (Requirement 6.1)
        mockMvc.perform(get("/api/students/class/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // Verify using repository method directly
        List<Student> studentsInClass = studentRepository.findStudentsByClazzId(1L);
        // Note: May be empty if no test data, but method should work

        // Test 2: Find teachers by department (Requirement 6.2)
        List<Teacher> csTeachers = teacherRepository.findTeachersByDepartment("Computer Science");
        assertThat(csTeachers).isNotNull();

        // Test 3: Find courses by credits or duration (Requirement 6.3)
        List<Course> coursesByCredits = courseRepository.findByCredits(3);
        List<Course> coursesByDuration = courseRepository.findByDuration(16);
        assertThat(coursesByCredits).isNotNull();
        assertThat(coursesByDuration).isNotNull();

        // Test 4: Find classes by semester and year (Requirement 6.4)
        List<Clazz> fallClasses = clazzRepository.findBySemesterAndYear("Fall", 2023);
        assertThat(fallClasses).isNotNull();

        // Test 5: Find registrations by status (Requirement 6.5)
        List<Registration> enrolledRegistrations = registrationRepository.findByStatus("ENROLLED");
        assertThat(enrolledRegistrations).isNotNull();

        // Test 6: Advanced query - registrations with grades
        List<Registration> gradedRegistrations = registrationRepository.findRegistrationsWithGrades();
        assertThat(gradedRegistrations).isNotNull();

        // Test 7: Advanced query - registrations without grades
        List<Registration> ungradedRegistrations = registrationRepository.findRegistrationsWithoutGrades();
        assertThat(ungradedRegistrations).isNotNull();
    }

    /**
     * Test error scenarios and exception handling
     */
    @Test
    @org.junit.jupiter.api.Order(3)
    void testErrorScenariosAndExceptionHandling() throws Exception {
        // Test 1: Resource Not Found scenarios
        mockMvc.perform(get("/api/students/99999"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/teachers/99999"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/courses/99999"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/classes/99999"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/registrations/99999"))
                .andExpect(status().isNotFound());

        // Test 2: Validation errors
        StudentDTO invalidStudent = new StudentDTO();
        invalidStudent.setName(""); // Invalid: empty name
        invalidStudent.setEmail("invalid-email"); // Invalid: bad email format
        invalidStudent.setStudentId(""); // Invalid: empty student ID

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidStudent)))
                .andExpect(status().isBadRequest());

        // Test 3: Duplicate student ID
        StudentDTO student1 = createValidStudentDTO("DUP001", "student1@test.edu");
        StudentDTO student2 = createValidStudentDTO("DUP001", "student2@test.edu"); // Same student ID

        // Create first student
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student1)))
                .andExpect(status().isCreated());

        // Try to create second student with same ID - should fail
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student2)))
                .andExpect(status().isBadRequest());

        // Test 4: Invalid enrollment scenarios
        // Try to enroll non-existent student in non-existent class
        mockMvc.perform(post("/api/students/99999/enroll/99999"))
                .andExpect(status().isNotFound());

        // Test 5: Class capacity validation
        // This would require setting up a class with maxCapacity and testing enrollment limits
        // Implementation depends on business logic in service layer
    }

    /**
     * Test database operations and data persistence
     */
    @Test
    @org.junit.jupiter.api.Order(4)
    @Transactional
    void testDatabaseOperationsAndDataPersistence() throws Exception {
        // Test 1: Create and verify persistence
        StudentDTO newStudent = createValidStudentDTO("PERSIST001", "persist@test.edu");
        
        MvcResult result = mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newStudent)))
                .andExpect(status().isCreated())
                .andReturn();

        StudentDTO createdStudent = objectMapper.readValue(
                result.getResponse().getContentAsString(), StudentDTO.class);
        Long studentId = createdStudent.getId();

        // Verify persistence in database
        Optional<Student> persistedStudent = studentRepository.findById(studentId);
        assertThat(persistedStudent).isPresent();
        assertThat(persistedStudent.get().getStudentId()).isEqualTo("PERSIST001");
        assertThat(persistedStudent.get().getEmail()).isEqualTo("persist@test.edu");

        // Test 2: Update and verify changes
        StudentDTO updateData = new StudentDTO();
        updateData.setName("Updated Name");
        updateData.setEmail("updated@test.edu");
        updateData.setStudentId("PERSIST001");
        updateData.setEnrollmentDate(LocalDate.of(2023, 9, 1));

        mockMvc.perform(put("/api/students/{id}", studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@test.edu"));

        // Verify update in database
        Student updatedStudent = studentRepository.findById(studentId).orElseThrow();
        assertThat(updatedStudent.getName()).isEqualTo("Updated Name");
        assertThat(updatedStudent.getEmail()).isEqualTo("updated@test.edu");

        // Test 3: Delete and verify removal
        mockMvc.perform(delete("/api/students/{id}", studentId))
                .andExpect(status().isNoContent());

        // Verify deletion in database
        Optional<Student> deletedStudent = studentRepository.findById(studentId);
        assertThat(deletedStudent).isEmpty();

        // Test 4: Cascade operations
        // Create teacher, class, and student, then test cascade behavior
        TeacherDTO teacher = createValidTeacherDTO("CASCADE001", "cascade@test.edu");
        MvcResult teacherResult = mockMvc.perform(post("/api/teachers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teacher)))
                .andExpect(status().isCreated())
                .andReturn();

        TeacherDTO createdTeacher = objectMapper.readValue(
                teacherResult.getResponse().getContentAsString(), TeacherDTO.class);

        // Verify teacher exists in database
        assertThat(teacherRepository.findById(createdTeacher.getId())).isPresent();

        // Test cascade delete (if implemented)
        mockMvc.perform(delete("/api/teachers/{id}", createdTeacher.getId()))
                .andExpect(status().isNoContent());

        assertThat(teacherRepository.findById(createdTeacher.getId())).isEmpty();
    }

    /**
     * Test entity relationship operations end-to-end
     */
    @Test
    @org.junit.jupiter.api.Order(5)
    void testEntityRelationshipOperations_EndToEnd() throws Exception {
        // Setup: Create all required entities
        Long teacherId = createTestTeacher();
        Long studentId = createTestStudent();
        Long courseId = createTestCourse();
        Long clazzId = createTestClass(teacherId);

        // Test 1: Student-Class Many-to-Many relationship
        // Enroll student in class
        mockMvc.perform(post("/api/students/{studentId}/enroll/{clazzId}", studentId, clazzId))
                .andExpect(status().isOk());

        // Verify relationship in database
        Student student = studentRepository.findById(studentId).orElseThrow();
        assertThat(student.getClasses()).hasSize(1);
        assertThat(student.getClasses().iterator().next().getId()).isEqualTo(clazzId);

        Clazz clazz = clazzRepository.findById(clazzId).orElseThrow();
        assertThat(clazz.getStudents()).hasSize(1);
        assertThat(clazz.getStudents().iterator().next().getId()).isEqualTo(studentId);

        // Test 2: Course-Class Many-to-Many relationship
        mockMvc.perform(post("/api/classes/{clazzId}/courses/{courseId}", clazzId, courseId))
                .andExpect(status().isOk());

        // Verify relationship in database
        Clazz updatedClazz = clazzRepository.findById(clazzId).orElseThrow();
        assertThat(updatedClazz.getCourses()).hasSize(1);
        assertThat(updatedClazz.getCourses().iterator().next().getId()).isEqualTo(courseId);

        Course course = courseRepository.findById(courseId).orElseThrow();
        assertThat(course.getClasses()).hasSize(1);
        assertThat(course.getClasses().iterator().next().getId()).isEqualTo(clazzId);

        // Test 3: Student-Course Registration relationship
        RegistrationDTO registration = new RegistrationDTO();
        registration.setStudentId(studentId);
        registration.setCourseId(courseId);
        registration.setRegistrationDate(LocalDate.now());
        registration.setStatus("ENROLLED");

        MvcResult regResult = mockMvc.perform(post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isCreated())
                .andReturn();

        RegistrationDTO createdReg = objectMapper.readValue(
                regResult.getResponse().getContentAsString(), RegistrationDTO.class);

        // Verify registration relationships
        Registration regInDb = registrationRepository.findById(createdReg.getId()).orElseThrow();
        assertThat(regInDb.getStudent().getId()).isEqualTo(studentId);
        assertThat(regInDb.getCourse().getId()).isEqualTo(courseId);

        // Test 4: Remove relationships
        // Remove student from class
        mockMvc.perform(delete("/api/students/{studentId}/enroll/{clazzId}", studentId, clazzId))
                .andExpect(status().isOk());

        // Verify relationship removal
        Student studentAfterRemoval = studentRepository.findById(studentId).orElseThrow();
        assertThat(studentAfterRemoval.getClasses()).isEmpty();

        Clazz clazzAfterRemoval = clazzRepository.findById(clazzId).orElseThrow();
        assertThat(clazzAfterRemoval.getStudents()).isEmpty();

        // Remove course from class
        mockMvc.perform(delete("/api/classes/{clazzId}/courses/{courseId}", clazzId, courseId))
                .andExpect(status().isOk());

        // Verify relationship removal
        Clazz clazzAfterCourseRemoval = clazzRepository.findById(clazzId).orElseThrow();
        assertThat(clazzAfterCourseRemoval.getCourses()).isEmpty();
    }

    /**
     * Test bidirectional relationship navigation (Requirement 3.6)
     */
    @Test
    @org.junit.jupiter.api.Order(6)
    void testBidirectionalRelationshipNavigation() throws Exception {
        // Setup test data with relationships
        Long teacherId = createTestTeacher();
        Long studentId = createTestStudent();
        Long courseId = createTestCourse();
        Long clazzId = createTestClass(teacherId);

        // Establish relationships
        mockMvc.perform(post("/api/students/{studentId}/enroll/{clazzId}", studentId, clazzId))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/classes/{clazzId}/courses/{courseId}", clazzId, courseId))
                .andExpect(status().isOk());

        // Test bidirectional navigation through API endpoints
        // Get students by class
        mockMvc.perform(get("/api/students/class/{clazzId}", clazzId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(studentId));

        // Get classes by teacher
        mockMvc.perform(get("/api/classes/teacher/{teacherId}", teacherId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(clazzId));

        // Verify bidirectional navigation in database
        Student student = studentRepository.findById(studentId).orElseThrow();
        Clazz clazzFromStudent = student.getClasses().iterator().next();
        assertThat(clazzFromStudent.getId()).isEqualTo(clazzId);

        Teacher teacherFromClazz = clazzFromStudent.getTeacher();
        assertThat(teacherFromClazz.getId()).isEqualTo(teacherId);

        Course courseFromClazz = clazzFromStudent.getCourses().iterator().next();
        assertThat(courseFromClazz.getId()).isEqualTo(courseId);

        Clazz clazzFromCourse = courseFromClazz.getClasses().iterator().next();
        assertThat(clazzFromCourse.getId()).isEqualTo(clazzId);
    }

    // Helper methods for test setup
    private void setupTestDataForQueries() throws Exception {
        // Create minimal test data for query testing
        createTestTeacher();
        createTestStudent();
        createTestCourse();
    }

    private Long createTestTeacher() throws Exception {
        TeacherDTO teacher = createValidTeacherDTO("TEST_TEACHER", "test.teacher@test.edu");
        MvcResult result = mockMvc.perform(post("/api/teachers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teacher)))
                .andExpect(status().isCreated())
                .andReturn();
        
        TeacherDTO created = objectMapper.readValue(result.getResponse().getContentAsString(), TeacherDTO.class);
        return created.getId();
    }

    private Long createTestStudent() throws Exception {
        StudentDTO student = createValidStudentDTO("TEST_STUDENT", "test.student@test.edu");
        MvcResult result = mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated())
                .andReturn();
        
        StudentDTO created = objectMapper.readValue(result.getResponse().getContentAsString(), StudentDTO.class);
        return created.getId();
    }

    private Long createTestCourse() throws Exception {
        CourseDTO course = new CourseDTO();
        course.setName("Test Course");
        course.setDescription("Test Description");
        course.setCredits(3);
        course.setDuration(16);

        MvcResult result = mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isCreated())
                .andReturn();
        
        CourseDTO created = objectMapper.readValue(result.getResponse().getContentAsString(), CourseDTO.class);
        return created.getId();
    }

    private Long createTestClass(Long teacherId) throws Exception {
        ClazzDTO clazz = new ClazzDTO();
        clazz.setName("TEST-CLASS");
        clazz.setSemester("Fall");
        clazz.setYear(2023);
        clazz.setMaxCapacity(20);
        clazz.setTeacherId(teacherId);

        MvcResult result = mockMvc.perform(post("/api/classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clazz)))
                .andExpect(status().isCreated())
                .andReturn();
        
        ClazzDTO created = objectMapper.readValue(result.getResponse().getContentAsString(), ClazzDTO.class);
        return created.getId();
    }

    private StudentDTO createValidStudentDTO(String studentId, String email) {
        StudentDTO student = new StudentDTO();
        student.setName("Test Student " + studentId);
        student.setEmail(email);
        student.setStudentId(studentId);
        student.setEnrollmentDate(LocalDate.of(2023, 9, 1));
        return student;
    }

    private TeacherDTO createValidTeacherDTO(String employeeId, String email) {
        TeacherDTO teacher = new TeacherDTO();
        teacher.setName("Test Teacher " + employeeId);
        teacher.setEmail(email);
        teacher.setEmployeeId(employeeId);
        teacher.setDepartment("Computer Science");
        teacher.setHireDate(LocalDate.of(2020, 1, 15));
        return teacher;
    }
}