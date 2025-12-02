package com.abhimanyu.dogsitting.mobile

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.abhimanyu.dogsitting.mobile.ui.bookinglist.BookingListScreen
import com.abhimanyu.dogsitting.mobile.ui.bookinglist.BookingListViewModel
import com.abhimanyu.dogsitting.mobile.ui.theme.DogSittingTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogSittingTheme() {
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
    // Get or create a ViewModel tied to this composition / Activity
    val bookingListViewModel: BookingListViewModel = viewModel()
    val uiState = bookingListViewModel.uiState

    BookingListScreen(
        uiState = uiState,
        onRefresh = { bookingListViewModel.loadBookings() },
        onCreateBookingClick = {
            // TODO: navigate to Create Booking screen (we'll add Navigation soon)
        },
        onBookingClick = { booking ->
            // TODO: navigate to a Booking Details / Status Update screen
        }
    )
}

