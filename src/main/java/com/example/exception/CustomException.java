package com.example.exception;

import org.springframework.http.HttpStatus;

//Custom Exception for handling specific errors
public class CustomException extends RuntimeException{
    private final HttpStatus status;

    public CustomException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }
    public HttpStatus getStatus(){
        return status;
    }
}
