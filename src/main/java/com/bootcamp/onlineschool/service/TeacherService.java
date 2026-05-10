package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.TeacherRegistry;
import com.bootcamp.onlineschool.model.Teacher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {
    private final TeacherRegistry teacherRegistry;

    public TeacherService(TeacherRegistry teacherRegistry) {
        this.teacherRegistry = teacherRegistry;
    }

    public void addTeacher(Teacher teacher) {
        if (teacher == null) throw new TeacherNotFoundException("Teacher cannot be null");
        teacherRegistry.AddTeacher(teacher);
    }

    public Teacher findTeacherById(String id) {
        Teacher teacher = teacherRegistry.findTeacherById(id);
        if (teacher == null) {
            throw new TeacherNotFoundException("Teacher with ID " + id + " not found");
        }
        return teacher;
    }

    public List<Teacher> getAllTeachers() {
        return teacherRegistry.getAllTeachers();
    }

    public List<Teacher> findTeachersByDepartment(String departmentId) {
        return teacherRegistry.findTeacherByDepartmentId(departmentId);
    }

    public void removeTeacher(String id) {
        teacherRegistry.removeTeacherById(id);
    }

    public int getTotalTeachers() {
        return teacherRegistry.getTeacherCount();
    }


    public static class TeacherNotFoundException extends RuntimeException {
        public TeacherNotFoundException(String message) {
            super(message);
        }
    }
}

