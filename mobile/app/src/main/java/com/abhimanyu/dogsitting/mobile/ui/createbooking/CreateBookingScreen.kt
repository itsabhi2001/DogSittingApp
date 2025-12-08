package com.abhimanyu.dogsitting.mobile.ui.createbooking

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBookingScreen(
    uiState: CreateBookingUiState,
    onClientNameChange: (String) -> Unit,
    onClientEmailChange: (String) -> Unit,
    onPetNameChange: (String) -> Unit,
    onPetSizeChange: (String) -> Unit,
    onServiceTypeChange: (String) -> Unit,
    onStartDateChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onEstimateClick: () -> Unit,
    onSubmitClick: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Booking") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (uiState.successMessage != null) {
                Text(
                    text = uiState.successMessage,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            OutlinedTextField(
                value = uiState.clientName,
                onValueChange = onClientNameChange,
                label = { Text("Client name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.clientEmail,
                onValueChange = onClientEmailChange,
                label = { Text("Client email") },
                modifier = Modifier.fillMaxWidth()
            )


            OutlinedTextField(
                value = uiState.petName,
                onValueChange = onPetNameChange,
                label = { Text("Pet name") },
                modifier = Modifier.fillMaxWidth()
            )

            // For now simple text fields for enums; later we can convert to dropdowns.
            OutlinedTextField(
                value = uiState.petSize,
                onValueChange = onPetSizeChange,
                label = { Text("Pet size (SMALL / MEDIUM / LARGE)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.serviceType,
                onValueChange = onServiceTypeChange,
                label = { Text("Service (HOUSE_SITTING / WALK / DROP_IN)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.startDate,
                onValueChange = onStartDateChange,
                label = { Text("Start date (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.endDate,
                onValueChange = onEndDateChange,
                label = { Text("End date (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.notes,
                onValueChange = onNotesChange,
                label = { Text("Notes (optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            if (uiState.estimatedPrice != null) {
                Text(
                    text = "Estimated price: $${uiState.estimatedPrice}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onEstimateClick,
                    enabled = !uiState.isEstimating && !uiState.isSubmitting,
                    modifier = Modifier.weight(1f)
                ) {
                    if (uiState.isEstimating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Estimating...")
                    } else {
                        Text("Estimate")
                    }
                }

                Button(
                    onClick = onSubmitClick,
                    enabled = !uiState.isSubmitting,
                    modifier = Modifier.weight(1f)
                ) {
                    if (uiState.isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Saving...")
                    } else {
                        Text("Create")
                    }
                }
            }
        }
    }
}
