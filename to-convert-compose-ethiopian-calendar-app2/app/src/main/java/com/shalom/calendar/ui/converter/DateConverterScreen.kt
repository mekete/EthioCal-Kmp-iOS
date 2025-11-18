package com.shalom.calendar.ui.converter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shalom.android.material.datepicker.EthiopicDatePickerDialog
import com.shalom.calendar.R
import org.threeten.extra.chrono.EthiopicDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateConverterScreen(
    viewModel: DateConverterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val monthNames = remember { context.resources.getStringArray(R.array.ethiopian_months) }
    val weekdayNames = remember { context.resources.getStringArray(R.array.weekday_names_full) }

    // Date picker states
    var showGregorianPicker by remember { mutableStateOf(false) }
    var showEthiopianPicker by remember { mutableStateOf(false) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    // Result dialog states
    var resultDialogData by remember { mutableStateOf<ResultDialogData?>(null) }

    // Show dialogs for successful conversions/calculations
    LaunchedEffect(uiState.ethiopianResult) {
        if (uiState.ethiopianResult.isNotEmpty() && uiState.gregorianError == null) {
            // Format Gregorian input date
            val gregorianInput = try {
                val day = uiState.gregorianDay.toIntOrNull()
                val month = uiState.gregorianMonth.toIntOrNull()
                val year = uiState.gregorianYear.toIntOrNull()
                if (day != null && month != null && year != null) {
                    val date = LocalDate.of(year, month, day)
                    date.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy", java.util.Locale.US))
                } else null
            } catch (e: Exception) {
                null
            }

            if (gregorianInput != null) {
                resultDialogData = ResultDialogData(
                    gregorianDate = gregorianInput,
                    ethiopianDate = uiState.ethiopianResult
                )
            }
        }
    }

    LaunchedEffect(uiState.gregorianResult) {
        if (uiState.gregorianResult.isNotEmpty() && uiState.ethiopianError == null) {
            // Format Ethiopian input date with day of week
            val ethiopianInput = try {
                val day = uiState.ethiopianDay.toIntOrNull()
                val month = uiState.ethiopianMonth.toIntOrNull()
                val year = uiState.ethiopianYear.toIntOrNull()
                if (day != null && month != null && year != null) {
                    val date = EthiopicDate.of(year, month, day)
                    val monthName = monthNames.getOrNull(month - 1) ?: ""
                    // Get day of week from the Gregorian equivalent
                    val gregorianDate = LocalDate.from(date)
                    val dayOfWeekIndex = (gregorianDate.dayOfWeek.value - 1) % 7
                    val dayOfWeek = weekdayNames.getOrNull(dayOfWeekIndex) ?: ""
                    "$dayOfWeek, $monthName $day, $year"
                } else null
            } catch (e: Exception) {
                null
            }

            if (ethiopianInput != null) {
                resultDialogData = ResultDialogData(
                    ethiopianDate = ethiopianInput,
                    gregorianDate = uiState.gregorianResult
                )
            }
        }
    }

    LaunchedEffect(uiState.differenceResult) {
        if (uiState.differenceResult.isNotEmpty() && uiState.differenceError == null) {
            resultDialogData = ResultDialogData(
                differenceResult = uiState.differenceResult
            )
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = stringResource(R.string.screen_title_date_converter), style = MaterialTheme.typography.titleLarge)
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer))
    }) { padding ->
        Column(modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding(), bottom = 0.dp)
                .padding(16.dp)
                .verticalScroll(scrollState), verticalArrangement = Arrangement.spacedBy(12.dp)) { // Section 1: Gregorian to Ethiopian
            GregorianToEthiopianSection(gregorianDay = uiState.gregorianDay, gregorianMonth = uiState.gregorianMonth, gregorianYear = uiState.gregorianYear, result = uiState.ethiopianResult, error = uiState.gregorianError, onDayChange = { day ->
                viewModel.setGregorianDate(day, uiState.gregorianMonth, uiState.gregorianYear)
            }, onMonthChange = { month ->
                viewModel.setGregorianDate(uiState.gregorianDay, month, uiState.gregorianYear)
            }, onYearChange = { year ->
                viewModel.setGregorianDate(uiState.gregorianDay, uiState.gregorianMonth, year)
            }, onPickClick = { showGregorianPicker = true }, onConvert = { viewModel.convertToEthiopian() })

            // Section 2: Ethiopian to Gregorian
            EthiopianToGregorianSection(ethiopianDay = uiState.ethiopianDay, ethiopianMonth = uiState.ethiopianMonth, ethiopianYear = uiState.ethiopianYear, result = uiState.gregorianResult, error = uiState.ethiopianError, onDayChange = { day ->
                viewModel.setEthiopianDate(day, uiState.ethiopianMonth, uiState.ethiopianYear)
            }, onMonthChange = { month ->
                viewModel.setEthiopianDate(uiState.ethiopianDay, month, uiState.ethiopianYear)
            }, onYearChange = { year ->
                viewModel.setEthiopianDate(uiState.ethiopianDay, uiState.ethiopianMonth, year)
            }, onPickClick = { showEthiopianPicker = true }, onConvert = { viewModel.convertToGregorian() })

            // Section 3: Date Difference
            DateDifferenceSection(startDate = uiState.startDate, endDate = uiState.endDate, result = uiState.differenceResult, error = uiState.differenceError, onStartDateClick = { showStartDatePicker = true }, onEndDateClick = { showEndDatePicker = true }, onCalculate = { viewModel.calculateDateDifference() })
        }
    }

    // Gregorian Date Picker Dialog
    if (showGregorianPicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())

        DatePickerDialog(onDismissRequest = { showGregorianPicker = false }, confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { millis ->
                    val date = LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000))
                    viewModel.setGregorianDateFromPicker(date)
                }
                showGregorianPicker = false
            }) {
                Text(stringResource(R.string.button_ok))
            }
        }, dismissButton = {
            TextButton(onClick = { showGregorianPicker = false }) {
                Text(stringResource(R.string.button_cancel))
            }
        }) {
            DatePicker(state = datePickerState)
        }
    }

    // Ethiopian Date Picker Dialog using the Compose-based datepicker library
    if (showEthiopianPicker) {
        EthiopicDatePickerDialog(selectedDate = EthiopicDate.now(), onDateSelected = { ethiopicDate ->
            viewModel.setEthiopianDateFromPicker(ethiopicDate)
        }, onDismiss = {
            showEthiopianPicker = false
        })
    }

    // Start Date Picker Dialog for Date Difference
    if (showStartDatePicker) {
        EthiopicDatePickerDialog(selectedDate = uiState.startDate ?: EthiopicDate.now(), onDateSelected = { ethiopicDate ->
            viewModel.setStartDate(ethiopicDate)
        }, onDismiss = {
            showStartDatePicker = false
        })
    }

    // End Date Picker Dialog for Date Difference
    if (showEndDatePicker) {
        EthiopicDatePickerDialog(selectedDate = uiState.endDate ?: EthiopicDate.now(), onDateSelected = { ethiopicDate ->
            viewModel.setEndDate(ethiopicDate)
        }, onDismiss = {
            showEndDatePicker = false
        })
    }

    // Result Dialog
    if (resultDialogData != null) {
        ResultDialog(
            data = resultDialogData!!,
            onDismiss = { resultDialogData = null }
        )
    }
}

/**
 * Data class to hold conversion/calculation result information
 */
data class ResultDialogData(
    val ethiopianDate: String? = null,
    val gregorianDate: String? = null,
    val differenceResult: String? = null
)

@Composable
fun GregorianToEthiopianSection(
    gregorianDay: String, gregorianMonth: String, gregorianYear: String, result: String, error: String?, onDayChange: (String) -> Unit, onMonthChange: (String) -> Unit, onYearChange: (String) -> Unit, onPickClick: () -> Unit, onConvert: () -> Unit
) {

    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            Text(text = stringResource(R.string.label_from_gregorian), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)

            // Input row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                DateInputField(value = gregorianDay, onValueChange = onDayChange, label = "DD", placeholder = "DD", modifier = Modifier.weight(1f), maxValue = 31, maxLength = 2)

                DateInputField(value = gregorianMonth, onValueChange = onMonthChange, label = "MM", placeholder = "MM", modifier = Modifier.weight(1f), maxValue = 12, maxLength = 2)

                DateInputField(value = gregorianYear, onValueChange = onYearChange, label = "YYYY", placeholder = "YYYY", modifier = Modifier.weight(1.5f), maxLength = 4)

                FilledTonalIconButton(onClick = onPickClick, modifier = Modifier
                        .padding(top = 8.dp)
                        .size(56.dp)) {
                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = stringResource(R.string.cd_pick_date), modifier = Modifier.size(28.dp))
                }
            }

            // Convert button
            FilledTonalButton(onClick = onConvert, modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()) {
                Text(stringResource(R.string.button_to_ethiopian))
            }

            // Error message
            if (error != null) {
                Text(text = error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun EthiopianToGregorianSection(
    ethiopianDay: String, ethiopianMonth: String, ethiopianYear: String, result: String, error: String?, onDayChange: (String) -> Unit, onMonthChange: (String) -> Unit, onYearChange: (String) -> Unit, onPickClick: () -> Unit, onConvert: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            Text(text = stringResource(R.string.label_from_ethiopian), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)

            // Input row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                DateInputField(value = ethiopianDay, onValueChange = onDayChange, label = "DD", placeholder = "DD", modifier = Modifier.weight(1f), maxValue = 31, maxLength = 2)

                DateInputField(value = ethiopianMonth, onValueChange = onMonthChange, label = "MM", placeholder = "MM", modifier = Modifier.weight(1f), maxValue = 13, maxLength = 2)

                DateInputField(value = ethiopianYear, onValueChange = onYearChange, label = "YYYY", placeholder = "YYYY", modifier = Modifier.weight(1.5f), maxLength = 4)

                FilledTonalIconButton(onClick = onPickClick, modifier = Modifier
                        .padding(top = 4.dp)
                        .size(56.dp)) {
                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = stringResource(R.string.cd_pick_date), modifier = Modifier.size(28.dp))
                }
            }

            FilledTonalButton(onClick = onConvert, modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()) {
                Text(stringResource(R.string.button_to_gregorian))
            }

            // Error message
            if (error != null) {
                Text(text = error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun DateDifferenceSection(
    startDate: EthiopicDate?, endDate: EthiopicDate?, result: String, error: String?, onStartDateClick: () -> Unit, onEndDateClick: () -> Unit, onCalculate: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            Text(text = stringResource(R.string.label_date_difference), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) { // Start Date Picker
                Card(onClick = onStartDateClick, modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
                    Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = stringResource(R.string.label_start_date), style = MaterialTheme.typography.labelSmall)
                            Text(text = startDate?.let { formatEthiopianDate(it) } ?: "—", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                // End Date Picker
                Card(onClick = onEndDateClick, modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)) {
                    Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = stringResource(R.string.label_end_date), style = MaterialTheme.typography.labelSmall)
                            Text(text = endDate?.let { formatEthiopianDate(it) } ?: "—", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            // Calculate button
            FilledTonalButton(onClick = onCalculate, modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()) {
                Text(stringResource(R.string.button_calculate_difference))
            }

            // Error message
            if (error != null) {
                Text(text = error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun DateInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    maxValue: Int? = null,
    maxLength: Int? = null
) {
    // Check if current value is invalid (exceeds maxValue)
    // Note: We allow typing any digits up to maxLength, but show error if value exceeds maxValue
    val intValue = value.toIntOrNull()
    val isInvalid = value.isNotEmpty() && (
        maxValue != null && intValue != null && intValue > maxValue
    )

    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            // Only enforce: digits only + max length
            // Don't block based on maxValue - let user type and show error border instead
            if (newValue.all { it.isDigit() }) {
                if (maxLength == null || newValue.length <= maxLength) {
                    onValueChange(newValue)
                }
            }
        },
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        isError = isInvalid,
//        supportingText = if (isInvalid) {
//            {
//                Text(
//                    text = "Max: $maxValue",
//                    style = MaterialTheme.typography.labelSmall
//                )
//            }
//        } else null,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = modifier
    )
}

/**
 * Formats an Ethiopian date for display.
 * Returns format: "Month Day, Year" (e.g., "Meskerem 1, 2017")
 */
@Composable
private fun formatEthiopianDate(date: EthiopicDate): String {
    val monthNames = LocalContext.current.resources.getStringArray(R.array.ethiopian_months)
    val monthName = monthNames.getOrNull(date.get(ChronoField.MONTH_OF_YEAR) - 1) ?: ""
    val day = date.get(ChronoField.DAY_OF_MONTH)
    val year = date.get(ChronoField.YEAR)
    return "$monthName $day, $year"
}

/**
 * Dialog to display conversion/calculation results
 * Format similar to DateDetailsDialog without the Create Reminder button
 */
@Composable
fun ResultDialog(
    data: ResultDialogData,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (data.differenceResult != null) {
                    stringResource(R.string.date_difference_result_title)
                } else {
                    stringResource(R.string.conversion_result_title)
                },
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Show date difference result if present
                if (data.differenceResult != null) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = stringResource(R.string.label_difference_result),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = data.differenceResult,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    // Show Ethiopian Date if present
                    data.ethiopianDate?.let { ethiopianDate ->
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = stringResource(R.string.label_ethiopian_date),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = ethiopianDate,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Show Gregorian Date if present
                    data.gregorianDate?.let { gregorianDate ->
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = stringResource(R.string.label_gregorian_date),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = gregorianDate,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.button_ok))
            }
        }
    )
}
