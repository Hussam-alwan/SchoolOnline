package com.bootcamp.onlineschool.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple test for exception handling infrastructure
 */
class ExceptionHandlerTest {

    @Test
    void resourceNotFoundException_ShouldHaveCorrectMessage() {
        ResourceNotFoundException exception = new ResourceNotFoundException("User", "id", "123");
        assertEquals("User not found with id: '123'", exception.getMessage());
    }

    @Test
    void validationException_ShouldHaveCorrectMessage() {
        ValidationException exception = new ValidationException("Validation failed");
        assertEquals("Validation failed", exception.getMessage());
    }

    @Test
    void globalExceptionHandler_ShouldHandleResourceNotFoundException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResourceNotFoundException exception = new ResourceNotFoundException("User not found");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/test");
        ServletWebRequest webRequest = new ServletWebRequest(request);

        ResponseEntity<ErrorResponse> response = handler.handleResourceNotFoundException(exception, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Resource Not Found", response.getBody().getError());
    }

    @Test
    void globalExceptionHandler_ShouldHandleValidationException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ValidationException exception = new ValidationException("Invalid data");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/test");
        ServletWebRequest webRequest = new ServletWebRequest(request);

        ResponseEntity<ErrorResponse> response = handler.handleValidationException(exception, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Validation Error", response.getBody().getError());
    }

    @Test
    void errorResponse_ShouldBuildCorrectly() {
        ErrorResponse response = ErrorResponse.builder()
                .status(400)
                .error("Test Error")
                .message("Test message")
                .path("/test")
                .build();

        assertEquals(400, response.getStatus());
        assertEquals("Test Error", response.getError());
        assertEquals("Test message", response.getMessage());
        assertEquals("/test", response.getPath());
    }
}