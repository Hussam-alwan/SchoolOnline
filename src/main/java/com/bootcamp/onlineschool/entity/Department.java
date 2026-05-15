package com.bootcamp.onlineschool.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table
public class Department {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Department ID is required")
    @Size(max = 20, message = "Department ID must be at most 20 characters")
    @Column(nullable = false, unique = true)
    private String departmentId;

    @NotBlank(message = "Department name is required")
    @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Department head is required")
    @Size(min = 2, max = 100, message = "Department head must be between 2 and 100 characters")
    @Column(nullable = false)
    private String head;

    @NotNull(message = "Department budget is required")
    @PositiveOrZero(message = "Department budget must be zero or positive")
    @Column(nullable = false)
    private Double budget;

    @NotBlank(message = "Department location is required")
    @Size(min = 2, max = 200, message = "Department location must be between 2 and 200 characters")
    @Column(nullable = false)
    private String location;

    @NotNull(message = "Established date is required")
    @Past(message = "Established date must be in the past")
    @Column(nullable = false)
    private LocalDate establishedDate;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    public Department(String departmentId, String name, String head, Double budget, String location, LocalDate establishedDate) {
        this.departmentId = departmentId;
        this.name = name;
        this.head = head;
        this.budget = budget;
        this.location = location;
        this.establishedDate = establishedDate;
    }

    public Department() {}

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
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

    public LocalDate getEstablishedDate() {
        return establishedDate;
    }

    public void setEstablishedDate(LocalDate establishedDate) {
        this.establishedDate = establishedDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(id, that.id) && Objects.equals(departmentId, that.departmentId) && Objects.equals(name, that.name) && Objects.equals(head, that.head) && Objects.equals(budget, that.budget) && Objects.equals(location, that.location) && Objects.equals(establishedDate, that.establishedDate) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, departmentId, name, head, budget, location, establishedDate, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", departmentId='" + departmentId + '\'' +
                ", name='" + name + '\'' +
                ", head='" + head + '\'' +
                ", budget=" + budget +
                ", location='" + location + '\'' +
                ", establishedDate=" + establishedDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public Boolean isWithinBudget(Double amount) {
        return amount != null && amount <= budget;
    }

    public void increaseBudget(Double amount) {
        if (amount != null && amount > 0) {
            budget += amount;
        }
    }

    public void decreaseBudget(Double amount) {
        if (amount != null && amount > 0) {
            if (budget - amount >= 0) {
                budget -= amount;
            } else {
                throw new IllegalArgumentException("Cannot decrease budget below zero");
            }
        }
    }
}
