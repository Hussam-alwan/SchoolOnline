package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByEmail(String email);

    List<Teacher> findByDepartment(String department);

    List<Teacher> findByYearsOfExperienceGreaterThanEqual(Integer years);

    List<Teacher> findBySalaryBetween(Double min, Double max);

    @Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.courses WHERE t.id = :id")
    Optional<Teacher> findByIdWithCourses(@Param("id") Long id);

    @Query("SELECT t FROM Teacher t WHERE SIZE(t.courses) >= :minCourses")
    List<Teacher> findTeachersWithMinCourses(@Param("minCourses") int minCourses);

    @Query("SELECT AVG(t.salary) FROM Teacher t WHERE t.department = :dept")
    Double getAverageSalaryByDepartment(@Param("dept") String department);
}
