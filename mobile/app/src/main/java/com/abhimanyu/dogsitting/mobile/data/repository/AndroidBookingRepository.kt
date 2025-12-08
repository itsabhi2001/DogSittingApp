package com.abhimanyu.dogsitting.mobile.data.repository


import com.abhimanyu.dogsitting.shared.models.BookingRequest
import com.abhimanyu.dogsitting.shared.models.BookingResponse
import com.abhimanyu.dogsitting.shared.models.BookingStatsResponse
import com.abhimanyu.dogsitting.shared.models.PriceEstimateRequest
import com.abhimanyu.dogsitting.shared.models.PriceEstimateResponse
import com.abhimanyu.dogsitting.mobile.data.remote.BookingApiService
import com.abhimanyu.dogsitting.shared.domain.BookingRepository
class AndroidBookingRepository(private val api: BookingApiService) : BookingRepository {

    // For now we just need "get all bookings"
    override suspend fun getAllBookings(): List<BookingResponse> {
        return api.getAllBookings()
    }
    override suspend fun createBooking(request: BookingRequest): BookingResponse {
        return api.createBooking(request)
    }

    override suspend fun estimatePrice(request: PriceEstimateRequest): PriceEstimateResponse {
        return api.estimatePrice(request)
    }

    override suspend fun updateStatus(id: Long, newStatus: String): BookingResponse {
        // backend expects: { "status": "CONFIRMED" } etc.
        val body = mapOf("status" to newStatus)
        return api.updateStatus(id, body)
    }

    override suspend fun getBookingById(id: Long): BookingResponse {
        return api.getBookingById(id)
    }

    override suspend fun getStats(): BookingStatsResponse {
        return api.getStats()
    }

}