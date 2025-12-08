package com.abhimanyu.dogsitting.mobile.ui.bookingdetail

import com.abhimanyu.dogsitting.mobile.data.models.BookingResponse

data class BookingDetailUiState(
    val isLoading: Boolean = false,
    val booking: BookingResponse? = null,
    val errorMessage: String? = null,
    val isUpdatingStatus: Boolean = false
)
