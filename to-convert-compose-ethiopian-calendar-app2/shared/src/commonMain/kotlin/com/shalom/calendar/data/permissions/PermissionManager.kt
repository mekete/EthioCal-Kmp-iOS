package com.shalom.calendar.data.permissions

import kotlinx.coroutines.flow.StateFlow

/**
 * Permission state data class
 */
data class PermissionState(
    val notificationsPermission: Permission = Permission(),
    val exactAlarmPermission: Permission = Permission()
)

data class Permission(
    val isGranted: Boolean = false,
    val shouldShowRationale: Boolean = false
)

/**
 * Platform-specific permission manager interface
 */
expect class PermissionManager {
    val permissionState: StateFlow<PermissionState>

    fun openNotificationSettings()
    fun requestNotificationPermission()
    fun checkPermissions()
}
