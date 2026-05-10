package com.bootcamp.onlineschool;

import com.bootcamp.onlineschool.model.Teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherRegistry {

    private final Map<String, Teacher> teachers= new HashMap<>();

    public void AddTeacher(Teacher teacher) {
        if (teacher == null) throw new IllegalArgumentException("Teacher cannot be null");
        teachers.put(teacher.getTeacherId(), teacher);
    }

    public Teacher findTeacherById(String teacherId) {
        return teachers.get(teacherId);
    }

    public List<Teacher> getAllTeachers() {
        return new ArrayList<>(teachers.values());
    }

    public void removeTeacherById(String teacherId) {

        teachers.remove(teacherId);
    }

    public List<Teacher> findTeacherByDepartmentId(String departmentId) {
        if (departmentId ==null) return new  ArrayList<>();
        return teachers.values().stream()
                .filter(teacher -> departmentId.equalsIgnoreCase(teacher.getDepartment()))
                .toList();
    }

    public int getTeacherCount() {
        return teachers.size();
    }

    public void clear() {
        teachers.clear();
    }

}
