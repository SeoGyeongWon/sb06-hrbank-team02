package com.team2.hrbank.department.repository;

import com.team2.hrbank.department.domain.Department;

import java.util.List;

public interface DepartmentRepositoryCustom {

  List<Department> findDepartmentsWithCursor(
      String nameOrDescription,
      String sortField,
      String sortDirection,
      Long idAfter,
      int size
  );
}
