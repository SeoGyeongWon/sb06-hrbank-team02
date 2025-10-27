package com.team2.hrbank.file;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileConfig {

  @Value("${file.upload-directory:./file-data-map}")
  private String uploadDirectory;

  @Bean
  public Path fileStoragePath() {
    return Paths.get(uploadDirectory);
  }

}
