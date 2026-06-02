package com.bootcamp.onlineschool.entity;

import com.bootcamp.onlineschool.config.JpaAuditingConfig;
import com.bootcamp.onlineschool.repository.DepartmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(JpaAuditingConfig.class) // enable auditing so the not-null audit columns get populated on persist
@DisplayName("Department Optimistic Locking Tests")
class DepartmentOptimisticLockTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Department persistDepartment() {
        Department dept = departmentRepository.save(
                new Department("Engineering", "ENG", 500_000.0, "Building A"));
        entityManager.flush();
        entityManager.clear();
        return dept;
    }

    @Test
    @DisplayName("A freshly persisted department starts at version 0")
    void newDepartmentStartsAtVersionZero() {
        Department dept = persistDepartment();

        Department loaded = departmentRepository.findById(dept.getId()).orElseThrow();
        assertEquals(0L, loaded.getVersion(), "Hibernate should initialise @Version to 0 on insert");
    }

    @Test
    @DisplayName("Each update increments the version")
    void updateIncrementsVersion() {
        Department dept = persistDepartment();

        Department loaded = departmentRepository.findById(dept.getId()).orElseThrow();
        loaded.setBudget(600_000.0);
        Department saved = departmentRepository.saveAndFlush(loaded);

        assertEquals(1L, saved.getVersion(), "First update should bump the version to 1");
    }

    @Test
    @DisplayName("A stale update loses the race and throws an optimistic lock exception")
    void concurrentUpdateThrowsOptimisticLockException() {
        Department dept = persistDepartment();
        Long id = dept.getId();

        // Two clients read the same row independently; both hold version 0.
        Department clientA = departmentRepository.findById(id).orElseThrow();
        entityManager.clear(); // detach clientA so the next read is a separate instance
        Department clientB = departmentRepository.findById(id).orElseThrow();
        entityManager.clear(); // detach clientB

        assertEquals(0L, clientA.getVersion());
        assertEquals(0L, clientB.getVersion());

        // Client B commits first: version 0 -> 1 in the database.
        clientB.setBudget(700_000.0);
        departmentRepository.saveAndFlush(clientB);
        entityManager.clear();

        // Client A still thinks the version is 0, so its write must be rejected.
        clientA.setBudget(800_000.0);
        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> departmentRepository.saveAndFlush(clientA),
                "A stale update should be rejected by optimistic locking");
    }

    @Test
    @DisplayName("The winning update is the one that persists")
    void winningUpdatePersists() {
        Department dept = persistDepartment();
        Long id = dept.getId();

        Department clientA = departmentRepository.findById(id).orElseThrow();
        entityManager.clear();
        Department clientB = departmentRepository.findById(id).orElseThrow();
        entityManager.clear();

        clientB.setBudget(700_000.0);
        departmentRepository.saveAndFlush(clientB);
        entityManager.clear();

        clientA.setBudget(800_000.0);
        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> departmentRepository.saveAndFlush(clientA));
        entityManager.clear();

        Department reloaded = departmentRepository.findById(id).orElseThrow();
        assertEquals(700_000.0, reloaded.getBudget(), 0.001,
                "Client B's committed value should survive");
        assertEquals(1L, reloaded.getVersion(), "Only one update should have succeeded");
    }
}
