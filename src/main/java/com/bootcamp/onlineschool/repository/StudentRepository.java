package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Student;
import com.bootcamp.onlineschool.entity.Clazz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    /**
     * Find a student by student ID
     * @param studentId the student ID to search for
     * @return Optional containing the student if found
     */
    Optional<Student> findByStudentId(String studentId);
    
    /**
     * Check if a student exists with the given student ID
     * @param studentId the student ID to check
     * @return true if student exists, false otherwise
     */
    boolean existsByStudentId(String studentId);
    
    /**
     * Find a student by email address
     * @param email the email to search for
     * @return Optional containing the student if found
     */
    Optional<Student> findByEmail(String email);
    
    /**
     * Find students by class (custom query method for advanced operations)
     * @param clazz the class to search for
     * @return List of students enrolled in the specified class
     */
    @Query("SELECT s FROM Student s JOIN s.classes c WHERE c = :clazz")
    List<Student> findStudentsByClazz(@Param("clazz") Clazz clazz);
    
    /**
     * Find students by class ID (custom query method for advanced operations)
     * @param clazzId the class ID to search for
     * @return List of students enrolled in the class with the specified ID
     */
    @Query("SELECT s FROM Student s JOIN s.classes c WHERE c.id = :clazzId")
    List<Student> findStudentsByClazzId(@Param("clazzId") Long clazzId);
}