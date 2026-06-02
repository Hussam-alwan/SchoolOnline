package com.bootcamp.onlineschool.entity;

import com.bootcamp.onlineschool.config.JpaAuditingConfig;
import com.bootcamp.onlineschool.repository.DepartmentRepository;
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
@DisplayName("Department Soft Delete Tests")
class DepartmentSoftDeleteTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private TestEntityManager entityManager;

  private Department persistDepartment(String name, String code) {
        Department dept = departmentRepository.save(new Department(name, code, 500_000.0, "Building A"));
        entityManager.flush();
        entityManager.clear();
        return dept;
    }

    private void softDelete(Long id) {
        Department dept = departmentRepository.findById(id).orElseThrow();
        dept.setDeleted(true);
        departmentRepository.save(dept);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("A freshly saved department is not deleted")
    void newDepartmentIsNotDeleted() {
        Department dept = persistDepartment("Engineering", "ENG");

        Department loaded = departmentRepository.findById(dept.getId()).orElseThrow();
        assertFalse(loaded.isDeleted(), "A new department should default to deleted = false");
    }

    @Test
    @DisplayName("Soft delete hides the entity from findById")
    void softDeleteHidesFromFindById() {
        Department dept = persistDepartment("Engineering", "ENG");

        softDelete(dept.getId());

        assertTrue(departmentRepository.findById(dept.getId()).isEmpty(),
                "@Where should hide the soft-deleted department from findById");
    }

    @Test
    @DisplayName("Soft delete hides the entity from findAll")
    void softDeleteHidesFromFindAll() {
        Department alive = persistDepartment("Engineering", "ENG");
        Department toDelete = persistDepartment("Mathematics", "MATH");

        softDelete(toDelete.getId());

        List<Department> all = departmentRepository.findAll();
        assertTrue(all.stream().anyMatch(d -> d.getId().equals(alive.getId())),
                "findAll should still contain the non-deleted department");
        assertTrue(all.stream().noneMatch(d -> d.getId().equals(toDelete.getId())),
                "findAll should not contain the soft-deleted department");
    }

    @Test
    @DisplayName("Soft-deleted row still physically exists in the database")
    void softDeletedRowStillExists() {
        Department dept = persistDepartment("Engineering", "ENG");

        softDelete(dept.getId());

        // Native query bypasses @Where, so it can still see the row
        Department raw = departmentRepository.getDepartmentById(dept.getId());
        assertNotNull(raw, "The row should still exist physically after a soft delete");
        assertTrue(raw.isDeleted(), "The persisted row should be marked deleted = true");

        List<Department> deleted = departmentRepository.getDeletedDepartments();
        assertTrue(deleted.stream().anyMatch(d -> d.getId().equals(dept.getId())),
                "getDeletedDepartments should find the soft-deleted row");
    }

    @Test
    @DisplayName("restoreById makes a soft-deleted entity visible again")
    void restoreMakesEntityVisibleAgain() {
        Department dept = persistDepartment("Engineering", "ENG");
        softDelete(dept.getId());
        assertTrue(departmentRepository.findById(dept.getId()).isEmpty(), "precondition: hidden after soft delete");

        departmentRepository.restoreById(dept.getId());
        entityManager.flush();
        entityManager.clear();

        assertTrue(departmentRepository.findById(dept.getId()).isPresent(),
                "Restored department should be visible again");
        assertFalse(departmentRepository.findById(dept.getId()).orElseThrow().isDeleted(),
                "Restored department should have deleted = false");
    }

    @Test
    @DisplayName("hardDeleteById permanently removes the row")
    void hardDeleteRemovesRowPermanently() {
        Department dept = persistDepartment("Engineering", "ENG");
        softDelete(dept.getId());

        departmentRepository.hardDeleteById(dept.getId());
        entityManager.flush();
        entityManager.clear();

        assertNull(departmentRepository.getDepartmentById(dept.getId()),
                "Hard delete should remove the row entirely, even from native queries");
        assertTrue(departmentRepository.getDeletedDepartments().stream()
                        .noneMatch(d -> d.getId().equals(dept.getId())),
                "No deleted row should remain after a hard delete");
    }
}
