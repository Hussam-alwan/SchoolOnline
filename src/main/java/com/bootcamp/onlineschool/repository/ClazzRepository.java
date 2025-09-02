package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Clazz;
import com.bootcamp.onlineschool.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClazzRepository extends JpaRepository<Clazz, Long> {
    
    /**
     * Find a class by name
     * @param name the class name to search for
     * @return Optional containing the class if found
     */
    Optional<Clazz> findByName(String name);
    
    /**
     * Find classes by semester
     * @param semester the semester to search for
     * @return List of classes in the specified semester
     */
    List<Clazz> findBySemester(String semester);
    
    /**
     * Find classes by year
     * @param year the year to search for
     * @return List of classes in the specified year
     */
    List<Clazz> findByYear(Integer year);
    
    /**
     * Find classes by teacher
     * @param teacher the teacher to search for
     * @return List of classes taught by the specified teacher
     */
    List<Clazz> findByTeacher(Teacher teacher);
    
    /**
     * Find classes by teacher ID
     * @param teacherId the teacher ID to search for
     * @return List of classes taught by the teacher with the specified ID
     */
    List<Clazz> findByTeacherId(Long teacherId);
    
    /**
     * Check if a class exists with the given name
     * @param name the class name to check
     * @return true if class exists, false otherwise
     */
    boolean existsByName(String name);
    
    /**
     * Find classes by semester AND year (custom query method for advanced operations)
     * @param semester the semester to search for
     * @param year the year to search for
     * @return List of classes in the specified semester and year
     */
    @Query("SELECT c FROM Clazz c WHERE c.semester = :semester AND c.year = :year")
    List<Clazz> findClassesBySemesterAndYear(@Param("semester") String semester, @Param("year") Integer year);
    
    /**
     * Find classes by semester and year (using derived query method)
     * @param semester the semester to search for
     * @param year the year to search for
     * @return List of classes in the specified semester and year
     */
    List<Clazz> findBySemesterAndYear(String semester, Integer year);
    
    /**
     * Find classes with capacity greater than or equal to specified value
     * @param minCapacity the minimum capacity
     * @return List of classes with at least the specified capacity
     */
    List<Clazz> findByMaxCapacityGreaterThanEqual(Integer minCapacity);
}