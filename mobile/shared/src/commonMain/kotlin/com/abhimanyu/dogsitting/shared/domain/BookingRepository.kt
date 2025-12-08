package com.abhimanyu.dogsitting.shared.domain


import com.abhimanyu.dogsitting.shared.models.BookingResponse
import com.abhimanyu.dogsitting.shared.models.BookingRequest
import com.abhimanyu.dogsitting.shared.models.PriceEstimateRequest
import com.abhimanyu.dogsitting.shared.models.PriceEstimateResponse
import com.abhimanyu.dogsitting.shared.models.BookingStatsResponse

interface BookingRepository {
    suspend fun getAllBookings(): List<BookingResponse>
    suspend fun getBookingById(id: Long): BookingResponse
    suspend fun createBooking(request: BookingRequest): BookingResponse
    suspend fun estimatePrice(request: PriceEstimateRequest): PriceEstimateResponse
    suspend fun updateStatus(id: Long, newStatus: String): BookingResponse
    suspend fun getStats(): BookingStatsResponse
}