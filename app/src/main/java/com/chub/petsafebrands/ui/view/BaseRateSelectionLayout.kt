package com.chub.petsafebrands.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chub.petsafebrands.R
import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.CurrencyRateItem
import com.chub.petsafebrands.isValidAmountOfMoney
import java.math.BigDecimal

@Composable
fun BaseRateSelectionLayout(
    baseRate: CurrencyRateItem,
    baseAmount: String,
    rates: List<CurrencyRateItem>,
    onBaseRateSelected: (CurrencyRateItem) -> Unit,
    onAmountChanged: (String) -> Unit
) {
    Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.primaryContainer)) {

        var expanded by remember { mutableStateOf(false) }

        Row(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .clickable { expanded = true }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = baseRate.currency.name)
                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Localized description")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                rates.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            onBaseRateSelected(selectionOption)
                            expanded = false
                        }, text = {
                            Text(text = selectionOption.currency.name)
                        })
                }
            }
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
    BaseRateSelectionLayout(
        baseRate = CurrencyRateItem(Currency.EUR, BigDecimal.ONE),
        baseAmount = "100.0",
        rates = listOf(
            CurrencyRateItem(Currency.USD, BigDecimal(1.2)),
            CurrencyRateItem(Currency.GBP, BigDecimal(0.8)),
            CurrencyRateItem(Currency.JPY, BigDecimal(130.0)),
            CurrencyRateItem(Currency.AUD, BigDecimal(1.6)),
            CurrencyRateItem(Currency.CAD, BigDecimal(1.5)),
            CurrencyRateItem(Currency.CHF, BigDecimal(1.1)),
        ),
        onBaseRateSelected = {},
        onAmountChanged = {}
    )
}