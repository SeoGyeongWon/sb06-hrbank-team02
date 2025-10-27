package com.team2.hrbank.employee.dto;

import java.util.List;

public record BackupEmployeePageResponseDto(
        List<BackupEmployeeDto> content,
        String nextCursor,
        Long nextIdAfter,
        Integer size,
        Long totalElements,
        Boolean hasNext
) {
}
