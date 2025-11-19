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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shalom.calendar.R
import com.shalom.calendar.domain.model.HolidayOccurrence
import com.shalom.calendar.ui.components.MonthHeaderItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarItemListScreen(
    viewModel: CalendarItemListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val hideHolidayInfoDialog by viewModel.hideHolidayInfoDialog.collectAsState(initial = false)
    var showInfoDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(R.string.screen_title_holidays), style = MaterialTheme.typography.titleLarge)
            }, actions = {
                if (!hideHolidayInfoDialog) {
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = stringResource(R.string.cd_holiday_info))
                    }
                }
            }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer))
        },

        ) { padding ->
        when (val state = uiState) {
            is CalendarItemListUiState.Loading -> {
                Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(padding), contentAlignment = Alignment.Center) { //We are fetching from memory, no delay at all
                    //CircularProgressIndicator()
                }
            }

            is CalendarItemListUiState.Error -> {
                Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(padding), contentAlignment = Alignment.Center) {
                    Text(text = state.message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                }
            }

            is CalendarItemListUiState.Success -> {
                CalendarItemListContent(modifier = Modifier.padding(top = padding.calculateTopPadding(), bottom = 0.dp), currentYear = state.currentYear, calendarItems = state.filteredCalendarItems, onYearIncrement = { viewModel.incrementYear() }, onYearDecrement = { viewModel.decrementYear() })
            }
        }

        if (showInfoDialog) {
            HolidayConfigureHintDialog(onDismiss = { showInfoDialog = false }, onDoNotShowAgain = { doNotShow ->
                if (doNotShow) {
                    viewModel.setHideHolidayInfoDialog(true)
                }
                showInfoDialog = false
            })
        }
    }
}

@Composable
private fun CalendarItemListContent(
    modifier: Modifier = Modifier, currentYear: Int, calendarItems: List<HolidayOccurrence>, onYearIncrement: () -> Unit, onYearDecrement: () -> Unit
) {
    var selectedHoliday by remember { mutableStateOf<HolidayOccurrence?>(null) }
    Column(modifier = modifier
            .fillMaxSize()
            .padding(16.dp)) { // Year Selector Section
        MonthHeaderItem(centerText = "$currentYear ${stringResource(R.string.label_ec_suffix)}", prevButtonLabel = "${currentYear - 1}", nextButtonLabel = "${currentYear + 1}", onPrevClick = onYearDecrement, onNextClick = onYearIncrement, onCenterClick = { // TODO: Add center click handler implementation
        }, currentPage = currentYear, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 0.dp), centerTextStyle = MaterialTheme.typography.headlineMedium, centerFontWeight = FontWeight.Bold, centerMinWidth = 150.dp)

        Spacer(modifier = Modifier.height(8.dp))

        // Calendar Items List
        if (calendarItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(R.string.empty_no_holidays_display), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            val monthNames = stringArrayResource(R.array.ethiopian_months)
            val weekdayNamesShort = stringArrayResource(R.array.weekday_names_short)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(calendarItems) { calendarItem ->
                    CalendarItemCard(calendarItem = calendarItem, monthNames = monthNames, weekdayNamesShort = weekdayNamesShort, onClick = { selectedHoliday = calendarItem })
                }
            }
        }
    }

    // Show holiday details dialog when a holiday is selected
    selectedHoliday?.let { holiday ->
        HolidayDetailsDialog(holiday = holiday, monthNames = stringArrayResource(R.array.ethiopian_months), onDismiss = { selectedHoliday = null })
    }
}

@Composable
private fun CalendarItemCard(
    calendarItem: HolidayOccurrence, monthNames: Array<String>, weekdayNamesShort: Array<String>, onClick: () -> Unit
) {
    HolidayItem(
        holiday = calendarItem,
        monthNames = monthNames,
        weekdayNamesShort = weekdayNamesShort,
        showCard = true,
        onClick = onClick
    )
}

@Composable
private fun HolidayConfigureHintDialog(
    onDismiss: () -> Unit, onDoNotShowAgain: (Boolean) -> Unit
) {
    var doNotShowAgain by remember { mutableStateOf(false) }

    AlertDialog(onDismissRequest = { onDismiss() }, title = {
        Text(text = stringResource(R.string.holiday_info_dialog_title))
    }, text = {
        Column {
            Text(text = stringResource(R.string.holiday_info_dialog_message))

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Checkbox(checked = doNotShowAgain, onCheckedChange = { doNotShowAgain = it })
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.holiday_info_dialog_checkbox), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }, confirmButton = {
        TextButton(onClick = { onDoNotShowAgain(doNotShowAgain) }) {
            Text(stringResource(R.string.button_ok))
        }
    })
}
