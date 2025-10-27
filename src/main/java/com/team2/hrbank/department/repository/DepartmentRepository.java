package com.team2.hrbank.department.repository;

import com.team2.hrbank.department.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>, DepartmentRepositoryCustom {

  boolean existsByName(String name);

  Optional<Department> findByName(String name);
}