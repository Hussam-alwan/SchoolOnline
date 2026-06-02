package com.bootcamp.onlineschool.repository;

import com.bootcamp.onlineschool.dto.DepartmentSearchCriteria;
import com.bootcamp.onlineschool.entity.Department;
import java.util.List;

public interface DepartmentRepositoryCustom {

    List<Department> search(DepartmentSearchCriteria criteria);
}
