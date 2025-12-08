package com.abhimanyu.dogsitting.shared.domain.usecase

import com.abhimanyu.dogsitting.shared.domain.BookingRepository
import com.abhimanyu.dogsitting.shared.models.BookingStatsResponse

class GetDashboardStatsUseCase(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(): BookingStatsResponse {
        return repository.getStats()
    }
}
