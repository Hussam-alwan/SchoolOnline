package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.dto.UserDTO;
import com.bootcamp.onlineschool.entity.User;
import com.bootcamp.onlineschool.entity.Student;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.exception.ValidationException;
import com.bootcamp.onlineschool.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        testUser = new Student("John Doe", "john.doe@example.com", "STU001", LocalDate.of(2023, 1, 15));
        testUser.setId(1L);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
        
        testUserDTO = new UserDTO();
        testUserDTO.setId(1L);
        testUserDTO.setName("John Doe");
        testUserDTO.setEmail("john.doe@example.com");
        testUserDTO.setCreatedAt(testUser.getCreatedAt());
        testUserDTO.setUpdatedAt(testUser.getUpdatedAt());
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        // Given
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserDTO> result = userService.findAll();

        // Then
        assertEquals(1, result.size());
        assertEquals(testUserDTO.getId(), result.get(0).getId());
        assertEquals(testUserDTO.getName(), result.get(0).getName());
        assertEquals(testUserDTO.getEmail(), result.get(0).getEmail());
        verify(userRepository).findAll();
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        UserDTO result = userService.findById(1L);

        // Then
        assertEquals(testUserDTO.getId(), result.getId());
        assertEquals(testUserDTO.getName(), result.getName());
        assertEquals(testUserDTO.getEmail(), result.getEmail());
        verify(userRepository).findById(1L);
    }

    @Test
    void findById_WhenUserNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> userService.findById(1L)
        );
        assertEquals("User not found with id: '1'", exception.getMessage());
        verify(userRepository).findById(1L);
    }

    @Test
    void findByEmail_WhenValidEmail_ShouldReturnUser() {
        // Given
        String email = "john.doe@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        // When
        Optional<UserDTO> result = userService.findByEmail(email);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testUserDTO.getId(), result.get().getId());
        assertEquals(testUserDTO.getName(), result.get().getName());
        assertEquals(testUserDTO.getEmail(), result.get().getEmail());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void findByEmail_WhenNullEmail_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> userService.findByEmail(null)
        );
        assertEquals("Email cannot be null or empty", exception.getMessage());
        verify(userRepository, never()).findByEmail(any());
    }

    @Test
    void findByEmail_WhenEmptyEmail_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> userService.findByEmail("")
        );
        assertEquals("Email cannot be null or empty", exception.getMessage());
        verify(userRepository, never()).findByEmail(any());
    }

    @Test
    void existsByEmail_WhenValidEmail_ShouldReturnTrue() {
        // Given
        String email = "john.doe@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // When
        boolean result = userService.existsByEmail(email);

        // Then
        assertTrue(result);
        verify(userRepository).existsByEmail(email);
    }

    @Test
    void existsByEmail_WhenNullEmail_ShouldThrowValidationException() {
        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> userService.existsByEmail(null)
        );
        assertEquals("Email cannot be null or empty", exception.getMessage());
        verify(userRepository, never()).existsByEmail(any());
    }

    @Test
    void update_WhenValidData_ShouldUpdateUser() {
        // Given
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setName("Jane Doe");
        updatedUserDTO.setEmail("jane.doe@example.com");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail("jane.doe@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        UserDTO result = userService.update(1L, updatedUserDTO);

        // Then
        assertEquals("Jane Doe", testUser.getName());
        assertEquals("jane.doe@example.com", testUser.getEmail());
        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail("jane.doe@example.com");
        verify(userRepository).save(testUser);
    }

    @Test
    void update_WhenUserNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setName("Jane Doe");
        updatedUserDTO.setEmail("jane.doe@example.com");
        
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> userService.update(1L, updatedUserDTO)
        );
        assertEquals("User not found with id: '1'", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void update_WhenEmailAlreadyExists_ShouldThrowValidationException() {
        // Given
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setName("Jane Doe");
        updatedUserDTO.setEmail("existing@example.com");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> userService.update(1L, updatedUserDTO)
        );
        assertEquals("Email already exists: existing@example.com", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail("existing@example.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void update_WhenNullUpdatedUser_ShouldThrowValidationException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> userService.update(1L, null)
        );
        assertEquals("Updated user data cannot be null", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void update_WhenNullName_ShouldThrowValidationException() {
        // Given
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setName(null);
        updatedUserDTO.setEmail("jane.doe@example.com");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> userService.update(1L, updatedUserDTO)
        );
        assertEquals("Name is required", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteById_WhenUserExists_ShouldDeleteUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        userService.deleteById(1L);

        // Then
        verify(userRepository).findById(1L);
        verify(userRepository).delete(testUser);
    }

    @Test
    void deleteById_WhenUserNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> userService.deleteById(1L)
        );
        assertEquals("User not found with id: '1'", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(userRepository, never()).delete(any());
    }
}