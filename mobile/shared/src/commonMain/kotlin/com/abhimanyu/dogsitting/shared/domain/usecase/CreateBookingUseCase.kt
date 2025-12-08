package com.abhimanyu.dogsitting.shared.domain.usecase

import com.abhimanyu.dogsitting.shared.domain.BookingRepository
import com.abhimanyu.dogsitting.shared.models.BookingRequest
import com.abhimanyu.dogsitting.shared.models.BookingResponse

class CreateBookingUseCase(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(request: BookingRequest): BookingResponse {
        return repository.createBooking(request)
    }
}
