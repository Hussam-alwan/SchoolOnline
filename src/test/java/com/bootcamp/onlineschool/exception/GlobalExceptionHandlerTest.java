package com.bootcamp.onlineschool.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for GlobalExceptionHandler
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/test/endpoint");
        webRequest = new ServletWebRequest(request);
    }

    @Test
    void handleResourceNotFoundException_ShouldReturn404() {
        // Given
        ResourceNotFoundException exception = new ResourceNotFoundException("User", "id", "1");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleResourceNotFoundException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(404, errorResponse.getStatus());
        assertEquals("Resource Not Found", errorResponse.getError());
        assertEquals("User not found with id: '1'", errorResponse.getMessage());
        assertEquals("/test/endpoint", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void handleValidationException_ShouldReturn400() {
        // Given
        ValidationException exception = new ValidationException("Invalid business rule");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Validation Error", errorResponse.getError());
        assertEquals("Invalid business rule", errorResponse.getMessage());
        assertEquals("/test/endpoint", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void handleMethodArgumentNotValidException_ShouldReturn400WithValidationErrors() {
        // Given
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("testObject", "name", "Name cannot be blank");
        FieldError fieldError2 = new FieldError("testObject", "email", "Email cannot be blank");
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));
        
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleMethodArgumentNotValidException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Validation Failed", errorResponse.getError());
        assertEquals("Input validation failed", errorResponse.getMessage());
        assertEquals("/test/endpoint", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
        assertNotNull(errorResponse.getValidationErrors());
        assertEquals("Name cannot be blank", errorResponse.getValidationErrors().get("name"));
        assertEquals("Email cannot be blank", errorResponse.getValidationErrors().get("email"));
    }

    @Test
    void handleIllegalArgumentException_ShouldReturn400() {
        // Given
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument provided");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgumentException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Invalid Argument", errorResponse.getError());
        assertEquals("Invalid argument provided", errorResponse.getMessage());
        assertEquals("/test/endpoint", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void handleGlobalException_ShouldReturn500() {
        // Given
        RuntimeException exception = new RuntimeException("Unexpected runtime error");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGlobalException(exception, webRequest);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(500, errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());
        assertEquals("An unexpected error occurred", errorResponse.getMessage());
        assertEquals("/test/endpoint", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void errorResponse_BuilderPattern_ShouldWork() {
        // Given & When
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(400)
                .error("Test Error")
                .message("Test message")
                .path("/test")
                .build();

        // Then
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Test Error", errorResponse.getError());
        assertEquals("Test message", errorResponse.getMessage());
        assertEquals("/test", errorResponse.getPath());
    }
}