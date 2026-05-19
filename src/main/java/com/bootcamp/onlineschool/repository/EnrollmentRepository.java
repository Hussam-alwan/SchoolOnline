package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Enrollment;
import com.bootcamp.onlineschool.entity.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudent_Id(Long studentId);

    List<Enrollment> findByCourse_Id(Long courseId);

    List<Enrollment> findByStatus(EnrollmentStatus status);

    Optional<Enrollment> findByStudent_IdAndCourse_Id(Long studentId, Long courseId);

    @Query("SELECT e FROM Enrollment e WHERE e.enrollmentDate BETWEEN :start AND :end")
    List<Enrollment> findByEnrollmentDateBetween(@Param("start") LocalDate start,
                                                 @Param("end") LocalDate end);

    @Query("SELECT e FROM Enrollment e WHERE e.grade = :grade")
    List<Enrollment> findByGrade(@Param("grade") String grade);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.status = :status")
    long countByStatus(@Param("status") EnrollmentStatus status);

    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId AND e.status = :status")
    List<Enrollment> findByCourseIdAndStatus(@Param("courseId") Long courseId,
                                             @Param("status") EnrollmentStatus status);
}
