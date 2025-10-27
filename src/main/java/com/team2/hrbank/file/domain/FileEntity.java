package com.team2.hrbank.file.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "files",
    uniqueConstraints = {
    @UniqueConstraint(name = "uk_file_owner_type_owner_id",columnNames = {"owner_type","owner_id"})}
)
@Entity
public class FileEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false , name="file_name")
  private String name;

  @Column(nullable = false, name = "file_type")
  private String type;

  @Column(nullable = false, name = "file_size")
  private Long size;

  @Column(nullable = false, name = "file_path")
  private String path;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false,name = "owner_type")
  private OwnerType ownerType;

  @Column(nullable = false, name = "owner_id")
  private Long ownerId;

  @Builder
  public FileEntity(String name, String type, Long size, String path, OwnerType ownerType, Long ownerId) {
    this.name = name;
    this.type = type;
    this.size = size;
    this.path = path;
    this.ownerType = ownerType;
    this.ownerId = ownerId;
  }

}
