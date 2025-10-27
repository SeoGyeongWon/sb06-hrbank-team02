package com.team2.hrbank.file;

import com.team2.hrbank.file.domain.FileEntity;
import com.team2.hrbank.file.dto.FileMetadataDto;
import org.mapstruct.Mapper;

@Mapper
public interface FileMapper {

  FileMetadataDto toDto(FileEntity fileEntity);

}
