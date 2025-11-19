package com.shalom.calendar.ui.components

import androidx.compose.runtime.Composable
import com.shalom.ethiopicchrono.EthiopicDate
import com.shalom.android.material.datepicker.EthiopicDatePickerDialog as AndroidEthiopicDatePickerDialog

@Composable
actual fun EthiopicDatePickerDialog(
    selectedDate: EthiopicDate,
    onDateSelected: (EthiopicDate) -> Unit,
    onDismiss: () -> Unit
) {
    AndroidEthiopicDatePickerDialog(
        selectedDate = selectedDate,
        onDateSelected = onDateSelected,
        onDismiss = onDismiss
    )
}
