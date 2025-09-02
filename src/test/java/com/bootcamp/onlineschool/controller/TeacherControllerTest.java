package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.dto.TeacherDTO;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.exception.ValidationException;
import com.bootcamp.onlineschool.service.TeacherService;
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

@WebMvcTest(TeacherController.class)
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @Autowired
    private ObjectMapper objectMapper;

    private TeacherDTO teacherDTO;

    @BeforeEach
    void setUp() {
        teacherDTO = new TeacherDTO();
        teacherDTO.setId(1L);
        teacherDTO.setName("John Teacher");
        teacherDTO.setEmail("john.teacher@example.com");
        teacherDTO.setEmployeeId("EMP001");
        teacherDTO.setDepartment("Computer Science");
        teacherDTO.setHireDate(LocalDate.of(2020, 1, 15));
        teacherDTO.setCreatedAt(LocalDateTime.now());
        teacherDTO.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createTeacher_WithValidData_ShouldReturnCreatedTeacher() throws Exception {
        TeacherDTO createRequest = new TeacherDTO();
        createRequest.setName("John Teacher");
        createRequest.setEmail("john.teacher@example.com");
        createRequest.setEmployeeId("EMP001");
        createRequest.setDepartment("Computer Science");
        createRequest.setHireDate(LocalDate.of(2020, 1, 15));

        when(teacherService.create(any(TeacherDTO.class))).thenReturn(teacherDTO);

        mockMvc.perform(post("/api/teachers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Teacher"))
                .andExpect(jsonPath("$.email").value("john.teacher@example.com"))
                .andExpect(jsonPath("$.employeeId").value("EMP001"))
                .andExpect(jsonPath("$.department").value("Computer Science"));

        verify(teacherService).create(any(TeacherDTO.class));
    }

    @Test
    void createTeacher_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        TeacherDTO invalidRequest = new TeacherDTO();
        invalidRequest.setName(""); // Invalid name
        invalidRequest.setEmail("invalid-email"); // Invalid email

        mockMvc.perform(post("/api/teachers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(teacherService, never()).create(any(TeacherDTO.class));
    }

    @Test
    void getAllTeachers_ShouldReturnListOfTeachers() throws Exception {
        List<TeacherDTO> teachers = Arrays.asList(teacherDTO);
        when(teacherService.findAll()).thenReturn(teachers);

        mockMvc.perform(get("/api/teachers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Teacher"))
                .andExpect(jsonPath("$[0].employeeId").value("EMP001"));

        verify(teacherService).findAll();
    }

    @Test
    void getTeacherById_WhenTeacherExists_ShouldReturnTeacher() throws Exception {
        when(teacherService.findById(1L)).thenReturn(teacherDTO);

        mockMvc.perform(get("/api/teachers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Teacher"))
                .andExpect(jsonPath("$.employeeId").value("EMP001"));

        verify(teacherService).findById(1L);
    }

    @Test
    void getTeacherById_WhenTeacherNotFound_ShouldReturnNotFound() throws Exception {
        when(teacherService.findById(1L)).thenThrow(new ResourceNotFoundException("Teacher", "id", 1L));

        mockMvc.perform(get("/api/teachers/1"))
                .andExpect(status().isNotFound());

        verify(teacherService).findById(1L);
    }

    @Test
    void getTeacherByEmployeeId_WhenTeacherExists_ShouldReturnTeacher() throws Exception {
        when(teacherService.findByEmployeeId("EMP001")).thenReturn(Optional.of(teacherDTO));

        mockMvc.perform(get("/api/teachers/employeeId/EMP001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.employeeId").value("EMP001"));

        verify(teacherService).findByEmployeeId("EMP001");
    }

    @Test
    void getTeacherByEmployeeId_WhenTeacherNotFound_ShouldReturnNotFound() throws Exception {
        when(teacherService.findByEmployeeId("EMP999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/teachers/employeeId/EMP999"))
                .andExpect(status().isNotFound());

        verify(teacherService).findByEmployeeId("EMP999");
    }

    @Test
    void getTeachersByDepartment_ShouldReturnListOfTeachers() throws Exception {
        List<TeacherDTO> teachers = Arrays.asList(teacherDTO);
        when(teacherService.findByDepartment("Computer Science")).thenReturn(teachers);

        mockMvc.perform(get("/api/teachers/department/Computer Science"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].department").value("Computer Science"));

        verify(teacherService).findByDepartment("Computer Science");
    }

    @Test
    void updateTeacher_WithValidData_ShouldReturnUpdatedTeacher() throws Exception {
        TeacherDTO updateRequest = new TeacherDTO();
        updateRequest.setName("Jane Teacher");
        updateRequest.setEmail("jane.teacher@example.com");
        updateRequest.setEmployeeId("EMP002");
        updateRequest.setDepartment("Mathematics");
        updateRequest.setHireDate(LocalDate.of(2021, 3, 1));

        TeacherDTO updatedTeacher = new TeacherDTO();
        updatedTeacher.setId(1L);
        updatedTeacher.setName("Jane Teacher");
        updatedTeacher.setEmail("jane.teacher@example.com");
        updatedTeacher.setEmployeeId("EMP002");
        updatedTeacher.setDepartment("Mathematics");
        updatedTeacher.setHireDate(LocalDate.of(2021, 3, 1));

        when(teacherService.update(eq(1L), any(TeacherDTO.class))).thenReturn(updatedTeacher);

        mockMvc.perform(put("/api/teachers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jane Teacher"))
                .andExpect(jsonPath("$.employeeId").value("EMP002"))
                .andExpect(jsonPath("$.department").value("Mathematics"));

        verify(teacherService).update(eq(1L), any(TeacherDTO.class));
    }

    @Test
    void deleteTeacher_WhenTeacherExists_ShouldReturnNoContent() throws Exception {
        doNothing().when(teacherService).deleteById(1L);

        mockMvc.perform(delete("/api/teachers/1"))
                .andExpect(status().isNoContent());

        verify(teacherService).deleteById(1L);
    }

    @Test
    void deleteTeacher_WhenTeacherHasClasses_ShouldReturnBadRequest() throws Exception {
        doThrow(new ValidationException("Cannot delete teacher with assigned classes"))
                .when(teacherService).deleteById(1L);

        mockMvc.perform(delete("/api/teachers/1"))
                .andExpect(status().isBadRequest());

        verify(teacherService).deleteById(1L);
    }

    @Test
    void assignToClass_ShouldReturnUpdatedTeacher() throws Exception {
        when(teacherService.assignToClass(1L, 1L)).thenReturn(teacherDTO);

        mockMvc.perform(post("/api/teachers/1/assign/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.employeeId").value("EMP001"));

        verify(teacherService).assignToClass(1L, 1L);
    }

    @Test
    void assignToClass_WhenTeacherNotFound_ShouldReturnNotFound() throws Exception {
        when(teacherService.assignToClass(1L, 1L))
                .thenThrow(new ResourceNotFoundException("Teacher", "id", 1L));

        mockMvc.perform(post("/api/teachers/1/assign/1"))
                .andExpect(status().isNotFound());

        verify(teacherService).assignToClass(1L, 1L);
    }

    @Test
    void assignToClass_WhenClassAlreadyHasTeacher_ShouldReturnBadRequest() throws Exception {
        when(teacherService.assignToClass(1L, 1L))
                .thenThrow(new ValidationException("Class already has a teacher assigned"));

        mockMvc.perform(post("/api/teachers/1/assign/1"))
                .andExpect(status().isBadRequest());

        verify(teacherService).assignToClass(1L, 1L);
    }

    @Test
    void removeFromClass_ShouldReturnUpdatedTeacher() throws Exception {
        when(teacherService.removeFromClass(1L, 1L)).thenReturn(teacherDTO);

        mockMvc.perform(delete("/api/teachers/1/assign/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.employeeId").value("EMP001"));

        verify(teacherService).removeFromClass(1L, 1L);
    }

    @Test
    void existsByEmployeeId_WhenTeacherExists_ShouldReturnTrue() throws Exception {
        when(teacherService.existsByEmployeeId("EMP001")).thenReturn(true);

        mockMvc.perform(get("/api/teachers/exists/employeeId/EMP001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(true));

        verify(teacherService).existsByEmployeeId("EMP001");
    }
}