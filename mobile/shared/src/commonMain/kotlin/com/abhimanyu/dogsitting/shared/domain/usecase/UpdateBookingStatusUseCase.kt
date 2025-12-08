package com.abhimanyu.dogsitting.shared.domain.usecase

import com.abhimanyu.dogsitting.shared.domain.BookingRepository
import com.abhimanyu.dogsitting.shared.models.BookingResponse

class UpdateBookingStatusUseCase(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(id: Long, newStatus: String): BookingResponse {
        return repository.updateStatus(id, newStatus)
    }
}
