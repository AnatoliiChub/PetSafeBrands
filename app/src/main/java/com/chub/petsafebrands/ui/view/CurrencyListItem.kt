package com.chub.petsafebrands.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chub.petsafebrands.R
import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.CurrencyRateItem
import com.chub.petsafebrands.toMoneyString

@Composable
fun CurrencyListItem(
    rate: CurrencyRateItem,
    selectedRates: List<CurrencyRateItem>,
    onClick: (CurrencyRateItem) -> Unit
) {
    Card(modifier = Modifier.padding(horizontal = 8.dp), colors = CardDefaults.cardColors(
        containerColor = if (selectedRates.firstOrNull { it.currency == rate.currency } != null) {
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(text = stringResource(R.string.currency))
                Text(text = rate.currency.toString(), fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.widthIn(min = 32.dp))
            Box(contentAlignment = Alignment.CenterEnd) {
                Text(text = rate.value.toMoneyString(), maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Preview
@Composable
fun CurrencyListItemPreview() {
    CurrencyListItem(
        rate = CurrencyRateItem(
            currency = Currency.USD,
            coefficient = 1.0,
            value = 12546456587567874636456456434646456457456452.0
        ),
        selectedRates = emptyList()
    ) {}
}