package com.abhimanyu.dogsitting.mobile.ui.bookinglist

import com.abhimanyu.dogsitting.mobile.data.models.BookingResponse

data class BookingListUiState(
    val isLoading: Boolean = false,
    val bookings: List<BookingResponse> = emptyList(),
    val errorMessage: String? = null
)
