package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByStudentId(String studentId);

    boolean existsByStudentId(String studentId);

    List<Student> findByNameContainingIgnoreCase(String name);

    List<Student> findByGpaGreaterThanEqual(double gpa);

    @Query("SELECT AVG(s.gpa) FROM Student s")
    Double getAverageGpa();
}
