package com.chub.petsafebrands.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chub.petsafebrands.R
import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.CurrencyRateItem
import com.chub.petsafebrands.isValidAmountOfMoney
import java.math.BigDecimal

@Composable
fun CurrencyHeader(
    baseRate: CurrencyRateItem,
    baseAmount: String,
    onAmountChanged: (String) -> Unit
) {
    Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.primaryContainer)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = baseRate.currency.name, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
            TextField(
                modifier = Modifier.width(160.dp),
                label = { Text(text = stringResource(R.string.amount)) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                value = baseAmount,
                isError = !baseAmount.isValidAmountOfMoney(),
                onValueChange = onAmountChanged
            )
        }
    }
}

@Preview
@Composable
fun BaseRateSelectionLayoutPreview() {
    CurrencyHeader(
        baseRate = CurrencyRateItem(Currency.EUR, BigDecimal.ONE),
        baseAmount = "100.0",
        onAmountChanged = {}
    )
}