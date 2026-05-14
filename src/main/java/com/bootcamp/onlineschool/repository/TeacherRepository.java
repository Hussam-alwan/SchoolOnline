package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Teacher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

   // Homework Assignment 1: additional query methods

    /**
     * Find all teachers belonging to a department.
     * Derived query method using Spring Data JPA naming conventions.
     * @param department the department name
     * @return list of teachers in the given department
     */
    List<Teacher> findByDepartment(String department);

    /**
     * Find teachers with at least the given number of years of experience.
     * @param years minimum years of experience
     * @return list of experienced teachers
     */
    List<Teacher> findByYearsOfExperienceGreaterThanEqual(Integer years);

    /**
     * Find teachers whose salary is within the inclusive range [min, max].
     * @param min minimum salary (inclusive)
     * @param max maximum salary (inclusive)
     * @return list of teachers in the given salary range
     */
    List<Teacher> findBySalaryBetween(Double min, Double max);

    /**
     * JPQL: return all teachers ordered alphabetically by name.
     * @return list of all teachers sorted by name ascending
     */
    @Query("SELECT t FROM Teacher t ORDER BY t.name ASC")
    List<Teacher> findAllOrderByNameAsc();

    /**
     * JPQL: find teachers hired strictly after the given date.
     * @param date cutoff date (exclusive)
     * @return teachers hired after the date
     */
    @Query("SELECT t FROM Teacher t WHERE t.hireDate > :date")
    List<Teacher> findHiredAfter(@Param("date") LocalDate date);

    /**
     * JPQL: count how many teachers belong to a department.
     * @param department department name
     * @return number of teachers in that department
     */
    @Query("SELECT COUNT(t) FROM Teacher t WHERE t.department = :department")
    long countByDepartment(@Param("department") String department);

    /**
     * JPQL: compute the average salary of teachers in a department.
     * @param department department name
     * @return average salary, or null if no teachers in department
     */
    @Query("SELECT AVG(t.salary) FROM Teacher t WHERE t.department = :department")
    Double getAverageSalaryByDepartment(@Param("department") String department);

    /**
     * JPQL: find the top N highest-paid teachers. Use a Pageable of size N
     * (e.g. PageRequest.of(0, n)) to retrieve the top N rows.
     * @param pageable pagination parameter limiting result size
     * @return teachers sorted by salary descending
     */
    @Query("SELECT t FROM Teacher t ORDER BY t.salary DESC")
    List<Teacher> findTopHighestPaid(Pageable pageable);
}