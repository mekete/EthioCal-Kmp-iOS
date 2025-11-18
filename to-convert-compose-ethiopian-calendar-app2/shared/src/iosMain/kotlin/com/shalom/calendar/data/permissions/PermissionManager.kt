package com.shalom.calendar.data.permissions

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNUserNotificationCenter
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.Foundation.NSURL

actual class PermissionManager {
    private val _permissionState = MutableStateFlow(PermissionState())
    actual val permissionState: StateFlow<PermissionState> = _permissionState.asStateFlow()

    init {
        checkPermissions()
    }

    actual fun checkPermissions() {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.getNotificationSettingsWithCompletionHandler { settings ->
            val isGranted = settings?.authorizationStatus == UNAuthorizationStatusAuthorized
            _permissionState.value = PermissionState(
                notificationsPermission = Permission(
                    isGranted = isGranted,
                    shouldShowRationale = false
                ),
                exactAlarmPermission = Permission(
                    isGranted = true, // iOS doesn't have exact alarm permission concept
                    shouldShowRationale = false
                )
            )
        }
    }

    actual fun openNotificationSettings() {
        val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString)
        if (url != null) {
            UIApplication.sharedApplication.openURL(url)
        }
    }

    actual fun requestNotificationPermission() {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        val options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge

        center.requestAuthorizationWithOptions(options) { granted, error ->
            _permissionState.value = PermissionState(
                notificationsPermission = Permission(
                    isGranted = granted,
                    shouldShowRationale = false
                ),
                exactAlarmPermission = Permission(
                    isGranted = true,
                    shouldShowRationale = false
                )
            )
        }
    }
}
