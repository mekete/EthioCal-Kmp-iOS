package com.shalom.calendar.ui.event

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.shalom.calendar.data.local.entity.EventInstance
import com.shalom.calendar.presentation.event.EventUiState
import com.shalom.calendar.presentation.event.EventViewModel
import ethiopiancalendar.composeapp.generated.resources.Res
import ethiopiancalendar.composeapp.generated.resources.button_cancel
import ethiopiancalendar.composeapp.generated.resources.button_delete
import ethiopiancalendar.composeapp.generated.resources.cd_add_event
import ethiopiancalendar.composeapp.generated.resources.cd_delete_event
import ethiopiancalendar.composeapp.generated.resources.delete_event_confirm_message
import ethiopiancalendar.composeapp.generated.resources.delete_event_confirm_title
import ethiopiancalendar.composeapp.generated.resources.empty_no_events
import ethiopiancalendar.composeapp.generated.resources.ethiopian_months
import ethiopiancalendar.composeapp.generated.resources.screen_title_events
import ethiopiancalendar.composeapp.generated.resources.weekday_names_short
import com.shalom.calendar.ui.components.EthiopicDatePickerDialog
import com.shalom.ethiopicchrono.ChronoField
import com.shalom.ethiopicchrono.EthiopicDate
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration.Companion.hours

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(
    selectedDate: String? = null,
    hasEvents: Boolean = false,
    viewModel: EventViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val monthNames = stringArrayResource(Res.array.ethiopian_months)
    val weekdayNamesShort = stringArrayResource(Res.array.weekday_names_short)

    var showAddEventDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf<EventInstance?>(null) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    // Parse selected date if provided
    LaunchedEffect(selectedDate) {
        selectedDate?.let { dateStr ->
            try {
                val parts = dateStr.split("-")
                if (parts.size == 3) {
                    val year = parts[0].toInt()
                    val month = parts[1].toInt()
                    val day = parts[2].toInt()
                    val ethiopicDate = EthiopicDate.of(year, month, day)
                    val localDate = ethiopicDate.toLocalDate()
                    viewModel.setFilterStartDate(localDate)
                    viewModel.setFilterEndDate(localDate)
                }
            } catch (e: Exception) {
                // Ignore parsing errors
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.screen_title_events),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = { showStartDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Filter dates"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddEventDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(Res.string.cd_add_event)
                )
            }
        }
    ) { padding ->
        when (val state = uiState) {
            is EventUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is EventUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }

            is EventUiState.Success -> {
                if (state.events.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = stringResource(Res.string.empty_no_events),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.events) { event ->
                            EventCard(
                                event = event,
                                monthNames = monthNames,
                                onDelete = { showDeleteConfirmDialog = event }
                            )
                        }
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    showDeleteConfirmDialog?.let { event ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = null },
            title = { Text(stringResource(Res.string.delete_event_confirm_title)) },
            text = { Text(stringResource(Res.string.delete_event_confirm_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteEvent(event.eventId)
                        showDeleteConfirmDialog = null
                    }
                ) {
                    Text(stringResource(Res.string.button_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = null }) {
                    Text(stringResource(Res.string.button_cancel))
                }
            }
        )
    }

    // Date filter pickers
    if (showStartDatePicker) {
        val filterStart = (uiState as? EventUiState.Success)?.filterStartDate
        val selectedEthiopicDate = filterStart?.let { EthiopicDate.from(it) } ?: EthiopicDate.now()
        EthiopicDatePickerDialog(
            selectedDate = selectedEthiopicDate,
            onDateSelected = { date ->
                viewModel.setFilterStartDate(date.toLocalDate())
            },
            onDismiss = { showStartDatePicker = false }
        )
    }

    if (showEndDatePicker) {
        val filterEnd = (uiState as? EventUiState.Success)?.filterEndDate
        val selectedEthiopicDate = filterEnd?.let { EthiopicDate.from(it) } ?: EthiopicDate.now()
        EthiopicDatePickerDialog(
            selectedDate = selectedEthiopicDate,
            onDateSelected = { date ->
                viewModel.setFilterEndDate(date.toLocalDate())
            },
            onDismiss = { showEndDatePicker = false }
        )
    }

    // Add event dialog - simplified
    if (showAddEventDialog) {
        AddEventDialog(
            onDismiss = { showAddEventDialog = false },
            onSave = { title, description, date, time ->
                val year = date.get(ChronoField.YEAR_OF_ERA)
                val month = date.get(ChronoField.MONTH_OF_YEAR)
                val day = date.get(ChronoField.DAY_OF_MONTH)
                viewModel.createEvent(
                    summary = title,
                    description = description,
                    startTime = time,
                    ethiopianYear = year,
                    ethiopianMonth = month,
                    ethiopianDay = day
                )
                showAddEventDialog = false
            }
        )
    }
}

@Composable
fun EventCard(
    event: EventInstance,
    monthNames: List<String>,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.summary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Format Ethiopian date
                val monthName = monthNames.getOrElse(event.ethiopianMonth - 1) { "" }
                Text(
                    text = "$monthName ${event.ethiopianDay}, ${event.ethiopianYear}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Show event time
                val localDateTime = event.instanceStart.toLocalDateTime(TimeZone.currentSystemDefault())
                val hour = if (localDateTime.hour == 0) 12 else if (localDateTime.hour > 12) localDateTime.hour - 12 else localDateTime.hour
                val amPm = if (localDateTime.hour < 12) "AM" else "PM"
                val timeStr = "$hour:${localDateTime.minute.toString().padStart(2, '0')} $amPm"
                Text(
                    text = timeStr,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(Res.string.cd_delete_event),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun AddEventDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, EthiopicDate, Instant) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(EthiopicDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Event") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                androidx.compose.material3.OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                androidx.compose.material3.OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextButton(onClick = { showDatePicker = true }) {
                    val month = selectedDate.get(ChronoField.MONTH_OF_YEAR)
                    val day = selectedDate.get(ChronoField.DAY_OF_MONTH)
                    val year = selectedDate.get(ChronoField.YEAR_OF_ERA)
                    Text("Date: $month/$day/$year")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(
                            title,
                            description,
                            selectedDate,
                            Clock.System.now() + 1.hours
                        )
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    if (showDatePicker) {
        EthiopicDatePickerDialog(
            selectedDate = selectedDate,
            onDateSelected = { date ->
                selectedDate = date
            },
            onDismiss = { showDatePicker = false }
        )
    }
}
