package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.dto.DepartmentDTO;
import com.bootcamp.onlineschool.entity.Department;
import com.bootcamp.onlineschool.entity.Teacher;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.repository.CourseRepository;
import com.bootcamp.onlineschool.repository.DepartmentRepository;
import com.bootcamp.onlineschool.repository.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;

    public DepartmentService(DepartmentRepository departmentRepository,
                             TeacherRepository teacherRepository,
                             CourseRepository courseRepository) {
        this.departmentRepository = departmentRepository;
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
    }

    public Department createDepartment(DepartmentDTO dto) {
        Department department = new Department(
                dto.getName(),
                dto.getCode(),
                dto.getBudget(),
                dto.getLocation()
        );
        return departmentRepository.save(department);
    }

    @Transactional(readOnly = true)
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department updateDepartment(Long id, DepartmentDTO dto) {
        Department department = getDepartmentById(id);
        department.setName(dto.getName());
        department.setCode(dto.getCode());
        department.setBudget(dto.getBudget());
        department.setLocation(dto.getLocation());
        return departmentRepository.save(department);
    }

    public void deleteDepartment(Long id) {
        Department department = getDepartmentById(id);
        department.setDeleted(true);
        departmentRepository.save(department);
        //departmentRepository.delete(department);
    }

    public void assignTeacherToDepartment(Long departmentId, Long teacherId) {
        Department department = getDepartmentById(departmentId);
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));
        department.addTeacher(teacher);
    }

    public void removeTeacherFromDepartment(Long departmentId, Long teacherId) {
        Department department = getDepartmentById(departmentId);
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));
        department.removeTeacher(teacher);
    }

    public void assignCourseToDepartment(Long departmentId, Long courseId) {
        Department department = getDepartmentById(departmentId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        department.addCourse(course);
    }

    public void removeCourseFromDepartment(Long departmentId, Long courseId) {
        Department department = getDepartmentById(departmentId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        department.removeCourse(course);
    }

    @Transactional(readOnly = true)
    public Optional<Department> getDepartmentWithTeachers(Long id) {
        return departmentRepository.findByIdWithTeachers(id);
    }

    @Transactional(readOnly = true)
    public Optional<Department> getDepartmentWithCourses(Long id) {
        return departmentRepository.findByIdWithCourses(id);
    }

    @Transactional(readOnly = true)
    public Double getTotalBudget() {
        return departmentRepository.getTotalBudget();
    }
}
