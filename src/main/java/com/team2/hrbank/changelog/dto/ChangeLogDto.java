package com.team2.hrbank.changelog.dto;

import com.team2.hrbank.changelog.domain.ChangeLogType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChangeLogDto(
        Long id,
        ChangeLogType type,
        String employeeNumber,
        String memo,
        String ipAddress,
        LocalDateTime at
) {
}
