package com.team2.hrbank.backup.dto;

public record BackupCompleteRequestDto(
        Long backupId,
        Long fileId
) {
}
