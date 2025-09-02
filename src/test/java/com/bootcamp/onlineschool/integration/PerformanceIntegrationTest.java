package com.bootcamp.onlineschool.integration;

import com.bootcamp.onlineschool.dto.*;
import com.bootcamp.onlineschool.entity.*;
import com.bootcamp.onlineschool.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests focusing on performance, bulk operations, and load scenarios
 * Tests system behavior under various load conditions and bulk data operations
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PerformanceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    /**
     * Test bulk creation of entities
     */
    @Test
    @Transactional
    void testBulkEntityCreation() throws Exception {
        int bulkSize = 50;
        
        // Bulk create students
        List<Long> studentIds = new ArrayList<>();
        for (int i = 1; i <= bulkSize; i++) {
            StudentDTO student = createStudentDTO("BULK" + String.format("%03d", i), 
                    "bulk.student" + i + "@test.edu");
            
            MvcResult result = mockMvc.perform(post("/api/students")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(student)))
                    .andExpect(status().isCreated())
                    .andReturn();
            
            StudentDTO created = objectMapper.readValue(result.getResponse().getContentAsString(), StudentDTO.class);
            studentIds.add(created.getId());
        }

        // Verify all students were created
        assertThat(studentIds).hasSize(bulkSize);
        assertThat(studentRepository.count()).isGreaterThanOrEqualTo(bulkSize);

        // Bulk create teachers
        List<Long> teacherIds = new ArrayList<>();
        for (int i = 1; i <= bulkSize; i++) {
            TeacherDTO teacher = createTeacherDTO("BULKEMP" + String.format("%03d", i), 
                    "bulk.teacher" + i + "@test.edu");
            
            MvcResult result = mockMvc.perform(post("/api/teachers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(teacher)))
                    .andExpect(status().isCreated())
                    .andReturn();
            
            TeacherDTO created = objectMapper.readValue(result.getResponse().getContentAsString(), TeacherDTO.class);
            teacherIds.add(created.getId());
        }

        // Verify all teachers were created
        assertThat(teacherIds).hasSize(bulkSize);
        assertThat(teacherRepository.count()).isGreaterThanOrEqualTo(bulkSize);

        // Test bulk retrieval performance
        long startTime = System.currentTimeMillis();
        
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        
        long endTime = System.currentTimeMillis();
        long retrievalTime = endTime - startTime;
        
        // Assert reasonable response time (adjust threshold as needed)
        assertThat(retrievalTime).isLessThan(5000); // 5 seconds max
    }

    /**
     * Test bulk relationship operations
     */
    @Test
    @Transactional
    void testBulkRelationshipOperations() throws Exception {
        // Create base entities
        Long teacherId = createTestTeacher();
        Long clazzId = createTestClass(teacherId);
        
        // Create multiple students
        List<Long> studentIds = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Long studentId = createTestStudent("BULK" + i, "bulk" + i + "@test.edu");
            studentIds.add(studentId);
        }

        // Bulk enroll students in class
        long startTime = System.currentTimeMillis();
        
        for (Long studentId : studentIds) {
            mockMvc.perform(post("/api/students/{studentId}/enroll/{clazzId}", studentId, clazzId))
                    .andExpect(status().isOk());
        }
        
        long endTime = System.currentTimeMillis();
        long enrollmentTime = endTime - startTime;
        
        // Verify all enrollments
        List<Student> enrolledStudents = studentRepository.findStudentsByClazzId(clazzId);
        assertThat(enrolledStudents).hasSize(20);
        
        // Assert reasonable performance
        assertThat(enrollmentTime).isLessThan(10000); // 10 seconds max for 20 enrollments

        // Test bulk unenrollment
        startTime = System.currentTimeMillis();
        
        for (Long studentId : studentIds) {
            mockMvc.perform(delete("/api/students/{studentId}/enroll/{clazzId}", studentId, clazzId))
                    .andExpect(status().isOk());
        }
        
        endTime = System.currentTimeMillis();
        long unenrollmentTime = endTime - startTime;
        
        // Verify all unenrollments
        List<Student> remainingStudents = studentRepository.findStudentsByClazzId(clazzId);
        assertThat(remainingStudents).isEmpty();
        
        // Assert reasonable performance
        assertThat(unenrollmentTime).isLessThan(10000); // 10 seconds max for 20 unenrollments
    }

    /**
     * Test concurrent operations
     */
    @Test
    void testConcurrentOperations() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        // Create base entities for concurrent testing
        Long teacherId = createTestTeacher();
        Long courseId = createTestCourse();
        Long clazzId = createTestClass(teacherId);

        // Concurrent student creation
        for (int i = 1; i <= 10; i++) {
            final int index = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    StudentDTO student = createStudentDTO("CONCURRENT" + index, 
                            "concurrent" + index + "@test.edu");
                    
                    mockMvc.perform(post("/api/students")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(student)))
                            .andExpect(status().isCreated());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, executor);
            futures.add(future);
        }

        // Wait for all concurrent operations to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // Verify all students were created successfully
        List<Student> concurrentStudents = studentRepository.findAll();
        long concurrentCount = concurrentStudents.stream()
                .filter(s -> s.getStudentId().startsWith("CONCURRENT"))
                .count();
        assertThat(concurrentCount).isEqualTo(10);

        executor.shutdown();
    }

    /**
     * Test large dataset queries and pagination
     */
    @Test
    @Transactional
    void testLargeDatasetQueries() throws Exception {
        // Create a large dataset
        int datasetSize = 100;
        
        // Create teachers in different departments
        String[] departments = {"Computer Science", "Mathematics", "Physics", "Chemistry", "Biology"};
        for (int i = 0; i < datasetSize; i++) {
            TeacherDTO teacher = createTeacherDTO("LARGE" + i, "large" + i + "@test.edu");
            teacher.setDepartment(departments[i % departments.length]);
            
            mockMvc.perform(post("/api/teachers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(teacher)))
                    .andExpect(status().isCreated());
        }

        // Test query performance on large dataset
        long startTime = System.currentTimeMillis();
        
        // Query by department
        List<Teacher> csTeachers = teacherRepository.findTeachersByDepartment("Computer Science");
        
        long endTime = System.currentTimeMillis();
        long queryTime = endTime - startTime;
        
        // Verify results
        assertThat(csTeachers).hasSizeGreaterThan(0);
        assertThat(queryTime).isLessThan(1000); // 1 second max for department query

        // Test retrieval of all teachers
        startTime = System.currentTimeMillis();
        
        mockMvc.perform(get("/api/teachers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        
        endTime = System.currentTimeMillis();
        long retrievalTime = endTime - startTime;
        
        assertThat(retrievalTime).isLessThan(3000); // 3 seconds max for full retrieval
    }

    /**
     * Test complex relationship queries performance
     */
    @Test
    @Transactional
    void testComplexRelationshipQueriesPerformance() throws Exception {
        // Setup complex relationship scenario
        Long teacherId = createTestTeacher();
        Long clazzId = createTestClass(teacherId);
        
        // Create multiple courses and students
        List<Long> courseIds = new ArrayList<>();
        List<Long> studentIds = new ArrayList<>();
        
        for (int i = 1; i <= 10; i++) {
            Long courseId = createTestCourse("Course " + i);
            courseIds.add(courseId);
            
            Long studentId = createTestStudent("COMPLEX" + i, "complex" + i + "@test.edu");
            studentIds.add(studentId);
        }

        // Establish relationships
        for (Long courseId : courseIds) {
            mockMvc.perform(post("/api/classes/{clazzId}/courses/{courseId}", clazzId, courseId))
                    .andExpect(status().isOk());
        }

        for (Long studentId : studentIds) {
            mockMvc.perform(post("/api/students/{studentId}/enroll/{clazzId}", studentId, clazzId))
                    .andExpect(status().isOk());
        }

        // Create registrations
        for (Long studentId : studentIds) {
            for (Long courseId : courseIds) {
                RegistrationDTO registration = new RegistrationDTO();
                registration.setStudentId(studentId);
                registration.setCourseId(courseId);
                registration.setRegistrationDate(LocalDate.now());
                registration.setStatus("ENROLLED");
                
                mockMvc.perform(post("/api/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registration)))
                        .andExpect(status().isCreated());
            }
        }

        // Test complex query performance
        long startTime = System.currentTimeMillis();
        
        // Query students by class
        List<Student> studentsInClass = studentRepository.findStudentsByClazzId(clazzId);
        
        // Query registrations by status
        List<Registration> enrolledRegistrations = registrationRepository.findByStatus("ENROLLED");
        
        long endTime = System.currentTimeMillis();
        long complexQueryTime = endTime - startTime;
        
        // Verify results
        assertThat(studentsInClass).hasSize(10);
        assertThat(enrolledRegistrations).hasSize(100); // 10 students * 10 courses
        assertThat(complexQueryTime).isLessThan(2000); // 2 seconds max for complex queries
    }

    /**
     * Test memory usage with large datasets
     */
    @Test
    @Transactional
    void testMemoryUsageWithLargeDatasets() throws Exception {
        Runtime runtime = Runtime.getRuntime();
        
        // Measure initial memory
        runtime.gc();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // Create a moderately large dataset
        int datasetSize = 200;
        
        for (int i = 1; i <= datasetSize; i++) {
            StudentDTO student = createStudentDTO("MEMORY" + i, "memory" + i + "@test.edu");
            
            mockMvc.perform(post("/api/students")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(student)))
                    .andExpect(status().isCreated());
        }

        // Measure memory after creation
        runtime.gc();
        long afterCreationMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // Retrieve all students to test memory usage during retrieval
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        
        // Measure memory after retrieval
        runtime.gc();
        long afterRetrievalMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // Log memory usage for analysis
        System.out.println("Initial Memory: " + initialMemory / 1024 / 1024 + " MB");
        System.out.println("After Creation: " + afterCreationMemory / 1024 / 1024 + " MB");
        System.out.println("After Retrieval: " + afterRetrievalMemory / 1024 / 1024 + " MB");
        
        // Basic memory usage assertions (adjust thresholds as needed)
        long memoryIncrease = afterRetrievalMemory - initialMemory;
        assertThat(memoryIncrease).isLessThan(100 * 1024 * 1024); // Less than 100MB increase
    }

    // Helper methods
    private Long createTestTeacher() throws Exception {
        TeacherDTO teacher = createTeacherDTO("PERF001", "perf.teacher@test.edu");
        MvcResult result = mockMvc.perform(post("/api/teachers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teacher)))
                .andExpect(status().isCreated())
                .andReturn();
        
        TeacherDTO created = objectMapper.readValue(result.getResponse().getContentAsString(), TeacherDTO.class);
        return created.getId();
    }

    private Long createTestStudent(String studentId, String email) throws Exception {
        StudentDTO student = createStudentDTO(studentId, email);
        MvcResult result = mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated())
                .andReturn();
        
        StudentDTO created = objectMapper.readValue(result.getResponse().getContentAsString(), StudentDTO.class);
        return created.getId();
    }

    private Long createTestCourse() throws Exception {
        return createTestCourse("Performance Test Course");
    }

    private Long createTestCourse(String name) throws Exception {
        CourseDTO course = new CourseDTO();
        course.setName(name);
        course.setDescription("Performance test course");
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
        clazz.setName("PERF-101");
        clazz.setSemester("Fall");
        clazz.setYear(2023);
        clazz.setMaxCapacity(100);
        clazz.setTeacherId(teacherId);

        MvcResult result = mockMvc.perform(post("/api/classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clazz)))
                .andExpect(status().isCreated())
                .andReturn();
        
        ClazzDTO created = objectMapper.readValue(result.getResponse().getContentAsString(), ClazzDTO.class);
        return created.getId();
    }

    private StudentDTO createStudentDTO(String studentId, String email) {
        StudentDTO student = new StudentDTO();
        student.setName("Performance Student " + studentId);
        student.setEmail(email);
        student.setStudentId(studentId);
        student.setEnrollmentDate(LocalDate.of(2023, 9, 1));
        return student;
    }

    private TeacherDTO createTeacherDTO(String employeeId, String email) {
        TeacherDTO teacher = new TeacherDTO();
        teacher.setName("Performance Teacher " + employeeId);
        teacher.setEmail(email);
        teacher.setEmployeeId(employeeId);
        teacher.setDepartment("Computer Science");
        teacher.setHireDate(LocalDate.of(2020, 1, 15));
        return teacher;
    }
}