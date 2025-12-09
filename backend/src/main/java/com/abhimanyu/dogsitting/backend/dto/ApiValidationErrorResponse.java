package com.abhimanyu.dogsitting.backend.dto;

import java.util.List;

public class ApiValidationErrorResponse {

    private String message;
    private List<FieldError> fieldErrors;

    public ApiValidationErrorResponse(String message, List<FieldError> fieldErrors) {
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public String getMessage() {
        return message;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public static class FieldError {
        private String field;
        private String message;

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }
}
