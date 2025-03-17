package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class StudentTestResultNotFoundException extends RuntimeException {

    public StudentTestResultNotFoundException(String message) {
        super(message);
    }
}
