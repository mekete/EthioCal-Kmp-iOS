package com.shalom.calendar.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.HolidayVillage
import androidx.compose.material.icons.filled.Language
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shalom.calendar.R

@Composable
fun WelcomePage(
    onGetStarted: () -> Unit, modifier: Modifier = Modifier
) {
    Column(modifier = modifier
            .fillMaxSize()
            .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
        Spacer(modifier = Modifier.height(12.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
            OnboardingTitle(text = stringResource(R.string.onboarding_welcome_title))
            OnboardingSubtitle(text = stringResource(R.string.onboarding_welcome_subtitle))
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            OnboardingOptionCard(icon = Icons.Filled.Event, title = stringResource(R.string.onboarding_feature_dual_calendar), description = null, isSelected = false, onClick = null)

            OnboardingOptionCard(icon = Icons.Filled.HolidayVillage, title = stringResource(R.string.onboarding_feature_holidays), description = null, isSelected = false, onClick = null)

            OnboardingOptionCard(icon = Icons.Filled.AddAlert, title = stringResource(R.string.onboarding_feature_reminders), description = null, isSelected = false, onClick = null)

            OnboardingOptionCard(icon = Icons.Filled.Language, title = stringResource(R.string.onboarding_feature_multilingual), description = null, isSelected = false, onClick = null)
        } // Get Started Button
        OnboardingButton(text = stringResource(R.string.onboarding_get_started), onClick = onGetStarted)

        Spacer(modifier = Modifier.height(16.dp))
    }
}


