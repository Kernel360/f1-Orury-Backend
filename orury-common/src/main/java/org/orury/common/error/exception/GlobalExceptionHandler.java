package org.orury.common.error.exception;

import lombok.extern.slf4j.Slf4j;
import org.orury.common.error.code.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //비즈니스 로직 예외 처리 -> BadRequest의 역할
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ErrorResponse.of(e.getStatus(), e.getMessage()));
    }

    //인증 예외 처리 -> Unauthorized의 역할
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ErrorResponse.of(e.getStatus(), e.getMessage()));
    }

    // InfraStucture의 Reader, Store에서 발생하는 예외 처리
    @ExceptionHandler(InfraImplException.class)
    public ResponseEntity<ErrorResponse> handleInfraImplException(InfraImplException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ErrorResponse.of(e.getStatus(), e.getMessage()));
    }

    // @Valid에서 유효성 검증에 실패한 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError error = Objects.requireNonNull(e.getBindingResult().getFieldError(), "validation exception");
        return ResponseEntity
                .status(e.getStatusCode())
                .body(ErrorResponse.of(e.getStatusCode().value(), error.getDefaultMessage()));
    }

    //그 외 모든 예외 처리 -> InternalServerError의 역할
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("### Unexpected Error Occurred : {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "unhandled exception"));
    }
}
