package com.shalom.calendar.ui.holidaylist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shalom.calendar.domain.model.HolidayOccurrence
import com.shalom.calendar.domain.model.HolidayType
import com.shalom.ethiopicchrono.EthiopicDate
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.ChronoField
import java.util.Locale

@Composable
fun HolidayItem(
    modifier: Modifier = Modifier, holiday: HolidayOccurrence, monthNames: Array<String>, weekdayNamesShort: Array<String>, showCard: Boolean = false, onClick: (() -> Unit)? = null
) {
    val content: @Composable () -> Unit = {
        Row(modifier = Modifier
                .fillMaxWidth()
                .then(if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }), verticalAlignment = Alignment.CenterVertically) { // Day number
            Text(text = String.format(Locale.US, "%02d", holiday.holiday.ethiopianDay), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Thin)
            Spacer(modifier = Modifier.width(12.dp))

            // Colored vertical bar
            Box(modifier = Modifier
                    .size(4.dp, 30.dp)
                    .background(holiday.holiday.type.getColor()))

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                    Text(text = holiday.holiday.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)

                    if (holiday.holiday.type == HolidayType.MUSLIM_DAY_OFF || holiday.holiday.type == HolidayType.MUSLIM_WORKING) {
                        Spacer(modifier = Modifier.width(4.dp))

                        if (holiday.holiday.isVerified) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Verified", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = formatEthiopicDate(holiday.actualEthiopicDate, monthNames, weekdayNamesShort), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Text(text = formatGregorianDate(holiday.actualEthiopicDate, weekdayNamesShort), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontStyle = FontStyle.Italic)
                }
            }
        }
    }

    if (showCard) {
        Card(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Box(modifier = modifier.padding(8.dp)) {
                content()
            }
        }
    } else {
        Box(modifier = modifier) {
            content()
        }
    }
}

fun formatEthiopicDate(date: EthiopicDate, monthNames: Array<String>, weekdayNamesShort: Array<String>): String {
    val year = date.get(ChronoField.YEAR_OF_ERA)
    val month = date.get(ChronoField.MONTH_OF_YEAR)
    val day = date.get(ChronoField.DAY_OF_MONTH)

    val monthName = if (month in 1..13) monthNames[month - 1] else "Unknown"

    // Get day of week from the Gregorian equivalent
    val gregorianDate = LocalDate.from(date)
    val dayOfWeekIndex = (gregorianDate.dayOfWeek.value - 1) % 7 // Convert to 0-6 (Mon=0, Sun=6)
    val dayOfWeekName = weekdayNamesShort.getOrNull(dayOfWeekIndex) ?: ""

    return "$dayOfWeekName, $monthName $day, $year"
}

fun formatGregorianDate(date: EthiopicDate, weekdayNamesShort: Array<String>): String {
    val gregorianDate = LocalDate.from(date)

    // Get abbreviated English weekday name (e.g., "Mon", "Tue")
    val dayOfWeekName = gregorianDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)

    // Get abbreviated month name (e.g., "Jan", "Feb")
    val monthAbbr = gregorianDate.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)

    return "$dayOfWeekName, $monthAbbr ${gregorianDate.dayOfMonth}, ${gregorianDate.year}"
}

/**
 * Dialog that displays holiday details including name, description, and celebration
 */
@Composable
fun HolidayDetailsDialog(
    holiday: HolidayOccurrence, monthNames: Array<String>, onDismiss: () -> Unit
) {
//    AlertDialog(onDismissRequest = onDismiss, title = {
//        Text(text = holiday.holiday.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
//    }, text = {
//        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
//
//            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//                Text(text = formatEthiopicDate(holiday.actualEthiopicDate, monthNames), style = MaterialTheme.typography.bodyMedium)
//                Text(text = formatGregorianDate(holiday.actualEthiopicDate), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontStyle = FontStyle.Italic)
//
//            }
//
//            Column(modifier = Modifier
//                    .fillMaxWidth()
//                    .heightIn(max = 400.dp)
//
//
//                    .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
//                Text(text = holiday.holiday.description, style = MaterialTheme.typography.bodyMedium)
//
//                Text(text = holiday.holiday.celebration, style = MaterialTheme.typography.bodyMedium)
//            }
//
//            // Holiday type indicator
//            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                Box(modifier = Modifier
//                        .size(8.dp, 24.dp)
//                        .background(holiday.holiday.type.getColor(), RoundedCornerShape(2.dp)))
//                Text(text = if (holiday.holiday.isDayOff) "Public Holiday" else "Working Day", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
//            }
//        }
//    }, confirmButton = {
//        TextButton(onClick = onDismiss) {
//            Text("Close")
//        }
//    })
}
