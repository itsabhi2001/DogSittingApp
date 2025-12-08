package com.abhimanyu.dogsitting.mobile.data.models

data class BookingRequest (
    val clientName: String,
    val clientEmail: String,
    val petName: String,
    val petSize: String,       // "SMALL", "MEDIUM", "LARGE"
    val serviceType: String,   // "HOUSE_SITTING", "WALK", "DROP_IN"
    val startDate: String,     // "YYYY-MM-DD"
    val endDate: String,       // "YYYY-MM-DD"
    val notes: String?
)