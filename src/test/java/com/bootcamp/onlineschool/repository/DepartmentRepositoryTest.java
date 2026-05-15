package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class DepartmentRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department department1;
    private Department department2;
    private Department department3;

    @BeforeEach
    void setup() {
        department1=new Department("DEPT-001", "Engineering", "Alice Johnson", 75000.0, "Istanbul", LocalDate.of(2018, 3, 15));

        department2=new Department("DEPT-002", "Marketing", "Bob Smith", 40000.0, "Ankara", LocalDate.of(2020, 7, 1));

        department3=new Department("DEPT-003", "Human Resources", "Carol White", 30000.0, "Istanbul", LocalDate.of(2022, 11, 20));

        entityManager.persistAndFlush(department1);
        entityManager.persistAndFlush(department2);
        entityManager.persistAndFlush(department3);
    }

    @Test
    @DisplayName("should return department when department ID exists")
    public void findDepartmentById() {
        //when
        Department foundDepartment = departmentRepository.findById(department1.getId()).orElse(null);

        //then
        assertNotNull(foundDepartment);
        assertEquals("Engineering", foundDepartment.getName());
    }

    @Test
    @DisplayName("should update department name")
    public void updateDepartmentName() {
        //given
        department1.setName("Bob Smith");

        //when
        departmentRepository.save(department1);
        entityManager.flush();

        //then
        Department updated = departmentRepository.findById(department1.getId()).orElse(null);
        assertNotNull(updated);
        assertEquals("Bob Smith", updated.getName());
        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    @DisplayName("should delete department by ID")
    public void deleteDepartmentById() {
        //when
        departmentRepository.deleteById(department2.getId());
        entityManager.flush();

        Department deleted = departmentRepository.findById(department2.getId()).orElse(null);

        //then
        assertNull(deleted);
    }

    @Test
    @DisplayName("should find by departmentId")
    public void findDepartmentByDepartmentId() {
        Department foundDepartment = departmentRepository.findByDepartmentId("DEPT-001").orElse(null);

        assertNotNull(foundDepartment);
        assertEquals("Engineering", foundDepartment.getName());
    }

    @Test
    @DisplayName("should find all departments with budget greater than")
    public void findAllDepartmentsWithBudgetGreaterThan() {
        List<Department> departments = departmentRepository.findByBudgetGreaterThan(35000.0);

        assertEquals(2, departments.size());
        assertTrue(departments.stream().anyMatch(d -> d.getName().equals("Engineering")));
        assertTrue(departments.stream().anyMatch(d -> d.getName().equals("Marketing")));
    }

    @Test
    @DisplayName("should find all departments with budget between")
    public void findAllDepartmentsWithBudgetBetween() {
        List<Department> departments = departmentRepository.findByBudgetBetween(25000.0, 45000.0);

        assertEquals(2, departments.size());
        assertTrue(departments.stream().anyMatch(d -> d.getName().equals("Human Resources")));
        assertTrue(departments.stream().anyMatch(d -> d.getName().equals("Marketing")));
    }

    @Test
    @DisplayName("should find all departments established after a certain date")
    public void findAllDepartmentsEstablishedAfter() {
        List<Department> departments = departmentRepository.findByEstablishedDateAfter(LocalDate.of(2019, 1, 1));

        assertEquals(2, departments.size());
        assertTrue(departments.stream().anyMatch(d -> d.getName().equals("Marketing")));
        assertTrue(departments.stream().anyMatch(d -> d.getName().equals("Human Resources")));
    }

    @Test
    @DisplayName("should find all departments ordered by established date descending")
    public void findAllDepartmentsOrderedByEstablishedDateDesc() {
        List<Department> departments = departmentRepository.findAllOrderByEstablishedDateDesc();

        assertEquals(3, departments.size());
        assertEquals("Human Resources", departments.get(0).getName());
        assertEquals("Marketing", departments.get(1).getName());
        assertEquals("Engineering", departments.get(2).getName());
    }

    @Test
    @DisplayName("should find total budget of all departments")
    public void findTotalBudget() {
        Double totalBudget = departmentRepository.findTotalBudget();

        assertEquals(145000.0, totalBudget);
    }

    @Test
    @DisplayName("should find departments with above average budget")
    public void findDepartmentsWithAboveAverageBudget() {
        List<Department> departments = departmentRepository.findDepartmentsWithAboveAverageBudget();

        assertEquals(1, departments.size());
        assertTrue(departments.stream().anyMatch(d -> d.getName().equals("Engineering")));
    }

    @Test
    @DisplayName("should find departments by established year")
    public void findDepartmentsByEstablishedYear() {
        List<Department> departments = departmentRepository.findDepartmentByEstablishedYear(2020);

        assertEquals(1, departments.size());
        assertEquals("Marketing", departments.getFirst().getName());
    }

    @Test
    @DisplayName("should find distinct locations of departments")
    public void findDistinctLocations() {
        List<Object[]> locations = departmentRepository.findDistinctLocations();

        assertEquals(2, locations.size());
    }
}
