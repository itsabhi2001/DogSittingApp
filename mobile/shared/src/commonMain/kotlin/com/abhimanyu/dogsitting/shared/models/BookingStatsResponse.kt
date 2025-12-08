package com.abhimanyu.dogsitting.shared.models

data class BookingStatsResponse(
    val total: Int,
    val pending: Int,
    val confirmed: Int,
    val canceled: Int
)