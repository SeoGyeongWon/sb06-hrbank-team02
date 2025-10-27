package com.team2.hrbank.file.dto;

public record FileUploadResponseDto(
    Long fileId,
    String fileName,
    String fileType,
    Long fileSize,
    String filePath
) {}
