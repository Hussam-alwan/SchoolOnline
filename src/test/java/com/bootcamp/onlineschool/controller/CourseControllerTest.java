package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.dto.CourseDTO;
import com.bootcamp.onlineschool.service.CourseService;
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

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    private CourseDTO courseDTO;

    @BeforeEach
    void setUp() {
        courseDTO = new CourseDTO();
        courseDTO.setId(1L);
        courseDTO.setName("Java Programming");
        courseDTO.setDescription("Introduction to Java programming");
        courseDTO.setCredits(3);
        courseDTO.setDuration(40);
    }

    @Test
    void createCourse_ShouldReturnCreatedCourse() throws Exception {
        when(courseService.create(any(CourseDTO.class))).thenReturn(courseDTO);

        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courseDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Java Programming"))
                .andExpect(jsonPath("$.credits").value(3))
                .andExpect(jsonPath("$.duration").value(40));

        verify(courseService).create(any(CourseDTO.class));
    }

    @Test
    void getAllCourses_ShouldReturnListOfCourses() throws Exception {
        List<CourseDTO> courses = Arrays.asList(courseDTO);
        when(courseService.findAll()).thenReturn(courses);

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Java Programming"));

        verify(courseService).findAll();
    }

    @Test
    void getCourseById_ShouldReturnCourse() throws Exception {
        when(courseService.findById(1L)).thenReturn(courseDTO);

        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Java Programming"));

        verify(courseService).findById(1L);
    }

    @Test
    void getCourseByName_ShouldReturnCourse_WhenFound() throws Exception {
        when(courseService.findByName("Java Programming")).thenReturn(Optional.of(courseDTO));

        mockMvc.perform(get("/api/courses/name/Java Programming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Java Programming"));

        verify(courseService).findByName("Java Programming");
    }

    @Test
    void getCourseByName_ShouldReturnNotFound_WhenNotFound() throws Exception {
        when(courseService.findByName("Nonexistent Course")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/courses/name/Nonexistent Course"))
                .andExpect(status().isNotFound());

        verify(courseService).findByName("Nonexistent Course");
    }

    @Test
    void getCoursesByCredits_ShouldReturnListOfCourses() throws Exception {
        List<CourseDTO> courses = Arrays.asList(courseDTO);
        when(courseService.findByCredits(3)).thenReturn(courses);

        mockMvc.perform(get("/api/courses/credits/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].credits").value(3));

        verify(courseService).findByCredits(3);
    }

    @Test
    void getCoursesByDuration_ShouldReturnListOfCourses() throws Exception {
        List<CourseDTO> courses = Arrays.asList(courseDTO);
        when(courseService.findByDuration(40)).thenReturn(courses);

        mockMvc.perform(get("/api/courses/duration/40"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].duration").value(40));

        verify(courseService).findByDuration(40);
    }

    @Test
    void getCoursesByCreditsOrDuration_ShouldReturnListOfCourses() throws Exception {
        List<CourseDTO> courses = Arrays.asList(courseDTO);
        when(courseService.findByCreditsOrDuration(3, 40)).thenReturn(courses);

        mockMvc.perform(get("/api/courses/search?credits=3&duration=40"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].credits").value(3));

        verify(courseService).findByCreditsOrDuration(3, 40);
    }

    @Test
    void getCoursesByMinCredits_ShouldReturnListOfCourses() throws Exception {
        List<CourseDTO> courses = Arrays.asList(courseDTO);
        when(courseService.findByMinCredits(2)).thenReturn(courses);

        mockMvc.perform(get("/api/courses/min-credits/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].credits").value(3));

        verify(courseService).findByMinCredits(2);
    }

    @Test
    void getCoursesByMaxDuration_ShouldReturnListOfCourses() throws Exception {
        List<CourseDTO> courses = Arrays.asList(courseDTO);
        when(courseService.findByMaxDuration(50)).thenReturn(courses);

        mockMvc.perform(get("/api/courses/max-duration/50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].duration").value(40));

        verify(courseService).findByMaxDuration(50);
    }

    @Test
    void updateCourse_ShouldReturnUpdatedCourse() throws Exception {
        CourseDTO updatedCourse = new CourseDTO();
        updatedCourse.setId(1L);
        updatedCourse.setName("Advanced Java Programming");
        updatedCourse.setCredits(4);
        updatedCourse.setDuration(50);

        when(courseService.update(eq(1L), any(CourseDTO.class))).thenReturn(updatedCourse);

        mockMvc.perform(put("/api/courses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCourse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Advanced Java Programming"))
                .andExpect(jsonPath("$.credits").value(4));

        verify(courseService).update(eq(1L), any(CourseDTO.class));
    }

    @Test
    void deleteCourse_ShouldReturnNoContent() throws Exception {
        doNothing().when(courseService).deleteById(1L);

        mockMvc.perform(delete("/api/courses/1"))
                .andExpect(status().isNoContent());

        verify(courseService).deleteById(1L);
    }

    @Test
    void existsByName_ShouldReturnTrue_WhenCourseExists() throws Exception {
        when(courseService.existsByName("Java Programming")).thenReturn(true);

        mockMvc.perform(get("/api/courses/exists/name/Java Programming"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(courseService).existsByName("Java Programming");
    }

    @Test
    void existsByName_ShouldReturnFalse_WhenCourseDoesNotExist() throws Exception {
        when(courseService.existsByName("Nonexistent Course")).thenReturn(false);

        mockMvc.perform(get("/api/courses/exists/name/Nonexistent Course"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(courseService).existsByName("Nonexistent Course");
    }
}