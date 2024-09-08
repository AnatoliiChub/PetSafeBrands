package com.chub.petsafebrands.ui.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chub.petsafebrands.R
import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.CurrencyRateItem
import com.chub.petsafebrands.domain.pojo.DayFxRate
import com.chub.petsafebrands.domain.toFormattedString
import com.chub.petsafebrands.toMoneyString
import com.chub.petsafebrands.ui.screen.daily.state.SortBy
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CurrencyRateComparisonTable(rates: List<DayFxRate>, sortBy: SortBy, onSortByChanged: (SortBy) -> Unit) {
    LazyColumn(Modifier.fillMaxSize()) {
        stickyHeader {
            Row(Modifier.padding(horizontal = 8.dp)) {
                TableField(
                    text = stringResource(R.string.date),
                    onClick = { onSortByChanged(SortBy.ByDate) },
                    isSelected = sortBy == SortBy.ByDate
                )
                rates.firstOrNull()?.rates?.forEach {
                    TableField(
                        text = it.currency.name,
                        onClick = { onSortByChanged(SortBy.ByCurrency(it.currency)) },
                        isSelected = sortBy == SortBy.ByCurrency(it.currency)
                    )
                }
            }
        }
        items(rates) { rate ->
            Row(Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                TableField(text = rate.date.toFormattedString())
                rate.rates.forEach { rate -> TableField(text = rate.value.toMoneyString()) }
            }
        }
    }
}

@Preview
@Composable
fun CurrencyRateComparisonTablePreview() {
    CurrencyRateComparisonTable(
        rates = listOf(
            DayFxRate(
                Date(), listOf(
                    CurrencyRateItem(Currency.USD, 1.0),
                    CurrencyRateItem(Currency.JPY, 0.8),
                )
            ),
            DayFxRate(
                Date(), listOf(
                    CurrencyRateItem(Currency.USD, 1.1),
                    CurrencyRateItem(Currency.JPY, 0.9),
                )
            ),
            DayFxRate(
                Date(), listOf(
                    CurrencyRateItem(Currency.USD, 1.2),
                    CurrencyRateItem(Currency.JPY, 1.0),
                )
            ),
            DayFxRate(
                Date(), listOf(
                    CurrencyRateItem(Currency.USD, 1.3),
                    CurrencyRateItem(Currency.JPY, 1.1),
                )
            ),
            DayFxRate(
                Date(), listOf(
                    CurrencyRateItem(Currency.USD, 1.4),
                    CurrencyRateItem(Currency.JPY, 1.2),
                )
            ),
        ),
        sortBy = SortBy.ByDate,
        onSortByChanged = {}
    )
}