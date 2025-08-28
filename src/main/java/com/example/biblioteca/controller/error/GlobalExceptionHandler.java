package com.example.biblioteca.controller.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message, T data) {
        return ResponseEntity.status(status)
                .body(ApiResponse.error(status.value(), message, data));
    }

    /* 404: recurso de negocio no encontrado */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    /* 404: endpoint inexistente (ruta equivocada) */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleNoHandler(NoHandlerFoundException ex) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("path", ex.getRequestURL());
        details.put("method", ex.getHttpMethod());
        return error(HttpStatus.NOT_FOUND, "La ruta solicitada no existe.", details);
    }

    /* 400: JSON mal formado */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleUnreadable(HttpMessageNotReadableException ex) {
        Map<String, Object> details = new LinkedHashMap<>();
        String root = (ex.getMostSpecificCause() != null) ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        details.put("cause", safeMsg(root));
        return error(HttpStatus.BAD_REQUEST,
                "El cuerpo de la solicitud está malformado o contiene errores de formato.",
                details);
    }

    /* 400: validaciones @Valid fallidas en @RequestBody */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                fieldErrors.put(err.getField(), err.getDefaultMessage()));
        return error(HttpStatus.BAD_REQUEST, "Error de validación", fieldErrors);
    }

    /* 400: violaciones en @RequestParam / @PathVariable */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .toList();
        return error(HttpStatus.BAD_REQUEST, "Parámetros inválidos", errors);
    }

    /* 422: error de negocio (puedes ajustar a 400 si prefieres) */
    @ExceptionHandler({BusinessException.class, ServiceException.class})
    public ResponseEntity<ApiResponse<Void>> handleBusiness(RuntimeException ex) {
        return error(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), null);
    }

    /* 409: conflictos de integridad (FK, UNIQUE, etc.) */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleDataIntegrity(DataIntegrityViolationException ex) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("hint", "Verifique claves foráneas, unicidad o longitudes de columnas.");
        details.put("cause", safeMsg(ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage()));
        return error(HttpStatus.CONFLICT, "La operación viola restricciones de integridad.", details);
    }

    /* 400: parámetro requerido faltante */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleMissingParam(MissingServletRequestParameterException ex) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("parameter", ex.getParameterName());
        details.put("expectedType", ex.getParameterType());
        return error(HttpStatus.BAD_REQUEST, "Falta un parámetro requerido.", details);
    }

    /* 4xx/5xx lanzadas como ErrorResponseException */
    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleErrorResponse(ErrorResponseException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        Map<String, Object> details = new LinkedHashMap<>();
        if (ex.getBody() != null) {
            if (ex.getBody().getTitle() != null) details.put("title", ex.getBody().getTitle());
            if (ex.getBody().getDetail() != null) details.put("detail", ex.getBody().getDetail());
            if (ex.getBody().getInstance() != null) details.put("instance", ex.getBody().getInstance().toString());
        }
        details.putIfAbsent("path", req.getRequestURI());
        return error(status, (String) details.getOrDefault("title", "Error"), details);
    }

    /* 500: cualquier otra excepción no controlada */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleGeneric(Exception ex) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("cause", safeMsg(ex.getMessage()));
        return error(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ha ocurrido un error no esperado. Intente nuevamente.",
                details);
    }

    /* Evita filtrar mensajes internos peligrosos o muy largos */
    private String safeMsg(String msg) {
        if (msg == null) return null;
        String trimmed = msg.replaceAll("[\\r\\n]+", " ").trim();
        return trimmed.length() > 400 ? trimmed.substring(0, 400) + "…" : trimmed;
    }
}
