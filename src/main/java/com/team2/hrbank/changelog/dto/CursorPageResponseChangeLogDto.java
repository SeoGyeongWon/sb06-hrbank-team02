package com.team2.hrbank.changelog.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CursorPageResponseChangeLogDto(
        List<ChangeLogDto> content,
        String nextCursor,
        Long nextIdAfter,
        Integer size,
        Long totalElements,
        Boolean hasNext
) {
}
