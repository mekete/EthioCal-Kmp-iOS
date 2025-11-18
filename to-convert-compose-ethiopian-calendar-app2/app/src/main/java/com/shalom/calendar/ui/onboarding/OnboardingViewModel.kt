package com.shalom.calendar.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shalom.calendar.data.analytics.AnalyticsEvent
import com.shalom.calendar.data.analytics.AnalyticsManager
import com.shalom.calendar.data.preferences.CalendarType
import com.shalom.calendar.data.preferences.Language
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.data.preferences.ThemePreferences
import com.shalom.calendar.ui.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingState(
    val currentPage: Int = 0,
    val selectedLanguage: Language = Language.AMHARIC,
    val selectedTheme: ThemeMode = ThemeMode.SYSTEM,
    val primaryCalendar: CalendarType = CalendarType.ETHIOPIAN,
    val showPublicHolidays: Boolean = true,
    val showOrthodoxHolidays: Boolean = true,
    val showMuslimHolidays: Boolean = false,
    val showOrthodoxDayNames: Boolean = false,
    val displayDualCalendar: Boolean = false,
    val isCompleted: Boolean = false,
    val onboardingStartTime: Long = System.currentTimeMillis()
)

const val TOTAL_ONBOARDING_PAGES = 5

// Page names for analytics
private val PAGE_NAMES = arrayOf(
    "Welcome",
    "Language Selection",
    "Theme Selection",
    "Holiday Selection",
    "Calendar Display"
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val settingsPreferences: SettingsPreferences,
    private val themePreferences: ThemePreferences,
    private val analyticsManager: AnalyticsManager
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    init {
        // Track onboarding start
        analyticsManager.logEvent(AnalyticsEvent.OnboardingStart)
    }

    fun setCurrentPage(page: Int) {
        _state.value = _state.value.copy(currentPage = page)
        // Track page view
        if (page in PAGE_NAMES.indices) {
            analyticsManager.logEvent(
                AnalyticsEvent.OnboardingPageView(
                    pageNumber = page,
                    pageName = PAGE_NAMES[page]
                )
            )
        }
    }

    fun setLanguage(language: Language) {
        _state.value = _state.value.copy(selectedLanguage = language)
        // Apply immediately
        viewModelScope.launch {
            settingsPreferences.setLanguage(language)
        }
        // Track selection
        analyticsManager.logEvent(
            AnalyticsEvent.OnboardingLanguageSelected(language.name)
        )
    }

    fun setTheme(theme: ThemeMode) {
        _state.value = _state.value.copy(selectedTheme = theme)
        // Apply immediately
        viewModelScope.launch {
            themePreferences.setThemeMode(theme)
        }
        // Track selection
        analyticsManager.logEvent(
            AnalyticsEvent.OnboardingThemeSelected(
                theme = theme.name,
                mode = theme.name
            )
        )
    }

    fun setPrimaryCalendar(calendarType: CalendarType) {
        _state.value = _state.value.copy(primaryCalendar = calendarType)
        // Apply immediately
        viewModelScope.launch {
            settingsPreferences.setPrimaryCalendar(calendarType)
        }
    }

    fun setPublicHolidays(enabled: Boolean) {
        _state.value = _state.value.copy(showPublicHolidays = enabled)
        // Apply immediately
        viewModelScope.launch {
            settingsPreferences.setShowPublicHolidays(enabled)
        }
    }

    fun setOrthodoxHolidays(enabled: Boolean) {
        _state.value = _state.value.copy(showOrthodoxHolidays = enabled)
        // Apply immediately
        viewModelScope.launch {
            settingsPreferences.setShowOrthodoxFastingHolidays(enabled)
        }
    }

    fun setMuslimHolidays(enabled: Boolean) {
        _state.value = _state.value.copy(showMuslimHolidays = enabled)
        // Apply immediately
        viewModelScope.launch {
            settingsPreferences.setShowMuslimHolidays(enabled)
        }
    }

    // Track holiday configuration when user moves away from holiday page
    private fun trackHolidayConfiguration() {
        val state = _state.value
        analyticsManager.logEvent(
            AnalyticsEvent.OnboardingHolidaysConfigured(
                publicEnabled = state.showPublicHolidays,
                orthodoxEnabled = state.showOrthodoxHolidays,
                muslimEnabled = state.showMuslimHolidays
            )
        )
    }

    fun setOrthodoxDayNames(enabled: Boolean) {
        _state.value = _state.value.copy(showOrthodoxDayNames = enabled)
        // Apply immediately
        viewModelScope.launch {
            settingsPreferences.setShowOrthodoxDayNames(enabled)
        }
    }

    fun setDisplayDualCalendar(enabled: Boolean) {
        _state.value = _state.value.copy(displayDualCalendar = enabled)
        // Apply immediately
        viewModelScope.launch {
            settingsPreferences.setDisplayDualCalendar(enabled)
        }
    }

    fun nextPage() {
        val currentPage = _state.value.currentPage

        // Track holiday configuration when leaving holiday page (page 3)
        if (currentPage == 3) {
            trackHolidayConfiguration()
        }

        // Track calendar configuration when leaving calendar page (page 4)
        if (currentPage == 4) {
            analyticsManager.logEvent(
                AnalyticsEvent.OnboardingCalendarSelected(
                    primaryCalendar = _state.value.primaryCalendar.name,
                    dualEnabled = _state.value.displayDualCalendar
                )
            )
        }

        if (currentPage < TOTAL_ONBOARDING_PAGES - 1) {
            _state.value = _state.value.copy(currentPage = currentPage + 1)
        }
    }

    fun previousPage() {
        val currentPage = _state.value.currentPage
        if (currentPage > 0) {
            _state.value = _state.value.copy(currentPage = currentPage - 1)
        }
    }

    /**
     * Complete onboarding - mark as completed
     * Settings are already saved as user makes selections
     */
    fun completeOnboarding() {
        android.util.Log.d("OnboardingViewModel", "completeOnboarding called")

        // Calculate duration
        val duration = System.currentTimeMillis() - _state.value.onboardingStartTime

        // Track completion
        analyticsManager.logEvent(
            AnalyticsEvent.OnboardingCompleted(duration = duration)
        )

        viewModelScope.launch {
            // Mark onboarding as completed
            settingsPreferences.setHasCompletedOnboarding(true)
            android.util.Log.d("OnboardingViewModel", "Onboarding marked as completed")
            // Update state to trigger navigation in the composable
            _state.value = _state.value.copy(isCompleted = true)
        }
    }

    /**
     * Skip onboarding - apply smart defaults and mark as completed
     */
    fun skipOnboarding() {
        android.util.Log.d("OnboardingViewModel", "skipOnboarding called")

        // Track skip
        analyticsManager.logEvent(
            AnalyticsEvent.OnboardingSkipped(lastPage = _state.value.currentPage)
        )

        viewModelScope.launch {
            // Apply smart defaults (in case user hasn't made any selections yet)
            settingsPreferences.setLanguage(Language.AMHARIC)
            themePreferences.setThemeMode(ThemeMode.SYSTEM)
            settingsPreferences.setPrimaryCalendar(CalendarType.ETHIOPIAN)
            settingsPreferences.setShowPublicHolidays(true)
            settingsPreferences.setShowOrthodoxFastingHolidays(true)
            settingsPreferences.setShowMuslimHolidays(false)
            settingsPreferences.setShowOrthodoxDayNames(false)
            settingsPreferences.setDisplayDualCalendar(false)
            settingsPreferences.setHasCompletedOnboarding(true)
            android.util.Log.d("OnboardingViewModel", "Default settings saved, onboarding completed")
            // Update state to trigger navigation in the composable
            _state.value = _state.value.copy(isCompleted = true)
        }
    }
}
