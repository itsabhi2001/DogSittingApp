package com.abhimanyu.dogsitting.mobile.di

import com.abhimanyu.dogsitting.shared.domain.BookingRepository
import com.abhimanyu.dogsitting.mobile.data.remote.RetrofitClient
import com.abhimanyu.dogsitting.mobile.data.repository.AndroidBookingRepository
import com.abhimanyu.dogsitting.shared.domain.usecase.GetBookingUseCase
import com.abhimanyu.dogsitting.shared.domain.usecase.GetBookingByIdUseCase
import com.abhimanyu.dogsitting.shared.domain.usecase.CreateBookingUseCase
import com.abhimanyu.dogsitting.shared.domain.usecase.EstimatePriceUseCase
import com.abhimanyu.dogsitting.shared.domain.usecase.UpdateBookingStatusUseCase
import com.abhimanyu.dogsitting.shared.domain.usecase.GetDashboardStatsUseCase


object ServiceLocator {

    private val bookingApiService = RetrofitClient.bookingApiService

    val bookingRepository: BookingRepository by lazy<BookingRepository> {
        AndroidBookingRepository(bookingApiService)
    }

    val getBookingUsecase: GetBookingUseCase =
        GetBookingUseCase(bookingRepository)

    val getBookingByIdUseCase: GetBookingByIdUseCase =
        GetBookingByIdUseCase(bookingRepository)

    val createBookingUseCase: CreateBookingUseCase =
        CreateBookingUseCase(bookingRepository)

    val estimatePriceUseCase: EstimatePriceUseCase =
        EstimatePriceUseCase(bookingRepository)

    val updateBookingStatusUseCase: UpdateBookingStatusUseCase =
        UpdateBookingStatusUseCase(bookingRepository)

    val getDashboardStatsUseCase: GetDashboardStatsUseCase =
        GetDashboardStatsUseCase(bookingRepository)
}
