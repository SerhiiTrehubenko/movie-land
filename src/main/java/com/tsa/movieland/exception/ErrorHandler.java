package com.tsa.movieland.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(GenreNotFoundException.class)
    public ResponseEntity<ErrorMessage> genreNotFoundException(GenreNotFoundException e) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<ErrorMessage> movieNotFoundException(MovieNotFoundException e) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MovieEnrichmentException.class)
    public ResponseEntity<ErrorMessage> movieEnrichmentException(MovieEnrichmentException e) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorMessage> movieEnrichmentException(SQLException e) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build(),
                HttpStatus.BAD_REQUEST);
    }
}
