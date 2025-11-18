package com.shalom.calendar.data.permissions

/**
 * Represents the state of a single permission.
 */
sealed class PermissionState {
    /**
     * Permission is granted by the user.
     */
    object Granted : PermissionState()

    /**
     * Permission is denied but can still be requested.
     */
    object Denied : PermissionState()

    /**
     * Permission is permanently denied (user selected "Don't ask again").
     * Must direct user to app settings.
     */
    object PermanentlyDenied : PermissionState()

    /**
     * Permission is not required on this Android version.
     */
    object NotRequired : PermissionState()

    val isGranted: Boolean get() = this is Granted || this is NotRequired
}

/**
 * Represents the overall app permissions state for notifications and alarms.
 */
data class AppPermissionsState(
    val notificationsPermission: PermissionState, val exactAlarmPermission: PermissionState, val notificationsEnabledInSystem: Boolean, val canScheduleReminders: Boolean, val canShowNotifications: Boolean
) {
    /**
     * True if all required permissions are granted and system settings allow notifications.
     */
    val isFullyEnabled: Boolean
        get() = canScheduleReminders && canShowNotifications && notificationsEnabledInSystem

    /**
     * True if at least one permission is missing.
     */
    val hasMissingPermissions: Boolean
        get() = !notificationsPermission.isGranted || !exactAlarmPermission.isGranted

    /**
     * Get a user-friendly description of what's missing.
     */
    fun getMissingPermissionsDescription(): String? {
        val missing = mutableListOf<String>()

        if (!notificationsPermission.isGranted) {
            missing.add("notification permission")
        }
        if (!exactAlarmPermission.isGranted) {
            missing.add("exact alarm permission")
        }
        if (!notificationsEnabledInSystem && notificationsPermission.isGranted) {
            missing.add("notifications are disabled in system settings")
        }

        return if (missing.isEmpty()) null else missing.joinToString(" and ")
    }
}
