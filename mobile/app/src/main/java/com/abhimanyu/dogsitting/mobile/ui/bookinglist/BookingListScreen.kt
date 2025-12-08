package com.abhimanyu.dogsitting.mobile.ui.bookinglist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abhimanyu.dogsitting.shared.models.BookingResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingListScreen(
    uiState: BookingListUiState,
    onRefresh: () -> Unit,
    onCreateBookingClick: () -> Unit,
    onBookingClick: (BookingResponse) -> Unit,
    onDashboardClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onStatusFilterChange: (StatusFilter) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bookings") },
                actions = {
                    // Dashboard icon
                    IconButton(onClick = onDashboardClick) {
                        Icon(
                            imageVector = Icons.Filled.BarChart,
                            contentDescription = "Dashboard"
                        )
                    }
                    // New booking icon
                    IconButton(onClick = onCreateBookingClick) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Create booking"
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
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.errorMessage != null -> {
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

                else -> {
                    // Content with filters + list
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 🔍 Search field
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = onSearchQueryChange,
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Search (client, pet, status, service)") },
                            singleLine = true,
                            trailingIcon = {
                                if (uiState.searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { onSearchQueryChange("") }) {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = "Clear search"
                                        )
                                    }
                                }
                            }
                        )

                        // ✅ Status filter chips
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            StatusFilter.values().forEach { filter ->
                                val selected = uiState.statusFilter == filter
                                FilterChip(
                                    selected = selected,
                                    onClick = { onStatusFilterChange(filter) },
                                    label = {
                                        Text(
                                            when (filter) {
                                                StatusFilter.ALL -> "All"
                                                StatusFilter.PENDING -> "Pending"
                                                StatusFilter.CONFIRMED -> "Confirmed"
                                                StatusFilter.CANCELED -> "Canceled"
                                            }
                                        )
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // 📝 Results area
                        when {
                            uiState.allBookings.isEmpty() -> {
                                // No data at all
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text("No bookings yet.")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(onClick = onCreateBookingClick) {
                                        Text("Create your first booking")
                                    }
                                }
                            }

                            uiState.filteredBookings.isEmpty() -> {
                                // We have bookings but none match filters/search
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text("No bookings match your filters.")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    TextButton(onClick = {
                                        onSearchQueryChange("")
                                        onStatusFilterChange(StatusFilter.ALL)
                                    }) {
                                        Text("Clear filters")
                                    }
                                }
                            }

                            else -> {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(uiState.filteredBookings) { booking ->
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
            text = "${booking.petName} • ${booking.serviceType.toDisplayName()}",
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
private fun String.toDisplayName(): String =
    this
        .lowercase()
        .split("_")
        .joinToString(" ") { word ->
            word.replaceFirstChar { it.uppercase() }
        }