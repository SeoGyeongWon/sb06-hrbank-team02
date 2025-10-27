package com.team2.hrbank.employee.dto;

import org.jspecify.annotations.Nullable;

import java.time.LocalDate;

public record EmployeeCreateRequest(
        String name,
        String email,
        Long departmentId,
        String position,
        LocalDate hireDate,
        @Nullable String memo
) {
}
