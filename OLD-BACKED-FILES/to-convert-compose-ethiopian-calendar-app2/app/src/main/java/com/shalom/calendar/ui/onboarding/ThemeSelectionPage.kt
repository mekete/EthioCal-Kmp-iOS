package com.shalom.calendar.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shalom.calendar.R
import com.shalom.calendar.ui.theme.ThemeMode

@Composable
fun ThemeSelectionPage(
    selectedTheme: ThemeMode, onThemeSelected: (ThemeMode) -> Unit, modifier: Modifier = Modifier
) {
    Column(modifier = modifier
            .fillMaxSize()
            .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(12.dp))

        OnboardingHeader(title = stringResource(R.string.onboarding_theme_title), subtitle = stringResource(R.string.onboarding_theme_description))

        Spacer(modifier = Modifier.height(32.dp))

        // Theme options
        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            OnboardingOptionCard(icon = Icons.Default.LightMode, title = stringResource(R.string.onboarding_theme_light), description = stringResource(R.string.onboarding_theme_light_desc), isSelected = selectedTheme == ThemeMode.LIGHT, onClick = { onThemeSelected(ThemeMode.LIGHT) })

            OnboardingOptionCard(icon = Icons.Default.DarkMode, title = stringResource(R.string.onboarding_theme_dark), description = stringResource(R.string.onboarding_theme_dark_desc), isSelected = selectedTheme == ThemeMode.DARK, onClick = { onThemeSelected(ThemeMode.DARK) })

            OnboardingOptionCard(icon = Icons.Default.SettingsBrightness, title = stringResource(R.string.onboarding_theme_system), description = stringResource(R.string.onboarding_theme_system_desc), isSelected = selectedTheme == ThemeMode.SYSTEM, onClick = { onThemeSelected(ThemeMode.SYSTEM) })
        }
    }
}
