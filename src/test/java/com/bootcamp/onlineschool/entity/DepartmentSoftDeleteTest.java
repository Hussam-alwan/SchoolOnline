package com.bootcamp.onlineschool.entity;

import com.bootcamp.onlineschool.repository.DepartmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Department Soft Delete Tests")
class DepartmentSoftDeleteTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private TestEntityManager entityManager;

    /** Persist a department, then detach everything so the next read hits the DB (not the 1st-level cache). */
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
        assertEquals(1, all.size(), "findAll should return only non-deleted departments");
        assertEquals(alive.getId(), all.get(0).getId());
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
        assertEquals(1, deleted.size(), "getDeletedDepartments should find the soft-deleted row");
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
        assertTrue(departmentRepository.getDeletedDepartments().isEmpty(),
                "No deleted rows should remain after a hard delete");
    }

    @Test
    @DisplayName("A freshly saved department is not deleted")
    void newDepartmentIsNotDeleted() {
        Department dept = persistDepartment("Engineering", "ENG");

        Department loaded = departmentRepository.findById(dept.getId()).orElseThrow();
        assertFalse(loaded.isDeleted(), "A new department should default to deleted = false");
    }
}
