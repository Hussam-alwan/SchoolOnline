package com.bootcamp.onlineschool.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for OpenAPI configuration
 */
@SpringBootTest
@AutoConfigureWebMvc
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class OpenApiConfigTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    void testSwaggerUiIsAccessible() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    @Test
    void testOpenApiDocsIsAccessible() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.openapi").value("3.0.1"))
                .andExpect(jsonPath("$.info.title").value("Online School Backend API"))
                .andExpect(jsonPath("$.info.version").value("1.0.0"))
                .andExpect(jsonPath("$.info.description").exists())
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.paths").exists())
                .andExpect(jsonPath("$.components.schemas").exists());
    }

    @Test
    void testApiDocumentationContainsExpectedTags() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags[?(@.name == 'User Management')]").exists())
                .andExpect(jsonPath("$.tags[?(@.name == 'Student Management')]").exists())
                .andExpect(jsonPath("$.tags[?(@.name == 'Teacher Management')]").exists())
                .andExpect(jsonPath("$.tags[?(@.name == 'Course Management')]").exists())
                .andExpect(jsonPath("$.tags[?(@.name == 'Class Management')]").exists())
                .andExpect(jsonPath("$.tags[?(@.name == 'Registration Management')]").exists());
    }

    @Test
    void testApiDocumentationContainsSchemas() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components.schemas.UserDTO").exists())
                .andExpect(jsonPath("$.components.schemas.StudentDTO").exists())
                .andExpect(jsonPath("$.components.schemas.TeacherDTO").exists())
                .andExpect(jsonPath("$.components.schemas.CourseDTO").exists())
                .andExpect(jsonPath("$.components.schemas.ClazzDTO").exists())
                .andExpect(jsonPath("$.components.schemas.RegistrationDTO").exists());
    }
}