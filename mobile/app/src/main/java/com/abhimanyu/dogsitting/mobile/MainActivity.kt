package com.abhimanyu.dogsitting.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abhimanyu.dogsitting.mobile.ui.bookinglist.BookingListScreen
import com.abhimanyu.dogsitting.mobile.ui.bookinglist.BookingListViewModel
import com.abhimanyu.dogsitting.mobile.ui.createbooking.CreateBookingScreen
import com.abhimanyu.dogsitting.mobile.ui.createbooking.CreateBookingViewModel
import com.abhimanyu.dogsitting.mobile.ui.bookingdetail.BookingDetailScreen
import com.abhimanyu.dogsitting.mobile.ui.bookingdetail.BookingDetailViewModel
import com.abhimanyu.dogsitting.mobile.ui.theme.DogSittingTheme
import com.abhimanyu.dogsitting.mobile.ui.dashboard.DashboardScreen
import com.abhimanyu.dogsitting.mobile.ui.dashboard.DashboardViewModel



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


            DogSittingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BookingAppRoot()
                }
            }


        }
    }
}

@Composable
fun BookingAppRoot() {
    val navController = rememberNavController()

    val bookingListViewModel: BookingListViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = "booking_list"
    ) {
        composable("booking_list") {
            val uiState = bookingListViewModel.uiState

            BookingListScreen(
                uiState = uiState,
                onRefresh = { bookingListViewModel.loadBookings() },
                onCreateBookingClick = {
                    navController.navigate("create_booking")
                },
                onBookingClick = { booking ->
                    navController.navigate("booking_details/${booking.id}")
                },
                onDashboardClick = {
                    navController.navigate("dashboard")
                }
            )
        }

        composable("create_booking") {
            val vm: CreateBookingViewModel = viewModel()
            val uiState = vm.uiState

            CreateBookingScreen(
                uiState = uiState,
                onClientNameChange = vm::updateClientName,
                onClientEmailChange = vm::updateClientEmail,
                onPetNameChange = vm::updatePetName,
                onPetSizeChange = vm::updatePetSize,
                onServiceTypeChange = vm::updateServiceType,
                onStartDateChange = vm::updateStartDate,
                onEndDateChange = vm::updateEndDate,
                onNotesChange = vm::updateNotes,
                onEstimateClick = vm::estimatePrice,
                onSubmitClick = {
                    vm.submitBooking {
                        bookingListViewModel.loadBookings()
                        navController.popBackStack()
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("booking_details/{bookingId}") { backStackEntry ->
            val bookingId = backStackEntry
                .arguments
                ?.getString("bookingId")
                ?.toLongOrNull()

            if (bookingId == null) {
                navController.popBackStack()
                return@composable
            }

            val detailVm: BookingDetailViewModel = viewModel()
            val uiState = detailVm.uiState

            // Load the booking whenever we enter this screen with this ID
            LaunchedEffect(bookingId) {
                detailVm.loadBooking(bookingId)
            }

            BookingDetailScreen(
                uiState = uiState,
                onStatusChange = { newStatus ->
                    detailVm.UpdateStatus(
                        bookingId = bookingId,
                        newStatus = newStatus,
                        onSuccess = {
                            // Refresh list & go back after successful update
                            bookingListViewModel.loadBookings()
                            navController.popBackStack()
                        }
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable("dashboard") {
            val dashboardViewModel: DashboardViewModel = viewModel()
            val uiState = dashboardViewModel.uiState

            DashboardScreen(
                uiState = uiState,
                onRefresh = { dashboardViewModel.loadStats() },
                onBackToBookings = {
                    navController.popBackStack(
                        route = "booking_list",
                        inclusive = false
                    )
                }
            )
        }
    }

}




