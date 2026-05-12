package com.bootcamp.onlineschool.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "school")
public class SchoolProperties {

    private int  maxStudentsPerCourse;
    private int maxTeachersPerDepartment;
    private double defaultGpaThreshold;

    public int getMaxStudentsPerCourse() {
        return maxStudentsPerCourse;
    }

    public void setMaxStudentsPerCourse(int maxStudentsPerCourse) {
        this.maxStudentsPerCourse = maxStudentsPerCourse;
    }

    public int getMaxTeachersPerDepartment() {
        return maxTeachersPerDepartment;
    }

    public void setMaxTeachersPerDepartment(int maxTeachersPerDepartment) {
        this.maxTeachersPerDepartment = maxTeachersPerDepartment;
    }

    public double getDefaultGpaThreshold() {
        return defaultGpaThreshold;
    }

    public void setDefaultGpaThreshold(double defaultGpaThreshold) {
        this.defaultGpaThreshold = defaultGpaThreshold;
    }
}
