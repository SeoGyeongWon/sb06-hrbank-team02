package com.team2.hrbank.employee.dto;

import java.time.LocalDate;

public record CursorPageRequestEmployeeDto(
        String nameOrEmail,
        String employeeNumber,
        String departmentName,
        String position,
        LocalDate hireDateFrom,
        LocalDate hireDateTo,
        String status,
        Long idAfter,
        Long cursor,
        Integer size,
        String sortField,
        String sortDirection
) {
}
