package com.team2.hrbank.file.dto;

import com.team2.hrbank.file.domain.OwnerType;

public record FileDownloadRequestDto(
    Long fileId,
    OwnerType ownerType,
    Long ownerId
) {}
