package com.task.fibank.dto;

public class ResponseDto {
    private int statusCode;       // HTTP status code
    private String statusMessage;  // A message describing the result of the operation
    public ResponseDto(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
