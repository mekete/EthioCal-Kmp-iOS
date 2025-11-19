package com.shalom.calendar.data.permissions

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * Utility object for checking notification and alarm permissions.
 *
 * This helper provides convenient methods to check permission states
 * across different Android versions.
 */
object PermissionHelper {

    /**
     * Check if the app has permission to post notifications.
     *
     * On Android 13+ (API 33+), this requires the POST_NOTIFICATIONS runtime permission.
     * On older versions, notifications are always allowed (unless disabled in system settings).
     *
     * @param context Application context
     * @return true if permission is granted or not required
     */
    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else { // Always granted on Android 12 and below
            true
        }
    }

    /**
     * Check if the app has permission to schedule exact alarms.
     *
     * On Android 12+ (API 31+), this requires the SCHEDULE_EXACT_ALARM permission.
     * - Android 12-13: Auto-granted
     * - Android 14+: Denied by default, user must grant in settings
     *
     * @param context Application context
     * @return true if permission is granted or not required
     */
    fun hasExactAlarmPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.canScheduleExactAlarms()
        } else { // Always allowed on Android 11 and below
            true
        }
    }

    /**
     * Check if notifications are enabled in system settings.
     *
     * Even with POST_NOTIFICATIONS permission granted, users can disable
     * notifications for the app in system settings.
     *
     * @param context Application context
     * @return true if notifications are enabled
     */
    fun areNotificationsEnabledInSystem(context: Context): Boolean {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.areNotificationsEnabled()
    }

    /**
     * Check if the app can show notifications.
     *
     * This combines both the runtime permission check and system settings check.
     *
     * @param context Application context
     * @return true if the app can show notifications
     */
    fun canShowNotifications(context: Context): Boolean {
        return hasNotificationPermission(context) && areNotificationsEnabledInSystem(context)
    }

    /**
     * Check if the app can schedule event reminders.
     *
     * This requires both notification and exact alarm permissions.
     *
     * @param context Application context
     * @return true if the app can schedule reminders
     */
    fun canScheduleReminders(context: Context): Boolean {
        return hasNotificationPermission(context) && hasExactAlarmPermission(context)
    }

    /**
     * Get the current state of the POST_NOTIFICATIONS permission.
     *
     * @param context Application context
     * @return Permission state
     */
    fun getNotificationPermissionState(context: Context): PermissionState {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

            if (hasPermission) {
                PermissionState.Granted
            } else { // Note: We can't reliably detect "PermanentlyDenied" state from Context alone
                // This requires Activity.shouldShowRequestPermissionRationale()
                PermissionState.Denied
            }
        } else {
            PermissionState.NotRequired
        }
    }

    /**
     * Get the current state of the SCHEDULE_EXACT_ALARM permission.
     *
     * @param context Application context
     * @return Permission state
     */
    fun getExactAlarmPermissionState(context: Context): PermissionState {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (alarmManager.canScheduleExactAlarms()) {
                PermissionState.Granted
            } else {
                PermissionState.Denied
            }
        } else {
            PermissionState.NotRequired
        }
    }

    /**
     * Get the complete permissions state for the app.
     *
     * @param context Application context
     * @return Complete permissions state
     */
    fun getAppPermissionsState(context: Context): AppPermissionsState {
        val notificationPermission = getNotificationPermissionState(context)
        val exactAlarmPermission = getExactAlarmPermissionState(context)
        val notificationsEnabled = areNotificationsEnabledInSystem(context)

        return AppPermissionsState(notificationsPermission = notificationPermission, exactAlarmPermission = exactAlarmPermission, notificationsEnabledInSystem = notificationsEnabled, canScheduleReminders = canScheduleReminders(context), canShowNotifications = canShowNotifications(context))
    }

    /**
     * Check if POST_NOTIFICATIONS permission is required on this Android version.
     *
     * @return true if running on Android 13+ (API 33+)
     */
    fun isNotificationPermissionRequired(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    /**
     * Check if SCHEDULE_EXACT_ALARM permission is required on this Android version.
     *
     * @return true if running on Android 12+ (API 31+)
     */
    fun isExactAlarmPermissionRequired(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }
}
