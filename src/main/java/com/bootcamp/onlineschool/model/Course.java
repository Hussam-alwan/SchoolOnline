package com.bootcamp.onlineschool.model;

import java.util.Objects;

public class Course {
    private String courseId;
    private String name;
    private int credits;

    public Course(String courseId, String name, int credits) {
        this.courseId = courseId;
        this.name = name;
        this.credits = credits;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return credits == course.credits && Objects.equals(courseId, course.courseId) && Objects.equals(name, course.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, name, credits);
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", name='" + name + '\'' +
                ", credits=" + credits +
                '}';
    }


}
