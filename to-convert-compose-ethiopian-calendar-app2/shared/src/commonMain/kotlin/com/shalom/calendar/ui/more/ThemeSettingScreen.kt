package com.shalom.calendar.ui.more

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.shalom.calendar.data.preferences.AppTheme
import com.shalom.calendar.data.preferences.ThemeMode
import com.shalom.calendar.presentation.theme.ThemeViewModel
import com.shalom.calendar.shared.resources.Res
import com.shalom.calendar.shared.resources.cd_back
import com.shalom.calendar.shared.resources.cd_selected
import com.shalom.calendar.shared.resources.label_theme_color
import com.shalom.calendar.shared.resources.label_theme_mode
import com.shalom.calendar.shared.resources.screen_title_settings
import com.shalom.calendar.shared.resources.theme_color_names
import com.shalom.calendar.shared.resources.theme_mode_names
import com.shalom.calendar.ui.theme.blue_light_primary
import com.shalom.calendar.ui.theme.blue_light_secondary
import com.shalom.calendar.ui.theme.green_light_primary
import com.shalom.calendar.ui.theme.green_light_secondary
import com.shalom.calendar.ui.theme.orange_light_primary
import com.shalom.calendar.ui.theme.orange_light_secondary
import com.shalom.calendar.ui.theme.purple_light_primary
import com.shalom.calendar.ui.theme.purple_light_secondary
import com.shalom.calendar.ui.theme.red_light_primary
import com.shalom.calendar.ui.theme.red_light_secondary
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSettingScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: ThemeViewModel = koinViewModel()
) {
    val currentTheme by viewModel.appTheme.collectAsState()
    val currentMode by viewModel.themeMode.collectAsState()
    val themeColorNames = stringArrayResource(Res.array.theme_color_names)
    val themeModeNames = stringArrayResource(Res.array.theme_mode_names)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(Res.string.screen_title_settings))
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.cd_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ThemeColorSection(
                    currentTheme = currentTheme,
                    themeColorNames = themeColorNames,
                    onThemeSelected = { viewModel.setAppTheme(it) }
                )
            }

            item {
                ThemeModeSection(
                    currentMode = currentMode,
                    themeModeNames = themeModeNames,
                    onModeSelected = { viewModel.setThemeMode(it) }
                )
            }
        }
    }
}

@Composable
fun ThemeColorSection(
    currentTheme: AppTheme,
    themeColorNames: List<String>,
    onThemeSelected: (AppTheme) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.label_theme_color),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AppTheme.entries.forEachIndexed { index, theme ->
                    ThemeColorOption(
                        theme = theme,
                        themeName = themeColorNames.getOrElse(index) { theme.name },
                        isSelected = theme == currentTheme,
                        onSelected = { onThemeSelected(theme) }
                    )
                }
            }
        }
    }
}

@Composable
fun ThemeColorOption(
    theme: AppTheme,
    themeName: String,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    val primaryColor = when (theme) {
        AppTheme.BLUE -> blue_light_primary
        AppTheme.RED -> red_light_primary
        AppTheme.GREEN -> green_light_primary
        AppTheme.PURPLE -> purple_light_primary
        AppTheme.ORANGE -> orange_light_primary
    }

    val secondaryColor = when (theme) {
        AppTheme.BLUE -> blue_light_secondary
        AppTheme.RED -> red_light_secondary
        AppTheme.GREEN -> green_light_secondary
        AppTheme.PURPLE -> purple_light_secondary
        AppTheme.ORANGE -> orange_light_secondary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelected() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        else null
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
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(primaryColor)
                    )
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(secondaryColor)
                    )
                }

                Text(
                    text = themeName,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurface
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(Res.string.cd_selected),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun ThemeModeSection(
    currentMode: ThemeMode,
    themeModeNames: List<String>,
    onModeSelected: (ThemeMode) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.label_theme_mode),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ThemeMode.entries.forEachIndexed { index, mode ->
                    ThemeModeOption(
                        mode = mode,
                        modeName = themeModeNames.getOrElse(index) { mode.name },
                        isSelected = mode == currentMode,
                        onSelected = { onModeSelected(mode) }
                    )
                }
            }
        }
    }
}

@Composable
fun ThemeModeOption(
    mode: ThemeMode,
    modeName: String,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelected() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = modeName,
                style = MaterialTheme.typography.titleMedium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurface
            )

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(Res.string.cd_selected),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
