package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    /**
     * Find a teacher by employee ID
     * @param employeeId the employee ID to search for
     * @return Optional containing the teacher if found
     */
    Optional<Teacher> findByEmployeeId(String employeeId);
    
    /**
     * Check if a teacher exists with the given employee ID
     * @param employeeId the employee ID to check
     * @return true if teacher exists, false otherwise
     */
    boolean existsByEmployeeId(String employeeId);
    
    /**
     * Find a teacher by email address
     * @param email the email to search for
     * @return Optional containing the teacher if found
     */
    Optional<Teacher> findByEmail(String email);
    
    /**
     * Find teachers by department (custom query method for advanced operations)
     * @param department the department to search for
     * @return List of teachers in the specified department
     */
    List<Teacher> findTeachersByDepartment(String department);
    
    /**
     * Find teachers by department (case-insensitive)
     * @param department the department to search for
     * @return List of teachers in the specified department (case-insensitive)
     */
    List<Teacher> findTeachersByDepartmentIgnoreCase(String department);
}