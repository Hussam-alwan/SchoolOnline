package com.bootcamp.onlineschool.dto;

import jakarta.validation.constraints.*;

public class StudentDTO {

    private String id;

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "GPA is required")
    @Min(value = 0, message = "GPA cannot be negative")
    @Max(value = 4, message = "GPA cannot be greater than 4")
    private Double gpa;

    public StudentDTO() {
    }

    public StudentDTO(String id, String name, String email, Double gpa) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gpa = gpa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }
}
