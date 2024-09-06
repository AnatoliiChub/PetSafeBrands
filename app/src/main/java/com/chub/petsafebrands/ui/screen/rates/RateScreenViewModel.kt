package com.chub.petsafebrands.ui.screen.rates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chub.petsafebrands.Config.MAX_SELECTED_RATES
import com.chub.petsafebrands.UiRate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RateScreenViewModel @Inject constructor() : ViewModel() {

    private val baseAmount = MutableStateFlow("100.0")
    private val isLoading = MutableStateFlow(false)
    private val rates = MutableStateFlow(
        listOf(
            UiRate("EUR", 0.85),
            UiRate("JPY", 110.0),
            UiRate("GBP", 0.72),
            UiRate("AUD", 1.35),
            UiRate("CAD", 1.25),
            UiRate("CHF", 0.92),
            UiRate("CNY", 6.45),
            UiRate("SEK", 8.5),
            UiRate("NZD", 1.45)
        )
    )
    private val currentRate = MutableStateFlow(UiRate("USD", 1.0))
    private val selectedRates = MutableStateFlow(emptyList<UiRate>())

    val state = combine(
        isLoading,
        currentRate,
        combine(rates, baseAmount) { rates, baseAmount ->
            rates.map { rate -> rate.copy(value = rate.coefficient * (baseAmount.toDoubleOrNull() ?: 0.0)) }
        },
        baseAmount,
        selectedRates
    ) { isLoading, currentRate, rates, baseAmount, selectedRates ->

        RatesScreenState(
            isLoading = isLoading,
            currentRate = currentRate,
            rates = rates,
            baseAmount = baseAmount,
            selectedRates = selectedRates
        )
    }.flowOn(Dispatchers.Default)
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RatesScreenState(
                isLoading = false,
                currentRate = null,
                rates = mutableListOf(),
                baseAmount = ""
            )
        )

    fun onAction(action: RateScreenAction) {
        when (action) {
            is RateScreenAction.BaseRateChanged -> {
                currentRate.value = action.rate
            }

            is RateScreenAction.BaseAmountChanged -> {
                baseAmount.value = action.amount
            }

            is RateScreenAction.RateSelected -> {
                val selectedRates = state.value.selectedRates.toMutableList()
                if (selectedRates.contains(action.rate)) {
                    selectedRates.remove(action.rate)
                } else if (selectedRates.size >= MAX_SELECTED_RATES) {
                    selectedRates.removeLast()
                    selectedRates.add(action.rate)
                } else {
                    selectedRates.add(action.rate)
                }
                this.selectedRates.value = selectedRates
            }
        }
    }
}