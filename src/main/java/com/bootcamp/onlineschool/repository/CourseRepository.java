package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    /**
     * Find a course by name
     * @param name the course name to search for
     * @return Optional containing the course if found
     */
    Optional<Course> findByName(String name);
    
    /**
     * Find courses by credits
     * @param credits the number of credits
     * @return List of courses with the specified credits
     */
    List<Course> findByCredits(Integer credits);
    
    /**
     * Find courses by duration
     * @param duration the duration in hours
     * @return List of courses with the specified duration
     */
    List<Course> findByDuration(Integer duration);
    
    /**
     * Check if a course exists with the given name
     * @param name the course name to check
     * @return true if course exists, false otherwise
     */
    boolean existsByName(String name);
    
    /**
     * Find courses by credits OR duration (custom query method for advanced operations)
     * @param credits the number of credits to search for
     * @param duration the duration to search for
     * @return List of courses matching either the credits or duration criteria
     */
    @Query("SELECT c FROM Course c WHERE c.credits = :credits OR c.duration = :duration")
    List<Course> findCoursesByCreditsOrDuration(@Param("credits") Integer credits, @Param("duration") Integer duration);
    
    /**
     * Find courses by minimum credits
     * @param minCredits the minimum number of credits
     * @return List of courses with at least the specified credits
     */
    List<Course> findByCreditsGreaterThanEqual(Integer minCredits);
    
    /**
     * Find courses by maximum duration
     * @param maxDuration the maximum duration
     * @return List of courses with duration less than or equal to the specified value
     */
    List<Course> findByDurationLessThanEqual(Integer maxDuration);
}