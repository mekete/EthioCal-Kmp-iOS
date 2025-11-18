package com.shalom.calendar.ui.event

import android.Manifest
import android.os.Build
import androidx.activity.ComponentActivity
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.shalom.android.material.datepicker.EthiopicDatePickerDialog
import com.shalom.calendar.R
import com.shalom.calendar.data.local.entity.EventInstance
import com.shalom.calendar.data.permissions.PermissionManager
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.domain.model.EventCategory
import com.shalom.calendar.ui.permissions.ExactAlarmPermissionRationaleDialog
import com.shalom.calendar.ui.permissions.NotificationPermissionRationaleDialog
import com.shalom.calendar.ui.permissions.PermissionDeniedBanner
import com.shalom.calendar.ui.permissions.PermissionFixDialog
import kotlinx.coroutines.launch
import org.threeten.extra.chrono.EthiopicDate
import java.time.Duration
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.TemporalAdjusters
import kotlin.math.abs

/**
 * Calculate relative time difference from now to the given time.
 * Returns formatted string like "In 3 hours", "2 days ago", etc.
 */
@Composable
private fun getRelativeTimeText(targetTime: ZonedDateTime): String {
    val now = ZonedDateTime.now()
    val duration = Duration.between(now, targetTime)
    val seconds = duration.seconds
    val absSeconds = abs(seconds)

    val isPast = seconds < 0

    return when {
        absSeconds < 60 -> { // Less than 1 minute
            if (isPast) {
                stringResource(R.string.time_just_now)
            } else {
                stringResource(R.string.time_in_a_moment)
            }
        }

        absSeconds < 3600 -> { // Less than 1 hour - show minutes
            val minutes = absSeconds / 60
            val unit = if (minutes == 1L) {
                stringResource(R.string.time_minute_singular)
            } else {
                stringResource(R.string.time_minute_plural)
            }
            if (isPast) {
                stringResource(R.string.time_ago, minutes, unit)
            } else {
                stringResource(R.string.time_in_future, minutes, unit)
            }
        }

        absSeconds < 86400 -> { // Less than 1 day - show hours
            val hours = absSeconds / 3600
            val unit = if (hours == 1L) {
                stringResource(R.string.time_hour_singular)
            } else {
                stringResource(R.string.time_hour_plural)
            }
            if (isPast) {
                stringResource(R.string.time_ago, hours, unit)
            } else {
                stringResource(R.string.time_in_future, hours, unit)
            }
        }

        absSeconds < 604800 -> { // Less than 1 week - show days
            val days = absSeconds / 86400
            val unit = if (days == 1L) {
                stringResource(R.string.time_day_singular)
            } else {
                stringResource(R.string.time_day_plural)
            }
            if (isPast) {
                stringResource(R.string.time_ago, days, unit)
            } else {
                stringResource(R.string.time_in_future, days, unit)
            }
        }

        absSeconds < 2592000 -> { // Less than 30 days - show weeks
            val weeks = absSeconds / 604800
            val unit = if (weeks == 1L) {
                stringResource(R.string.time_week_singular)
            } else {
                stringResource(R.string.time_week_plural)
            }
            if (isPast) {
                stringResource(R.string.time_ago, weeks, unit)
            } else {
                stringResource(R.string.time_in_future, weeks, unit)
            }
        }

        absSeconds < 31536000 -> { // Less than 1 year - show months
            val months = absSeconds / 2592000
            val unit = if (months == 1L) {
                stringResource(R.string.time_month_singular)
            } else {
                stringResource(R.string.time_month_plural)
            }
            if (isPast) {
                stringResource(R.string.time_ago, months, unit)
            } else {
                stringResource(R.string.time_in_future, months, unit)
            }
        }

        else -> { // 1 year or more
            val years = absSeconds / 31536000
            val unit = if (years == 1L) {
                stringResource(R.string.time_year_singular)
            } else {
                stringResource(R.string.time_year_plural)
            }
            if (isPast) {
                stringResource(R.string.time_ago, years, unit)
            } else {
                stringResource(R.string.time_in_future, years, unit)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun EventScreen(
    event: String, permissionManager: PermissionManager, selectedDate: String? = null, hasEvents: Boolean = false, viewModel: EventViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val permissionState by permissionManager.permissionState.collectAsState()

    // Track if we've already processed the selectedDate to prevent re-triggering
    val hasProcessedSelectedDate = remember(selectedDate) { mutableStateOf(false) }

    // Handle selected date from navigation with smart logic
    // Wait for uiState to be Success before processing
    // Only process ONCE per navigation (tracked by hasProcessedSelectedDate)
    LaunchedEffect(selectedDate, hasEvents, uiState) {
        if (selectedDate != null && !hasProcessedSelectedDate.value && uiState is EventUiState.Success) {
            try { // Parse date string "YYYY-M-D"
                val parts = selectedDate.split("-")
                if (parts.size == 3) {
                    val year = parts[0].toInt()
                    val month = parts[1].toInt()
                    val day = parts[2].toInt()
                    val ethiopicDate = EthiopicDate.of(year, month, day)

                    // Check if the selected date is in the current month
                    val today = EthiopicDate.now()
                    val isCurrentMonth = (year == today.get(ChronoField.YEAR_OF_ERA) && month == today.get(ChronoField.MONTH_OF_YEAR))

                    if (hasEvents) { // Date has events: just set filter to show them
                        if (!isCurrentMonth) { // Set filter to the selected date's month
                            viewModel.setFilterToMonth(ethiopicDate)
                        } // Don't open dialog, just show the events
                    } else { // Date has no events: set filter and open dialog
                        if (!isCurrentMonth) { // Set filter AND open dialog atomically
                            viewModel.setFilterAndOpenDialog(ethiopicDate)
                        } else { // Current month: just open dialog with preset date
                            viewModel.showAddEventDialogWithDate(ethiopicDate)
                        }
                    }

                    // Mark as processed to prevent re-triggering on state changes
                    hasProcessedSelectedDate.value = true
                }
            } catch (e: Exception) { // Invalid date format, ignore
            }
        }
    }

    // Settings preferences for persistent flag tracking
    val settingsPreferences = remember { SettingsPreferences(context) }
    val coroutineScope = rememberCoroutineScope()

    // Track if preferences have loaded from DataStore
    var preferencesLoaded by remember { mutableStateOf(false) }
    var dontAskUntil by remember { mutableStateOf(0L) }

    // Collect preferences from DataStore and mark as loaded when first real emission arrives
    LaunchedEffect(Unit) { // Collect dontAskUntil preference
        launch {
            settingsPreferences.notificationPermissionDontAskUntil.collect { value ->
                dontAskUntil = value
            }
        } // Wait a bit for first emission to arrive, then mark as loaded
        kotlinx.coroutines.delay(50)
        preferencesLoaded = true
    }

    // Check if we're within the "don't ask" period
    val isWithinDontAskPeriod = dontAskUntil > System.currentTimeMillis()

    // Track if dialog has been dismissed in this session (to prevent showing again in same session)
    var dialogDismissedThisSession by remember { mutableStateOf(false) }

    // Dialog states
    var showPermissionFixDialog by remember { mutableStateOf(false) }
    var showExactAlarmRationale by remember { mutableStateOf(false) }
    var showNotificationRationale by remember { mutableStateOf(false) }

    // Track if user dismissed the rationale dialog (for showing banner)
    var userDismissedRationale by remember { mutableStateOf(false) }

    // Track if we've attempted to request permission (to detect permanent denial)
    var hasAttemptedPermissionRequest by remember { mutableStateOf(false) }

    // State to trigger permission request with delay
    var shouldLaunchPermissionRequest by remember { mutableStateOf(false) }

    // POST_NOTIFICATIONS permission state
    val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS) { granted ->
            if (granted) {
                permissionManager.refreshPermissionState() // Clear the dismissed flag since permission is now granted
                userDismissedRationale = false
            } else { // User denied permission, show banner
                userDismissedRationale = true // Mark that we've attempted (to detect permanent denial)
                hasAttemptedPermissionRequest = true
            }
        }
    } else {
        null
    }

    // Show permission dialog on first EventScreen visit - wait for preferences to load
    LaunchedEffect(preferencesLoaded, isWithinDontAskPeriod) { // Wait for preferences to load before making decision
        if (!preferencesLoaded) {
            return@LaunchedEffect
        }

        if (!isWithinDontAskPeriod && !dialogDismissedThisSession && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionState?.let { permState ->
                if (!permState.status.isGranted) { // Show rationale dialog
                    showNotificationRationale = true
                }
            }
        }
    }

    // Launch permission request with delay to avoid animation conflicts
    LaunchedEffect(shouldLaunchPermissionRequest) {
        if (shouldLaunchPermissionRequest) { // Small delay to ensure dialog animation is complete
            kotlinx.coroutines.delay(300)
            notificationPermissionState?.let { permState ->
                permState.launchPermissionRequest()
            }
            shouldLaunchPermissionRequest = false
        }
    }

    // Notification permission rationale dialog
    if (showNotificationRationale) {
        NotificationPermissionRationaleDialog(onGrantClick = {
            showNotificationRationale = false
            dialogDismissedThisSession = true

            // Check if permission might be permanently denied before attempting request
            notificationPermissionState?.let { permState ->
                val shouldShowRationale = when (val status = permState.status) {
                    is com.google.accompanist.permissions.PermissionStatus.Denied -> status.shouldShowRationale
                    else -> false
                }

                // If shouldShowRationale is true, we can request permission
                // If false and we haven't attempted yet, try once (might be first request)
                // If false and we have attempted, go to settings (permanently denied)
                if (!shouldShowRationale && hasAttemptedPermissionRequest) {
                    (context as? ComponentActivity)?.let {
                        permissionManager.openAppSettings(it)
                    }
                    userDismissedRationale = true
                } else { // Try permission request
                    shouldLaunchPermissionRequest = true
                }
            }
        }, onDismiss = { dontAskForOneWeek ->
            showNotificationRationale = false
            dialogDismissedThisSession = true

            // User clicked "Not Now"
            coroutineScope.launch {
                if (dontAskForOneWeek) { // Checkbox is checked: Set timestamp to one week from now
                    val oneWeekFromNow = System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000) // 7 days
                    settingsPreferences.setNotificationPermissionDontAskUntil(oneWeekFromNow)
                }
            }
            userDismissedRationale = true
        })
    }

    // Show permission fix dialog
    if (showPermissionFixDialog) {
        PermissionFixDialog(needsNotificationPermission = !permissionState.notificationsPermission.isGranted, needsExactAlarmPermission = !permissionState.exactAlarmPermission.isGranted, onRequestNotifications = { // Dialog will be dismissed by the PermissionFixDialog before calling this
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionState?.let { permState ->
                    val shouldShowRationale = when (val status = permState.status) {
                        is com.google.accompanist.permissions.PermissionStatus.Denied -> status.shouldShowRationale
                        else -> false
                    }

                    // If permission is permanently denied (shouldShowRationale=false and we've attempted before),
                    // or if we've already tried and failed, go to settings
                    if (!shouldShowRationale && hasAttemptedPermissionRequest) {
                        (context as? ComponentActivity)?.let {
                            permissionManager.openAppSettings(it)
                        }
                    } else {
                        shouldLaunchPermissionRequest = true
                    }
                }
            } else { // On older Android, open settings directly
                (context as? ComponentActivity)?.let {
                    permissionManager.openNotificationSettings(it)
                }
            }
        }, onRequestExactAlarm = {
            showExactAlarmRationale = true
        }, onOpenSettings = {
            (context as? ComponentActivity)?.let {
                permissionManager.openAppSettings(it)
            }
        }, onDismiss = {
            showPermissionFixDialog = false
        })
    }

    // Show exact alarm rationale before opening settings
    if (showExactAlarmRationale) {
        ExactAlarmPermissionRationaleDialog(onGrantClick = {
            showExactAlarmRationale = false
            (context as? ComponentActivity)?.let {
                permissionManager.requestExactAlarmPermission(it)
            }
        }, onDismiss = {
            showExactAlarmRationale = false
        })
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = stringResource(R.string.screen_title_events), style = MaterialTheme.typography.titleLarge)
        }, actions = { // Show Filter/Show All button based on current filter state
            val state = uiState as? EventUiState.Success
            if (state != null) {
                val isShowingAll = state.filterStartDate?.year == 1900 && state.filterEndDate?.year == 2100

                TextButton(onClick = {
                    if (isShowingAll) {
                        viewModel.setFilterToCurrentMonth()
                    } else {
                        viewModel.clearDateFilters()
                    }
                }) {
                    Text(text = if (isShowingAll) {
                        stringResource(R.string.filter)
                    } else {
                        stringResource(R.string.show_all)
                    }, color = MaterialTheme.colorScheme.primary)
                }
            }
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer))
    }, floatingActionButton = {
        FloatingActionButton(onClick = { viewModel.showAddEventDialog() }, containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary) {
            Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.add_event))
        }
    }) { padding ->
        Column(modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding(), bottom = 0.dp)) { // Show permission warning banner if permissions are missing
            // AND user has dismissed the rationale
            // BUT NOT if we're within the "don't ask" period
            if (preferencesLoaded && permissionState.hasMissingPermissions && userDismissedRationale && !isWithinDontAskPeriod) {
                val missingDescription = permissionState.getMissingPermissionsDescription()
                if (missingDescription != null) {
                    PermissionDeniedBanner(missingPermissions = missingDescription, onActionClick = { // Open notification settings directly, just like MoreScreen
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            (context as? ComponentActivity)?.let { activity ->
                                permissionManager.openNotificationSettings(activity)
                            }
                        } else { // On older Android, just open app settings
                            (context as? ComponentActivity)?.let { activity ->
                                permissionManager.openAppSettings(activity)
                            }
                        }
                    })
                }
            }

            // Main content
            Box(modifier = Modifier.fillMaxSize()) {
                when (val state = uiState) {
                    is EventUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    is EventUiState.Success -> {
                        if (state.events.isEmpty() && state.filterStartDate == null && state.filterEndDate == null) {
                            EmptyEventsPlaceholder()
                        } else {
                            EventList(events = state.events,
                                filterStartDate = state.filterStartDate,
                                filterEndDate = state.filterEndDate,
                                onStartDateSelected = { viewModel.setFilterStartDate(it) },
                                onEndDateSelected = { viewModel.setFilterEndDate(it) },
                                onClearStartDate = { viewModel.clearFilterStartDate() },
                                onClearEndDate = { viewModel.clearFilterEndDate() },
                                onClearFilters = { viewModel.clearDateFilters() },
                                onSetFilterToMonth = { viewModel.setFilterToCurrentMonth() },
                                onEventClick = { /* TODO: Show event details */ },
                                onEditEvent = { viewModel.showEditEventDialog(it) },
                                onDeleteEvent = { viewModel.deleteEvent(it) })
                        }

                        // Show dialog if open
                        if (state.isDialogOpen) {
                            AddEventDialog(onDismiss = { viewModel.hideAddEventDialog() }, editingEvent = state.editingEvent, initialDate = state.presetDate, onCreateEvent = { summary, description, startTime, endTime, isAllDay, recurrenceRule, reminderMinutes, category, ethYear, ethMonth, ethDay ->
                                viewModel.createEvent(summary = summary, description = description, startTime = startTime, endTime = endTime, isAllDay = isAllDay, recurrenceRule = recurrenceRule, reminderMinutesBefore = reminderMinutes, category = category, ethiopianYear = ethYear, ethiopianMonth = ethMonth, ethiopianDay = ethDay)
                            }, onUpdateEvent = { eventId, summary, description, startTime, endTime, isAllDay, recurrenceRule, reminderMinutes, category, ethYear, ethMonth, ethDay ->
                                viewModel.updateEvent(eventId = eventId, summary = summary, description = description, startTime = startTime, endTime = endTime, isAllDay = isAllDay, recurrenceRule = recurrenceRule, reminderMinutesBefore = reminderMinutes, category = category, ethiopianYear = ethYear, ethiopianMonth = ethMonth, ethiopianDay = ethDay)
                            })
                        }
                    }

                    is EventUiState.Error -> {
                        ErrorMessage(message = state.message, modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}

@Composable
private fun EventList(
    events: List<EventInstance>, filterStartDate: LocalDate?, filterEndDate: LocalDate?, onStartDateSelected: (LocalDate?) -> Unit, onEndDateSelected: (LocalDate?) -> Unit, onClearStartDate: () -> Unit, onClearEndDate: () -> Unit, onClearFilters: () -> Unit, onSetFilterToMonth: () -> Unit, onEventClick: (String) -> Unit, onEditEvent: (String) -> Unit, onDeleteEvent: (String) -> Unit
) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { // Date filter section
        item {
            DateFilterSection(startDate = filterStartDate, endDate = filterEndDate, onStartDateClick = { showStartDatePicker = true }, onEndDateClick = { showEndDatePicker = true }, onClearStartDate = onClearStartDate, onClearEndDate = onClearEndDate, onClearFilters = onClearFilters)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(items = events, key = { "${it.eventId}_${it.instanceStart.toInstant().toEpochMilli()}" }) { event ->
            EventCard(event = event, onClick = { onEventClick(event.eventId) }, onEdit = { onEditEvent(event.eventId) }, onDelete = { onDeleteEvent(event.eventId) })
        }
    }

    // Start date picker dialog - Ethiopic
    if (showStartDatePicker) { // Convert LocalDate to EthiopicDate for the picker
        // If date is extreme (1900) or null, default to first day of current month
        val initialEthiopicDate = if (filterStartDate != null && filterStartDate.year != 1900) {
            EthiopicDate.from(filterStartDate)
        } else {
            val today = EthiopicDate.now()
            today.with(TemporalAdjusters.firstDayOfMonth())
        }

        EthiopicDatePickerDialog(selectedDate = initialEthiopicDate, onDateSelected = { selectedEthiopicDate -> // Convert EthiopicDate back to LocalDate for filtering
            val selectedGregorianDate = LocalDate.from(selectedEthiopicDate)
            onStartDateSelected(selectedGregorianDate)
        }, onDismiss = { showStartDatePicker = false })
    }

    // End date picker dialog - Ethiopic
    if (showEndDatePicker) { // Convert LocalDate to EthiopicDate for the picker
        // If date is extreme (2100) or null, default to last day of current month
        val initialEthiopicDate = if (filterEndDate != null && filterEndDate.year != 2100) {
            EthiopicDate.from(filterEndDate)
        } else {
            val today = EthiopicDate.now()
            today.with(TemporalAdjusters.lastDayOfMonth())
        }

        EthiopicDatePickerDialog(selectedDate = initialEthiopicDate, onDateSelected = { selectedEthiopicDate -> // Convert EthiopicDate back to LocalDate for filtering
            val selectedGregorianDate = LocalDate.from(selectedEthiopicDate)
            onEndDateSelected(selectedGregorianDate)
        }, onDismiss = { showEndDatePicker = false })
    }
}

@Composable
private fun DateFilterSection(
    startDate: LocalDate?, endDate: LocalDate?, onStartDateClick: () -> Unit, onEndDateClick: () -> Unit, onClearStartDate: () -> Unit, onClearEndDate: () -> Unit, onClearFilters: () -> Unit
) {
    val monthNames = stringArrayResource(R.array.ethiopian_months)

    // Helper function to format Ethiopian date
    fun formatEthiopianDate(localDate: LocalDate): String {
        val ethDate = EthiopicDate.from(localDate)
        val year = ethDate.get(ChronoField.YEAR)
        val month = ethDate.get(ChronoField.MONTH_OF_YEAR)
        val day = ethDate.get(ChronoField.DAY_OF_MONTH)
        val monthName = if (month in 1..13) monthNames[month - 1] else "Unknown"
        return "$monthName $day, $year"
    }

    // Determine the title text based on filter state
    val titleText = if (startDate != null && endDate != null) { // Check if showing "all" range (1900-2100)
        if (startDate.year == 1900 && endDate.year == 2100) {
            stringResource(R.string.showing_all_events)
        } else {
            stringResource(R.string.showing_from_to, formatEthiopianDate(startDate), formatEthiopianDate(endDate))
        }
    } else if (startDate != null) {
        stringResource(R.string.showing_from, formatEthiopianDate(startDate))
    } else if (endDate != null) {
        stringResource(R.string.showing_until, formatEthiopianDate(endDate))
    } else {
        stringResource(R.string.showing_all_events)
    }

    Column(modifier = Modifier.fillMaxWidth()) { // Display only the status text, no button
        Text(text = titleText, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(8.dp), maxLines = 1)

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) { // Start date picker
            DateInputField(modifier = Modifier.weight(1f), label = "Start Date", date = startDate, onClick = onStartDateClick, onClear = onClearStartDate, monthNames = monthNames)

            // End date picker
            DateInputField(modifier = Modifier.weight(1f), label = "End Date", date = endDate, onClick = onEndDateClick, onClear = onClearEndDate, monthNames = monthNames)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateInputField(
    modifier: Modifier = Modifier, label: String, date: LocalDate?, onClick: () -> Unit, onClear: () -> Unit, monthNames: Array<String>
) { // Check if date is an extreme value (used for "show all")
    val isExtremeDate = date != null && (date.year == 1900 || date.year == 2100)

    // Format date in Ethiopian calendar
    val displayText = if (date != null && !isExtremeDate) {
        val ethDate = EthiopicDate.from(date)
        val year = ethDate.get(ChronoField.YEAR)
        val month = ethDate.get(ChronoField.MONTH_OF_YEAR)
        val day = ethDate.get(ChronoField.DAY_OF_MONTH)
        val monthName = if (month in 1..13) monthNames[month - 1] else "Unknown"
        "$monthName $day, $year"
    } else {
        "—"
    }

    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), onClick = onClick, modifier = modifier) {
        Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = label, style = MaterialTheme.typography.labelSmall)
                Text(text = displayText, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            } // Show clear button when date is set and not an extreme value
            if (date != null && !isExtremeDate) {
                IconButton(onClick = onClear, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Clear date", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventCard(
    event: EventInstance, onClick: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(onClick = onEdit, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 0.dp), verticalAlignment = Alignment.CenterVertically) { // Date number on the left (like holiday cards)
            Text(text = String.format(java.util.Locale.US, "%02d", event.ethiopianDay), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Thin)

            Spacer(modifier = Modifier.width(12.dp))

            // Event color indicator
            Box(modifier = Modifier
                    .size(4.dp, 50.dp)
                    .background(color = try {
                        EventCategory.valueOf(event.category).getColor()
                    } catch (e: IllegalArgumentException) {
                        EventCategory.PERSONAL.getColor()
                    }, shape = RoundedCornerShape(2.dp)))

            Spacer(modifier = Modifier.width(12.dp))

            // Event details - 3 rows
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {


                    event.reminderMinutesBefore?.let { minutes ->
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.width(4.dp))

                    }

                    Text(text = event.summary, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1)
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Row 2: Ethiopian date (left) and Gregorian date (right)
                val monthNames = stringArrayResource(R.array.ethiopian_months)
                Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) { // Ethiopian date on the left
                    Text(text = formatEthiopicDate(event, monthNames), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)

                    // Gregorian date on the right
                    Text(text = formatGregorianDate(event), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.StartEllipsis)
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Row 3: Time range, then icons with info
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) { // Time range at the start
                    val timeText = if (event.isAllDay) {
                        stringResource(R.string.all_day_event)
                    } else {
                        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                        val startTime = event.instanceStart.format(timeFormatter)
                        val endTime = event.instanceEnd?.format(timeFormatter) ?: ""
                        if (endTime.isNotEmpty()) {
                            "$startTime-$endTime"
                        } else {
                            startTime
                        }
                    }
                    Text(text = timeText, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Medium)

                    // Relative time with icon
                    val relativeTimeText = getRelativeTimeText(event.instanceStart)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.tertiary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = relativeTimeText, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }

                    // Recurring indicator - icon only
                    if (event.isRecurring) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = stringResource(R.string.recurring_event), modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(R.string.delete_event), tint = MaterialTheme.colorScheme.error)
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(onDismissRequest = { showDeleteDialog = false }, title = { Text(stringResource(R.string.delete_event_title)) }, text = { Text(stringResource(R.string.delete_event_message, event.summary)) }, confirmButton = {
            TextButton(onClick = {
                onDelete()
                showDeleteDialog = false
            }) {
                Text(stringResource(R.string.delete))
            }
        }, dismissButton = {
            TextButton(onClick = { showDeleteDialog = false }) {
                Text(stringResource(R.string.cancel))
            }
        })
    }
}

/**
 * Format Ethiopian date only
 */
private fun formatEthiopicDate(event: EventInstance, monthNames: Array<String>): String {
    val year = event.ethiopianYear
    val month = event.ethiopianMonth
    val day = event.ethiopianDay

    val monthName = if (month in 1..13) monthNames[month - 1] else "Unknown"

    return "$monthName $day, $year"
}

/**
 * Format Gregorian date from Ethiopian date fields
 */
private fun formatGregorianDate(event: EventInstance): String { // Create EthiopicDate from event's Ethiopian date fields
    val ethiopicDate = EthiopicDate.of(event.ethiopianYear, event.ethiopianMonth, event.ethiopianDay)
    val gregorianDate = LocalDate.from(ethiopicDate)
    return "${gregorianDate.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${gregorianDate.dayOfMonth}, ${gregorianDate.year}"
}

@Composable
private fun EmptyEventsPlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(R.string.no_events), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(R.string.tap_plus_to_add_event), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f), textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun ErrorMessage(
    message: String, modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = Icons.Default.Info, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(R.string.error_loading_events), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = message, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
    }
}
