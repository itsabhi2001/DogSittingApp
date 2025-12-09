package com.abhimanyu.dogsitting.backend.exception;


import com.abhimanyu.dogsitting.backend.dto.ApiValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiValidationErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        var fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new ApiValidationErrorResponse.FieldError(
                        fe.getField(),
                        fe.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        return new ApiValidationErrorResponse(
                "Validation failed",
                fieldErrors
        );
    }
}

