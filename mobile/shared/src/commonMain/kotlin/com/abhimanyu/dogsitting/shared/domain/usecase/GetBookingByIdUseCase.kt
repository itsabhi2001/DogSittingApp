package com.abhimanyu.dogsitting.shared.domain.usecase

import com.abhimanyu.dogsitting.shared.domain.BookingRepository
import com.abhimanyu.dogsitting.shared.models.BookingResponse

class GetBookingByIdUseCase(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(id: Long): BookingResponse {
        return repository.getBookingById(id)
    }
}
