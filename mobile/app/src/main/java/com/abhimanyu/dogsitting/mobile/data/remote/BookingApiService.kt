package com.abhimanyu.dogsitting.mobile.data.remote

import com.abhimanyu.dogsitting.mobile.data.models.BookingResponse
import com.abhimanyu.dogsitting.mobile.data.models.BookingRequest
import com.abhimanyu.dogsitting.mobile.data.models.BookingStatsResponse
import com.abhimanyu.dogsitting.mobile.data.models.PriceEstimateRequest
import com.abhimanyu.dogsitting.mobile.data.models.PriceEstimateResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface BookingApiService {
    @GET("api/v1/bookings")
    suspend fun getAllBookings(): List<BookingResponse>

    @GET("api/v1/bookings/{id}")
    suspend fun getBookingById(
        @Path("id") id: Long
    ): BookingResponse

    @POST("api/v1/bookings")
    suspend fun createBooking(
        @Body request: BookingRequest
    ): BookingResponse

    @POST("api/v1/bookings/estimate")
    suspend fun estimatePrice(
        @Body request: PriceEstimateRequest
    ): PriceEstimateResponse

    @PATCH("api/v1/bookings/{id}/status")
    suspend fun updateStatus(
        @Path("id") id: Long,
        @Body statusUpdate: Map<String, String> // e.g. { "status": "CONFIRMED" }
    ): BookingResponse

    @GET("api/v1/bookings/stats")
    suspend fun getStats(): BookingStatsResponse
}