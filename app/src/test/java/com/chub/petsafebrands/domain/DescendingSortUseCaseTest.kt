package com.chub.petsafebrands.domain

import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.CurrencyRateItem
import com.chub.petsafebrands.domain.pojo.DayFxRate
import com.chub.petsafebrands.domain.pojo.SortBy
import com.chub.petsafebrands.domain.usecases.DescendingSortUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.stream.Stream


class DescendingSortUseCaseTest {

    private val descendingSortUseCase = DescendingSortUseCase()

    companion object {
        @JvmStatic
        fun provideDayFxRates(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    listOf(
                        DayFxRate(
                            LocalDate.of(2024, 9, 2).toDate(), listOf(
                                CurrencyRateItem(Currency.USD, value = BigDecimal("1.1")),
                                CurrencyRateItem(Currency.EUR, value = BigDecimal("0.9")),
                            )
                        ),
                        DayFxRate(
                            LocalDate.of(2024, 9, 3).toDate(), listOf(
                                CurrencyRateItem(Currency.USD, value = BigDecimal("1.2")),
                                CurrencyRateItem(Currency.EUR, value = BigDecimal("0.85")),
                            )
                        ),
                        DayFxRate(
                            LocalDate.of(2024, 9, 1).toDate(), listOf(
                                CurrencyRateItem(Currency.USD, value = BigDecimal("1.0")),
                                CurrencyRateItem(Currency.EUR, value = BigDecimal("0.95")),

                                )
                        ),
                        DayFxRate(
                            LocalDate.of(2024, 9, 4).toDate(), listOf(
                                CurrencyRateItem(Currency.USD, value = BigDecimal("1.15")),
                                CurrencyRateItem(Currency.EUR, value = BigDecimal("0.88")),
                            )
                        ),
                        DayFxRate(
                            LocalDate.of(2024, 9, 5).toDate(), listOf(
                                CurrencyRateItem(Currency.USD, value = BigDecimal("1.18")),
                                CurrencyRateItem(Currency.EUR, value = BigDecimal("0.87")),
                            )
                        ),
                        DayFxRate(
                            LocalDate.of(2024, 9, 6).toDate(), listOf(
                                CurrencyRateItem(Currency.USD, value = BigDecimal("1.13")),
                                CurrencyRateItem(Currency.EUR, value = BigDecimal("0.86")),
                            )
                        )
                    )
                )
            )
        }
    }

    @ParameterizedTest
    @MethodSource("provideDayFxRates")
    fun `test sorting by date`(list: List<DayFxRate>) {
        val sortedList = descendingSortUseCase(list, SortBy.ByDate)
        assertEquals(LocalDate.of(2024, 9, 6).toDate(), sortedList[0].date)
        assertEquals(LocalDate.of(2024, 9, 5).toDate(), sortedList[1].date)
        assertEquals(LocalDate.of(2024, 9, 4).toDate(), sortedList[2].date)
        assertEquals(LocalDate.of(2024, 9, 3).toDate(), sortedList[3].date)
        assertEquals(LocalDate.of(2024, 9, 2).toDate(), sortedList[4].date)
        assertEquals(LocalDate.of(2024, 9, 1).toDate(), sortedList[5].date)
    }

    @ParameterizedTest
    @MethodSource("provideDayFxRates")
    fun `test sorting by currency USD`(list: List<DayFxRate>) {
        val sortedList = descendingSortUseCase(list, SortBy.ByCurrency(Currency.USD))
        assertEquals(BigDecimal("1.2"), sortedList[0].rates.find { it.currency == Currency.USD }?.value)
        assertEquals(BigDecimal("1.18"), sortedList[1].rates.find { it.currency == Currency.USD }?.value)
        assertEquals(BigDecimal("1.15"), sortedList[2].rates.find { it.currency == Currency.USD }?.value)
        assertEquals(BigDecimal("1.13"), sortedList[3].rates.find { it.currency == Currency.USD }?.value)
        assertEquals(BigDecimal("1.1"), sortedList[4].rates.find { it.currency == Currency.USD }?.value)
        assertEquals(BigDecimal("1.0"), sortedList[5].rates.find { it.currency == Currency.USD }?.value)
    }

    @ParameterizedTest
    @MethodSource("provideDayFxRates")
    fun `test sorting by currency EUR`(list: List<DayFxRate>) {
        val sortedList = descendingSortUseCase(list, SortBy.ByCurrency(Currency.EUR))
        assertEquals(BigDecimal("0.95"), sortedList[0].rates.find { it.currency == Currency.EUR }?.value)
        assertEquals(BigDecimal("0.9"), sortedList[1].rates.find { it.currency == Currency.EUR }?.value)
        assertEquals(BigDecimal("0.88"), sortedList[2].rates.find { it.currency == Currency.EUR }?.value)
        assertEquals(BigDecimal("0.87"), sortedList[3].rates.find { it.currency == Currency.EUR }?.value)
        assertEquals(BigDecimal("0.86"), sortedList[4].rates.find { it.currency == Currency.EUR }?.value)
        assertEquals(BigDecimal("0.85"), sortedList[5].rates.find { it.currency == Currency.EUR }?.value)
    }
}
