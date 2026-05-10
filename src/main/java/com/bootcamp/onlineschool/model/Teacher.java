package com.bootcamp.onlineschool.model;


import java.util.Objects;

public class Teacher {
    private String teacherId;
    private String name;
    private String email;
    private String department;
    private Integer yearsOfExperience;

    public Teacher(String teacherId, String name, String email, String department, Integer yearsOfExperience) {
        this.teacherId = teacherId;
        this.name = name;
        this.email = email;
        this.department = department;
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(teacherId, teacher.teacherId) && Objects.equals(name, teacher.name) && Objects.equals(email, teacher.email) && Objects.equals(department, teacher.department) && Objects.equals(yearsOfExperience, teacher.yearsOfExperience);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherId, name, email, department, yearsOfExperience);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teacherId='" + teacherId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", yearsOfExperience=" + yearsOfExperience +
                '}';
    }
}
