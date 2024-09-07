package com.chub.petsafebrands.ui.screen.rates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chub.petsafebrands.Config.MAX_SELECTED_RATES
import com.chub.petsafebrands.data.FixerApiException
import com.chub.petsafebrands.domain.GetFxRatesUseCase
import com.chub.petsafebrands.domain.model.Currency
import com.chub.petsafebrands.domain.model.UiRate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RateScreenViewModel @Inject constructor(val getFxRatesUseCase: GetFxRatesUseCase) : ViewModel() {

    private val baseAmount = MutableStateFlow("100.0")
    private val isLoading = MutableStateFlow(false)
    private val rates = MutableStateFlow(emptyList<UiRate>())
    private val currentRate = MutableStateFlow(UiRate(Currency.EUR, 1.0))
    private val selectedRates = MutableStateFlow(emptyList<UiRate>())
    private val error = MutableStateFlow("")
    private val contentState = combine(
        currentRate,
        combine(rates, baseAmount) { rates, baseAmount ->
            rates.map { rate -> rate.copy(value = rate.coefficient * (baseAmount.toDoubleOrNull() ?: 0.0)) }
        },
        baseAmount,
        selectedRates
    ) { currentRate, rates, baseAmount, selectedRates ->
        RatesContentState(
            baseAmount = baseAmount,
            currentRate = currentRate,
            rates = rates,
            selectedRates = selectedRates
        )
    }

    val state = combine(
        isLoading,
        error,
        contentState
    ) { isLoading, error, ratesState ->
        RatesScreenState(isLoading = isLoading, error = error, contentState = ratesState)
    }.flowOn(Dispatchers.Default)
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RatesScreenState(isLoading = false, error = "", contentState = RatesContentState())
        )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            fetchRates(Currency.EUR)
        }
    }

    fun fetchRates() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchRates(currentRate.value.currency)
        }
    }

    fun onAction(action: RateScreenAction) {
        when (action) {
            is RateScreenAction.BaseRateChanged -> {
                currentRate.value = action.rate
                viewModelScope.launch(Dispatchers.IO) {
                    fetchRates(action.rate.currency)
                }
            }

            is RateScreenAction.BaseAmountChanged -> {
                baseAmount.value = action.amount
            }

            is RateScreenAction.RateSelected -> {
                val selectedRates = state.value.contentState.selectedRates.toMutableList()
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

    private suspend fun fetchRates(currency: Currency) {
        isLoading.value = true
        try {
            val fxRates = getFxRatesUseCase(currency)
            fxRates.baseRate?.let {
                rates.value = fxRates.rates
                currentRate.value = fxRates.baseRate
                error.value = ""
            }
            selectedRates.value = emptyList()
        } catch (exception: FixerApiException) {
            error.value = exception.message ?: "Unknown error"
        }
        isLoading.value = false
    }
}