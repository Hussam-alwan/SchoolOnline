package com.bootcamp.onlineschool.controller;

import com.bootcamp.onlineschool.dto.UserDTO;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.exception.ValidationException;
import com.bootcamp.onlineschool.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("John Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setCreatedAt(LocalDateTime.now());
        userDTO.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createUser_WithValidData_ShouldReturnCreatedUser() throws Exception {
        UserDTO createRequest = new UserDTO();
        createRequest.setName("John Doe");
        createRequest.setEmail("john.doe@example.com");

        when(userService.create(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService).create(any(UserDTO.class));
    }

    @Test
    void createUser_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        UserDTO invalidRequest = new UserDTO();
        invalidRequest.setName(""); // Invalid name
        invalidRequest.setEmail("invalid-email"); // Invalid email

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).create(any(UserDTO.class));
    }

    @Test
    void createUser_WithDuplicateEmail_ShouldReturnBadRequest() throws Exception {
        UserDTO createRequest = new UserDTO();
        createRequest.setName("John Doe");
        createRequest.setEmail("existing@example.com");

        when(userService.create(any(UserDTO.class)))
                .thenThrow(new ValidationException("Email already exists: existing@example.com"));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());

        verify(userService).create(any(UserDTO.class));
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        List<UserDTO> users = Arrays.asList(userDTO);
        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));

        verify(userService).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() throws Exception {
        when(userService.findById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService).findById(1L);
    }

    @Test
    void getUserById_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        when(userService.findById(1L)).thenThrow(new ResourceNotFoundException("User", "id", 1L));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());

        verify(userService).findById(1L);
    }

    @Test
    void getUserByEmail_WhenUserExists_ShouldReturnUser() throws Exception {
        when(userService.findByEmail("john.doe@example.com")).thenReturn(Optional.of(userDTO));

        mockMvc.perform(get("/api/users/email/john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService).findByEmail("john.doe@example.com");
    }

    @Test
    void getUserByEmail_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        when(userService.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/email/nonexistent@example.com"))
                .andExpect(status().isNotFound());

        verify(userService).findByEmail("nonexistent@example.com");
    }

    @Test
    void updateUser_WithValidData_ShouldReturnUpdatedUser() throws Exception {
        UserDTO updateRequest = new UserDTO();
        updateRequest.setName("Jane Doe");
        updateRequest.setEmail("jane.doe@example.com");

        UserDTO updatedUser = new UserDTO();
        updatedUser.setId(1L);
        updatedUser.setName("Jane Doe");
        updatedUser.setEmail("jane.doe@example.com");

        when(userService.update(eq(1L), any(UserDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane.doe@example.com"));

        verify(userService).update(eq(1L), any(UserDTO.class));
    }

    @Test
    void updateUser_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        UserDTO invalidRequest = new UserDTO();
        invalidRequest.setName(""); // Invalid name
        invalidRequest.setEmail("invalid-email"); // Invalid email

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).update(anyLong(), any(UserDTO.class));
    }

    @Test
    void updateUser_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        UserDTO updateRequest = new UserDTO();
        updateRequest.setName("Jane Doe");
        updateRequest.setEmail("jane.doe@example.com");

        when(userService.update(eq(1L), any(UserDTO.class)))
                .thenThrow(new ResourceNotFoundException("User", "id", 1L));

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());

        verify(userService).update(eq(1L), any(UserDTO.class));
    }

    @Test
    void updateUser_WithDuplicateEmail_ShouldReturnBadRequest() throws Exception {
        UserDTO updateRequest = new UserDTO();
        updateRequest.setName("Jane Doe");
        updateRequest.setEmail("existing@example.com");

        when(userService.update(eq(1L), any(UserDTO.class)))
                .thenThrow(new ValidationException("Email already exists: existing@example.com"));

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());

        verify(userService).update(eq(1L), any(UserDTO.class));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldReturnNoContent() throws Exception {
        doNothing().when(userService).deleteById(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteById(1L);
    }

    @Test
    void deleteUser_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("User", "id", 1L)).when(userService).deleteById(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound());

        verify(userService).deleteById(1L);
    }

    @Test
    void existsByEmail_WhenUserExists_ShouldReturnTrue() throws Exception {
        when(userService.existsByEmail("john.doe@example.com")).thenReturn(true);

        mockMvc.perform(get("/api/users/exists/email/john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(true));

        verify(userService).existsByEmail("john.doe@example.com");
    }

    @Test
    void existsByEmail_WhenUserNotExists_ShouldReturnFalse() throws Exception {
        when(userService.existsByEmail("nonexistent@example.com")).thenReturn(false);

        mockMvc.perform(get("/api/users/exists/email/nonexistent@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(false));

        verify(userService).existsByEmail("nonexistent@example.com");
    }
}