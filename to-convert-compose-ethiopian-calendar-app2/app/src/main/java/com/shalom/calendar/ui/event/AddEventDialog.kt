package com.shalom.calendar.ui.event

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreTime
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.shalom.android.material.datepicker.EthiopicDatePickerDialog
import com.shalom.calendar.R
import com.shalom.calendar.data.local.entity.EventEntity
import com.shalom.calendar.data.local.entity.RecurrenceEndOption
import com.shalom.calendar.data.local.entity.RecurrenceFrequency
import com.shalom.calendar.data.local.entity.RecurrenceRule
import com.shalom.calendar.data.local.entity.parseRRule
import com.shalom.calendar.domain.model.EventCategory
import org.threeten.extra.chrono.EthiopicDate
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventDialog(
    onDismiss: () -> Unit, editingEvent: EventEntity? = null, initialDate: EthiopicDate? = null, onCreateEvent: (
        summary: String, description: String?, startTime: ZonedDateTime, endTime: ZonedDateTime?, isAllDay: Boolean, recurrenceRule: RecurrenceRule?, reminderMinutesBefore: Int?, category: String, ethiopianYear: Int, ethiopianMonth: Int, ethiopianDay: Int
    ) -> Unit, onUpdateEvent: (
        eventId: String, summary: String, description: String?, startTime: ZonedDateTime, endTime: ZonedDateTime?, isAllDay: Boolean, recurrenceRule: RecurrenceRule?, reminderMinutesBefore: Int?, category: String, ethiopianYear: Int, ethiopianMonth: Int, ethiopianDay: Int
    ) -> Unit
) {
    val isEditMode = editingEvent != null

    // Parse recurrence rule if editing
    val parsedRecurrenceRule = editingEvent?.recurrenceRule?.parseRRule()

    // Determine initial date: use initialDate if provided, else use editing event date, else today
    val defaultDate = when {
        initialDate != null -> LocalDate.from(initialDate)
        editingEvent != null -> editingEvent.startTime.toLocalDate()
        else -> LocalDate.now()
    }

    // Form state - pre-filled with editingEvent data if in edit mode or initialDate
    var title by remember { mutableStateOf(editingEvent?.summary ?: "") }
    var description by remember { mutableStateOf(editingEvent?.description ?: "") }
    var selectedGregorianDate by remember { mutableStateOf(defaultDate) }
    var startTime by remember {
        mutableStateOf(editingEvent?.startTime?.toLocalTime() ?: LocalTime.now())
    }
    var endTime by remember {
        mutableStateOf(editingEvent?.endTime?.toLocalTime() ?: LocalTime.now().plusHours(1))
    }
    var isAllDay by remember { mutableStateOf(editingEvent?.isAllDay ?: false) }
    var showGregorianDatePicker by remember { mutableStateOf(false) }
    var showEthiopianDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    // Ethiopian date derived from selectedDate
    // Note: This represents the SAME date as selectedDate, just in the Ethiopian calendar
    // The two dates are kept synchronized - changing either one updates the other
    var ethiopianDate by remember {
        mutableStateOf(EthiopicDate.from(selectedGregorianDate))
    }

    // Recurrence state - pre-filled if editing
    var recurrenceFrequency by remember {
        mutableStateOf(parsedRecurrenceRule?.frequency ?: RecurrenceFrequency.NONE)
    }
    var selectedWeekDays by remember {
        mutableStateOf(parsedRecurrenceRule?.weekDays ?: setOf())
    }
    var recurrenceEndOption by remember {
        mutableStateOf(parsedRecurrenceRule?.endOption ?: RecurrenceEndOption.NEVER)
    }
    var recurrenceEndDate by remember {
        mutableStateOf(parsedRecurrenceRule?.endDate?.let {
            java.time.Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
        })
    }
    var showRecurrenceEndDatePicker by remember { mutableStateOf(false) }

    // Reminder state - pre-filled if editing
    // For new events, reminder is enabled by default with 5 minutes
    var reminderEnabled by remember {
        mutableStateOf(editingEvent?.let { it.reminderMinutesBefore != null } ?: true)
    }
    var reminderMinutes by remember { mutableStateOf(editingEvent?.reminderMinutesBefore ?: 5) }
    var showReminderTimeDialog by remember { mutableStateOf(false) }

    // Category state - pre-filled if editing
    var category by remember { mutableStateOf(editingEvent?.category ?: "PERSONAL") }

    // Error state
    var titleError by remember { mutableStateOf(false) }

    // Time formatter for AM/PM display
    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()) { // Header
            Text(text = if (isEditMode) stringResource(R.string.edit_event) else stringResource(R.string.add_event), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(20.dp))

            // Title input
            OutlinedTextField(value = title, onValueChange = {
                title = it
                titleError = false
            }, label = { Text(stringResource(R.string.title)) }, modifier = Modifier.fillMaxWidth(), singleLine = true, isError = titleError, supportingText = if (titleError) {
                { Text(stringResource(R.string.title_required)) }
            } else null)

            Spacer(modifier = Modifier.height(12.dp))

            // Description input
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text(stringResource(R.string.description_optional)) }, modifier = Modifier.fillMaxWidth(), minLines = 2, maxLines = 3)

            Spacer(modifier = Modifier.height(16.dp))

            // Dual Date Picker (Gregorian and Ethiopian) - Clock-like design
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) { // Ethiopian Date Picker
                Card(onClick = { showEthiopianDatePicker = true }, modifier = Modifier.weight(1f)) {
                    Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = stringResource(R.string.pick_ethiopian), style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(text = ethiopianDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US)), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.StartEllipsis)
                        }
                    }
                } // Gregorian Date Picker
                Card(onClick = { showGregorianDatePicker = true }, modifier = Modifier.weight(1f)) {
                    Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = stringResource(R.string.pick_gregorian), style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(text = selectedGregorianDate.toString(), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.StartEllipsis)
                        }
                    }
                }


            }

            Spacer(modifier = Modifier.height(12.dp))

            // Time pickers (only if not all-day)
            if (!isAllDay) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) { // Start time
                    Card(onClick = { showStartTimePicker = true }, modifier = Modifier.weight(1f)) {
                        Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.AccessTime, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = stringResource(R.string.start_time), style = MaterialTheme.typography.labelSmall)
                                Text(text = startTime.format(timeFormatter), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    // End time
                    Card(onClick = { showEndTimePicker = true }, modifier = Modifier.weight(1f)) {
                        Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.AccessTime, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = stringResource(R.string.end_time), style = MaterialTheme.typography.labelSmall)
                                Text(text = endTime?.format(timeFormatter) ?: "—", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            // All-day toggle
            Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isAllDay = !isAllDay }
                    .padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.all_day_event), style = MaterialTheme.typography.bodyLarge)
                Switch(checked = isAllDay, onCheckedChange = { isAllDay = it })
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Recurrence section
            RecurrenceSection(recurrenceFrequency = recurrenceFrequency, onRecurrenceFrequencyChange = { newFrequency ->
                recurrenceFrequency = newFrequency // When enabling weekly repeat, set default end date to 1 month from start
                if (newFrequency == RecurrenceFrequency.WEEKLY) {
                    recurrenceEndOption = RecurrenceEndOption.UNTIL
                    if (recurrenceEndDate == null) {
                        recurrenceEndDate = selectedGregorianDate.plusMonths(1)
                    }
                }
            }, selectedWeekDays = selectedWeekDays, onWeekDaysChange = { selectedWeekDays = it }, recurrenceEndDate = recurrenceEndDate, onRecurrenceEndDateClick = { showRecurrenceEndDatePicker = true })

            Spacer(modifier = Modifier.height(16.dp))

            // Reminder section
            Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = if (reminderEnabled) {
                        val timeText = formatReminderTime(reminderMinutes)
                        stringResource(R.string.show_reminder_before, timeText)
                    } else {
                        stringResource(R.string.add_reminder)
                    }, style = MaterialTheme.typography.bodyLarge)
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) { // Settings icon (visible only when reminder is enabled)
                    if (reminderEnabled) {
                        IconButton(onClick = { showReminderTimeDialog = true }, modifier = Modifier.size(40.dp)) {
                            Icon(imageVector = Icons.Default.MoreTime, contentDescription = "Configure reminder time", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                        }
                    }

                    Switch(checked = reminderEnabled, onCheckedChange = { reminderEnabled = it })
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category selection
            CategorySelector(selectedCategory = category, onCategoryChange = { category = it })

            Spacer(modifier = Modifier.height(24.dp))

            // Action buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel))
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = {
                    if (title.isBlank()) {
                        titleError = true
                        return@Button
                    }

                    // Build ZonedDateTime
                    val zoneId = ZoneId.systemDefault()
                    val startDateTime = if (isAllDay) {
                        ZonedDateTime.of(selectedGregorianDate, LocalTime.MIDNIGHT, zoneId)
                    } else {
                        ZonedDateTime.of(selectedGregorianDate, startTime, zoneId)
                    }

                    val endDateTime = if (isAllDay || endTime == null) {
                        null
                    } else {
                        ZonedDateTime.of(selectedGregorianDate, endTime, zoneId)
                    }

                    // Build recurrence rule
                    val recurrenceRule = if (recurrenceFrequency != RecurrenceFrequency.NONE) {
                        RecurrenceRule(frequency = recurrenceFrequency, weekDays = if (recurrenceFrequency == RecurrenceFrequency.WEEKLY) selectedWeekDays else emptySet(), endOption = recurrenceEndOption, endDate = recurrenceEndDate?.atStartOfDay(zoneId)?.toInstant()?.toEpochMilli())
                    } else null

                    // Extract Ethiopian date components for storage
                    // Note: ethiopianDate and selectedDate represent the SAME date,
                    // kept synchronized through EthiopicDate.from() and LocalDate.from() conversions
                    val ethiopianYear = ethiopianDate.get(ChronoField.YEAR)
                    val ethiopianMonth = ethiopianDate.get(ChronoField.MONTH_OF_YEAR)
                    val ethiopianDay = ethiopianDate.get(ChronoField.DAY_OF_MONTH)

                    if (isEditMode) { // Update existing event
                        onUpdateEvent(editingEvent.id, title.trim(), description.ifBlank { null }, startDateTime, endDateTime, isAllDay, recurrenceRule, if (reminderEnabled) reminderMinutes else null, category, ethiopianYear, ethiopianMonth, ethiopianDay)
                    } else { // Create new event
                        onCreateEvent(title.trim(), description.ifBlank { null }, startDateTime, endDateTime, isAllDay, recurrenceRule, if (reminderEnabled) reminderMinutes else null, category, ethiopianYear, ethiopianMonth, ethiopianDay)
                    }
                }) {
                    Text(if (isEditMode) stringResource(R.string.update) else stringResource(R.string.create))
                }
            }
        }
    }

    // Date picker dialogs
    if (showGregorianDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedGregorianDate.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli())
        DatePickerDialog(onDismissRequest = { showGregorianDatePicker = false }, confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { millis ->
                    selectedGregorianDate = java.time.Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate() // Update Ethiopian date when Gregorian date changes
                    ethiopianDate = EthiopicDate.from(selectedGregorianDate)
                }
                showGregorianDatePicker = false
            }) {
                Text(stringResource(R.string.ok))
            }
        }, dismissButton = {
            TextButton(onClick = { showGregorianDatePicker = false }) {
                Text(stringResource(R.string.cancel))
            }
        }) {
            DatePicker(state = datePickerState)
        }
    }

    // Ethiopian Date picker dialog
    if (showEthiopianDatePicker) {
        EthiopicDatePickerDialog(selectedDate = ethiopianDate, onDateSelected = { selectedEthiopianDate ->
            ethiopianDate = selectedEthiopianDate // Update Gregorian date when Ethiopian date changes
            selectedGregorianDate = LocalDate.from(selectedEthiopianDate)
        }, onDismiss = { showEthiopianDatePicker = false })
    }

    // Time pickers
    if (showStartTimePicker) {
        val timePickerState = rememberTimePickerState(initialHour = startTime.hour, initialMinute = startTime.minute, is24Hour = false)
        TimePickerDialog(onDismissRequest = { showStartTimePicker = false }, confirmButton = {
            TextButton(onClick = {
                startTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                showStartTimePicker = false
            }) {
                Text(stringResource(R.string.ok))
            }
        }, dismissButton = {
            TextButton(onClick = { showStartTimePicker = false }) {
                Text(stringResource(R.string.cancel))
            }
        }) {
            TimePicker(state = timePickerState)
        }
    }

    if (showEndTimePicker) {
        val timePickerState = rememberTimePickerState(initialHour = endTime?.hour ?: 10, initialMinute = endTime?.minute ?: 0, is24Hour = false)
        TimePickerDialog(onDismissRequest = { showEndTimePicker = false }, confirmButton = {
            TextButton(onClick = {
                endTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                showEndTimePicker = false
            }) {
                Text(stringResource(R.string.ok))
            }
        }, dismissButton = {
            TextButton(onClick = { showEndTimePicker = false }) {
                Text(stringResource(R.string.cancel))
            }
        }) {
            TimePicker(state = timePickerState)
        }
    }

    if (showRecurrenceEndDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = (recurrenceEndDate ?: selectedGregorianDate.plusMonths(1)).atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli())
        DatePickerDialog(onDismissRequest = { showRecurrenceEndDatePicker = false }, confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { millis ->
                    recurrenceEndDate = java.time.Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
                }
                showRecurrenceEndDatePicker = false
            }) {
                Text(stringResource(R.string.ok))
            }
        }, dismissButton = {
            TextButton(onClick = { showRecurrenceEndDatePicker = false }) {
                Text(stringResource(R.string.cancel))
            }
        }) {
            DatePicker(state = datePickerState)
        }
    }

    // Reminder time selection dialog
    if (showReminderTimeDialog) {
        ReminderTimeDialog(selectedMinutes = reminderMinutes, onMinutesChange = { reminderMinutes = it }, onDismiss = { showReminderTimeDialog = false })
    }
}

@Composable
private fun TimePickerDialog(
    onDismissRequest: () -> Unit, confirmButton: @Composable () -> Unit, dismissButton: @Composable () -> Unit, content: @Composable () -> Unit
) {
    AlertDialog(onDismissRequest = onDismissRequest, confirmButton = confirmButton, dismissButton = dismissButton, text = content)
}


@Composable
private fun RecurrenceSection(
    recurrenceFrequency: RecurrenceFrequency, onRecurrenceFrequencyChange: (RecurrenceFrequency) -> Unit, selectedWeekDays: Set<DayOfWeek>, onWeekDaysChange: (Set<DayOfWeek>) -> Unit, recurrenceEndDate: LocalDate?, onRecurrenceEndDateClick: () -> Unit
) {
    val isRepeatWeekly = recurrenceFrequency == RecurrenceFrequency.WEEKLY

    Column { // Repeat Weekly toggle
        Row(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onRecurrenceFrequencyChange(if (isRepeatWeekly) RecurrenceFrequency.NONE else RecurrenceFrequency.WEEKLY)
                }
                .padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(R.string.repeat_weekly), style = MaterialTheme.typography.bodyLarge)
            Switch(checked = isRepeatWeekly, onCheckedChange = { enabled ->
                onRecurrenceFrequencyChange(if (enabled) RecurrenceFrequency.WEEKLY else RecurrenceFrequency.NONE)
            })
        }

        // Weekly options with mandatory end date
        if (isRepeatWeekly) {
            Spacer(modifier = Modifier.height(12.dp))

            // Labels row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = stringResource(R.string.repeat_on), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = stringResource(R.string.label_end_date), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Weekday buttons and date picker icon in single row
            WeekDaySelectorWithDatePicker(selectedDays = selectedWeekDays, onDaysChange = onWeekDaysChange, endDate = recurrenceEndDate, onDatePickerClick = onRecurrenceEndDateClick)
        }
    }
}

@Composable
private fun WeekDaySelectorWithDatePicker(
    selectedDays: Set<DayOfWeek>, onDaysChange: (Set<DayOfWeek>) -> Unit, endDate: LocalDate?, onDatePickerClick: () -> Unit
) {
    val weekDays = listOf(DayOfWeek.MONDAY to "M", DayOfWeek.TUESDAY to "T", DayOfWeek.WEDNESDAY to "W", DayOfWeek.THURSDAY to "T", DayOfWeek.FRIDAY to "F", DayOfWeek.SATURDAY to "S", DayOfWeek.SUNDAY to "S")

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { // 7 weekday buttons with slightly smaller size
        weekDays.forEach { (day, label) ->
            val isSelected = selectedDays.contains(day)
            Box(modifier = Modifier
                    .size(36.dp) // Reduced from 40dp to accommodate date picker icon
                    .background(color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent, shape = RoundedCornerShape(18.dp))
                    .border(width = 1.dp, color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(18.dp))
                    .clickable {
                        val newDays = if (isSelected) {
                            selectedDays - day
                        } else {
                            selectedDays + day
                        }
                        onDaysChange(newDays)
                    }, contentAlignment = Alignment.Center) {
                Text(text = label, color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.labelSmall, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
            }
        }

        // Date picker icon button
        IconButton(onClick = onDatePickerClick, modifier = Modifier.size(36.dp)) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = stringResource(R.string.select_end_date), tint = if (endDate != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
private fun ReminderTimeDialog(
    selectedMinutes: Int, onMinutesChange: (Int) -> Unit, onDismiss: () -> Unit
) {
    val reminderOptions = listOf(0 to R.string.at_time_of_event, 5 to R.string.minutes_before_5, 15 to R.string.minutes_before_15, 30 to R.string.minutes_before_30, 60 to R.string.hour_before_1)

    AlertDialog(onDismissRequest = onDismiss, title = {
        Text(text = stringResource(R.string.reminder), style = MaterialTheme.typography.titleLarge)
    }, text = {
        Column {
            reminderOptions.forEach { (minutes, stringRes) ->
                Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onMinutesChange(minutes)
                            onDismiss()
                        }
                        .padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selectedMinutes == minutes, onClick = {
                        onMinutesChange(minutes)
                        onDismiss()
                    })
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = stringResource(stringRes), style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }, confirmButton = {
        TextButton(onClick = onDismiss) {
            Text(stringResource(R.string.ok))
        }
    })
}

@Composable
private fun CategorySelector(
    selectedCategory: String, onCategoryChange: (String) -> Unit
) {
    val categories = EventCategory.entries

    Column {
        Text(text = stringResource(R.string.category), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            categories.forEach { category ->
                val isSelected = selectedCategory == category.name
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onCategoryChange(category.name) }) {
                    Box(modifier = Modifier
                            .size(40.dp)
                            .background(color = if (isSelected) category.getColor() else Color.Transparent, shape = CircleShape)
                            .border(width = 2.dp, color = category.getColor(), shape = CircleShape), contentAlignment = Alignment.Center) { // First letter of category
                        Text(text = category.name.first().toString(), color = if (isSelected) Color.White else category.getColor(), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}


/**
 * Formats reminder minutes into a human-readable string.
 * Returns format: "5 min", "15 min", "30 min", "1 hour", etc.
 */
private fun formatReminderTime(minutes: Int): String {
    return when (minutes) {
        0 -> "0 min"
        60 -> "1 hour"
        in 60..Int.MAX_VALUE -> {
            val hours = minutes / 60
            if (minutes % 60 == 0) {
                "$hours hour${if (hours > 1) "s" else ""}"
            } else {
                "$minutes min"
            }
        }

        else -> "$minutes min"
    }
}
