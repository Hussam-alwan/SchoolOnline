package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.config.SchoolProperties;
import com.bootcamp.onlineschool.model.Department;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DepartmentService {

    private final Map<String, Department> departmentMap = new HashMap<>();
    private final SchoolProperties schoolProperties;

    public DepartmentService(SchoolProperties schoolProperties) {
        this.schoolProperties = schoolProperties;
    }

    @CacheEvict(value = "departments", allEntries = true)
    public Department createDepartment(String id, String name, String head, double budget) {
        if (departmentMap.containsKey(id)) {
            throw new DepartmentAlreadyExistsException("Department already exists with ID: " + id);
        }

        Department department = new Department(id, name, head, budget);
        departmentMap.put(id, department);
        return department;
    }

    @Cacheable(value = "departments", key = "#id")
    public Department getDepartmentById(String id) {
        Department department = departmentMap.get(id);
        if (department == null) {
            throw new DepartmentNotFoundException("Department not found with ID: " + id);
        }

        return department;
    }

@Cacheable(value = "departments", key = "'allDepartments'")
    public List<Department> getAllDepartments() {
        return new ArrayList<>(departmentMap.values());
    }

@CacheEvict(value = "departments", allEntries = true)
    public void assignTeacherToDepartment(String deptId, String teacherId) {
        Department department = getDepartmentById(deptId);

        // Enforce the configured maximum
        if (department.getTeacherCount() >= schoolProperties.getMaxTeachersPerDepartment()) {
            throw new IllegalStateException(
                    "Department is full. Maximum teachers per department: "
                            + schoolProperties.getMaxTeachersPerDepartment()
            );
        }

        department.addTeacherId(teacherId);
    }

    @CacheEvict(value = "departments", allEntries = true)
    public void removeTeacherFromDepartment(String deptId, String teacherId) {
        Department department = getDepartmentById(deptId);
        if (department==null){
            throw new DepartmentNotFoundException("Department not found with ID: " + deptId);
        }
        department.removeTeacherId(teacherId);
    }
    @Cacheable(value = "departments", key = "'budget_' + #min + '_' + #max")
    public List<Department> getDepartmentsByBudgetRange(double min, double max) {
        return departmentMap.values().stream()
                .filter(d -> d.getBudget() >= min && d.getBudget() <= max)
                .toList();
    }

    @Cacheable(value = "departments", key = "'totalBudget'")
    public double getTotalBudget() {
        return departmentMap.values().stream()
                .mapToDouble(Department::getBudget)
                .sum();
    }

   @CacheEvict(value = "departments", allEntries = true)
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