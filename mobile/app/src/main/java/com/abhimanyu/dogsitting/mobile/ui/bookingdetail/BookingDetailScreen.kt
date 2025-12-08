package com.abhimanyu.dogsitting.mobile.ui.bookingdetail


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailScreen(
    uiState: BookingDetailUiState,
    onStatusChange: (String) -> Unit,
    onBack: () -> Unit
) {
    val booking = uiState.booking

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Booking Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.errorMessage != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.errorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onBack) {
                            Text("Back")
                        }
                    }
                }

                booking != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "${booking.petName} (${booking.petSize})",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text("Service: ${booking.serviceType}")
                        Text("Client: ${booking.clientName}")
                        Text("Email: ${booking.clientEmail}")
                        Text("Dates: ${booking.startDate} → ${booking.endDate}")
                        Text("Notes: ${booking.notes?.ifBlank { "None" } ?: "None"}")
                        Text(
                            text = "Total price: $${booking.totalPrice}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Current status: ${booking.status}",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Update status:",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatusButton(
                                label = "Pending",
                                statusValue = "PENDING",
                                onStatusChange = onStatusChange,
                                enabled = !uiState.isUpdatingStatus
                            )
                            StatusButton(
                                label = "Confirmed",
                                statusValue = "CONFIRMED",
                                onStatusChange = onStatusChange,
                                enabled = !uiState.isUpdatingStatus
                            )
                            StatusButton(
                                label = "Canceled",
                                statusValue = "CANCELED",
                                onStatusChange = onStatusChange,
                                enabled = !uiState.isUpdatingStatus
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusButton(
    label: String,
    statusValue: String,
    onStatusChange: (String) -> Unit,
    enabled: Boolean
) {
    Button(
        onClick = { onStatusChange(statusValue) },
        enabled = enabled
    ) {
        Text(label)
    }
}
