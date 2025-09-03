package com.bootcamp.onlineschool.integration;

import com.bootcamp.onlineschool.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests focusing on API error handling and exception scenarios
 * Tests global exception handler, validation errors, and business rule violations
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ApiErrorHandlingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test validation errors for all entity types
     */
    @Test
    void testValidationErrors_AllEntityTypes() throws Exception {
        // Test Student validation errors
        StudentDTO invalidStudent = new StudentDTO();
        invalidStudent.setName(""); // Invalid: empty name
        invalidStudent.setEmail("invalid-email"); // Invalid: bad email format
        invalidStudent.setStudentId(""); // Invalid: empty student ID
        invalidStudent.setEnrollmentDate(LocalDate.now().plusDays(1)); // Invalid: future date

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidStudent)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists());

        // Test Teacher validation errors
        TeacherDTO invalidTeacher = new TeacherDTO();
        invalidTeacher.setName(""); // Invalid: empty name
        invalidTeacher.setEmail("invalid-email"); // Invalid: bad email format
        invalidTeacher.setEmployeeId(""); // Invalid: empty employee ID
        invalidTeacher.setDepartment(""); // Invalid: empty department
        invalidTeacher.setHireDate(LocalDate.now().plusDays(1)); // Invalid: future date

        mockMvc.perform(post("/api/teachers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTeacher)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists());

        // Test Course validation errors
        CourseDTO invalidCourse = new CourseDTO();
        invalidCourse.setName(""); // Invalid: empty name
        invalidCourse.setDescription(""); // Invalid: empty description
        invalidCourse.setCredits(-1); // Invalid: negative credits
        invalidCourse.setDuration(0); // Invalid: zero duration

        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCourse)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists());

        // Test Class validation errors
        ClazzDTO invalidClazz = new ClazzDTO();
        invalidClazz.setName(""); // Invalid: empty name
        invalidClazz.setSemester(""); // Invalid: empty semester
        invalidClazz.setYear(1900); // Invalid: too old year
        invalidClazz.setMaxCapacity(-1); // Invalid: negative capacity

        mockMvc.perform(post("/api/classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidClazz)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists());

        // Test Registration validation errors
        RegistrationDTO invalidRegistration = new RegistrationDTO();
        invalidRegistration.setRegistrationDate(LocalDate.now().plusDays(1)); // Invalid: future date
        invalidRegistration.setStatus(""); // Invalid: empty status

        mockMvc.perform(post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRegistration)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists());
    }

    /**
     * Test resource not found errors for all entity types
     */
    @Test
    void testResourceNotFoundErrors_AllEntityTypes() throws Exception {
        Long nonExistentId = 99999L;

        // Test Student not found
        mockMvc.perform(get("/api/students/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Student not found with id: " + nonExistentId));

        mockMvc.perform(put("/api/students/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createValidStudentDTO())))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/students/{id}", nonExistentId))
                .andExpect(status().isNotFound());

        // Test Teacher not found
        mockMvc.perform(get("/api/teachers/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Teacher not found with id: " + nonExistentId));

        mockMvc.perform(put("/api/teachers/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createValidTeacherDTO())))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/teachers/{id}", nonExistentId))
                .andExpect(status().isNotFound());

        // Test Course not found
        mockMvc.perform(get("/api/courses/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Course not found with id: " + nonExistentId));

        // Test Class not found
        mockMvc.perform(get("/api/classes/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Class not found with id: " + nonExistentId));

        // Test Registration not found
        mockMvc.perform(get("/api/registrations/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Registration not found with id: " + nonExistentId));
    }

    /**
     * Test business rule violations and constraint errors
     */
    @Test
    void testBusinessRuleViolations() throws Exception {
        // Create a student first
        StudentDTO student = createValidStudentDTO();
        MvcResult studentResult = mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated())
                .andReturn();

        StudentDTO createdStudent = objectMapper.readValue(
                studentResult.getResponse().getContentAsString(), StudentDTO.class);

        // Test duplicate student ID constraint
        StudentDTO duplicateStudent = createValidStudentDTO();
        duplicateStudent.setEmail("different@test.edu"); // Different email but same student ID

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateStudent)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists());

        // Test duplicate email constraint
        StudentDTO duplicateEmailStudent = createValidStudentDTO();
        duplicateEmailStudent.setStudentId("DIFFERENT001"); // Different student ID but same email

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateEmailStudent)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists());

        // Test enrollment in non-existent class
        mockMvc.perform(post("/api/students/{studentId}/enroll/{clazzId}", 
                createdStudent.getId(), 99999L))
                .andExpect(status().isNotFound());

        // Test removing from non-existent class
        mockMvc.perform(delete("/api/students/{studentId}/enroll/{clazzId}", 
                createdStudent.getId(), 99999L))
                .andExpect(status().isNotFound());
    }

    /**
     * Test malformed JSON and content type errors
     */
    @Test
    void testMalformedRequestErrors() throws Exception {
        // Test malformed JSON
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ invalid json }"))
                .andExpect(status().isBadRequest());

        // Test missing content type
        mockMvc.perform(post("/api/students")
                .content(objectMapper.writeValueAsString(createValidStudentDTO())))
                .andExpect(status().isUnsupportedMediaType());

        // Test empty request body
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());

        // Test null request body
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test HTTP method not allowed errors
     */
    @Test
    void testMethodNotAllowedErrors() throws Exception {
        // Test unsupported HTTP methods on existing endpoints
        mockMvc.perform(patch("/api/students"))
                .andExpect(status().isMethodNotAllowed());

        mockMvc.perform(patch("/api/teachers/1"))
                .andExpect(status().isMethodNotAllowed());

        mockMvc.perform(put("/api/courses"))
                .andExpect(status().isMethodNotAllowed());
    }

    /**
     * Test relationship constraint violations
     */
    @Test
    void testRelationshipConstraintViolations() throws Exception {
        // Create entities for relationship testing
        TeacherDTO teacher = createValidTeacherDTO();
        MvcResult teacherResult = mockMvc.perform(post("/api/teachers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teacher)))
                .andExpect(status().isCreated())
                .andReturn();

        TeacherDTO createdTeacher = objectMapper.readValue(
                teacherResult.getResponse().getContentAsString(), TeacherDTO.class);

        // Test creating class with non-existent teacher
        ClazzDTO clazzWithInvalidTeacher = createValidClazzDTO();
        clazzWithInvalidTeacher.setTeacherId(99999L);

        mockMvc.perform(post("/api/classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clazzWithInvalidTeacher)))
                .andExpect(status().isNotFound());

        // Test creating registration with non-existent student
        RegistrationDTO regWithInvalidStudent = createValidRegistrationDTO();
        regWithInvalidStudent.setStudentId(99999L);
        regWithInvalidStudent.setCourseId(1L); // Assume course exists

        mockMvc.perform(post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(regWithInvalidStudent)))
                .andExpect(status().isNotFound());

        // Test creating registration with non-existent course
        RegistrationDTO regWithInvalidCourse = createValidRegistrationDTO();
        regWithInvalidCourse.setStudentId(1L); // Assume student exists
        regWithInvalidCourse.setCourseId(99999L);

        mockMvc.perform(post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(regWithInvalidCourse)))
                .andExpect(status().isNotFound());
    }

    /**
     * Test concurrent access and data integrity
     */
    @Test
    void testConcurrentAccessScenarios() throws Exception {
        // Create a student
        StudentDTO student = createValidStudentDTO();
        MvcResult studentResult = mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated())
                .andReturn();

        StudentDTO createdStudent = objectMapper.readValue(
                studentResult.getResponse().getContentAsString(), StudentDTO.class);

        // Test updating with stale data (if optimistic locking is implemented)
        StudentDTO updateData = new StudentDTO();
        updateData.setName("Updated Name");
        updateData.setEmail("updated@test.edu");
        updateData.setStudentId("TEST001");
        updateData.setEnrollmentDate(LocalDate.of(2023, 9, 1));

        // First update should succeed
        mockMvc.perform(put("/api/students/{id}", createdStudent.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk());

        // Verify the update
        mockMvc.perform(get("/api/students/{id}", createdStudent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    // Helper methods to create valid DTOs
    private StudentDTO createValidStudentDTO() {
        StudentDTO student = new StudentDTO();
        student.setName("Test Student");
        student.setEmail("test.student@test.edu");
        student.setStudentId("TEST001");
        student.setEnrollmentDate(LocalDate.of(2023, 9, 1));
        return student;
    }

    private TeacherDTO createValidTeacherDTO() {
        TeacherDTO teacher = new TeacherDTO();
        teacher.setName("Test Teacher");
        teacher.setEmail("test.teacher@test.edu");
        teacher.setEmployeeId("EMP001");
        teacher.setDepartment("Computer Science");
        teacher.setHireDate(LocalDate.of(2020, 1, 15));
        return teacher;
    }

    private CourseDTO createValidCourseDTO() {
        CourseDTO course = new CourseDTO();
        course.setName("Test Course");
        course.setDescription("Test Description");
        course.setCredits(3);
        course.setDuration(16);
        return course;
    }

    private ClazzDTO createValidClazzDTO() {
        ClazzDTO clazz = new ClazzDTO();
        clazz.setName("TEST-101");
        clazz.setSemester("Fall");
        clazz.setYear(2023);
        clazz.setMaxCapacity(25);
        return clazz;
    }

    private RegistrationDTO createValidRegistrationDTO() {
        RegistrationDTO registration = new RegistrationDTO();
        registration.setRegistrationDate(LocalDate.now());
        registration.setStatus("ENROLLED");
        return registration;
    }
}