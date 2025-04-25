package com.mymoneylog.server.utils;
 
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final int status;

    public CustomException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public CustomException(String message, int status) {
        super(message);
        this.status = status;
    }
}