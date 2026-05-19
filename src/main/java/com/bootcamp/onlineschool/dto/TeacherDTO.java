package com.bootcamp.onlineschool.dto;

import jakarta.validation.constraints.*;

public class TeacherDTO {

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 1, max = 50)
    private String department;

    @NotNull
    @Min(0)
    private Integer yearsOfExperience;

    @NotNull
    @PositiveOrZero
    private Double salary;

    public TeacherDTO() {
    }

    public TeacherDTO(String name, String email, String department,
                      Integer yearsOfExperience, Double salary) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.yearsOfExperience = yearsOfExperience;
        this.salary = salary;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}
