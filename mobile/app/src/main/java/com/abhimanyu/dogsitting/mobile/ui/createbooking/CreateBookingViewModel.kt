package com.abhimanyu.dogsitting.mobile.ui.createbooking

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhimanyu.dogsitting.shared.models.BookingRequest
import com.abhimanyu.dogsitting.shared.models.PriceEstimateRequest
import com.abhimanyu.dogsitting.mobile.di.ServiceLocator
import com.abhimanyu.dogsitting.shared.domain.usecase.CreateBookingUseCase
import com.abhimanyu.dogsitting.shared.domain.usecase.EstimatePriceUseCase
import kotlinx.coroutines.launch

class CreateBookingViewModel(
    private val createBooking: CreateBookingUseCase = ServiceLocator.createBookingUseCase,
    private val estimatePriceUseCase: EstimatePriceUseCase = ServiceLocator.estimatePriceUseCase
) : ViewModel() {

    var uiState by mutableStateOf(CreateBookingUiState())
        private set

    fun updateClientName(value: String) {
        uiState = uiState.copy(clientName = value)
    }

    fun updateClientEmail(value: String) {
        uiState = uiState.copy(clientEmail = value)
    }

    fun updatePetName(value: String) {
        uiState = uiState.copy(petName = value)
    }

    fun updatePetSize(value: String) {
        uiState = uiState.copy(petSize = value)
    }

    fun updateServiceType(value: String) {
        uiState = uiState.copy(serviceType = value)
    }

    fun updateStartDate(value: String) {
        uiState = uiState.copy(startDate = value)
    }

    fun updateEndDate(value: String) {
        uiState = uiState.copy(endDate = value)
    }

    fun updateNotes(value: String) {
        uiState = uiState.copy(notes = value)
    }

    private fun buildRequest(): BookingRequest {
        return BookingRequest(
            clientName = uiState.clientName,
            clientEmail = uiState.clientEmail,
            petName = uiState.petName,
            petSize = uiState.petSize,
            serviceType = uiState.serviceType,
            startDate = uiState.startDate,
            endDate = uiState.endDate,
            notes = uiState.notes.ifBlank { null }
        )
    }
    private fun buildEstimateRequest(): PriceEstimateRequest {
        return PriceEstimateRequest(
            serviceType = uiState.serviceType,
            petSize = uiState.petSize,
            startDate = uiState.startDate,
            endDate = uiState.endDate
        )
    }

    fun estimatePrice() {
        viewModelScope.launch {
            uiState = uiState.copy(
                isEstimating = true,
                errorMessage = null,
                successMessage = null,
            )

            try {
                val request = buildEstimateRequest()
                Log.d("EstimateDebug", "Sending estimate request: $request")
                val response = estimatePriceUseCase(request)
                Log.d("EstimateDebug", "New estimatedPrice: ${response.estimatedPrice}")

                uiState = uiState.copy(
                    isEstimating = false,
                    estimatedPrice = response.estimatedPrice

                )
                Log.d("EstimateDebug", "UiState after estimate: $uiState")

            } catch (e: Exception) {
                Log.e("EstimateDebug", "Estimate failed", e)
                uiState = uiState.copy(
                    isEstimating = false,
                    errorMessage = "Failed to estimate price: ${e.message}"
                )
            }




        }
    }

    fun submitBooking(onSuccess: () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(
                isSubmitting = true,
                errorMessage = null,
                successMessage = null

            )
            try {
                val request = buildRequest()
                val created = createBooking(request)
                uiState = uiState.copy(
                    isSubmitting = false,
                    successMessage = "Booking created for ${created.petName}",
                    estimatedPrice = null // reset or keep as you like
                )
                onSuccess()
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isSubmitting = false,
                    errorMessage = "Failed to create booking: ${e.message}"
                )
            }
        }
    }
}