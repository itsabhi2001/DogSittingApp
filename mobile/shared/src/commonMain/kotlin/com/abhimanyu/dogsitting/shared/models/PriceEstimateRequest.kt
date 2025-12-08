package com.abhimanyu.dogsitting.shared.models

data class PriceEstimateRequest(
    val serviceType: String,
    val petSize: String,
    val startDate: String,
    val endDate: String
)