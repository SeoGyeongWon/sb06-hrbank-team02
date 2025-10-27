package com.team2.hrbank.employee.dto;

import org.jspecify.annotations.Nullable;

import java.time.LocalDate;

public record EmployeeUpdateRequest(
        String name,
        String email,
        Long departmentId,
        String position,
        LocalDate hireDate,
        String status,
        @Nullable String memo
) {
}
