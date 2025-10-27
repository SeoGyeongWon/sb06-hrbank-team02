package com.team2.hrbank.employee.dto;

import java.time.LocalDate;

public record BackupEmployeeDto(
        Long id,
        String employeeNumber,
        String name,
        String email,
        String departmentName,
        String position,
        LocalDate hireDate,
        String status
) {
}
