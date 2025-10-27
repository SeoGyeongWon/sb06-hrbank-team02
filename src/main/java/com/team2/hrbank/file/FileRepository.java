package com.team2.hrbank.file;

import org.springframework.data.jpa.repository.JpaRepository;
import com.team2.hrbank.file.domain.FileEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity,Long>{ }