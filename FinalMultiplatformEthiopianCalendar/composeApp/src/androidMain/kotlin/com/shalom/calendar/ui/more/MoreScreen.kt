package com.shalom.calendar.ui.more

import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shalom.calendar.BuildConfig
import com.shalom.calendar.R
import com.shalom.calendar.data.permissions.PermissionManager
import com.shalom.calendar.data.preferences.CalendarType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreen(
    permissionManager: PermissionManager, settingsViewModel: SettingsViewModel = hiltViewModel(), themeViewModel: ThemeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val currentLanguage by settingsViewModel.language.collectAsState()
    val primaryCalendar by settingsViewModel.primaryCalendar.collectAsState()
    val displayDualCalendar by settingsViewModel.displayDualCalendar.collectAsState()
    val secondaryCalendar by settingsViewModel.secondaryCalendar.collectAsState()
    val showOrthodoxDayNames by settingsViewModel.showOrthodoxDayNames.collectAsState()
    val showPublicHolidays by settingsViewModel.showPublicHolidays.collectAsState()
    val showOrthodoxFastingHolidays by settingsViewModel.showOrthodoxFastingHolidays.collectAsState()
    val showMuslimHolidays by settingsViewModel.showMuslimHolidays.collectAsState()
    val displayTwoClocks by settingsViewModel.displayTwoClocks.collectAsState()
    val primaryWidgetTimezone by settingsViewModel.primaryWidgetTimezone.collectAsState()
    val secondaryWidgetTimezone by settingsViewModel.secondaryWidgetTimezone.collectAsState()
    val currentThemeMode by themeViewModel.themeMode.collectAsState()
    val permissionState by permissionManager.permissionState.collectAsState()

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showThemeModeDialog by remember { mutableStateOf(false) }
    var showWidgetDialog by remember { mutableStateOf(false) }
    var showCalendarDialog by remember { mutableStateOf(false) }
    var showHolidaysDialog by remember { mutableStateOf(false) }
    var showOrthodoxDayNamesDialog by remember { mutableStateOf(false) }
    var showAboutUsDialog by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(R.string.screen_title_more), style = MaterialTheme.typography.titleLarge)
            }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer))
        },

        ) { padding ->
        Surface(modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding(), bottom = 0.dp), color = MaterialTheme.colorScheme.background) {
            LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                item {
                    SettingItem(icon = Icons.Default.Language, title = stringResource(R.string.menu_language), onClick = {
                        showLanguageDialog = true
                    })
                }

                item {
                    SettingItem(icon = Icons.Default.Palette, title = stringResource(R.string.label_theme_mode), onClick = {
                        showThemeModeDialog = true
                    })
                }

                // Calendar to Display Button
                item {
                    SettingItem(icon = Icons.Default.CalendarMonth, title = stringResource(R.string.settings_ethiopian_gregorian_display), onClick = { showCalendarDialog = true })
                }

                // Holidays to Display Button
                item {
                    SettingItem(icon = Icons.Default.ViewAgenda, title = stringResource(R.string.settings_holidays_display), onClick = { showHolidaysDialog = true })
                }

                // Orthodox Day Names Button (visible only when showOrthodoxDayNames is true)
                if (showOrthodoxDayNames) {
                    item {
                        SettingItem(icon = Icons.Default.Church, title = stringResource(R.string.settings_orthodox_day_names_button), onClick = { showOrthodoxDayNamesDialog = true })
                    }
                }

                // Widget Options Button
                item {
                    SettingItem(icon = Icons.Default.Widgets, title = stringResource(R.string.settings_widget_options), onClick = { showWidgetDialog = true })
                }

                // Notifications Settings Button - only show if notifications are not enabled
                if (!permissionState.canShowNotifications) {
                    item {
                        SettingItem(icon = Icons.Default.Notifications, title = stringResource(R.string.menu_notifications), onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                (context as? ComponentActivity)?.let { activity ->
                                    permissionManager.openNotificationSettings(activity)
                                }
                            } else { // On older Android, just open app settings
                                (context as? ComponentActivity)?.let { activity ->
                                    permissionManager.openAppSettings(activity)
                                }
                            }
                        })
                    }
                }

                // Share App Button
                item {
                    SettingItem(icon = Icons.Default.Share, title = stringResource(R.string.menu_share_app), onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "Ethiopian Calendar App")
                            putExtra(Intent.EXTRA_TEXT, "Check out the Ethiopian Calendar app: https://play.google.com/store/apps/details?id=com.shalom.calendar")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share App"))
                    })
                }

                item {
                    SettingItem(icon = Icons.Default.Info, title = stringResource(R.string.menu_about_us), onClick = { showAboutUsDialog = true })
                }
            }
        }

        if (showLanguageDialog) {
            LanguageDialog(currentLanguage = currentLanguage, onLanguageSelected = { language ->
                settingsViewModel.setLanguage(language)
            }, onDismiss = {
                showLanguageDialog = false
            })
        }

        // Theme Mode Dialog
        if (showThemeModeDialog) {
            ThemeModeDialog(currentMode = currentThemeMode, onModeSelected = { mode ->
                themeViewModel.setThemeMode(mode)
            }, onDismiss = {
                showThemeModeDialog = false
            })
        }

        // Widget Settings Dialog
        if (showWidgetDialog) {
            WidgetSettingsDialog(displayTwoClocks = displayTwoClocks, primaryTimezone = primaryWidgetTimezone, secondaryTimezone = secondaryWidgetTimezone, onDisplayTwoClocksChange = { settingsViewModel.setDisplayTwoClocks(it) }, onPrimaryTimezoneChange = { settingsViewModel.setPrimaryWidgetTimezone(it) }, onSecondaryTimezoneChange = { settingsViewModel.setSecondaryWidgetTimezone(it) }, onDismiss = { showWidgetDialog = false })
        }

        // Calendar Display Dialog
        if (showCalendarDialog) {
            CalendarDisplayDialog(primaryCalendar = primaryCalendar, displayDualCalendar = displayDualCalendar, secondaryCalendar = secondaryCalendar, onPrimaryCalendarChange = { settingsViewModel.setPrimaryCalendar(it) }, onDisplayDualCalendarChange = { settingsViewModel.setDisplayDualCalendar(it) }, onSecondaryCalendarChange = { settingsViewModel.setSecondaryCalendar(it) }, onDismiss = { showCalendarDialog = false })
        }

        // Holidays Display Dialog
        if (showHolidaysDialog) {
            HolidaysDisplayDialog(showOrthodoxDayNames = showOrthodoxDayNames,
                showDayOffHolidays = showPublicHolidays,
                showOrthodoxFastingHolidays = showOrthodoxFastingHolidays,
                showMuslimHolidays = showMuslimHolidays,
                onShowOrthodoxDayNamesChange = { settingsViewModel.setShowOrthodoxDayNames(it) },
                onShowDayOffHolidaysChange = { settingsViewModel.setShowPublicHolidays(it) },
                onShowOrthodoxFastingHolidaysChange = { settingsViewModel.setShowOrthodoxFastingHolidays(it) },
                onShowMuslimHolidaysChange = { settingsViewModel.setShowMuslimHolidays(it) },
                onDismiss = { showHolidaysDialog = false })
        }

        // Orthodox Day Names Dialog
        if (showOrthodoxDayNamesDialog) {
            OrthodoxDayNamesDialog(onDismiss = { showOrthodoxDayNamesDialog = false })
        }

        // About Us Dialog
        if (showAboutUsDialog) {
            AboutUsDialog(onDismiss = { showAboutUsDialog = false })
        }
    }
}

private const val PRIVACY_POLICY = "https://sites.google.com/view/ethio-calendar-privacy-policy"

private const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.shalom.calendar"

private const val CONTACT_EMAIL = "mailto:kerod.apps@gmail.com"

@Composable
fun AboutUsDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(onDismissRequest = onDismiss, title = {
        Text(text = stringResource(R.string.about_us_dialog_title), style = MaterialTheme.typography.headlineSmall)
    }, text = {
        LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { // Developer Section
            item {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = stringResource(R.string.about_us_developer), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(text = stringResource(R.string.about_us_developed_by), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            item { HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp)) }

            // Contact Us
            item {
                AboutUsItem(title = stringResource(R.string.about_us_contact_us), onClick = {
                    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                        data = CONTACT_EMAIL.toUri()
                        putExtra(Intent.EXTRA_SUBJECT, "Ethiopian Calendar - Contact")
                    }
                    context.startActivity(Intent.createChooser(emailIntent, "Send Email"))
                })
            }

            item { HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp)) }

            // Rate App
            item {
                AboutUsItem(title = stringResource(R.string.about_us_rate_app), onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, PLAY_STORE_URL.toUri())
                    context.startActivity(intent)
                })
            }

            item { HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp)) }

            // App Version
            item {
                AboutUsInfoItem(title = stringResource(R.string.about_us_app_version), value = BuildConfig.VERSION_NAME)
            }

            item { HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp)) }

            // Build Time
            item {
                AboutUsInfoItem(title = stringResource(R.string.about_us_build_time), value = BuildConfig.BUILD_TIME)
            }

            item { HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp)) }

            // Privacy Policy
            item {
                AboutUsItem(title = stringResource(R.string.about_us_privacy_policy), onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, PRIVACY_POLICY.toUri())
                    context.startActivity(intent)
                })
            }

        }
    }, confirmButton = {
        TextButton(onClick = onDismiss) {
            Text(stringResource(R.string.button_ok))
        }
    })
}

@Composable
fun AboutUsItem(
    title: String, onClick: () -> Unit
) {
    Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun AboutUsInfoItem(
    title: String, value: String
) {
    Column(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)) {
        Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f))
    }
}

@Composable
fun SettingItem(
    icon: ImageVector, title: String, onClick: () -> Unit
) {
    Card(modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                Text(text = title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = stringResource(R.string.cd_navigate), tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun CalendarTypeOption(
    text: String, selected: Boolean, enabled: Boolean = true, onClick: () -> Unit
) {
    Row(modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = selected, onClick = onClick, enabled = enabled, colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary, disabledSelectedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f), disabledUnselectedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge, color = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant
        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f))
    }
}

@Composable
fun CalendarDisplayDialog(
    primaryCalendar: CalendarType, displayDualCalendar: Boolean, secondaryCalendar: CalendarType, onPrimaryCalendarChange: (CalendarType) -> Unit, onDisplayDualCalendarChange: (Boolean) -> Unit, onSecondaryCalendarChange: (CalendarType) -> Unit, onDismiss: () -> Unit
) { // Auto-select secondary calendar based on primary
    val autoSelectedSecondary = if (primaryCalendar == CalendarType.ETHIOPIAN) {
        CalendarType.GREGORIAN
    } else {
        CalendarType.ETHIOPIAN
    }

    AlertDialog(onDismissRequest = onDismiss, title = {
        Text(text = stringResource(R.string.settings_calendar_display_dialog_title), style = MaterialTheme.typography.titleLarge)
    }, text = {
        Column(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) { // Primary Calendar Section
            Text(text = stringResource(R.string.settings_primary_calendar), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

            Column(modifier = Modifier.fillMaxWidth()) {
                CalendarTypeOption(text = stringResource(R.string.settings_calendar_ethiopian), selected = primaryCalendar == CalendarType.ETHIOPIAN, onClick = {
                    onPrimaryCalendarChange(CalendarType.ETHIOPIAN) // Auto-select opposite for secondary
                    onSecondaryCalendarChange(CalendarType.GREGORIAN)
                })

                CalendarTypeOption(text = stringResource(R.string.settings_calendar_gregorian), selected = primaryCalendar == CalendarType.GREGORIAN, onClick = {
                    onPrimaryCalendarChange(CalendarType.GREGORIAN) // Auto-select opposite for secondary
                    onSecondaryCalendarChange(CalendarType.ETHIOPIAN)
                })
            }

            HorizontalDivider()

            // Display Dual Calendar Section
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.settings_display_dual_calendar), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
                Switch(checked = displayDualCalendar, onCheckedChange = onDisplayDualCalendarChange)
            }

            // Secondary Calendar Section (shown only if dual calendar is enabled)
            if (displayDualCalendar) {
                HorizontalDivider()

                Text(text = stringResource(R.string.settings_secondary_calendar), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

                Column(modifier = Modifier.fillMaxWidth()) {
                    CalendarTypeOption(text = stringResource(R.string.settings_calendar_ethiopian), selected = autoSelectedSecondary == CalendarType.ETHIOPIAN, enabled = false, onClick = { /* Disabled - auto-selected */ })

                    CalendarTypeOption(text = stringResource(R.string.settings_calendar_gregorian), selected = autoSelectedSecondary == CalendarType.GREGORIAN, enabled = false, onClick = { /* Disabled - auto-selected */ })
                }
            }
        }
    }, confirmButton = {
        TextButton(onClick = onDismiss) {
            Text(stringResource(R.string.button_ok))
        }
    })
}

@Composable
fun ThemeModeDialog(
    currentMode: com.shalom.calendar.ui.theme.ThemeMode, onModeSelected: (com.shalom.calendar.ui.theme.ThemeMode) -> Unit, onDismiss: () -> Unit
) {
    val themeModeNames = stringArrayResource(R.array.theme_mode_names)

    AlertDialog(onDismissRequest = onDismiss, title = {
        Text(text = stringResource(R.string.label_theme_mode), style = MaterialTheme.typography.titleLarge)
    }, text = {
        Column(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            com.shalom.calendar.ui.theme.ThemeMode.entries.forEachIndexed { index, mode ->
                ThemeModeOption(mode = mode, modeName = themeModeNames[index], isSelected = mode == currentMode, onSelected = { onModeSelected(mode) })
            }
        }
    }, confirmButton = {
        TextButton(onClick = onDismiss) {
            Text(stringResource(R.string.button_ok))
        }
    })
}
