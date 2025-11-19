package com.shalom.calendar.ui.more

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ViewAgenda
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.shalom.calendar.data.preferences.CalendarType
import com.shalom.calendar.data.preferences.Language
import com.shalom.calendar.presentation.settings.SettingsViewModel
import com.shalom.calendar.ui.theme.ThemeMode
import com.shalom.calendar.presentation.theme.ThemeViewModel
import ethiopiancalendar.composeapp.generated.resources.Res
import ethiopiancalendar.composeapp.generated.resources.button_ok
import ethiopiancalendar.composeapp.generated.resources.cd_navigate
import ethiopiancalendar.composeapp.generated.resources.label_theme_mode
import ethiopiancalendar.composeapp.generated.resources.menu_about_us
import ethiopiancalendar.composeapp.generated.resources.menu_language
import ethiopiancalendar.composeapp.generated.resources.menu_notifications
import ethiopiancalendar.composeapp.generated.resources.menu_share_app
import ethiopiancalendar.composeapp.generated.resources.screen_title_more
import ethiopiancalendar.composeapp.generated.resources.settings_calendar_ethiopian
import ethiopiancalendar.composeapp.generated.resources.settings_calendar_gregorian
import ethiopiancalendar.composeapp.generated.resources.settings_display_dual_calendar
import ethiopiancalendar.composeapp.generated.resources.settings_ethiopian_gregorian_display
import ethiopiancalendar.composeapp.generated.resources.settings_holidays_display
import ethiopiancalendar.composeapp.generated.resources.settings_primary_calendar
import ethiopiancalendar.composeapp.generated.resources.theme_mode_names
import com.shalom.calendar.util.ShareManager
import com.shalom.calendar.util.UrlLauncher
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreen(
    urlLauncher: UrlLauncher,
    shareManager: ShareManager,
    settingsViewModel: SettingsViewModel = koinViewModel(),
    themeViewModel: ThemeViewModel = koinViewModel()
) {
    val currentLanguage by settingsViewModel.language.collectAsState()
    val primaryCalendar by settingsViewModel.primaryCalendar.collectAsState()
    val displayDualCalendar by settingsViewModel.displayDualCalendar.collectAsState()
    val showPublicHolidays by settingsViewModel.showPublicHolidays.collectAsState()
    val showOrthodoxFastingHolidays by settingsViewModel.showOrthodoxFastingHolidays.collectAsState()
    val showMuslimHolidays by settingsViewModel.showMuslimHolidays.collectAsState()
    val currentThemeMode by themeViewModel.themeMode.collectAsState()

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showThemeModeDialog by remember { mutableStateOf(false) }
    var showCalendarDialog by remember { mutableStateOf(false) }
    var showHolidaysDialog by remember { mutableStateOf(false) }
    var showAboutUsDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.screen_title_more),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding(), bottom = 0.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    SettingItem(
                        icon = Icons.Default.Language,
                        title = stringResource(Res.string.menu_language),
                        onClick = { showLanguageDialog = true }
                    )
                }

                item {
                    SettingItem(
                        icon = Icons.Default.Palette,
                        title = stringResource(Res.string.label_theme_mode),
                        onClick = { showThemeModeDialog = true }
                    )
                }

                item {
                    SettingItem(
                        icon = Icons.Default.CalendarMonth,
                        title = stringResource(Res.string.settings_ethiopian_gregorian_display),
                        onClick = { showCalendarDialog = true }
                    )
                }

                item {
                    SettingItem(
                        icon = Icons.Default.ViewAgenda,
                        title = stringResource(Res.string.settings_holidays_display),
                        onClick = { showHolidaysDialog = true }
                    )
                }

                item {
                    SettingItem(
                        icon = Icons.Default.Share,
                        title = stringResource(Res.string.menu_share_app),
                        onClick = {
                            shareManager.shareText(
                                "Check out the Ethiopian Calendar app: https://play.google.com/store/apps/details?id=com.shalom.calendar",
                                "Ethiopian Calendar App"
                            )
                        }
                    )
                }

                item {
                    SettingItem(
                        icon = Icons.Default.Info,
                        title = stringResource(Res.string.menu_about_us),
                        onClick = { showAboutUsDialog = true }
                    )
                }
            }
        }

        // Language Dialog
        if (showLanguageDialog) {
            LanguageDialog(
                currentLanguage = currentLanguage,
                onLanguageSelected = { language ->
                    settingsViewModel.setLanguage(language)
                },
                onDismiss = { showLanguageDialog = false }
            )
        }

        // Theme Mode Dialog
        if (showThemeModeDialog) {
            ThemeModeDialog(
                currentMode = currentThemeMode,
                onModeSelected = { mode ->
                    themeViewModel.setThemeMode(mode)
                },
                onDismiss = { showThemeModeDialog = false }
            )
        }

        // Calendar Display Dialog
        if (showCalendarDialog) {
            CalendarDisplayDialog(
                primaryCalendar = primaryCalendar,
                displayDualCalendar = displayDualCalendar,
                onPrimaryCalendarChange = { settingsViewModel.setPrimaryCalendar(it) },
                onDisplayDualCalendarChange = { settingsViewModel.setDisplayDualCalendar(it) },
                onDismiss = { showCalendarDialog = false }
            )
        }

        // Holidays Display Dialog
        if (showHolidaysDialog) {
            HolidaysDisplayDialog(
                showPublicHolidays = showPublicHolidays,
                showOrthodoxHolidays = showOrthodoxFastingHolidays,
                showMuslimHolidays = showMuslimHolidays,
                onShowPublicHolidaysChange = { settingsViewModel.setShowPublicHolidays(it) },
                onShowOrthodoxHolidaysChange = { settingsViewModel.setShowOrthodoxFastingHolidays(it) },
                onShowMuslimHolidaysChange = { settingsViewModel.setShowMuslimHolidays(it) },
                onDismiss = { showHolidaysDialog = false }
            )
        }

        // About Us Dialog
        if (showAboutUsDialog) {
            AboutUsDialog(
                urlLauncher = urlLauncher,
                onDismiss = { showAboutUsDialog = false }
            )
        }
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = stringResource(Res.string.cd_navigate),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun LanguageDialog(
    currentLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.menu_language)) },
        text = {
            Column {
                Language.entries.forEach { language ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLanguageSelected(language) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = language == currentLanguage,
                            onClick = { onLanguageSelected(language) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(language.displayName)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.button_ok))
            }
        }
    )
}

@Composable
fun ThemeModeDialog(
    currentMode: ThemeMode,
    onModeSelected: (ThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    val themeModeNames = stringArrayResource(Res.array.theme_mode_names)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.label_theme_mode)) },
        text = {
            Column {
                ThemeMode.entries.forEachIndexed { index, mode ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onModeSelected(mode) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = mode == currentMode,
                            onClick = { onModeSelected(mode) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(themeModeNames.getOrElse(index) { mode.name })
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.button_ok))
            }
        }
    )
}

@Composable
fun CalendarDisplayDialog(
    primaryCalendar: CalendarType,
    displayDualCalendar: Boolean,
    onPrimaryCalendarChange: (CalendarType) -> Unit,
    onDisplayDualCalendarChange: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.settings_ethiopian_gregorian_display)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.settings_primary_calendar),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPrimaryCalendarChange(CalendarType.ETHIOPIAN) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = primaryCalendar == CalendarType.ETHIOPIAN,
                        onClick = { onPrimaryCalendarChange(CalendarType.ETHIOPIAN) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(Res.string.settings_calendar_ethiopian))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPrimaryCalendarChange(CalendarType.GREGORIAN) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = primaryCalendar == CalendarType.GREGORIAN,
                        onClick = { onPrimaryCalendarChange(CalendarType.GREGORIAN) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(Res.string.settings_calendar_gregorian))
                }

                HorizontalDivider()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.settings_display_dual_calendar),
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = displayDualCalendar,
                        onCheckedChange = onDisplayDualCalendarChange
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.button_ok))
            }
        }
    )
}

@Composable
fun HolidaysDisplayDialog(
    showPublicHolidays: Boolean,
    showOrthodoxHolidays: Boolean,
    showMuslimHolidays: Boolean,
    onShowPublicHolidaysChange: (Boolean) -> Unit,
    onShowOrthodoxHolidaysChange: (Boolean) -> Unit,
    onShowMuslimHolidaysChange: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.settings_holidays_display)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Public Holidays")
                    Switch(
                        checked = showPublicHolidays,
                        onCheckedChange = onShowPublicHolidaysChange
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Orthodox Holidays")
                    Switch(
                        checked = showOrthodoxHolidays,
                        onCheckedChange = onShowOrthodoxHolidaysChange
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Muslim Holidays")
                    Switch(
                        checked = showMuslimHolidays,
                        onCheckedChange = onShowMuslimHolidaysChange
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.button_ok))
            }
        }
    )
}

@Composable
fun AboutUsDialog(
    urlLauncher: UrlLauncher,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.menu_about_us)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Developer",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text("Developed by Kerod Apps")

                HorizontalDivider()

                TextButton(
                    onClick = {
                        urlLauncher.openEmail("kerod.apps@gmail.com", "Ethiopian Calendar - Contact")
                    }
                ) {
                    Text("Contact Us")
                }

                TextButton(
                    onClick = {
                        urlLauncher.openUrl("https://play.google.com/store/apps/details?id=com.shalom.calendar")
                    }
                ) {
                    Text("Rate App")
                }

                TextButton(
                    onClick = {
                        urlLauncher.openUrl("https://sites.google.com/view/ethio-calendar-privacy-policy")
                    }
                ) {
                    Text("Privacy Policy")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.button_ok))
            }
        }
    )
}
