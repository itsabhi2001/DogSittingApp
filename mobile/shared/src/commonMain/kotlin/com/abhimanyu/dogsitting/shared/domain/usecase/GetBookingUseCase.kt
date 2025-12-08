package com.abhimanyu.dogsitting.shared.domain.usecase

import com.abhimanyu.dogsitting.shared.domain.BookingRepository
import com.abhimanyu.dogsitting.shared.models.BookingResponse

class GetBookingUseCase(private val repository: BookingRepository) {

    suspend operator fun invoke(): List<BookingResponse> {
        return repository.getAllBookings()
    }

}