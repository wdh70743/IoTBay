package org.example.iotbay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UnauthorizedAccessException extends RuntimeException{
    public UnauthorizedAccessException(String message){
        super(message);
    }
}
