package com.example.biblioteca.service.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.OffsetDateTime;

public class ProblemBuilder {
    public static ProblemDetail buil(HttpStatus status, String title, String detail, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(status);
        pd.setTitle(title);
        pd.setDetail(detail);
        pd.setType(URI.create("https://api.biblioteca.com/errors/" + status.value()));
        pd.setInstance(URI.create(req.getRequestURI())); // la ruta que falló

        // Agregar información adicional como metadata
        pd.setProperty("timestamp", OffsetDateTime.now().toString());
        pd.setProperty("method", req.getMethod());
        pd.setProperty("requestId", getRequestId()); // desde MDC
        return pd;
    }

    // Recupera el requestId si estás usando MDC en filtros
    private static String getRequestId() {
        return org.slf4j.MDC.get("requestId"); // null si no está definido
    }
}
