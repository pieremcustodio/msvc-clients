package com.piere.bootcamp.clients.model.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;
    private String message;
    private Object data;
    private LocalDate date;

    public static Response ok(String message, Object data) {
        return new Response(true, message, data, LocalDate.now());
    }

    public static Response error(String message) {
        return new Response(false, message, null, LocalDate.now());
    }
}
