package com.shalom.calendar.data.permissions

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import com.shalom.calendar.data.analytics.AnalyticsEvent
import com.shalom.calendar.data.analytics.AnalyticsManager
import com.shalom.calendar.data.initialization.ReminderReregistrationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages notification and alarm permissions for the app.
 *
 * Responsibilities:
 * - Track permission states with reactive StateFlow
 * - Provide methods to request permissions
 * - Monitor permission changes
 * - Coordinate permission recovery flows
 *
 * This is a singleton injected by Hilt and should be accessed throughout the app
 * to get the current permission state.
 */
@Singleton
class PermissionManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val analyticsManager: AnalyticsManager
) {
    private val _permissionState = MutableStateFlow(getInitialPermissionState())
    val permissionState: StateFlow<AppPermissionsState> = _permissionState.asStateFlow()

    private var alarmPermissionReceiver: BroadcastReceiver? = null
    private var reminderReregistrationManager: ReminderReregistrationManager? = null
    private var previousPermissionState: AppPermissionsState? = null

    /**
     * Get the initial permission state on app launch.
     */
    private fun getInitialPermissionState(): AppPermissionsState {
        return PermissionHelper.getAppPermissionsState(context)
    }

    /**
     * Refresh the current permission state.
     * Call this after requesting permissions or when returning from settings.
     *
     * @return Updated permissions state
     */
    fun refreshPermissionState(): AppPermissionsState {
        val oldState = previousPermissionState ?: _permissionState.value
        val newState = PermissionHelper.getAppPermissionsState(context)
        _permissionState.value = newState
        previousPermissionState = newState

        // Track permission changes
        trackPermissionChanges(oldState, newState)

        return newState
    }

    /**
     * Track permission changes and log analytics events
     */
    private fun trackPermissionChanges(oldState: AppPermissionsState, newState: AppPermissionsState) {
        // Track notification permission changes
        if (oldState.notificationsPermission.isGranted != newState.notificationsPermission.isGranted) {
            if (newState.notificationsPermission.isGranted) {
                analyticsManager.logEvent(
                    AnalyticsEvent.PermissionGranted(permission = "notifications")
                )
            } else {
                analyticsManager.logEvent(
                    AnalyticsEvent.PermissionDenied(
                        permission = "notifications",
                        permanently = false
                    )
                )
            }
        }

        // Track exact alarm permission changes
        if (oldState.exactAlarmPermission.isGranted != newState.exactAlarmPermission.isGranted) {
            if (newState.exactAlarmPermission.isGranted) {
                analyticsManager.logEvent(
                    AnalyticsEvent.PermissionGranted(permission = "exact_alarm")
                )
            } else {
                analyticsManager.logEvent(
                    AnalyticsEvent.PermissionDenied(
                        permission = "exact_alarm",
                        permanently = false
                    )
                )
            }
        }
    }

    /**
     * Request POST_NOTIFICATIONS permission.
     *
     * This should be called from an Activity context.
     * On Android 13+, this will show the system permission dialog.
     * On older versions, this is a no-op.
     *
     * Note: The actual permission request must be handled by the Activity
     * using ActivityResultContracts or Accompanist Permissions.
     * This method is just a helper to check if the request is needed.
     *
     * @param activity The activity to request from
     * @return true if permission request is needed, false if already granted or not required
     */
    fun shouldRequestNotificationPermission(activity: ComponentActivity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            !PermissionHelper.hasNotificationPermission(activity)
        } else {
            false
        }
    }

    /**
     * Open the system settings page to grant SCHEDULE_EXACT_ALARM permission.
     *
     * On Android 12+, this opens the "Alarms & reminders" settings page.
     * This is required because SCHEDULE_EXACT_ALARM is not a runtime permission
     * but a special app access permission.
     *
     * @param activity The activity to launch settings from
     */
    fun requestExactAlarmPermission(activity: ComponentActivity) {
        // Track settings open
        analyticsManager.logEvent(
            AnalyticsEvent.PermissionSettingsOpened(permission = "exact_alarm")
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            try {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = Uri.parse("package:${context.packageName}")
                }
                activity.startActivity(intent)
            } catch (e: Exception) { // Fallback to general app settings
                openAppSettings(activity)
            }
        }
    }

    /**
     * Open the app's notification settings page.
     *
     * Useful when notifications are disabled in system settings.
     *
     * @param activity The activity to launch settings from
     */
    fun openNotificationSettings(activity: ComponentActivity) {
        // Track settings open
        analyticsManager.logEvent(
            AnalyticsEvent.PermissionSettingsOpened(permission = "notifications")
        )

        try {
            val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                }
            } else {
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.parse("package:${context.packageName}")
                }
            }
            activity.startActivity(intent)
        } catch (e: Exception) {
            openAppSettings(activity)
        }
    }

    /**
     * Open the app's general settings page.
     *
     * @param activity The activity to launch settings from
     */
    fun openAppSettings(activity: ComponentActivity) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:${context.packageName}")
            }
            activity.startActivity(intent)
        } catch (e: Exception) {
        }
    }

    /**
     * Register a listener for SCHEDULE_EXACT_ALARM permission changes.
     *
     * On Android 12+, the system broadcasts when this permission is granted.
     * This allows us to automatically refresh permission state and re-register reminders.
     *
     * Should be called once during app initialization.
     *
     * @param reminderManager ReminderReregistrationManager to re-register reminders when permission granted
     */
    fun registerPermissionChangeListener(reminderManager: ReminderReregistrationManager) {
        this.reminderReregistrationManager = reminderManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Only register if not already registered
            if (alarmPermissionReceiver != null) {
                return
            }

            alarmPermissionReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.action == AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED) {
                        val newState = refreshPermissionState()

                        // If permission was granted, re-register all reminders
                        if (newState.exactAlarmPermission.isGranted) {
                            reminderManager.reregisterReminders()
                        }
                    }
                }
            }

            val filter = IntentFilter(AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED)
            context.registerReceiver(alarmPermissionReceiver, filter)
        }
    }

    /**
     * Unregister the permission change listener.
     *
     * Should be called when cleaning up, though typically not needed
     * since PermissionManager is a singleton that lives for the app lifetime.
     */
    fun unregisterPermissionChangeListener() {
        alarmPermissionReceiver?.let {
            try {
                context.unregisterReceiver(it)
                alarmPermissionReceiver = null
            } catch (e: Exception) {
            }
        }
    }

    /**
     * Check if all required permissions are granted.
     *
     * @return true if app can schedule reminders and show notifications
     */
    fun hasAllPermissions(): Boolean {
        return _permissionState.value.isFullyEnabled
    }

    /**
     * Check if any permission is missing.
     *
     * @return true if at least one permission is not granted
     */
    fun hasMissingPermissions(): Boolean {
        return _permissionState.value.hasMissingPermissions
    }

    /**
     * Get a user-friendly description of missing permissions.
     *
     * @return Description string, or null if no permissions are missing
     */
    fun getMissingPermissionsDescription(): String? {
        return _permissionState.value.getMissingPermissionsDescription()
    }
}
