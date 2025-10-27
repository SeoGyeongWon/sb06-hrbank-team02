package com.team2.hrbank.file.dto;

import com.team2.hrbank.file.domain.OwnerType;

public record FileUploadRequestDto(
    String fileName,
    String fileType,
    Long fileSize,
    OwnerType ownerType,
    Long ownerId,
    byte[] fileData
) {

}
