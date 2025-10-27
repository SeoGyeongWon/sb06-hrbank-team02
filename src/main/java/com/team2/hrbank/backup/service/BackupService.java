package com.team2.hrbank.backup.service;

import com.team2.hrbank.backup.domain.BackupStatus;
import com.team2.hrbank.backup.dto.BackupDto;
import com.team2.hrbank.backup.dto.CursorPageRequestBackupDto;
import com.team2.hrbank.backup.dto.CursorPageResponseBackupDto;

interface BackupService {

    // 데이터 백업 생성
    BackupDto addBackup();

    // 데이터 백업 이력 목록 조회
    CursorPageResponseBackupDto getBackups(CursorPageRequestBackupDto request);

    // 최근 백업 정보 조회
    BackupDto getLatestBackup(BackupStatus status);

}
