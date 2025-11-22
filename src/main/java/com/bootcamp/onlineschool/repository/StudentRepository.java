package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * StudentRepository demonstrates Spring Data JPA
 * 
 * Demonstrates:
 * - @Repository annotation
 * - JpaRepository interface
 * - Custom query methods
 * - @Query annotation for JPQL
 * - Named parameters
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    /**
     * Find student by student ID
     */
    Optional<Student> findByStudentId(String studentId);
    
    /**
     * Find students by name (case-insensitive)
     */
    List<Student> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find students by email
     */
    Optional<Student> findByEmail(String email);
    
    /**
     * Find students with GPA above threshold
     */
    @Query("SELECT s FROM Student s WHERE s.gpa >= :gpaThreshold ORDER BY s.gpa DESC")
    List<Student> findHighAchievers(@Param("gpaThreshold") Double gpaThreshold);
    
    /**
     * Find all students sorted by name
     */
    @Query("SELECT s FROM Student s ORDER BY s.name ASC")
    List<Student> findAllSortedByName();
    
    /**
     * Count students with GPA above threshold
     */
    @Query("SELECT COUNT(s) FROM Student s WHERE s.gpa >= :gpaThreshold")
    Long countHighAchievers(@Param("gpaThreshold") Double gpaThreshold);
    
    /**
     * Calculate average GPA
     */
    @Query("SELECT AVG(s.gpa) FROM Student s")
    Double getAverageGpa();
}
