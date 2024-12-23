package com.piere.bootcamp.clients.model.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@Data
public class Response implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;
    private String message;
    private Object data;
    private LocalDate date;

    public Response() {
    }

    public Response(boolean success, String message, Object data, LocalDate date) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.date = date;
    }

    public static Response ok(String message, Object data) {
        return new Response(true, message, data, LocalDate.now());
    }

    public static Response error(String message) {
        return new Response(false, message, null, LocalDate.now());
    }
}
