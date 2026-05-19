package com.bootcamp.onlineschool.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DepartmentDTO {

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @NotNull
    @Size(min = 1, max = 10)
    private String code;

    @NotNull
    @Min(0)
    private Double budget;

    @Size(max = 100)
    private String location;

    public DepartmentDTO() {
    }

    public DepartmentDTO(String name, String code, Double budget, String location) {
        this.name = name;
        this.code = code;
        this.budget = budget;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
