package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>, DepartmentRepositoryCustom {

    Optional<Department> findByCode(String code);

    Optional<Department> findByName(String name);

    List<Department> findByBudgetGreaterThan(Double budget);

    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.teachers WHERE d.id = :id")
    Optional<Department> findByIdWithTeachers(@Param("id") Long id);

    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.courses WHERE d.id = :id")
    Optional<Department> findByIdWithCourses(@Param("id") Long id);

    @Query("SELECT SIZE(d.teachers) FROM Department d WHERE d.id = :id")
    Integer countTeachersByDepartment(@Param("id") Long id);

    @Query("SELECT COALESCE(SUM(d.budget), 0.0) FROM Department d")
    Double getTotalBudget();

    @Query(value = "SELECT * FROM \"departments\" WHERE \"id\" = :id", nativeQuery = true)
    Department getDepartmentById(@Param("id") Long id);

    @Query(value = "SELECT * FROM \"departments\" WHERE \"deleted\" = true", nativeQuery = true)
    List<Department> getDeletedDepartments();

    @Modifying
    @Query(value = "UPDATE \"departments\" SET \"deleted\" = false WHERE \"id\" = :id", nativeQuery = true)
    void restoreById(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM \"departments\" WHERE \"id\" = :id", nativeQuery = true)
    void hardDeleteById(@Param("id") Long id);
}
