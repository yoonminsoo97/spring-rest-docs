package com.example.restdocs.error.dto;

import lombok.Getter;

import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Field {

    private String field;
    private String input;
    private String message;

    private Field(String field, String input, String message) {
        this.field = field;
        this.input = input;
        this.message = message;
    }

    public static List<Field> errors(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(fieldError -> new Field(
                        fieldError.getField(),
                        fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString(),
                        fieldError.getDefaultMessage()
                )).collect(Collectors.toList());
    }

}
