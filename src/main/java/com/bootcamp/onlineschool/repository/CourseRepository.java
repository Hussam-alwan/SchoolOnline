package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseId(String courseId);

    boolean existsByCourseId(String courseId);
}