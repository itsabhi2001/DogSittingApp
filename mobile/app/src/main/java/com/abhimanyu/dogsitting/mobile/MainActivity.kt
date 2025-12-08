package com.abhimanyu.dogsitting.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.abhimanyu.dogsitting.mobile.ui.bookingdetail.BookingDetailScreen
import com.abhimanyu.dogsitting.mobile.ui.bookingdetail.BookingDetailViewModel
import com.abhimanyu.dogsitting.mobile.ui.bookinglist.BookingListScreen
import com.abhimanyu.dogsitting.mobile.ui.bookinglist.BookingListViewModel
import com.abhimanyu.dogsitting.mobile.ui.createbooking.CreateBookingScreen
import com.abhimanyu.dogsitting.mobile.ui.createbooking.CreateBookingViewModel
import com.abhimanyu.dogsitting.mobile.ui.dashboard.DashboardScreen
import com.abhimanyu.dogsitting.mobile.ui.dashboard.DashboardViewModel
import com.abhimanyu.dogsitting.mobile.ui.navigation.BottomNavItems
import com.abhimanyu.dogsitting.mobile.ui.theme.DogSittingTheme
import androidx.compose.material3.Scaffold

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

    // We still keep a single BookingListViewModel instance shared across routes
    val bookingListViewModel: BookingListViewModel = viewModel()

    // Observe navigation so we know which tab to highlight / when to show bottom bar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            // Only show bottom bar on the main "root" destinations
            val currentRoute = currentDestination?.route
            if (currentRoute in BottomNavItems.bottomRoutes) {
                NavigationBar {
                    BottomNavItems.bottomRoutes.forEach { route ->
                        val label = when (route) {
                            BottomNavItems.ROUTE_BOOKINGS -> "Bookings"
                            BottomNavItems.ROUTE_DASHBOARD -> "Dashboard"
                            else -> route
                        }

                        // Simple built-in icons for now
                        val iconRes = when (route) {
                            BottomNavItems.ROUTE_BOOKINGS -> R.drawable.ic_bookings
                            BottomNavItems.ROUTE_DASHBOARD -> android.R.drawable.ic_menu_compass
                            else -> android.R.drawable.ic_menu_help
                        }

                        val selected = currentDestination.isRouteInHierarchy(route)

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(route) {
                                    // Pop up to start destination to avoid a giant back stack
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = label
                                )
                            },
                            label = { Text(label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItems.ROUTE_BOOKINGS,
            modifier = Modifier.padding(innerPadding)
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
                    // You can keep this or remove it later once you rely on bottom nav
                    onDashboardClick = {
                        navController.navigate("dashboard")
                    },
                    onSearchQueryChange = bookingListViewModel::updateSearchQuery,
                    onStatusFilterChange = bookingListViewModel::updateStatusFilter
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
}

// Helper to keep selection state correct for bottom nav
private fun NavDestination?.isRouteInHierarchy(route: String): Boolean {
    if (this == null) return false
    return hierarchy.any { it.route == route }
}
