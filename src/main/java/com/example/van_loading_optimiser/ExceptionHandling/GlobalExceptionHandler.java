package com.example.van_loading_optimiser.ExceptionHandling;

import com.example.van_loading_optimiser.ExceptionHandling.Exceptions.InvalidRequestException;
import com.example.van_loading_optimiser.ExceptionHandling.Exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /*ვალიდაციისთვის exception*/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorExceptionResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                })
                .toList();

        return ResponseEntity.badRequest().body(
                ApiErrorExceptionResponse.builder()
                        .timestamp(Instant.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("Validation failed")
                        .details(details)
                        .build()
        );
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiErrorExceptionResponse> handleInvalidRequest(InvalidRequestException ex) {
        return ResponseEntity.badRequest().body(
                ApiErrorExceptionResponse.builder()
                        .timestamp(Instant.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("Invalid request")
                        .details(List.of(ex.getMessage()))
                        .build()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorExceptionResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiErrorExceptionResponse.builder()
                        .timestamp(Instant.now())
                        .status(HttpStatus.NOT_FOUND.value())
                        .error("Not found")
                        .details(List.of(ex.getMessage()))
                        .build()
        );
    }
}
