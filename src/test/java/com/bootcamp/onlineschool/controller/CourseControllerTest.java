package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.dto.CourseDTO;
import com.bootcamp.onlineschool.service.CourseService;
import com.bootcamp.onlineschool.service.CourseService.CourseAlreadyExistsException;
import com.bootcamp.onlineschool.service.CourseService.CourseFullException;
import com.bootcamp.onlineschool.service.CourseService.CourseNotFoundException;
import com.bootcamp.onlineschool.service.CourseService.NoStudentsEnrolledException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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

    private CourseDTO sampleCourse;
    private CourseDTO fullCourse;

    @BeforeEach
    void setUp() {
        sampleCourse = new CourseDTO("CS101", "Intro to Java", 3, "Dr. Smith", 30, 10);
        fullCourse   = new CourseDTO("CS999", "Advanced AI",   4, "Dr. Ada",    5,  5);
    }

    @Test
    @DisplayName("POST /api/courses → 201 Created with Location header")
    void createCourse_success() throws Exception {
        when(courseService.createCourseDTO(any())).thenReturn(sampleCourse);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCourse)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value("CS101"))
                .andExpect(jsonPath("$.availableSeats").value(20));
    }

    @Test
    @DisplayName("POST /api/courses with blank courseName → 400")
    void createCourse_blankName() throws Exception {
        CourseDTO invalid = new CourseDTO("C001", "", 3, "Dr. Smith", 30);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/courses with credits=0 → 400")
    void createCourse_invalidCredits() throws Exception {
        CourseDTO invalid = new CourseDTO("C002", "Some Course", 0, "Dr. Smith", 30);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/courses with duplicate ID → 409 Conflict")
    void createCourse_duplicate() throws Exception {
        when(courseService.createCourseDTO(any()))
                .thenThrow(new CourseAlreadyExistsException("Course already exists: CS101"));

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCourse)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("GET /api/courses → 200 OK with course list")
    void getAllCourses_returnsList() throws Exception {
        when(courseService.getAllCoursesDTO()).thenReturn(List.of(sampleCourse, fullCourse));

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("CS101"));
    }

    @Test
    @DisplayName("GET /api/courses with no data → 200 OK empty list")
    void getAllCourses_empty() throws Exception {
        when(courseService.getAllCoursesDTO()).thenReturn(List.of());

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /api/courses/{id} → 200 OK when course exists")
    void getCourseById_found() throws Exception {
        when(courseService.getCourseByIdDTO("CS101")).thenReturn(sampleCourse);

        mockMvc.perform(get("/api/courses/CS101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("CS101"))
                .andExpect(jsonPath("$.instructor").value("Dr. Smith"))
                .andExpect(jsonPath("$.enrolledStudents").value(10));
    }

    @Test
    @DisplayName("GET /api/courses/{id} → 404 when course does not exist")
    void getCourseById_notFound() throws Exception {
        when(courseService.getCourseByIdDTO("MISSING"))
                .thenThrow(new CourseNotFoundException("Course not found: MISSING"));

        mockMvc.perform(get("/api/courses/MISSING"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/courses/{id} → 200 OK when update succeeds")
    void updateCourse_success() throws Exception {
        CourseDTO updated = new CourseDTO("CS101", "Intro to Java v2", 4, "Dr. Smith", 30, 10);
        when(courseService.updateCourse(eq("CS101"), any())).thenReturn(updated);

        mockMvc.perform(put("/api/courses/CS101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseName").value("Intro to Java v2"))
                .andExpect(jsonPath("$.credits").value(4));
    }

    @Test
    @DisplayName("PUT /api/courses/{id} → 404 when course does not exist")
    void updateCourse_notFound() throws Exception {
        when(courseService.updateCourse(eq("MISSING"), any()))
                .thenThrow(new CourseNotFoundException("Course not found: MISSING"));

        mockMvc.perform(put("/api/courses/MISSING")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCourse)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/courses/{id} → 204 No Content on success")
    void deleteCourse_success() throws Exception {
        doNothing().when(courseService).deleteCourseOrThrow("CS101");

        mockMvc.perform(delete("/api/courses/CS101"))
                .andExpect(status().isNoContent());

        verify(courseService).deleteCourseOrThrow("CS101");
    }

    @Test
    @DisplayName("DELETE /api/courses/{id} → 404 when course does not exist")
    void deleteCourse_notFound() throws Exception {
        doThrow(new CourseNotFoundException("Course not found: GONE"))
                .when(courseService).deleteCourseOrThrow("GONE");

        mockMvc.perform(delete("/api/courses/GONE"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/courses/{id}/enroll → 200 OK with updated enrolledStudents")
    void enrollStudent_success() throws Exception {
        CourseDTO afterEnroll = new CourseDTO("CS101", "Intro to Java", 3, "Dr. Smith", 30, 11);
        when(courseService.enrollStudentDTO("CS101")).thenReturn(afterEnroll);

        mockMvc.perform(post("/api/courses/CS101/enroll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enrolledStudents").value(11))
                .andExpect(jsonPath("$.availableSeats").value(19));
    }

    @Test
    @DisplayName("POST /api/courses/{id}/enroll → 400 when course is full")
    void enrollStudent_courseFull() throws Exception {
        when(courseService.enrollStudentDTO("CS999"))
                .thenThrow(new CourseFullException("Course 'CS999' is full (5/5 seats taken)"));

        mockMvc.perform(post("/api/courses/CS999/enroll"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/courses/{id}/unenroll → 200 OK with updated enrolledStudents")
    void unenrollStudent_success() throws Exception {
        CourseDTO afterUnenroll = new CourseDTO("CS101", "Intro to Java", 3, "Dr. Smith", 30, 9);
        when(courseService.unenrollStudentDTO("CS101")).thenReturn(afterUnenroll);

        mockMvc.perform(post("/api/courses/CS101/unenroll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enrolledStudents").value(9));
    }

    @Test
    @DisplayName("POST /api/courses/{id}/unenroll → 400 when no students enrolled")
    void unenrollStudent_noStudents() throws Exception {
        when(courseService.unenrollStudentDTO("CS101"))
                .thenThrow(new NoStudentsEnrolledException("Course 'CS101' has no enrolled students"));

        mockMvc.perform(post("/api/courses/CS101/unenroll"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/courses/available → 200 OK with only non-full courses")
    void getAvailableCourses() throws Exception {
        when(courseService.getAvailableCoursesDTO()).thenReturn(List.of(sampleCourse));

        mockMvc.perform(get("/api/courses/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("CS101"));
    }

    @Test
    @DisplayName("GET /api/courses/search?name=java&minCredits=3 → 200 OK filtered results")
    void searchCourses_withBothParams() throws Exception {
        when(courseService.searchCourses("java", 3)).thenReturn(List.of(sampleCourse));

        mockMvc.perform(get("/api/courses/search")
                        .param("name", "java")
                        .param("minCredits", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].courseName").value("Intro to Java"));
    }
}
