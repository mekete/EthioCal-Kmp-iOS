package com.shalom.calendar.ui.month

import android.os.Build
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.shalom.calendar.R
import com.shalom.calendar.data.local.entity.EventInstance
import com.shalom.calendar.data.permissions.PermissionManager
import com.shalom.calendar.data.preferences.CalendarType
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.domain.model.EventCategory
import com.shalom.calendar.domain.model.HolidayOccurrence
import com.shalom.calendar.ui.components.MonthHeaderItem
import com.shalom.calendar.ui.holidaylist.HolidayDetailsDialog
import com.shalom.calendar.ui.holidaylist.HolidayItem
import com.shalom.calendar.ui.holidaylist.formatEthiopicDate
import kotlinx.coroutines.launch
import org.threeten.extra.chrono.EthiopicDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthCalendarScreen(
    viewModel: MonthCalendarViewModel = hiltViewModel(), permissionManager: PermissionManager, navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Permission state
    val permissionState by permissionManager.permissionState.collectAsState()

    // Settings preferences for checking 2 weeks
    val settingsPreferences = remember { SettingsPreferences(context) }

    val twoWeeksPassed by produceState(initialValue = false) {
        value = settingsPreferences.hasTwoWeeksPassedSinceFirstLaunch()
    }
    var showNotificationBanner by remember { mutableStateOf(false) }
    val bannerDismissed by settingsPreferences.notificationBannerDismissed.collectAsState(initial = false)

    // Check if we should show the banner
    LaunchedEffect(twoWeeksPassed, permissionState, bannerDismissed) {
        if (twoWeeksPassed && !permissionState.notificationsPermission.isGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !bannerDismissed) {
            showNotificationBanner = true
        }
    }

    val pagerState = rememberPagerState(initialPage = rememberSaveable { viewModel.initialPage }, pageCount = { MonthCalendarViewModel.TOTAL_PAGES })

    // Track current page's Ethiopian date
    val currentEthiopicDate = remember(pagerState.currentPage) {
        viewModel.getEthiopicDateForPage(pagerState.currentPage)
    }

    val monthNames = stringArrayResource(R.array.ethiopian_months)
    val weekdayNamesShort = stringArrayResource(R.array.weekday_names_short)
    val weekdayNamesFull = stringArrayResource(R.array.weekday_names_full)
    val monthDescription = formatEthiopicDate(currentEthiopicDate, monthNames, weekdayNamesShort)
    val monthDescriptionCd = stringResource(R.string.cd_calendar_state, monthDescription)

    // Get today's dates for top bar
    val todayEthiopicDate = remember { EthiopicDate.now() }
    val todayGregorianDate = remember { LocalDate.now() }
    val todayEthiopicText = formatEthiopicDate(todayEthiopicDate, monthNames, weekdayNamesFull)
    val todayGregorianText = todayGregorianDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy", Locale.US))

    val primaryCalendar by viewModel.primaryCalendar.collectAsState()
    val displayDualCalendar by viewModel.displayDualCalendar.collectAsState()

    // Dialog state
    val showDateDetailsDialog by viewModel.showDateDetailsDialog.collectAsState()
    val selectedDateForDialog by viewModel.selectedDateForDialog.collectAsState()
    val selectedDateHasEvents by viewModel.selectedDateHasEvents.collectAsState()

    // Show date details dialog
    if (showDateDetailsDialog && selectedDateForDialog != null) {
        DateDetailsDialog(date = selectedDateForDialog!!, hasEvents = selectedDateHasEvents, onDismiss = { viewModel.hideDateDetailsDialog() }, onCreateReminder = { // Format date as string for navigation
            val dateString = "${selectedDateForDialog!!.get(ChronoField.YEAR_OF_ERA)}-" + "${selectedDateForDialog!!.get(ChronoField.MONTH_OF_YEAR)}-" + "${selectedDateForDialog!!.get(ChronoField.DAY_OF_MONTH)}"

            viewModel.hideDateDetailsDialog()
            navController.navigate("event?selectedDate=$dateString&hasEvents=$selectedDateHasEvents")
        })
    }

    Scaffold(topBar = {
        TopAppBar(navigationIcon = {
            Icon(imageVector = Icons.Default.CalendarToday, tint = MaterialTheme.colorScheme.primary, //painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(R.string.screen_title_month_calendar), modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .size(28.dp))
        }, title = {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) { // Ethiopian date - larger
                Text(text = todayEthiopicText, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface) // Gregorian date - smaller
                Text(text = todayGregorianText, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }, actions = { // Today button - animate scroll to today's page
            TextButton(onClick = {
                scope.launch {
                    val todayPage = viewModel.getTodayPage()
                    pagerState.animateScrollToPage(todayPage)
                }
            }) {
                Text(stringResource(R.string.button_today))
            }
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer))
    }) { padding ->
        Column(modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding(), bottom = 0.dp)) { // Show gentle notification banner if 2 weeks have passed
            if (showNotificationBanner) {
                NotificationReminderBanner(onEnableClick = { // Open notification settings
                    (context as? ComponentActivity)?.let {
                        permissionManager.openNotificationSettings(it)
                    }

                    // Dismiss banner
                    showNotificationBanner = false
                    scope.launch {
                        settingsPreferences.setNotificationBannerDismissed(true)
                    }
                }, onDismiss = {
                    showNotificationBanner = false
                    scope.launch {
                        settingsPreferences.setNotificationBannerDismissed(true)
                    }
                })
            }

            // Second row: Prev button, month description, Next button
            // Calculate button labels and center text based on primary calendar and dual mode
            val prevButtonLabel: String
            val centerText: String
            val nextButtonLabel: String

            when (primaryCalendar) {
                CalendarType.ETHIOPIAN -> {
                    val year = currentEthiopicDate.get(ChronoField.YEAR_OF_ERA)
                    val month = currentEthiopicDate.get(ChronoField.MONTH_OF_YEAR)
                    val monthName = if (month in 1..13) monthNames[month - 1].uppercase() else "Unknown"
                    centerText = "$monthName $year"

                    if (displayDualCalendar) {
                        // Dual mode: Show Gregorian months (secondary calendar) on buttons
                        prevButtonLabel = getGregorianMonthAtStart(currentEthiopicDate)
                        nextButtonLabel = getGregorianMonthAtEnd(currentEthiopicDate)
                    } else {
                        // Single mode: Show Ethiopian months (same as primary) on buttons
                        prevButtonLabel = getPreviousEthiopianMonth(currentEthiopicDate, monthNames)
                        nextButtonLabel = getNextEthiopianMonth(currentEthiopicDate, monthNames)
                    }
                }

                CalendarType.GREGORIAN -> {
                    val ethiopianYear = currentEthiopicDate.get(ChronoField.YEAR_OF_ERA)
                    val ethiopianMonthValue = currentEthiopicDate.get(ChronoField.MONTH_OF_YEAR)
                    val daysInEthiopianMonth = when (ethiopianMonthValue) {
                        13 -> if (ethiopianYear % 4 == 3) 6 else 5  // Pagume
                        else -> 30
                    }
                    val lastDayOfEthiopianMonth = EthiopicDate.of(ethiopianYear, ethiopianMonthValue, daysInEthiopianMonth)
                    val gregorianDate = LocalDate.from(lastDayOfEthiopianMonth)
                    val gregorianYear = gregorianDate.year
                    val gregorianMonth = gregorianDate.monthValue
                    centerText = "${gregorianDate.month.name.uppercase()} $gregorianYear"

                    if (displayDualCalendar) {
                        // Dual mode: Show Ethiopian months (secondary calendar) on buttons
                        prevButtonLabel = getEthiopianMonthAtStart(gregorianYear, gregorianMonth, monthNames)
                        nextButtonLabel = getEthiopianMonthAtEnd(gregorianYear, gregorianMonth, monthNames)
                    } else {
                        // Single mode: Show Gregorian months (same as primary) on buttons
                        prevButtonLabel = getPreviousGregorianMonth(gregorianDate)
                        nextButtonLabel = getNextGregorianMonth(gregorianDate)
                    }
                }

                else -> { // Fallback to Ethiopian
                    val year = currentEthiopicDate.get(ChronoField.YEAR_OF_ERA)
                    val month = currentEthiopicDate.get(ChronoField.MONTH_OF_YEAR)
                    val monthName = if (month in 1..13) monthNames[month - 1].uppercase() else "Unknown"
                    centerText = "$monthName $year"

                    if (displayDualCalendar) {
                        // Dual mode: Show Gregorian months on buttons
                        prevButtonLabel = getGregorianMonthAtStart(currentEthiopicDate)
                        nextButtonLabel = getGregorianMonthAtEnd(currentEthiopicDate)
                    } else {
                        // Single mode: Show Ethiopian months on buttons
                        prevButtonLabel = getPreviousEthiopianMonth(currentEthiopicDate, monthNames)
                        nextButtonLabel = getNextEthiopianMonth(currentEthiopicDate, monthNames)
                    }
                }
            }

            MonthHeaderItem(centerText = centerText, prevButtonLabel = prevButtonLabel, nextButtonLabel = nextButtonLabel, onPrevClick = {
                scope.launch {
                    val prevPage = (pagerState.currentPage - 1).coerceAtLeast(0)
                    pagerState.animateScrollToPage(prevPage)
                }
            }, onNextClick = {
                scope.launch {
                    val nextPage = (pagerState.currentPage + 1).coerceAtMost(MonthCalendarViewModel.TOTAL_PAGES - 1)
                    pagerState.animateScrollToPage(nextPage)
                }
            }, onCenterClick = { // TODO: Add center click handler implementation
            }, currentPage = pagerState.currentPage, prevButtonEnabled = pagerState.currentPage > 0, nextButtonEnabled = pagerState.currentPage < MonthCalendarViewModel.TOTAL_PAGES - 1, contentDescriptionText = monthDescriptionCd)

            // HorizontalPager with recommended configuration
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize(), //beyondViewportPageCount = 2, // Preload 2 pages on each side
                flingBehavior = PagerDefaults.flingBehavior(state = pagerState)) { page -> // Load data for this page - remember the flow to prevent recreation on recomposition
                val monthData by remember(page) {
                    viewModel.loadMonthDataForPage(page)
                }.collectAsState(initial = MonthCalendarUiState.Loading)

                MonthCalendarPage(uiState = monthData, onDateClick = { date, hasEvents ->
                    viewModel.selectDate(date) // Show date details dialog with event status
                    viewModel.showDateDetailsDialog(date, hasEvents)
                })
            }
        }
    }
}

@Composable
fun MonthCalendarPage(
    uiState: MonthCalendarUiState, onDateClick: (EthiopicDate, Boolean) -> Unit
) {
    when (uiState) {
        is MonthCalendarUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { //CircularProgressIndicator()
            }
        }

        is MonthCalendarUiState.Success -> {
            MonthCalendarContent(state = uiState, onDateClick = onDateClick)
        }

        is MonthCalendarUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text(text = stringResource(R.string.error_loading_calendar), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = uiState.message, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun MonthCalendarContent(
    state: MonthCalendarUiState.Success, onDateClick: (EthiopicDate, Boolean) -> Unit, modifier: Modifier = Modifier
) { // Read month names once for all cells
    val monthNames = stringArrayResource(R.array.ethiopian_months)

    // Pre-compute holiday and event maps for O(1) lookup instead of O(n) filtering per cell
    val holidayMap = remember(state.calendarItems) {
        state.calendarItems.groupBy { holiday ->
            Triple(holiday.actualEthiopicDate.get(ChronoField.YEAR_OF_ERA), holiday.actualEthiopicDate.get(ChronoField.MONTH_OF_YEAR), holiday.actualEthiopicDate.get(ChronoField.DAY_OF_MONTH))
        }
    }

    val eventMap = remember(state.events) {
        state.events.groupBy { event ->
            Triple(event.ethiopianYear, event.ethiopianMonth, event.ethiopianDay)
        }
    }

    // Cache today's date to avoid repeated calls
    val today = remember { EthiopicDate.now() }

    Column(modifier = modifier
            .fillMaxSize()
            .padding(16.dp)) {
        HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
        WeekdayHeader()
        HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(modifier = Modifier.height(8.dp))

        // Calendar grid - wraps content height for dynamic 5 or 6 week months
        LazyVerticalGrid(columns = GridCells.Fixed(7), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.wrapContentHeight()) {
            items(count = state.dateList.size, key = { index ->
                val date = state.dateList[index]
                "${date.get(ChronoField.YEAR_OF_ERA)}-${date.get(ChronoField.MONTH_OF_YEAR)}-${date.get(ChronoField.DAY_OF_MONTH)}"
            }) { index ->
                val date = state.dateList[index]
                val dateKey = Triple(date.get(ChronoField.YEAR_OF_ERA), date.get(ChronoField.MONTH_OF_YEAR), date.get(ChronoField.DAY_OF_MONTH))

                val dateEvents = eventMap[dateKey] ?: emptyList()
                val hasEvents = dateEvents.isNotEmpty()

                DateCell(date = date,
                    currentMonth = state.currentMonth.get(ChronoField.MONTH_OF_YEAR),
                    currentGregorianYear = state.currentGregorianYear,
                    currentGregorianMonth = state.currentGregorianMonth,
                    isToday = date == today,
                    isSelected = date == state.selectedDate,
                    holidays = holidayMap[dateKey] ?: emptyList(),
                    events = dateEvents,
                    primaryCalendar = state.primaryCalendar,
                    displayDualCalendar = state.displayDualCalendar,
                    secondaryCalendar = state.secondaryCalendar,
                    monthNames = monthNames,
                    onClick = { onDateClick(date, hasEvents) })
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)

        HolidayListSection(holidays = state.calendarItems, modifier = Modifier.weight(1f))
    }
}



@Composable
fun WeekdayHeader() {
    val weekdays = stringArrayResource(R.array.weekday_names_short).toList()

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        weekdays.forEach { day ->
            Text(text = day, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Thin, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
        }
    }
}

/**
 * Represents the four different display modes for date cells
 */
private enum class DateDisplayMode {
    ETHIOPIAN_ONLY, GREGORIAN_ONLY, DUAL_ETHIOPIAN_PRIMARY, DUAL_GREGORIAN_PRIMARY
}

/**
 * Determines the display mode based on calendar preferences
 */
private fun getDisplayMode(
    primaryCalendar: CalendarType, displayDualCalendar: Boolean, secondaryCalendar: CalendarType
): DateDisplayMode {
    return when { // Single calendar display
        !displayDualCalendar -> {
            if (primaryCalendar == CalendarType.ETHIOPIAN) {
                DateDisplayMode.ETHIOPIAN_ONLY
            } else {
                DateDisplayMode.GREGORIAN_ONLY
            }
        } // Dual calendar display
        primaryCalendar == CalendarType.ETHIOPIAN && secondaryCalendar == CalendarType.GREGORIAN -> {
            DateDisplayMode.DUAL_ETHIOPIAN_PRIMARY
        }

        primaryCalendar == CalendarType.GREGORIAN && secondaryCalendar == CalendarType.ETHIOPIAN -> {
            DateDisplayMode.DUAL_GREGORIAN_PRIMARY
        } // Fallback to Ethiopian primary
        else -> DateDisplayMode.DUAL_ETHIOPIAN_PRIMARY
    }
}

/**
 * Display content for Ethiopian-only calendar mode
 */
@Composable
private fun PrimaryOnlyCellContent(
    primaryDay: Int, holidays: List<HolidayOccurrence>, events: List<EventInstance>, textColor: Color, isCurrentMonth: Boolean
) {

    Box(modifier = Modifier.fillMaxSize().padding(6.dp)) {
        if (isCurrentMonth && events.isNotEmpty()) {
            val eventColor = try {
                EventCategory.valueOf(events.first().category).getColor()
            } catch (e: IllegalArgumentException) {
                EventCategory.PERSONAL.getColor()
            }
            Box(modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(10.dp)
                    .background(eventColor, CircleShape))
        }

        Row(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = primaryDay.toString(),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = textColor,
                style = MaterialTheme.typography.bodyLarge.copy(platformStyle = PlatformTextStyle(includeFontPadding = false)),
            )
        }

        holidays.take(1).forEach { holiday ->
            Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .align(Alignment.BottomStart) // <-- forces the Row to the bottom
            ) {
                Box(modifier = Modifier
                        .fillMaxSize()
                        .background(holiday.holiday.type.getColor()))
            }
        }
    }
}

/**
 * Display content for Dual calendar with Ethiopian primary
 */
@Composable
private fun DualDateCellContent(
    primaryDay: Int, secondaryDay: Int, holidays: List<HolidayOccurrence>, events: List<EventInstance>, textColor: Color, isCurrentMonth: Boolean
) {

    Box(modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)) {
        Column(modifier = Modifier.padding(bottom = 4.dp)) {
            if (isCurrentMonth && events.isNotEmpty()) {
                val eventColor = try {
                    EventCategory.valueOf(events.first().category).getColor()
                } catch (e: IllegalArgumentException) {
                    EventCategory.PERSONAL.getColor()
                }

                Box(modifier = Modifier
                        .size(10.dp)
                        .background(eventColor, CircleShape))
            } else {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = secondaryDay.toString(),
                    fontSize = 10.sp,
                    textAlign = TextAlign.Left,
                    color = textColor.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.labelSmall.copy(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                )
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = primaryDay.toString(),
                fontSize = 20.sp,
                textAlign = TextAlign.Right, //fontWeight = FontWeight.Thin,
                color = textColor,
                style = MaterialTheme.typography.bodyLarge.copy(platformStyle = PlatformTextStyle(includeFontPadding = false)),
            )
        }

        Row(modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .align(Alignment.BottomStart) // <-- forces the Row to the bottom
        ) {
            holidays.take(1).forEach { holiday ->
                Box(modifier = Modifier
                        .fillMaxSize()
                        .background(holiday.holiday.type.getColor()))
            }
        }
    }
}


@Composable
fun DateCell(
    date: EthiopicDate, currentMonth: Int, currentGregorianYear: Int?, currentGregorianMonth: Int?, isToday: Boolean, isSelected: Boolean, holidays: List<HolidayOccurrence>, events: List<EventInstance>, primaryCalendar: CalendarType, displayDualCalendar: Boolean, secondaryCalendar: CalendarType, monthNames: Array<String>, onClick: () -> Unit
) {
    val ethiopianDay = date.get(ChronoField.DAY_OF_MONTH)
    val gregorianDate = LocalDate.from(date)
    val gregorianDay = gregorianDate.dayOfMonth

    // Determine if the date is in the "current" month based on primary calendar
    val isCurrentMonth = when (primaryCalendar) {
        CalendarType.GREGORIAN -> { // Check against Gregorian month when Gregorian is primary
            currentGregorianYear != null && currentGregorianMonth != null && gregorianDate.year == currentGregorianYear && gregorianDate.monthValue == currentGregorianMonth
        }

        else -> { // Check against Ethiopian month for Ethiopian and other calendars
            date.get(ChronoField.MONTH_OF_YEAR) == currentMonth
        }
    }

    val isTodayAndInMonth = isToday && isCurrentMonth // Determine display mode once per cell
    val displayMode = getDisplayMode(displayDualCalendar = displayDualCalendar, primaryCalendar = primaryCalendar, secondaryCalendar = secondaryCalendar)
    val isDualCalendar = displayMode == DateDisplayMode.DUAL_ETHIOPIAN_PRIMARY || displayMode == DateDisplayMode.DUAL_GREGORIAN_PRIMARY

    val textColor = when {
        !isCurrentMonth -> MaterialTheme.colorScheme.surface //onSurface..copy(alpha = 0.4f)
        else -> MaterialTheme.colorScheme.onSurface
    }

    // Border color for today indicator
    val borderColor = when {
        isTodayAndInMonth -> MaterialTheme.colorScheme.primary
        else -> Color.Transparent
    }

    // Shape based on calendar mode
    val shape = if (isDualCalendar) RoundedCornerShape(4.dp) else CircleShape

    // Accessibility content description
    val contentDesc = buildString {
        append(formatEthiopianDateFull(date, monthNames))
        if (isToday) append(", Today")
        if (isSelected) append(", Selected")
        if (holidays.isNotEmpty()) {
            val holidayNames = holidays.joinToString(", ") { it.holiday.title }
            append(", Holidays: $holidayNames")
        }
    }

    Box(modifier = Modifier
            .then(if (isDualCalendar) {
                Modifier
            } else {
                Modifier.aspectRatio(1f)
            })
            .clip(shape)
            .then(if (borderColor != Color.Transparent) {
                Modifier.border(2.dp, borderColor, shape)
            } else {
                Modifier
            })
            .clickable { onClick() }
            .padding(2.dp)
            .semantics {
                this.contentDescription = contentDesc
            }, contentAlignment = if (isDualCalendar) Alignment.Center else Alignment.Center) {
        when (displayMode) {
            DateDisplayMode.ETHIOPIAN_ONLY -> {
                PrimaryOnlyCellContent(primaryDay = ethiopianDay, holidays = holidays, events = events, textColor = textColor, isCurrentMonth = isCurrentMonth)
            }

            DateDisplayMode.GREGORIAN_ONLY -> {
                PrimaryOnlyCellContent(primaryDay = gregorianDay, holidays = holidays, events = events, textColor = textColor, isCurrentMonth = isCurrentMonth)
            }

            DateDisplayMode.DUAL_ETHIOPIAN_PRIMARY -> {
                DualDateCellContent(primaryDay = ethiopianDay, secondaryDay = gregorianDay, holidays = holidays, events = events, textColor = textColor, isCurrentMonth = isCurrentMonth)
            }

            DateDisplayMode.DUAL_GREGORIAN_PRIMARY -> {
                DualDateCellContent(primaryDay = gregorianDay, secondaryDay = ethiopianDay, holidays = holidays, events = events, textColor = textColor, isCurrentMonth = isCurrentMonth)
            }
        }
    }
}

@Composable
fun HolidayListSection(
    holidays: List<HolidayOccurrence>, modifier: Modifier = Modifier
) {
    val monthNames = stringArrayResource(R.array.ethiopian_months)
    val weekdayNamesShort = stringArrayResource(R.array.weekday_names_short)
    var selectedHoliday by remember { mutableStateOf<HolidayOccurrence?>(null) }

    if (holidays.isEmpty()) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier
                    .size(4.dp, 30.dp)
                    .background(MaterialTheme.colorScheme.surface))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.empty_no_holidays_month), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

    } else {
        LazyColumn(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(holidays) { holiday ->
                HolidayItem(holiday = holiday, monthNames = monthNames, weekdayNamesShort = weekdayNamesShort, showCard = false, onClick = { selectedHoliday = holiday })
            }
        }
    }

    // Show holiday details dialog when a holiday is selected
    selectedHoliday?.let { holiday ->
        HolidayDetailsDialog(holiday = holiday, monthNames = monthNames, onDismiss = { selectedHoliday = null })
    }
}


private fun formatEthiopianDateFull(date: EthiopicDate, monthNames: Array<String>): String {
    val year = date.get(ChronoField.YEAR_OF_ERA)
    val month = date.get(ChronoField.MONTH_OF_YEAR)
    val day = date.get(ChronoField.DAY_OF_MONTH)
    val gregorianDate = LocalDate.from(date)

    val monthName = if (month in 1..13) monthNames[month - 1] else "Unknown"

    return "$monthName $day, $year (${gregorianDate.month.name} ${gregorianDate.dayOfMonth}, ${gregorianDate.year})"
}

/**
 * Gentle notification reminder banner shown after 2 weeks
 */
@Composable
fun NotificationReminderBanner(
    onEnableClick: () -> Unit, onDismiss: () -> Unit
) {
    Card(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(imageVector = Icons.Default.NotificationsNone, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = stringResource(R.string.notification_banner_title), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text(text = stringResource(R.string.notification_banner_message), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                TextButton(onClick = onEnableClick) {
                    Text(text = stringResource(R.string.notification_banner_enable), color = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = stringResource(R.string.notification_banner_dismiss), tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }
    }
}

/**
 * Get short month name for Ethiopian calendar
 */
private fun getEthiopianMonthShort(monthNames: Array<String>, month: Int): String {
    if (month !in 1..13) return ""
    val fullName = monthNames[month - 1] // Return first 3-5 characters for short form (uppercase)
    return when (month) {
        1 -> "MESK" // Meskerem
        2 -> "TIK" // Tikimt
        3 -> "HID" // Hidar
        4 -> "TAH" // Tahsas
        5 -> "TIR" // Tir
        6 -> "YEK" // Yekatit
        7 -> "MEG" // Megabit
        8 -> "MIA" // Miazia
        9 -> "GIN" // Ginbot
        10 -> "SEN" // Sene
        11 -> "HAM" // Hamle
        12 -> "NEH" // Nehase
        13 -> "PAG" // Pagume
        else -> fullName.take(3).uppercase()
    }
}

/**
 * Get the previous Ethiopian month name (for single calendar mode)
 */
private fun getPreviousEthiopianMonth(currentEthiopicDate: EthiopicDate, monthNames: Array<String>): String {
    val currentMonth = currentEthiopicDate.get(ChronoField.MONTH_OF_YEAR)
    val currentYear = currentEthiopicDate.get(ChronoField.YEAR_OF_ERA)

    val prevMonth = if (currentMonth == 1) 13 else currentMonth - 1
    return getEthiopianMonthShort(monthNames, prevMonth)
}

/**
 * Get the next Ethiopian month name (for single calendar mode)
 */
private fun getNextEthiopianMonth(currentEthiopicDate: EthiopicDate, monthNames: Array<String>): String {
    val currentMonth = currentEthiopicDate.get(ChronoField.MONTH_OF_YEAR)
    val currentYear = currentEthiopicDate.get(ChronoField.YEAR_OF_ERA)

    val nextMonth = if (currentMonth == 13) 1 else currentMonth + 1
    return getEthiopianMonthShort(monthNames, nextMonth)
}

/**
 * Get the previous Gregorian month name (for single calendar mode)
 */
private fun getPreviousGregorianMonth(currentGregorianDate: LocalDate): String {
    val prevMonthDate = currentGregorianDate.minusMonths(1)
    return getGregorianMonthShort(prevMonthDate)
}

/**
 * Get the next Gregorian month name (for single calendar mode)
 */
private fun getNextGregorianMonth(currentGregorianDate: LocalDate): String {
    val nextMonthDate = currentGregorianDate.plusMonths(1)
    return getGregorianMonthShort(nextMonthDate)
}

/**
 * Get short month name for Gregorian calendar
 */
private fun getGregorianMonthShort(localDate: LocalDate): String {
    return localDate.month.name.take(3) // Returns JAN, FEB, MAR, etc.
}

/**
 * Calculate the Gregorian month at the start of an Ethiopian month
 */
private fun getGregorianMonthAtStart(ethiopianDate: EthiopicDate): String {
    val firstDay = EthiopicDate.of(ethiopianDate.get(ChronoField.YEAR_OF_ERA), ethiopianDate.get(ChronoField.MONTH_OF_YEAR), 1)
    val gregorianDate = LocalDate.from(firstDay)
    return getGregorianMonthShort(gregorianDate)
}

/**
 * Calculate the Gregorian month at the end of an Ethiopian month
 */
private fun getGregorianMonthAtEnd(ethiopianDate: EthiopicDate): String {
    val year = ethiopianDate.get(ChronoField.YEAR_OF_ERA)
    val month = ethiopianDate.get(ChronoField.MONTH_OF_YEAR)
    val lastDay = when (month) {
        13 -> if (year % 4 == 3) 6 else 5  // Pagume
        else -> 30
    }
    val lastDayOfMonth = EthiopicDate.of(year, month, lastDay)
    val gregorianDate = LocalDate.from(lastDayOfMonth)
    return getGregorianMonthShort(gregorianDate)
}

/**
 * Calculate the Ethiopian month at the start of a Gregorian month
 */
private fun getEthiopianMonthAtStart(gregorianYear: Int, gregorianMonth: Int, monthNames: Array<String>): String {
    val firstDay = LocalDate.of(gregorianYear, gregorianMonth, 1)
    val ethiopianDate = EthiopicDate.from(firstDay)
    return getEthiopianMonthShort(monthNames, ethiopianDate.get(ChronoField.MONTH_OF_YEAR))
}

/**
 * Calculate the Ethiopian month at the end of a Gregorian month
 */
private fun getEthiopianMonthAtEnd(gregorianYear: Int, gregorianMonth: Int, monthNames: Array<String>): String {
    val firstDay = LocalDate.of(gregorianYear, gregorianMonth, 1)
    val lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth())
    val ethiopianDate = EthiopicDate.from(lastDay)
    return getEthiopianMonthShort(monthNames, ethiopianDate.get(ChronoField.MONTH_OF_YEAR))
}

/**
 * Format EthiopicDate with day of week in "DDD, MMMM DD, YYYY" format
 */
private fun formatEthiopicDateWithDayOfWeek(date: EthiopicDate, monthNames: Array<String>, weekdayNames: Array<String>): String {
    val year = date.get(ChronoField.YEAR_OF_ERA)
    val month = date.get(ChronoField.MONTH_OF_YEAR)
    val day = date.get(ChronoField.DAY_OF_MONTH)
    val monthName = if (month in 1..13) monthNames[month - 1] else "Unknown"

    // Get day of week from the Gregorian equivalent
    val gregorianDate = LocalDate.from(date)
    val dayOfWeekIndex = (gregorianDate.dayOfWeek.value - 1) % 7 // Convert to 0-6 (Mon=0, Sun=6)
    val dayOfWeekName = weekdayNames.getOrNull(dayOfWeekIndex) ?: ""

    return "$dayOfWeekName, $monthName $day, $year"
}

/**
 * Format Gregorian date with day of week in "DDD, MMMM DD, YYYY" format
 * Always uses English day/month names for Gregorian dates
 */
private fun formatGregorianDateWithDayOfWeek(date: EthiopicDate, weekdayNames: Array<String>): String {
    val gregorianDate = LocalDate.from(date)

    // Use English locale for Gregorian dates
    val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy", Locale.US)
    return gregorianDate.format(formatter)
}

/**
 * Date Details Dialog - Shows when user clicks on a date
 * Displays Ethiopian and Gregorian dates with day of week
 * Allows navigation to EventScreen to create a reminder or view existing events
 */
@Composable
fun DateDetailsDialog(
    date: EthiopicDate, hasEvents: Boolean, onDismiss: () -> Unit, onCreateReminder: () -> Unit
) {
    val monthNames = stringArrayResource(R.array.ethiopian_months)
    val weekdayNames = stringArrayResource(R.array.weekday_names_full)

    val ethiopianText = formatEthiopicDateWithDayOfWeek(date, monthNames, weekdayNames)
    val gregorianText = formatGregorianDateWithDayOfWeek(date, weekdayNames)

    // Button text changes based on whether date has events
    val buttonText = if (hasEvents) {
        stringResource(R.string.button_view_events)
    } else {
        stringResource(R.string.button_create_reminder)
    }

    AlertDialog(onDismissRequest = onDismiss, title = {
        Text(text = stringResource(R.string.date_details_dialog_title), style = MaterialTheme.typography.titleLarge)
    }, text = {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) { // Ethiopian Date
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = stringResource(R.string.label_ethiopian_date), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = ethiopianText, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            }

            // Gregorian Date
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = stringResource(R.string.label_gregorian_date), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = gregorianText, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            }
        }
    }, confirmButton = {
        Button(onClick = onCreateReminder) {
            Text(buttonText)
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text(stringResource(R.string.button_close))
        }
    })
}