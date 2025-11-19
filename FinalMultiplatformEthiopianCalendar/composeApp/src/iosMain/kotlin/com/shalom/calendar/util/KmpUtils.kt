package com.shalom.calendar.util

import platform.Foundation.NSUUID

actual fun randomUUID(): String = NSUUID().UUIDString()
