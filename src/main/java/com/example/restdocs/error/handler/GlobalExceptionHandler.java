package com.example.restdocs.error.handler;

import com.example.restdocs.error.dto.Field;
import com.example.restdocs.error.exception.BaseException;
import com.example.restdocs.error.exception.ErrorType;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ProblemDetail> handle(BaseException ex) {
        ErrorType errorType = ex.getErrorType();
        ProblemDetail problemDetail = createProblemDetail(errorType);
        return ResponseEntity.status(errorType.getHttpStatus()).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handle(MethodArgumentNotValidException ex) {
        ErrorType errorType = ErrorType.INVALID_INPUT_VALUE;
        ProblemDetail problemDetail = createProblemDetail(errorType);
        problemDetail.setProperty("errors", Field.errors(ex.getFieldErrors()));
        return ResponseEntity.badRequest().body(problemDetail);
    }


    private ProblemDetail createProblemDetail(ErrorType errorType) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(errorType.getHttpStatus(), errorType.getMessage());
        problemDetail.setType(URI.create(""));
        problemDetail.setProperty("error_code", errorType.getErrorCode());
        return problemDetail;
    }

}
