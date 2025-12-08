package com.abhimanyu.dogsitting.mobile.ui.bookingdetail

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhimanyu.dogsitting.mobile.data.remote.RetrofitClient
import com.abhimanyu.dogsitting.mobile.data.repository.BookingRepository
import com.abhimanyu.dogsitting.mobile.ui.createbooking.CreateBookingUiState
import kotlinx.coroutines.launch

class BookingDetailViewModel (
    private val repository: BookingRepository = BookingRepository(RetrofitClient.bookingApiService)
): ViewModel(){
    var uiState by mutableStateOf(BookingDetailUiState())
        private set

    fun loadBooking(id: Long){
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage =  null)
            try {
                val booking = repository.getBookingById(id)
                uiState = uiState.copy(
                    isLoading = false,
                    booking = booking,
                    errorMessage = null
                )
            }
            catch (e: Exception){
                Log.e("BookingDetailVM", "Failed to load booking", e)
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Failed to load booking: ${e.message}"
                )
            }
        }
    }

    fun UpdateStatus( bookingId: Long, newStatus: String, onSuccess: () -> Unit){
        viewModelScope.launch {
            uiState = uiState.copy(
                isUpdatingStatus = true,
                errorMessage = null
            )
            try {
                val updated = repository.updateStatus(bookingId, newStatus)
                uiState = uiState.copy(
                    isUpdatingStatus = false,
                    booking = updated
                )
                onSuccess()
            } catch (e: Exception) {
                Log.e("BookingDetailVM", "Failed to update status", e)
                uiState = uiState.copy(
                    isUpdatingStatus = false,
                    errorMessage = "Failed to update status: ${e.message}"
                )
            }
        }
    }
}