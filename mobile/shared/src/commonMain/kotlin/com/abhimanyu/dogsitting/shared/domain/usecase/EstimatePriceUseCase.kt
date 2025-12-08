package com.abhimanyu.dogsitting.shared.domain.usecase

import com.abhimanyu.dogsitting.shared.domain.BookingRepository
import com.abhimanyu.dogsitting.shared.models.PriceEstimateRequest
import com.abhimanyu.dogsitting.shared.models.PriceEstimateResponse

class EstimatePriceUseCase(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(request: PriceEstimateRequest): PriceEstimateResponse {
        return repository.estimatePrice(request)
    }
}
