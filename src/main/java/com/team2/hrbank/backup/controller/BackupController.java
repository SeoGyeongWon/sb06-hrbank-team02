package com.team2.hrbank.backup.controller;

import com.team2.hrbank.backup.domain.BackupStatus;
import com.team2.hrbank.backup.dto.*;
import com.team2.hrbank.backup.service.BasicBackupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "데이터 백업 관리", description = "데이터 백업 관리 API")
@RestController
@RequestMapping("/api/backups")
@RequiredArgsConstructor
public class BackupController {
    private final BasicBackupService basicBackupService;

    @Operation(summary = "데이터 백업 생성", description = "데이터 백업을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "백업 생성 성공",
                content = @Content(schema = @Schema(implementation = BackupDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "409", description = "이미 진행 중인 백업이 있음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<BackupDto> addBackup() {
        return ResponseEntity.ok(basicBackupService.addBackup());
    }

    @Operation(summary = "데이터 백업 목록 조회", description = "데이터 백업 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                content = @Content(schema = @Schema(implementation = CursorPageResponseBackupDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 도는 지원하지 않는 정렬 필드"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<CursorPageResponseBackupDto> getAllBackups(
            @Parameter(description = "작업자")
            @RequestParam(required = false) String worker,

            @Parameter(description = "상태 (IN_PROGRESS, COMPLETED, FAILED)")
            @RequestParam(required = false) String status,

            @Parameter(description = "시작 시간(부터)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startedAtFrom,

            @Parameter(description = "시작 시간(까지)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startedAtTo,

            @Parameter(description = "이전 페이지 마지막 요소 ID")
            @RequestParam(required = false) Long idAfter,

            @Parameter(description = "커서 (이전 페이지의 마지막 ID)")
            @RequestParam(required = false) String cursor,

            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "10") Integer size,

            @Parameter(description = "정렬 필드 (startedAt, endedAt, status)")
            @RequestParam(defaultValue = "startedAt") String sortField,

            @Parameter(description = "정렬 방향 (ASC, DESC)")
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        BackupStatus backupStatus = null;
        if (status != null && !status.isBlank()) {
            try {
                backupStatus = BackupStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 상태 값입니다: " + status);
            }
        }

        CursorPageRequestBackupDto request = CursorPageRequestBackupDto.builder()
                .worker(worker)
                .status(backupStatus)
                .startedAtFrom(startedAtFrom)
                .startedAtTo(startedAtTo)
                .idAfter(idAfter)
                .cursor(cursor)
                .size(size)
                .sortField(sortField)
                .sortDirection(sortDirection)
                .build();

        CursorPageResponseBackupDto response = basicBackupService.getBackups(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "최근 데이터 백업 조회", description = "가장 최근의 데이터 백업을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = BackupDto.class))),
            @ApiResponse(responseCode = "204", description = "백업 데이터 없음"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/latest")
    public ResponseEntity<BackupDto> getLatestBackup(
            @Parameter(description = "상태 (IN_PROGRESS, COMPLETED, FAILED)", example = "COMPLETED")
            @RequestParam(required = false, defaultValue = "COMPLETED") String status
    ) {
        BackupStatus backupStatus;
        try {
            backupStatus = BackupStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 상태 값입니다: " + status);
        }

        BackupDto backup = basicBackupService.getLatestBackup(backupStatus);
        return backup != null
                ? ResponseEntity.ok(backup)
                : ResponseEntity.noContent().build();
    }

    @Operation(summary = "데이터 백업 완료", description = "데이터 백업을 완료 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "백업 완료 처리 성공",
                    content = @Content(schema = @Schema(implementation = BackupDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "백업을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/{id}/complete")
    public ResponseEntity<BackupDto> completeBackup(
            @Parameter(description = "백업 ID", required = true)
            @PathVariable Long id,

            @RequestBody BackupCompleteRequestDto request
    ) {
        BackupDto backup = basicBackupService.completeBackup(id, request.fileId());
        return ResponseEntity.ok(backup);
    }

    @Operation(summary = "데이터 백업 실패", description = "데이터 백업을 실패 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "백업 실패 처리 성공",
                    content = @Content(schema = @Schema(implementation = BackupDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "백업을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/{id}/fail")
    public ResponseEntity<BackupDto> failBackup(
            @Parameter(description = "백업 ID", required = true)
            @PathVariable Long id,

            @RequestBody BackupFailRequestDto request
    ) {
        BackupDto backup = basicBackupService.failBackup(id, request.fileId());
        return ResponseEntity.ok(backup);
    }
}