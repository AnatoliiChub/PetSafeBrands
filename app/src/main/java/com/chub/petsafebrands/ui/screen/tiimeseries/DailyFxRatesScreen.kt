package com.chub.petsafebrands.ui.screen.tiimeseries

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chub.petsafebrands.R
import com.chub.petsafebrands.domain.model.Currency
import com.chub.petsafebrands.toMoneyString
import com.chub.petsafebrands.ui.view.CurrencyRateComparisonTable
import com.chub.petsafebrands.ui.view.ErrorLayout
import com.chub.petsafebrands.ui.view.LoadingLayout
import com.chub.petsafebrands.ui.view.TopBar

@Composable
fun DailyFxRatesScreen(onBack: () -> Unit, viewModel: DailyFxRatesViewModel = hiltViewModel()) {

    val state = viewModel.state.collectAsStateWithLifecycle()

    Scaffold(topBar = { TopBar(title = R.string.daily_fx_rates, onBack = onBack) }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), contentAlignment = Alignment.Center
        ) {
            with(state.value) {
                if (errorMessage.isNotEmpty()) {
                    ErrorLayout(errorMessage) { viewModel.fetchDailyRates() }
                } else {
                    Column(Modifier.fillMaxSize()) {
                        BaseAmountLayout(baseCurrency,baseAmount)
                        if (dayRates.isNotEmpty()) {
                            CurrencyRateComparisonTable(rates = dayRates)
                        }
                    }
                }
            }
            if (state.value.isLoading) {
                LoadingLayout()
            }
        }
    }
}

@Composable
private fun BaseAmountLayout(baseCurrency: Currency, baseAmount: Float) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        text = stringResource(
            R.string.base_amount,
            baseAmount.toDouble().toMoneyString(),
            baseCurrency.name
        ),
        fontWeight = FontWeight.Bold,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}