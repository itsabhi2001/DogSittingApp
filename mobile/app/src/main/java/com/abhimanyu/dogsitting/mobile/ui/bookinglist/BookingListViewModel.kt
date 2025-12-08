package com.abhimanyu.dogsitting.mobile.ui.bookinglist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhimanyu.dogsitting.mobile.di.ServiceLocator
import com.abhimanyu.dogsitting.shared.domain.usecase.GetBookingUseCase
import kotlinx.coroutines.launch

class BookingListViewModel(
    private val getBookingsUseCase: GetBookingUseCase = ServiceLocator.getBookingUsecase
) : ViewModel() {

    var uiState by mutableStateOf(BookingListUiState())
        private set

    init {
        // Automatically load bookings when ViewModel is first created
        loadBookings()
    }

    fun loadBookings() {
        // Set loading state + clear previous errors
        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val bookings = getBookingsUseCase()

                // Save raw list, then apply current filters
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = null,
                    allBookings = bookings
                )
                applyFilters()
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Unexpected error.",
                    allBookings = emptyList(),
                    filteredBookings = emptyList()
                )
            }
        }
    }


    fun updateSearchQuery(query: String) {
        uiState = uiState.copy(searchQuery = query)
        applyFilters()
    }


    fun updateStatusFilter(filter: StatusFilter) {
        uiState = uiState.copy(statusFilter = filter)
        applyFilters()
    }


    private fun applyFilters() {
        val state = uiState
        val query = state.searchQuery.trim().lowercase()
        var result = state.allBookings

        // Text search: client name, pet name, serviceType, status
        if (query.isNotEmpty()) {
            result = result.filter { booking ->
                booking.clientName.contains(query, ignoreCase = true) ||
                        booking.petName.contains(query, ignoreCase = true) ||
                        booking.serviceType.contains(query, ignoreCase = true) ||
                        booking.status.contains(query, ignoreCase = true)
            }
        }

        // Status filter
        result = result.filter { booking ->
            when (state.statusFilter) {
                StatusFilter.ALL -> true
                StatusFilter.PENDING -> booking.status == "PENDING"
                StatusFilter.CONFIRMED -> booking.status == "CONFIRMED"
                StatusFilter.CANCELED -> booking.status == "CANCELED"
            }
        }

        uiState = state.copy(filteredBookings = result)
    }
}
