package com.shalom.calendar.ui.converter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shalom.calendar.presentation.converter.DateConverterViewModel
import ethiopiancalendar.composeapp.generated.resources.Res
import ethiopiancalendar.composeapp.generated.resources.button_calculate_difference
import ethiopiancalendar.composeapp.generated.resources.button_cancel
import ethiopiancalendar.composeapp.generated.resources.button_ok
import ethiopiancalendar.composeapp.generated.resources.button_to_ethiopian
import ethiopiancalendar.composeapp.generated.resources.button_to_gregorian
import ethiopiancalendar.composeapp.generated.resources.cd_pick_date
import ethiopiancalendar.composeapp.generated.resources.conversion_result_title
import ethiopiancalendar.composeapp.generated.resources.date_difference_result_title
import ethiopiancalendar.composeapp.generated.resources.ethiopian_months
import ethiopiancalendar.composeapp.generated.resources.label_date_difference
import ethiopiancalendar.composeapp.generated.resources.label_difference_result
import ethiopiancalendar.composeapp.generated.resources.label_end_date
import ethiopiancalendar.composeapp.generated.resources.label_ethiopian_date
import ethiopiancalendar.composeapp.generated.resources.label_from_ethiopian
import ethiopiancalendar.composeapp.generated.resources.label_from_gregorian
import ethiopiancalendar.composeapp.generated.resources.label_gregorian_date
import ethiopiancalendar.composeapp.generated.resources.label_start_date
import ethiopiancalendar.composeapp.generated.resources.screen_title_date_converter
import ethiopiancalendar.composeapp.generated.resources.weekday_names_full
import com.shalom.calendar.ui.components.EthiopicDatePickerDialog
import com.shalom.calendar.util.currentTimeMillis
import com.shalom.ethiopicchrono.ChronoField
import com.shalom.ethiopicchrono.EthiopicDate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.isoDayNumber
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateConverterScreen(
    viewModel: DateConverterViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val monthNames = stringArrayResource(Res.array.ethiopian_months)
    val weekdayNames = stringArrayResource(Res.array.weekday_names_full)

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
            val gregorianInput = try {
                val day = uiState.gregorianDay.toIntOrNull()
                val month = uiState.gregorianMonth.toIntOrNull()
                val year = uiState.gregorianYear.toIntOrNull()
                if (day != null && month != null && year != null) {
                    val date = LocalDate(year, month, day)
                    formatGregorianDateFull(date)
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
            val ethiopianInput = try {
                val day = uiState.ethiopianDay.toIntOrNull()
                val month = uiState.ethiopianMonth.toIntOrNull()
                val year = uiState.ethiopianYear.toIntOrNull()
                if (day != null && month != null && year != null) {
                    val date = EthiopicDate.of(year, month, day)
                    val monthName = monthNames.getOrElse(month - 1) { "" }
                    val gregorianDate = date.toLocalDate()
                    val dayOfWeekIndex = (gregorianDate.dayOfWeek.isoDayNumber - 1) % 7
                    val dayOfWeek = weekdayNames.getOrElse(dayOfWeekIndex) { "" }
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.screen_title_date_converter),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding(), bottom = 0.dp)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GregorianToEthiopianSection(
                gregorianDay = uiState.gregorianDay,
                gregorianMonth = uiState.gregorianMonth,
                gregorianYear = uiState.gregorianYear,
                result = uiState.ethiopianResult,
                error = uiState.gregorianError,
                onDayChange = { day ->
                    viewModel.setGregorianDate(day, uiState.gregorianMonth, uiState.gregorianYear)
                },
                onMonthChange = { month ->
                    viewModel.setGregorianDate(uiState.gregorianDay, month, uiState.gregorianYear)
                },
                onYearChange = { year ->
                    viewModel.setGregorianDate(uiState.gregorianDay, uiState.gregorianMonth, year)
                },
                onPickClick = { showGregorianPicker = true },
                onConvert = { viewModel.convertToEthiopian() }
            )

            EthiopianToGregorianSection(
                ethiopianDay = uiState.ethiopianDay,
                ethiopianMonth = uiState.ethiopianMonth,
                ethiopianYear = uiState.ethiopianYear,
                result = uiState.gregorianResult,
                error = uiState.ethiopianError,
                onDayChange = { day ->
                    viewModel.setEthiopianDate(day, uiState.ethiopianMonth, uiState.ethiopianYear)
                },
                onMonthChange = { month ->
                    viewModel.setEthiopianDate(uiState.ethiopianDay, month, uiState.ethiopianYear)
                },
                onYearChange = { year ->
                    viewModel.setEthiopianDate(uiState.ethiopianDay, uiState.ethiopianMonth, year)
                },
                onPickClick = { showEthiopianPicker = true },
                onConvert = { viewModel.convertToGregorian() }
            )

            DateDifferenceSection(
                startDate = uiState.startDate,
                endDate = uiState.endDate,
                result = uiState.differenceResult,
                error = uiState.differenceError,
                monthNames = monthNames,
                onStartDateClick = { showStartDatePicker = true },
                onEndDateClick = { showEndDatePicker = true },
                onCalculate = { viewModel.calculateDateDifference() }
            )
        }
    }

    // Gregorian Date Picker Dialog
    if (showGregorianPicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showGregorianPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = LocalDate.fromEpochDays((millis / (24 * 60 * 60 * 1000)).toInt())
                        viewModel.setGregorianDateFromPicker(date)
                    }
                    showGregorianPicker = false
                }) {
                    Text(stringResource(Res.string.button_ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showGregorianPicker = false }) {
                    Text(stringResource(Res.string.button_cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Ethiopian Date Picker Dialog
    if (showEthiopianPicker) {
        EthiopicDatePickerDialog(
            selectedDate = EthiopicDate.now(),
            onDateSelected = { ethiopicDate ->
                viewModel.setEthiopianDateFromPicker(ethiopicDate)
            },
            onDismiss = { showEthiopianPicker = false }
        )
    }

    // Start Date Picker Dialog
    if (showStartDatePicker) {
        EthiopicDatePickerDialog(
            selectedDate = uiState.startDate ?: EthiopicDate.now(),
            onDateSelected = { ethiopicDate ->
                viewModel.setStartDate(ethiopicDate)
            },
            onDismiss = { showStartDatePicker = false }
        )
    }

    // End Date Picker Dialog
    if (showEndDatePicker) {
        EthiopicDatePickerDialog(
            selectedDate = uiState.endDate ?: EthiopicDate.now(),
            onDateSelected = { ethiopicDate ->
                viewModel.setEndDate(ethiopicDate)
            },
            onDismiss = { showEndDatePicker = false }
        )
    }

    // Result Dialog
    if (resultDialogData != null) {
        ResultDialog(
            data = resultDialogData!!,
            onDismiss = { resultDialogData = null }
        )
    }
}

data class ResultDialogData(
    val ethiopianDate: String? = null,
    val gregorianDate: String? = null,
    val differenceResult: String? = null
)

@Composable
fun GregorianToEthiopianSection(
    gregorianDay: String,
    gregorianMonth: String,
    gregorianYear: String,
    result: String,
    error: String?,
    onDayChange: (String) -> Unit,
    onMonthChange: (String) -> Unit,
    onYearChange: (String) -> Unit,
    onPickClick: () -> Unit,
    onConvert: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(Res.string.label_from_gregorian),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DateInputField(
                    value = gregorianDay,
                    onValueChange = onDayChange,
                    label = "DD",
                    placeholder = "DD",
                    modifier = Modifier.weight(1f),
                    maxValue = 31,
                    maxLength = 2
                )

                DateInputField(
                    value = gregorianMonth,
                    onValueChange = onMonthChange,
                    label = "MM",
                    placeholder = "MM",
                    modifier = Modifier.weight(1f),
                    maxValue = 12,
                    maxLength = 2
                )

                DateInputField(
                    value = gregorianYear,
                    onValueChange = onYearChange,
                    label = "YYYY",
                    placeholder = "YYYY",
                    modifier = Modifier.weight(1.5f),
                    maxLength = 4
                )

                FilledTonalIconButton(
                    onClick = onPickClick,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = stringResource(Res.string.cd_pick_date),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            FilledTonalButton(
                onClick = onConvert,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(stringResource(Res.string.button_to_ethiopian))
            }

            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun EthiopianToGregorianSection(
    ethiopianDay: String,
    ethiopianMonth: String,
    ethiopianYear: String,
    result: String,
    error: String?,
    onDayChange: (String) -> Unit,
    onMonthChange: (String) -> Unit,
    onYearChange: (String) -> Unit,
    onPickClick: () -> Unit,
    onConvert: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(Res.string.label_from_ethiopian),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DateInputField(
                    value = ethiopianDay,
                    onValueChange = onDayChange,
                    label = "DD",
                    placeholder = "DD",
                    modifier = Modifier.weight(1f),
                    maxValue = 31,
                    maxLength = 2
                )

                DateInputField(
                    value = ethiopianMonth,
                    onValueChange = onMonthChange,
                    label = "MM",
                    placeholder = "MM",
                    modifier = Modifier.weight(1f),
                    maxValue = 13,
                    maxLength = 2
                )

                DateInputField(
                    value = ethiopianYear,
                    onValueChange = onYearChange,
                    label = "YYYY",
                    placeholder = "YYYY",
                    modifier = Modifier.weight(1.5f),
                    maxLength = 4
                )

                FilledTonalIconButton(
                    onClick = onPickClick,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = stringResource(Res.string.cd_pick_date),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            FilledTonalButton(
                onClick = onConvert,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(stringResource(Res.string.button_to_gregorian))
            }

            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun DateDifferenceSection(
    startDate: EthiopicDate?,
    endDate: EthiopicDate?,
    result: String,
    error: String?,
    monthNames: List<String>,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    onCalculate: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(Res.string.label_date_difference),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    onClick = onStartDateClick,
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = stringResource(Res.string.label_start_date),
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = startDate?.let { formatEthiopianDate(it, monthNames) } ?: "—",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Card(
                    onClick = onEndDateClick,
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = stringResource(Res.string.label_end_date),
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = endDate?.let { formatEthiopianDate(it, monthNames) } ?: "—",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            FilledTonalButton(
                onClick = onCalculate,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(stringResource(Res.string.button_calculate_difference))
            }

            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
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
    val intValue = value.toIntOrNull()
    val isInvalid = value.isNotEmpty() && (
        maxValue != null && intValue != null && intValue > maxValue
    )

    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.all { it.isDigit() }) {
                if (maxLength == null || newValue.length <= maxLength) {
                    onValueChange(newValue)
                }
            }
        },
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        isError = isInvalid,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = modifier
    )
}

private fun formatEthiopianDate(date: EthiopicDate, monthNames: List<String>): String {
    val monthName = monthNames.getOrElse(date.get(ChronoField.MONTH_OF_YEAR) - 1) { "" }
    val day = date.get(ChronoField.DAY_OF_MONTH)
    val year = date.get(ChronoField.YEAR)
    return "$monthName $day, $year"
}

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
                    stringResource(Res.string.date_difference_result_title)
                } else {
                    stringResource(Res.string.conversion_result_title)
                },
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (data.differenceResult != null) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = stringResource(Res.string.label_difference_result),
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
                    data.ethiopianDate?.let { ethiopianDate ->
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = stringResource(Res.string.label_ethiopian_date),
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

                    data.gregorianDate?.let { gregorianDate ->
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = stringResource(Res.string.label_gregorian_date),
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
                Text(stringResource(Res.string.button_ok))
            }
        }
    )
}

// Helper function for full Gregorian date formatting
private fun formatGregorianDateFull(date: LocalDate): String {
    val weekdayNames = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    val monthNames = listOf("January", "February", "March", "April", "May", "June",
                           "July", "August", "September", "October", "November", "December")

    val dayOfWeek = weekdayNames.getOrElse((date.dayOfWeek.isoDayNumber - 1) % 7) { "" }
    val monthName = monthNames.getOrElse(date.monthNumber - 1) { "" }
    val dayStr = date.dayOfMonth.toString().padStart(2, '0')

    return "$dayOfWeek, $monthName $dayStr, ${date.year}"
}
