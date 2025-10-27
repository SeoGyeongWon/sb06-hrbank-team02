package com.team2.hrbank.file.service;

import com.team2.hrbank.file.FileMapper;
import com.team2.hrbank.file.FileRepository;

import com.team2.hrbank.file.domain.FileEntity;
import com.team2.hrbank.file.domain.OwnerType;
import com.team2.hrbank.file.dto.FileMetadataDto;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BasicFileService implements  FileService {

  private final FileRepository fileRepository;
  private final Path fileStoragePath;
  private final FileMapper fileMapper;

  @Override
  public void saveFile(MultipartFile multipartFile, Long ownerId, OwnerType ownerType,
      String fileType) throws IOException {
    if(multipartFile == null || multipartFile.isEmpty()) {
      return;
    }

    Path filePath = fileStoragePath.resolve(fileType);
    Files.createDirectory(filePath);

    Path targetPath = filePath.resolve(UUID.randomUUID()+"_"+ multipartFile.getOriginalFilename());
    multipartFile.transferTo(targetPath.toFile());

    fileRepository.save(FileEntity.builder()
        .name(multipartFile.getOriginalFilename())
        .type(multipartFile.getContentType())
        .size(multipartFile.getSize())
        .path(targetPath.toString())
        .ownerType(ownerType)
        .ownerId(ownerId)
        .build()
    );
  }

  @Override
  public void deleteFile(Long fileId) throws IOException {
    FileEntity fileEntity = fileRepository.findById(fileId)
        .orElseThrow(()-> new IllegalArgumentException("File not found"));
    fileRepository.delete(fileEntity);
  }

  @Override
  public byte[] fileDownload(Long fileId) throws IOException {
    FileEntity fileEntity = fileRepository.findById(fileId)
        .orElseThrow(()-> new IllegalArgumentException("File not found"));
    return Files.readAllBytes(Paths.get(fileEntity.getPath()));
  }

  @Override
  public FileMetadataDto getFileMetadata(Long fileId) {
    FileEntity fileEntity = fileRepository.findById(fileId)
        .orElseThrow(()-> new IllegalArgumentException("File not found"));
    return fileMapper.toDto(fileEntity);
  }
}
