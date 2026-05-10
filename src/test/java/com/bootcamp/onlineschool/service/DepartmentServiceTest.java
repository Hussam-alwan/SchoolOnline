package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.model.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("DepartmentService Tests")
public class DepartmentServiceTest {

    @Autowired
    private DepartmentService departmentService;

    @BeforeEach
    public void setUp() {
        departmentService.getAllDepartments()
                .forEach(d -> departmentService.deleteDepartment(d.getId()));
    }

    @Test
    @DisplayName("Should create a department and retrieve it by ID")
    public void testCreateDepartment() {
        departmentService.createDepartment("D001", "Mathematics", "Alice Smith", 5000.0);

        Department found = departmentService.getDepartmentById("D001");

        assertEquals("D001", found.getId());
        assertEquals("Mathematics", found.getName());
        assertEquals("Alice Smith", found.getHead());
        assertEquals(5000.0, found.getBudget());
    }

    @Test
    @DisplayName("Should throw DepartmentAlreadyExistsException for duplicate ID")
    public void testCreateDuplicateDepartmentThrowsException() {
        departmentService.createDepartment("D001", "Mathematics", "Alice Smith", 5000.0);

        assertThrows(DepartmentService.DepartmentAlreadyExistsException.class, () -> departmentService.createDepartment("D001", "Science", "Bob Jones", 3000.0));
    }

    @Test
    @DisplayName("Should throw DepartmentNotFoundException for unknown ID")
    public void testGetDepartmentByIdNotFound() {
        assertThrows(DepartmentService.DepartmentNotFoundException.class, () -> departmentService.getDepartmentById("UNKNOWN"));
    }

    @Test
    @DisplayName("Should increase teacher count after assigning a teacher")
    public void testAssignTeacherToDepartment() {
        departmentService.createDepartment("D001", "Mathematics", "Alice Smith", 5000.0);

        departmentService.assignTeacherToDepartment("D001", "T001");

        Department dept = departmentService.getDepartmentById("D001");
        assertEquals(1, dept.getTeacherCount());
    }

    @Test
    @DisplayName("Should not increase count when assigning the same teacher twice")
    public void testAssignSameTeacherTwiceDoesNotDuplicate() {
        departmentService.createDepartment("D001", "Mathematics", "Alice Smith", 5000.0);

        departmentService.assignTeacherToDepartment("D001", "T001");
        departmentService.assignTeacherToDepartment("D001", "T001");

        Department dept = departmentService.getDepartmentById("D001");
        assertEquals(1, dept.getTeacherCount());
    }

    @Test
    @DisplayName("Should throw DepartmentNotFoundException when assigning to unknown department")
    public void testAssignTeacherToUnknownDepartmentThrowsException() {
        assertThrows(DepartmentService.DepartmentNotFoundException.class, () -> departmentService.assignTeacherToDepartment("UNKNOWN", "T001"));
    }

    @Test
    @DisplayName("Should decrease teacher count after removing a teacher")
    public void testRemoveTeacherFromDepartment() {
        departmentService.createDepartment("D001", "Mathematics", "Alice Smith", 5000.0);
        departmentService.assignTeacherToDepartment("D001", "T001");
        departmentService.assignTeacherToDepartment("D001", "T002");

        departmentService.removeTeacherFromDepartment("D001", "T001");

        Department dept = departmentService.getDepartmentById("D001");
        assertEquals(1, dept.getTeacherCount());
    }

    @Test
    @DisplayName("Should return correct total budget across all departments")
    public void testGetTotalBudget() {
        departmentService.createDepartment("D001", "Mathematics", "Alice Smith", 5000.0);
        departmentService.createDepartment("D002", "Science", "Bob Jones", 3000.0);
        departmentService.createDepartment("D003", "History", "Carol White", 2000.0);

        double total = departmentService.getTotalBudget();

        assertEquals(10000.0, total);
    }

    @Test
    @DisplayName("Should return 0 when no departments exist")
    public void testGetTotalBudgetWhenEmpty() {
        assertEquals(0.0, departmentService.getTotalBudget());
    }

    @Test
    @DisplayName("Should return only departments within the budget range")
    public void testGetDepartmentsByBudgetRange() {
        departmentService.createDepartment("D001", "Mathematics", "Alice Smith", 5000.0);
        departmentService.createDepartment("D002", "Science", "Bob Jones", 3000.0);
        departmentService.createDepartment("D003", "History", "Carol White", 1000.0);

        List<Department> result = departmentService.getDepartmentsByBudgetRange(2000.0, 5000.0);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(d -> d.getBudget() >= 2000.0 && d.getBudget() <= 5000.0));
    }

    @Test
    @DisplayName("Should return empty list when no departments match the budget range")
    public void testGetDepartmentsByBudgetRangeNoMatch() {
        departmentService.createDepartment("D001", "Mathematics", "Alice Smith", 5000.0);

        List<Department> result = departmentService.getDepartmentsByBudgetRange(10000.0, 20000.0);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should throw DepartmentNotFoundException after deleting a department")
    public void testDeleteDepartment() {
        departmentService.createDepartment("D001", "Mathematics", "Alice Smith", 5000.0);
        departmentService.deleteDepartment("D001");

        assertThrows(DepartmentService.DepartmentNotFoundException.class, () -> departmentService.getDepartmentById("D001"));
    }

    @Test
    @DisplayName("Should return empty list when no departments exist")
    public void testGetAllDepartmentsWhenEmpty() {
        assertTrue(departmentService.getAllDepartments().isEmpty());
    }

    @Test
    @DisplayName("Should return all created departments")
    public void testGetAllDepartmentsReturnsAll() {
        departmentService.createDepartment("D001", "Mathematics", "Alice Smith", 5000.0);
        departmentService.createDepartment("D002", "Science", "Bob Jones", 3000.0);

        List<Department> result = departmentService.getAllDepartments();

        assertEquals(2, result.size());
    }
    @Test
    @DisplayName("Should return true when amount equals exactly the budget")
    public void testIsWithinBudgetExactAmount() {
        departmentService.createDepartment("D001", "Mathematics", "Alice Smith", 5000.0);
        Department dept = departmentService.getDepartmentById("D001");
        assertTrue(dept.isWithinBudget(5000.0));
    }
}