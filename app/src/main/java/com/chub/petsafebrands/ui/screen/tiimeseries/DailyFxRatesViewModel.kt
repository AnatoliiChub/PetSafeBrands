package com.chub.petsafebrands.ui.screen.tiimeseries

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.chub.petsafebrands.data.RequestTimeSeries
import com.chub.petsafebrands.domain.DescendingSortUseCase
import com.chub.petsafebrands.domain.GetDailyRatesUseCase
import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.CurrencyRateItem
import com.chub.petsafebrands.domain.pojo.DayFxRate
import com.chub.petsafebrands.domain.pojo.UiResult
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
    private val descendingSortUseCase: DescendingSortUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val baseAmount = MutableStateFlow(0f)
    private val baseCurrency = MutableStateFlow(Currency.EUR)
    private val dayRates = MutableStateFlow(emptyList<DayFxRate>())
    private val error = MutableStateFlow("")
    private val isLoading = MutableStateFlow(false)
    private val currencies: List<Currency>
    private val sortBy: MutableStateFlow<SortBy> = MutableStateFlow(SortBy.ByDate)
    private val contentState = combine(baseAmount, baseCurrency, dayRates, sortBy) { amount, currency, rates, sortBy ->
        val sortedRates = descendingSortUseCase(rates, sortBy)
        DailyRatesContentState(amount, currency, sortedRates, sortBy)
    }

    val state = combine(contentState, error, isLoading) { contentState, error, isLoading ->
            DailyRatesScreenState(contentState, error, isLoading)
        }.flowOn(Dispatchers.Default)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = DailyRatesScreenState(
                    DailyRatesContentState(
                        baseAmount = 0f,
                        baseCurrency = Currency.EUR,
                        dayRates = emptyList(),
                        sortBy = SortBy.ByDate
                    ), "", false
                )
            )


    init {
        val item = savedStateHandle.toRoute<TimeSeriesScreenNav>()
        baseAmount.value = item.baseAmount
        baseCurrency.value = Currency.entries[item.baseCurrency]
        currencies = item.currencies.map { Currency.entries[it] }
        fetchDailyRates()
    }

    fun onAction(action: DailyFxRatesAction) {
        when (action) {
            is DailyFxRatesAction.SortByChanged -> onSortByChanged(action.sortBy)
            DailyFxRatesAction.FetchDailyRates -> fetchDailyRates()
        }
    }

    private fun fetchDailyRates() {
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

    private fun onSortByChanged(sortBy: SortBy) {
        this.sortBy.value = sortBy
    }
}

