package com.edu.artexhibit.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponseDto {
    private int errorCode;
    private String details;
    private List<String> errors;
    private LocalDateTime timestamp;

    public ErrorResponseDto(int code, List<String> errors, LocalDateTime timestamp) {
        this.errorCode = code;
        this.errors = errors;
        this.timestamp = timestamp;
    }

    public ErrorResponseDto(int code, String details, LocalDateTime timestamp) {
        this.errorCode = code;
        this.details = details;
        this.timestamp = timestamp;
    }
}
