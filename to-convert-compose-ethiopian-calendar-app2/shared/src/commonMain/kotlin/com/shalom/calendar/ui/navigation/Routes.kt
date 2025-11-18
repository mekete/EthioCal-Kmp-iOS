package com.shalom.calendar.ui.navigation

/**
 * Navigation routes for the app
 */
object Routes {
    const val ONBOARDING = "onboarding"
    const val SPLASH = "splash"
    const val MONTH = "month"
    const val EVENT = "event"
    const val HOLIDAY = "holiday"
    const val CONVERTER = "converter"
    const val MORE = "more"
    const val THEME_SETTINGS = "theme_settings"
}

/**
 * Event screen route with optional parameters
 */
object EventRoute {
    const val ROUTE = "event?selectedDate={selectedDate}&hasEvents={hasEvents}"
    const val ARG_SELECTED_DATE = "selectedDate"
    const val ARG_HAS_EVENTS = "hasEvents"

    fun createRoute(selectedDate: String? = null, hasEvents: Boolean = false): String {
        return if (selectedDate != null) {
            "event?selectedDate=$selectedDate&hasEvents=$hasEvents"
        } else {
            "event"
        }
    }
}
