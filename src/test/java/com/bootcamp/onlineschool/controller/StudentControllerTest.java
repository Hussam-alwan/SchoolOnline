package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.dto.StudentDTO;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.exception.ValidationException;
import com.bootcamp.onlineschool.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentDTO studentDTO;

    @BeforeEach
    void setUp() {
        studentDTO = new StudentDTO();
        studentDTO.setId(1L);
        studentDTO.setName("John Student");
        studentDTO.setEmail("john.student@example.com");
        studentDTO.setStudentId("STU001");
        studentDTO.setEnrollmentDate(LocalDate.of(2023, 9, 1));
        studentDTO.setCreatedAt(LocalDateTime.now());
        studentDTO.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createStudent_WithValidData_ShouldReturnCreatedStudent() throws Exception {
        StudentDTO createRequest = new StudentDTO();
        createRequest.setName("John Student");
        createRequest.setEmail("john.student@example.com");
        createRequest.setStudentId("STU001");
        createRequest.setEnrollmentDate(LocalDate.of(2023, 9, 1));

        when(studentService.create(any(StudentDTO.class))).thenReturn(studentDTO);

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Student"))
                .andExpect(jsonPath("$.email").value("john.student@example.com"))
                .andExpect(jsonPath("$.studentId").value("STU001"));

        verify(studentService).create(any(StudentDTO.class));
    }

    @Test
    void createStudent_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        StudentDTO invalidRequest = new StudentDTO();
        invalidRequest.setName(""); // Invalid name
        invalidRequest.setEmail("invalid-email"); // Invalid email

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(studentService, never()).create(any(StudentDTO.class));
    }

    @Test
    void getAllStudents_ShouldReturnListOfStudents() throws Exception {
        List<StudentDTO> students = Arrays.asList(studentDTO);
        when(studentService.findAll()).thenReturn(students);

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Student"))
                .andExpect(jsonPath("$[0].studentId").value("STU001"));

        verify(studentService).findAll();
    }

    @Test
    void getStudentById_WhenStudentExists_ShouldReturnStudent() throws Exception {
        when(studentService.findById(1L)).thenReturn(studentDTO);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Student"))
                .andExpect(jsonPath("$.studentId").value("STU001"));

        verify(studentService).findById(1L);
    }

    @Test
    void getStudentById_WhenStudentNotFound_ShouldReturnNotFound() throws Exception {
        when(studentService.findById(1L)).thenThrow(new ResourceNotFoundException("Student", "id", 1L));

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isNotFound());

        verify(studentService).findById(1L);
    }

    @Test
    void getStudentByStudentId_WhenStudentExists_ShouldReturnStudent() throws Exception {
        when(studentService.findByStudentId("STU001")).thenReturn(Optional.of(studentDTO));

        mockMvc.perform(get("/api/students/studentId/STU001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.studentId").value("STU001"));

        verify(studentService).findByStudentId("STU001");
    }

    @Test
    void getStudentByStudentId_WhenStudentNotFound_ShouldReturnNotFound() throws Exception {
        when(studentService.findByStudentId("STU999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/students/studentId/STU999"))
                .andExpect(status().isNotFound());

        verify(studentService).findByStudentId("STU999");
    }

    @Test
    void getStudentsByClassId_ShouldReturnListOfStudents() throws Exception {
        List<StudentDTO> students = Arrays.asList(studentDTO);
        when(studentService.findStudentsByClazzId(1L)).thenReturn(students);

        mockMvc.perform(get("/api/students/class/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].studentId").value("STU001"));

        verify(studentService).findStudentsByClazzId(1L);
    }

    @Test
    void updateStudent_WithValidData_ShouldReturnUpdatedStudent() throws Exception {
        StudentDTO updateRequest = new StudentDTO();
        updateRequest.setName("Jane Student");
        updateRequest.setEmail("jane.student@example.com");
        updateRequest.setStudentId("STU002");
        updateRequest.setEnrollmentDate(LocalDate.of(2023, 9, 1));

        StudentDTO updatedStudent = new StudentDTO();
        updatedStudent.setId(1L);
        updatedStudent.setName("Jane Student");
        updatedStudent.setEmail("jane.student@example.com");
        updatedStudent.setStudentId("STU002");
        updatedStudent.setEnrollmentDate(LocalDate.of(2023, 9, 1));

        when(studentService.update(eq(1L), any(StudentDTO.class))).thenReturn(updatedStudent);

        mockMvc.perform(put("/api/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jane Student"))
                .andExpect(jsonPath("$.studentId").value("STU002"));

        verify(studentService).update(eq(1L), any(StudentDTO.class));
    }

    @Test
    void deleteStudent_WhenStudentExists_ShouldReturnNoContent() throws Exception {
        doNothing().when(studentService).deleteById(1L);

        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());

        verify(studentService).deleteById(1L);
    }

    @Test
    void enrollInClass_ShouldReturnUpdatedStudent() throws Exception {
        when(studentService.enrollInClass(1L, 1L)).thenReturn(studentDTO);

        mockMvc.perform(post("/api/students/1/enroll/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.studentId").value("STU001"));

        verify(studentService).enrollInClass(1L, 1L);
    }

    @Test
    void enrollInClass_WhenStudentNotFound_ShouldReturnNotFound() throws Exception {
        when(studentService.enrollInClass(1L, 1L))
                .thenThrow(new ResourceNotFoundException("Student", "id", 1L));

        mockMvc.perform(post("/api/students/1/enroll/1"))
                .andExpect(status().isNotFound());

        verify(studentService).enrollInClass(1L, 1L);
    }

    @Test
    void enrollInClass_WhenClassFull_ShouldReturnBadRequest() throws Exception {
        when(studentService.enrollInClass(1L, 1L))
                .thenThrow(new ValidationException("Class has reached maximum capacity"));

        mockMvc.perform(post("/api/students/1/enroll/1"))
                .andExpect(status().isBadRequest());

        verify(studentService).enrollInClass(1L, 1L);
    }

    @Test
    void removeFromClass_ShouldReturnUpdatedStudent() throws Exception {
        when(studentService.removeFromClass(1L, 1L)).thenReturn(studentDTO);

        mockMvc.perform(delete("/api/students/1/enroll/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.studentId").value("STU001"));

        verify(studentService).removeFromClass(1L, 1L);
    }

    @Test
    void existsByStudentId_WhenStudentExists_ShouldReturnTrue() throws Exception {
        when(studentService.existsByStudentId("STU001")).thenReturn(true);

        mockMvc.perform(get("/api/students/exists/studentId/STU001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(true));

        verify(studentService).existsByStudentId("STU001");
    }
}