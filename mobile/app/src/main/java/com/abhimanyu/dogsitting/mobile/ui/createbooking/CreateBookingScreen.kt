package com.abhimanyu.dogsitting.mobile.ui.createbooking

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
//import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Icon
import com.abhimanyu.dogsitting.mobile.R
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.LaunchedEffect

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
    // --- Date range picker state ---
    var showDateRangePicker by remember { mutableStateOf(false) }
    val dateRangePickerState = rememberDateRangePickerState()
    val dateFormatter = remember { DateTimeFormatter.ISO_LOCAL_DATE }

    LaunchedEffect(dateRangePickerState.selectedStartDateMillis) {
        val startMillis = dateRangePickerState.selectedStartDateMillis
        if (startMillis != null) {
            val startDate = Instant.ofEpochMilli(startMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            val formatted = startDate.format(dateFormatter)

            if (uiState.startDate != formatted) {
                onStartDateChange(formatted)
            }
        }
    }

    LaunchedEffect(dateRangePickerState.selectedEndDateMillis) {
        val endMillis = dateRangePickerState.selectedEndDateMillis
        if (endMillis != null) {
            val endDate = Instant.ofEpochMilli(endMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            val formatted = endDate.format(dateFormatter)

            if (uiState.endDate != formatted) {
                onEndDateChange(formatted)
            }
        }
    }

    if (showDateRangePicker) {
        DatePickerDialog(
            onDismissRequest = { showDateRangePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDateRangePicker = false
                        }

                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDateRangePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                title = { Text("Select stay dates") }
            )
        }
    }

    // --- Pet size slider state ---
    val petSizeOptions = listOf("SMALL", "MEDIUM", "LARGE")
    val currentPetSizeIndex = petSizeOptions.indexOf(uiState.petSize).let {
        if (it == -1) 1 else it // default MEDIUM
    }
    var petSizeSliderPosition by remember(uiState.petSize) {
        mutableStateOf(currentPetSizeIndex.toFloat())
    }

    // --- Service type options ---
    val serviceOptions = listOf(
        "HOUSE_SITTING" to "House Sitting",
        "WALK" to "Walk",
        "DROP_IN" to "Drop In"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_small_dog),
                            contentDescription = "Dog icon",
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 8.dp),
                            tint = Color.Unspecified
                        )
                        Text(
                            text = "Create Booking",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
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
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),   // 👈 make the whole column scrollable
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // --- Basic info fields ---
            OutlinedTextField(
                value = uiState.clientName,
                onValueChange = onClientNameChange,
                label = { Text("Client Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.clientEmail,
                onValueChange = onClientEmailChange,
                label = { Text("Client Email") },
                modifier = Modifier.fillMaxWidth(),
                /*
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )

                 */
            )

            OutlinedTextField(
                value = uiState.petName,
                onValueChange = onPetNameChange,
                label = { Text("Pet Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // --- Pet Size slider ---
            Text(
                text = "Pet Size",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_small_dog),
                    contentDescription = "Small dog",
                    modifier = Modifier.size(28.dp),
                    tint = androidx.compose.ui.graphics.Color.Unspecified
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(28.dp)         // make the slider area less tall
                        .padding(horizontal = 8.dp)
                ) {
                    Slider(
                        value = petSizeSliderPosition,
                        onValueChange = { newValue ->
                            petSizeSliderPosition = newValue
                            val index = newValue.roundToInt().coerceIn(0, petSizeOptions.lastIndex)
                            val selected = petSizeOptions[index]
                            onPetSizeChange(selected)
                        },
                        valueRange = 0f..2f,
                        steps = 1,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.ic_large_dog),
                    contentDescription = "Large dog",
                    modifier = Modifier.size(28.dp),
                    tint = androidx.compose.ui.graphics.Color.Unspecified
                )
            }
            Text(
                text = when (uiState.petSize) {
                    "SMALL" -> "Small"
                    "LARGE" -> "Large"
                    else -> "Medium"
                },
                style = MaterialTheme.typography.bodyMedium
            )

            // --- Service type segmented buttons ---
            Text(
                text = "Service Type",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 12.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                serviceOptions.forEach { (value, label) ->
                    val selected = uiState.serviceType == value
                    FilterChip(
                        selected = selected,
                        onClick = { onServiceTypeChange(value) },
                        label = { Text(label) }
                    )
                }
            }

            // --- Date range picker (start + end) ---
            Text(
                text = "Stay Dates",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 12.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = uiState.startDate,
                    onValueChange = {},
                    label = { Text("Start Date") },
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showDateRangePicker = true },
                    enabled = false,
                    readOnly = true
                )

                OutlinedTextField(
                    value = uiState.endDate,
                    onValueChange = {},
                    label = { Text("End Date") },
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showDateRangePicker = true },
                    enabled = false,
                    readOnly = true
                )
            }

            // --- Notes ---
            OutlinedTextField(
                value = uiState.notes,
                onValueChange = onNotesChange,
                label = { Text("Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp),
                maxLines = 4
            )

            // --- Estimated price ---
            if (uiState.estimatedPrice != null) {
                Text(
                    text = "Estimated Price: $${uiState.estimatedPrice}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // --- Error / success messages ---
            uiState.errorMessage?.let { msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.error
                )
            }

            uiState.successMessage?.let { msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- Actions ---
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onEstimateClick,
                    enabled = !uiState.isEstimating && !uiState.isSubmitting,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        if (uiState.isEstimating) "Estimating..." else "Estimate Price"
                    )
                }

                Button(
                    onClick = onSubmitClick,
                    enabled = !uiState.isSubmitting,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        if (uiState.isSubmitting) "Submitting..." else "Create Booking"
                    )
                }
            }
        }
    }
}
