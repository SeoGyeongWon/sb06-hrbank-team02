package com.team2.hrbank.backup.domain;

import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Backup {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Nullable
    private Long id;

    private String worker;
    private LocalDateTime startedAt;

    @Nullable
    private LocalDateTime endedAt;

    @Enumerated(EnumType.STRING)
    private BackupStatus status;

    @Nullable
    private Long fileId;

    @Builder
    private Backup(
            String worker,
            LocalDateTime startedAt,
            @Nullable LocalDateTime endedAt,
            BackupStatus status,
            @Nullable Long fileId
    ) {
        this.id = null;
        this.worker = worker;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.status = status;
        this.fileId = fileId;
    }

    public BackupBuilder toBuilder() {
        return Backup.builder()
                .worker(this.worker)
                .startedAt(this.startedAt)
                .endedAt(this.endedAt)
                .status(this.status)
                .fileId(this.fileId);
    }

    public static Backup create(String worker) {
        return Backup.builder()
                .worker(worker)
                .startedAt(LocalDateTime.now())
                .status(BackupStatus.IN_PROGRESS)
                .build();
    }

    public Backup withCompleted(LocalDateTime endedAt, Long fileId) {
        return this.toBuilder()
                .endedAt(endedAt)
                .status(BackupStatus.COMPLETED)
                .fileId(fileId)
                .build();
    }

    public Backup withFailed(LocalDateTime endedAt, Long fileId) {
        return this.toBuilder()
                .endedAt(endedAt)
                .status(BackupStatus.FAILED)
                .fileId(fileId)
                .build();
    }

    public Backup withSkipped() {
        return this.toBuilder()
                .endedAt(null)
                .status(BackupStatus.SKIPPED)
                .fileId(null)
                .build();
    }
}