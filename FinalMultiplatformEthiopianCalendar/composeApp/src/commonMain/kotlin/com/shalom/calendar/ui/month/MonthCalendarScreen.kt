package com.shalom.calendar.ui.month

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shalom.calendar.data.preferences.CalendarType
import com.shalom.calendar.domain.model.HolidayOccurrence
import com.shalom.calendar.presentation.month.MonthCalendarViewModel
import com.shalom.calendar.presentation.month.MonthPageData
import ethiopiancalendar.composeapp.generated.resources.Res
import ethiopiancalendar.composeapp.generated.resources.button_ok
import ethiopiancalendar.composeapp.generated.resources.cd_go_to_today
import ethiopiancalendar.composeapp.generated.resources.ethiopian_months
import ethiopiancalendar.composeapp.generated.resources.weekday_names_short
import com.shalom.calendar.ui.components.MonthHeaderItem
import com.shalom.calendar.ui.holidaylist.HolidayItem
import com.shalom.calendar.util.today
import com.shalom.ethiopicchrono.ChronoField
import com.shalom.ethiopicchrono.EthiopicDate
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthCalendarScreen(
    viewModel: MonthCalendarViewModel = koinViewModel(),
    onNavigateToEvent: ((String, Boolean) -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = rememberSaveable { viewModel.initialPage },
        pageCount = { MonthCalendarViewModel.TOTAL_PAGES }
    )

    val monthNames = stringArrayResource(Res.array.ethiopian_months)
    val weekdayNamesShort = stringArrayResource(Res.array.weekday_names_short)

    val currentEthiopicDate = remember(pagerState.currentPage) {
        viewModel.getEthiopicDateForPage(pagerState.currentPage)
    }

    val todayEthiopicDate = remember { EthiopicDate.now() }
    val todayGregorianDate = remember { today() }

    val primaryCalendar by viewModel.primaryCalendar.collectAsState()
    val displayDualCalendar by viewModel.displayDualCalendar.collectAsState()

    val showDateDetailsDialog by viewModel.showDateDetailsDialog.collectAsState()
    val selectedDateForDialog by viewModel.selectedDateForDialog.collectAsState()
    val selectedDateHasEvents by viewModel.selectedDateHasEvents.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        val month = todayEthiopicDate.get(ChronoField.MONTH_OF_YEAR)
                        val day = todayEthiopicDate.get(ChronoField.DAY_OF_MONTH)
                        val year = todayEthiopicDate.get(ChronoField.YEAR_OF_ERA)
                        val monthName = monthNames.getOrElse(month - 1) { "" }
                        Text(
                            text = "$monthName $day, $year",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = formatGregorianDateSimple(todayGregorianDate),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(viewModel.initialPage)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = stringResource(Res.string.cd_go_to_today)
                        )
                    }
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
                .padding(padding)
        ) {
            // Month header with navigation
            val currentMonth = currentEthiopicDate.get(ChronoField.MONTH_OF_YEAR)
            val currentYear = currentEthiopicDate.get(ChronoField.YEAR_OF_ERA)
            val currentMonthName = monthNames.getOrElse(currentMonth - 1) { "" }

            val prevDate = viewModel.getEthiopicDateForPage(pagerState.currentPage - 1)
            val nextDate = viewModel.getEthiopicDateForPage(pagerState.currentPage + 1)
            val prevMonthName = monthNames.getOrElse(prevDate.get(ChronoField.MONTH_OF_YEAR) - 1) { "" }
            val nextMonthName = monthNames.getOrElse(nextDate.get(ChronoField.MONTH_OF_YEAR) - 1) { "" }

            MonthHeaderItem(
                centerText = "$currentMonthName $currentYear",
                prevButtonLabel = prevMonthName,
                nextButtonLabel = nextMonthName,
                onPrevClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                },
                onNextClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                currentPage = pagerState.currentPage
            )

            // Weekday headers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                weekdayNamesShort.forEach { dayName ->
                    Text(
                        text = dayName,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Calendar pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                val pageData = viewModel.loadMonthDataForPage(page)
                CalendarMonthPage(
                    pageData = pageData,
                    primaryCalendar = primaryCalendar,
                    displayDualCalendar = displayDualCalendar,
                    monthNames = monthNames,
                    weekdayNamesShort = weekdayNamesShort,
                    onDayClick = { date ->
                        viewModel.showDateDetailsDialog(date)
                    }
                )
            }
        }
    }

    // Date details dialog
    if (showDateDetailsDialog && selectedDateForDialog != null) {
        DateDetailsDialog(
            date = selectedDateForDialog!!,
            hasEvents = selectedDateHasEvents,
            monthNames = monthNames,
            onDismiss = { viewModel.hideDateDetailsDialog() },
            onCreateReminder = {
                val dateString = "${selectedDateForDialog!!.get(ChronoField.YEAR_OF_ERA)}-" +
                        "${selectedDateForDialog!!.get(ChronoField.MONTH_OF_YEAR)}-" +
                        "${selectedDateForDialog!!.get(ChronoField.DAY_OF_MONTH)}"
                onNavigateToEvent?.invoke(dateString, selectedDateHasEvents)
                viewModel.hideDateDetailsDialog()
            }
        )
    }
}

@Composable
fun CalendarMonthPage(
    pageData: MonthPageData,
    primaryCalendar: CalendarType,
    displayDualCalendar: Boolean,
    monthNames: List<String>,
    weekdayNamesShort: List<String>,
    onDayClick: (EthiopicDate) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Calendar grid
        val grid = if (primaryCalendar == CalendarType.ETHIOPIAN) {
            pageData.ethiopianGrid
        } else {
            pageData.gregorianGrid
        }

        grid.chunked(7).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { dayData ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .then(
                                if (dayData != null) {
                                    Modifier
                                        .clickable { onDayClick(dayData.ethiopicDate) }
                                        .background(
                                            when {
                                                dayData.isToday -> MaterialTheme.colorScheme.primary
                                                dayData.holidays.isNotEmpty() -> MaterialTheme.colorScheme.primaryContainer
                                                else -> Color.Transparent
                                            }
                                        )
                                } else {
                                    Modifier
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (dayData != null) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = dayData.primaryDay.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (dayData.isToday) FontWeight.Bold else FontWeight.Normal,
                                    color = when {
                                        dayData.isToday -> MaterialTheme.colorScheme.onPrimary
                                        dayData.holidays.isNotEmpty() -> MaterialTheme.colorScheme.onPrimaryContainer
                                        else -> MaterialTheme.colorScheme.onSurface
                                    }
                                )
                                if (displayDualCalendar) {
                                    Text(
                                        text = dayData.secondaryDay.toString(),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 8.sp,
                                        color = when {
                                            dayData.isToday -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                                        }
                                    )
                                }
                                // Event indicator
                                if (dayData.hasEvents) {
                                    Box(
                                        modifier = Modifier
                                            .size(4.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.tertiary)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Holidays for current selection
        if (pageData.holidays.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                LazyColumn(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(pageData.holidays.take(3)) { holiday ->
                        HolidayItem(
                            holiday = holiday,
                            monthNames = monthNames,
                            weekdayNamesShort = weekdayNamesShort
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DateDetailsDialog(
    date: EthiopicDate,
    hasEvents: Boolean,
    monthNames: List<String>,
    onDismiss: () -> Unit,
    onCreateReminder: () -> Unit
) {
    val month = date.get(ChronoField.MONTH_OF_YEAR)
    val day = date.get(ChronoField.DAY_OF_MONTH)
    val year = date.get(ChronoField.YEAR_OF_ERA)
    val monthName = monthNames.getOrElse(month - 1) { "" }

    val gregorianDate = date.toLocalDate()
    val gregorianFormatted = formatGregorianDateSimple(gregorianDate)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "$monthName $day, $year",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = gregorianFormatted,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (hasEvents) {
                    Text(
                        text = "Has events",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onCreateReminder) {
                Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text("Add Event")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.button_ok))
            }
        }
    )
}

// Helper function to format Gregorian date without DateTimeFormatter
private fun formatGregorianDateSimple(date: LocalDate): String {
    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
    val monthName = monthNames.getOrElse(date.monthNumber - 1) { "" }
    return "$monthName ${date.dayOfMonth}, ${date.year}"
}
