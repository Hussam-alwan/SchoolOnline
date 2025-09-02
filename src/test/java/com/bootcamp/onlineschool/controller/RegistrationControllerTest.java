package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.dto.RegistrationDTO;
import com.bootcamp.onlineschool.service.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    @Autowired
    private ObjectMapper objectMapper;

    private RegistrationDTO registrationDTO;

    @BeforeEach
    void setUp() {
        registrationDTO = new RegistrationDTO();
        registrationDTO.setId(1L);
        registrationDTO.setRegistrationDate(LocalDate.now());
        registrationDTO.setStatus("ACTIVE");
        registrationDTO.setGrade("A");
        registrationDTO.setStudentId(1L);
        registrationDTO.setCourseId(1L);
        registrationDTO.setStudentName("John Smith");
        registrationDTO.setCourseName("Java Programming");
    }

    @Test
    void createRegistration_ShouldReturnCreatedRegistration() throws Exception {
        when(registrationService.create(any(RegistrationDTO.class))).thenReturn(registrationDTO);

        mockMvc.perform(post("/api/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.grade").value("A"))
                .andExpect(jsonPath("$.studentId").value(1L))
                .andExpect(jsonPath("$.courseId").value(1L));

        verify(registrationService).create(any(RegistrationDTO.class));
    }

    @Test
    void registerStudentForCourse_ShouldReturnCreatedRegistration() throws Exception {
        when(registrationService.registerStudentForCourse(1L, 1L)).thenReturn(registrationDTO);

        mockMvc.perform(post("/api/registrations/register/student/1/course/1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.studentId").value(1L))
                .andExpect(jsonPath("$.courseId").value(1L));

        verify(registrationService).registerStudentForCourse(1L, 1L);
    }

    @Test
    void getAllRegistrations_ShouldReturnListOfRegistrations() throws Exception {
        List<RegistrationDTO> registrations = Arrays.asList(registrationDTO);
        when(registrationService.findAll()).thenReturn(registrations);

        mockMvc.perform(get("/api/registrations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));

        verify(registrationService).findAll();
    }

    @Test
    void getRegistrationById_ShouldReturnRegistration() throws Exception {
        when(registrationService.findById(1L)).thenReturn(registrationDTO);

        mockMvc.perform(get("/api/registrations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(registrationService).findById(1L);
    }

    @Test
    void getRegistrationsByStudentId_ShouldReturnListOfRegistrations() throws Exception {
        List<RegistrationDTO> registrations = Arrays.asList(registrationDTO);
        when(registrationService.findByStudentId(1L)).thenReturn(registrations);

        mockMvc.perform(get("/api/registrations/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].studentId").value(1L));

        verify(registrationService).findByStudentId(1L);
    }

    @Test
    void getRegistrationsByCourseId_ShouldReturnListOfRegistrations() throws Exception {
        List<RegistrationDTO> registrations = Arrays.asList(registrationDTO);
        when(registrationService.findByCourseId(1L)).thenReturn(registrations);

        mockMvc.perform(get("/api/registrations/course/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].courseId").value(1L));

        verify(registrationService).findByCourseId(1L);
    }

    @Test
    void getRegistrationsByStatus_ShouldReturnListOfRegistrations() throws Exception {
        List<RegistrationDTO> registrations = Arrays.asList(registrationDTO);
        when(registrationService.findByStatus("ACTIVE")).thenReturn(registrations);

        mockMvc.perform(get("/api/registrations/status/ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));

        verify(registrationService).findByStatus("ACTIVE");
    }

    @Test
    void getRegistrationByStudentAndCourse_ShouldReturnRegistration_WhenFound() throws Exception {
        when(registrationService.findByStudentAndCourse(1L, 1L)).thenReturn(Optional.of(registrationDTO));

        mockMvc.perform(get("/api/registrations/student/1/course/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.studentId").value(1L))
                .andExpect(jsonPath("$.courseId").value(1L));

        verify(registrationService).findByStudentAndCourse(1L, 1L);
    }

    @Test
    void getRegistrationByStudentAndCourse_ShouldReturnNotFound_WhenNotFound() throws Exception {
        when(registrationService.findByStudentAndCourse(1L, 2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/registrations/student/1/course/2"))
                .andExpect(status().isNotFound());

        verify(registrationService).findByStudentAndCourse(1L, 2L);
    }

    @Test
    void getRegistrationsWithGrades_ShouldReturnListOfRegistrations() throws Exception {
        List<RegistrationDTO> registrations = Arrays.asList(registrationDTO);
        when(registrationService.findRegistrationsWithGrades()).thenReturn(registrations);

        mockMvc.perform(get("/api/registrations/with-grades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].grade").value("A"));

        verify(registrationService).findRegistrationsWithGrades();
    }

    @Test
    void getRegistrationsWithoutGrades_ShouldReturnListOfRegistrations() throws Exception {
        RegistrationDTO registrationWithoutGrade = new RegistrationDTO();
        registrationWithoutGrade.setId(2L);
        registrationWithoutGrade.setStatus("ACTIVE");
        registrationWithoutGrade.setStudentId(2L);
        registrationWithoutGrade.setCourseId(2L);

        List<RegistrationDTO> registrations = Arrays.asList(registrationWithoutGrade);
        when(registrationService.findRegistrationsWithoutGrades()).thenReturn(registrations);

        mockMvc.perform(get("/api/registrations/without-grades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(2L));

        verify(registrationService).findRegistrationsWithoutGrades();
    }

    @Test
    void updateRegistration_ShouldReturnUpdatedRegistration() throws Exception {
        RegistrationDTO updatedRegistration = new RegistrationDTO();
        updatedRegistration.setId(1L);
        updatedRegistration.setRegistrationDate(LocalDate.now()); // Add required field
        updatedRegistration.setStatus("COMPLETED");
        updatedRegistration.setGrade("B");
        updatedRegistration.setStudentId(1L);
        updatedRegistration.setCourseId(1L);

        when(registrationService.update(eq(1L), any(RegistrationDTO.class))).thenReturn(updatedRegistration);

        mockMvc.perform(put("/api/registrations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRegistration)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.grade").value("B"));

        verify(registrationService).update(eq(1L), any(RegistrationDTO.class));
    }

    @Test
    void updateRegistrationStatus_ShouldReturnUpdatedRegistration() throws Exception {
        RegistrationDTO updatedRegistration = new RegistrationDTO();
        updatedRegistration.setId(1L);
        updatedRegistration.setStatus("COMPLETED");

        when(registrationService.updateStatus(1L, "COMPLETED")).thenReturn(updatedRegistration);

        mockMvc.perform(patch("/api/registrations/1/status?status=COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(registrationService).updateStatus(1L, "COMPLETED");
    }

    @Test
    void updateRegistrationGrade_ShouldReturnUpdatedRegistration() throws Exception {
        RegistrationDTO updatedRegistration = new RegistrationDTO();
        updatedRegistration.setId(1L);
        updatedRegistration.setGrade("B+");

        when(registrationService.updateGrade(1L, "B+")).thenReturn(updatedRegistration);

        mockMvc.perform(patch("/api/registrations/1/grade?grade=B+"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.grade").value("B+"));

        verify(registrationService).updateGrade(1L, "B+");
    }

    @Test
    void deleteRegistration_ShouldReturnNoContent() throws Exception {
        doNothing().when(registrationService).deleteById(1L);

        mockMvc.perform(delete("/api/registrations/1"))
                .andExpect(status().isNoContent());

        verify(registrationService).deleteById(1L);
    }

    @Test
    void dropStudentFromCourse_ShouldReturnUpdatedRegistration() throws Exception {
        RegistrationDTO droppedRegistration = new RegistrationDTO();
        droppedRegistration.setId(1L);
        droppedRegistration.setStatus("DROPPED");
        droppedRegistration.setStudentId(1L);
        droppedRegistration.setCourseId(1L);

        when(registrationService.dropStudentFromCourse(1L, 1L)).thenReturn(droppedRegistration);

        mockMvc.perform(patch("/api/registrations/drop/student/1/course/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("DROPPED"))
                .andExpect(jsonPath("$.studentId").value(1L))
                .andExpect(jsonPath("$.courseId").value(1L));

        verify(registrationService).dropStudentFromCourse(1L, 1L);
    }
}