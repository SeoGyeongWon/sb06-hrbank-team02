package com.team2.hrbank.file.dto;

import com.team2.hrbank.file.domain.FileEntity;
import com.team2.hrbank.file.domain.OwnerType;
import lombok.Builder;

@Builder
public record FileMetadataDto(
    Long fileId,
    String fileName,
    String fileType,
    Long fileSize,
    String filePath,
    OwnerType ownerType,
    Long ownerId
){
  public static FileMetadataDto from(FileEntity fileEntity) {
    return FileMetadataDto.builder()
        .fileId(fileEntity.getId())
        .fileName(fileEntity.getName())
        .fileType(fileEntity.getType())
        .fileSize(fileEntity.getSize())
        .filePath(fileEntity.getPath())
        .ownerType(fileEntity.getOwnerType())
        .ownerId(fileEntity.getOwnerId())
        .build();
  }
}
