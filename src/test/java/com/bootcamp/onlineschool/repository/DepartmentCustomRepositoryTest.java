package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.config.JpaAuditingConfig;
import com.bootcamp.onlineschool.dto.DepartmentSearchCriteria;
import com.bootcamp.onlineschool.entity.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(JpaAuditingConfig.class) // enable auditing so the not-null audit columns get populated on persist
@DisplayName("DepartmentRepository Custom (Criteria API) Tests")
class DepartmentCustomRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        departmentRepository.save(new Department("Engineering", "ENG", 500_000.0, "Building A"));
        departmentRepository.save(new Department("Mathematics", "MATH", 200_000.0, "Building A"));
        departmentRepository.save(new Department("Physics", "PHYS", 800_000.0, "Building B"));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Empty criteria returns all departments")
    void emptyCriteriaReturnsAll() {
        List<Department> result = departmentRepository.search(new DepartmentSearchCriteria());
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Search by code returns the matching department")
    void searchByCode() {
        DepartmentSearchCriteria criteria = new DepartmentSearchCriteria();
        criteria.setCode("PHYS");

        List<Department> result = departmentRepository.search(criteria);

        assertEquals(1, result.size());
        assertEquals("Physics", result.get(0).getName());
    }

    @Test
    @DisplayName("Search by location returns all departments in that location")
    void searchByLocation() {
        DepartmentSearchCriteria criteria = new DepartmentSearchCriteria();
        criteria.setLocation("Building A");

        List<Department> result = departmentRepository.search(criteria);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(d -> d.getLocation().equals("Building A")));
    }

    @Test
    @DisplayName("Search by budget range filters inclusively")
    void searchByBudgetRange() {
        DepartmentSearchCriteria criteria = new DepartmentSearchCriteria();
        criteria.setMinBudget(200_000.0);
        criteria.setMaxBudget(500_000.0);

        List<Department> result = departmentRepository.search(criteria);

        assertEquals(2, result.size()); // Engineering (500k) and Mathematics (200k); Physics (800k) excluded
        assertTrue(result.stream().noneMatch(d -> d.getCode().equals("PHYS")));
    }

    @Test
    @DisplayName("Multiple criteria are combined with AND")
    void multipleCriteriaCombinedWithAnd() {
        DepartmentSearchCriteria criteria = new DepartmentSearchCriteria();
        criteria.setLocation("Building A");
        criteria.setMinBudget(300_000.0);

        List<Department> result = departmentRepository.search(criteria);

        // Only Engineering is in Building A AND has budget >= 300k
        assertEquals(1, result.size());
        assertEquals("ENG", result.get(0).getCode());
    }

    @Test
    @DisplayName("No match returns an empty list")
    void noMatchReturnsEmpty() {
        DepartmentSearchCriteria criteria = new DepartmentSearchCriteria();
        criteria.setCode("NOPE");

        List<Department> result = departmentRepository.search(criteria);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Search excludes soft-deleted departments (@Where applies to Criteria queries)")
    void searchExcludesSoftDeleted() {
        Department physics = departmentRepository.findByCode("PHYS").orElseThrow();
        physics.setDeleted(true);
        departmentRepository.save(physics);
        entityManager.flush();
        entityManager.clear();

        List<Department> result = departmentRepository.search(new DepartmentSearchCriteria());

        assertEquals(2, result.size());
        assertTrue(result.stream().noneMatch(d -> d.getCode().equals("PHYS")));
    }
}
