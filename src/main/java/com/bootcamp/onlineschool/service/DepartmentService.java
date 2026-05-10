package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.model.Department;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DepartmentService {

    private final Map<String, Department> departmentMap = new HashMap<>();

    public void createDepartment(String id, String name, String head, double budget) {
        if (departmentMap.containsKey(id)) {
            throw new DepartmentAlreadyExistsException("Department already exists with ID: " + id);
        }

        Department department = new Department(id, name, head, budget);
        departmentMap.put(id, department);
    }

    public Department getDepartmentById(String id) {
        Department department = departmentMap.get(id);
        if (department == null) {
            throw new DepartmentNotFoundException("Department not found with ID: " + id);
        }

        return department;
    }

    public List<Department> getAllDepartments() {
        return new ArrayList<>(departmentMap.values());
    }

    public void assignTeacherToDepartment(String deptId, String teacherId) {
        Department department = getDepartmentById(deptId);
        department.addTeacherId(teacherId);
    }

    public void removeTeacherFromDepartment(String deptId, String teacherId) {
        Department department = getDepartmentById(deptId);
        if (department==null){
            throw new DepartmentNotFoundException("Department not found with ID: " + deptId);
        }
        department.removeTeacherId(teacherId);
    }

    public List<Department> getDepartmentsByBudgetRange(double min, double max) {
        return departmentMap.values().stream()
                .filter(d -> d.getBudget() >= min && d.getBudget() <= max)
                .toList();
    }

    public double getTotalBudget() {
        return departmentMap.values().stream()
                .mapToDouble(Department::getBudget)
                .sum();
    }

    public void deleteDepartment(String id) {
        if (!departmentMap.containsKey(id)) {
            throw new DepartmentNotFoundException("Department not found with ID: " + id);
        }
        departmentMap.remove(id);
    }

    public static class DepartmentNotFoundException extends RuntimeException {
        public DepartmentNotFoundException(String message) {
            super(message);
        }
    }

    public static class DepartmentAlreadyExistsException extends RuntimeException {
        public DepartmentAlreadyExistsException(String message) {
            super(message);
        }
    }
}