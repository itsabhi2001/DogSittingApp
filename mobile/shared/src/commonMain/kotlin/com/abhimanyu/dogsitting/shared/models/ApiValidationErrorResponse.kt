package com.abhimanyu.dogsitting.shared.models

data class ApiValidationErrorResponse(
    val message: String? = null,
    val fieldErrors: List<FieldError>? = null
) {
    data class FieldError(
        val field: String,
        val message: String
    )
}
