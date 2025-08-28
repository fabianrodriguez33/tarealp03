package com.example.biblioteca.controller.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.Map;

public class ApiError {
    private OffsetDateTime timestamp;
    private int status;          // 400, 404, 422, etc.
    private String error;        // BAD_REQUEST, NOT_FOUND, ...
    private String message;      // mensaje corto para el usuario
    private String path;         // /api/recurso/1
    private String method;       // GET/POST/PUT...
    private Map<String, Object> details; // errores adicionales

    public ApiError(int status, String error, String message, String path, String method, Map<String, Object> details) {
        this.timestamp = OffsetDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.method = method;
        this.details = details;
    }

    // Método de fábrica estático para simplificar
    public static ApiError of(HttpStatus status, String message, HttpServletRequest req, Map<String, Object> details) {
        return new ApiError(
                status.value(),
                status.getReasonPhrase(),
                message,
                req.getRequestURI(),
                req.getMethod(),
                details
        );
    }

    // Sobrecarga sin detalles
    public static ApiError of(HttpStatus status, String message, HttpServletRequest req) {
        return of(status, message, req, null);
    }
}
