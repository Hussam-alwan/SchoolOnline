package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.TeacherRegistry;
import com.bootcamp.onlineschool.model.Teacher;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {
    private final TeacherRegistry teacherRegistry;

    public TeacherService(TeacherRegistry teacherRegistry) {
        this.teacherRegistry = teacherRegistry;
    }

 @CacheEvict(value = "teachers", allEntries = true)
    public void addTeacher(Teacher teacher) {
        if (teacher == null) throw new TeacherNotFoundException("Teacher cannot be null");
        teacherRegistry.AddTeacher(teacher);
    }

@Cacheable(value = "teachers", key = "#id")
    public Teacher findTeacherById(String id) {
        Teacher teacher = teacherRegistry.findTeacherById(id);
        if (teacher == null) {
            throw new TeacherNotFoundException("Teacher with ID " + id + " not found");
        }
        return teacher;
    }

  @Cacheable(value = "teachers", key = "'allTeachers'")
    public List<Teacher> getAllTeachers() {
        return teacherRegistry.getAllTeachers();
    }

 @Cacheable(value = "teachers", key = "'dept_' + #departmentId")
    public List<Teacher> findTeachersByDepartment(String departmentId) {
        return teacherRegistry.findTeacherByDepartmentId(departmentId);
    }
    @CacheEvict(value = "teachers", allEntries = true)
    public void removeTeacher(String id) {
        teacherRegistry.removeTeacherById(id);
    }

   @Cacheable(value = "teachers", key = "'totalCount'")
    public int getTotalTeachers() {
        return teacherRegistry.getTeacherCount();
    }


    public static class TeacherNotFoundException extends RuntimeException {
        public TeacherNotFoundException(String message) {
            super(message);
        }
    }
}

