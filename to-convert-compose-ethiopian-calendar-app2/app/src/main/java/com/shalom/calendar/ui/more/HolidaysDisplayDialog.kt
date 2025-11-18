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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shalom.calendar.R

@Composable
fun HolidaysDisplayDialog(
    showOrthodoxDayNames: Boolean, showDayOffHolidays: Boolean, showOrthodoxFastingHolidays: Boolean, showMuslimHolidays: Boolean, onShowOrthodoxDayNamesChange: (Boolean) -> Unit, onShowDayOffHolidaysChange: (Boolean) -> Unit, onShowOrthodoxFastingHolidaysChange: (Boolean) -> Unit, onShowMuslimHolidaysChange: (Boolean) -> Unit, onDismiss: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismiss, title = {
        Text(text = stringResource(R.string.settings_holidays_display_dialog_title), style = MaterialTheme.typography.headlineSmall)
    }, text = {
        Column(modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.settings_show_day_off_holidays), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                Switch(checked = showDayOffHolidays, onCheckedChange = onShowDayOffHolidaysChange)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.settings_show_orthodox_working_holidays), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                Switch(checked = showOrthodoxFastingHolidays, onCheckedChange = onShowOrthodoxFastingHolidaysChange)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.settings_show_muslim_working_holidays), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                Switch(checked = showMuslimHolidays, onCheckedChange = onShowMuslimHolidaysChange)
            } // Show Orthodox day names
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.settings_show_orthodox_day_names), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                Switch(checked = showOrthodoxDayNames, onCheckedChange = onShowOrthodoxDayNamesChange)
            }
        }
    }, confirmButton = {
        TextButton(onClick = onDismiss) {
            Text(stringResource(R.string.button_ok))
        }
    })
}
