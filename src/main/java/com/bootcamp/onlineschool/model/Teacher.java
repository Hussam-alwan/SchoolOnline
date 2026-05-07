package com.bootcamp.onlineschool.model;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends User {

    private String department;
    private final List<Course> coursesTaught=new ArrayList<>();

    public Teacher(String teacherId, String name, String email, String department) {
        super(teacherId, name, email);
        this.department = department;
    }

    @Override
    public String getRole() {
        return "Teacher";
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Course> getCoursesTaught() {
        return coursesTaught;
    }

    public void assignCourse(Course course){
        if (course!=null && !coursesTaught.contains(course)){
            this.coursesTaught.add(course);
        }
    }

    public void removeCourse(String course){
        this.coursesTaught.removeIf(c -> c.getCourseId().equals(course));
    }

    public List<Course> getCourses(){
        return this.coursesTaught;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "department='" + department + '\'' +
                ", coursesTaught=" + coursesTaught +
                '}';
    }
}
