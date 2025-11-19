package com.shalom.calendar.ui.holidaylist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shalom.calendar.domain.model.HolidayOccurrence
import com.shalom.calendar.presentation.holidaylist.CalendarItemListUiState
import com.shalom.calendar.presentation.holidaylist.CalendarItemListViewModel
import com.shalom.calendar.shared.resources.Res
import com.shalom.calendar.shared.resources.button_ok
import com.shalom.calendar.shared.resources.cd_holiday_info
import com.shalom.calendar.shared.resources.empty_no_holidays_display
import com.shalom.calendar.shared.resources.ethiopian_months
import com.shalom.calendar.shared.resources.holiday_info_dialog_checkbox
import com.shalom.calendar.shared.resources.holiday_info_dialog_message
import com.shalom.calendar.shared.resources.holiday_info_dialog_title
import com.shalom.calendar.shared.resources.label_ec_suffix
import com.shalom.calendar.shared.resources.screen_title_holidays
import com.shalom.calendar.shared.resources.weekday_names_short
import com.shalom.calendar.ui.components.MonthHeaderItem
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarItemListScreen(
    viewModel: CalendarItemListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val hideHolidayInfoDialog by viewModel.hideHolidayInfoDialog.collectAsState(initial = false)
    var showInfoDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.screen_title_holidays),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    if (!hideHolidayInfoDialog) {
                        IconButton(onClick = { showInfoDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = stringResource(Res.string.cd_holiday_info)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { padding ->
        when (val state = uiState) {
            is CalendarItemListUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    // Loading indicator (empty - fetching from memory)
                }
            }

            is CalendarItemListUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is CalendarItemListUiState.Success -> {
                CalendarItemListContent(
                    modifier = Modifier.padding(
                        top = padding.calculateTopPadding(),
                        bottom = 0.dp
                    ),
                    currentYear = state.currentYear,
                    calendarItems = state.filteredCalendarItems,
                    onYearIncrement = { viewModel.incrementYear() },
                    onYearDecrement = { viewModel.decrementYear() }
                )
            }
        }

        if (showInfoDialog) {
            HolidayConfigureHintDialog(
                onDismiss = { showInfoDialog = false },
                onDoNotShowAgain = { doNotShow ->
                    if (doNotShow) {
                        viewModel.setHideHolidayInfoDialog(true)
                    }
                    showInfoDialog = false
                }
            )
        }
    }
}

@Composable
private fun CalendarItemListContent(
    modifier: Modifier = Modifier,
    currentYear: Int,
    calendarItems: List<HolidayOccurrence>,
    onYearIncrement: () -> Unit,
    onYearDecrement: () -> Unit
) {
    var selectedHoliday by remember { mutableStateOf<HolidayOccurrence?>(null) }
    val ecSuffix = stringResource(Res.string.label_ec_suffix)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Year Selector Section
        MonthHeaderItem(
            centerText = "$currentYear $ecSuffix",
            prevButtonLabel = "${currentYear - 1}",
            nextButtonLabel = "${currentYear + 1}",
            onPrevClick = onYearDecrement,
            onNextClick = onYearIncrement,
            onCenterClick = { },
            currentPage = currentYear,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 0.dp),
            centerTextStyle = MaterialTheme.typography.headlineMedium,
            centerFontWeight = FontWeight.Bold,
            centerMinWidth = 150.dp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Calendar Items List
        if (calendarItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.empty_no_holidays_display),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            val monthNames = stringArrayResource(Res.array.ethiopian_months)
            val weekdayNamesShort = stringArrayResource(Res.array.weekday_names_short)

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(calendarItems) { calendarItem ->
                    HolidayItem(
                        holiday = calendarItem,
                        monthNames = monthNames,
                        weekdayNamesShort = weekdayNamesShort,
                        showCard = true,
                        onClick = { selectedHoliday = calendarItem }
                    )
                }
            }
        }
    }

    // Show holiday details dialog when a holiday is selected
    selectedHoliday?.let { holiday ->
        val monthNames = stringArrayResource(Res.array.ethiopian_months)
        HolidayDetailsDialog(
            holiday = holiday,
            monthNames = monthNames,
            onDismiss = { selectedHoliday = null }
        )
    }
}

@Composable
private fun HolidayConfigureHintDialog(
    onDismiss: () -> Unit,
    onDoNotShowAgain: (Boolean) -> Unit
) {
    var doNotShowAgain by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = stringResource(Res.string.holiday_info_dialog_title))
        },
        text = {
            Column {
                Text(text = stringResource(Res.string.holiday_info_dialog_message))

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = doNotShowAgain,
                        onCheckedChange = { doNotShowAgain = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(Res.string.holiday_info_dialog_checkbox),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onDoNotShowAgain(doNotShowAgain) }) {
                Text(stringResource(Res.string.button_ok))
            }
        }
    )
}
