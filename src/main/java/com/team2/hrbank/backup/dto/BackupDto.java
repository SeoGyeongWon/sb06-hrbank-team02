package com.team2.hrbank.backup.dto;

import com.team2.hrbank.backup.domain.BackupStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BackupDto(
        Long id,
        String worker,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        BackupStatus status,
        Long fileId
) {
}
