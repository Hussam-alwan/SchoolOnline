package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.dto.ClazzDTO;
import com.bootcamp.onlineschool.service.ClazzService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClazzController.class)
class ClazzControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClazzService clazzService;

    @Autowired
    private ObjectMapper objectMapper;

    private ClazzDTO clazzDTO;

    @BeforeEach
    void setUp() {
        clazzDTO = new ClazzDTO();
        clazzDTO.setId(1L);
        clazzDTO.setName("Java Bootcamp 2024");
        clazzDTO.setSemester("Spring");
        clazzDTO.setYear(2024);
        clazzDTO.setMaxCapacity(30);
        clazzDTO.setTeacherId(1L);
        clazzDTO.setTeacherName("John Doe");
    }

    @Test
    void createClass_ShouldReturnCreatedClass() throws Exception {
        when(clazzService.create(any(ClazzDTO.class))).thenReturn(clazzDTO);

        mockMvc.perform(post("/api/classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clazzDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Java Bootcamp 2024"))
                .andExpect(jsonPath("$.semester").value("Spring"))
                .andExpect(jsonPath("$.year").value(2024))
                .andExpect(jsonPath("$.maxCapacity").value(30))
                .andExpect(jsonPath("$.teacherId").value(1L));

        verify(clazzService).create(any(ClazzDTO.class));
    }

    @Test
    void getAllClasses_ShouldReturnListOfClasses() throws Exception {
        List<ClazzDTO> classes = Arrays.asList(clazzDTO);
        when(clazzService.findAll()).thenReturn(classes);

        mockMvc.perform(get("/api/classes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Java Bootcamp 2024"));

        verify(clazzService).findAll();
    }

    @Test
    void getClassById_ShouldReturnClass() throws Exception {
        when(clazzService.findById(1L)).thenReturn(clazzDTO);

        mockMvc.perform(get("/api/classes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Java Bootcamp 2024"));

        verify(clazzService).findById(1L);
    }

    @Test
    void getClassByName_ShouldReturnClass_WhenFound() throws Exception {
        when(clazzService.findByName("Java Bootcamp 2024")).thenReturn(Optional.of(clazzDTO));

        mockMvc.perform(get("/api/classes/name/Java Bootcamp 2024"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Java Bootcamp 2024"));

        verify(clazzService).findByName("Java Bootcamp 2024");
    }

    @Test
    void getClassByName_ShouldReturnNotFound_WhenNotFound() throws Exception {
        when(clazzService.findByName("Nonexistent Class")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/classes/name/Nonexistent Class"))
                .andExpect(status().isNotFound());

        verify(clazzService).findByName("Nonexistent Class");
    }

    @Test
    void getClassesBySemesterAndYear_ShouldReturnListOfClasses() throws Exception {
        List<ClazzDTO> classes = Arrays.asList(clazzDTO);
        when(clazzService.findBySemesterAndYear("Spring", 2024)).thenReturn(classes);

        mockMvc.perform(get("/api/classes/semester/Spring/year/2024"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].semester").value("Spring"))
                .andExpect(jsonPath("$[0].year").value(2024));

        verify(clazzService).findBySemesterAndYear("Spring", 2024);
    }

    @Test
    void getClassesByTeacherId_ShouldReturnListOfClasses() throws Exception {
        List<ClazzDTO> classes = Arrays.asList(clazzDTO);
        when(clazzService.findByTeacherId(1L)).thenReturn(classes);

        mockMvc.perform(get("/api/classes/teacher/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].teacherId").value(1L));

        verify(clazzService).findByTeacherId(1L);
    }

    @Test
    void updateClass_ShouldReturnUpdatedClass() throws Exception {
        ClazzDTO updatedClass = new ClazzDTO();
        updatedClass.setId(1L);
        updatedClass.setName("Advanced Java Bootcamp 2024");
        updatedClass.setSemester("Fall");
        updatedClass.setYear(2024);
        updatedClass.setMaxCapacity(25);
        updatedClass.setTeacherId(1L);

        when(clazzService.update(eq(1L), any(ClazzDTO.class))).thenReturn(updatedClass);

        mockMvc.perform(put("/api/classes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedClass)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Advanced Java Bootcamp 2024"))
                .andExpect(jsonPath("$.semester").value("Fall"))
                .andExpect(jsonPath("$.maxCapacity").value(25));

        verify(clazzService).update(eq(1L), any(ClazzDTO.class));
    }

    @Test
    void deleteClass_ShouldReturnNoContent() throws Exception {
        doNothing().when(clazzService).deleteById(1L);

        mockMvc.perform(delete("/api/classes/1"))
                .andExpect(status().isNoContent());

        verify(clazzService).deleteById(1L);
    }

    @Test
    void addStudentToClass_ShouldReturnUpdatedClass() throws Exception {
        when(clazzService.addStudent(1L, 2L)).thenReturn(clazzDTO);

        mockMvc.perform(post("/api/classes/1/students/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Java Bootcamp 2024"));

        verify(clazzService).addStudent(1L, 2L);
    }

    @Test
    void removeStudentFromClass_ShouldReturnUpdatedClass() throws Exception {
        when(clazzService.removeStudent(1L, 2L)).thenReturn(clazzDTO);

        mockMvc.perform(delete("/api/classes/1/students/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Java Bootcamp 2024"));

        verify(clazzService).removeStudent(1L, 2L);
    }

    @Test
    void addCourseToClass_ShouldReturnUpdatedClass() throws Exception {
        when(clazzService.addCourse(1L, 3L)).thenReturn(clazzDTO);

        mockMvc.perform(post("/api/classes/1/courses/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Java Bootcamp 2024"));

        verify(clazzService).addCourse(1L, 3L);
    }

    @Test
    void removeCourseFromClass_ShouldReturnUpdatedClass() throws Exception {
        when(clazzService.removeCourse(1L, 3L)).thenReturn(clazzDTO);

        mockMvc.perform(delete("/api/classes/1/courses/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Java Bootcamp 2024"));

        verify(clazzService).removeCourse(1L, 3L);
    }

    @Test
    void existsByName_ShouldReturnTrue_WhenClassExists() throws Exception {
        when(clazzService.existsByName("Java Bootcamp 2024")).thenReturn(true);

        mockMvc.perform(get("/api/classes/exists/name/Java Bootcamp 2024"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(clazzService).existsByName("Java Bootcamp 2024");
    }

    @Test
    void existsByName_ShouldReturnFalse_WhenClassDoesNotExist() throws Exception {
        when(clazzService.existsByName("Nonexistent Class")).thenReturn(false);

        mockMvc.perform(get("/api/classes/exists/name/Nonexistent Class"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(clazzService).existsByName("Nonexistent Class");
    }
}