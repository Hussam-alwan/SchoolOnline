package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.dto.TeacherDTO;
import com.bootcamp.onlineschool.entity.Teacher;
import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.repository.CourseRepository;
import com.bootcamp.onlineschool.repository.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;

    public TeacherService(TeacherRepository teacherRepository,
                          CourseRepository courseRepository) {
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
    }

    public Teacher createTeacher(TeacherDTO dto) {
        Teacher teacher = new Teacher(
                dto.getName(),
                dto.getEmail(),
                dto.getDepartmentName(),
                dto.getYearsOfExperience(),
                dto.getSalary()
        );
        return teacherRepository.save(teacher);
    }

    @Transactional(readOnly = true)
    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
    }

    public void assignCourseToTeacher(Long teacherId, Long courseId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        teacher.addCourse(course);
    }

    public void removeCourseFromTeacher(Long teacherId, Long courseId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        teacher.removeCourse(course);
    }

    @Transactional(readOnly = true)
    public Optional<Teacher> getTeacherWithCourses(Long id) {
        return teacherRepository.findByIdWithCourses(id);
    }

    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
        teacherRepository.delete(teacher);
    }
}
