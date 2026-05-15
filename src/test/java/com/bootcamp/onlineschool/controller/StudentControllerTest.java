package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.dto.StudentDTO;
import com.bootcamp.onlineschool.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@DisplayName("StudentController Tests")
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/students - creates student and returns 201 with Location")
    public void testCreateStudent() throws Exception {
        StudentDTO dto = new StudentDTO("S001", "John Doe", "john@school.edu", 3.8);
        when(studentService.addStudent(any(StudentDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", endsWith("/api/students/S001")))
                .andExpect(jsonPath("$.id").value("S001"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@school.edu"))
                .andExpect(jsonPath("$.gpa").value(3.8));
    }

    @Test
    @DisplayName("POST /api/students - returns 400 when email invalid")
    public void testCreateStudent_InvalidEmail() throws Exception {
        StudentDTO dto = new StudentDTO("S001", "John", "not-an-email", 3.5);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(studentService, never()).addStudent(any());
    }

    @Test
    @DisplayName("POST /api/students - returns 400 when GPA out of range")
    public void testCreateStudent_GpaOutOfRange() throws Exception {
        StudentDTO dto = new StudentDTO("S001", "John", "john@school.edu", 5.0);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/students - returns 400 when name blank")
    public void testCreateStudent_BlankName() throws Exception {
        StudentDTO dto = new StudentDTO("S001", "", "john@school.edu", 3.5);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/students - returns empty list")
    public void testGetAllStudents_Empty() throws Exception {
        when(studentService.getAllStudents()).thenReturn(List.of());

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /api/students - returns list of students")
    public void testGetAllStudents_WithData() throws Exception {
        when(studentService.getAllStudents()).thenReturn(List.of(
                new StudentDTO("S001", "Alice", "alice@school.edu", 3.7),
                new StudentDTO("S002", "Bob", "bob@school.edu", 3.4)
        ));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].name").value("Bob"));
    }

    @Test
    @DisplayName("GET /api/students?name=Alice - filters by name")
    public void testGetAllStudents_FilteredByName() throws Exception {
        when(studentService.findStudentsByName("Alice")).thenReturn(List.of(
                new StudentDTO("S001", "Alice Johnson", "alice@school.edu", 3.7)
        ));

        mockMvc.perform(get("/api/students").param("name", "Alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Alice Johnson"));

        verify(studentService).findStudentsByName("Alice");
        verify(studentService, never()).getAllStudents();
    }

    @Test
    @DisplayName("GET /api/students/{id} - returns student when found")
    public void testGetStudentById_Found() throws Exception {
        when(studentService.getStudentById("S001"))
                .thenReturn(new StudentDTO("S001", "Alice", "alice@school.edu", 3.7));

        mockMvc.perform(get("/api/students/S001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("S001"))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    @DisplayName("GET /api/students/{id} - returns 404 when not found")
    public void testGetStudentById_NotFound() throws Exception {
        when(studentService.getStudentById("MISSING"))
                .thenThrow(new StudentService.StudentNotFoundException("Student not found: MISSING"));

        mockMvc.perform(get("/api/students/MISSING"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/students/{id} - updates student and returns 200")
    public void testUpdateStudent_Success() throws Exception {
        StudentDTO updated = new StudentDTO("S001", "Alice Updated", "alice@school.edu", 3.9);
        when(studentService.updateStudent(eq("S001"), any(StudentDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/students/S001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Updated"))
                .andExpect(jsonPath("$.gpa").value(3.9));
    }

    @Test
    @DisplayName("PUT /api/students/{id} - returns 404 when not found")
    public void testUpdateStudent_NotFound() throws Exception {
        StudentDTO dto = new StudentDTO("MISSING", "Ghost", "ghost@school.edu", 3.0);
        when(studentService.updateStudent(eq("MISSING"), any(StudentDTO.class)))
                .thenThrow(new StudentService.StudentNotFoundException("Student not found: MISSING"));

        mockMvc.perform(put("/api/students/MISSING")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/students/{id} - returns 204 on success")
    public void testDeleteStudent_Success() throws Exception {
        doNothing().when(studentService).deleteStudent("S001");

        mockMvc.perform(delete("/api/students/S001"))
                .andExpect(status().isNoContent());

        verify(studentService).deleteStudent("S001");
    }

    @Test
    @DisplayName("DELETE /api/students/{id} - returns 404 when not found")
    public void testDeleteStudent_NotFound() throws Exception {
        doThrow(new StudentService.StudentNotFoundException("Student not found: MISSING"))
                .when(studentService).deleteStudent("MISSING");

        mockMvc.perform(delete("/api/students/MISSING"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/students/high-achievers - uses default threshold 3.5")
    public void testGetHighAchievers_DefaultThreshold() throws Exception {
        when(studentService.getHighAchievers(3.5)).thenReturn(List.of(
                new StudentDTO("S001", "Alice", "alice@school.edu", 3.8),
                new StudentDTO("S002", "Bob", "bob@school.edu", 3.6)
        ));

        mockMvc.perform(get("/api/students/high-achievers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(studentService).getHighAchievers(3.5);
    }

    @Test
    @DisplayName("GET /api/students/high-achievers?gpa=3.8 - uses custom threshold")
    public void testGetHighAchievers_CustomThreshold() throws Exception {
        when(studentService.getHighAchievers(3.8)).thenReturn(List.of(
                new StudentDTO("S001", "Alice", "alice@school.edu", 3.9)
        ));

        mockMvc.perform(get("/api/students/high-achievers").param("gpa", "3.8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].gpa").value(3.9));

        verify(studentService).getHighAchievers(3.8);
    }
}
