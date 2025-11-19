package com.shalom.calendar.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.ViewDay
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ethiopiancalendar.composeapp.generated.resources.Res
import ethiopiancalendar.composeapp.generated.resources.onboarding_holidays_description
import ethiopiancalendar.composeapp.generated.resources.onboarding_holidays_muslim
import ethiopiancalendar.composeapp.generated.resources.onboarding_holidays_muslim_desc
import ethiopiancalendar.composeapp.generated.resources.onboarding_holidays_orthodox
import ethiopiancalendar.composeapp.generated.resources.onboarding_holidays_orthodox_days
import ethiopiancalendar.composeapp.generated.resources.onboarding_holidays_orthodox_days_desc
import ethiopiancalendar.composeapp.generated.resources.onboarding_holidays_orthodox_desc
import ethiopiancalendar.composeapp.generated.resources.onboarding_holidays_public
import ethiopiancalendar.composeapp.generated.resources.onboarding_holidays_public_desc
import ethiopiancalendar.composeapp.generated.resources.onboarding_holidays_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun HolidaySelectionPage(
    showPublicHolidays: Boolean,
    showOrthodoxHolidays: Boolean,
    showMuslimHolidays: Boolean,
    showOrthodoxDayNames: Boolean,
    onPublicHolidaysChanged: (Boolean) -> Unit,
    onOrthodoxHolidaysChanged: (Boolean) -> Unit,
    onMuslimHolidaysChanged: (Boolean) -> Unit,
    onOrthodoxDayNamesChanged: (Boolean) -> Unit,
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
            title = stringResource(Res.string.onboarding_holidays_title),
            subtitle = stringResource(Res.string.onboarding_holidays_description)
        )

        Spacer(modifier = Modifier.height(32.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            item {
                OnboardingOptionCard(
                    title = stringResource(Res.string.onboarding_holidays_public),
                    description = stringResource(Res.string.onboarding_holidays_public_desc),
                    icon = Icons.Filled.Flag,
                    isSelected = showPublicHolidays,
                    indicatorType = IndicatorType.CHECKBOX,
                    onCheckedChange = onPublicHolidaysChanged
                )
            }

            item {
                OnboardingOptionCard(
                    title = stringResource(Res.string.onboarding_holidays_orthodox),
                    description = stringResource(Res.string.onboarding_holidays_orthodox_desc),
                    icon = Icons.Filled.Church,
                    isSelected = showOrthodoxHolidays,
                    indicatorType = IndicatorType.CHECKBOX,
                    onCheckedChange = onOrthodoxHolidaysChanged
                )
            }

            item {
                OnboardingOptionCard(
                    title = stringResource(Res.string.onboarding_holidays_muslim),
                    description = stringResource(Res.string.onboarding_holidays_muslim_desc),
                    icon = Icons.Filled.NightlightRound,
                    isSelected = showMuslimHolidays,
                    indicatorType = IndicatorType.CHECKBOX,
                    onCheckedChange = onMuslimHolidaysChanged
                )
            }

            item {
                OnboardingOptionCard(
                    title = stringResource(Res.string.onboarding_holidays_orthodox_days),
                    description = stringResource(Res.string.onboarding_holidays_orthodox_days_desc),
                    icon = Icons.Filled.ViewDay,
                    isSelected = showOrthodoxDayNames,
                    indicatorType = IndicatorType.CHECKBOX,
                    onCheckedChange = onOrthodoxDayNamesChanged
                )
            }
        }
    }
}
