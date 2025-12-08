package com.abhimanyu.dogsitting.mobile.ui.bookinglist

import com.abhimanyu.dogsitting.shared.models.BookingResponse

// Simple enum to track which status filter is active
enum class StatusFilter {
    ALL, PENDING, CONFIRMED, CANCELED
}

data class BookingListUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    // Raw list from the backend
    val allBookings: List<BookingResponse> = emptyList(),

    // List after applying search + filters
    val filteredBookings: List<BookingResponse> = emptyList(),

    // UI filter state
    val searchQuery: String = "",
    val statusFilter: StatusFilter = StatusFilter.ALL
)
