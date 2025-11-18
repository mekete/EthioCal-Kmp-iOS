package com.shalom.calendar.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.shalom.ethiopicchrono.ChronoField
import com.shalom.ethiopicchrono.ChronoUnit
import com.shalom.ethiopicchrono.EthiopicDate

/**
 * iOS implementation of Ethiopian date picker.
 * This is a simplified version - can be enhanced with full picker UI later.
 */
@Composable
actual fun EthiopicDatePickerDialog(
    selectedDate: EthiopicDate,
    onDateSelected: (EthiopicDate) -> Unit,
    onDismiss: () -> Unit
) {
    var currentDate by remember { mutableStateOf(selectedDate) }

    val monthNames = listOf(
        "Meskerem", "Tikimt", "Hidar", "Tahsas", "Tir", "Yekatit",
        "Megabit", "Miazia", "Ginbot", "Sene", "Hamle", "Nehase", "Pagume"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Ethiopian Date",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                // Display current selection
                val month = currentDate.get(ChronoField.MONTH_OF_YEAR)
                val day = currentDate.get(ChronoField.DAY_OF_MONTH)
                val year = currentDate.get(ChronoField.YEAR_OF_ERA)
                val monthName = monthNames.getOrElse(month - 1) { "Unknown" }

                Text(
                    text = "$monthName $day, $year",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(vertical = 24.dp)
                )

                // Simple increment/decrement controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = {
                        currentDate = currentDate.minus(1, ChronoUnit.DAYS)
                    }) {
                        Text("- Day")
                    }
                    TextButton(onClick = {
                        currentDate = currentDate.plus(1, ChronoUnit.DAYS)
                    }) {
                        Text("+ Day")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = {
                        currentDate = currentDate.minus(1, ChronoUnit.MONTHS)
                    }) {
                        Text("- Month")
                    }
                    TextButton(onClick = {
                        currentDate = currentDate.plus(1, ChronoUnit.MONTHS)
                    }) {
                        Text("+ Month")
                    }
                }

                // Action buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    TextButton(onClick = {
                        onDateSelected(currentDate)
                        onDismiss()
                    }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}
