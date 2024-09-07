package com.chub.petsafebrands.ui.screen.rates

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chub.petsafebrands.Config.MAX_SELECTED_RATES
import com.chub.petsafebrands.R
import com.chub.petsafebrands.ui.view.BaseRateSelectionLayout
import com.chub.petsafebrands.ui.view.CurrencyListItem
import com.chub.petsafebrands.ui.view.ErrorLayout
import com.chub.petsafebrands.ui.view.LoadingLayout
import com.chub.petsafebrands.ui.view.TextFloatingButton
import com.chub.petsafebrands.ui.view.TopBar

@Composable
fun RatesScreen(viewModel: RateScreenViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Scaffold(topBar = { TopBar(title = R.string.rates) }, floatingActionButton = {
        if (state.value.contentState.selectedRates.size == MAX_SELECTED_RATES) {
            TextFloatingButton(text = R.string.show_details, icon = Icons.Default.Info) {}
        }
    }, floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (state.value.error.isNotEmpty()) {
                ErrorLayout(state.value.error) { viewModel.fetchRates() }
            } else {
                Content(viewModel, state.value.contentState)
            }
            if (state.value.isLoading) {
                LoadingLayout()
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun Content(
    viewModel: RateScreenViewModel,
    state: RatesContentState
) {
    with(state) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            stickyHeader {
                currentRate?.let {
                    BaseRateSelectionLayout(baseRate = currentRate,
                        baseAmount,
                        rates = rates,
                        { viewModel.onAction(RateScreenAction.BaseRateChanged(it)) },
                        { viewModel.onAction(RateScreenAction.BaseAmountChanged(it)) }
                    )
                }
            }
            items(rates, key = { it.currency }) { rate ->
                CurrencyListItem(rate = rate, selectedRates = selectedRates) {
                    viewModel.onAction(RateScreenAction.RateSelected(rate))
                }
            }
        }
    }
}




