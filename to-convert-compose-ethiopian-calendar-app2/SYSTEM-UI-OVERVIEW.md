# ComposeCalendarTwp - Application UI Overview

## UI Layer Architecture

This document provides a comprehensive overview of the Android UI layer built with **Jetpack Compose** for the ComposeCalendarTwp calendar application.

### Key Characteristics
- **Framework**: Jetpack Compose (declarative UI)
- **Language**: Kotlin
- **State Management**: StateFlow + MutableStateFlow + Recomposition
- **Navigation**: Bottom navigation + argument passing
- **Theming**: Material 3 design system with 5 color schemes
- **Accessibility**: Content descriptions, semantic properties
- **Localization**: Multi-language support (Amharic, English, Oromiffa, Tigrigna, French)

---

## UI Layer Structure

```
java/com/shalom/calendar/ui/
├── month/                    # Month calendar view (primary calendar)
│   ├── MonthCalendarScreen.kt
│   ├── MonthCalendarViewModel.kt
│   └── MonthCalendarUiState.kt
├── event/                    # Event creation/management screen
│   ├── EventScreen.kt
│   ├── EventViewModel.kt
│   ├── EventUiState.kt
│   └── AddEventDialog.kt
├── converter/               # Date converter tool
│   ├── DateConverterScreen.kt
│   └── DateConverterViewModel.kt
├── holidaylist/             # Holiday list view
│   ├── HolidayListScreen.kt
│   ├── HolidayListViewModel.kt
│   ├── HolidayListUiState.kt
│   ├── HolidayItem.kt
│   └── HolidayDetailsDialog.kt
├── onboarding/              # Initial setup flow
│   ├── OnboardingScreen.kt
│   ├── OnboardingViewModel.kt
│   ├── WelcomePage.kt
│   ├── LanguageSelectionPage.kt
│   ├── ThemeSelectionPage.kt
│   ├── HolidaySelectionPage.kt
│   ├── CalendarDisplayPage.kt
│   └── OnboardingComponents.kt
├── more/                    # Settings & configuration
│   ├── MoreScreen.kt
│   ├── SettingsViewModel.kt
│   ├── ThemeViewModel.kt
│   ├── ThemeSettingScreen.kt
│   ├── LanguageDialog.kt
│   ├── HolidaysDisplayDialog.kt
│   ├── OrthodoxDayNamesDialog.kt
│   └── WidgetSettingsDialog.kt
├── theme/                   # Theme & styling
│   ├── AppTheme.kt         # Theme enums
│   ├── Theme.kt            # Compose theme configuration
│   ├── Color.kt            # Color definitions
│   └── Type.kt             # Typography definitions
├── components/              # Reusable composables
│   ├── MonthHeaderItem.kt
│   ├── AutocompleteTextField.kt
│   └── ...other components
└── permissions/             # Permission-related UI
    ├── PermissionComponents.kt
    ├── NotificationPermissionRationaleDialog.kt
    ├── ExactAlarmPermissionRationaleDialog.kt
    ├── PermissionDeniedBanner.kt
    └── PermissionFixDialog.kt
```

---

## Screen Hierarchy & Navigation

### Main Navigation (Bottom Tab Bar)

The app uses bottom navigation with 5 main screens:

```
MainActivity
├── [Tab 1] Month Calendar Screen (MonthCalendarScreen)
├── [Tab 2] Event Screen (EventScreen)
├── [Tab 3] Holiday List Screen (CalendarItemListScreen)
├── [Tab 4] Date Converter Screen (DateConverterScreen)
└── [Tab 5] More Screen (MoreScreen)
```

### Navigation Flow

```
App Launch
    ↓
CalendarApplication.onCreate()
    ↓
MainActivity.onCreate()
    ↓
Has completed onboarding?
├─ NO → OnboardingScreen (5 pages)
│   └── On complete → SplashScreen → Animated transition
│
└─ YES → SplashScreen
    └── After animation → Month calendar view
```

---

## Detailed Screen Descriptions

### 1. **MonthCalendarScreen** (`ui/month/`)

**Purpose**: Primary calendar view displaying month grid in either Ethiopian or Gregorian calendar.

**Key Features**:
- **Dual Calendar Support**: Display both Ethiopian and Gregorian dates in same cell
- **Horizontal Pager**: ±60 months (5 years) from current date = 121 pages total
- **Date Grid**: 42-cell grid (6 weeks × 7 days) to accommodate month boundaries
- **Holiday Display**: Shows holiday indicators below each cell with color coding
- **Date Click Dialog**: Clicking a date shows "Date Details Dialog" with both calendars
- **Notification Banner**: Appears after 2 weeks if notifications not enabled
- **"Today" Button**: Quick navigation to current date

**ViewModel: MonthCalendarViewModel**
```kotlin
@HiltViewModel
class MonthCalendarViewModel @Inject constructor(
    holidayRepository, eventRepository, settingsPreferences
)
```
- Manages 121 pages with ±60 month offset
- Loads month data lazily per page
- Combines holidays, events, and user preferences
- Handles Ethiopian/Gregorian month grid generation
- State Flow exports:
  - `primaryCalendar`: Selected primary calendar type
  - `displayDualCalendar`: Whether to show dual calendars
  - `selectedDate`: Currently selected date
  - `showDateDetailsDialog`: Date details popup state

**UI State: MonthCalendarUiState**
```kotlin
sealed class MonthCalendarUiState {
    data object Loading : MonthCalendarUiState()
    data class Success(...) : MonthCalendarUiState()
    data class Error(val message: String) : MonthCalendarUiState()
}
```

**Key Composables**:
- `MonthCalendarScreen`: Top-level composable
- `MonthCalendarPage`: Page content for pager
- `MonthCalendarContent`: Grid layout with dates
- `DateCell`: Individual date cell with holiday/event indicators
- `PrimaryOnlyCellContent`: Single calendar display
- `DualDateCellContent`: Dual calendar with stacked display
- `DateDetailsDialog`: Shows date info and navigation to events
- `NotificationReminderBanner`: Permission request banner

**Display Modes**:
1. **Ethiopian Only**: Single calendar (default)
2. **Gregorian Only**: Single calendar alternative
3. **Dual (Ethiopian Primary)**: Dual calendar with Ethiopian as main
4. **Dual (Gregorian Primary)**: Dual calendar with Gregorian as main

**Holiday Display**:
- Color-coded bottom bar indicates holiday type
- Takes 1st holiday if multiple on same date
- Can click to see holiday details in dialog

---

### 2. **EventScreen** (`ui/event/`)

**Purpose**: Create, view, edit, and delete events with date filtering and reminder management.

**Key Features**:
- **Event List**: LazyColumn with date filtering (start/end date)
- **Date Filter Section**: Shows current filter status and allows date picker
- **Event Cards**: Display with Ethiopian date, time, relative time, category
- **Create/Edit Events**: AddEventDialog with full form
- **Delete Events**: Confirmation dialog before deletion
- **Permission Management**: Notification & exact alarm permission handling
- **Filter Buttons**: "Filter" / "Show All" toggle

**ViewModel: EventViewModel**
```kotlin
@HiltViewModel
class EventViewModel @Inject constructor(
    eventRepository, permissionManager, settingsPreferences
)
```
- Manages event CRUD operations
- Tracks filter dates and active filters
- Manages dialog open/close states
- Handles preset dates from navigation
- State Flow exports:
  - `uiState`: Loading/Success/Error with events list
  - `isDialogOpen`: Event creation/edit dialog visibility

**UI State: EventUiState**
```kotlin
sealed class EventUiState {
    data object Loading : EventUiState()
    data class Success(
        val events: List<EventInstance>,
        val filterStartDate: LocalDate?,
        val filterEndDate: LocalDate?,
        val isDialogOpen: Boolean,
        val editingEvent: EventInstance?,
        val presetDate: EthiopicDate?
    ) : EventUiState()
    data class Error(val message: String) : EventUiState()
}
```

**Key Composables**:
- `EventScreen`: Top-level with scaffold
- `EventList`: LazyColumn of events with filter section
- `DateFilterSection`: Start/end date pickers and status text
- `DateInputField`: Clickable date input card
- `EventCard`: Individual event display with details
- `AddEventDialog`: Full event creation/editing form
- `EmptyEventsPlaceholder`: No events state
- `ErrorMessage`: Error display

**Permissions Handled**:
- `POST_NOTIFICATIONS` (Android 13+): Permission request on first visit
- `SCHEDULE_EXACT_ALARM` (Android 12+): Rationale dialog before opening settings
- Persistent "don't ask for X days" flag in DataStore

**Date Filtering**:
- Supports range filtering (start and/or end dates)
- Ethiopian date pickers using `EthiopicDatePickerDialog`
- Converts between EthiopicDate and LocalDate internally
- Extreme dates (1900, 2100) represent "show all"

---

### 3. **HolidayListScreen** (`ui/holidaylist/`)

**Function**: `CalendarItemListScreen` - Display holidays by year with filtering options

**Purpose**: Browse holidays for any year with configurable display types.

**Key Features**:
- **Year Navigation**: Previous/next year buttons
- **Holiday Cards**: Grouped by month with date, name, and type
- **Holiday Details**: Click to show full holiday information
- **Info Dialog**: Help text explaining holiday types
- **Filter Options**: Holiday type filtering (public, orthodox, muslim)

**ViewModel: CalendarItemListViewModel**
- Manages current year state
- Filters holidays based on user preferences
- Loads holidays from repository
- State Flow exports:
  - `uiState`: Loading/Success/Error with filtered holidays
  - `currentYear`: Currently displayed year

**UI State: CalendarItemListUiState**
```kotlin
sealed class CalendarItemListUiState {
    data object Loading : CalendarItemListUiState()
    data class Success(
        val filteredCalendarItems: List<HolidayOccurrence>,
        val currentYear: Int
    ) : CalendarItemListUiState()
    data class Error(val message: String) : CalendarItemListUiState()
}
```

**Key Composables**:
- `CalendarItemListScreen`: Top-level screen
- `CalendarItemListContent`: Month header + holiday list
- `HolidayItem`: Individual holiday card with date and type
- `HolidayDetailsDialog`: Extended holiday information
- `HolidayConfigureHintDialog`: Help dialog

**Holiday Types with Color Coding**:
- **National Day Off** (NATIONAL_DAY_OFF): Red
- **Orthodox Day Off** (ORTHODOX_DAY_OFF): Green
- **Orthodox Working** (ORTHODOX_WORKING): Light green
- **Muslim Day Off** (MUSLIM_DAY_OFF): Blue
- **Muslim Working** (MUSLIM_WORKING): Light blue

---

### 4. **DateConverterScreen** (`ui/converter/`)

**Purpose**: Convert between Gregorian, Ethiopian, and Hirji calendars with difference calculator.

**Key Features**:
- **Conversion Directions**:
  - Gregorian ↔ Ethiopian
  - Ethiopian ↔ Gregorian
- **Date Selection**: Manual input or date picker
- **Date Difference Calculator**: Days between two dates
- **Result Dialogs**: Show conversion results with formatting

**ViewModel: DateConverterViewModel**
```kotlin
@HiltViewModel
class DateConverterViewModel @Inject constructor(
    // dependencies
)
```
- Performs date conversions
- Calculates date differences
- Manages result dialog states
- State Flow exports:
  - `uiState`: Current converter state
  - `ethiopianResult`, `gregorianResult`: Conversion results
  - `diffResult`: Difference calculator result

**Key Composables**:
- `DateConverterScreen`: Main container with scroll
- Conversion input sections with pickers
- Difference calculator input
- Result dialogs with formatted output
- `EthiopicDatePickerDialog`: Custom Ethiopian date picker

**Supported Conversions**:
- Gregorian (any year) → Ethiopian
- Ethiopian (any year) → Gregorian
- Date difference calculations with result formatting

---

### 5. **OnboardingScreen** (`ui/onboarding/`)

**Purpose**: First-run setup guide for new users with 5 sequential pages.

**Pages**:
1. **Welcome Page**: Introduction with "Get Started" button
2. **Language Selection**: Choose app language (Amharic, English, Oromiffa, Tigrigna, French)
3. **Theme Selection**: Light/Dark/System mode + color (Blue, Red, Green, Purple, Orange)
4. **Holiday Selection**: Toggle public, orthodox, and muslim holidays
5. **Calendar Display**: Choose primary calendar and dual calendar display option

**ViewModel: OnboardingViewModel**
```kotlin
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    settingsPreferences
)
```
- Manages current page state
- Saves preferences immediately on selection
- Handles skip/completion logic
- Smart defaults if user skips
- State Flow: `state: OnboardingState`

**UI Pattern**:
- **HorizontalPager**: No swipe navigation, button-controlled only
- **Top Bar**: Back button appears after page 0
- **Bottom Bar**: Page indicators + Next/Done button
- **Animations**: Spring animations for page transitions

**Key Composables**:
- `OnboardingScreen`: Pager container
- `WelcomePage`: Intro page
- `LanguageSelectionPage`: Language radio buttons
- `ThemeSelectionPage`: Theme grid + mode selection
- `HolidaySelectionPage`: Checkboxes for holiday types
- `CalendarDisplayPage`: Radio buttons for calendar options
- `OnboardingTopBar`, `OnboardingBottomBar`: Navigation controls

---

### 6. **MoreScreen** (`ui/more/`)

**Purpose**: Settings, configuration, theme selection, and app information.

**Sections**:
1. **Language** (LanguageDialog): App language selection
2. **Display** (ThemeSettingScreen): Light/dark mode selection
3. **Theme** (AppTheme selection): Color scheme (Blue, Red, Green, Purple, Orange)
4. **Calendar** (HolidaysDisplayDialog): Holiday type toggles
5. **Orthodox Day Names**: Show/hide custom day names
6. **Widget Settings** (WidgetSettingsDialog): Timezone configuration for widget clocks
7. **Notifications**: Open app notification settings
8. **Share**: Share app via intent
9. **About**: App version and information

**ViewModels**:
- **SettingsViewModel**: Calendar and display preferences
- **ThemeViewModel**: Theme color and mode selection

**Key Composables**:
- `MoreScreen`: Main settings list
- `SettingItem`: Reusable settings row with icon, title, action
- `LanguageDialog`: Radio button selection
- `ThemeSettingScreen`: Light/Dark/System mode toggle
- `HolidaysDisplayDialog`: Holiday type checkboxes
- `OrthodoxDayNamesDialog`: Day name format selection
- `WidgetSettingsDialog`: Timezone autocomplete fields

---

## Theme & Styling System

### Theme Enums (`ui/theme/AppTheme.kt`)

```kotlin
enum class AppTheme(val displayName: String) {
    BLUE("Blue"),
    RED("Red"),
    GREEN("Green"),
    PURPLE("Purple"),
    ORANGE("Orange")
}

enum class ThemeMode(val displayName: String) {
    SYSTEM("System Default"),
    LIGHT("Light"),
    DARK("Dark")
}
```

### Theme Configuration (`ui/theme/Theme.kt`)

- **Material 3 Design System**: Color schemes for each theme
- **Light & Dark Variants**: Each color has light/dark colors
- **Dynamic Colors**: (if Material You enabled)
- **Typography**: Custom font styles using Material 3 type scale
- **Shapes**: Rounded corners configured globally

### Color Definitions (`ui/theme/Color.kt`)

Define primary, secondary, tertiary colors for each of the 5 themes in both light and dark modes.

### Typography (`ui/theme/Type.kt`)

Defines Material 3 typography with custom font families if needed.

### Theme Application

Applied at MainActivity level:
```kotlin
AppTheme(theme = themeViewModel.currentTheme, mode = themeViewModel.themeMode) {
    MainNavigation(...)
}
```

---

## Reusable Components

### 1. **MonthHeaderItem** (`ui/components/MonthHeaderItem.kt`)

**Purpose**: Month navigation header with prev/next buttons and center text.

**Props**:
- `centerText: String` - Month and year display
- `prevButtonLabel: String` - Previous button text (month name)
- `nextButtonLabel: String` - Next button text (month name)
- `onPrevClick`, `onNextClick`: Click handlers
- `onCenterClick`: Center text click handler
- `currentPage`, `prevButtonEnabled`, `nextButtonEnabled`: State props

**Used in**: MonthCalendarScreen

---

### 2. **AutocompleteTextField** (`ui/components/AutocompleteTextField.kt`)

**Purpose**: Timezone selection with autocomplete suggestions.

**Props**:
- `value: String` - Current search text
- `onValueChange: (String) -> Unit` - Text change callback
- `onTimezoneSelected: (TimeZoneData) -> Unit` - Selection callback
- `timeZoneList: List<TimeZoneData>` - Available timezones
- `label`, `placeholder`: Text field labels

**Features**:
- Real-time filtering (case-insensitive)
- Searches in city names and display names
- Limits to 10 suggestions
- Dropdown menu with scrolling

**Used in**: WidgetSettingsDialog (timezone selection)

---

### 3. **HolidayItem** (`ui/holidaylist/HolidayItem.kt`)

**Purpose**: Display single holiday with date, name, and type.

**Props**:
- `holiday: HolidayOccurrence` - Holiday data
- `monthNames: Array<String>` - For date formatting
- `weekdayNamesShort: Array<String>` - For date formatting
- `showCard: Boolean` - Card styling vs list styling
- `onClick: () -> Unit` - Click handler

**Styling**:
- Colored left border by holiday type
- Large date number on left
- Holiday name and type in body
- Click to show details dialog

**Used in**: HolidayListScreen, MonthCalendarScreen holiday section

---

## State Management Pattern

### ViewModel Architecture

Each screen has a dedicated ViewModel managing:

```
ViewModel
├── Repositories (injected)
├── State (Mutable/State Flow)
├── Methods (user actions)
└── Flows (observe preferences)
```

**Example Pattern (EventViewModel)**:
```kotlin
@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val permissionManager: PermissionManager,
    private val settingsPreferences: SettingsPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<EventUiState>(EventUiState.Loading)
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    // User action methods
    fun createEvent(...) {
        viewModelScope.launch {
            try {
                // perform action
                _uiState.value = EventUiState.Success(...)
            } catch (e: Exception) {
                _uiState.value = EventUiState.Error(e.message)
            }
        }
    }
}
```

### Data Flow

```
Repository (Flow<Data>)
    ↓
ViewModel (Transform/Filter)
    ↓
StateFlow (UI observable state)
    ↓
Composable (collectAsState())
    ↓
Recomposition (on state change)
```

### State Scope

- **Local State**: UI-only (dialogs, form input) → `remember { mutableStateOf() }`
- **Screen State**: ViewModel managed → `StateFlow<UiState>`
- **App-wide State**: Preferences → `SettingsPreferences` (DataStore)
- **Temporary State**: `LaunchedEffect` for one-time operations

---

## Permission & Dialog Management

### Permission Handling (`ui/permissions/`)

**Components**:
- `NotificationPermissionRationaleDialog`: Explains notification benefit
- `ExactAlarmPermissionRationaleDialog`: Explains exact alarm benefit
- `PermissionDeniedBanner`: Shows missing permissions with action button
- `PermissionFixDialog`: Lists missing permissions with action buttons

**Flow**:
1. First EventScreen visit → Show notification rationale
2. User grants → Permission granted, dismiss banner
3. User denies → Show "don't ask" checkbox option
4. User selects "don't ask for 1 week" → Set timestamp in DataStore
5. When creating event with reminder → Show PermissionFixDialog if missing

**Permission Flags**:
- `notificationPermissionDontAskUntil`: Timestamp for "don't ask" period
- Checked before showing rationale dialogs

---

## Dialogs & Popups

### Common Dialog Patterns

**AlertDialog** (Material 3):
- Used for confirmations, info, selections
- Custom content in `text` block
- `confirmButton` and `dismissButton`

**EthiopicDatePickerDialog** (Custom):
- Ethiopian calendar date selection
- Loaded from custom library
- Used in event/converter screens

**DropdownMenu**:
- Timezone selector with autocomplete
- Scrollable suggestions list

---

## Composition Performance Optimization

### Techniques Used

1. **Key-based Items**: LazyVerticalGrid uses key-based items for efficient recomposition
   ```kotlin
   items(count = state.dateList.size, key = { index ->
       val date = state.dateList[index]
       "${date.year}-${date.month}-${date.day}"
   }) { index ->
       DateCell(...)
   }
   ```

2. **Remember with Key**: Prevents unnecessary recalculations
   ```kotlin
   val holidayMap = remember(state.calendarItems) {
       state.calendarItems.groupBy { ... }
   }
   ```

3. **Lazy Pager Loading**: Only loads current page data
   ```kotlin
   val monthData by remember(page) {
       viewModel.loadMonthDataForPage(page)
   }.collectAsState()
   ```

4. **Separate State Objects**: Prevents cascading recompositions
   ```kotlin
   val primaryCalendar by viewModel.primaryCalendar.collectAsState()
   val displayDualCalendar by viewModel.displayDualCalendar.collectAsState()
   val selectedDate by viewModel.selectedDate.collectAsState()
   ```

5. **Flow Combination**: Combine multiple flows before collecting
   ```kotlin
   combine(flow1, flow2, flow3, ...) { values ->
       computeUiState(values)
   }.collectAsState()
   ```

---

## Accessibility Features

### Semantic Properties

- **Content Descriptions**: All icons have contentDescription strings
- **Semantic Modifiers**: `semantics { contentDescription = ... }`
- **Accessibility Names**: Screen titles and important elements labeled

### Example (DateCell):
```kotlin
val contentDesc = buildString {
    append(formatEthiopianDateFull(date, monthNames))
    if (isToday) append(", Today")
    if (isSelected) append(", Selected")
    if (holidays.isNotEmpty()) {
        val holidayNames = holidays.joinToString(", ") { it.holiday.title }
        append(", Holidays: $holidayNames")
    }
}

Box(modifier = Modifier.semantics {
    this.contentDescription = contentDesc
}) { ... }
```

### Window Insets

Handles:
- Navigation bar padding
- Status bar padding
- Safe area for edge-to-edge display

---

## Localization in UI

### String Resources

- All UI strings in `res/values/strings.xml`
- Translated copies in `res/values-am/` (Amharic), etc.
- Language switching recreates Activity

### Formatted Strings

Example from EventScreen:
```kotlin
stringResource(R.string.showing_from_to, startDate, endDate)
```

### Array Resources

Ethiopian month names, weekday names loaded via:
```kotlin
val monthNames = stringArrayResource(R.array.ethiopian_months)
val weekdayNames = stringArrayResource(R.array.weekday_names_full)
```

---

## Navigation Patterns

### Bottom Navigation

MainActivity uses bottom navigation bar with 5 destinations:
- Month (Calendar icon)
- Event (Event icon)
- Holiday (Holiday icon)
- Converter (Converter icon)
- More (Settings icon)

### Argument Passing

**Example**: MonthCalendarScreen → EventScreen
```kotlin
// From DateDetailsDialog
viewModel.hideDateDetailsDialog()
navController.navigate("event?selectedDate=$dateString&hasEvents=$selectedDateHasEvents")

// In EventScreen
@Composable
fun EventScreen(
    event: String,
    selectedDate: String? = null,
    hasEvents: Boolean = false,
    ...
) {
    LaunchedEffect(selectedDate, hasEvents, uiState) {
        if (selectedDate != null && uiState is EventUiState.Success) {
            val parts = selectedDate.split("-")
            val year = parts[0].toInt()
            val month = parts[1].toInt()
            val day = parts[2].toInt()
            // Handle navigation
        }
    }
}
```

---

## Common UI Patterns

### Loading States

```kotlin
when (val state = uiState) {
    is EventUiState.Loading -> {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
    is EventUiState.Success -> {
        // Show content
    }
    is EventUiState.Error -> {
        ErrorMessage(message = state.message)
    }
}
```

### Empty States

Each screen with lists shows placeholder when empty:
- Icon (large, muted)
- Title text
- Descriptive text
- Optional action button

### Filter/Status Display

Screens using filters show current filter state:
- Text indicating what's being shown
- "Filter" / "Show All" toggle button
- Individual clear buttons per filter

---

## Layout Patterns

### Top App Bar Pattern

```kotlin
Scaffold(
    topBar = {
        TopAppBar(
            title = { Text(...) },
            navigationIcon = { Icon(...) },
            actions = { /* icon buttons */ },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        )
    },
    floatingActionButton = { /* FAB */ },
    bottomBar = { /* bottom nav */ }
) { padding ->
    Column(modifier = Modifier.padding(top = padding.calculateTopPadding())) {
        // content
    }
}
```

### List Layouts

- **LazyColumn**: Vertical scrolling lists (events, holidays)
- **LazyVerticalGrid**: Grid layouts with fixed columns (calendar dates)
- **HorizontalPager**: Pageable horizontal scrolling (onboarding, month view)

### Spacing & Padding

- Consistent 16.dp padding for screen edges
- 8.dp spacing between items
- 4-8.dp spacing within cards
- 2.dp divider thickness

---

## Testing Considerations

### Testability Features

1. **Hilt Injection**: Easy to mock dependencies in ViewModel
2. **StateFlow**: Can collect and assert state changes
3. **Composable Preview**: @Preview on composables for UI testing
4. **Separated Logic**: ViewModels are testable independently
5. **Repository Pattern**: Repositories can be mocked

### Example Test (ViewModel):
```kotlin
@Test
fun testEventCreation() {
    val viewModel = EventViewModel(mockRepository)
    viewModel.createEvent(...)

    runTest {
        val state = viewModel.uiState.first { it is EventUiState.Success }
        assert(state.events.isNotEmpty())
    }
}
```

---

## Future UI Enhancements

Based on code comments and architecture:

1. **Event Details View**: Click event to show full details
2. **Event Search**: Search by title, description, category
3. **Recurring Event Editor**: Visual RRULE editor
4. **Multiple Event Colors**: Category-based event coloring
5. **Gesture Support**: Swipe to navigate months
6. **Widget Customization**: User-configurable widget layout
7. **Night Light Theme**: Specialized theme for evening use
8. **Animated Transitions**: Shared element transitions
9. **Event Reminders**: Visual/sound configuration per event
10. **Calendar Sync UI**: Google Calendar sync status display

---

## Best Practices Followed

1. **Single Responsibility**: Each composable has one main purpose
2. **State Hoisting**: State managed at appropriate level
3. **Composition Over Inheritance**: Reusable composables
4. **Immutable Data**: Data classes marked as data class
5. **Null Safety**: Kotlin null safety throughout
6. **Resource Efficiency**: LazyColumn/Grid for large lists
7. **Material 3 Compliance**: Follows Material Design guidelines
8. **Accessibility**: Semantic properties and content descriptions
9. **Localization**: All strings in resource files
10. **Theme Flexibility**: Dynamic theme switching without restart (mostly)

---

## Troubleshooting Common UI Issues

### Issue: Pager Not Updating
**Solution**: Ensure `pageCount` is stable or use `rememberPagerState` correctly

### Issue: Keyboard Not Closing
**Solution**: Use `FocusManager.clearFocus()` or dismiss keyboard explicitly

### Issue: Recomposition Performance
**Solution**: Use `remember` with proper keys, avoid creating objects in composable body

### Issue: Dialog Not Showing
**Solution**: Ensure state is correct and dialog composable is in composition tree

---

## File Size & Composition Metrics

- **MonthCalendarScreen.kt**: ~880 lines (largest, includes helper functions)
- **EventScreen.kt**: ~790 lines
- **Theme.kt**: ~300 lines
- **MoreScreen.kt**: ~400 lines
- **OnboardingScreen.kt**: ~200 lines
- **Total UI Layer**: ~35 Kotlin files, ~5500+ lines of UI code

---

## Performance Metrics

- **Month Grid Rendering**: 42 cells rendered via LazyVerticalGrid
- **Pager Load**: Lazy loading per page (only current + adjacent pages)
- **List Animations**: Spring animations with DampingRatioLowBouncy
- **Theme Change**: 50-100ms Activity recreation (not runtime switch)
- **Dialog Appearance**: 300ms delay before permission request (avoid conflicts)

---

*Generated: 2025-11-16*
*Application: ComposeCalendarTwp*
*UI Framework: Jetpack Compose*
*Package: java/com/shalom/calendar/ui*
