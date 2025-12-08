package com.abhimanyu.dogsitting.mobile.ui.bookinglist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abhimanyu.dogsitting.mobile.data.models.BookingResponse


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingListScreen(
    uiState: BookingListUiState,
    onRefresh: () -> Unit,
    onCreateBookingClick: () -> Unit,
    onBookingClick: (BookingResponse) -> Unit,
    onDashboardClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bookings") },
                actions = {
                    Button(onClick = onDashboardClick) {
                        Text("Dashboard")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onCreateBookingClick) {
                        Text("New")
                    }
                }

            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    // Centered loading spinner
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.errorMessage != null -> {
                    // Error message + retry button
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.errorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onRefresh) {
                            Text("Retry")
                        }
                    }
                }

                uiState.bookings.isEmpty() -> {
                    // Empty state
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No bookings yet.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onCreateBookingClick) {
                            Text("Create your first booking")
                        }
                    }
                }

                else -> {
                    // Actual list
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.bookings) { booking ->
                            BookingListItem(
                                booking = booking,
                                onClick = { onBookingClick(booking) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BookingListItem(
    booking: BookingResponse,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(
            text = "${booking.petName} • ${booking.serviceType}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Client: ${booking.clientName}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Status: ${booking.status} • \$${booking.totalPrice}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
