package com.piere.bootcamp.clients.exception;

import com.piere.bootcamp.clients.model.enums.TypeException;
import lombok.Getter;

@Getter
public class BankException extends RuntimeException {
    
    private final TypeException type;

    public BankException(String message, TypeException type) {
        super(message);
        this.type = type;
    }
}
