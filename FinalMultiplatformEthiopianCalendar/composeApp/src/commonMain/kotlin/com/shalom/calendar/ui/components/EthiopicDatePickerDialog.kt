package com.shalom.calendar.ui.components

import androidx.compose.runtime.Composable
import com.shalom.ethiopicchrono.EthiopicDate

/**
 * Ethiopian date picker dialog composable.
 * Platform-specific implementations provide the actual UI.
 *
 * @param selectedDate The initially selected Ethiopian date (defaults to today)
 * @param onDateSelected Callback when a date is selected
 * @param onDismiss Callback when the dialog is dismissed
 */
@Composable
expect fun EthiopicDatePickerDialog(
    selectedDate: EthiopicDate = EthiopicDate.now(),
    onDateSelected: (EthiopicDate) -> Unit,
    onDismiss: () -> Unit
)
