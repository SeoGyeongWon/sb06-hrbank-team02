package com.team2.hrbank.file.service;

import com.team2.hrbank.file.domain.OwnerType;
import com.team2.hrbank.file.dto.FileMetadataDto;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  void saveFile(MultipartFile multipartFile, Long ownerId, OwnerType ownerType, String fileType) throws IOException;

  void deleteFile(Long fileId) throws IOException;

  byte[] fileDownload(Long fileId) throws IOException;

  FileMetadataDto getFileMetadata(Long fileId);



}
