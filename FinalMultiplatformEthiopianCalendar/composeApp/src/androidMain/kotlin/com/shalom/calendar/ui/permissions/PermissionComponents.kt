package com.shalom.calendar.ui.permissions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import com.shalom.calendar.R

/**
 * Dialog explaining why notification permission is needed.
 *
 * Shows before requesting POST_NOTIFICATIONS permission to provide context.
 *
 * @param onGrantClick Called when user clicks "Grant Permission"
 * @param onDismiss Called when user dismisses the dialog with dontAskForOneWeek checkbox state
 */
@Composable
fun NotificationPermissionRationaleDialog(
    onGrantClick: () -> Unit, onDismiss: (dontAskForOneWeek: Boolean) -> Unit
) {
    var dontAskForOneWeek by remember { mutableStateOf(false) }

    AlertDialog(onDismissRequest = { onDismiss(dontAskForOneWeek) }, icon = {
        Icon(imageVector = Icons.Default.NotificationsOff, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
    }, title = {
        Text(text = stringResource(R.string.permission_notification_title), style = MaterialTheme.typography.headlineSmall)
    }, text = {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = stringResource(R.string.permission_notification_rationale), style = MaterialTheme.typography.bodyMedium)
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = dontAskForOneWeek, onCheckedChange = { dontAskForOneWeek = it })
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Don't ask me for one week", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }, confirmButton = {
        Button(onClick = onGrantClick) {
            Text(stringResource(R.string.permission_grant))
        }
    }, dismissButton = {
        TextButton(onClick = { onDismiss(dontAskForOneWeek) }) {
            Text(stringResource(R.string.permission_not_now))
        }
    })
}

/**
 * Dialog explaining why exact alarm permission is needed.
 *
 * Shows before directing user to settings to grant SCHEDULE_EXACT_ALARM.
 *
 * @param onGrantClick Called when user clicks "Open Settings"
 * @param onDismiss Called when user dismisses the dialog
 */
@Composable
fun ExactAlarmPermissionRationaleDialog(
    onGrantClick: () -> Unit, onDismiss: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismiss, icon = {
        Icon(imageVector = Icons.Default.NotificationsOff, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
    }, title = {
        Text(text = stringResource(R.string.permission_exact_alarm_title), style = MaterialTheme.typography.headlineSmall)
    }, text = {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = stringResource(R.string.permission_exact_alarm_rationale), style = MaterialTheme.typography.bodyMedium)
            Text(text = stringResource(R.string.permission_exact_alarm_instructions), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }, confirmButton = {
        Button(onClick = onGrantClick) {
            Text(stringResource(R.string.permission_open_settings))
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text(stringResource(R.string.permission_not_now))
        }
    })
}

/**
 * Banner shown when permissions are missing.
 *
 * Displays at the top of EventScreen to alert users that reminders won't work.
 *
 * @param missingPermissions Description of what permissions are missing
 * @param onActionClick Called when user clicks "Fix" button
 * @param modifier Modifier for the banner
 */
@Composable
fun PermissionDeniedBanner(
    missingPermissions: String, onActionClick: () -> Unit, modifier: Modifier = Modifier
) {
    Card(modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
        Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = stringResource(R.string.permission_banner_title), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onErrorContainer)
                    Text(text = stringResource(R.string.permission_banner_message, missingPermissions), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }
            TextButton(onClick = onActionClick) {
                Text(text = stringResource(R.string.permission_fix), color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

/**
 * Compact warning banner for missing permissions.
 *
 * More subtle than PermissionDeniedBanner, can be used in smaller spaces.
 *
 * @param onActionClick Called when user clicks the banner
 * @param modifier Modifier for the banner
 */
@Composable
fun PermissionWarningBanner(
    onActionClick: () -> Unit, modifier: Modifier = Modifier
) {
    Card(modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer), onClick = onActionClick) {
        Row(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
            Text(text = stringResource(R.string.permission_warning_compact), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.NotificationsOff, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
        }
    }
}

/**
 * Dialog showing options for fixing missing permissions.
 *
 * Provides targeted actions based on which permissions are missing.
 *
 * @param needsNotificationPermission Whether POST_NOTIFICATIONS is needed
 * @param needsExactAlarmPermission Whether SCHEDULE_EXACT_ALARM is needed
 * @param onRequestNotifications Called when user wants to grant notification permission
 * @param onRequestExactAlarm Called when user wants to grant exact alarm permission
 * @param onOpenSettings Called when user wants to open general settings
 * @param onDismiss Called when user dismisses the dialog
 */
@Composable
fun PermissionFixDialog(
    needsNotificationPermission: Boolean, needsExactAlarmPermission: Boolean, onRequestNotifications: () -> Unit, onRequestExactAlarm: () -> Unit, onOpenSettings: () -> Unit, onDismiss: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismiss, icon = {
        Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
    }, title = {
        Text(text = stringResource(R.string.permission_fix_title), style = MaterialTheme.typography.headlineSmall)
    }, text = {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(text = stringResource(R.string.permission_fix_message), style = MaterialTheme.typography.bodyMedium)

            if (needsNotificationPermission) {
                OutlinedButton(onClick = {
                    onDismiss()
                    onRequestNotifications()
                }, modifier = Modifier.fillMaxWidth()) {
                    Icon(imageVector = Icons.Default.NotificationsOff, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.permission_grant_notifications))
                }
            }

            if (needsExactAlarmPermission) {
                OutlinedButton(onClick = {
                    onDismiss()
                    onRequestExactAlarm()
                }, modifier = Modifier.fillMaxWidth()) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.permission_grant_exact_alarms))
                }
            }

            if (!needsNotificationPermission && !needsExactAlarmPermission) { // If permissions are granted but notifications are disabled in system
                OutlinedButton(onClick = {
                    onDismiss()
                    onOpenSettings()
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.permission_open_settings))
                }
            }
        }
    }, confirmButton = {
        TextButton(onClick = onDismiss) {
            Text(stringResource(R.string.permission_close))
        }
    })
}
