package com.shalom.android.material.datepicker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.draw.rotate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import org.threeten.extra.chrono.EthiopicDate
import java.time.LocalDate
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit

/**
 * A Compose-based Ethiopian date picker dialog.
 *
 * @param selectedDate The initially selected Ethiopian date (defaults to today)
 * @param onDateSelected Callback when a date is selected
 * @param onDismiss Callback when the dialog is dismissed
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EthiopicDatePickerDialog(
    selectedDate: EthiopicDate = EthiopicDate.now(),
    onDateSelected: (EthiopicDate) -> Unit,
    onDismiss: () -> Unit
) {
    val ethiopianMonths = stringArrayResource(R.array.ethiopian_months)
    val todayDate = remember { EthiopicDate.now() }
    var tempSelectedDate by remember { mutableStateOf(selectedDate) }
    var showYearPicker by remember { mutableStateOf(false) }

    // Calculate initial page index (0 = current month)
    val initialPage = remember { 1200 } // Middle page to allow scrolling both ways
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { 2400 } // Large range to allow scrolling
    )
    val coroutineScope = rememberCoroutineScope()

    // Calculate current month based on pager page
    val currentMonth = remember(pagerState.currentPage) {
        val monthsFromInitial = pagerState.currentPage - initialPage
        selectedDate.plus(monthsFromInitial.toLong(), ChronoUnit.MONTHS) as EthiopicDate
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(400.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Title and date display with padding
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp, bottom = 16.dp)
                ) {
                    // Title - smaller, thinner font
                    Text(
                        text = stringResource(R.string.ethiopic_picker_title),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Selected date display - thin but big font
                    Text(
                        text = formatSelectedDate(tempSelectedDate, ethiopianMonths),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Divider - thin gray line end-to-end
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 1.dp
                )

                // Calendar content with padding
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Month/Year selector
                    MonthYearSelector(
                        currentMonth = currentMonth,
                        monthNames = ethiopianMonths,
                        showYearPicker = showYearPicker,
                        onPreviousMonth = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                        onNextMonth = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        onMonthLabelClick = {
                            showYearPicker = !showYearPicker
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Year picker or Calendar grid with pager
                    if (showYearPicker) {
                        YearPickerGrid(
                            currentYear = currentMonth.get(ChronoField.YEAR),
                            onYearSelected = { year ->
                                val monthsToAdd = (year - selectedDate.get(ChronoField.YEAR)) * 12 +
                                    (currentMonth.get(ChronoField.MONTH_OF_YEAR) - selectedDate.get(ChronoField.MONTH_OF_YEAR))
                                coroutineScope.launch {
                                    pagerState.scrollToPage(initialPage + monthsToAdd)
                                }
                                showYearPicker = false
                            }
                        )
                    } else {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxWidth()
                        ) { page ->
                            val monthsFromInitial = page - initialPage
                            val monthToShow = selectedDate.plus(monthsFromInitial.toLong(), ChronoUnit.MONTHS) as EthiopicDate

                            CalendarGrid(
                                currentMonth = monthToShow,
                                selectedDate = tempSelectedDate,
                                todayDate = todayDate,
                                onDateClick = { date ->
                                    tempSelectedDate = date
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(stringResource(R.string.mtrl_picker_cancel))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = {
                                onDateSelected(tempSelectedDate)
                                onDismiss()
                            }
                        ) {
                            Text(stringResource(R.string.mtrl_picker_confirm))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthYearSelector(
    currentMonth: EthiopicDate,
    monthNames: Array<String>,
    showYearPicker: Boolean,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onMonthLabelClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Month label on left with dropdown indicator
        Row(
            modifier = Modifier
                .clickable { onMonthLabelClick() }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${getEthiopicMonthName(currentMonth.get(ChronoField.MONTH_OF_YEAR), monthNames)} ${currentMonth.get(ChronoField.YEAR)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = if (showYearPicker) "Close year picker" else "Open year picker",
                modifier = Modifier.rotate(if (showYearPicker) 180f else 0f)
            )
        }

        // Navigation icons on right
        Row {
            IconButton(onClick = onPreviousMonth) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Previous Month"
                )
            }
            IconButton(onClick = onNextMonth) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Next Month"
                )
            }
        }
    }
}

@Composable
private fun CalendarGrid(
    currentMonth: EthiopicDate,
    selectedDate: EthiopicDate,
    todayDate: EthiopicDate,
    onDateClick: (EthiopicDate) -> Unit
) {
    val weekdayNames = stringArrayResource(R.array.weekday_names_mon_first)
    Column {
        // Day of week headers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            weekdayNames.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Calendar days
        val days = generateCalendarDays(currentMonth)

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(days) { day ->
                if (day != null) {
                    DayCell(
                        day = day,
                        isSelected = day == selectedDate,
                        isToday = day == todayDate,
                        isCurrentMonth = day.get(ChronoField.MONTH_OF_YEAR) == currentMonth.get(ChronoField.MONTH_OF_YEAR) && day.get(ChronoField.YEAR) == currentMonth.get(ChronoField.YEAR),
                        onClick = { onDateClick(day) }
                    )
                } else {
                    // Empty cell for padding
                    Box(modifier = Modifier.size(40.dp))
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    day: EthiopicDate,
    isSelected: Boolean,
    isToday: Boolean,
    isCurrentMonth: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.surface
                }
            )
            .then(
                if (isToday) {
                    Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                } else {
                    Modifier
                }
            )
            .clickable(enabled = isCurrentMonth) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.get(ChronoField.DAY_OF_MONTH).toString(),
            color = when {
                isSelected -> MaterialTheme.colorScheme.onPrimary
                isToday -> MaterialTheme.colorScheme.primary
                isCurrentMonth -> MaterialTheme.colorScheme.onSurface
                else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
        )
    }
}

/**
 * Generates calendar days for the given Ethiopian month.
 * Returns a list that always has 6 rows (42 items) for consistent dialog height.
 * Uses Monday as the first day of the week (Monday=0, Sunday=6).
 */
private fun generateCalendarDays(month: EthiopicDate): List<EthiopicDate?> {
    val firstDayOfMonth = EthiopicDate.of(month.get(ChronoField.YEAR), month.get(ChronoField.MONTH_OF_YEAR), 1)
    val daysInMonth = month.lengthOfMonth()

    // Convert to Gregorian to get day of week (Monday=0, Sunday=6)
    val gregorianFirstDay = LocalDate.from(firstDayOfMonth)
    val startDayOfWeek = (gregorianFirstDay.dayOfWeek.value - 1) % 7
    val days = mutableListOf<EthiopicDate?>()
    repeat(startDayOfWeek) {
        days.add(null)
    }

    // Add all days of the month
    for (day in 1..daysInMonth) {
        days.add(EthiopicDate.of(month.get(ChronoField.YEAR), month.get(ChronoField.MONTH_OF_YEAR), day))
    }
    while (days.size < 42) {
        days.add(null)
    }

    return days
}

@Composable
private fun YearPickerGrid(
    currentYear: Int,
    onYearSelected: (Int) -> Unit
) {
    val years = (1900..2100).toList()
    val gridState = rememberLazyGridState()

    // Scroll to current year on first composition
    LaunchedEffect(currentYear) {
        val index = years.indexOf(currentYear)
        if (index >= 0) {
            // Center the current year in view (3 columns, so we want the year in middle row)
            // Calculate offset to show current year near center
            val targetIndex = (index - 6).coerceAtLeast(0)
            gridState.scrollToItem(targetIndex)
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        state = gridState,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(years) { year ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (year == currentYear)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surface
                    )
                    .clickable { onYearSelected(year) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = year.toString(),
                    color = if (year == currentYear)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (year == currentYear) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

/**
 * Formats the selected date for display.
 */
private fun formatSelectedDate(date: EthiopicDate, monthNames: Array<String>): String {
    val monthName = getEthiopicMonthName(date.get(ChronoField.MONTH_OF_YEAR), monthNames)
    val day = date.get(ChronoField.DAY_OF_MONTH)
    val year = date.get(ChronoField.YEAR)
    return "$monthName $day, $year"
}

/**
 * Gets the Ethiopian month name from the string resources.
 */
private fun getEthiopicMonthName(month: Int, monthNames: Array<String>): String {
    return monthNames.getOrNull(month - 1) ?: ""
}
