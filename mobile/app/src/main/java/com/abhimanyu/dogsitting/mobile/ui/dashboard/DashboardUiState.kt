package com.abhimanyu.dogsitting.mobile.ui.dashboard

import com.abhimanyu.dogsitting.mobile.data.models.BookingStatsResponse

data class DashboardUiState(
    val isLoading: Boolean = false,
    val stats: BookingStatsResponse? = null,
    val errorMessage: String? = null
)
