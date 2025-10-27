package com.team2.hrbank.backup.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CursorPageResponseBackupDto(
        List<BackupDto> content,
        String nextCursor,
        Long nextIdAfter,
        Integer size,
        Long totalElements,
        Boolean hasNext
) {
}
