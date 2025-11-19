package com.shalom.calendar.domain.model

/**
 * Returns the current time in milliseconds since epoch.
 * Platform-specific implementation.
 */
expect fun currentTimeMillis(): Long
