package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.dto.DepartmentSearchCriteria;
import com.bootcamp.onlineschool.entity.Department;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class DepartmentRepositoryCustomImpl implements DepartmentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Department> search(DepartmentSearchCriteria criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Department> query = cb.createQuery(Department.class);
        Root<Department> department = query.from(Department.class);

        // Build the WHERE clause dynamically: add a predicate only for criteria that are present.
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getCode() != null) {
            predicates.add(cb.equal(department.get("code"), criteria.getCode()));
        }
        if (criteria.getLocation() != null) {
            predicates.add(cb.equal(department.get("location"), criteria.getLocation()));
        }
        if (criteria.getMinBudget() != null) {
            predicates.add(cb.greaterThanOrEqualTo(department.get("budget"), criteria.getMinBudget()));
        }
        if (criteria.getMaxBudget() != null) {
            predicates.add(cb.lessThanOrEqualTo(department.get("budget"), criteria.getMaxBudget()));
        }

        // No criteria -> no predicates -> returns all (non-deleted) departments.
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }
}
