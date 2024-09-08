package com.chub.petsafebrands.ui.screen.rates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chub.petsafebrands.config.Config.MAX_SELECTED_RATES
import com.chub.petsafebrands.domain.GetFxRatesUseCase
import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.CurrencyRateItem
import com.chub.petsafebrands.domain.pojo.UiResult
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

    companion object {
        private const val INITIAL_BASE_AMOUNT = "100.0"
    }

    private val baseAmount = MutableStateFlow(INITIAL_BASE_AMOUNT)
    private val isLoading = MutableStateFlow(false)
    private val rates = MutableStateFlow(emptyList<CurrencyRateItem>())
    private val currentRate = MutableStateFlow(CurrencyRateItem(Currency.EUR))
    private val selectedRates = MutableStateFlow(emptyList<CurrencyRateItem>())
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
    ) { isLoading, error, contentState ->
        RatesScreenState(isLoading = isLoading, error = error, contentState = contentState)
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

    fun onAction(action: RateScreenAction) {
        when (action) {
            is RateScreenAction.BaseCurrencyChanged -> onBaseCurrencyChanged(action)
            is RateScreenAction.RateSelected -> onRateSelected(action)
            is RateScreenAction.FetchRates -> fetchRates()
            is RateScreenAction.BaseAmountChanged -> {
                baseAmount.value = action.amount
            }
        }
    }

    private fun fetchRates() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchRates(currentRate.value.currency)
        }
    }

    private suspend fun fetchRates(currency: Currency) {
        isLoading.value = true
        selectedRates.value = emptyList()
        when (val result = getFxRatesUseCase(currency)) {
            is UiResult.Success -> {
                result.data?.let {
                    currentRate.value = it.baseRate
                    rates.value = it.rates
                    error.value = ""
                }

            }

            is UiResult.Failure -> error.value = result.errorMessage
        }
        isLoading.value = false
    }

    private fun onRateSelected(action: RateScreenAction.RateSelected) {
        if (baseAmount.value.toDoubleOrNull() == null) {
            return
        }
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

    private fun onBaseCurrencyChanged(action: RateScreenAction.BaseCurrencyChanged) {
        currentRate.value = action.rate
        viewModelScope.launch(Dispatchers.IO) {
            fetchRates(action.rate.currency)
        }
    }
}