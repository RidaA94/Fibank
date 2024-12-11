package com.task.fibank.dto;


public class ErrorResponseDto {
    private String error;  // A short description or name of the error
    private String message; // A detailed message regarding the error

    public ErrorResponseDto(String error, String message) {
        this.error = error;
        this.message = message;
    }
}
