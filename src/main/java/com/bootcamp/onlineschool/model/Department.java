package com.bootcamp.onlineschool.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Department {
    private String id;
    private String name;
    private String head;
    private Double budget;
    private List<String> teachersId;

    public Department(String id, String name, String head, double budget) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Department ID cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Department name cannot be null or blank");
        }
        if (head == null || head.isBlank()) {
            throw new IllegalArgumentException("Department head cannot be null or blank");
        }
        if (budget < 0) {
            throw new IllegalArgumentException("Budget cannot be negative");
        }

        this.id = id;
        this.name = name;
        this.head = head;
        this.budget = budget;
        this.teachersId = new ArrayList<>();
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

    public List<String> getTeachersId() {
        return teachersId;
    }

    public void setTeachersId(List<String> teachersId) {
        this.teachersId = teachersId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(head, that.head) && Objects.equals(budget, that.budget) && Objects.equals(teachersId, that.teachersId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, head, budget, teachersId);
    }

    @Override
    public String toString() {
        return "Department{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", head='" + head + '\'' +
                ", budget=" + budget +
                ", teachersId=" + teachersId +
                '}';
    }

    public void addTeacherId(String teacherId) {
        if (teacherId == null || teacherId.isBlank()) {
            throw new IllegalArgumentException("Teacher ID cannot be null or blank");
        }
        // Only add if not already present — no exception, just skip it
        if (!teachersId.contains(teacherId)) {
            teachersId.add(teacherId);
        }
    }

    public void removeTeacherId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Teacher ID cannot be null or blank");
        }
        teachersId.remove(id);
    }

    public int getTeacherCount() {
        return teachersId.size();
    }

    public boolean isWithinBudget(Double budget) {
        return budget >= this.budget;
    }


}
