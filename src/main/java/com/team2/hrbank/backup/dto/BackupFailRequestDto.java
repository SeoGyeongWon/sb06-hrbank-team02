package com.team2.hrbank.backup.dto;

public record BackupFailRequestDto(
        Long backupId,
        Long fileId
) {
}
