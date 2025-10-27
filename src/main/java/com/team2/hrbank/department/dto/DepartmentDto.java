package com.team2.hrbank.department.dto;

import java.time.LocalDate;

public record DepartmentDto(
        Long id,
        String name,
        String description,
        LocalDate establishedDate,
        Long employeeCount
) {
}
