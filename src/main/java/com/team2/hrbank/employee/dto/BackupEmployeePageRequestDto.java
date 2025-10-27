package com.team2.hrbank.employee.dto;

public record BackupEmployeePageRequestDto(
        Long cursor,
        Long idAfter,
        Integer size
) {
}
