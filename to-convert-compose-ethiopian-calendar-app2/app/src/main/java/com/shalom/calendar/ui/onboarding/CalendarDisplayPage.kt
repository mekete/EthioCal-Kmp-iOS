package com.shalom.calendar.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LibraryAddCheck
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shalom.calendar.R
import com.shalom.calendar.data.preferences.CalendarType

@Composable
fun CalendarDisplayPage(
    primaryCalendar: CalendarType, displayDualCalendar: Boolean, onPrimaryCalendarChanged: (CalendarType) -> Unit, onDualCalendarChanged: (Boolean) -> Unit, modifier: Modifier = Modifier
) {
    Column(modifier = modifier
            .fillMaxSize()
            .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(12.dp))

        OnboardingHeader(title = stringResource(R.string.onboarding_calendar_title), subtitle = stringResource(R.string.onboarding_calendar_description))

        Spacer(modifier = Modifier.height(32.dp))

        // Calendar options
        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            OnboardingOptionCard(icon = Icons.Default.Home, title = stringResource(R.string.settings_calendar_ethiopian), isSelected = primaryCalendar == CalendarType.ETHIOPIAN, onClick = { onPrimaryCalendarChanged(CalendarType.ETHIOPIAN) })

            OnboardingOptionCard(icon = Icons.Default.Language, title = stringResource(R.string.settings_calendar_gregorian), isSelected = primaryCalendar == CalendarType.GREGORIAN, onClick = { onPrimaryCalendarChanged(CalendarType.GREGORIAN) })

            OnboardingOptionCard(icon = Icons.Default.LibraryAddCheck, title = stringResource(R.string.onboarding_calendar_dual_display), description = stringResource(R.string.onboarding_calendar_dual_display_desc), isSelected = displayDualCalendar, indicatorType = IndicatorType.SWITCH, onCheckedChange = onDualCalendarChanged)
        }
    }
}
