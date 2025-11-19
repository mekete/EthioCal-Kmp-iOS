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
import androidx.compose.ui.unit.dp
import com.shalom.calendar.data.preferences.CalendarType
import ethiopiancalendar.composeapp.generated.resources.Res
import ethiopiancalendar.composeapp.generated.resources.onboarding_calendar_description
import ethiopiancalendar.composeapp.generated.resources.onboarding_calendar_dual_display
import ethiopiancalendar.composeapp.generated.resources.onboarding_calendar_dual_display_desc
import ethiopiancalendar.composeapp.generated.resources.onboarding_calendar_title
import ethiopiancalendar.composeapp.generated.resources.settings_calendar_ethiopian
import ethiopiancalendar.composeapp.generated.resources.settings_calendar_gregorian
import org.jetbrains.compose.resources.stringResource

@Composable
fun CalendarDisplayPage(
    primaryCalendar: CalendarType,
    displayDualCalendar: Boolean,
    onPrimaryCalendarChanged: (CalendarType) -> Unit,
    onDualCalendarChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        OnboardingHeader(
            title = stringResource(Res.string.onboarding_calendar_title),
            subtitle = stringResource(Res.string.onboarding_calendar_description)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Calendar options
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OnboardingOptionCard(
                icon = Icons.Default.Home,
                title = stringResource(Res.string.settings_calendar_ethiopian),
                isSelected = primaryCalendar == CalendarType.ETHIOPIAN,
                onClick = { onPrimaryCalendarChanged(CalendarType.ETHIOPIAN) }
            )

            OnboardingOptionCard(
                icon = Icons.Default.Language,
                title = stringResource(Res.string.settings_calendar_gregorian),
                isSelected = primaryCalendar == CalendarType.GREGORIAN,
                onClick = { onPrimaryCalendarChanged(CalendarType.GREGORIAN) }
            )

            OnboardingOptionCard(
                icon = Icons.Default.LibraryAddCheck,
                title = stringResource(Res.string.onboarding_calendar_dual_display),
                description = stringResource(Res.string.onboarding_calendar_dual_display_desc),
                isSelected = displayDualCalendar,
                indicatorType = IndicatorType.SWITCH,
                onCheckedChange = onDualCalendarChanged
            )
        }
    }
}
