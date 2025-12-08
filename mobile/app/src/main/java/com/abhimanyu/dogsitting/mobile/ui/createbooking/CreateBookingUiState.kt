package com.abhimanyu.dogsitting.mobile.ui.createbooking

data class CreateBookingUiState(
    val clientName: String = "",
    val clientEmail: String = "",
    val petName: String = "",
    val petSize: String = "MEDIUM",          // default
    val serviceType: String = "HOUSE_SITTING",
    val startDate: String = "",
    val endDate: String = "",
    val notes: String = "",

    val isSubmitting: Boolean = false,
    val isEstimating: Boolean = false,
    val estimatedPrice: Double? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
