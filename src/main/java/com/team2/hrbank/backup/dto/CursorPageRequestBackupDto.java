package com.team2.hrbank.backup.dto;

import com.team2.hrbank.backup.domain.BackupStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CursorPageRequestBackupDto(
        String worker,
        BackupStatus status,
        LocalDateTime startedAtFrom,
        LocalDateTime startedAtTo,
        Long idAfter,
        String cursor,
        Integer size,
        String sortField,
        String sortDirection
) {
}