package com.bootcamp.onlineschool.exception;

import com.bootcamp.onlineschool.controller.StudentController;
import com.bootcamp.onlineschool.dto.StudentDTO;
import com.bootcamp.onlineschool.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("GlobalExceptionHandler Tests")
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("ResourceNotFoundException returns 404 with proper error structure")
    public void testResourceNotFound() throws Exception {
        when(studentService.getStudentById("MISSING"))
                .thenThrow(new ResourceNotFoundException("Student not found: MISSING"));

        mockMvc.perform(get("/api/v1/students/MISSING"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Student not found: MISSING"))
                .andExpect(jsonPath("$.path").value("/api/v1/students/MISSING"))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    @DisplayName("ResourceNotFoundException on DELETE returns 404")
    public void testResourceNotFoundOnDelete() throws Exception {
        doThrow(new ResourceNotFoundException("Student not found: X"))
                .when(studentService).deleteStudent("X");

        mockMvc.perform(delete("/api/v1/students/X"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/v1/students/X"));
    }

    @Test
    @DisplayName("BadRequestException returns 400 with error message")
    public void testBadRequest() throws Exception {
        StudentDTO dto = new StudentDTO("S001", "John", "john@school.edu", 3.5);
        when(studentService.addStudent(any(StudentDTO.class)))
                .thenThrow(new BadRequestException("Invalid student data"));

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid student data"))
                .andExpect(jsonPath("$.path").value("/api/v1/students"));
    }

    @Test
    @DisplayName("ConflictException returns 409 with error message")
    public void testConflict() throws Exception {
        StudentDTO dto = new StudentDTO("S001", "John", "john@school.edu", 3.5);
        when(studentService.addStudent(any(StudentDTO.class)))
                .thenThrow(new ConflictException("Student already exists: S001"));

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Student already exists: S001"));
    }

    @Test
    @DisplayName("Validation error returns 400 with errors array containing all field violations")
    public void testValidationErrors_MultipleFields() throws Exception {
        StudentDTO dto = new StudentDTO("S001", "", "not-an-email", 5.0);

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.message").value("Invalid request body"))
                .andExpect(jsonPath("$.path").value("/api/v1/students"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasItem(containsString("email"))))
                .andExpect(jsonPath("$.errors", hasItem(containsString("gpa"))))
                .andExpect(jsonPath("$.errors", hasItem(containsString("name"))));
    }

    @Test
    @DisplayName("Validation error returns 400 with single field violation")
    public void testValidationErrors_SingleField() throws Exception {
        StudentDTO dto = new StudentDTO("S001", "John", "john@school.edu", 5.5);

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]", containsString("gpa")));
    }

    @Test
    @DisplayName("Malformed JSON returns 400 with Bad Request error")
    public void testMalformedJson() throws Exception {
        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"not-valid-json\""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Malformed JSON request"))
                .andExpect(jsonPath("$.path").value("/api/v1/students"));
    }

    @Test
    @DisplayName("Wrong type in JSON body returns 400 with malformed JSON message")
    public void testMalformedJson_WrongType() throws Exception {
        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"S001\",\"name\":\"John\",\"email\":\"a@b.c\",\"gpa\":\"not-a-number\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    @DisplayName("Unexpected RuntimeException returns 500 with generic message")
    public void testUnexpectedException() throws Exception {
        when(studentService.getAllStudents())
                .thenThrow(new RuntimeException("kaboom"));

        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"))
                .andExpect(jsonPath("$.path").value("/api/v1/students"));
    }

    @Test
    @DisplayName("Error response always includes a timestamp")
    public void testErrorResponseStructure_Timestamp() throws Exception {
        when(studentService.getStudentById(eq("X")))
                .thenThrow(new ResourceNotFoundException("nope"));

        mockMvc.perform(get("/api/v1/students/X"))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    @DisplayName("Subclass of ResourceNotFoundException is also handled as 404")
    public void testResourceNotFound_Subclass() throws Exception {
        when(studentService.getStudentById("X"))
                .thenThrow(new StudentService.StudentNotFoundException("Student not found: X"));

        mockMvc.perform(get("/api/v1/students/X"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }
}
