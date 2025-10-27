package com.team2.hrbank.changelog.dto;

import com.team2.hrbank.changelog.domain.ChangeLogType;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class ChangeLogRequestDto {

    private ChangeLogRequestDto() {
        
    }

    public record PaginatedLogRequest(
            String employeeNumber,
            ChangeLogType type,
            String memo,
            String ipAddress,
            LocalDateTime atFrom,
            LocalDateTime atTo,
            Long idAfter,
            String cursor,
            @NotBlank(message = "페이지 크기는 필수입니다.")
            Integer size,
            @NotBlank(message = "정렬 기준은 필수입니다.")
            String sortField,
            String sortDirection
    ) {


    }

}
