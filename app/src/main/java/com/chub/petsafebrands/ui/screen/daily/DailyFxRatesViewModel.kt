package com.chub.petsafebrands.ui.screen.daily

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.chub.petsafebrands.config.Config.STOP_TIMEOUT_MILLIS
import com.chub.petsafebrands.di.qualifiers.WorkDispatcher
import com.chub.petsafebrands.domain.usecases.DescendingSortUseCase
import com.chub.petsafebrands.domain.usecases.GetDailyRatesUseCase
import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.CurrencyRateItem
import com.chub.petsafebrands.domain.pojo.DayFxRate
import com.chub.petsafebrands.domain.pojo.SortBy
import com.chub.petsafebrands.domain.pojo.UiResult
import com.chub.petsafebrands.navigation.TimeSeriesScreenNav
import com.chub.petsafebrands.ui.screen.ErrorState
import com.chub.petsafebrands.ui.screen.daily.state.DailyRatesContentState
import com.chub.petsafebrands.ui.screen.daily.state.DailyRatesScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class DailyFxRatesViewModel @Inject constructor(
    @WorkDispatcher
    private val workDispatcher: CoroutineDispatcher,
    private val getDailyRatesUseCase: GetDailyRatesUseCase,
    private val descendingSortUseCase: DescendingSortUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val DAYS_NUMBER = 5
    }

    private val baseAmount = MutableStateFlow(BigDecimal.ZERO)
    private val baseCurrency = MutableStateFlow(Currency.EUR)
    private val dayRates = MutableStateFlow(emptyList<DayFxRate>())
    private val error: MutableStateFlow<ErrorState> = MutableStateFlow(ErrorState.None)
    private val isLoading = MutableStateFlow(false)
    private val currencies: List<Currency>
    private val sortBy: MutableStateFlow<SortBy> = MutableStateFlow(SortBy.ByDate)

    private val contentState = combine(baseAmount, baseCurrency, dayRates, sortBy) { amount, currency, rates, sortBy ->
        val sortedRates = descendingSortUseCase(rates, sortBy)
        DailyRatesContentState(amount, currency, sortedRates, sortBy)
    }

    val state = combine(contentState, error, isLoading) { contentState, error, isLoading ->
        DailyRatesScreenState(contentState, error, isLoading)
    }.flowOn(workDispatcher)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = DailyRatesScreenState(
                DailyRatesContentState(
                    baseAmount = BigDecimal.ZERO,
                    baseCurrency = Currency.EUR,
                    dayRates = emptyList(),
                    sortBy = SortBy.ByDate
                ), ErrorState.None, false
            )
        )


    init {
        val item = savedStateHandle.toRoute<TimeSeriesScreenNav>()
        baseAmount.value = BigDecimal(item.baseAmount)
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
        viewModelScope.launch(workDispatcher) {
            isLoading.value = true
            val base = CurrencyRateItem(currency = baseCurrency.value, value = baseAmount.value)
            when (val uiResult = getDailyRatesUseCase(base, currencies, DAYS_NUMBER)) {
                is UiResult.Success -> {
                    uiResult.data?.let { dayRates.value = it }
                    error.value = ErrorState.None
                }

                is UiResult.Failure -> error.value = ErrorState.Error(uiResult.errorMessage)
            }
            isLoading.value = false
        }
    }

    private fun onSortByChanged(sortBy: SortBy) {
        this.sortBy.value = sortBy
    }
}

