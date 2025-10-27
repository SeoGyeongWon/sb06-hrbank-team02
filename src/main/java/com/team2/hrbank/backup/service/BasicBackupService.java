package com.team2.hrbank.backup.service;

import com.team2.hrbank.backup.domain.BackupStatus;
import com.team2.hrbank.backup.dto.BackupDto;
import com.team2.hrbank.backup.dto.CursorPageRequestBackupDto;
import com.team2.hrbank.backup.dto.CursorPageResponseBackupDto;
import com.team2.hrbank.backup.repository.BackupRepository;
import com.team2.hrbank.changelog.service.ChangeLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BasicBackupService implements BackupService {

    private final BackupRepository backupRepository;
    private final ChangeLogService changeLogService;

    @Override
    public BackupDto addBackup() {
        return null;
    }

    @Override
    public CursorPageResponseBackupDto getBackups(CursorPageRequestBackupDto request) {
        return null;
    }

    @Override
    public BackupDto getLatestBackup(BackupStatus status) {
        return null;
    }

    public BackupDto completeBackup(Long backupId, Long fileId) {
        return null;
    }

    public BackupDto failBackup(Long backupId, Long fileId) {
        return null;
    }
}
