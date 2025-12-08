package com.abhimanyu.dogsitting.mobile.data.models

data class BookingStatsResponse(
    val total: Int,
    val pending: Int,
    val confirmed: Int,
    val canceled: Int
)