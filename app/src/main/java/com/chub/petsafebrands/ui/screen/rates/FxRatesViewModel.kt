package com.chub.petsafebrands.ui.screen.rates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chub.petsafebrands.config.Config.MAX_SELECTED_RATES
import com.chub.petsafebrands.config.Config.STOP_TIMEOUT_MILLIS
import com.chub.petsafebrands.di.qualifiers.WorkDispatcher
import com.chub.petsafebrands.domain.usecases.GetFxRatesUseCase
import com.chub.petsafebrands.domain.usecases.MultiplicationUseCase
import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.CurrencyRateItem
import com.chub.petsafebrands.domain.pojo.UiResult
import com.chub.petsafebrands.ui.screen.ErrorState
import com.chub.petsafebrands.ui.screen.rates.state.RatesContentState
import com.chub.petsafebrands.ui.screen.rates.state.RatesScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FxRatesViewModel @Inject constructor(
    @WorkDispatcher
    private val workDispatcher: CoroutineDispatcher,
    private val getFxRatesUseCase: GetFxRatesUseCase,
    private val multiplicationUseCase: MultiplicationUseCase
) : ViewModel() {

    companion object {
        private const val INITIAL_BASE_AMOUNT = "100.0"
    }

    private val currentRateItem = CurrencyRateItem(Currency.EUR)

    private val baseAmount = MutableStateFlow(INITIAL_BASE_AMOUNT)
    private val isLoading = MutableStateFlow(false)
    private val rates = MutableStateFlow(emptyList<CurrencyRateItem>())
    private val selectedRates = MutableStateFlow(emptyList<CurrencyRateItem>())
    private val error: MutableStateFlow<ErrorState> = MutableStateFlow(ErrorState.None)
    private val updatedRates = combine(rates, baseAmount) { rates, baseAmount ->
        rates.map { rate -> rate.copy(value = multiplicationUseCase(baseAmount, rate.coefficient)) }
    }

    private val contentState = combine(
        updatedRates,
        baseAmount,
        selectedRates
    ) { rates, baseAmount, selectedRates ->
        RatesContentState(
            baseAmount = baseAmount,
            currentRate = currentRateItem,
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
    }.flowOn(workDispatcher)
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = RatesScreenState(
                isLoading = false,
                error = ErrorState.None,
                contentState = RatesContentState()
            )
        )

    init {
        fetchRates()
    }

    fun onAction(action: FxRatesAction) {
        when (action) {
            is FxRatesAction.FxRatesSelected -> onRateSelected(action)
            is FxRatesAction.FetchRates -> fetchRates()
            is FxRatesAction.BaseAmountChanged -> {
                baseAmount.value = action.amount
            }
        }
    }

    private fun fetchRates() {
        viewModelScope.launch(workDispatcher) {
            isLoading.value = true
            selectedRates.value = emptyList()
            when (val result = getFxRatesUseCase(currentRateItem)) {
                is UiResult.Success -> {
                    result.data?.let {
                        rates.value = it.rates
                        error.value = ErrorState.None
                    }
                }

                is UiResult.Failure -> error.value = ErrorState.Error(result.errorMessage)
            }
            isLoading.value = false
        }
    }

    private fun onRateSelected(action: FxRatesAction.FxRatesSelected) {
        val selectedRates = state.value.contentState.selectedRates.toMutableList()
        if (selectedRates.any { it.currency == action.rate.currency }) {
            selectedRates.removeIf { it.currency == action.rate.currency }
        } else if (selectedRates.size >= MAX_SELECTED_RATES) {
            selectedRates.removeLast()
            selectedRates.add(action.rate)
        } else {
            selectedRates.add(action.rate)
        }
        this.selectedRates.value = selectedRates
    }
}