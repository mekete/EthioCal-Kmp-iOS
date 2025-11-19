package com.shalom.calendar.data.permissions

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

actual class PermissionManager(
    private val context: Context
) {
    private val _permissionState = MutableStateFlow(getInitialPermissionState())
    actual val permissionState: StateFlow<PermissionState> = _permissionState.asStateFlow()

    private fun getInitialPermissionState(): PermissionState {
        return PermissionState(
            notificationsPermission = Permission(
                isGranted = hasNotificationPermission(),
                shouldShowRationale = false
            ),
            exactAlarmPermission = Permission(
                isGranted = hasExactAlarmPermission(),
                shouldShowRationale = false
            )
        )
    }

    actual fun checkPermissions() {
        _permissionState.value = PermissionState(
            notificationsPermission = Permission(
                isGranted = hasNotificationPermission(),
                shouldShowRationale = false
            ),
            exactAlarmPermission = Permission(
                isGranted = hasExactAlarmPermission(),
                shouldShowRationale = false
            )
        )
    }

    actual fun openNotificationSettings() {
        try {
            val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            } else {
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.parse("package:${context.packageName}")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to app settings
            openAppSettings()
        }
    }

    actual fun requestNotificationPermission() {
        // Note: Actual permission request must be done from Activity
        // This is a placeholder - the UI layer should handle the actual request
        checkPermissions()
    }

    private fun openAppSettings() {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:${context.packageName}")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Ignore
        }
    }

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Pre-Android 13 doesn't require explicit permission
        }
    }

    private fun hasExactAlarmPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.canScheduleExactAlarms()
        } else {
            true // Pre-Android 12 doesn't require this permission
        }
    }
}
