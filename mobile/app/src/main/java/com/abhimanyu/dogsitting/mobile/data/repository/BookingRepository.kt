package com.abhimanyu.dogsitting.mobile.data.repository

import com.abhimanyu.dogsitting.mobile.data.models.BookingResponse
import com.abhimanyu.dogsitting.mobile.data.remote.BookingApiService

class BookingRepository(private val api: BookingApiService) {

    // For now we just need "get all bookings"
    suspend fun fetchAllBookings(): List<BookingResponse> {
        return api.getAllBookings()
    }
    // - createBooking()
    // - updateBookingStatus()
    // - estimatePrice()
    // - getStats()
}