package com.shalom.calendar.ui.more

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shalom.calendar.R
import com.shalom.calendar.data.preferences.Language

@Composable
fun LanguageDialog(
    currentLanguage: Language, onLanguageSelected: (Language) -> Unit, onDismiss: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismiss, title = {
        Text(text = stringResource(R.string.pref_language_dialog_title), style = MaterialTheme.typography.headlineSmall)
    }, text = {
        LazyColumn(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(Language.entries) { language ->
                LanguageItem(language = language, isSelected = language == currentLanguage, onClick = {
                    onLanguageSelected(language)
                    onDismiss()
                })
            }
        }
    }, confirmButton = {
        TextButton(onClick = onDismiss) {
            Text(stringResource(R.string.button_cancel))
        }
    }, shape = RoundedCornerShape(16.dp))
}

@Composable
private fun LanguageItem(
    language: Language, isSelected: Boolean, onClick: () -> Unit
) {
    Card(modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }, shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    })) {
        Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = language.displayName, style = MaterialTheme.typography.bodyLarge, color = if (isSelected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurface
            })
            if (isSelected) {
                RadioButton(selected = true, onClick = null, colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary))
            }
        }
    }
}
