package com.abhimanyu.dogsitting.mobile.data.remote

import com.abhimanyu.dogsitting.mobile.data.models.BookingResponse
import retrofit2.http.GET
interface BookingApiService {
    @GET("api/v1/bookings")
    suspend fun getAllBookings(): List<BookingResponse>
}