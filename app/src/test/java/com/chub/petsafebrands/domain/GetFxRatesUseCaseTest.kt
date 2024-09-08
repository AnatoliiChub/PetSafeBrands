package com.chub.petsafebrands.domain

import com.chub.petsafebrands.data.repo.FxRatesRepository
import com.chub.petsafebrands.data.response.DayRateResponse
import com.chub.petsafebrands.data.retrofit.Result
import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.CurrencyRateItem
import com.chub.petsafebrands.domain.pojo.FxRates
import com.chub.petsafebrands.domain.pojo.UiResult
import com.chub.petsafebrands.domain.usecases.GetFxRatesUseCase
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.math.BigDecimal

class GetFxRatesUseCaseTest {

    private lateinit var repository: FxRatesRepository
    private lateinit var getFxRatesUseCase: GetFxRatesUseCase
    private var annotationsClosable: AutoCloseable? = null

    @BeforeEach
    fun setUp() {
        annotationsClosable = MockitoAnnotations.openMocks(this)
        repository = Mockito.mock(FxRatesRepository::class.java)
        getFxRatesUseCase = GetFxRatesUseCase(repository)
    }

    @Test
    fun `test successful data retrieval`() = runBlocking {
        val baseCurrency = CurrencyRateItem(currency = Currency.USD, coefficient = BigDecimal.ONE)
        val currencies = listOf(Currency.EUR, Currency.GBP, Currency.JPY)
        val rates = mapOf(
            "EUR" to "0.85", "GBP" to "0.75", "JPY" to "110.0"
        )
        val fxRates = FxRates(
            baseRate = baseCurrency,
            rates = listOf(
                CurrencyRateItem(currency = Currency.EUR, coefficient = BigDecimal("0.85")),
                CurrencyRateItem(currency = Currency.GBP, coefficient = BigDecimal("0.75")),
                CurrencyRateItem(currency = Currency.JPY, coefficient = BigDecimal("110.0"))
            )
        )
        val exchangeRateResponse = DayRateResponse(
            success = true,
            base = baseCurrency.currency.name,
            date = "2021-01-01",
            rates = rates,
            error = null
        )

        Mockito.`when`(repository.getRates(baseCurrency.currency.name, currencies))
            .thenReturn(Result.Success(exchangeRateResponse))

        val result = getFxRatesUseCase(baseCurrency, currencies)

        assert(result is UiResult.Success)
        Assertions.assertEquals(fxRates, (result as UiResult.Success).data)
    }

    @Test
    fun `test failure scenario`() = runBlocking {
        val baseCurrency = CurrencyRateItem(currency = Currency.USD, coefficient = BigDecimal.ONE)
        val currencies = listOf(Currency.EUR, Currency.GBP, Currency.JPY)
        val errorMessage = "Error fetching rates"

        Mockito.`when`(repository.getRates(baseCurrency.currency.name, currencies))
            .thenReturn(Result.Failure(401, errorMessage))

        val result = getFxRatesUseCase(baseCurrency, currencies)

        assert(result is UiResult.Failure)
        Assertions.assertEquals(errorMessage, (result as UiResult.Failure).errorMessage)
    }

    @Test
    fun `test network error scenario`() = runBlocking {
        val baseCurrency = CurrencyRateItem(currency = Currency.USD, coefficient = BigDecimal.ONE)
        val currencies = listOf(Currency.EUR, Currency.GBP, Currency.JPY)

        Mockito.`when`(repository.getRates(baseCurrency.currency.name, currencies)).thenReturn(Result.NetworkError)

        val result = getFxRatesUseCase(baseCurrency, currencies)

        assert(result is UiResult.Failure)
        Assertions.assertTrue((result as UiResult.Failure).errorMessage.isNotEmpty())
    }

    @AfterEach
    fun tearDown() {
        annotationsClosable?.close()
    }
}