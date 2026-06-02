package com.bootcamp.onlineschool.entity;

import com.bootcamp.onlineschool.config.JpaAuditingConfig;
import com.bootcamp.onlineschool.model.Course;
import com.bootcamp.onlineschool.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JpaAuditingConfig.class)
@DisplayName("AuditableEntity Audit Fields Tests")
public class AuditableEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private Student student;

    @BeforeEach
    public void setUp() {
        student = new Student("STU001", "John Doe", "john@school1.edu", 3.5);
        new Course("CS101", "Introduction to Computer Science", 3);
    }

    @Test
    @DisplayName("Should set createdAt when entity is persisted")
    public void testCreatedAtSetOnPersist() {
        assertNull(student.getCreatedAt(), "createdAt should be null before persist");

        LocalDateTime beforePersist = LocalDateTime.now();
        Student persisted = entityManager.persistAndFlush(student);
        LocalDateTime afterPersist = LocalDateTime.now();

        assertNotNull(persisted.getCreatedAt(), "createdAt should be set after persist");
        assertTrue(persisted.getCreatedAt().isAfter(beforePersist.minusSeconds(1)),
                "createdAt should be close to current time");
        assertTrue(persisted.getCreatedAt().isBefore(afterPersist.plusSeconds(1)),
                "createdAt should be close to current time");
    }

    @Test
    @DisplayName("Should set createdBy when entity is persisted")
    public void testCreatedBySetOnPersist() {
        assertNull(student.getCreatedBy(), "createdBy should be null before persist");

        Student persisted = entityManager.persistAndFlush(student);

        assertNotNull(persisted.getCreatedBy(), "createdBy should be set after persist");
        assertEquals("SYSTEM", persisted.getCreatedBy(), "createdBy should be 'SYSTEM'");
    }

    @Test
    @DisplayName("Should set updatedAt when entity is persisted")
    public void testUpdatedAtSetOnPersist() {
        assertNull(student.getUpdatedAt(), "updatedAt should be null before persist");

        Student persisted = entityManager.persistAndFlush(student);

        assertNotNull(persisted.getUpdatedAt(), "updatedAt should be set after persist");
        assertEquals(persisted.getCreatedAt(), persisted.getUpdatedAt(),
                "updatedAt should equal createdAt on initial persist");
    }

    @Test
    @DisplayName("Should set updatedBy when entity is persisted")
    public void testUpdatedBySetOnPersist() {

        assertNull(student.getUpdatedBy(), "updatedBy should be null before persist");

        Student persisted = entityManager.persistAndFlush(student);
        assertNotNull(persisted.getUpdatedBy(), "updatedBy should be set after persist");
        assertEquals("SYSTEM", persisted.getUpdatedBy(), "updatedBy should be 'SYSTEM'");
    }

    @Test
    @DisplayName("Should update updatedAt when entity is modified")
    public void testUpdatedAtChangedOnUpdate() throws InterruptedException {

        Student persisted = entityManager.persistAndFlush(student);
        LocalDateTime createdAt = persisted.getCreatedAt();
        LocalDateTime originalUpdatedAt = persisted.getUpdatedAt();

        Thread.sleep(100);

        persisted.setName("Jane Doe");
        Student updated = entityManager.persistAndFlush(persisted);

        assertNotNull(updated.getUpdatedAt(), "updatedAt should not be null");
        assertEquals(createdAt, updated.getCreatedAt(),
                "createdAt should not change on update");
        assertNotEquals(originalUpdatedAt, updated.getUpdatedAt(),
                "updatedAt should be different after update");
        assertTrue(updated.getUpdatedAt().isAfter(originalUpdatedAt),
                "updatedAt should be later than original");
    }

    @Test
    @DisplayName("Should update updatedBy when entity is modified")
    public void testUpdatedByChangedOnUpdate() throws InterruptedException {
        Student persisted = entityManager.persistAndFlush(student);
        String createdBy = persisted.getCreatedBy();

        Thread.sleep(100);

        persisted.setEmail("jane@school2.edu");
        Student updated = entityManager.persistAndFlush(persisted);

        assertEquals(createdBy, updated.getCreatedBy(),
                "createdBy should not change on update");
        assertEquals("SYSTEM", updated.getUpdatedBy(),
                "updatedBy should be 'SYSTEM' after update");
    }

    @Test
    @DisplayName("Should preserve createdAt and createdBy after update")
    public void testCreatedAuditFieldsImmutable() throws InterruptedException {
        // Arrange
        Student persisted = entityManager.persistAndFlush(student);
        LocalDateTime originalCreatedAt = persisted.getCreatedAt();
        String originalCreatedBy = persisted.getCreatedBy();

        Thread.sleep(100);

        // Act: Modify the entity multiple times
        persisted.setName("Updated Name");
        entityManager.persistAndFlush(persisted);

        Thread.sleep(100);

        persisted.setEmail("updated@school3.edu");
        Student finalState = entityManager.persistAndFlush(persisted);

        // Assert
        assertEquals(originalCreatedAt, finalState.getCreatedAt(),
                "createdAt should never change");
        assertEquals(originalCreatedBy, finalState.getCreatedBy(),
                "createdBy should never change");
        assertNotEquals(originalCreatedAt, finalState.getUpdatedAt(),
                "updatedAt should be different after modification");
    }


    @Test
    @DisplayName("Should work with Teacher entity")
    public void testAuditFieldsWithTeacherEntity() {
        // Arrange
        Teacher teacher = new Teacher("Dr. Smith", "smith@school1.edu",
                "Engineering", 5, 75000.0);
        assertNull(teacher.getCreatedAt(), "Teacher createdAt should be null before persist");

        // Act
        Teacher persisted = entityManager.persistAndFlush(teacher);

        // Assert
        assertNotNull(persisted.getCreatedAt(), "Teacher createdAt should be set");
        assertNotNull(persisted.getCreatedBy(), "Teacher createdBy should be set");
        assertEquals("SYSTEM", persisted.getCreatedBy(), "Teacher createdBy should be 'SYSTEM'");
    }

    @Test
    @DisplayName("Should work with Department entity")
    public void testAuditFieldsWithDepartmentEntity() {
        // Arrange
        Department dept = new Department("Engineering", "ENG", 500000.0, "Building A");
        assertNull(dept.getCreatedAt(), "Department createdAt should be null before persist");

        // Act
        Department persisted = entityManager.persistAndFlush(dept);

        // Assert
        assertNotNull(persisted.getCreatedAt(), "Department createdAt should be set");
        assertNotNull(persisted.getCreatedBy(), "Department createdBy should be set");
        assertEquals("SYSTEM", persisted.getCreatedBy(), "Department createdBy should be 'SYSTEM'");
    }


    @Test
    @DisplayName("Should set all audit fields on first persist")
    public void testAllAuditFieldsSetTogether() {
        // Arrange
        assertTrue(student.getCreatedAt() == null &&
                  student.getCreatedBy() == null &&
                  student.getUpdatedAt() == null &&
                  student.getUpdatedBy() == null,
                "All audit fields should be null before persist");

        // Act
        Student persisted = entityManager.persistAndFlush(student);

        // Assert
        assertNotNull(persisted.getCreatedAt(), "createdAt should not be null");
        assertNotNull(persisted.getCreatedBy(), "createdBy should not be null");
        assertNotNull(persisted.getUpdatedAt(), "updatedAt should not be null");
        assertNotNull(persisted.getUpdatedBy(), "updatedBy should not be null");

        assertEquals("SYSTEM", persisted.getCreatedBy(), "createdBy should be 'SYSTEM'");
        assertEquals("SYSTEM", persisted.getUpdatedBy(), "updatedBy should be 'SYSTEM'");
        assertEquals(persisted.getCreatedAt(), persisted.getUpdatedAt(),
                "createdAt should equal updatedAt on initial persist");
    }

    @Test
    @DisplayName("Should audit multiple separate entities independently")
    public void testMultipleEntitiesAuditedIndependently() throws InterruptedException {
        // Arrange & Act: Create first student
        Student student1 = new Student("STU001", "Alice", "alice@school1.edu");
        Student persisted1 = entityManager.persistAndFlush(student1);
        LocalDateTime student1CreatedAt = persisted1.getCreatedAt();

        Thread.sleep(100);

        // Create second student
        Student student2 = new Student("STU002", "Bob", "bob@school2.edu");
        Student persisted2 = entityManager.persistAndFlush(student2);
        LocalDateTime student2CreatedAt = persisted2.getCreatedAt();

        // Assert
        assertNotNull(student1CreatedAt, "Student 1 createdAt should be set");
        assertNotNull(student2CreatedAt, "Student 2 createdAt should be set");
        assertTrue(student2CreatedAt.isAfter(student1CreatedAt),
                "Student 2 should have later createdAt than Student 1");
    }

    @Test
    @DisplayName("Audit fields should have sensible values")
    public void testAuditFieldsHaveSensibleValues() {
        // Arrange & Act
        LocalDateTime beforePersist = LocalDateTime.now();
        Student persisted = entityManager.persistAndFlush(student);
        LocalDateTime afterPersist = LocalDateTime.now();

        // Assert
        assertTrue(persisted.getCreatedAt().isAfter(beforePersist.minusSeconds(1)) &&
                  persisted.getCreatedAt().isBefore(afterPersist.plusSeconds(1)),
                "createdAt should be within reasonable time range");
        assertTrue(persisted.getUpdatedAt().isAfter(beforePersist.minusSeconds(1)) &&
                  persisted.getUpdatedAt().isBefore(afterPersist.plusSeconds(1)),
                "updatedAt should be within reasonable time range");
        assertEquals(persisted.getCreatedAt(), persisted.getUpdatedAt(),
                "Initial createdAt and updatedAt should match");
        assertEquals("SYSTEM", persisted.getCreatedBy(), "createdBy should be 'SYSTEM'");
        assertEquals("SYSTEM", persisted.getUpdatedBy(), "updatedBy should be 'SYSTEM'");
    }
}

