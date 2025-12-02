package com.abhimanyu.dogsitting.mobile.ui.bookinglist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhimanyu.dogsitting.mobile.data.remote.RetrofitClient
import com.abhimanyu.dogsitting.mobile.data.repository.BookingRepository
import kotlinx.coroutines.launch

class BookingListViewModel(private val repository: BookingRepository = BookingRepository(RetrofitClient.bookingApiService)) : ViewModel() {
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
                val bookings = repository.fetchAllBookings()
                uiState = uiState.copy(
                    isLoading = false,
                    bookings = bookings,
                    errorMessage = null
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Unexpected error."
                )
            }
        }
    }
}