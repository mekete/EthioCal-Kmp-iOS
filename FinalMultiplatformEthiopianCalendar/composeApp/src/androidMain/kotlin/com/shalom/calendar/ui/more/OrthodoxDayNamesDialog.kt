package com.shalom.calendar.ui.more

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shalom.calendar.R
import com.shalom.calendar.domain.model.HolidayType
import com.shalom.calendar.domain.model.OrthodoxDaysList

@Composable
@ExperimentalMaterial3Api
fun OrthodoxDayNamesDialog(
    onDismiss: () -> Unit
) {
    val orthodoxDays = remember { OrthodoxDaysList.getOrthodoxDays() }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(horizontal = 8.dp)) { // Title
            Text(text = stringResource(R.string.settings_orthodox_day_names_dialog_title), style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 16.dp))

            // List of Orthodox days
            LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 8.dp)) {
                items(orthodoxDays) { day ->
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), shape = RoundedCornerShape(8.dp)) {
                        Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.Top) { // Geez and Arabic date numbers
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = day.geezDate, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

                                Text(text = day.ethiopianDate, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Thin), color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            Box(modifier = Modifier
                                    .width(4.dp)
                                    .height(48.dp)
                                    .background(HolidayType.ORTHODOX_WORKING.getColor()))

                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(text = day.amharicName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)

                                Text(text = day.englishName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
                            }
                        }
                    }
                }
            }

            // OK Button
            Button(onClick = onDismiss, modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)) {
                Text(stringResource(R.string.button_ok))
            }
        }
    }
}
