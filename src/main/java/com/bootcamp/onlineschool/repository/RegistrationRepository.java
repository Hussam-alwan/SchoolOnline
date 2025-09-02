package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Registration;
import com.bootcamp.onlineschool.entity.Student;
import com.bootcamp.onlineschool.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    
    /**
     * Find registrations by student
     * @param student the student to search for
     * @return List of registrations for the specified student
     */
    List<Registration> findByStudent(Student student);
    
    /**
     * Find registrations by student ID
     * @param studentId the student ID to search for
     * @return List of registrations for the student with the specified ID
     */
    List<Registration> findByStudentId(Long studentId);
    
    /**
     * Find registrations by course
     * @param course the course to search for
     * @return List of registrations for the specified course
     */
    List<Registration> findByCourse(Course course);
    
    /**
     * Find registrations by course ID
     * @param courseId the course ID to search for
     * @return List of registrations for the course with the specified ID
     */
    List<Registration> findByCourseId(Long courseId);
    
    /**
     * Find registrations by status
     * @param status the status to search for
     * @return List of registrations with the specified status
     */
    List<Registration> findByStatus(String status);
    
    /**
     * Find a registration by student and course
     * @param student the student
     * @param course the course
     * @return Optional containing the registration if found
     */
    Optional<Registration> findByStudentAndCourse(Student student, Course course);
    
    /**
     * Find a registration by student ID and course ID
     * @param studentId the student ID
     * @param courseId the course ID
     * @return Optional containing the registration if found
     */
    Optional<Registration> findByStudentIdAndCourseId(Long studentId, Long courseId);
    
    /**
     * Check if a registration exists for the given student and course
     * @param student the student
     * @param course the course
     * @return true if registration exists, false otherwise
     */
    boolean existsByStudentAndCourse(Student student, Course course);
    
    /**
     * Find registrations by status (custom query method for advanced operations)
     * This method is already implemented above, but adding additional status-related queries
     * @param status the status to search for
     * @return List of registrations with the specified status
     */
    @Query("SELECT r FROM Registration r WHERE r.status = :status")
    List<Registration> findRegistrationsByStatus(@Param("status") String status);
    
    /**
     * Find registrations by status (case-insensitive)
     * @param status the status to search for
     * @return List of registrations with the specified status (case-insensitive)
     */
    List<Registration> findByStatusIgnoreCase(String status);
    
    /**
     * Find registrations with grades
     * @return List of registrations that have grades assigned
     */
    @Query("SELECT r FROM Registration r WHERE r.grade IS NOT NULL")
    List<Registration> findRegistrationsWithGrades();
    
    /**
     * Find registrations without grades
     * @return List of registrations that don't have grades assigned
     */
    @Query("SELECT r FROM Registration r WHERE r.grade IS NULL")
    List<Registration> findRegistrationsWithoutGrades();
}