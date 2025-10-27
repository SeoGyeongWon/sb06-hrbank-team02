package com.team2.hrbank.employee.dto;

public record EmployeeDistributionDto(
        String groupKey,
        Long count,
        Double percentage
) {
}
