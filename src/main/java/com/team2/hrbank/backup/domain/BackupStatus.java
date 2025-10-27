package com.team2.hrbank.backup.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BackupStatus {
    IN_PROGRESS("진행중"),
    COMPLETED("완료"),
    FAILED("실패"),
    SKIPPED("건너뜀");

    private final String label;
}
