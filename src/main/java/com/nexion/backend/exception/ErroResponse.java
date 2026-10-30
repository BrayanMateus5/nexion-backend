package com.nexion.backend.exception;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErroResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;

    public ErroResponse(LocalDateTime timestamp, int status, String error, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.error = error;
    }
}
