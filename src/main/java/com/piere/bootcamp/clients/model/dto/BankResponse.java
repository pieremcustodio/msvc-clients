package com.piere.bootcamp.clients.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.piere.bootcamp.clients.model.enums.TypeException;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class BankResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private boolean success;

    private String code;

    private String message;

    private TypeException type;

    private transient Object data;

    private LocalDateTime dateTime;

    public static BankResponse error(String code, String message, TypeException type) {
        return new BankResponse(false, code, message, type, null, LocalDateTime.now());
    }

    public static BankResponse ok(String message, Object data) {
        return new BankResponse(true, "200", message, null, data, LocalDateTime.now());
    }
}
