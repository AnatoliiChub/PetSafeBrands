package com.chub.petsafebrands.ui.screen.tiimeseries

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.chub.petsafebrands.data.RequestTimeSeries
import com.chub.petsafebrands.domain.GetDailyRatesUseCase
import com.chub.petsafebrands.domain.model.Currency
import com.chub.petsafebrands.domain.model.CurrencyRateItem
import com.chub.petsafebrands.domain.model.DayFxRate
import com.chub.petsafebrands.domain.model.UiResult
import com.chub.petsafebrands.navigation.TimeSeriesScreenNav
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
class DailyFxRatesViewModel @Inject constructor(
    private val getDailyRatesUseCase: GetDailyRatesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val baseAmount = MutableStateFlow(0f)
    private val baseCurrency = MutableStateFlow(Currency.EUR)
    private val dayRates = MutableStateFlow(emptyList<DayFxRate>())
    private val error = MutableStateFlow("")
    private val isLoading = MutableStateFlow(false)
    private val currencies: List<Currency>

    val state =
        combine(baseAmount, baseCurrency, dayRates, error, isLoading) { amount, currency, rates, error, isLoading ->
            DailySeriesScreenState(amount, currency, rates, error, isLoading)
        }.flowOn(Dispatchers.Default)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = DailySeriesScreenState(0f, Currency.EUR, emptyList())
            )

    init {
        val item = savedStateHandle.toRoute<TimeSeriesScreenNav>()
        baseAmount.value = item.baseAmount
        baseCurrency.value = Currency.entries[item.baseCurrency]
        currencies = item.currencies.map { Currency.entries[it] }
        fetchDailyRates()
    }

    fun fetchDailyRates() {
        viewModelScope.launch(Dispatchers.Default) {
            isLoading.value = true
            val uiResult = getDailyRatesUseCase(
                RequestTimeSeries(
                    CurrencyRateItem(baseCurrency.value, value = baseAmount.value.toDouble()),
                    currencies
                )
            )
            when (uiResult) {
                is UiResult.Success -> {
                    uiResult.data?.let { dayRates.value = it }
                    error.value = ""
                }

                is UiResult.Failure -> error.value = uiResult.errorMessage
            }
            isLoading.value = false
        }
    }
}