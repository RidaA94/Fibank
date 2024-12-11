package com.task.fibank.exception;

import com.task.fibank.dto.ErrorResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice

public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CashierNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleCashierNotFound(CashierNotFoundException ex) {
        logger.warn("Cashier not found: {}", ex.getCashierName());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto("Cashier Not Found", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException ex) {
        logger.warn("Illegal argument: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto("Invalid Input", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex) {
        logger.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto("Internal Server Error", "An unexpected error occurred"));
    }
}
