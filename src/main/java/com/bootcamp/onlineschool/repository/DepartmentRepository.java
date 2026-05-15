package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByDepartmentId(String id);

    Optional<Department> findByName(String name);

    List<Department> findByBudgetGreaterThan(Double amount);

    List<Department> findByBudgetBetween(Double min, Double max);

    List<Department> findByEstablishedDateAfter(LocalDate establishedDateAfter);

    @Query("SELECT d FROM Department d ORDER BY d.establishedDate DESC")
    List<Department> findAllOrderByEstablishedDateDesc();

    //List<Department> findAllByOrderByEstablishedDateDesc();

    @Query("SELECT SUM (d.budget) FROM Department d")
    Double findTotalBudget();

    @Query("SELECT d FROM Department d where d.budget > (SELECT AVG(d2.budget) FROM Department d2)")
    List<Department> findDepartmentsWithAboveAverageBudget();

    @Query("SELECT d.location FROM Department d GROUP BY d.location")
    List<Object[]> findDistinctLocations();

    @Query("SELECT d FROM Department d WHERE YEAR(d.establishedDate) = :year")
    List<Department> findDepartmentByEstablishedYear(@Param("year") int year);


}