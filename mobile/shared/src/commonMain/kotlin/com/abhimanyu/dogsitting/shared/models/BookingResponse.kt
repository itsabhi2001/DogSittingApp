package com.abhimanyu.dogsitting.shared.models


data class BookingResponse (
    val id: Long,
    val clientName: String,
    val clientEmail: String,
    val petName: String,
    val petSize: String,
    val serviceType: String,
    val startDate: String,
    val endDate: String,
    val notes: String,
    val status: String,
    val totalPrice: Double,
    val createdAt: String
)
