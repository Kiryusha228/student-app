package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class YandexGptResponseNotFoundException extends RuntimeException {
    public YandexGptResponseNotFoundException(String message) {
        super(message);
    }
}
