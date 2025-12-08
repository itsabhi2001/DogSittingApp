package com.abhimanyu.dogsitting.mobile.data.repository


import com.abhimanyu.dogsitting.mobile.data.models.BookingRequest
import com.abhimanyu.dogsitting.mobile.data.models.BookingResponse
import com.abhimanyu.dogsitting.mobile.data.models.BookingStatsResponse
import com.abhimanyu.dogsitting.mobile.data.models.PriceEstimateRequest
import com.abhimanyu.dogsitting.mobile.data.models.PriceEstimateResponse
import com.abhimanyu.dogsitting.mobile.data.remote.BookingApiService
class BookingRepository(private val api: BookingApiService) {

    // For now we just need "get all bookings"
    suspend fun getAllBookings(): List<BookingResponse> {
        return api.getAllBookings()
    }
    suspend fun createBooking(request: BookingRequest): BookingResponse {
        return api.createBooking(request)
    }

    suspend fun estimatePrice(request: PriceEstimateRequest): PriceEstimateResponse {
        return api.estimatePrice(request)
    }

    suspend fun updateStatus(id: Long, newStatus: String): BookingResponse {
        // backend expects: { "status": "CONFIRMED" } etc.
        val body = mapOf("status" to newStatus)
        return api.updateStatus(id, body)
    }

    suspend fun getBookingById(id: Long): BookingResponse {
        return api.getBookingById(id)
    }

    suspend fun getStats(): BookingStatsResponse {
        return api.getStats()
    }

}