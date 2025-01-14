package com.piere.bootcamp.clients.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.piere.bootcamp.clients.model.dto.BankResponse;
import com.piere.bootcamp.clients.model.enums.TypeException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Error occurred: {}", ex.getMessage());
        return new ResponseEntity<>(BankResponse.error("500", ex.getMessage(), TypeException.E), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
        log.error("Error occurred: {}", ex.getMessage());
        if (ex instanceof BankException) {
            BankException bankException = (BankException) ex;
            return new ResponseEntity<>(BankResponse.error("500", bankException.getMessage(), bankException.getType()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(BankResponse.error("500", ex.getMessage(), TypeException.E), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("Error occurred: {}", ex.getMessage());
        return new ResponseEntity<>(BankResponse.error("500", ex.getMessage(), TypeException.E), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
