package com.example.van_loading_optimiser.ExceptionHandling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ApiErrorExceptionResponse {

    private Instant timestamp;
    private int status;
    private String error;
    private List<String> details;


}
