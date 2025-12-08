package com.abhimanyu.dogsitting.mobile.ui.dashboard

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhimanyu.dogsitting.mobile.data.remote.RetrofitClient
import com.abhimanyu.dogsitting.mobile.data.repository.AndroidBookingRepository
import com.abhimanyu.dogsitting.mobile.di.ServiceLocator
import com.abhimanyu.dogsitting.shared.domain.usecase.GetDashboardStatsUseCase
import kotlinx.coroutines.launch

class DashboardViewModel (private val getStats: GetDashboardStatsUseCase = ServiceLocator.getDashboardStatsUseCase
) : ViewModel() {

    var uiState by mutableStateOf(DashboardUiState())
        private set

    init {
        loadStats()
    }
    fun loadStats() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val stats = getStats()
                uiState = uiState.copy(
                    isLoading = false,
                    stats = stats,
                    errorMessage = null
                )
            } catch (e: Exception) {
                Log.e("DashboardVM", "Failed to load stats", e)
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Failed to load stats: ${e.message}"
                )
            }
        }
    }
}