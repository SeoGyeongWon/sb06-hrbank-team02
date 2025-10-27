package com.team2.hrbank.file.exception;

import java.io.FileNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class FileExceptionHandler {

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<String> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
    return ResponseEntity
        .status(HttpStatus.PAYLOAD_TOO_LARGE)
        .body(e.getMessage());
  }

  @ExceptionHandler(FileNotFoundException.class)
  public ResponseEntity<String> handleFileNotFoundException(FileNotFoundException e) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(e.getMessage());
  }
}
