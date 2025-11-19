package com.shalom.calendar.widget

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.AndroidRemoteViews
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.shalom.calendar.MainActivity
import com.shalom.calendar.R
import com.shalom.calendar.data.model.ADDIS_ABABA_ZONE_ID
import com.shalom.calendar.data.model.NEW_YORK_ZONE_ID
import com.shalom.calendar.data.preferences.SettingsPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import com.shalom.ethiopicchrono.EthiopicDate
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class CalendarGlanceWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<CalendarWidgetState> = CalendarWidgetStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                CalendarWidgetContent()
            }
        }
    }
}

@Composable
fun CalendarWidgetContent() {
    val context = LocalContext.current
    val packageName = context.packageName
    val widgetState = currentState<CalendarWidgetState>()

    val widgetData = getWidgetData(context, widgetState)

    Box(modifier = GlanceModifier.fillMaxSize().background(GlanceTheme.colors.background).padding(16.dp).clickable(actionStartActivity<MainActivity>()), contentAlignment = Alignment.TopCenter) {
        Column(modifier = GlanceModifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            val remoteViews = RemoteViews(packageName, R.layout.dual_clock_text_layout).apply {
                val (showDualClockVar, primaryTimezoneVar, secondaryTimezoneVar) = runBlocking {
                    try {
                        val settingsPreferences = SettingsPreferences(context)
                        Triple(settingsPreferences.displayTwoClocks.first(), settingsPreferences.primaryWidgetTimezone.first(), settingsPreferences.secondaryWidgetTimezone.first())
                    } catch (e: Exception) {
                        Triple(true, ADDIS_ABABA_ZONE_ID, NEW_YORK_ZONE_ID)
                    }
                }

                val primaryTimezoneTxt = primaryTimezoneVar.ifEmpty { ADDIS_ABABA_ZONE_ID }
                val secondaryTimezoneText = secondaryTimezoneVar.ifEmpty { NEW_YORK_ZONE_ID }

                val primaryTimezoneActual = try {
                    ZoneId.of(primaryTimezoneTxt)
                } catch (e: Exception) {
                    ZoneId.of(ADDIS_ABABA_ZONE_ID)
                }
                val secondaryTimezoneActual = try {
                    ZoneId.of(secondaryTimezoneText)
                } catch (e: Exception) {
                    ZoneId.systemDefault()
                }

                setString(R.id.txtc_primary_time, "setTimeZone", primaryTimezoneTxt)
                setString(R.id.txtv_primary_period, "setTimeZone", primaryTimezoneTxt)
                setString(R.id.txtc_secondary_time, "setTimeZone", secondaryTimezoneText)
                setString(R.id.txtv_secondary_period, "setTimeZone", secondaryTimezoneText) //
                val nowInEthio = EthiopicDate.now()
                val nowInPlace = LocalDate.now(ZoneId.systemDefault())
                val nowPrimary = LocalDate.now(primaryTimezoneActual)
                val nowSecondary = LocalDate.now(secondaryTimezoneActual) //
                val headerLeft = nowInEthio.format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy"))
                val headerCenter = nowInPlace.format(DateTimeFormatter.ofPattern("MMM dd", Locale.US))
                setTextViewText(R.id.txtv_header_left, headerLeft)
                setTextViewText(R.id.txtv_header_center, headerCenter) //


                val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US)
                setTextViewText(R.id.txtv_primary_date, nowPrimary.format(dateFormatter))
                setTextViewText(R.id.txtv_secondary_date, nowSecondary.format(dateFormatter))
                setTextViewText(R.id.txtv_local_label, extractCityNameFromTimezone(primaryTimezoneTxt))
                setTextViewText(R.id.txtv_world_label, extractCityNameFromTimezone(secondaryTimezoneText))

                // Control visibility of dual clock based on user preference
                setViewVisibility(R.id.rell_world_container, if (showDualClockVar) android.view.View.VISIBLE else android.view.View.GONE)
                setViewVisibility(R.id.divider_dual_clock, if (showDualClockVar) android.view.View.VISIBLE else android.view.View.GONE)

                val refreshIntent = Intent(context, DateChangeBroadcastReceiver::class.java).apply {
                    action = DateChangeBroadcastReceiver.ACTION_REFRESH_WIDGET
                }
                val refreshPendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                setOnClickPendingIntent(R.id.imgbtn_refresh, refreshPendingIntent)
            }

            AndroidRemoteViews(remoteViews)
            Spacer(modifier = GlanceModifier.height(16.dp))
            RemindersSection(events = widgetData.upcomingEvents)
        }
    }
}

@Composable
fun RemindersSection(events: List<WidgetEvent>) {
    Column(modifier = GlanceModifier.fillMaxWidth().padding(top = 8.dp)) {
        Text(text = "Upcoming Events", style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GlanceTheme.colors.onBackground), modifier = GlanceModifier.padding(bottom = 8.dp))

        if (events.isEmpty()) {
            Text(text = "No upcoming events", style = TextStyle(fontSize = 13.sp, color = GlanceTheme.colors.onSurfaceVariant), modifier = GlanceModifier.padding(start = 4.dp, top = 4.dp))
        } else {
            events.take(3).forEach { event ->
                EventItem(event = event)
            }
        }
    }
}

@Composable
fun EventItem(event: WidgetEvent) {
    val context = LocalContext.current
    Row(modifier = GlanceModifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) { // Colored vertical strip
        Box(modifier = GlanceModifier.width(3.dp).height(40.dp).background(try {
            com.shalom.calendar.domain.model.EventCategory.valueOf(event.category).getColor()
        } catch (e: IllegalArgumentException) {
            com.shalom.calendar.domain.model.EventCategory.PERSONAL.getColor()
        }), content = {

        })

        Spacer(modifier = GlanceModifier.width(8.dp))

        // Event details
        Column(modifier = GlanceModifier.defaultWeight()) {
            Text(text = event.title, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, color = GlanceTheme.colors.onBackground), maxLines = 1)

            Spacer(modifier = GlanceModifier.height(3.dp))

            Text(text = formatEventTime(event, context), style = TextStyle(fontSize = 12.sp, color = GlanceTheme.colors.onSurfaceVariant), maxLines = 1)
        }
    }
}

data class WidgetData(
    val currentDate: ZonedDateTime, val formattedDate: String, val nairobiTime: String, val localTime: String, val upcomingEvents: List<WidgetEvent>
)

fun getWidgetData(context: Context, state: CalendarWidgetState): WidgetData {
    val now = ZonedDateTime.now()
    val dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy")
    val formattedDate = now.format(dateFormatter)

    val use24HourFormat = runBlocking {
        try {
            val settingsPreferences = SettingsPreferences(context)
            settingsPreferences.use24HourFormat.first()
        } catch (e: Exception) {
            false
        }
    }

    val timePattern = if (use24HourFormat) "HH:mm" else "h:mm a"
    val timeFormatter = DateTimeFormatter.ofPattern(timePattern)

    val nairobiZone = ZoneId.of(ADDIS_ABABA_ZONE_ID)
    val nairobiTime = now.withZoneSameInstant(nairobiZone).format(timeFormatter)
    val localTime = now.format(timeFormatter)

    return WidgetData(currentDate = now, formattedDate = formattedDate, nairobiTime = nairobiTime, localTime = localTime, upcomingEvents = state.events)
}

fun formatEventTime(event: WidgetEvent, context: Context): String {
    val use24HourFormat = runBlocking {
        try {
            val settingsPreferences = SettingsPreferences(context)
            settingsPreferences.use24HourFormat.first()
        } catch (e: Exception) {
            false
        }
    }

    val timePattern = if (use24HourFormat) "HH:mm" else "h:mm a"
    val timeFormatter = DateTimeFormatter.ofPattern(timePattern)
    val dateFormatter = DateTimeFormatter.ofPattern("MMM d")

    val startTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(event.startTime), ZoneId.systemDefault())

    return if (event.isAllDay) {
        startTime.format(dateFormatter)
    } else {
        "${startTime.format(timeFormatter)} – ${startTime.format(dateFormatter)}"
    }
}

/**
 * Extracts a user-friendly city name from a timezone ID.
 * For example: "Africa/Addis_Ababa" -> "Addis Ababa"
 *              "America/New_York" -> "New York"
 *              "UTC" -> "UTC"
 */
fun extractCityNameFromTimezone(timezoneId: String): String {
    if (timezoneId.isEmpty()) {
        return "Local"
    }

    // Extract city name after the last "/"
    val cityName = timezoneId.substringAfterLast("/", timezoneId)

    // Replace underscores with spaces for better readability
    return cityName.replace("_", " ")
}
