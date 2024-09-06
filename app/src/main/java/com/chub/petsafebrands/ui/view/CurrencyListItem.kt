package com.chub.petsafebrands.ui.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chub.petsafebrands.UiRate
import java.util.Locale

@Composable
fun CurrencyListItem(rate: UiRate, selectedRates: List<UiRate>, onClick: (UiRate) -> Unit) {
    Card(modifier = Modifier.padding(horizontal = 8.dp), colors = CardDefaults.cardColors(
        containerColor = if (selectedRates.contains(rate)) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainer
        }
    ), onClick = {
        onClick(rate)
    }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Currency :")
                Text(text = rate.name, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(contentAlignment = Alignment.CenterEnd) {
                Log.d("CurrencyListItem", "Rate: ${rate.value}")
                Text(text = String.format(Locale.US, "%.2f", rate.value))
            }
        }
    }
}