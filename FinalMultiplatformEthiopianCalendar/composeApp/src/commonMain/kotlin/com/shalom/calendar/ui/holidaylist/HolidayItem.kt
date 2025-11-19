package com.shalom.calendar.ui.holidaylist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shalom.calendar.domain.model.HolidayOccurrence
import com.shalom.calendar.domain.model.HolidayType
import com.shalom.ethiopicchrono.ChronoField
import com.shalom.ethiopicchrono.EthiopicDate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber

@Composable
fun HolidayItem(
    modifier: Modifier = Modifier,
    holiday: HolidayOccurrence,
    monthNames: List<String>,
    weekdayNamesShort: List<String>,
    showCard: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val content: @Composable () -> Unit = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Day number
            Text(
                text = holiday.holiday.ethiopianDay.toString().padStart(2, '0'),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Thin
            )
            Spacer(modifier = Modifier.width(12.dp))

            // Colored vertical bar
            Box(
                modifier = Modifier
                    .size(4.dp, 30.dp)
                    .background(holiday.holiday.type.getColor())
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = holiday.holiday.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )

                    if (holiday.holiday.type == HolidayType.MUSLIM_DAY_OFF ||
                        holiday.holiday.type == HolidayType.MUSLIM_WORKING) {
                        Spacer(modifier = Modifier.width(4.dp))

                        if (holiday.holiday.isVerified) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatEthiopicDate(holiday.actualEthiopicDate, monthNames, weekdayNamesShort),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = formatGregorianDate(holiday.actualEthiopicDate),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }

    if (showCard) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
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

fun formatEthiopicDate(date: EthiopicDate, monthNames: List<String>, weekdayNamesShort: List<String>): String {
    val year = date.get(ChronoField.YEAR_OF_ERA)
    val month = date.get(ChronoField.MONTH_OF_YEAR)
    val day = date.get(ChronoField.DAY_OF_MONTH)

    val monthName = monthNames.getOrElse(month - 1) { "Unknown" }

    // Get day of week from the Gregorian equivalent
    val gregorianDate = date.toLocalDate()
    // weekdayNamesShort is [Sun, Mon, Tue, Wed, Thu, Fri, Sat] - 0=Sunday
    // isoDayNumber: Monday=1, ..., Sunday=7
    // Convert to 0=Sunday, 1=Monday, ..., 6=Saturday
    val dayOfWeekIndex = gregorianDate.dayOfWeek.isoDayNumber % 7 // Sun=0, Mon=1, ..., Sat=6
    val dayOfWeekName = weekdayNamesShort.getOrElse(dayOfWeekIndex) { "" }

    return "$dayOfWeekName, $monthName $day, $year"
}

fun formatGregorianDate(date: EthiopicDate): String {
    val gregorianDate = date.toLocalDate()

    // Abbreviated weekday names
    val weekdayAbbr = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val dayOfWeekName = weekdayAbbr.getOrElse((gregorianDate.dayOfWeek.isoDayNumber - 1) % 7) { "" }

    // Abbreviated month names
    val monthAbbr = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    val monthName = monthAbbr.getOrElse(gregorianDate.monthNumber - 1) { "" }

    return "$dayOfWeekName, $monthName ${gregorianDate.dayOfMonth}, ${gregorianDate.year}"
}

/**
 * Dialog that displays holiday details including name, description, and celebration
 */
@Composable
fun HolidayDetailsDialog(
    holiday: HolidayOccurrence,
    monthNames: List<String>,
    onDismiss: () -> Unit
) {
    // TODO: Implement holiday details dialog
}
