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
import com.abhimanyu.dogsitting.shared.models.ApiValidationErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException

import kotlinx.coroutines.launch
import android.util.Patterns
import java.time.LocalDate

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

    private fun validateBeforeSubmit(): Boolean {
        uiState = uiState.copy(
            errorMessage = null,
            successMessage = null,
            clientNameError = null,
            clientEmailError = null,
            petNameError = null,
            petSizeError = null,
            serviceTypeError = null,
            dateError = null
        )

        var hasError = false
        // 1. Client name
        if (uiState.clientName.isBlank()) {
            uiState = uiState.copy(clientNameError = "Client name is required.")
            hasError = true
        }

        // 2) Email
        if (uiState.clientEmail.isBlank()) {
            uiState = uiState.copy(clientEmailError = "Email is required.")
            hasError = true
        } else if (!Patterns.EMAIL_ADDRESS.matcher(uiState.clientEmail).matches()) {
            uiState = uiState.copy(clientEmailError = "Please enter a valid email.")
            hasError = true
        }

        // 3) Pet name
        if (uiState.petName.isBlank()) {
            uiState = uiState.copy(petNameError = "Pet name is required.")
            hasError = true
        }

        // 4) Pet size
        if (uiState.petSize.isBlank()) {
            uiState = uiState.copy(petSizeError = "Please select a pet size.")
            hasError = true
        }

        // 5) Service type
        if (uiState.serviceType.isBlank()) {
            uiState = uiState.copy(serviceTypeError = "Please select a service type.")
            hasError = true
        }

        // 6) Dates
        if (uiState.startDate.isBlank() || uiState.endDate.isBlank()) {
            uiState = uiState.copy(dateError = "Please select a start and end date.")
            hasError = true
        } else {
            try {
                val start = LocalDate.parse(uiState.startDate)
                val end = LocalDate.parse(uiState.endDate)
                if (end.isBefore(start)) {
                    uiState = uiState.copy(dateError = "End date cannot be before start date.")
                    hasError = true
                }
            } catch (e: Exception) {
                uiState = uiState.copy(dateError = "Dates look invalid. Please pick them again.")
                hasError = true
            }
        }

        if (hasError) {
            uiState = uiState.copy(
                errorMessage = "Please fix the highlighted fields."
            )
        }

        // All good
        return !hasError
    }

    private fun validateBeforeEstimate(): Boolean {
        uiState = uiState.copy(
            errorMessage = null,
            successMessage = null
        )

        if (uiState.serviceType.isBlank()) {
            uiState = uiState.copy(errorMessage = "Please select a service type before estimating.")
            return false
        }

        if (uiState.startDate.isBlank() || uiState.endDate.isBlank()) {
            uiState = uiState.copy(errorMessage = "Please pick stay dates before estimating.")
            return false
        }

        return true
    }

    fun estimatePrice() {
        if (!validateBeforeEstimate()) return

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

        if (!validateBeforeSubmit()) return

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
                val friendlyMessage = when (e) {
                    is HttpException -> {
                        if (e.code() == 400) {
                            parseValidationError(e)
                        } else {
                            "Server error (${e.code()}). Please try again."
                        }
                    }
                    else -> "Failed to create booking: ${e.message}"
                }

                uiState = uiState.copy(
                    isSubmitting = false,
                    errorMessage = friendlyMessage
                )
            }
        }
    }

    private fun parseValidationError(e: HttpException): String {
        val errorBody = e.response()?.errorBody()?.string() ?: return "Validation failed."

        return try {
            val gson = Gson()
            val apiError = gson.fromJson(errorBody, ApiValidationErrorResponse::class.java)

            // Optional: map field errors into field-level UI errors
            val fieldErrors = apiError.fieldErrors.orEmpty()
            var newState = uiState.copy(
                clientNameError = null,
                clientEmailError = null,
                petNameError = null,
                petSizeError = null,
                serviceTypeError = null,
                dateError = null
            )

            for (fe in fieldErrors) {
                when (fe.field) {
                    "clientName" -> newState = newState.copy(clientNameError = fe.message)
                    "clientEmail" -> newState = newState.copy(clientEmailError = fe.message)
                    "petName" -> newState = newState.copy(petNameError = fe.message)
                    "petSize" -> newState = newState.copy(petSizeError = fe.message)
                    "serviceType" -> newState = newState.copy(serviceTypeError = fe.message)
                    "startDate", "endDate" -> newState = newState.copy(dateError = fe.message)
                }
            }

            uiState = newState

            // Return a general summary message
            apiError.message ?: "Validation failed."
        } catch (ex: Exception) {
            "Validation failed."
        }
    }


}