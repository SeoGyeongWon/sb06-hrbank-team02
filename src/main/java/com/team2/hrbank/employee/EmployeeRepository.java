package com.team2.hrbank.employee;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.team2.hrbank.employee.domain.Employee;
import com.team2.hrbank.employee.domain.EmployeeStatus;
import com.team2.hrbank.employee.dto.CursorPageRequestEmployeeDto;
import com.team2.hrbank.employee.dto.CursorPageResponseEmployeeDto;
import com.team2.hrbank.employee.dto.EmployeeDistributionDto;
import com.team2.hrbank.employee.dto.EmployeeTrendDto;
import io.github.openfeign.querydsl.jpa.spring.repository.QuerydslJpaRepository;

import java.time.LocalDate;
import java.util.List;

import static com.team2.hrbank.employee.domain.QEmployee.employee;

public interface EmployeeRepository extends QuerydslJpaRepository<Employee, Long> {

    default CursorPageResponseEmployeeDto searchAllBy(CursorPageRequestEmployeeDto request) {
        return null;
    }

    default List<EmployeeTrendDto> findTrendsBy(LocalDate from, LocalDate to, String unit) {
        return null;
    }

    default List<EmployeeDistributionDto> findDistributionsBy(
            String groupBy,
            EmployeeStatus status
    ) {
        return select(Projections.constructor(
                EmployeeDistributionDto.class,
                Expressions.constant(employee.departmentId.toString()),
                employee.count(),
                Expressions.constant(0.0)))
                .from(employee)
                .where(employee.status.eq(status))
                .groupBy(employee.departmentId)
                .fetch();
    }

    Long countByStatusAndHireDateBetween(EmployeeStatus status, LocalDate hireDateAfter, LocalDate hireDateBefore);
}
