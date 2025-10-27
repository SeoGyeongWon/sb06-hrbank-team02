package com.team2.hrbank.file.dto;

import com.team2.hrbank.file.domain.OwnerType;
import lombok.Builder;

@Builder
public record FileDownloadResponseDto(
    Long fileId,
    String fileName,
    String fileType,
    Long fileSize,
    String filePath,
    OwnerType ownerType,
    Long ownerId,
    byte[] fileData
) {}
