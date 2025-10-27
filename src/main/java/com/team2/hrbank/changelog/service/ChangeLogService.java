package com.team2.hrbank.changelog.service;

import com.team2.hrbank.changelog.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface ChangeLogService {

    CursorPageResponseChangeLogDto getChangeLogs(ChangeLogRequestDto.PaginatedLogRequest request);

    List<DiffDto> getDiffByChangeLogId(Long id);

    Long getTotalCount(LocalDateTime fromDate, LocalDateTime toDate);

    //BackUp 모듈의 백업 여부 판단을 위한 최근 로그 일자 조회
    LocalDateTime getRecentLogDate();

    ChangeLogDto addEmployeeInsertLog(EmployeeLogDto.CreateLogRequest request);

    ChangeLogDto addEmployeeUpdateLog(EmployeeLogDto.CreateLogRequest request);

    ChangeLogDto addEmployeeDeleteLog(EmployeeLogDto.CreateDeleteLogRequest request);

}