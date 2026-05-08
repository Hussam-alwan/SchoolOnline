package com.bootcamp.onlineschool.comparator;

import com.bootcamp.onlineschool.model.Student;

import java.util.Comparator;

public class StudentGpaNameComparator implements Comparator<Student> {

    @Override
    public int compare(Student o1, Student o2) {
        int gpaComparison = Double.compare(o2.getGpa(), o1.getGpa());
        if (gpaComparison != 0) {
            return gpaComparison;
        }
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}
