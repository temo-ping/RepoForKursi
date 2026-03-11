package com.example.van_loading_optimiser.ExceptionHandling.Exceptions;

public class InvalidRequestException extends RuntimeException{
    public InvalidRequestException(String message) {
        super(message);
    }
}
