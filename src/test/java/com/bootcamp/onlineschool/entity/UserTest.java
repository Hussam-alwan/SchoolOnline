package com.bootcamp.onlineschool.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Create a concrete implementation for testing
    private static class TestUser extends User {
        public TestUser() {
            super();
        }

        public TestUser(String name, String email) {
            super(name, email);
        }
    }

    @Test
    void testValidUser() {
        TestUser user = new TestUser("John Doe", "john.doe@example.com");
        
        Set<ConstraintViolation<TestUser>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Valid user should have no validation errors");
    }

    @Test
    void testUserWithBlankName() {
        TestUser user = new TestUser("", "john.doe@example.com");
        
        Set<ConstraintViolation<TestUser>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "User with blank name should have validation errors");
        
        boolean hasNameError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name"));
        assertTrue(hasNameError, "Should have validation error for name field");
    }

    @Test
    void testUserWithInvalidEmail() {
        TestUser user = new TestUser("John Doe", "invalid-email");
        
        Set<ConstraintViolation<TestUser>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "User with invalid email should have validation errors");
        
        boolean hasEmailError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));
        assertTrue(hasEmailError, "Should have validation error for email field");
    }

    @Test
    void testUserWithNullName() {
        TestUser user = new TestUser(null, "john.doe@example.com");
        
        Set<ConstraintViolation<TestUser>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "User with null name should have validation errors");
    }

    @Test
    void testUserWithNullEmail() {
        TestUser user = new TestUser("John Doe", null);
        
        Set<ConstraintViolation<TestUser>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "User with null email should have validation errors");
    }

    @Test
    void testUserEqualsAndHashCode() {
        TestUser user1 = new TestUser("John Doe", "john.doe@example.com");
        TestUser user2 = new TestUser("Jane Doe", "jane.doe@example.com");
        
        // Test equals with same instance
        assertEquals(user1, user1);
        
        // Test equals with different instances but no ID set
        assertNotEquals(user1, user2);
        
        // Test with same ID
        user1.setId(1L);
        user2.setId(1L);
        assertEquals(user1, user2);
        
        // Test hashCode consistency
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testUserToString() {
        TestUser user = new TestUser("John Doe", "john.doe@example.com");
        user.setId(1L);
        
        String toString = user.toString();
        assertTrue(toString.contains("John Doe"));
        assertTrue(toString.contains("john.doe@example.com"));
        assertTrue(toString.contains("id=1"));
    }

    @Test
    void testUserConstructors() {
        // Test default constructor
        TestUser user1 = new TestUser();
        assertNull(user1.getName());
        assertNull(user1.getEmail());
        
        // Test parameterized constructor
        TestUser user2 = new TestUser("John Doe", "john.doe@example.com");
        assertEquals("John Doe", user2.getName());
        assertEquals("john.doe@example.com", user2.getEmail());
    }
}