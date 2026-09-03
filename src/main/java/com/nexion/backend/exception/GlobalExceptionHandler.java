package com.nexion.backend.exception;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErroResponse> handleNotFound(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErroResponse> handleBusiness(BusinessException ex) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> handleConflict(DataIntegrityViolationException ex) {
        return build(HttpStatus.CONFLICT, "Violação de integridade (registro duplicado)");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b).orElse("Dados inválidos");
        return build(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGeneric(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<ErroResponse> handleBadCredentials(Exception ex) {
        return build(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
    }

    private ResponseEntity<ErroResponse> build(HttpStatus status, String message) {
        ErroResponse body = new ErroResponse(
                LocalDateTime.now(), status.value(), status.getReasonPhrase(), message);
        return ResponseEntity.status(status).body(body);
    }
}