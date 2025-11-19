package com.shalom.calendar.ui.more

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.shalom.calendar.R
import com.shalom.calendar.data.model.getTimeZoneList
import com.shalom.calendar.ui.components.AutocompleteTextField

@Composable
fun WidgetSettingsDialog(
    displayTwoClocks: Boolean, primaryTimezone: String, secondaryTimezone: String, onDisplayTwoClocksChange: (Boolean) -> Unit, onPrimaryTimezoneChange: (String) -> Unit, onSecondaryTimezoneChange: (String) -> Unit, onDismiss: () -> Unit
) { // Get the list of timezones
    val timeZoneList = remember { getTimeZoneList() }

    // Find display names for the current timezone IDs
    val primaryDisplayName = remember(primaryTimezone) {
        timeZoneList.find { it.zoneId == primaryTimezone }?.displayName ?: primaryTimezone
    }
    val secondaryDisplayName = remember(secondaryTimezone) {
        timeZoneList.find { it.zoneId == secondaryTimezone }?.displayName ?: secondaryTimezone
    }

    var primaryTimezoneText by remember { mutableStateOf(primaryDisplayName) }
    var secondaryTimezoneText by remember { mutableStateOf(secondaryDisplayName) }

    AlertDialog(onDismissRequest = onDismiss, title = {
        Text(text = stringResource(R.string.settings_widget_dialog_title), style = MaterialTheme.typography.headlineSmall)
    }, text = {
        Column(modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) { // Primary Widget timezone with autocomplete
            AutocompleteTextField(value = primaryTimezoneText, onValueChange = {
                primaryTimezoneText = it
            }, onTimezoneSelected = { timezone ->
                primaryTimezoneText = timezone.displayName
                onPrimaryTimezoneChange(timezone.zoneId)
            }, timeZoneList = timeZoneList, label = stringResource(R.string.settings_primary_timezone), placeholder = stringResource(R.string.settings_timezone_hint), modifier = Modifier.fillMaxWidth())

            // Secondary Widget timezone with autocomplete
            AutocompleteTextField(value = secondaryTimezoneText, onValueChange = {
                secondaryTimezoneText = it
            }, onTimezoneSelected = { timezone ->
                secondaryTimezoneText = timezone.displayName
                onSecondaryTimezoneChange(timezone.zoneId)
            }, timeZoneList = timeZoneList, label = stringResource(R.string.settings_secondary_timezone), placeholder = stringResource(R.string.settings_timezone_hint), enabled = displayTwoClocks, modifier = Modifier.fillMaxWidth())

            //Display two clocks
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.settings_display_two_clocks),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = displayTwoClocks,
                    onCheckedChange = onDisplayTwoClocksChange
                )
            }
            Text(
                text = "Changes may take up to 30 mins to reflect",
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic
            )
        }
    }, confirmButton = {
        TextButton(onClick = onDismiss) {
            Text(stringResource(R.string.button_ok))
        }
    })
}
